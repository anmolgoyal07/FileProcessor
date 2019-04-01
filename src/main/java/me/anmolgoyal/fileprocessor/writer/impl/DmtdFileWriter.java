package me.anmolgoyal.fileprocessor.writer.impl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.springframework.stereotype.Service;

import me.anmolgoyal.fileprocessor.exception.SystemException;
import me.anmolgoyal.fileprocessor.model.FileInfo;
import me.anmolgoyal.fileprocessor.writer.FileWriter;

@Service("dmtdFileWriter")
public class DmtdFileWriter implements FileWriter {

	
	@Override
	public void writeFile(FileInfo fileInfo) {
		String content = String.format("words:%d speicalChar:%d vowels:%d", fileInfo.getWordsCount(),
				fileInfo.getSpecialCharCount(), fileInfo.getVowelCount());
		String fileName = fileInfo.getFileName().toString();
		File file = new File(fileName);
		if(file.exists()) {
			file.delete();
		}
		try (BufferedWriter writer = Files.newBufferedWriter(fileInfo.getFileName())) {
			writer.write(content);
		} catch (IOException e) {
			throw new SystemException(String.format("Error while writing the file::%s", fileInfo.getFileName()), e);
		}
	}

}
