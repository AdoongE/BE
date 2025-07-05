package com.adoonge.seedzip.seed.service;

import com.adoonge.seedzip.bookmark.repository.SeedBookmarkRepository;
import com.adoonge.seedzip.category.domain.Category;
import com.adoonge.seedzip.category.repository.CategoryRepository;
import com.adoonge.seedzip.filter.domain.Filter;
import com.adoonge.seedzip.filter.repository.FilterRepository;
import com.adoonge.seedzip.global.exception.ErrorCode;
import com.adoonge.seedzip.global.exception.SeedzipException;
import com.adoonge.seedzip.member.domain.Member;
import com.adoonge.seedzip.seed.domain.File;
import com.adoonge.seedzip.seed.domain.Seed;
import com.adoonge.seedzip.seed.domain.SeedType;
import com.adoonge.seedzip.seed.domain.mapping.CategorySeed;
import com.adoonge.seedzip.seed.domain.mapping.SeedTag;
import com.adoonge.seedzip.seed.dto.SeedDTO;
import com.adoonge.seedzip.seed.dto.projection.CategorySeedProjection;
import com.adoonge.seedzip.seed.dto.projection.FileSeedProjection;
import com.adoonge.seedzip.seed.dto.projection.SeedProjectionResult;
import com.adoonge.seedzip.seed.dto.projection.SeedTagProjection;
import com.adoonge.seedzip.seed.dto.request.SeedDeleteListRequest;
import com.adoonge.seedzip.seed.dto.request.SeedFilteringRequest;
import com.adoonge.seedzip.seed.dto.request.SeedRequest;
import com.adoonge.seedzip.seed.dto.request.SeedUpdateRequest;
import com.adoonge.seedzip.seed.dto.response.SeedResponse;
import com.adoonge.seedzip.seed.repository.CategorySeedRepository;
import com.adoonge.seedzip.seed.repository.FileRepository;
import com.adoonge.seedzip.seed.repository.SeedRepository;
import com.adoonge.seedzip.seed.repository.SeedRepositoryCustom;
import com.adoonge.seedzip.seed.repository.SeedTagRepository;
import com.adoonge.seedzip.tag.domain.Tag;
import com.adoonge.seedzip.tag.domain.UsedDefaultTag;
import com.adoonge.seedzip.tag.domain.type.DefaultTagType;
import com.adoonge.seedzip.tag.repository.TagRepository;
import com.adoonge.seedzip.tag.repository.UsedDefaultTagRepository;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
@Slf4j
public class SeedService {

	private final FileService fileService;
	private final SeedCacheService seedCacheService;

	private final SeedRepository seedRepository;
	private final FileRepository fileRepository;
	private final CategorySeedRepository categorySeedRepository;
	private final SeedTagRepository seedTagRepository;
	private final TagRepository tagRepository;
	private final UsedDefaultTagRepository usedDefaultTagRepository;
	private final CategoryRepository categoryRepository;
	private final SeedRepositoryCustom seedRepositoryCustom;
	private final FilterRepository filterRepository;
	private final SeedBookmarkRepository seedBookmarkRepository;

	private static final Set<String> DEFAULT_TAG_NAMES = Arrays.stream(DefaultTagType.values())
		.map(DefaultTagType::getDisplayName)
		.collect(Collectors.toSet());

	private static final Long DEFAULT_VIEW_COUNT = 3L;
	private static final Long UNREAD_VIEW_COUNT = 0L;

