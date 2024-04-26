package com.systemdesign.tokenbucket.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TokenBucketResponse {

    private String key;
    private Long requestCount;

    public static TokenBucketResponse from(String key, Long requestCount) {
        return TokenBucketResponse.builder()
                .key(key)
                .requestCount(requestCount)
                .build();
    }
}
