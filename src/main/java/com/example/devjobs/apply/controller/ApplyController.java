package com.example.devjobs.apply.controller;

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

    @GetMapping("/read/{code}")
    public ResponseEntity<ApplyDTO> read(@PathVariable int code) {
        ApplyDTO dto = service.read(code);
        return new ResponseEntity(dto, HttpStatus.OK); // 성공시 204
    }

    @PutMapping("/modify")
    public ResponseEntity modify(@RequestBody ApplyDTO dto) {
        service.modify(dto);
        return new ResponseEntity(dto, HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/remove/{code}")
    public ResponseEntity remove(@PathVariable("code") Integer code) {
        service.remove(code);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

}
