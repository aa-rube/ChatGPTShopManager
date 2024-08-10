package app.manager.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Configuration
public class MyJedisPoolConfig {

    @Bean
    public JedisPool jedisPool(RedisConfig redisConfig) {
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        return new JedisPool(poolConfig, redisConfig.getHost(), redisConfig.getPort(), redisConfig.getTimeout());
    }
}