package com.responsyve.codegen.domain;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;

import lombok.Data;

@Data
@XmlRootElement(name="property")
@XmlAccessorType(XmlAccessType.FIELD)
public class CodeGenProperty {
	
	@XmlAttribute(name="name")
	private String name;
	
	@XmlAttribute(name="type")
	private String type;
	
	@XmlValue
	private String value;
	
}
