package me.anmolgoyal.fileprocessor.service.impl;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import me.anmolgoyal.fileprocessor.exception.SystemException;
import me.anmolgoyal.fileprocessor.service.DirectoryProcessService;
import me.anmolgoyal.fileprocessor.service.FileWatcherService;

@Service
public class FileWatcherServiceImpl implements FileWatcherService {

	@Autowired
	private WatchService watcher;
	
	@Autowired
	private DirectoryProcessService directoryProcessService;

	private final Map<WatchKey, Path> watcherKeys = new HashMap<WatchKey, Path>();;

	public void registerDir(Path dir) {
		// registering watcher on a dir path
		WatchKey key;
		try {
			key = dir.register(watcher, ENTRY_CREATE);
		} catch (IOException e) {
			throw new SystemException("Unable to register watcher on dir:: " + dir, e);
		}
		watcherKeys.put(key, dir);

	}

	/*
	 * Process event if new file is created
	 */
	public void processEvents() {
		while (true) {
			// wait for key to be signalled
			WatchKey key;
			try {
				// blocking action
				key = watcher.take();
			} catch (InterruptedException ex) {
				throw new SystemException("Got interupted while getiing event from watcher", ex);
			}

			Path dir = watcherKeys.get(key);
			if (dir == null) {
				System.err.println("WatchKey not recognized!!");
				continue;
			}

			for (WatchEvent<?> event : key.pollEvents()) {
				@SuppressWarnings("rawtypes")
				WatchEvent.Kind kind = event.kind();

				// Context for directory entry event is the file name of entry
				@SuppressWarnings("unchecked")
				Path name = ((WatchEvent<Path>) event).context();
				Path child = dir.resolve(name);

				// if directory is created, and watching recursively, then register it and its
				// sub-directories
				if (kind == ENTRY_CREATE) {
					if (Files.isRegularFile(child)
							&& (child.toString().endsWith("txt") || child.toString().endsWith("csv"))) {
						// print out event
						System.out.format("%s: %s\n", event.kind().name(), child);
						directoryProcessService.processNewFileInDir(dir, child);
					}
				}
			}

			// reset key and remove from set if directory no longer accessible
			boolean valid = key.reset();
			if (!valid) {
				watcherKeys.remove(key);

				// all directories are inaccessible
				if (watcherKeys.isEmpty()) {
					break;
				}
			}
		}
	}
}
