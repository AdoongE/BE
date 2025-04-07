package com.adoonge.seedzip.seed.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.adoonge.seedzip.member.domain.Member;
import com.adoonge.seedzip.seed.domain.File;
import com.adoonge.seedzip.seed.domain.Seed;
import com.adoonge.seedzip.seed.repository.FileRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Service
@Slf4j
@Transactional(readOnly = true)
public class FileService {

	private final FileRepository fileRepository;

	@Transactional
	public void saveLink(String contentLink, Seed seed) {
		fileRepository.save(
			File.builder()
				.link(contentLink)
				.seed(seed)
				.build()
		);

	}

	@Transactional
	public void saveFiles(List<MultipartFile> files, Seed seed) {

	}
}
