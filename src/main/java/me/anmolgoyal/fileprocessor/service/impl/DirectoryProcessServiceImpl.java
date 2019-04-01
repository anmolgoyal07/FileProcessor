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
import me.anmolgoyal.fileprocessor.service.DirectoryProcessService;
import me.anmolgoyal.fileprocessor.service.FileProcessService;
import me.anmolgoyal.fileprocessor.writer.FileWriter;

@Service
public class DirectoryProcessServiceImpl implements DirectoryProcessService {

	@Autowired
	private FileProcessService fileProcessingService;

	@Autowired
	@Qualifier("dmtdFileWriter")
	private FileWriter dmtdFileWriter;

	/*
	 * 1. Dir input 2. Read dir 2.1 if file than process csv & txt file 2.1.1 get
	 * the list of in that folder 2.1.2 read,object(list<string) ,write 2.2 if dir
	 * go to point 2
	 */
	public void processRootDir(String dirPath) {
		List<Path> dirNames = getAllDirectoryPath(Paths.get(dirPath));
		dirNames.forEach(dirName -> processDirectory(dirName));
	}

	public void processDirectory(Path dir) {

		List<Path> fileNames = getAllFilesInDir(dir);

		int wordsCount = 0, vowelCount = 0, specialCharCount = 0;
		FileInfo fileInfo;
		for (Path filepath : fileNames) {
			fileInfo = fileProcessingService.processFile(filepath);
			wordsCount += fileInfo.getWordsCount();
			vowelCount += fileInfo.getVowelCount();
			specialCharCount += fileInfo.getSpecialCharCount();
		}

	}

	/**
	 * Get All Files name in a dir
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
