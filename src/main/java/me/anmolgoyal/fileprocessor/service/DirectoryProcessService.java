package me.anmolgoyal.fileprocessor.service;

import java.nio.file.Path;

public interface DirectoryProcessService {

	/*
	 * Process the root & all child dir
	 */
	public void processRootDir(String dirPath);

	/*
	 * process all files in current directory
	 */
	public void processDirectory(Path dir);
}
