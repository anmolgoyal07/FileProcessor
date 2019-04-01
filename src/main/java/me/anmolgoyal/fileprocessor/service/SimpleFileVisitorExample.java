package me.anmolgoyal.fileprocessor.service;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.WatchService;
import java.nio.file.attribute.BasicFileAttributes;
/*
public class SimpleFileVisitor implements FileVisitor<Path>{

	
	private  WatchService watcher;
	
	@Override
	public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
		//register watcher
		//2. get the all file otxt & csv
		//3 iterte loop 
		//3.1 per -> wordprocessor (write into file return (words,vovels,))
		//
		System.out.println("preVisitDirectory "+dir);
		
		Files.newDirectoryStream(dir,
		        path -> {
		        String fileName	= path.toString();
		        return (fileName.endsWith(".txt")|| fileName.endsWith(".csv"));
		        }).
		        forEach(System.out::println);
		// TODO Auto-generated method stub
		return FileVisitResult.CONTINUE;
	}

	@Override
	public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
		// TODO Auto-generated method stub
		return FileVisitResult.CONTINUE;
	}

	@Override
	public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
		// TODO Auto-generated method stub
		return FileVisitResult.CONTINUE;
	}

	@Override
	public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
		// TODO Auto-generated method stub
		System.out.println("POST VISIT DIRECORY "+dir);
		return FileVisitResult.CONTINUE;
	}

}
*/
