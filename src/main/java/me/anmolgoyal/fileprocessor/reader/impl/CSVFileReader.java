package me.anmolgoyal.fileprocessor.reader.impl;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import me.anmolgoyal.fileprocessor.reader.FileReader;

@Service("csvFileReader")
public class CSVFileReader implements FileReader {

	public List<String> readFile(Path filepath) {

		List<String> fileContent = new ArrayList<String>();
		try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filepath.toFile())))) {
			fileContent = br.lines().map(row -> row.replaceAll(",", " ")).collect(Collectors.toList());
		} catch (FileNotFoundException e) {
			fileContent = null;
			System.out.println("CSV File not found:: " + filepath);
		} catch (IOException e) {
			fileContent = null;
			System.out.println("Error while reading the csv file:: " + filepath);
		}
		return fileContent;
	}

}
