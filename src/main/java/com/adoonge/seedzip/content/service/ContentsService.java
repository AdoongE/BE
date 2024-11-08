package com.adoonge.seedzip.content.service;

import com.adoonge.seedzip.category.domain.Category;
import com.adoonge.seedzip.category.domain.Visibility;
import com.adoonge.seedzip.category.repository.CategoryRepository;
import com.adoonge.seedzip.content.domain.Contents;
import com.adoonge.seedzip.content.domain.Document;
import com.adoonge.seedzip.content.domain.Link;
import com.adoonge.seedzip.content.domain.Tag;
import com.adoonge.seedzip.content.domain.mapping.CategoryContent;
import com.adoonge.seedzip.content.domain.mapping.ContentTag;
import com.adoonge.seedzip.content.dto.request.ContentsRequest;
import com.adoonge.seedzip.content.dto.response.ContentsDocResponse;
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
import java.util.List;
import java.util.Objects;

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
        for (String tagName : request.getTags()) {
            Tag tag = tagRepository.findByTagName(tagName)
                    .orElseGet(() -> {
                        Tag newTag = Tag.builder()
                                .tagName(tagName)
                                .build();
                        return tagRepository.save(newTag); // 존재하지 않으면 태그 생성
                    });

            // ContentTag 엔티티 생성 후 저장
            ContentTag contentTag = new ContentTag();
            contentTag.setContents(contents); // Content 엔티티는 이미 존재한다고 가정
            contentTag.setTag(tag);
            contentTagRepository.save(contentTag);
        }

        if(Objects.isNull(request.getBoardCategory())){
            Category category = categoryRepository.findByName("default");
            request.setBoardCategory(new String[] {"default"});
        }
        else{
            // 카테고리 저장
            for (String categoryName : request.getBoardCategory()) {
                Category category = categoryRepository.findByName(categoryName);

                // CategoryContent 엔티티 생성 후 저장
                CategoryContent categoryContent = new CategoryContent();
                categoryContent.setContents(contents); // Content 엔티티는 이미 존재한다고 가정
                categoryContent.setCategory(category);
                categoryContentRepository.save(categoryContent);
            }
        }

        List<String> fileUrls = new ArrayList<>();

        for (MultipartFile file : files) {
            try {
                // S3에 파일 업로드 및 URL 가져오기
                String fileUrl = s3Service.uploadDocFile(file);
                fileUrls.add(fileUrl);

                // URL 저장
                Document document = documentRepository.save(request.toDocEntity(contents, fileUrl));
            }
            catch (IOException e){
                e.printStackTrace();
                return null;
            }
        }
        return ContentsDocResponse.fromEntity("문서를 저장했습니다!", contents);
    }

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

        for (String links : request.getContentLinks()) {
            Link link = Link.builder()
                    .link(links)
                    .contents(contents)
                    .build();
            linkRepository.save(link);
        }

        // 태그 저장
        for (String tagName : request.getTags()) {
            Tag tag = tagRepository.findByTagName(tagName)
                    .orElseGet(() -> {
                        Tag newTag = Tag.builder()
                                .tagName(tagName)
                                .build();
                        return tagRepository.save(newTag); // 존재하지 않으면 태그 생성
                    });

            // ContentTag 엔티티 생성 후 저장
            ContentTag contentTag = new ContentTag();
            contentTag.setContents(contents); // Content 엔티티는 이미 존재한다고 가정
            contentTag.setTag(tag);
            contentTagRepository.save(contentTag);
        }

        if(Objects.isNull(request.getBoardCategory())){
            Category category = categoryRepository.findByName("default");
            request.setBoardCategory(new String[] {"default"});
        }
        else{
            // 카테고리 저장
            for (String categoryName : request.getBoardCategory()) {
                Category category = categoryRepository.findByName(categoryName);

                // CategoryContent 엔티티 생성 후 저장
                CategoryContent categoryContent = new CategoryContent();
                categoryContent.setContents(contents); // Content 엔티티는 이미 존재한다고 가정
                categoryContent.setCategory(category);
                categoryContentRepository.save(categoryContent);
            }
        }
        return ContentsLinkResponse.fromEntity("링크를 저장했습니다!", contents);
    }
}
