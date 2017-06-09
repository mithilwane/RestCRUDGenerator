package com.responsyve.codegen;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLStreamException;

import com.responsyve.codegen.generator.CodeWriter;
import com.responsyve.codegen.generator.XMLFileParser;

/**
 * Hello world!
 *
 */
public class App {
	
	public static void main(String[] args) throws IOException {
		
		// String folder = args[0];
		String folder = "C:/Users/1370096/eworkspace/restcrudgenerator/testFiles";
		Files.list(Paths.get(folder)).forEach(file -> {
			CodeWriter writer = new CodeWriter(folder + File.separator + "generated");
			XMLFileParser parser = new XMLFileParser(file.toString(), writer);
			try {
				parser.parseFile();
				//parser.printFile();	
			} catch (JAXBException e) {
				e.printStackTrace();
			}
		});
		
	}
}
