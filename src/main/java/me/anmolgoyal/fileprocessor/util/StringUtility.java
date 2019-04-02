package me.anmolgoyal.fileprocessor.util;

import me.anmolgoyal.fileprocessor.model.FileInfo;

public class StringUtility {

	public static FileInfo decode(String str) {
		FileInfo fileInfo = null;
		int wordsCount = 0, vowelCount = 0, specialCharCount = 0;
		String[] strArr = str.split(" ");
		if (strArr.length == 3) {
			// decoding words count
			String[] keyValue = strArr[0].split(":");
			if (keyValue.length == 2) {
				wordsCount = Integer.parseInt(keyValue[1]);
			}
			// decoding special char count
			keyValue = strArr[1].split(":");
			if (keyValue.length == 2) {
				specialCharCount = Integer.parseInt(keyValue[1]);
			}
			// decoding vowel count
			keyValue = strArr[2].split(":");
			if (keyValue.length == 2) {
				vowelCount = Integer.parseInt(keyValue[1]);
			}
			fileInfo = new FileInfo(wordsCount, vowelCount, specialCharCount);
		}
		return fileInfo;

	}
}
