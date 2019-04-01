package me.anmolgoyal.fileprocessor.reader;

import java.nio.file.Path;
import java.util.List;

public interface FileReader {

	List<String> readFile(Path fileName);
}
