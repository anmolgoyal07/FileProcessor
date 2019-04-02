package me.anmolgoyal.fileprocessor.service.impl;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import me.anmolgoyal.fileprocessor.exception.SystemException;
import me.anmolgoyal.fileprocessor.model.FileInfo;
import me.anmolgoyal.fileprocessor.reader.FileReader;
import me.anmolgoyal.fileprocessor.service.DirectoryProcessService;
import me.anmolgoyal.fileprocessor.service.FileProcessService;
import me.anmolgoyal.fileprocessor.util.StringUtility;
import me.anmolgoyal.fileprocessor.writer.FileWriter;

@Service
public class DirectoryProcessServiceImpl implements DirectoryProcessService {

	@Autowired
	private FileProcessService fileProcessingService;

	@Autowired
	@Qualifier("dmtdFileWriter")
	private FileWriter dmtdFileWriter;

	@Autowired
	private WatchService watcher;
	
	@Autowired
	@Qualifier("txtFileReader")
	private FileReader textReader;

	private final Map<WatchKey, Path> watcherKeys = new HashMap<WatchKey, Path>();;

	/**
	 * To list out all the directory and invoke further logic 
	 */
	public void processRootDir(String dirPath) {
		List<Path> allDirectories = getAllDirectoryPath(Paths.get(dirPath));
		allDirectories.forEach(dirName -> processDirectory(dirName));
		processEvents();
	}
	
	/**
	 * Process directory and and invoke File processing logic.
	 */
	public void processDirectory(Path dir) {
		//fetching all files in a dir
		List<Path> allFiles = getAllFilesInDir(dir);
		int wordsCount = 0, vowelCount = 0, specialCharCount = 0;
		FileInfo fileInfo;
		for (Path filepath : allFiles) {
			
			fileInfo = fileProcessingService.processFile(filepath);
			
			if (fileInfo != null) {
				wordsCount += fileInfo.getWordsCount();
				vowelCount += fileInfo.getVowelCount();
				specialCharCount += fileInfo.getSpecialCharCount();
			}
			
		}
		fileInfo = new FileInfo(wordsCount, vowelCount, specialCharCount);
		String dirDMTDFile = dir.resolve(dir.getFileName()).toString() + ".dmtd";
		fileInfo.setFileName(Paths.get(dirDMTDFile));
		dmtdFileWriter.writeFile(fileInfo);

		//registering watcher on a dir path
		WatchKey key;
		try {
			key = dir.register(watcher, ENTRY_CREATE);
		} catch (IOException e) {
			throw new SystemException("Unable to register watcher on dir:: "+dir, e);
		}
		watcherKeys.put(key, dir);
	}

	/**
	 * Read the new file & update the dir dmtd file
	 * @param dir
	 * @param file
	 */
	private void processNewFileInDir(Path dir, Path file) {
		FileInfo fileInfo = fileProcessingService.processFile(file);
		
		String fileName = dir.resolve(dir.getFileName()).toString() + ".dmtd";
		List<String> fileContent = textReader.readFile(Paths.get(fileName));
		FileInfo dirFileInfo =  new FileInfo();
		if(!fileContent.isEmpty()) {
			dirFileInfo = StringUtility.decode(fileContent.get(0));
		}
		dirFileInfo.setSpecialCharCount(dirFileInfo.getSpecialCharCount() + fileInfo.getSpecialCharCount());
		dirFileInfo.setVowelCount(dirFileInfo.getVowelCount() + fileInfo.getVowelCount());
		dirFileInfo.setWordsCount(dirFileInfo.getWordsCount() + fileInfo.getWordsCount());
		dirFileInfo.setFileName(Paths.get(fileName) );
		dmtdFileWriter.writeFile(dirFileInfo);
	}


	/*
	 * Process event if new file is created
	 */
	private void processEvents() {
		while (true) {
			// wait for key to be signalled
			WatchKey key;
			try {
				//blocking action
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
				Path name = ((WatchEvent<Path>)event).context();
				Path child = dir.resolve(name);

				// if directory is created, and watching recursively, then register it and its sub-directories
				if (kind == ENTRY_CREATE) {
					if (Files.isRegularFile(child) &&( child.toString().endsWith("txt") || child.toString().endsWith("csv") ) ) {
						// print out event
						System.out.format("%s: %s\n", event.kind().name(), child);
						processNewFileInDir(dir, child);
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

	/**
	 * Get All Files name of a dir
	 * 
	 * @param dir
	 * @return
	 */
	private List<Path> getAllFilesInDir(Path dir) {
		List<Path> fileNames = new ArrayList<>();
		try {
			Files.newDirectoryStream(dir, path -> {
				String fileName = path.toString();
				return (fileName.endsWith(".txt") || fileName.endsWith(".csv"));
			}).forEach(path -> fileNames.add(path));
		} catch (Exception e) {
			throw new SystemException("Error while reading file from directory:: " + dir, e);
		}
		return fileNames;
	}

	/*
	 * Get All Directory Names
	 */
	private List<Path> getAllDirectoryPath(Path path) {

		List<Path> dirNames = new ArrayList<>();
		try {
			Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
				@Override
				public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
					dirNames.add(dir);
					return FileVisitResult.CONTINUE;
				}
			});
		} catch (IOException e) {
			throw new SystemException("Error while reading all directories from path:: " + path, e);
		}
		return dirNames;
	}

}
