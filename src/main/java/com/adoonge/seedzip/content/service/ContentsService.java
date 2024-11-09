package com.adoonge.seedzip.content.service;

import com.adoonge.seedzip.category.domain.Category;
import com.adoonge.seedzip.category.domain.Visibility;
import com.adoonge.seedzip.category.repository.CategoryRepository;
import com.adoonge.seedzip.content.domain.*;
import com.adoonge.seedzip.content.domain.mapping.CategoryContent;
import com.adoonge.seedzip.content.domain.mapping.ContentTag;
import com.adoonge.seedzip.content.dto.request.ContentsRequest;
import com.adoonge.seedzip.content.dto.response.ContentsDocResponse;
import com.adoonge.seedzip.content.dto.response.ContentsImageResponse;
import com.adoonge.seedzip.content.dto.response.ContentsLinkResponse;
import com.adoonge.seedzip.content.repository.*;
import com.adoonge.seedzip.member.domain.Member;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

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
    private LinkRepository linkRepository;

    @Autowired
    private final S3Service s3Service;
    @Autowired
    private ImageRepository imageRepository;

    @Transactional
    public ContentsDocResponse createDocContents(ContentsRequest.docContentsRequest request, List<MultipartFile> files, Member member) {
        if(Objects.isNull(request.getContentName())){
            request.setContentName(null);
        }
        if(Objects.isNull(request.getDDay())){
            request.setDDay(null);
        }
        if(Objects.isNull(request.getContentDetail())){
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

        if(Objects.isNull(request.getBoardCategory())){
            Category category = categoryRepository.findByName("default");
            request.setBoardCategory(new String[] {"default"});
        }
        else{
            // 카테고리 저장
            Arrays.stream(request.getBoardCategory())  // String[]을 스트림으로 변환
                    .map(categoryName -> categoryRepository.findByName(categoryName))
                    .forEach(category -> {
                        CategoryContent categoryContent = new CategoryContent();
                        categoryContent.setContents(contents); // Content 엔티티는 이미 존재한다고 가정
                        categoryContent.setCategory(category);
                        categoryContentRepository.save(categoryContent);
                    });
        }

        List<String> fileUrls = new ArrayList<>();

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
        return ContentsDocResponse.fromEntity("문서를 저장했습니다!", contents);
    }

    @Transactional
    public ContentsLinkResponse createLinkContents(ContentsRequest.linkContentsRequest request, Member member) {
        if(Objects.isNull(request.getContentName())){
            request.setContentName(null);
        }
        if(Objects.isNull(request.getDDay())){
            request.setDDay(null);
        }
        if(Objects.isNull(request.getContentDetail())){
            request.setContentDetail(null);
        }

        Contents contents = contentsRepository.save(request.toContentEntity(member));

        Arrays.stream(request.getContentLinks())
                .map(links -> Link.builder()
                        .link(links)
                        .contents(contents)
                        .build())
                .forEach(link -> linkRepository.save(link));

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

        if(Objects.isNull(request.getBoardCategory())){
            Category category = categoryRepository.findByName("default");
            request.setBoardCategory(new String[] {"default"});
        }
        else{
            // 카테고리 저장
            Arrays.stream(request.getBoardCategory())  // String[]을 스트림으로 변환
                    .map(categoryName -> categoryRepository.findByName(categoryName))
                    .forEach(category -> {
                        CategoryContent categoryContent = new CategoryContent();
                        categoryContent.setContents(contents); // Content 엔티티는 이미 존재한다고 가정
                        categoryContent.setCategory(category);
                        categoryContentRepository.save(categoryContent);
                    });
        }
        return ContentsLinkResponse.fromEntity("링크를 저장했습니다!", contents);
    }

    @Transactional
    public ContentsImageResponse createImageContents(ContentsRequest.imageContentsRequest request, List<MultipartFile> files, Member member) {
        if(Objects.isNull(request.getContentName())){
            request.setContentName(null);
        }
        if(Objects.isNull(request.getDDay())){
            request.setDDay(null);
        }
        if(Objects.isNull(request.getContentDetail())){
            request.setContentDetail(null);
        }

        Contents contents = contentsRepository.save(request.toContentEntity(member));

        // 태그 저장
        Arrays.stream(request.getTags())  // request.getTags()가 String[]일 때
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

        if(Objects.isNull(request.getBoardCategory())){
            Category category = categoryRepository.findByName("default");
            request.setBoardCategory(new String[] {"default"});
        }
        else{
            // 카테고리 저장
            Arrays.stream(request.getBoardCategory())  // String[]을 스트림으로 변환
                    .map(categoryName -> categoryRepository.findByName(categoryName))
                    .forEach(category -> {
                        CategoryContent categoryContent = new CategoryContent();
                        categoryContent.setContents(contents); // Content 엔티티는 이미 존재한다고 가정
                        categoryContent.setCategory(category);
                        categoryContentRepository.save(categoryContent);
                    });
        }

        List<String> fileUrls = new ArrayList<>();

        AtomicInteger index = new AtomicInteger(0); // 현재 인덱스를 추적하기 위한 변수
        int thumbnailIndex = request.getThumbnailImage(); // imgThumbnail 인덱스 가져오기

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

                // 저장
                imageRepository.save(image);

                // 인덱스 증가
                index.getAndIncrement();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        return ContentsImageResponse.fromEntity("이미지를 저장했습니다!", contents);
    }
}
