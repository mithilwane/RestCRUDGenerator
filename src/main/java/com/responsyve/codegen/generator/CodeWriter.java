package com.responsyve.codegen.generator;

import java.io.IOException;
import java.lang.reflect.Field;
import javax.lang.model.element.Modifier;

import com.responsyve.codegen.domain.CodeGenClass;
import com.responsyve.codegen.domain.CodeGenField;
import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;

public class CodeWriter {

	private String genLocation;

	private CodeGenClass cgclass;

	TypeSpec classSpec;

	public CodeGenClass getCgclass() {
		return cgclass;
	}

	public void setCgclass(CodeGenClass cgclass) {
		this.cgclass = cgclass;
	}

	public CodeWriter(String location) {
		this.genLocation = location;
	}

	public void generatePojo() throws ClassNotFoundException, NoSuchFieldException, SecurityException, IOException {
		createPOJOClass();
		addAnnotationsToPOJOClass();
		writeFile();
	}

	public void writeFile() throws IOException {
		JavaFile javaFile = JavaFile.builder("com.mithil.test", classSpec).build();
		javaFile.writeTo(System.out);
	}

	public void createPOJOClass() {
		classSpec = TypeSpec.classBuilder(cgclass.getClassName()).addModifiers(Modifier.PUBLIC).build();
	}

	public void addAnnotationsToPOJOClass() throws ClassNotFoundException, NoSuchFieldException, SecurityException {
		cgclass.getAnnotations().forEach(annotation -> {
			Class<?> cls;
			try {
				cls = Class.forName(annotation.getAnnotationName());
				if (annotation.getAnnotationProperty() == null) {
					classSpec = classSpec.toBuilder().addAnnotation(AnnotationSpec.builder(cls).build()).build();
				} else {
					annotation.getAnnotationProperty().forEach(prop -> {
						String[] data = getFieldNameFromClass(prop.getValue());
						try {
							Class<?> propcls = Class.forName(data[0]);
							classSpec = classSpec.toBuilder().addAnnotation(AnnotationSpec.builder(cls)
									.addMember(prop.getName(), "$T.$L", propcls, data[1]).build()).build();
						} catch (ClassNotFoundException e) {
							e.printStackTrace();
						}
					});
				}
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		});
	}

	private String[] getFieldNameFromClass(String path) {
		String[] data = new String[2];
		int lastDot = path.lastIndexOf(".");

		String className = path.substring(0, lastDot);
		String fieldName = path.substring(lastDot + 1);

		Class myClass;
		Field myField;
		try {
			myClass = Class.forName(className);
			data[0] = myClass.getName();
			myField = myClass.getDeclaredField(fieldName);
			data[1] = myField.getName();
		} catch (ClassNotFoundException | NoSuchFieldException | SecurityException e) {
			e.printStackTrace();
		}

		return data;
	}

	public void addFieldToPOJOClass(CodeGenField field) {

	}
}
