package com.adoonge.seedzip.content.service;

import com.adoonge.seedzip.category.domain.Category;
import com.adoonge.seedzip.category.repository.CategoryRepository;
import com.adoonge.seedzip.content.domain.*;
import com.adoonge.seedzip.content.domain.mapping.CategoryContent;
import com.adoonge.seedzip.content.domain.mapping.ContentTag;
import com.adoonge.seedzip.content.dto.request.ContentsRequest;
import com.adoonge.seedzip.content.dto.response.ContentsAllResponse;
import com.adoonge.seedzip.content.repository.*;
import com.adoonge.seedzip.global.exception.ErrorCode;
import com.adoonge.seedzip.global.exception.SeedzipException;
import com.adoonge.seedzip.member.domain.Member;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Slf4j
public class ContentsService {
	@Autowired
	private final ContentsRepository contentsRepository;

	@Autowired
	private final DocumentRepository documentRepository;

	@Autowired
	private final TagRepository tagRepository;

	@Autowired
	private final ContentTagRepository contentTagRepository;

	@Autowired
	private final CategoryRepository categoryRepository;

	@Autowired
	private final CategoryContentRepository categoryContentRepository;

	@Autowired
	private final LinkRepository linkRepository;

	@Autowired
	private final S3Service s3Service;
	@Autowired
	private final ImageRepository imageRepository;

	@Transactional
	public ContentsAllResponse.contentResponse createContents(ContentsRequest.allContentsRequest request,
		List<MultipartFile> files, Member member) {
		if (Objects.isNull(request.getContentName())) {
			request.setContentName(null);
		}
		if (Objects.isNull(request.getDDay())) {
			request.setDDay(null);
		}
		if (Objects.isNull(request.getContentDetail())) {
			request.setContentDetail(null);
		}
		Contents contents = contentsRepository.save(request.toContentEntity(member));

		// 태그 저장
		Arrays.stream(request.getTags())
			.map(tagName -> tagRepository.findByTagName(tagName)
				.orElseGet(() -> tagRepository.save(
					Tag.builder()
						.tagName(tagName)
						.build())))
			.forEach(tag -> {
				ContentTag contentTag = new ContentTag();
				contentTag.setContents(contents); // Content 엔티티는 이미 존재한다고 가정
				contentTag.setTag(tag);
				contentTagRepository.save(contentTag);
			});

		if (Objects.isNull(request.getBoardCategory())) {
			Category category = categoryRepository.findByMemberIdAndName(member.getId(), "default");
			request.setBoardCategory(new String[] {"default"});
		}

		// 카테고리 저장
		Arrays.stream(request.getBoardCategory())  // String[]을 스트림으로 변환
			.map(categoryName -> categoryRepository.findByMemberIdAndName(member.getId(), categoryName))
			.forEach(category -> {
				CategoryContent categoryContent = new CategoryContent();
				categoryContent.setContents(contents); // Content 엔티티는 이미 존재한다고 가정
				categoryContent.setCategory(category);
				categoryContentRepository.save(categoryContent);
			});

		List<String> fileUrls = new ArrayList<>();

		if (request.getDataType().equals(ContentsDataType.PDF)) {
			// PDF
			files.stream().forEach(file -> {
				try {
					// S3에 파일 업로드 및 URL 가져오기
					String fileUrl = s3Service.uploadDocFile(file);
					fileUrls.add(fileUrl);

					// URL 저장
					Document document = documentRepository.save(request.toDocEntity(contents, fileUrl));
				} catch (IOException e) {
					e.printStackTrace();
				}
			});

		} else if (request.getDataType().equals(ContentsDataType.LINK)) {
			// LINK
			Link link = Link.builder()
				.link(request.getContentLink())
				.contents(contents)
				.build();
			linkRepository.save(link);
		} else if (request.getDataType().equals(ContentsDataType.IMAGE)) {
			// IMAGE
			AtomicInteger index = new AtomicInteger(0); // 현재 인덱스를 추적하기 위한 변수
			int thumbnailIndex = request.getThumbnailImage();

			files.stream().forEach(file -> {
				try {
					// S3에 파일 업로드 및 URL 가져오기
					String fileUrl = s3Service.uploadImgFile(file);
					fileUrls.add(fileUrl);

					// Image 엔티티 생성
					Image image = request.toImgEntity(contents, fileUrl);

					// 인덱스가 thumbnailIndex와 일치하면 imgThumbnail을 true로 설정
					if (index.get() == thumbnailIndex) {
						image.setImgThumbnail(true);
					}

					imageRepository.save(image);
					index.getAndIncrement();

				} catch (IOException e) {
					e.printStackTrace();
				}
			});
		}
		return ContentsAllResponse.contentResponse.fromEntity("콘텐츠를 저장했습니다!", contents);
	}

