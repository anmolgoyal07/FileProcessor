package me.anmolgoyal.fileprocessor.listener;

import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalNotification;

import me.anmolgoyal.fileprocessor.service.FileProcessService;

@Component
public class FileRemovalListener<K, V> implements RemovalListener<K, V> {

	@Autowired
	private FileProcessService fileProcessService;

	public void onRemoval(RemovalNotification<K, V> notification) {
		
		boolean isModified = fileProcessService.verifyFileAfterRemove(Paths.get(notification.getKey().toString()),
				Long.parseLong(notification.getValue().toString()));
		
		if (!isModified) {
			System.out.println("File removed successfully from cache" + notification.getKey());
		}

	}
}
