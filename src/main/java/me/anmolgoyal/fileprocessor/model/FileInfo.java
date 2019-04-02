package me.anmolgoyal.fileprocessor.model;

import java.nio.file.Path;

public class FileInfo {

	private Path fileName;
	private int wordsCount;
	private int vowelCount;
	private int specialCharCount;
	
	public FileInfo() {
		super();
		this.wordsCount = 0;
		this.vowelCount = 0;
		this.specialCharCount = 0;
	}
	
	public FileInfo(int wordsCount, int vowelCount, int specialCharCount) {
		super();
		this.wordsCount = wordsCount;
		this.vowelCount = vowelCount;
		this.specialCharCount = specialCharCount;
	}
	
	public int getWordsCount() {
		return wordsCount;
	}
	public void setWordsCount(int wordsCount) {
		this.wordsCount = wordsCount;
	}
	public int getVowelCount() {
		return vowelCount;
	}
	public void setVowelCount(int vowelCount) {
		this.vowelCount = vowelCount;
	}
	public int getSpecialCharCount() {
		return specialCharCount;
	}
	public void setSpecialCharCount(int specialCharCount) {
		this.specialCharCount = specialCharCount;
	}
	public Path getFileName() {
		return fileName;
	}

	public void setFileName(Path fileName) {
		this.fileName = fileName;
	}
	
	
}
