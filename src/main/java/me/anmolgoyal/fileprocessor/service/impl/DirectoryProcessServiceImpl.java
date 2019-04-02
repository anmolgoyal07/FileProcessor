package me.anmolgoyal.fileprocessor.service.impl;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import me.anmolgoyal.fileprocessor.exception.SystemException;
import me.anmolgoyal.fileprocessor.model.FileInfo;
import me.anmolgoyal.fileprocessor.reader.FileReader;
import me.anmolgoyal.fileprocessor.service.DirectoryProcessService;
import me.anmolgoyal.fileprocessor.service.FileProcessService;
import me.anmolgoyal.fileprocessor.service.FileWatcherService;
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
	@Qualifier("txtFileReader")
	private FileReader textReader;

	@Autowired
	private FileWatcherService fileWatcherService;
	/**
	 * To list out all the directory and invoke further logic 
	 */
	public void processRootDir(String dirPath) {
		List<Path> allDirectories = getAllDirectoryPath(Paths.get(dirPath));
		allDirectories.forEach(dirName -> processDirectory(dirName));
		fileWatcherService.processEvents();
	}
	
	/**
	 * Process directory and and invoke File processing logic.
	 */
	public void processDirectory(Path dir) {
		//fetching all files in a dir
		System.out.println("Starting processing dir: "+dir);
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
		fileWatcherService.registerDir(dir);
		System.out.println("Processing finish for dir: "+dir+"\n");
	}

	/**
	 * Read the new file & update the dir dmtd file
	 * @param dir
	 * @param file
	 */
	public void processNewFileInDir(Path dir, Path file) {
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


	/**
	 * To get All Files name of a dir
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

	/**
	 * 
	 * @param path
	 * @return
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
