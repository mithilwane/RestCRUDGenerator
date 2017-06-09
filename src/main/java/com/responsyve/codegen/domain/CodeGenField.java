package com.responsyve.codegen.domain;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Data;

@Data
@XmlRootElement(name="field")
@XmlAccessorType(XmlAccessType.FIELD)
public class CodeGenField {
	
	@XmlAttribute(name="name")
	private String name;
	
	@XmlAttribute(name="type")
	private String type;
	
	@XmlElement(name="annotation")
	private List <CodeGenAnnotation> annotations;
}
