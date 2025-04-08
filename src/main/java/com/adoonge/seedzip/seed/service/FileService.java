package com.adoonge.seedzip.seed.service;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.adoonge.seedzip.global.exception.ErrorCode;
import com.adoonge.seedzip.global.exception.SeedzipException;
import com.adoonge.seedzip.global.service.S3Service;
import com.adoonge.seedzip.seed.domain.File;
import com.adoonge.seedzip.seed.domain.Seed;
import com.adoonge.seedzip.seed.domain.SeedType;
import com.adoonge.seedzip.seed.repository.FileRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Service
@Slf4j
@Transactional(readOnly = true)
public class FileService {

	private final FileRepository fileRepository;
	private final S3Service s3Service;

	@Transactional
	public void saveLink(String contentLink, Seed seed) {
		fileRepository.save(
			File.linkBuilder()
				.link(contentLink)
				.seed(seed)
				.build()
		);

	}

	@Transactional
	public void saveFiles(List<MultipartFile> files, Seed seed) {
		AtomicInteger index = new AtomicInteger(0);
		long thumbnailIndex = seed.getThumbnailIdx();

		files.forEach(
			file -> {
				try {
					String fileLink;
					if(seed.getSeedType() == SeedType.IMAGE){
						fileLink = s3Service.uploadImgFile(file);
					}else{
						fileLink = s3Service.uploadDocFile(file);
					}

					fileRepository.save(
						File.fileBuilder()
							.link(fileLink)
							.fileName(file.getOriginalFilename())
							.isThumbnail(index.get() == thumbnailIndex)
							.seed(seed)
							.build()
					);

					index.getAndIncrement();
				} catch (Exception e) {
					throw SeedzipException.from(ErrorCode.S3_UPLOAD_FAILURE);
				}
			}
		);
	}
}
