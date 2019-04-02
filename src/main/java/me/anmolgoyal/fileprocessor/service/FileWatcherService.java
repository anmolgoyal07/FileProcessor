package me.anmolgoyal.fileprocessor.service;

import java.nio.file.Path;

public interface FileWatcherService {

	public void registerDir(Path dir);
	
	public void processEvents();
}
