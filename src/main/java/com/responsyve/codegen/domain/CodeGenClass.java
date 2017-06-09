package com.responsyve.codegen.domain;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Data;

@Data
@XmlRootElement(name="class")
@XmlAccessorType(XmlAccessType.FIELD)
public class CodeGenClass {

	@XmlAttribute(name="name")
	private String className;
	
	@XmlElementWrapper(name="annotations")
	@XmlElement(name="annotation")
	private List <CodeGenAnnotation> annotations;
	
	@XmlElementWrapper(name="fields")
	@XmlElement(name="field")
	private List <CodeGenField> fields;
	
}
