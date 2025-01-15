package com.adoonge.seedzip.content.service;

import com.adoonge.seedzip.category.domain.Category;
import com.adoonge.seedzip.category.repository.CategoryRepository;
import com.adoonge.seedzip.content.domain.*;
import com.adoonge.seedzip.content.domain.mapping.CategoryContent;
import com.adoonge.seedzip.content.domain.mapping.ContentTag;
import com.adoonge.seedzip.content.dto.request.ContentsFilterRequest;
import com.adoonge.seedzip.content.dto.request.ContentsRequest;
import com.adoonge.seedzip.content.dto.request.ContentsUrlRequest;
import com.adoonge.seedzip.content.dto.request.S3DeleteRequest;
import com.adoonge.seedzip.content.dto.response.ContentsAllResponse;
import com.adoonge.seedzip.content.repository.*;
import com.adoonge.seedzip.filter.domain.Filter;
import com.adoonge.seedzip.filter.repository.FilterRepository;
import com.adoonge.seedzip.global.exception.ErrorCode;
import com.adoonge.seedzip.global.exception.SeedzipException;
import com.adoonge.seedzip.member.domain.Member;
import com.adoonge.seedzip.tag.domain.QTag;
import com.adoonge.seedzip.tag.domain.Tag;
import com.adoonge.seedzip.tag.domain.UsedDefaultTag;
import com.adoonge.seedzip.tag.domain.type.DefaultTagType;
import com.adoonge.seedzip.tag.repository.TagRepository;
import com.adoonge.seedzip.tag.repository.UsedDefaultTagRepository;

