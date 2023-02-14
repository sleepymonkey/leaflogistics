package com.leaflogistics.controller;

import com.leaflogistics.service.ITransposeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(path="/api", produces="application/json", consumes="application/json")
public class TransposeController {
    private ITransposeService transposeService;


    @Autowired
    public void setTransposeService(ITransposeService transposeService) {
        this.transposeService = transposeService;
    }


    @PostMapping("/transpose")
    public ResponseEntity<String> transpose(@RequestBody String inputJson) {
        String respJson = this.transposeService.transpose(inputJson);
        return new ResponseEntity<>(respJson, HttpStatus.OK);
    }
}
