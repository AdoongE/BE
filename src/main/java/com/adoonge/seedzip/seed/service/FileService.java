package com.adoonge.seedzip.seed.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
}
