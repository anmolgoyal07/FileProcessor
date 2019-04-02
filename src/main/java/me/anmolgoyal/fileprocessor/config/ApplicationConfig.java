package me.anmolgoyal.fileprocessor.config;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.WatchService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import me.anmolgoyal.fileprocessor.exception.SystemException;

@Configuration
public class ApplicationConfig {
	
	@Bean
	public WatchService watchService() {
		try {
			return FileSystems.getDefault().newWatchService();
		} catch (IOException e) {
			throw new SystemException("Unable to intialize watcher service", e);
		}
	}
	
}
