package me.anmolgoyal.fileprocessor.listener;

import org.springframework.stereotype.Component;

import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalNotification;

@Component
public class FileRemovalListener implements RemovalListener{
	public void onRemoval(RemovalNotification notification) {
		System.out.println("FileInfo associated with the key("+
				notification.getKey()+ ") is removed.");
	}
}
