package com.adoonge.seedzip.summary.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.api.services.youtube.YouTube;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;

import java.io.IOException;
import java.util.Collections;
import java.util.List;;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Service
@Slf4j
public class YouTubeService {

	@Value("${YOUTUBE_API_KEY}")
	private String apiKey;

	// url로 비디오 검색하는 함수
	public String searchVideos(String query) throws IOException {

		JacksonFactory jacksonFactory = new JacksonFactory();

		// YouTube 객체를 빌드하여 API에 접근할 수 있는 YouTube 클라이언트 생성
		YouTube youtube = new YouTube.Builder(
			new com.google.api.client.http.javanet.NetHttpTransport(),
			jacksonFactory,
			request -> {})
			.build();

		// YouTube Search API를 사용하여 동영상 검색을 위한 요청 객체 생성
		YouTube.Search.List search = youtube.search().list(Collections.singletonList("id,snippet"));

		search.setKey(apiKey);	// API 키 설정
		search.setQ(extractVideoId(query));		// 검색어 설정

		// 검색 요청 실행 및 응답 받아오기
		SearchListResponse searchResponse = search.execute();

		// 검색 결과에서 동영상 목록 가져오기
		List<SearchResult> searchResultList = searchResponse.getItems();

		if (searchResultList != null && searchResultList.size() > 0) {
			//검색 결과 중 첫 번째 동영상 정보 가져오기
			SearchResult searchResult = searchResultList.get(0);

			// 동영상의 ID, 제목, 채널명, description 가져오기
			//String videoId = searchResult.getId().getVideoId();
			String videoTitle = searchResult.getSnippet().getTitle();
			String channelTitle = searchResult.getSnippet().getChannelTitle();
			String description = searchResult.getSnippet().getDescription();


			return "Title: " + videoTitle +  "\nChannelTitle : "
				+ channelTitle
				+ "\nDescription : " + description;

		}
		return "검색 결과가 없습니다";
	}

	// url에서 비디오 아이디 추출
	public static String extractVideoId(String videoUrl) {
		String videoId = null;

		// "v=" 뒤에 있는 값을 추출
		if (videoUrl.contains("v=")) {
			videoId = videoUrl.substring(videoUrl.indexOf("v=") + 2);
			// 만약 URL에 다른 파라미터가 있으면 (&t= 등) 이를 처리
			if (videoId.contains("&")) {
				videoId = videoId.substring(0, videoId.indexOf("&"));
			}
		}

		return videoId;
	}

}
