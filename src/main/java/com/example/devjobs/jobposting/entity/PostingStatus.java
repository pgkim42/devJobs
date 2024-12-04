package com.example.devjobs.jobposting.entity;

// 공고 상태
public enum PostingStatus {

    OPEN("모집중"),
    CLOSE("마감");

    private final String description;

    PostingStatus(String description) {
        this.description = description;
    }

    // 설명을 반환하는 메서드
    public String getDescription() {
        return description;
    }
}
