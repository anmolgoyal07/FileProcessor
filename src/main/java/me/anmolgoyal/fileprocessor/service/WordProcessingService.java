package me.anmolgoyal.fileprocessor.service;

import java.util.List;

import me.anmolgoyal.fileprocessor.model.FileInfo;

public interface WordProcessingService {

	public FileInfo processFileContent(List<String> fileContent);
}
