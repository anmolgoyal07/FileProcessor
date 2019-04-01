package me.anmolgoyal.fileprocessor.service;

import java.nio.file.Path;

import me.anmolgoyal.fileprocessor.model.FileInfo;

public interface FileProcessService {

	/*
	 * it read the file , process it , write the mtd file
	 */
	public FileInfo processFile(Path filepath);
	
}
