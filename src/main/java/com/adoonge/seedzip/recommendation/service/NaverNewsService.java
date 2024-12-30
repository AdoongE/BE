package com.adoonge.seedzip.recommendation.service;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class NaverNewsService {

	@Value("${X_NAVER_CLIENT_ID}")
	private String X_NAVER_CLIENT_ID;

	@Value("${X_NAVER_CLIENT_SECRET}")
	private String X_NAVER_CLIENT_SECRET;

	public String searchNews(String title) throws UnsupportedEncodingException {
		String encoded = parseUrl(title);	// 검색어 인코딩
		log.info(encoded);

		// URI 설정
		String apiUrl = "https://openapi.naver.com/v1/search/news.json" +
			"?query=" + encoded +
			"&display=1" +
			"&start=1" +
			"&sort=sim";

		URI uri = URI.create(apiUrl);

		// 헤더 설정
		RequestEntity<Void> req = RequestEntity
			.get(uri)
			.header("X-Naver-Client-Id", X_NAVER_CLIENT_ID)
			.header("X-Naver-Client-Secret", X_NAVER_CLIENT_SECRET)
			.header("User-Agent", "curl/7.49.1")
			.header("Accept", "*/*")
			.header("Host", "openapi.naver.com")
			.build();

		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<String> response = restTemplate.exchange(req, String.class);

		String body = response.getBody();
		return body;
	}

	private String parseUrl(String title) throws UnsupportedEncodingException {

		return URLEncoder.encode(title, StandardCharsets.UTF_8)
			.replace("+", "%20");
	}


}
