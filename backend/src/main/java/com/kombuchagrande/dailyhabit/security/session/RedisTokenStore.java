package com.kombuchagrande.dailyhabit.security.session;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class RedisTokenStore {

    private final StringRedisTemplate redis;

    // rt:{tid} -> uid  (TTL = refresh 만료)
    public void saveRefreshSession(String tid, Long uid, Duration ttl) {
        String key = keyRt(tid);
        redis.opsForValue().set(key, String.valueOf(uid), ttl);
        addIndex(uid, tid); // 인덱스 갱신
    }

    public boolean isRefreshSessionActive(String tid, Long uid) {
        String stored = redis.opsForValue().get(keyRt(tid));
        return stored != null && stored.equals(String.valueOf(uid));
    }

    // 단일 세션 삭제 (로그아웃)
    public void deleteRefreshSession(String tid, Long uid) {
        redis.delete(keyRt(tid));
        removeIndex(uid, tid); // 인덱스 갱신
    }

    // 모든 세션 삭제 (전체 로그아웃)
    public int deleteAllSessionsByUser(Long uid) {
        Set<String> tids = redis.opsForSet().members(keyRtIdx(uid));
        int count = 0;
        if (tids != null && !tids.isEmpty()) {
            List<String> keys = new ArrayList<>(tids.size());
            for (String tid : tids) {
                keys.add(keyRt(tid));
            }
            redis.delete(keys);
            count = tids.size();
        }
        redis.delete(keyRtIdx(uid)); // 인덱스 제거
        return count;
    }

    // ===== 인덱스 조작 =====
    /** 유저별 인덱스에 tid 추가 */
    public void addIndex(Long uid, String tid) {
        redis.opsForSet().add(keyRtIdx(uid), tid);
    }

    /** 유저별 인덱스에서 tid 제거 */
    public void removeIndex(Long uid, String tid) {
        redis.opsForSet().remove(keyRtIdx(uid), tid);
    }

    // ===== cutoff =====
    // cutoff:{uid} -> epochSec (전체 로그아웃 커트오프)
    public void setUserCutoff(Long uid, long epochSec) {
        redis.opsForValue().set(keyCutoff(uid), String.valueOf(epochSec));
    }

    public Long getUserCutoff(Long uid) {
        String v = redis.opsForValue().get(keyCutoff(uid));
        return v == null ? null : Long.parseLong(v);
    }

    // device_cutoff:{tid} -> epochSec (단일 기기 컷오프, TTL로 자동 청소)
    public void setDeviceCutoff(String tid, long epochSec, Duration ttl) {
        String key = keyDeviceCutoff(tid);
        redis.opsForValue().set(key, String.valueOf(epochSec), ttl);
    }

    public Long getDeviceCutoff(String tid) {
        String v = redis.opsForValue().get(keyDeviceCutoff(tid));
        return v == null ? null : Long.parseLong(v);
    }

    // ===== keys =====
    private static String keyRt(String tid)            { return "rt:" + tid; }
    private static String keyRtIdx(Long uid)           { return "rtidx:" + uid; }
    private static String keyCutoff(Long uid)          { return "cutoff:" + uid; }
    private static String keyDeviceCutoff(String tid)  { return "device_cutoff:" + tid; }
}