import com.querydsl.core.BooleanBuilder;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

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

	private final ContentsRepository contentsRepository;
	private final DocumentRepository documentRepository;
	private final TagRepository tagRepository;
	private final UsedDefaultTagRepository usedDefaultTagRepository;
	private final ContentTagRepository contentTagRepository;
	private final CategoryRepository categoryRepository;
	private final CategoryContentRepository categoryContentRepository;
	private final LinkRepository linkRepository;
	private final S3Service s3Service;
	private final ImageRepository imageRepository;
	private final ContentsRepositoryCustom contentsRepositoryCustom;
	private final FilterRepository filterRepository;

	@Transactional
	public ContentsAllResponse.contentResponse createContents(ContentsRequest.allContentsRequest request, Member member) {
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
		for (String tagName : request.getTags()) {
			Tag tag = findOrCreateTag(tagName, member);

			ContentTag contentTag = ContentTag.builder()
				.contents(contents)
				.tag(tag).build();

			contentTagRepository.save(contentTag);
		}

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

		if (request.getDataType().equals(ContentsDataType.LINK)) {
			// LINK
			Link link = Link.builder()
					.link(request.getContentLink())
					.contents(contents)
					.build();
			linkRepository.save(link);
		}

		return ContentsAllResponse.contentResponse.fromEntity("콘텐츠를 저장했습니다!", contents);
	}

	@Transactional
	public ContentsAllResponse.contentResponse uploadContents(Long contentsId, List<MultipartFile> files, Member member) {

		Contents contents = contentsRepository.findByContentsId(contentsId);
		List<String> fileUrls = new ArrayList<>();

		if (contents.getContentsDataType().equals(ContentsDataType.PDF)) {
			// PDF

			AtomicInteger index = new AtomicInteger(0); // 현재 인덱스를 추적하기 위한 변수
			int thumbnailIndex = contents.getThumbnailIdx();

			files.stream().forEach(file -> {
				try {
					// S3에 파일 업로드 및 URL 가져오기
					String fileUrl = s3Service.uploadDocFile(file);
					fileUrls.add(fileUrl);

					// URL 저장
					Document document = documentRepository.save(
							Document.builder()
							.docLink(fileUrl)
							.contents(contents)
							.docName(file.getOriginalFilename())
							.build());

					if (index.get() == thumbnailIndex) {
						document.setDocThumbnail(true);
					}

					documentRepository.save(document);
					index.getAndIncrement();

				} catch (IOException e) {
					e.printStackTrace();
				}
			});

		} else if (contents.getContentsDataType().equals(ContentsDataType.IMAGE)) {
			// IMAGE
			AtomicInteger index = new AtomicInteger(0); // 현재 인덱스를 추적하기 위한 변수
			int thumbnailIndex = contents.getThumbnailIdx();

			files.stream().forEach(file -> {
				try {
					// S3에 파일 업로드 및 URL 가져오기
					String fileUrl = s3Service.uploadImgFile(file);
					fileUrls.add(fileUrl);

					// Image 엔티티 생성
					Image image = Image.builder()
							.imgLink(fileUrl)
							.contents(contents)
							.imgName(file.getOriginalFilename())
							.build();

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
		return ContentsAllResponse.contentResponse.fromEntity("파일을 저장했습니다!", contents);
	}

	@Transactional
	public ContentsAllResponse.contentResponse synchronizeContents(Long contentsId, ContentsUrlRequest request, Member member) {

		Contents contents = contentsRepository.findByContentsId(contentsId);
		List<String> fileUrls = request.fileUrls();
		List<String> fileNames = request.fileNames();

		if (contents.getContentsDataType().equals(ContentsDataType.PDF)) {
			// PDF

			AtomicInteger index = new AtomicInteger(0); // 현재 인덱스를 추적하기 위한 변수
			int thumbnailIndex = contents.getThumbnailIdx();

			fileUrls.stream().forEach(fileUrl -> {

					// URL 저장
					Document document = documentRepository.save(
							Document.builder()
									.docLink(fileUrl)
									.contents(contents)
									.docName(fileNames.get(index.get()))
									.build());

					if (index.get() == thumbnailIndex) {
						document.setDocThumbnail(true);
					}

					documentRepository.save(document);
					index.getAndIncrement();

			});

		} else if (contents.getContentsDataType().equals(ContentsDataType.IMAGE)) {
			// IMAGE
			AtomicInteger index = new AtomicInteger(0); // 현재 인덱스를 추적하기 위한 변수
			int thumbnailIndex = contents.getThumbnailIdx();

			fileUrls.stream().forEach(fileUrl -> {

					// Image 엔티티 생성
					Image image = Image.builder()
							.imgLink(fileUrl)
							.contents(contents)
							.imgName(fileNames.get(index.get()))
							.build();

					// 인덱스가 thumbnailIndex와 일치하면 imgThumbnail을 true로 설정
					if (index.get() == thumbnailIndex) {
						image.setImgThumbnail(true);
					}

					imageRepository.save(image);
					index.getAndIncrement();

			});
		}
		return ContentsAllResponse.contentResponse.fromEntity("파일을 저장했습니다!", contents);
	}

	@Transactional
	public List<ContentsAllResponse.contentsInfo> getAllContents(Member member) {
		List<Contents> contentsList = contentsRepository.findByMemberId(member.getId());

		if (contentsList.isEmpty())
			return null;

		return generateResponseFromContentsList(contentsList);
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
		return generateResponseFromContentsList(contentsList);
	}

	@Transactional
	public ContentsAllResponse.getContents getContentsDetail(Long contentsId) {
		Contents contents = contentsRepository.findByContentsId(contentsId);    // 링크 조회

		String contentLink = null;
		List<String> contentImage = null;
		List<String> contentDoc = null;
		Long thumbnailImage = -1L;
		List<String> title = new ArrayList<>();

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
				title.add(image.getImgName());

				// 썸네일 이미지인 경우 ID 저장
				if (image.isImgThumbnail()) {
					thumbnailImage = (long)idx;
				}
				idx++;
			}
		}

		// PDF 경우
		if (ContentsDataType.PDF.equals(contents.getContentsDataType())) {
			contentDoc = new ArrayList<>();

			List<Document> documentList = documentRepository.findAllByContents_ContentsId(contents.getContentsId());

			int idx = 0;
			for (Document document : documentList) {
				// 이미지 URL 추가
				contentDoc.add(document.getDocLink());
				title.add(document.getDocName());

				if (document.isDocThumbnail()) {
					thumbnailImage = (long)idx;
				}
				idx++;
			}
		}

		// 카테고리
		List<Long> categoryIds = categoryContentRepository.findCategoryIdsByContentId(contentsId);
		List<String> categoryNames = categoryIds.stream()
			.map(categoryId -> categoryRepository.findById(categoryId)
				.map(Category::getName)
				.orElse(null))
			.filter(Objects::nonNull)
			.collect(Collectors.toList());

		// tagId에 해당하는 tagName 리스트 가져오기
		List<Long> tagIds = contentTagRepository.findTagIdsByContentId(contentsId);
		log.info(tagIds.toString());
		List<String> tagNames = tagIds.stream()
			.map(tagId -> tagRepository.findById(tagId)
				.map(Tag::getTagName)
				.orElse(null))
			.filter(Objects::nonNull)
			.collect(Collectors.toList());

		return new ContentsAllResponse.getContents(
			contents.getContentsId(),
			contents.getContentsDataType(),
				(contents.getContentsName() == null || contents.getContentsName().isEmpty()) ? String.valueOf(contents.getCreatedAt().toLocalDate()) : contents.getContentsName(),
			contentLink,
			contentImage,
			contentDoc,
			title,
			thumbnailImage,
			categoryNames,
			tagNames,
			contents.getDDay(),
			contents.getContentsDetail()

		);
	}

	// 콘텐츠 수정 후 업로드
	@Transactional
	public ContentsAllResponse.contentResponse uploadModifiedContents(Long contentsId, List<MultipartFile> files, Member member) {

		Contents contents = contentsRepository.findByContentsId(contentsId);
		List<String> fileUrls = new ArrayList<>();

		// 다른 DB도 접근하는 경우
		if (contents.getContentsDataType().equals(ContentsDataType.PDF)) {

			List<Document> existingDocuments = documentRepository.findAllByContents_ContentsId(contentsId);

			existingDocuments.forEach(document -> {
//				s3Service.deleteDocFile(document.getDocLink());
				documentRepository.delete(document);
			});

			AtomicInteger index = new AtomicInteger(0); // 현재 인덱스를 추적하기 위한 변수
			int thumbnailIndex = contents.getThumbnailIdx();

			files.stream().forEach(file -> {
				try {
					// S3에 파일 업로드 및 URL 가져오기
					String fileUrl = s3Service.uploadDocFile(file);
					fileUrls.add(fileUrl);

					// URL 저장
					Document document = documentRepository.save(
							Document.builder()
									.docLink(fileUrl)
									.contents(contents)
									.docName(file.getOriginalFilename())
									.build());

					if (index.get() == thumbnailIndex) {
						document.setDocThumbnail(true);
					}

					documentRepository.save(document);
					index.getAndIncrement();

				} catch (IOException e) {
					e.printStackTrace();
				}
			});
		} else if (contents.getContentsDataType().equals(ContentsDataType.IMAGE)) {
			List<Image> existingImages = imageRepository.findAllByContents_ContentsId(contentsId);
//
			existingImages.forEach(image -> {
//				s3Service.deleteImgFile(image.getImgLink());
				imageRepository.delete(image);
			});

			AtomicInteger index = new AtomicInteger(0); // 현재 인덱스를 추적하기 위한 변수
			int thumbnailIndex = contents.getThumbnailIdx();

			files.stream().forEach(file -> {
				try {
					// S3에 파일 업로드 및 URL 가져오기
					String fileUrl = s3Service.uploadImgFile(file);
					fileUrls.add(fileUrl);

					// URL 저장
					Image image = Image.builder()
							.imgLink(fileUrl)
							.contents(contents)
							.imgName(file.getOriginalFilename())
							.build();

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
		return ContentsAllResponse.contentResponse.fromEntity("파일을 수정했습니다!", contents);
	}

	// 콘텐츠 수정
	public ContentsAllResponse.contentResponse modifyContents(ContentsRequest.allContentsRequest request,
		Long contentsId, Member member) {
		Contents contents = contentsRepository.findById(contentsId)
			.orElseThrow(() -> new RuntimeException("Content not found"));

		if (!Objects.equals(contents.getContentsName(), request.getContentName())) {
			contents.setContentsName(request.getContentName());
		}

		if (!Objects.equals(contents.getDDay(), request.getDDay())) {
			contents.setDDay(request.getDDay());
		}
		if (!Objects.equals(contents.getContentsDetail(), request.getContentDetail())) {
			contents.setContentsDetail(request.getContentDetail());
		}

		contentsRepository.save(contents);

		// 기존 태그 조회
		List<ContentTag> existingContentTags = contentTagRepository.findAllByContents_ContentsId(
			contents.getContentsId());

		// contentTag 삭제 처리
		existingContentTags.forEach(contentTag -> contentTagRepository.delete(contentTag));

		// 태그 생성 및 사용
		Arrays.stream(request.getTags())
			.map(tagName -> {
				Tag tag = findOrCreateTag(tagName, member); // 태그 찾거나 생성
				return ContentTag.builder()
					.contents(contents)
					.tag(tag)
					.build();
			})
			.forEach(contentTagRepository::save); // ContentTag 저장

		// 카테고리
		if (Objects.isNull(request.getBoardCategory())) {
			Category category = categoryRepository.findByMemberIdAndName(member.getId(), "default");
			request.setBoardCategory(new String[] {"default"});
		}

		List<CategoryContent> existingCategoryContents = categoryContentRepository.findAllByContents_ContentsId(
			contents.getContentsId());
		List<String> requestedCategoryNames = Arrays.asList(request.getBoardCategory());

		// 기존 카테고리 이름 가져오기
		List<String> existingCategoryNames = existingCategoryContents.stream()
			.map(categoryContent -> categoryRepository.findById(categoryContent.getCategory().getCategoryId())
				.map(Category::getName) // Category에서 name 가져오기
				.orElse(null)) // Category가 없을 경우 null 처리
			.filter(Objects::nonNull) // null 값 필터링
			.collect(Collectors.toList());

		// 삭제할 카테고리: 요청에 없는 기존 카테고리
		List<CategoryContent> categoriesToDelete = existingCategoryContents.stream()
			.filter(categoryContent -> {
				String categoryName = categoryRepository.findById(categoryContent.getCategory().getCategoryId())
					.map(Category::getName)
					.orElse(null);
				return !requestedCategoryNames.contains(categoryName);
			})
			.collect(Collectors.toList());

		// 추가할 카테고리: 기존에 없는 새 요청 카테고리
		List<String> categoriesToAdd = requestedCategoryNames.stream()
			.filter(categoryName -> !existingCategoryNames.contains(categoryName))
			.collect(Collectors.toList());

		// 삭제 처리
		categoriesToDelete.forEach(categoryContent -> categoryContentRepository.delete(categoryContent));

		// 추가 처리
		categoriesToAdd.forEach(categoryName -> {
			if (categoryName == null || categoryName.trim().isEmpty()) {
				// 입력받은 카테고리가 없는 경우 default 카테고리 사용
				Category defaultCategory = categoryRepository.findByMemberIdAndName(member.getId(), "default");
				CategoryContent categoryContent = CategoryContent.builder()
					.contents(contents)
					.category(defaultCategory)
					.build();
				categoryContentRepository.save(categoryContent);
			} else {
				Category existingCategory = categoryRepository.findByMemberIdAndName(member.getId(), categoryName);
				if (existingCategory != null) {
					CategoryContent categoryContent = CategoryContent.builder()
						.contents(contents)
						.category(existingCategory)
						.build();
					categoryContentRepository.save(categoryContent);
				} else {
					// 카테고리가 존재하지 않으면 새로 생성
					Category newCategory = Category.builder()
						.name(categoryName)
						.member(member)
						.build();
					categoryRepository.save(newCategory);

					CategoryContent categoryContent = CategoryContent.builder()
						.contents(contents)
						.category(newCategory)
						.build();

					categoryContentRepository.save(categoryContent);
				}
			}
		});

		if (contents.getContentsDataType().equals(ContentsDataType.LINK)) {
			Optional<Link> existingLinkOptional = linkRepository.findByContents_ContentsId(contentsId);
			String requestLink = request.getContentLink();

			if (existingLinkOptional.isPresent()) {
				Link existingLink = existingLinkOptional.get();

				if (!existingLink.getLink().equals(request.getContentLink())) {
					linkRepository.delete(existingLink);
					Link newLink = Link.builder()
							.link(requestLink)
							.contents(contents)
							.build();
					linkRepository.save(newLink);
				}
			}
		}else{	// 이미지, PDF
			Contents byContentsId = contentsRepository.findByContentsId(contentsId);
			if(byContentsId.getThumbnailIdx() != request.getThumbnailImage()){
				byContentsId.setThumbnailIdx(request.getThumbnailImage());
				contentsRepository.save(byContentsId);
			}

			if(contents.getContentsDataType().equals(ContentsDataType.IMAGE)) {
				List<Image> existingImages = imageRepository.findAllByContents_ContentsId(contentsId);
				for (Image existingImage : existingImages) {
					existingImage.setImgThumbnail(false);
					imageRepository.save(existingImage);
				}
				existingImages.get(request.getThumbnailImage()).setImgThumbnail(true);
				imageRepository.save(existingImages.get(request.getThumbnailImage()));
			}else if(contents.getContentsDataType().equals(ContentsDataType.PDF)){
				List<Document> existingDocuments = documentRepository.findAllByContents_ContentsId(contentsId);
				for (Document existingDocument : existingDocuments) {
					existingDocument.setDocThumbnail(false);
					documentRepository.save(existingDocument);
				}
				existingDocuments.get(request.getThumbnailImage()).setDocThumbnail(true);
				documentRepository.save(existingDocuments.get(request.getThumbnailImage()));
			}

		}
		return ContentsAllResponse.contentResponse.fromEntity("콘텐츠를 수정했습니다!", contents);
	}

	// 콘텐츠 삭제
	@Transactional
	public void deleteContent(Long id, Member member) {
		Contents contents = contentsRepository.findById(id)
			.orElseThrow(() -> SeedzipException.from(ErrorCode.CONTENT_ACCESS_DENIED));

		//콘텐츠 소유자 검증
		if (!contents.getMember().getId().equals(member.getId())) {
			throw SeedzipException.from(ErrorCode.CATEGORY_ACCESS_DENIED);
		}

		//콘텐츠 삭제
		categoryContentRepository.deleteByContents(contents);
		contentTagRepository.deleteByContents(contents);

		if (ContentsDataType.LINK.equals(contents.getContentsDataType())) {
			linkRepository.deleteByContentsId(contents.getContentsId());
		}
		//IMAGE
		else if (ContentsDataType.IMAGE.equals(contents.getContentsDataType())) {
			// S3에서 삭제 코드 필요..
			List<Image> existingImages = imageRepository.findAllByContents_ContentsId(contents.getContentsId());
			imageRepository.deleteByContentsId(contents.getContentsId());

			existingImages.forEach(image -> {
				s3Service.deleteImgFile(image.getImgLink());
			});
		}
		//PDF
		else if (ContentsDataType.PDF.equals(contents.getContentsDataType())) {
			// S3에서 삭제 코드 필요..
			List<Document> existingDocuments = documentRepository.findAllByContents_ContentsId(
				contents.getContentsId());
			documentRepository.deleteByContentsId(contents.getContentsId());
			existingDocuments.forEach(document -> {
				s3Service.deleteDocFile(document.getDocLink());
			});
		}

		// 기존 태그 조회
		List<ContentTag> existingContentTags = contentTagRepository.findAllByContents_ContentsId(id);
		// contentTag 삭제 처리
		existingContentTags.forEach(contentTag -> contentTagRepository.delete(contentTag));

		contentsRepository.delete(contents);
	}

	//전체 콘텐츠 필터링 & 검색
	public List<ContentsAllResponse.contentsInfo> getFilteredContents(Member member, ContentsFilterRequest request) {

		BooleanBuilder builder = buildFilterConditions(request);

		List<Contents> result = contentsRepositoryCustom.findContentsByFilter(builder, member, request.tags());

		return generateResponseFromContentsList(result);
	}

	//카테고리 내 콘텐츠 필터링 & 검색
	public List<ContentsAllResponse.contentsInfo> getFilteredCategoryContents(Member member, Long categoryId, ContentsFilterRequest request) {

		BooleanBuilder builder = buildFilterConditions(request);

		List<Contents> result = contentsRepositoryCustom.findCategoryContentsByFilter(builder, member, categoryId, request.tags());

		return generateResponseFromContentsList(result);
	}

	private Tag findOrCreateTag(String tagName, Member member) {
		// 1. Default 태그 확인 (enum 클래스에서)
		if (Arrays.stream(DefaultTagType.values())
			.anyMatch(tag -> tag.getDisplayName().equals(tagName))) {

			Tag defaultTag = tagRepository.findByTagName(tagName)
				.orElseThrow(() -> SeedzipException.from(ErrorCode.TAG_NOT_FOUND));

			// 사용자별 UsedDefaultTag 조회, 저장
			usedDefaultTagRepository
				.findByMemberIdAndTagId(member.getId(), defaultTag.getId())    //사용한적 O
				.orElseGet(() ->    // 사용한적 없으면 저장
					usedDefaultTagRepository.save(UsedDefaultTag.builder()
						.member(member)
						.tag(defaultTag)
						.build()));

			return defaultTag;
		}

		// 2. 디폴트 태그가 아닌 경우 CustomTag로 저장
		return tagRepository.findByTagNameAndMemberId(tagName, member.getId())
			.orElseGet(() -> {
				return tagRepository.save(Tag.builder()
					.tagName(tagName)
					.member(member)
					.build());
			});
	}

	private List<ContentsAllResponse.contentsInfo> generateResponseFromContentsList(List<Contents> contentsList) {
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
					} else if (ContentsDataType.PDF.equals(content.getContentsDataType())) {
						Optional<Document> thumbnailDoc = documentRepository.findByContentsIdAndDocThumbnail(
								content.getContentsId(), true);
						if (thumbnailDoc.isPresent()) {
							thumbnailUrl = thumbnailDoc.get().getDocLink();
						}
					}
					else{
						Optional<Link> link = linkRepository.findByContents_ContentsId(content.getContentsId());
						if (link.isPresent()) {
							thumbnailUrl = link.get().getLink();
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
									.orElseThrow(() -> SeedzipException.from(ErrorCode.TAG_NOT_FOUND)))
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

	private BooleanBuilder buildFilterConditions(ContentsFilterRequest request) {
		QContents contents = QContents.contents;
		QTag tag = QTag.tag;

		BooleanBuilder builder = new BooleanBuilder();

		// 저장 형식 필터
		if (request.dataType() != null) {
			builder.and(contents.contentsDataType.eq(request.dataType()));
		}

		// 태그 필터
		if (request.tags() != null && !request.tags().isEmpty()) {
			builder.and(tag.tagName.in(request.tags())); // 태그 이름 필터링
		}

		// 키워드 검색
		if (request.keyword() != null && !request.keyword().trim().isEmpty()) {
			builder.and(contents.contentsName.containsIgnoreCase(request.keyword())
					.or(contents.contentsDetail.containsIgnoreCase(request.keyword())));
		}

		return builder;
	}

	// 커스텀 필터
	public List<ContentsAllResponse.contentsInfo> getCustomFilterContents(Member member, Long filterId) {
		Filter filter = filterRepository.findById(filterId)
			.orElseThrow(() -> SeedzipException.from(ErrorCode.FILTER_NOT_FOUND));

		// 필터 소유자 검증
		if (!filter.getMember().getId().equals(member.getId())) {
			throw SeedzipException.from(ErrorCode.FILTER_ACCESS_DENIED);
		}

		List<Contents> result = contentsRepositoryCustom.findContentsByCustomFilter(
			filter.getStartDate(),
			filter.getEndDate(),
			filter.getStorageFormats(),
			filter.getFromDDay(),
			filter.getToDDay(),
			filterId,
			member.getId()
		);

		return generateResponseFromContentsList(result);
	}

	//s3 이미지 삭제
	public void deleteS3Image(S3DeleteRequest request) {
		List<String> fileUrls = request.fileUrls();
		fileUrls.forEach(url -> {
			s3Service.deleteImgFile(url);
		});
	}

	//s3 문서 삭제
	public void deleteS3Document(S3DeleteRequest request) {
		List<String> fileUrls = request.fileUrls();
		fileUrls.forEach(url -> {
			s3Service.deleteDocFile(url);
		});
	}

}
