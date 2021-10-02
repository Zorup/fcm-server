package com.zorup.fcm.util;

import com.zorup.fcm.util.TokenQueryResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.zorup.fcm.util.TokenQueryResponse.*;

@Slf4j
@Repository
@RequiredArgsConstructor
public class RedisRepository {

    private final RedisTemplate redisTemplate;

    @Value("${redis-key-prefix}")
    private String prefix;

    /**
     *  fcm push-token 정보 저장에 Redis 사용
     *
     *  redis collection type: string
     *  key: prefix + userId   ex) if pushToken == xyz && prefix == "noti:" -> key= "noti:xyz"
     *  value: TokenQueryResponse.UserTokenInfo
     *
     */

    public void insertPushToken(UserTokenInfo userTokenInfo){
        ValueOperations<String, UserTokenInfo> ops = redisTemplate.opsForValue();
        String key = prefix + userTokenInfo.getUserId().toString();
        UserTokenInfo value = userTokenInfo;
        ops.set(key, value);
        log.info("push토큰 삽입완료 - Id: " + userTokenInfo.getUserId());
    }

    public List<UserTokenInfo> findPushTokens(List<Long> userIds){
        log.info("push토큰 찾기 - IDs: " + userIds);
        ValueOperations<String, UserTokenInfo> ops = redisTemplate.opsForValue();

        List<String> keys = userIds.stream()
                .map(uid -> prefix + uid.toString())
                .collect(Collectors.toList());

        return ops.multiGet(keys);  // 없는 key에 대해선 null로 채운 List를 반환 ex) multiGet("a", "b") -> return List[null, null]
    }

    public void deletePushToken(Long userId){
        String key = prefix + userId.toString();
        redisTemplate.delete(key);
        log.info("push토큰 삭제완료 - Id: " + userId);
    }



}
