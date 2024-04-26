package com.systemdesign.tokenbucket.service;

import com.systemdesign.tokenbucket.dto.request.TokenBucketResponse;
import com.systemdesign.tokenbucket.exception.RateLimitExceededException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static com.systemdesign.tokenbucket.exception.RateExceptionCode.COMMON_TOO_MANY_REQUESTS;

@Slf4j
@Service
@RequiredArgsConstructor
public class TokenBucketService {

    private final RedisTemplate<String, Long> redisTemplate;
    private final static String TOKEN_BUCKET_KEY = "TokenBucket:"; //키
    private final static long TOKEN_BUCKET_MAX_TOKEN = 1000; //최대 요청 허용 수
    private final static long TOKEN_BUCKET_REFILL_DURATION = 60; // 토큰 리필 시간


    public List<TokenBucketResponse> findAllTokenBucket() {
        Set<String> keys = redisTemplate.keys(TOKEN_BUCKET_KEY + "*");
        List<TokenBucketResponse> responses = new ArrayList<>();

        if (keys == null) {
            return responses;
        }

        for (String key : keys) {
            Long bucketTokens = redisTemplate.opsForValue().get(key);
            if (bucketTokens != null) {
                String bucketId = key.substring(TOKEN_BUCKET_KEY.length());
                responses.add(TokenBucketResponse.from(bucketId, bucketTokens));
            }
        }

        return responses;
    }

    public TokenBucketResponse createTokenBucket() {
        String redisKey = generateRedisKey();

        Long currentTokens = redisTemplate.opsForValue().get(redisKey);
        if (currentTokens == null) {

            redisTemplate.opsForValue().set(redisKey, TOKEN_BUCKET_MAX_TOKEN, TOKEN_BUCKET_REFILL_DURATION, TimeUnit.SECONDS);
            currentTokens = TOKEN_BUCKET_MAX_TOKEN;
        } else {
            if (currentTokens > 0) {
                redisTemplate.opsForValue().decrement(redisKey);
                currentTokens -= 1;
            } else {
                // 토큰 버킷의 토큰이 없을 경우 - 최대 요청 허용 수 초과
                log.debug("Rate limit exceeded. key: {}", redisKey);
                throw new RateLimitExceededException(COMMON_TOO_MANY_REQUESTS);
            }
        }
        return TokenBucketResponse.from(redisKey, currentTokens);
    }

    private String generateRedisKey() {
        return TOKEN_BUCKET_KEY + "responses";
    }
}
