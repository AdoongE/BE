package com.adoonge.seedzip.seed.service;

import com.adoonge.seedzip.seed.repository.SeedRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class SeedService {

    private final SeedRepository seedRepository;
}
