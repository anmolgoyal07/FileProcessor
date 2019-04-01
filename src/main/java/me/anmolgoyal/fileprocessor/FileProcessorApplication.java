package me.anmolgoyal.fileprocessor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import me.anmolgoyal.fileprocessor.service.DirectoryProcessService;

@SpringBootApplication
@ComponentScan(basePackages ="me.anmolgoyal.fileprocessor")
public class FileProcessorApplication implements CommandLineRunner {

	@Autowired
	private DirectoryProcessService directoryProcessService;
	
	public static void main(String[] args) {
		SpringApplication.run(FileProcessorApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		directoryProcessService.processRootDir("E:\\file_visit");
		
	}
	
	

}
