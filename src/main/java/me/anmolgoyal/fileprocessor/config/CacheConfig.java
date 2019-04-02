package me.anmolgoyal.fileprocessor.config;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.guava.GuavaCache;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.cache.interceptor.CacheResolver;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.cache.interceptor.SimpleKeyGenerator;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.common.cache.CacheBuilder;

import me.anmolgoyal.fileprocessor.listener.FileRemovalListener;

@Configuration
@EnableCaching
public class CacheConfig implements CachingConfigurer {

    public final static String CACHE_ONE = "cacheOne";

    @Value("${ttl.time}")
    private Long ttlTime;
    
    private final Logger log = LoggerFactory
            .getLogger(CacheConfig.class);

   @Autowired
   FileRemovalListener fileRemovalListener; 
    
    @Bean
    @Override
    public CacheManager cacheManager() {
        log.info("Initializing simple Guava Cache manager.");
        SimpleCacheManager cacheManager = new SimpleCacheManager();

        GuavaCache cache1 = new GuavaCache(CACHE_ONE, CacheBuilder.newBuilder()
                .expireAfterWrite(ttlTime, TimeUnit.SECONDS).removalListener(fileRemovalListener)
                .build());

        cacheManager.setCaches(Arrays.asList(cache1));
        return cacheManager;
    }
    
    @Bean
    public Cache cache(CacheManager cacheManager) {
    	return cacheManager.getCache(CACHE_ONE);
    }

    @Override
    public CacheResolver cacheResolver() {
        return null;
    }

    @Bean
    @Override
    public KeyGenerator keyGenerator() {
        return new SimpleKeyGenerator();
    }

    @Override
    public CacheErrorHandler errorHandler() {
        return null;
    }
}
