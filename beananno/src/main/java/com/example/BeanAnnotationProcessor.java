package com.example;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.ElementFilter;
import javax.lang.model.util.Elements;

/**
 * Created by fqzhang on 2018/1/15.
 */
@SupportedAnnotationTypes("com.example.Seriable")
@SupportedSourceVersion(SourceVersion.RELEASE_7)
public class BeanAnnotationProcessor extends AbstractProcessor {
    public Elements elementUtils;
    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        elementUtils = processingEnvironment.getElementUtils();
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnv) {
        System.out.println("*******Seriable********");
        Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(Seriable.class);
        TypeElement classElement = null;//声明类元素
        List<VariableElement> fileds = new ArrayList<>();
        Map<String,List<VariableElement>> maps = new HashMap<>();
        for (Element element : elements) {
            if (element.getKind() == ElementKind.CLASS) {
                classElement = (TypeElement) element;
                maps.put(classElement.getQualifiedName().toString(),fileds = new ArrayList<VariableElement>());
            } else if (element.getKind() == ElementKind.FIELD) {
                TypeElement enclosingElement = (TypeElement) element.getEnclosingElement();
                String className = enclosingElement.getQualifiedName().toString();
                if( maps.get(className) == null) {
                    maps.put(className,fileds = new ArrayList<VariableElement>());
                }
                maps.get(className).add((VariableElement) element);
            }
        }
        for (String key : maps.keySet()) {
            if (maps.get(key).size() == 0){
                TypeElement typeElement = elementUtils.getTypeElement(key);
                List<? extends Element> allMembers = elementUtils.getAllMembers(typeElement);
                if (allMembers.size() > 0 ) {
                    maps.get(key).addAll(ElementFilter.fieldsIn(allMembers));
                }
            }
        }
        System.out.println("写入硬盘：");
        generateCodes(maps);
        System.out.println("已经写入完成");
        return true;
    }

    private void generateCodes(Map<String, List<VariableElement>> maps) {
        File dir = new File("E://annoTest");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        for (String key : maps.keySet()) {
            File file = new File(dir,key.replace("\\.","_")+".txt");

            try {
                FileWriter fw = new FileWriter(file);
                fw.append("{").append("class:").append("\"").append(key).append("\"");
                fw.append(",\n");
                fw.append("fields:\n{\n");
                List<VariableElement> variables = maps.get(key);
                for (VariableElement variable : variables) {
                    fw.append(" ").append(variable.getSimpleName().toString()).append(":").append("\"").append(variable.asType().toString()).append("\"");
                }
                fw.append("}\n");
                fw.append("}");
                fw.flush();
                fw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
