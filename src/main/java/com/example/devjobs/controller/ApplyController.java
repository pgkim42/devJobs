package com.example.devjobs.controller;

import com.example.devjobs.apply.dto.ApplyDTO;
import com.example.devjobs.apply.service.ApplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/apply")
public class ApplyController {

    @Autowired
    ApplyService service;

    @PostMapping("/register")
    public ResponseEntity<Integer> register(@RequestBody ApplyDTO dto) {

        int Apply = service.register(dto);
        return new ResponseEntity<>(Apply, HttpStatus.CREATED); // 성공시 201

    }

    @GetMapping("/list")
    public ResponseEntity<List<ApplyDTO>> getList() {
        List<ApplyDTO> list = service.getList();
        return new ResponseEntity<>(list, HttpStatus.OK); // 성공시 200
    }

    @GetMapping("/read/{cd}")
    public ResponseEntity<ApplyDTO> read(@PathVariable int cd) {
        ApplyDTO dto = service.read(cd);
        return new ResponseEntity(dto, HttpStatus.OK); // 성공시 204
    }

    @PutMapping("/modify")
    public ResponseEntity modify(@RequestBody ApplyDTO dto) {
        service.modify(dto);
        return new ResponseEntity(dto, HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/remove/{cd}")
    public ResponseEntity remove(@PathVariable("cd") Integer cd) {
        service.remove(cd);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

}
