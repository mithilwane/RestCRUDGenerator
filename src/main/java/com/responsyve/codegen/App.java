package com.responsyve.codegen;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLStreamException;

import com.responsyve.codegen.domain.CodeGenClass;
import com.responsyve.codegen.generator.CodeWriter;
import com.responsyve.codegen.generator.XMLFileParser;

/**
 * Hello world!
 *
 */
public class App {

	public static void main(String[] args) throws IOException, ClassNotFoundException {

		String inputfolder = "C:/Users/1370096/Documents/eworkspace/restcrudgenerator/testFiles";
		String outfolder = "C:/Users/1370096/Documents/eworkspace/restcrudgenerator/generated";
		Files.list(Paths.get(inputfolder)).forEach(file -> {
			CodeWriter writer = new CodeWriter(outfolder);
			XMLFileParser parser = new XMLFileParser(file.toString());
			try {
				CodeGenClass cgclass = parser.parseFile();
				writer.setCgclass(cgclass);
				writer.generatePojo();
				writer.setupVelocityEngine();
				writer.generateRepository();
				writer.generateService();
				//parser.printFile();	
			} catch (JAXBException | ClassNotFoundException | NoSuchFieldException | SecurityException | IOException e) {
				e.printStackTrace();
			}
		});
		
	}
}