	@Transactional
	public List<ContentsAllResponse.contentsInfo> getAllContents(Member member) {
		List<Contents> contentsList = contentsRepository.findByMemberId(member.getId());

		if (contentsList.isEmpty())
			return null;
		return contentsList.stream()
			.map(content -> {
				// contentDateType이 IMAGE인 경우에만 썸네일 이미지 URL 전송, 아닌 경우 null
				String thumbnailUrl = null;
				if (ContentsDataType.IMAGE.equals(content.getContentsDataType())) {
					Optional<Image> thumbnailImage = imageRepository.findByContentsIdAndImgThumbnail(
						content.getContentsId(), true);
					if (thumbnailImage.isPresent()) {
						thumbnailUrl = thumbnailImage.get().getImgLink();
					}
				}
				// contentId에 해당하는 카테고리 리스트 조회
				List<Long> categoryIds = categoryContentRepository.findCategoryIdsByContentId(content.getContentsId());
				List<String> categoryNames = categoryIds.stream()
					.map(categoryId -> categoryRepository.findById(categoryId)
						.map(Category::getName)
						.orElse(null))
					.filter(Objects::nonNull)
					.collect(Collectors.toList());

				// contentId에 해당하는 태그 리스트 조회
				List<Long> tagIds = contentTagRepository.findTagIdsByContentId(content.getContentsId());
				List<String> tagNames = tagIds.stream()
					.map(tagId -> tagRepository.findById(tagId)
						.map(Tag::getTagName)
						.orElse(null))
					.filter(Objects::nonNull)
					.collect(Collectors.toList());

				// D-day 계산
				int dDayValue = 1;
				if (content.getDDay() != null) {
					LocalDate today = LocalDate.now();
					long daysBetween = ChronoUnit.DAYS.between(today, content.getDDay());

					if (daysBetween > 0) {
						dDayValue = -(int)daysBetween;
					} else if (daysBetween == 0) {
						dDayValue = 0;
					}
				}

				// ContentResponse 객체에 필요한 정보 담기
				return new ContentsAllResponse.contentsInfo(
					content.getContentsId(),
					content.getContentsName(),
					categoryIds,
					categoryNames,
					content.getContentsDataType(),
					thumbnailUrl, // IMAGE 아니면 null
					content.getUpdatedAt(),
					tagIds,
					tagNames,
					dDayValue
				);
			})
			.collect(Collectors.toList());
	}

	@Transactional
	public List<ContentsAllResponse.contentsInfo> getCategoryContents(Long categoryId) {
		List<Long> contentIds = categoryContentRepository.findContentIdsByCategoryId(categoryId);
		if (Objects.isNull(contentIds))
			return null;
		List<Contents> contentsList = contentsRepository.findByContentsIdIn(contentIds);
		if (contentsList.isEmpty())
			return null;
		// 있다면
		return contentsList.stream()
			.map(content -> {
				// contentDateType이 IMAGE인 경우에만 썸네일 이미지 URL 전송, 아닌 경우 null
				String thumbnailUrl = null;
				if (ContentsDataType.IMAGE.equals(content.getContentsDataType())) {
					Optional<Image> thumbnailImage = imageRepository.findByContentsIdAndImgThumbnail(
						content.getContentsId(), true);
					if (thumbnailImage.isPresent()) {
						thumbnailUrl = thumbnailImage.get().getImgLink();
					}
				}
				// contentId에 해당하는 카테고리 리스트 조회
				List<Long> categoryIds = categoryContentRepository.findCategoryIdsByContentId(content.getContentsId());
				List<String> categoryNames = categoryIds.stream()
					.map(categoryI -> categoryRepository.findById(categoryId)
						.map(Category::getName)
						.orElse(null))
					.filter(Objects::nonNull)
					.collect(Collectors.toList());

				// contentId에 해당하는 태그 리스트 조회
				List<Long> tagIds = contentTagRepository.findTagIdsByContentId(content.getContentsId());
				List<String> tagNames = tagIds.stream()
					.map(tagId -> tagRepository.findById(tagId)
						.map(Tag::getTagName)
						.orElse(null))
					.filter(Objects::nonNull)
					.collect(Collectors.toList());

				// D-day 계산
				int dDayValue = 1;
				if (content.getDDay() != null) {
					LocalDate today = LocalDate.now();
					long daysBetween = ChronoUnit.DAYS.between(today, content.getDDay());

					if (daysBetween > 0) {
						dDayValue = -(int)daysBetween;
					} else if (daysBetween == 0) {
						dDayValue = 0;
					}
				}

				// ContentResponse 객체에 필요한 정보 담기
				return new ContentsAllResponse.contentsInfo(
					content.getContentsId(),
					content.getContentsName(),
					categoryIds,
					categoryNames,
					content.getContentsDataType(),
					thumbnailUrl, // IMAGE 아니면 null
					content.getUpdatedAt(),
					tagIds,
					tagNames,
					dDayValue
				);
			})
			.collect(Collectors.toList());
	}

