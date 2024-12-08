package com.example.devjobs.applications.service;

import com.example.devjobs.applications.repository.ApplicationsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ApplicationsServiceImpl implements ApplicationsService{

    @Autowired
    ApplicationsRepository repository;

}
