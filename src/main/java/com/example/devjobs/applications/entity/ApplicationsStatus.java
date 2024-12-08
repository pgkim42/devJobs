package com.example.devjobs.applications.entity;

public enum ApplicationsStatus {

    AVAILABLE("지원가능"),
    COMPLETED("지원완료");

    private final String status;

    ApplicationsStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return this.status;
    }

}
