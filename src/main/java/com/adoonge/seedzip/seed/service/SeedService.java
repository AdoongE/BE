package com.adoonge.seedzip.seed.service;

import com.adoonge.seedzip.global.exception.ErrorCode;
import com.adoonge.seedzip.global.exception.SeedzipException;
import com.adoonge.seedzip.member.domain.Member;
import com.adoonge.seedzip.seed.domain.File;
import com.adoonge.seedzip.seed.domain.Seed;
import com.adoonge.seedzip.seed.domain.SeedType;
import com.adoonge.seedzip.seed.dto.response.SeedResponse;
import com.adoonge.seedzip.seed.dto.response.SeedResponse.GetAllSeeds;
import com.adoonge.seedzip.seed.repository.CategorySeedRepository;
import com.adoonge.seedzip.seed.repository.FileRepository;
import com.adoonge.seedzip.seed.repository.SeedRepository;
import com.adoonge.seedzip.seed.repository.SeedTagRepository;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Slf4j
public class SeedService {

    private final SeedRepository seedRepository;
    private final FileRepository fileRepository;
    private final CategorySeedRepository categorySeedRepository;
    private final SeedTagRepository seedTagRepository;

    @Transactional(readOnly = true)
    public SeedResponse.GetAllSeeds getAllSeeds(Member member, int page, int size, String sortBy, boolean isAsc, String seedType) {
        Sort.Direction direction = isAsc ? Sort.Direction.ASC : Sort.Direction.DESC;

        String sortField = switch (sortBy) {
            case "name" -> "seedName";
            case "latest" -> "createdAt";
            default -> "createdAt";
        };

        // 문자열 -> Enum 변환 (대소문자 구분 없이 처리)
        SeedType parsedSeedType = null;
        if (seedType != null && !seedType.isBlank()) {
            try {
                parsedSeedType = SeedType.valueOf(seedType.toUpperCase());
            } catch (IllegalArgumentException e) {
                throw SeedzipException.from(ErrorCode.INVALID_INPUT_VALUE);
            }
        }

        //페이징을 위한 Pageable 객체
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortField));

        //페이징으로 얻어온 Seed 리스트
        Page<Seed> seedList;
        if (seedType != null) {
            seedList = seedRepository.findByMemberAndSeedType(member, parsedSeedType, pageable);
        } else {
            seedList = seedRepository.findByMember(member, pageable);
        }

        if (seedList.isEmpty())
            return null;

        List<SeedResponse.SeedInfo> seedInfoList = generateResponseFromSeedList(seedList.getContent());

        //페이징 정보 추가
        SeedResponse.PageInfo pageInfo = SeedResponse.PageInfo.builder()
                .page(seedList.getNumber())
                .size(seedList.getSize())
                .totalPages(seedList.getTotalPages())
                .totalElements(seedList.getTotalElements())
                .isLast(seedList.isLast())
                .build();

        SeedResponse.GetAllSeeds getAllSeeds = GetAllSeeds.builder()
                .nickname(member.getNickname())
                .seedInfoList(seedInfoList)
                .pageInfo(pageInfo)
                .build();

        return getAllSeeds;
    }

    @Transactional(readOnly = true)
    public SeedResponse.GetAllSeeds getCategorySeeds(Member member, int page, int size, String sortBy, boolean isAsc, String seedType, long categoryId) {
        Sort.Direction direction = isAsc ? Sort.Direction.ASC : Sort.Direction.DESC;

        String sortField = switch (sortBy) {
            case "name" -> "seedName";
            case "latest" -> "createdAt";
            default -> "createdAt";
        };

        // 문자열 -> Enum 변환 (대소문자 구분 없이 처리)
        SeedType parsedSeedType = null;
        if (seedType != null && !seedType.isBlank()) {
            try {
                parsedSeedType = SeedType.valueOf(seedType.toUpperCase());
            } catch (IllegalArgumentException e) {
                throw SeedzipException.from(ErrorCode.INVALID_INPUT_VALUE);
            }
        }

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

        if (seedList.isEmpty())
            return null;

        List<SeedResponse.SeedInfo> seedInfoList = generateResponseFromSeedList(seedList.getContent());

        //페이징 정보 추가
        SeedResponse.PageInfo pageInfo = SeedResponse.PageInfo.builder()
                .page(seedList.getNumber())
                .size(seedList.getSize())
                .totalPages(seedList.getTotalPages())
                .totalElements(seedList.getTotalElements())
                .isLast(seedList.isLast())
                .build();

        SeedResponse.GetAllSeeds getAllSeeds = new SeedResponse.GetAllSeeds().builder()
                .nickname(member.getNickname())
                .seedInfoList(seedInfoList)
                .pageInfo(pageInfo)
                .build();

        return getAllSeeds;
    }

    //List<Seed> -> List<SeedResponse.SeedInfo>로 변환
    private List<SeedResponse.SeedInfo> generateResponseFromSeedList(List<Seed> seedList) {
        //여기 작성하기 + 카테고리 이름이랑 태그 이름 어떻게 효과적으로 가져올지 찾아보기
        return seedList.stream()
                .map(seed -> {
                    String thumbnailUrl = null;
                    //링크의 경우 thumbnail 없으니까, 해당 링크를 thumbnailUrl로 처리
                    if(seed.getSeedType().equals(SeedType.LINK)){
                        Optional<File> thumbnailFile = fileRepository.findBySeed(seed);
                        if(thumbnailFile.isPresent()){
                            thumbnailUrl = thumbnailFile.get().getLink();
                        }
                    }
                    // 이미지, PDF의 경우 isThumbnail이 true인 파일만 가져와서 s3 링크 추출
                    else {
                        Optional<File> thumbnailFile = fileRepository.findThumbnailBySeed(seed);
                        if(thumbnailFile.isPresent()){
                            thumbnailUrl = thumbnailFile.get().getLink();
                        }
                    }

                    //seedId에 해당하는 카테고리 리스트 조회
                    List<Long> categoryIds = categorySeedRepository.findCategoryIdsBySeed(seed);
                    List<String> categoryNames = categorySeedRepository.findCategoryNamesBySeed(seed);

                    //seedId에 해당하는 태그 리스트 조회
                    List<Long> tagIds = seedTagRepository.findTagIdsBySeed(seed);
                    List<String> tagNames = seedTagRepository.findTagNamesBySeed(seed);

                    // D-day 계산
                    int dDayValue = 1;
                    if (seed.getDDay() != null) {
                        LocalDate today = LocalDate.now();
                        long daysBetween = ChronoUnit.DAYS.between(today, seed.getDDay());

                        if (daysBetween > 0) {
                            dDayValue = -(int)daysBetween;
                        } else if (daysBetween == 0) {
                            dDayValue = 0;
                        }
                    }

                    // SeedResponse 객체에 필요한 정보 담기
                    return new SeedResponse.SeedInfo(
                            seed.getId(),
                            seed.getSeedName(),
                            categoryIds,
                            categoryNames,
                            seed.getSeedType(),
                            thumbnailUrl,
                            seed.getUpdatedAt(),
                            tagIds,
                            tagNames,
                            dDayValue
                    );

                })
                .collect(Collectors.toList());
    }
}