	@Transactional
	public ContentsAllResponse.getContents getContentsDetail(Long contentsId) {
		Contents contents = contentsRepository.findById(contentsId)
			.orElseThrow(() -> new RuntimeException("Content not found"));        // 링크 조회

		String contentLink = null;
		List<String> contentImage = null;
		List<String> contentDoc = null;
		Long thumbnailImage = -1L;

		// contentDataType이 LINK인 경우
		if (ContentsDataType.LINK.equals(contents.getContentsDataType())) {
			Optional<Link> linkEntity = linkRepository.findByContents_ContentsId(contents.getContentsId());
			contentLink = linkEntity.map(Link::getLink).orElse(null);
		}

		// IMAGE 경우
		if (ContentsDataType.IMAGE.equals(contents.getContentsDataType())) {
			contentImage = new ArrayList<>();

			List<Image> imageList = imageRepository.findAllByContents_ContentsId(contents.getContentsId());

			int idx = 0;
			for (Image image : imageList) {
				// 이미지 URL 추가
				contentImage.add(image.getImgLink());

				// 썸네일 이미지인 경우 ID 저장
				if (image.isImgThumbnail()) {
					thumbnailImage = (long) idx;
				}

				idx++;
			}
		}

		// PDF 경우
		if (ContentsDataType.PDF.equals(contents.getContentsDataType())) {
			contentDoc = new ArrayList<>();

			List<Document> documentList = documentRepository.findAllByContents_ContentsId(contents.getContentsId());

			for (Document document : documentList) {
				// 이미지 URL 추가
				contentDoc.add(document.getDocLink());
			}
		}

		// 카테고리
		List<Long> categoryIds = categoryContentRepository.findCategoryIdsByContentId(contentsId);
		System.out.println(categoryIds);
		List<String> categoryNames = categoryIds.stream()
			.map(categoryId -> categoryRepository.findById(categoryId)
				.map(Category::getName)
				.orElse(null))
			.filter(Objects::nonNull)
			.collect(Collectors.toList());

		// tagId에 해당하는 tagName 리스트 가져오기
		List<Long> tagIds = contentTagRepository.findTagIdsByContentId(contentsId);
		List<String> tagNames = tagIds.stream()
			.map(tagId -> tagRepository.findById(tagId)
				.map(Tag::getTagName)
				.orElse(null))
			.filter(Objects::nonNull)
			.collect(Collectors.toList());

		return new ContentsAllResponse.getContents(
			contents.getContentsId(),
			contents.getContentsDataType(),
			contents.getContentsName(),
			contentLink,
			contentImage,
			contentDoc,
			thumbnailImage,
			categoryNames,
			tagNames,
			contents.getDDay(),
			contents.getContentsDetail()

		);
	}

	// 콘텐츠 삭제
	@Transactional
	public void deleteContent(Long id, Member member){
		Contents contents = contentsRepository.findById(id)
			.orElseThrow(() -> SeedzipException.from(ErrorCode.CONTENT_ACCESS_DENIED));

		//콘텐츠 소유자 검증
		if (!contents.getMember().getId().equals(member.getId())) {
			throw SeedzipException.from(ErrorCode.CATEGORY_ACCESS_DENIED);
		}

		//콘텐츠 삭제
		contentsRepository.delete(contents);

		//IMAGE
		if (ContentsDataType.IMAGE.equals(contents.getContentsDataType())) {
			// S3에서 삭제 코드 필요..
		}
		//PDF
		if (ContentsDataType.PDF.equals(contents.getContentsDataType())) {
			// S3에서 삭제 코드 필요..
		}

	}
}
