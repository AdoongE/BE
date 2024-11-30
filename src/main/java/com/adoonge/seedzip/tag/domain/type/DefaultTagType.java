package com.adoonge.seedzip.tag.domain.type;

public enum DefaultTagType {
	PLANNING_IDEA("기획/아이디어"),
	TRAVEL("여행"),
	GLOBAL("글로벌"),
	RESTAURANT("맛집"),
	FOOD_COOKING("음식/요리"),
	EXERCISE("운동"),
	HEALTH("건강"),
	SPORTS("스포츠"),
	MOVIES_DRAMA("영화/드라마"),
	MUSICAL_THEATER("뮤지컬/연극"),
	ENTERTAINMENT("연예"),
	MUSIC("음악"),
	BEAUTY("뷰티"),
	FASHION("패션"),
	DESIGN("디자인"),
	UX_UI("UX/UI"),
	INTERIOR("인테리어"),
	PHOTOGRAPHY("사진"),
	VIDEO("영상"),
	SNS("SNS"),
	IT("IT"),
	BUSINESS("비즈니스"),
	SELF_IMPROVEMENT("자기계발"),
	PRODUCTIVITY("생산성"),
	LIFE("생활"),
	PET("반려동물"),
	BOOK_WRITING("책/글쓰기"),
	HOBBY("취미"),
	GAME("게임"),
	STUDY("공부"),
	FINANCE_INVESTMENT("금융/재테크"),
	REAL_ESTATE("부동산"),
	ART("예술"),
	ENVIRONMENT("환경"),
	HISTORY("역사"),
	SCIENCE("과학"),
	PHILOSOPHY("철학"),
	PSYCHOLOGY("심리학"),
	EDUCATION("교육"),
	POLITICS("정치");

	private final String displayName;

	DefaultTagType(String displayName) {
		this.displayName = displayName;
	}

	public String getDisplayName() {
		return displayName;
	}
}
