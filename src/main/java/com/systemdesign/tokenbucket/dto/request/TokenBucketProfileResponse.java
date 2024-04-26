package com.systemdesign.tokenbucket.dto.request;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class TokenBucketProfileResponse {

    private List<TokenBucketResponse> counters;

    public static TokenBucketProfileResponse from(List<TokenBucketResponse> counters) {
        return TokenBucketProfileResponse.builder()
                .counters(counters)
                .build();
    }
}
