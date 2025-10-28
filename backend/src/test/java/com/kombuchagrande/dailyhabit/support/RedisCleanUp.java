package com.kombuchagrande.dailyhabit.support;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.stereotype.Component;

@Component
public class RedisCleanUp {

    @Autowired
    RedisConnectionFactory connectionFactory;

    public void flushAll() {
        try (var conn = connectionFactory.getConnection()) {
            conn.serverCommands().flushAll();
        }
    }
}