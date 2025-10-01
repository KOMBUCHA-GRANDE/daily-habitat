package com.kombuchagrande.dailyhabit.security.session;

import com.kombuchagrande.dailyhabit.support.RedisCleanUp;
import com.kombuchagrande.dailyhabit.support.RedisTCBase;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.test.context.ActiveProfiles;

import java.time.Duration;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ActiveProfiles("test")
@Import({RedisTokenStore.class, RedisCleanUp.class})
class RedisTokenStoreTest extends RedisTCBase {

    @Autowired
    RedisTokenStore store;

    @Autowired
    RedisConnectionFactory connectionFactory;

    @Autowired
    RedisCleanUp clean;

    @BeforeEach
    void setUp() {
        // 매 테스트 전에 Redis 전체 비우기 (컨테이너 살아있으니 꼭 해줌)
        clean.flushAll();
    }


    @Nested
    @DisplayName("Refresh Session 관리")
    class RefreshSessionTest {

        @Test
        @DisplayName("세션 저장 후 uid 확인 가능해야 한다")
        void saveRefreshSession_thenRetrieve() {
            store.saveRefreshSession("tid-1", 101L, Duration.ofMinutes(5));

            assertThat(store.isRefreshSessionActive("tid-1", 101L)).isTrue();
        }

        @Test
        @DisplayName("세션 삭제 후 더 이상 조회되지 않아야 한다")
        void deleteRefreshSession_thenNotActive() {
            store.saveRefreshSession("tid-2", 102L, Duration.ofMinutes(5));
            store.deleteRefreshSession("tid-2", 102L);

            assertThat(store.isRefreshSessionActive("tid-2", 102L)).isFalse();
        }

        @Test
        @DisplayName("사용자의 모든 세션을 삭제할 수 있어야 한다")
        void deleteAllSessionsByUser_thenAllInactive() {
            store.saveRefreshSession("tid-3a", 103L, Duration.ofMinutes(5));
            store.saveRefreshSession("tid-3b", 103L, Duration.ofMinutes(5));

            int deleted = store.deleteAllSessionsByUser(103L);

            assertThat(deleted).isEqualTo(2);
            assertThat(store.isRefreshSessionActive("tid-3a", 103L)).isFalse();
            assertThat(store.isRefreshSessionActive("tid-3b", 103L)).isFalse();
        }
    }

    @Nested
    @DisplayName("Index 관리")
    class IndexTest {

        @Test
        @DisplayName("인덱스에 tid 추가/삭제 가능해야 한다")
        void addAndRemoveIndex() {
            store.addIndex(200L, "tid-200a");
            store.addIndex(200L, "tid-200b");

            // 삭제 후 남은 tid만 조회 가능해야 함
            store.removeIndex(200L, "tid-200a");
            store.saveRefreshSession("tid-200b", 200L, Duration.ofMinutes(5));

            int deleted = store.deleteAllSessionsByUser(200L);

            assertThat(deleted).isEqualTo(1);
            assertThat(store.isRefreshSessionActive("tid-200b", 200L)).isFalse();
        }
    }

    @Nested
    @DisplayName("Cutoff 관리")
    class CutoffTest {

        @Test
        @DisplayName("유저 cutoff 시간을 저장/조회할 수 있어야 한다")
        void setAndGetUserCutoff() {
            store.setUserCutoff(300L, 12345L);

            assertThat(store.getUserCutoff(300L)).isEqualTo(12345L);
        }

        @Test
        @DisplayName("디바이스 cutoff 시간을 TTL과 함께 저장/조회할 수 있어야 한다")
        void setAndGetDeviceCutoff() {
            store.setDeviceCutoff("tid-400", 67890L, Duration.ofSeconds(30));

            assertThat(store.getDeviceCutoff("tid-400")).isEqualTo(67890L);
        }
    }
}