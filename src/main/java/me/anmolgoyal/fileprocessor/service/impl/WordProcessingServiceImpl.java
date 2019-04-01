package me.anmolgoyal.fileprocessor.service.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import me.anmolgoyal.fileprocessor.model.FileInfo;
import me.anmolgoyal.fileprocessor.service.WordProcessingService;

@Service
public class WordProcessingServiceImpl implements WordProcessingService {

	@SuppressWarnings("serial")
	private final Set<Character> VOWELS = new HashSet<Character>() {
		{
			add('a');
			add('e');
			add('i');
			add('o');
			add('u');
		}
	};

	@SuppressWarnings("serial")
	private final Set<Character> SPECIAL_CHARS = new HashSet<Character>() {
		{
			add('@');
			add('#');
			add('$');
			add('*');
		}
	};

	public FileInfo processFileContent(List<String> fileContent) {
		int wordsCount = 0, vowelCount = 0, specialCharCount = 0;
		for (String line : fileContent) {
			wordsCount += line.split(" ").length;
			for (Character character : line.toCharArray()) {
				if (VOWELS.contains(character)) {
					++vowelCount;
				}
				if (SPECIAL_CHARS.contains(character)) {
					++specialCharCount;
				}
			}
		}
		return new FileInfo(wordsCount, vowelCount, specialCharCount);
	}
}
