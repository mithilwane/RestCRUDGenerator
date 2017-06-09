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
@XmlRootElement(name="annotation")
@XmlAccessorType(XmlAccessType.FIELD)
public class CodeGenAnnotation {
	
	@XmlAttribute(name="name")
	private String annotationName;
	
	@XmlElement(name="property")
	private List<CodeGenProperty> annotationProperty;
	
}