	@Transactional(readOnly = true)
	public SeedResponse.GetAllSeeds getAllSeeds(Member member, int page, int size, String sortBy, boolean isAsc,
		String seedType) {

		Sort.Direction direction = getSortDirection(isAsc);
		String sortField = getSortField(sortBy);
		SeedType parsedSeedType = parseSeedType(seedType);

		//페이징을 위한 Pageable 객체
		Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortField));

		//페이징으로 얻어온 Seed 리스트
		Page<Seed> seedList;
		if (seedType != null) {
			seedList = seedRepository.findByMemberAndSeedType(member, parsedSeedType, pageable);
		} else {
			seedList = seedRepository.findByMember(member, pageable);
		}

		if(seedList.isEmpty()){
			throw SeedzipException.from(ErrorCode.EMPTY_SEED);
		}
		return buildGetAllSeedsResponse(member, seedList);
	}

	@Transactional(readOnly = true)
	public SeedResponse.GetAllSeeds getCategorySeeds(Member member, int page, int size, String sortBy, boolean isAsc,
		String seedType, long categoryId) {

		Sort.Direction direction = getSortDirection(isAsc);
		String sortField = getSortField(sortBy);
		SeedType parsedSeedType = parseSeedType(seedType);

		//페이징을 위한 Pageable 객체
		Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortField));

		List<Long> seedIds = categorySeedRepository.findSeedIdsByCategoryId(categoryId);

		//페이징으로 얻어온 Seed 리스트
		Page<Seed> seedList;
		if (seedType != null) {
			seedList = seedRepository.findBySeedIdInAndSeedType(seedIds, parsedSeedType, pageable);
		} else {
			seedList = seedRepository.findBySeedIdIn(seedIds, pageable);
		}

		if (seedList.isEmpty()){
			throw SeedzipException.from(ErrorCode.EMPTY_SEED);
		}
		return buildGetAllSeedsResponse(member, seedList);
	}

	@Transactional
	public SeedResponse.SeedInfoSimple uploadSeed(SeedRequest seedRequest, Member member) {
		if(seedRequest.seedType() == SeedType.LINK && seedRequest.seedLink() == null) {
			throw SeedzipException.from(ErrorCode.EMPTY_LINK);
		}

		Seed seed = saveSeed(seedRequest, member);

		// 태그 및 카테고리 저장
		saveSeedTags(seedRequest.tags(), member, seed);
		if(seedRequest.boardCategories() == null) {
			saveSeedDefaultCategory(member, seed);
		} else {
			saveSeedCategories(seedRequest.boardCategories(), member, seed);
		}

		if (seedRequest.seedType() == SeedType.LINK) {
			fileService.saveLink(seedRequest.seedLink(), seed);
		}

		return SeedResponse.SeedInfoSimple
			.builder()
			.seedId(seed.getId())
			.seedName(seed.getSeedName())
			.build();
	}

	@Transactional
	public void uploadFiles(Long seedId, List<MultipartFile> files) {
		Seed seed = seedRepository.findById(seedId).orElseThrow(
			() -> SeedzipException.from(ErrorCode.SEED_NOT_FOUND)
		);

		fileService.saveFiles(files, seed);
	}

	@Transactional(readOnly = true)
	public SeedResponse.SeedDetail getSeedDetail(Long seedId) {
		Seed seed = getSeedOrThrow(seedId);
		seedCacheService.increaseViewCounts(seedId);
		List<File> files = fileRepository.findAllBySeed(seed)
				.orElseThrow(() -> SeedzipException.from(ErrorCode.FILE_NOT_FOUND));

		String seedLink = null;
		List<String> fileLinks = null;
		Long thumbnailImage = -1L;
		List<String> titles = null;

		//링크와 파일들 링크 얻고, 썸네일 처리
        if (seed.getSeedType().equals(SeedType.LINK)) {
            seedLink = files.get(0).getLink();
        } else {
			fileLinks = new ArrayList<>();
			titles = new ArrayList<>();
			AtomicInteger idx = new AtomicInteger(0);

			for(File file : files){
				fileLinks.add(file.getLink());
				titles.add(file.getFileName());
				if(Boolean.TRUE.equals(file.getIsThumbnail())) {
					thumbnailImage = (long) idx.get();
				}
				idx.getAndIncrement();
			}
        }

		//카테고리
		List<String> categoryNames = categorySeedRepository.findCategoryNamesBySeed(seed);
		//태그
		List<String> tagNames = seedTagRepository.findTagNamesBySeed(seed);

		return SeedResponse.SeedDetail.builder()
				.seedId(seed.getId())
				.seedType(seed.getSeedType())
				.seedName(seed.getSeedName())
				.seedLink(seedLink)
				.fileLinks(fileLinks)
				.titles(titles)
				.thumbnailImage(thumbnailImage)
				.categoryNames(categoryNames)
				.tagNames(tagNames)
				.dDay(seed.getDDay())
				.seedDetail(seed.getSeedDetail())
				.build();
	}

	@Transactional(readOnly = true)
	public SeedResponse.GetFilteredSeeds getFilteredSeeds(Member member, int page, int size, String sortBy, boolean isAsc,
														  String seedType, SeedFilteringRequest request) {
		Sort.Direction direction = getSortDirection(isAsc);
		String sortField = getSortField(sortBy);
		SeedType parsedSeedType = parseSeedType(seedType);

		//페이징을 위한 Pageable 객체
		Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortField));

		//페이징으로 얻어온 Seed 리스트
		Page<Seed> seedList;
		if (seedType != null) {
			seedList = seedRepositoryCustom.findSeedsByFiltering(member, pageable, parsedSeedType, request);
		} else {
			seedList = seedRepositoryCustom.findSeedsByFiltering(member, pageable, null, request);
		}

		if (seedList.isEmpty()){
			throw SeedzipException.from(ErrorCode.EMPTY_SEED);
		}
		return buildGetFilteredSeedsResponse(member, seedList);
	}

	@Transactional(readOnly = true)
	public SeedResponse.GetFilteredSeeds getFilteredCategorySeeds(Member member, int page, int size, String sortBy, boolean isAsc,
														  String seedType, Long categoryId, SeedFilteringRequest request) {
		Sort.Direction direction = getSortDirection(isAsc);
		String sortField = getSortField(sortBy);
		SeedType parsedSeedType = parseSeedType(seedType);

		//페이징을 위한 Pageable 객체
		Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortField));

		//페이징으로 얻어온 Seed 리스트
		Page<Seed> seedList;
		if (seedType != null) {
			seedList = seedRepositoryCustom.findCategorySeedsByFiltering(member, pageable, parsedSeedType, categoryId, request);
		} else {
			seedList = seedRepositoryCustom.findCategorySeedsByFiltering(member, pageable, null, categoryId, request);
		}

		if (seedList.isEmpty()){
			throw SeedzipException.from(ErrorCode.EMPTY_SEED);
		}
		return buildGetFilteredSeedsResponse(member, seedList);
	}

	@Transactional(readOnly = true)
	public SeedResponse.GetFilteredSeeds getFilteredBookmarkSeeds(Member member, int page, int size, String sortBy, boolean isAsc,
																  String seedType, String keyword) {
		Sort.Direction direction = getSortDirection(isAsc);
		String sortField = getSortField(sortBy);
		SeedType parsedSeedType = parseSeedType(seedType);

		//페이징을 위한 Pageable 객체
		Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortField));

		//페이징으로 얻어온 Seed 리스트
		Page<Seed> seedList;
		if (seedType != null) {
			seedList = seedRepositoryCustom.findBookmarkSeedsByFiltering(member, pageable, parsedSeedType, keyword);
		} else {
			seedList = seedRepositoryCustom.findBookmarkSeedsByFiltering(member, pageable, null, keyword);
		}

		if (seedList.isEmpty()){
			throw SeedzipException.from(ErrorCode.EMPTY_SEED);
		}
		return buildGetFilteredSeedsResponse(member, seedList);
	}

	@Transactional(readOnly = true)
	public SeedResponse.GetFilteredSeeds getCustomFilterSeeds(Member member, int page, int size, Long filterId) {
		Filter filter = filterRepository.findById(filterId)
				.orElseThrow(() -> SeedzipException.from(ErrorCode.FILTER_NOT_FOUND));

		if (!filter.getMember().getId().equals(member.getId())) {
			throw SeedzipException.from(ErrorCode.FILTER_ACCESS_DENIED);
		}

		//페이징을 위한 Pageable 객체
		Pageable pageable = PageRequest.of(page, size);
		Page<Seed> seedPage = seedRepositoryCustom.findSeedsByCustomFilter(pageable, filter.getStartDate(), filter.getEndDate(),
				filter.getStorageFormats(), filter.getFromDDay(), filter.getToDDay(), filter.getFilterId(), member.getId());
		List<Seed> seedList = seedPage.getContent();

		if (seedList.isEmpty()){
			throw SeedzipException.from(ErrorCode.EMPTY_SEED);
		}
		return buildGetFilteredSeedsResponse(member, seedPage);
	}

	@Transactional
	public void deleteSeed(Long id, Member member) {
		Seed seed = seedRepository.findById(id)
			.orElseThrow(() -> SeedzipException.from(ErrorCode.CONTENT_ACCESS_DENIED));

		//콘텐츠 소유자 검증
		if (!seed.getMember().getId().equals(member.getId())) {
			throw SeedzipException.from(ErrorCode.MEMBER_NOT_OWNER);
		}

		//카테고리 매핑 삭제
		categorySeedRepository.deleteAllBySeedId(id);

		//S3에서 파일 삭제
		deleteFileFromS3(id, seed);

		//파일 삭제
		fileRepository.deleteAllBySeedId(id);

		//태그 매핑 삭제
		seedTagRepository.deleteAllBySeedId(id);

		seedRepository.delete(seed);
	}

	// 콘텐츠 수정
	@Transactional
	public SeedResponse.SeedInfoSimple updateSeed(SeedUpdateRequest request,
		Long seedId, Member member) {
		if(request.seedType() == SeedType.LINK && request.seedLink() == null) {
			throw SeedzipException.from(ErrorCode.EMPTY_LINK);
		}

		Seed seed = getSeedOrThrow(seedId);
		if(!request.seedType().equals(seed.getSeedType())) {
			throw SeedzipException.from(ErrorCode.SEED_TYPE_NOT_SUPPORTED);
		}

		//콘텐츠 소유자 검증
		if (!seed.getMember().getId().equals(member.getId())) {
			throw SeedzipException.from(ErrorCode.MEMBER_NOT_OWNER);
		}

		// 기본 필드 수정
		if (!Objects.equals(seed.getSeedName(), request.seedName())) {
			seed.updateSeedName(request.seedName());
		}
		if (!Objects.equals(seed.getDDay(), request.dDay())) {
			seed.updateDDay(request.dDay());
		}
		if (!Objects.equals(seed.getSeedDetail(), request.seedDetail())) {
			seed.updateSeedDetail(request.seedDetail());
		}
		if (!Objects.equals(seed.getThumbnailIdx(), request.thumbnailImage())) {
			seed.updateThumbnailIdx(request.thumbnailImage());
		}

		// 태그
		seedTagRepository.deleteAllBySeedId(seedId);
		saveSeedTags(request.tags(), member, seed);

		// 카테고리
		categorySeedRepository.deleteAllBySeedId(seedId);
		saveSeedCategories(request.boardCategories(), member, seed);

		if (seed.getSeedType().equals(SeedType.LINK)) {
			File file = fileRepository.findBySeed(seed).orElseThrow(
				() -> SeedzipException.from(ErrorCode.FILE_NOT_FOUND)
			);
			if (!Objects.equals(file.getLink(), request.seedLink())) {
				file.updateLink(request.seedLink());
			}
		} else {    // 이미지, PDF
			deleteFileFromS3(seedId, seed);
			fileRepository.deleteAllBySeedId(seedId);
		}

		Seed updatedSeed = seedRepository.save(seed);
		return SeedResponse.SeedInfoSimple.builder()
			.seedId(updatedSeed.getId())
			.seedName(updatedSeed.getSeedName())
			.build();
	}

	//북마크된 씨드 조회
	@Transactional
	public SeedResponse.GetAllSeeds getBookmarkedSeeds(Member member, int page, int size, String sortBy, boolean isAsc,
								   String seedType) {
		Sort.Direction direction = getSortDirection(isAsc);
		String sortField = getSortField(sortBy);
		SeedType parsedSeedType = parseSeedType(seedType);

		//페이징을 위한 Pageable 객체
		Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortField));

		List<Long> seedIds = seedBookmarkRepository.findSeedIdsByMember(member);

		//페이징으로 얻어온 Seed 리스트
		Page<Seed> seedList;
		if (seedType != null) {
			seedList = seedRepository.findBySeedIdInAndSeedType(seedIds, parsedSeedType, pageable);
		} else {
			seedList = seedRepository.findBySeedIdIn(seedIds, pageable);
		}

		if (seedList.isEmpty()){
			throw SeedzipException.from(ErrorCode.EMPTY_SEED);
		}
		return buildGetAllSeedsResponse(member, seedList);
	}

	public SeedResponse.GetAllSeeds getPopularSeeds(int page, int size, Member member){
		Pageable pageable = PageRequest.of(page, size);
		Page<Seed> popularSeeds = seedRepository.findTop30ByMemberAndViewCountGreaterThanOrderByViewCountDesc(
			member, DEFAULT_VIEW_COUNT,
			pageable);

		if( popularSeeds.isEmpty() ) {
			throw SeedzipException.from(ErrorCode.EMPTY_SEED);
		}
		return buildGetAllSeedsResponse(member, popularSeeds);
	}

	public SeedResponse.GetAllSeeds getUnreadSeeds(Member member, int page, int size) {
		Pageable pageable = PageRequest.of(page, size);
		Page<Seed> seedList = seedRepository.findByMemberAndViewCount(member, UNREAD_VIEW_COUNT, pageable);

		if (seedList.isEmpty()) {
			throw SeedzipException.from(ErrorCode.EMPTY_SEED);
		}
		return buildGetAllSeedsResponse(member, seedList);
	}

	@Transactional
	public void deleteSeedList(SeedDeleteListRequest request, Member member) {
		List<Long> seedIds = request.seedIdList();
		if (seedIds.isEmpty())	return;

		List<Seed> seeds = seedRepository.findAllByIdIn(seedIds);
		if(seedIds.size() != seeds.size()) {
			throw SeedzipException.from(ErrorCode.SEED_ACCESS_DENIED);
		}

		// 소유자 검증 및 S3에서 파일 삭제
		for(Seed seed : seeds) {
			if(!seed.getMember().getId().equals(member.getId())) {
				throw SeedzipException.from(ErrorCode.MEMBER_NOT_OWNER);
			}
			deleteFileFromS3(seed.getId(), seed);
		}

		categorySeedRepository.deleteAllBySeedIdIn(seedIds);
		fileRepository.deleteAllBySeedIdIn(seedIds);
		seedTagRepository.deleteAllBySeedIdIn(seedIds);
		seedRepository.deleteAllByIdIn(seedIds);
	}

	private void deleteFileFromS3(Long id, Seed seed) {
		List<String> fileLinks = fileRepository.findFilesBySeedId(id).stream()
			.map(File::getLink)
			.toList();
		fileService.deleteFiles(seed.getSeedType(), fileLinks);
	}

	private SeedResponse.GetAllSeeds buildGetAllSeedsResponse(Member member, Page<Seed> seedList) {
		// seedId 리스트 추출
		List<Long> seedIds = seedList.stream()
			.map(Seed::getId)
			.toList();

		// seedId 리스트로 SeedProjectionResult 생성
		SeedProjectionResult seedProjectionResult = getSeedProjectionResult(seedIds);
		// 북마크된 씨드 ID 리스트를 Set으로 변환
		Set<Long> bookmarkedSeedSet = new HashSet<>(seedBookmarkRepository.findSeedIdsByMember(member));
		// SeedResponse.SeedInfo 리스트 생성
		List<SeedResponse.SeedInfo> seedInfoList = generateResponseFromSeedList(seedList.getContent(), seedProjectionResult, bookmarkedSeedSet);

		return SeedResponse.GetAllSeeds.builder()
			.nickname(member.getNickname())
			.seedInfoList(seedInfoList)
			.pageInfo(fromPage(seedList))
			.build();
	}

	private SeedResponse.GetFilteredSeeds buildGetFilteredSeedsResponse(Member member, Page<Seed> seedList) {
		// seedId 리스트 추출
		List<Long> seedIds = seedList.stream()
			.map(Seed::getId)
			.toList();

		SeedProjectionResult seedProjectionResult = getSeedProjectionResult(seedIds);

		Set<Long> bookmarkedSeedSet = new HashSet<>(seedBookmarkRepository.findSeedIdsByMember(member));

		List<SeedResponse.SeedInfoWithSeedDetail> seedInfoList = generateResponseWithDetailFromSeedList(seedList.getContent(), seedProjectionResult, bookmarkedSeedSet);

		return SeedResponse.GetFilteredSeeds.builder()
			.nickname(member.getNickname())
			.seedInfoList(seedInfoList)
			.pageInfo(fromPage(seedList))
			.build();
	}

	private static SeedResponse.PageInfo fromPage(Page<Seed> seedList) {
		return SeedResponse.PageInfo.builder()
			.page(seedList.getNumber())
			.size(seedList.getSize())
			.totalPages(seedList.getTotalPages())
			.totalElements(seedList.getTotalElements())
			.isLast(seedList.isLast())
			.build();
	}

	private SeedProjectionResult getSeedProjectionResult(List<Long> seedIds) {
		// 파일 프로젝션
		List<FileSeedProjection> fileProjections = fileRepository.findFileInfoBySeedIds(seedIds);
		Map<Long, String> thumbnailMap = getThumbnailMap(fileProjections);

		// 카테고리 프로젝션
		List<CategorySeedProjection> categoryProjections = categorySeedRepository.findCategoryInfoBySeedIds(seedIds);
		Map<Long, List<Long>> categoryIdMap = getCategoryIdMap(categoryProjections);
		Map<Long, List<String>> categoryNameMap = getCategoryNameMap(categoryProjections);

		// 태그 프로젝션
		List<SeedTagProjection> tagProjections = seedTagRepository.findTagInfoBySeedIds(seedIds);
		Map<Long, List<Long>> tagIdMap = getTagIdMap(tagProjections);
		Map<Long, List<String>> tagNameMap = getTagNameMap(tagProjections);

		return new SeedProjectionResult(
			thumbnailMap,
			categoryIdMap,
			categoryNameMap,
			tagIdMap,
			tagNameMap
		);
	}

	private Seed saveSeed(SeedRequest seedRequest, Member member) {
		SeedDTO seedDTO = SeedDTO.builder()
			.seedName(seedRequest.seedName() == null ? LocalDate.now().toString() : seedRequest.seedName())
			.seedDetail(seedRequest.seedDetail())
			.thumbnailImage(seedRequest.thumbnailImage() == null ? 0 : seedRequest.thumbnailImage())
			.dDay(seedRequest.dDay())
			.seedType(seedRequest.seedType())
			.member(member)
			.build();

		return seedRepository.save(seedDTO.toEntity());
	}

	private void saveSeedDefaultCategory(Member member, Seed seed) {
		Category category = categoryRepository.findByMemberIdAndName(member.getId(), "미분류")
				.orElseThrow(() -> SeedzipException.from(ErrorCode.CATEGORY_NOT_FOUND));
		categorySeedRepository.save(CategorySeed.builder()
				.category(category)
				.seed(seed)
				.build());
	}

	private void saveSeedCategories(String[] boardCategories, Member member, Seed seed) {
		Arrays.stream(boardCategories)
			.map(categoryName -> categoryRepository.findByMemberIdAndName(member.getId(), categoryName)
				.orElseThrow(() -> SeedzipException.from(ErrorCode.CATEGORY_NOT_FOUND)))
			.forEach(category -> {
				categorySeedRepository.save(
					CategorySeed.builder()
						.category(category)
						.seed(seed)
						.build()
				);
			});
	}

	private void saveSeedTags(String[] tags, Member member, Seed seed) {
		for (String tagName : tags) {
			Tag tag = findOrCreateTag(tagName, member);

			seedTagRepository.save(
				SeedTag.builder()
					.seed(seed)
					.tag(tag)
					.build()
			);
		}
	}

	private Tag findOrCreateTag(String tagName, Member member) {
		// 1.default tag 확인
		if(DEFAULT_TAG_NAMES.contains(tagName)) {
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
			.orElseGet(() ->
				tagRepository.save(Tag.builder()
					.tagName(tagName)
					.member(member)
					.build()));
	}

	//List<Seed> -> List<SeedResponse.SeedInfo>로 변환
	private List<SeedResponse.SeedInfo> generateResponseFromSeedList(
			List<Seed> seedList,
			SeedProjectionResult seedProjectionResult,
			Set<Long> bookmarkedSeedSet) {
		Map<Long, String> thumbnailMap = seedProjectionResult.thumbnailMap();
		Map<Long, List<Long>> categoryIdMap = seedProjectionResult.categoryIdMap();
		Map<Long, List<String>> categoryNameMap = seedProjectionResult.categoryNameMap();
		Map<Long, List<Long>> tagIdMap = seedProjectionResult.tagIdMap();
		Map<Long, List<String>> tagNameMap = seedProjectionResult.tagNameMap();

		return seedList.stream()
				.map(seed -> {
					Long seedId = seed.getId();

					// D-day 계산
					int dDayValue = 1;
					if (seed.getDDay() != null) {
						long days = ChronoUnit.DAYS.between(LocalDate.now(), seed.getDDay());
						dDayValue = days > 0 ? -(int) days : (days == 0 ? 0 : 1);
					}

					boolean isSaved = bookmarkedSeedSet.contains(seedId);

					return new SeedResponse.SeedInfo(
							seedId,
							seed.getSeedName(),
							categoryIdMap.getOrDefault(seedId, List.of()),
							categoryNameMap.getOrDefault(seedId, List.of()),
							seed.getSeedType(),
							thumbnailMap.getOrDefault(seedId, null),
							seed.getUpdatedAt(),
							tagIdMap.getOrDefault(seedId, List.of()),
							tagNameMap.getOrDefault(seedId, List.of()),
							dDayValue,
							isSaved
					);
				})
				.collect(Collectors.toList());
	}

	//List<Seed> -> List<SeedResponse.SeedInfoWithSeedDetail>로 변환 Refact
	private List<SeedResponse.SeedInfoWithSeedDetail> generateResponseWithDetailFromSeedList(
		List<Seed> seedList,
		SeedProjectionResult seedProjectionResult,
		Set<Long> bookmarkedSeedSet
	) {
		Map<Long, String> thumbnailMap = seedProjectionResult.thumbnailMap();
		Map<Long, List<Long>> categoryIdMap = seedProjectionResult.categoryIdMap();
		Map<Long, List<String>> categoryNameMap = seedProjectionResult.categoryNameMap();
		Map<Long, List<Long>> tagIdMap = seedProjectionResult.tagIdMap();
		Map<Long, List<String>> tagNameMap = seedProjectionResult.tagNameMap();

		return seedList.stream()
			.map(seed -> {
				Long seedId = seed.getId();

				// D-day 계산
				int dDayValue = 1;
				if (seed.getDDay() != null) {
					long days = ChronoUnit.DAYS.between(LocalDate.now(), seed.getDDay());
					dDayValue = days > 0 ? -(int) days : (days == 0 ? 0 : 1);
				}

				boolean isSaved = bookmarkedSeedSet.contains(seedId);

				return new SeedResponse.SeedInfoWithSeedDetail(
					seedId,
					seed.getSeedName(),
					categoryIdMap.getOrDefault(seedId, List.of()),
					categoryNameMap.getOrDefault(seedId, List.of()),
					seed.getSeedType(),
					thumbnailMap.getOrDefault(seedId, null),
					seed.getUpdatedAt(),
					tagIdMap.getOrDefault(seedId, List.of()),
					tagNameMap.getOrDefault(seedId, List.of()),
					dDayValue,
					seed.getSeedDetail(),
						isSaved
				);
			})
			.collect(Collectors.toList());
	}

	// 정렬 방향을 결정하는 메소드
	private Sort.Direction getSortDirection(boolean isAsc) {
		return isAsc ? Sort.Direction.ASC : Sort.Direction.DESC;
	}

	// sortBy 값에 따라 필드명을 결정하는 메소드
	private String getSortField(String sortBy) {
		return switch (sortBy) {
			case "name" -> "seedName";
			case "latest" -> "createdAt";
			default -> "createdAt";  // 기본값은 "createdAt"으로 설정
		};
	}

	// 문자열로 전달된 seedType을 Enum으로 변환하는 메소드
	private SeedType parseSeedType(String seedType) {
		if (seedType != null && !seedType.isBlank()) {
			try {
				return SeedType.valueOf(seedType.toUpperCase());  // 대소문자 구분 없이 변환
			} catch (IllegalArgumentException e) {
				// 잘못된 입력값에 대해 예외를 던짐
				throw SeedzipException.from(ErrorCode.INVALID_INPUT_VALUE);
			}
		}
		return null;  // seedType이 null 또는 빈 문자열일 경우 null 반환
	}


	private Map<Long, String> getThumbnailMap(List<FileSeedProjection> fileProjections) {
		return fileProjections.stream()
				.filter(p -> {
					if (p.getSeedType() == SeedType.LINK) return true;
					return Boolean.TRUE.equals(p.getIsThumbnail());
				})
				.collect(Collectors.toMap(FileSeedProjection::getSeedId, FileSeedProjection::getLink, (f1, f2) -> f1));
	}

	private Map<Long, List<Long>> getCategoryIdMap(List<CategorySeedProjection> categoryProjections) {
		return categoryProjections.stream()
				.collect(Collectors.groupingBy(CategorySeedProjection::getSeedId,
						Collectors.mapping(CategorySeedProjection::getCategoryId, Collectors.toList())));
	}

	private Map<Long, List<String>> getCategoryNameMap(List<CategorySeedProjection> categoryProjections) {
		return categoryProjections.stream()
				.collect(Collectors.groupingBy(CategorySeedProjection::getSeedId,
						Collectors.mapping(CategorySeedProjection::getCategoryName, Collectors.toList())));
	}

	private Map<Long, List<Long>> getTagIdMap (List<SeedTagProjection> tagProjections) {
		return tagProjections.stream()
				.collect(Collectors.groupingBy(SeedTagProjection::getSeedId,
						Collectors.mapping(SeedTagProjection::getTagId, Collectors.toList())));
	}

	private Map<Long, List<String>> getTagNameMap (List<SeedTagProjection> tagProjections) {
		return tagProjections.stream()
				.collect(Collectors.groupingBy(SeedTagProjection::getSeedId,
						Collectors.mapping(SeedTagProjection::getTagName, Collectors.toList())));
	}

	private Seed getSeedOrThrow(Long seedId) {
		return seedRepository.findById(seedId)
			.orElseThrow(() -> SeedzipException.from(ErrorCode.SEED_NOT_FOUND));
	}

}
