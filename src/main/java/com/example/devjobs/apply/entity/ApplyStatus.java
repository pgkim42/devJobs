package com.example.devjobs.apply.entity;

public enum ApplyStatus {

    AVAILABLE("지원가능"),
    COMPLETED("지원완료");

    private final String status;

    ApplyStatus(String status) {

        this.status = status;
    }

    public String getStatus() {

        return this.status;
    }

}
