package com.example;

import java.util.List;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.tools.Diagnostic;

/**
 * Created by fqzhang on 2018/1/15.
 */
@SupportedAnnotationTypes("com.example.InjectPrint")//参数是指定注解类型的全路径
public class InjectPrintProcessor extends AbstractProcessor {
    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        //获取InjectPrint类型注解，然后遍历
        for(Element element : roundEnvironment.getElementsAnnotatedWith(InjectPrint.class)){
            //元素类型是一个方法
            if(element.getKind() == ElementKind.METHOD){
                //强转成方法对应的element，同
                // 理，如果你的注解是一个类，那你可以强转成TypeElement
                ExecutableElement executableElement = (ExecutableElement)element;
                //打印方法名
                System.out.println(executableElement.getSimpleName());
                //打印方法的返回类型
                System.out.println(executableElement.getReturnType().toString());
                //获取方法所有的参数
                List<? extends VariableElement> params = executableElement.getParameters();
                for(VariableElement variableElement : params){//遍历并打印参数名
                    System.out.println(variableElement.getSimpleName());
                }
                //打印注解的值
                System.out.println("AnnotationValue:"+executableElement.getAnnotation(InjectPrint.class).value());
            }
        }
        return false;
    }
    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }
}
