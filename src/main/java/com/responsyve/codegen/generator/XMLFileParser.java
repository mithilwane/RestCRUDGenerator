package com.responsyve.codegen.generator;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import com.responsyve.codegen.domain.CodeGenAnnotation;
import com.responsyve.codegen.domain.CodeGenClass;
import com.responsyve.codegen.domain.CodeGenField;
import com.responsyve.codegen.domain.CodeGenProperty;

public class XMLFileParser {

	private String fileLocation;

	private CodeWriter writer;

	public XMLFileParser(String fileLocation, CodeWriter writer) {
		this.fileLocation = fileLocation;
		this.writer = writer;
	}
	
	//Testing
	public void printFile() throws JAXBException {
		JAXBContext jb = JAXBContext.newInstance(CodeGenClass.class);
		Marshaller um = jb.createMarshaller();
		
		
		CodeGenField field = new CodeGenField();
		field.setName("date");
		field.setType("Date");
		
		List<CodeGenAnnotation> fannotations = new ArrayList<>();
				
		CodeGenAnnotation fannotation1 = new CodeGenAnnotation();
		fannotation1.setAnnotationName("CreatedDate");
		
		fannotations.add(fannotation1);
		
		CodeGenAnnotation fannotation2 = new CodeGenAnnotation();
		fannotation2.setAnnotationName("TemporalType");
		
		CodeGenProperty fanno2prop = new CodeGenProperty();
		fanno2prop.setName("TemporalType.TIMESTAMP");
		
		List<CodeGenProperty> fprops = new ArrayList<>();
		fprops.add(fanno2prop);
		
		fannotation2.setAnnotationProperty(fprops);		
		
		fannotations.add(fannotation2);
		
		field.setAnnotations(fannotations);	
		
		List<CodeGenAnnotation> cannotations = new ArrayList<>();
		
		CodeGenAnnotation cannotation1 = new CodeGenAnnotation();
		cannotation1.setAnnotationName("Data");
		cannotations.add(cannotation1);
		
		CodeGenAnnotation cannotation2 = new CodeGenAnnotation();
		cannotation2.setAnnotationName("MappedSuperClass");
		cannotations.add(cannotation2);
		
		CodeGenAnnotation cannotation3 = new CodeGenAnnotation();
		cannotation3.setAnnotationName("Inheritance");
				
		CodeGenProperty canno3prop = new CodeGenProperty();
		canno3prop.setName("strategy");
		canno3prop.setValue("InheritanceType.TABLE_PER_CLASS");
		
		List<CodeGenProperty> cprops = new ArrayList<>();
		cprops.add(canno3prop);
				
		cannotation3.setAnnotationProperty(cprops);
		
		cannotations.add(cannotation3);
		
		List<CodeGenField> fields = new ArrayList<>();
		fields.add(field);
		
		CodeGenClass cgclass = new CodeGenClass();
		cgclass.setClassName("MyClass");
		cgclass.setAnnotations(cannotations);
		cgclass.setFields(fields);
		
		um.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        um.marshal(cgclass, System.out);
	}
	
	public void parseFile() throws JAXBException {
		
		JAXBContext jb = JAXBContext.newInstance(CodeGenClass.class);
		Unmarshaller um = jb.createUnmarshaller();
		CodeGenClass genClass = (CodeGenClass) um.unmarshal(new File(fileLocation));
		
		writer.setCgclass(genClass);
		try {
			writer.generatePojo();
		} catch (ClassNotFoundException | NoSuchFieldException | SecurityException | IOException e) {
			e.printStackTrace();
		}
	}
}
