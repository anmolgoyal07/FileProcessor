package me.anmolgoyal.fileprocessor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import me.anmolgoyal.fileprocessor.reader.impl.CSVFileReader;
import me.anmolgoyal.fileprocessor.reader.impl.TextFileReader;

public class Program {
 public static void main(String...st) throws FileNotFoundException, IOException {
	 //List<String> inputList = new CsvReader().readFile(Paths.get("E:\\\\file_visit\\\\java\\\\data.csv"));// processInputFile("E:\\file_visit\\java\\data.csv");
//	 List<String> inputList = new TextFileReader().readFile(Paths.get("E:\\file_visit\\java\\hibernate\\CACHE.txt"));// processInputFile("E:\\file_visit\\java\\data.csv");
//	 System.out.println(inputList);
	 
	 String fileName = "abc.txt";
		fileName = fileName.substring(0, fileName.lastIndexOf('.')+1)+"mtd";
		System.out.println(fileName);
 }
 

private static List<String> processInputFile(String inputFilePath) {
    List<String> inputList = new ArrayList<String>();
    try{
      File inputF = new File(inputFilePath);
      InputStream inputFS = new FileInputStream(inputF);
      BufferedReader br = new BufferedReader(new InputStreamReader(inputFS));
      // skip the header of the csv
      inputList = br.lines().map(x->x.replaceAll(",", " ")).collect(Collectors.toList());
      br.close();
    } catch (IOException e) {
    }
    return inputList ;
}
}
