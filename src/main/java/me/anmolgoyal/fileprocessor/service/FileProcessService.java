package me.anmolgoyal.fileprocessor.service;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import me.anmolgoyal.fileprocessor.model.FileInfo;
import me.anmolgoyal.fileprocessor.reader.FileReader;
import me.anmolgoyal.fileprocessor.reader.impl.CSVFileReader;
import me.anmolgoyal.fileprocessor.reader.impl.TextFileReader;
import me.anmolgoyal.fileprocessor.writer.FileWriter;

@Service
public class FileProcessService {

	@Autowired
	@Qualifier("txtFileReader")
	private FileReader textReader;

	@Autowired
	@Qualifier("csvFileReader")
	private CSVFileReader csvReader;
	
	@Autowired
	@Qualifier("mtdFileWriter")
	FileWriter mtdFileWriter;
	

	@Autowired
	private WordProcessingService wordProcessingService;

	public FileInfo processFile(Path filepath) {
		List<String> fileContent;
		FileInfo fileInfo = null;
		if (filepath.endsWith("txt")) {
			fileContent = textReader.readFile(filepath);
		} else {
			fileContent = csvReader.readFile(filepath);
		}
		// if any error occur while reading file just skipping for that file
		if (fileContent == null) {
			return fileInfo;
		}

		fileInfo = wordProcessingService.processFileContent(fileContent);
		String fileName = filepath.toString();
		fileName = fileName.substring(0, fileName.lastIndexOf('.')+1)+"mtd";
		fileInfo.setFileName(Paths.get(fileName));
		mtdFileWriter.writeFile(fileInfo);
		return fileInfo;

	}

}
