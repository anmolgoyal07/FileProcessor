package me.anmolgoyal.fileprocessor.service.impl;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.Cache;
import org.springframework.stereotype.Service;

import me.anmolgoyal.fileprocessor.model.FileInfo;
import me.anmolgoyal.fileprocessor.reader.FileReader;
import me.anmolgoyal.fileprocessor.reader.impl.CSVFileReader;
import me.anmolgoyal.fileprocessor.service.FileProcessService;
import me.anmolgoyal.fileprocessor.service.WordProcessingService;
import me.anmolgoyal.fileprocessor.writer.FileWriter;

@Service
public class FileProcessServiceImpl implements FileProcessService {

	@Autowired
	@Qualifier("txtFileReader")
	private FileReader textReader;

	@Autowired
	@Qualifier("csvFileReader")
	private FileReader csvReader;

	@Autowired
	@Qualifier("mtdFileWriter")
	FileWriter mtdFileWriter;
	
	@Autowired
	private Cache cache;

	@Autowired
	private WordProcessingService wordProcessingService;

	public FileInfo processFile(Path filepath) {
		
		if(cache.get(filepath.toString()) != null) {
			System.out.println("  File already processed : "+filepath);
			return null;
		}
		System.out.println("  Starting processing file : "+filepath);
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
		String mtdFileName = filepath.toString();
		mtdFileName = mtdFileName.substring(0, mtdFileName.lastIndexOf('.') + 1) + "mtd";
		fileInfo.setFileName(Paths.get(mtdFileName));
		mtdFileWriter.writeFile(fileInfo);
		
		//making entry in cache
		cache.put(filepath.toString(), filepath.toFile().lastModified());
		System.out.println("  File processed successfully : "+filepath);
		return fileInfo;

	}

}
