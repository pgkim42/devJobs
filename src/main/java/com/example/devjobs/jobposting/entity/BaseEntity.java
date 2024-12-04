package com.example.devjobs.jobposting.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@Setter
@EntityListeners(value = { AuditingEntityListener.class })
@MappedSuperclass  // 이 클래스는 엔티티로 사용되지 않고 상속만 받음
public abstract class BaseEntity {

    @CreatedDate
    @Column(name = "created_date", nullable = false, updatable = false)
    private LocalDateTime createdDate;  // 등록일

    @LastModifiedDate
    @Column(name = "updated_date")
    private LocalDateTime updatedDate;  // 수정일

}