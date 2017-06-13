package com.responsyve.codegen.generator;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.Field;

import javax.lang.model.element.Modifier;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;

import com.responsyve.codegen.domain.CodeGenClass;
import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;

public class CodeWriter {

	private String genLocation;

	private CodeGenClass cgclass;

	TypeSpec classSpec, repoClassSpec;

	FieldSpec spec;

	VelocityEngine ve;

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
		addFieldToPOJOClass();
		writeFile(cgclass.getClassName(), classSpec);
	}

	public void generateRepository() throws IOException {
		createRepositoryClass();
	}
	
	public void generateService() throws IOException {
		createServiceClass();
	}

	public void writeFile(String className, TypeSpec spec) throws IOException {
		JavaFile javaFile = JavaFile.builder("com.restcrud.domain", spec).build();
		javaFile.writeTo(new File(genLocation));
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

		String className = path.substring(0, lastDot).trim();
		String fieldName = path.substring(lastDot + 1).trim();

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

	public void addFieldToPOJOClass() {

		cgclass.getFields().forEach(field -> {
			Class<?> cls;
			try {
				cls = Class.forName(field.getType());
				if (field.getAnnotations() == null) {
					classSpec = classSpec.toBuilder()
							.addField(FieldSpec.builder(cls, field.getName()).addModifiers(Modifier.PRIVATE).build())
							.build();
				} else {
					field.getAnnotations().forEach(annotation -> {
						Class<?> anncls;
						try {
							anncls = Class.forName(annotation.getAnnotationName());
							if (annotation.getAnnotationProperty() == null) {
								spec = FieldSpec.builder(cls, field.getName())
										.addAnnotation(AnnotationSpec.builder(cls).build())
										.addModifiers(Modifier.PRIVATE).build();
							} else {
								annotation.getAnnotationProperty().forEach(prop -> {
									if (prop.getName().indexOf(".") > 0) {
										String[] data = getFieldNameFromClass(prop.getName());
										try {
											Class<?> propcls = Class.forName(data[0]);
											spec = FieldSpec.builder(cls, field.getName())
													.addAnnotation(AnnotationSpec.builder(anncls)
															.addMember("value", "$T.$L", propcls, data[1]).build())
													.addModifiers(Modifier.PRIVATE).build();
										} catch (ClassNotFoundException e) {
											e.printStackTrace();
										}
									} else if (prop.getValue().indexOf(".") > 0) {
										String[] data = getFieldNameFromClass(prop.getValue());
										try {
											Class<?> propcls = Class.forName(data[0]);
											spec = FieldSpec.builder(cls, field.getName())
													.addAnnotation(AnnotationSpec.builder(anncls)
															.addMember(prop.getName(), "$T.$L", propcls, data[1])
															.build())
													.addModifiers(Modifier.PRIVATE).build();
										} catch (ClassNotFoundException e) {
											e.printStackTrace();
										}
									} else {
										spec = FieldSpec.builder(cls, field.getName())
												.addAnnotation(AnnotationSpec.builder(anncls)
														.addMember(prop.getName(), "$S", prop.getValue()).build())
												.addModifiers(Modifier.PRIVATE).build();
									}
								});
							}
						} catch (ClassNotFoundException e) {
							e.printStackTrace();
						}
					});

					classSpec = classSpec.toBuilder().addField(spec).build();
					spec = null;
				}
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		});
	}

	public void setupVelocityEngine() {
		ClassLoader classLoader = getClass().getClassLoader();
		File file = new File(classLoader.getResource("templates").getFile());

		ve = new VelocityEngine();
		ve.setProperty(RuntimeConstants.FILE_RESOURCE_LOADER_PATH, file.getAbsolutePath());
		ve.init();
	}

	public void createRepositoryClass() throws IOException {

		Template t = ve.getTemplate("repository.vm");
		VelocityContext vc = new VelocityContext();
		vc.put("package", "com.restcrud");
		vc.put("class", cgclass.getClassName());

		FileWriter fw = new FileWriter(new File(genLocation + File.separator + cgclass.getClassName() + "Repository.java"));
		t.merge(vc, fw);
	}
	
	public void createServiceClass() throws IOException {

		Template t = ve.getTemplate("service.vm");
		VelocityContext vc = new VelocityContext();
		vc.put("package", "com.restcrud");
		vc.put("class", cgclass.getClassName());

		FileWriter fw = new FileWriter(new File(genLocation + File.separator + cgclass.getClassName() + "Service.java"));
		t.merge(vc, fw);
	}
}
