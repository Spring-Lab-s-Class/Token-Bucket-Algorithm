package com.systemdesign.tokenbucket.controller;

import com.systemdesign.tokenbucket.dto.request.TokenBucketResponse;
import com.systemdesign.tokenbucket.service.TokenBucketService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("token-bucket")
public class TokenBucketController {

    private final TokenBucketService tokenBucketService;

    @GetMapping
    public ResponseEntity<List<TokenBucketResponse>> findAllTokenBucket() {
        return ResponseEntity.status(OK)
                .body(tokenBucketService.findAllTokenBucket());
    }

    @PostMapping
    public ResponseEntity<TokenBucketResponse> createTokenBucket() {
        return ResponseEntity.status(CREATED)
                .body(tokenBucketService.createTokenBucket());
    }
}
