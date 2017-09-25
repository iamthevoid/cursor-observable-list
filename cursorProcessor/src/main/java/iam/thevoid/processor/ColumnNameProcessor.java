package iam.thevoid.processor;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.TypeVariableName;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;

import iam.thevoid.annotation.ColumnName;

/**
 * Created by iam on 23/09/2017.
 */
@AutoService(Processor.class)
@SupportedAnnotationTypes("iam.thevoid.annotation.ColumnName")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class ColumnNameProcessor extends AbstractProcessor {

    private Elements elementUtils;
    private Filer filer;
    private Messager messager;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        messager = processingEnvironment.getMessager();
        elementUtils = processingEnvironment.getElementUtils();
        filer = processingEnvironment.getFiler();
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        HashMap<Element, List<Element>> classes = getClasses(roundEnvironment);
        for (Map.Entry<Element, List<Element>> entry :
                classes.entrySet()) {
            Element enclosing = entry.getKey();
            PackageElement pkg = elementUtils.getPackageOf(enclosing);
            String packageName = pkg.isUnnamed() ? "" : pkg.getQualifiedName().toString();

            MethodSpec.Builder method = MethodSpec.methodBuilder("bind")
                    .addParameter(TypeVariableName.get("T"), "target")
                    .addAnnotation(Override.class)
                    .addModifiers(Modifier.PUBLIC)
                    .addParameter(TypeVariableName.get("android.database.Cursor"), "cursor");

            List<Element> elements = entry.getValue();

            for (Element field : elements) {
                method.addStatement("target.$L = cursor.getString(cursor.getColumnIndex($S))", field.getSimpleName(), field.getAnnotation(ColumnName.class).name());
            }

            TypeSpec.Builder builder =
                    TypeSpec.classBuilder(enclosing.getSimpleName() + "_CursorBinding")
                            .superclass(ParameterizedTypeName.get(ClassName.get("iam.thevoid.columnbinder", "CursorDataWrapper"), TypeVariableName.get("T")))
                            .addTypeVariable(TypeVariableName.get("T", TypeVariableName.get(enclosing.toString())))
                            .addMethod(method.build())
                            .addModifiers(Modifier.FINAL, Modifier.PUBLIC);
            try {
                JavaFile.builder(packageName, builder.build()).build().writeTo(filer);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return true;
    }

    private HashMap<Element, List<Element>> getClasses(RoundEnvironment roundEnvironment) {
        Set<? extends Element> fields = roundEnvironment.getElementsAnnotatedWith(ColumnName.class);
        HashMap<Element, List<Element>> res = new HashMap<>();
        for (Element field : fields) {
            Element enclosing = field.getEnclosingElement();
            if (res.containsKey(enclosing)) {
                add(res, enclosing, field);
            } else {
                create(res, enclosing, field);
            }
        }

        return res;
    }

    private void create(HashMap<Element, List<Element>> res, Element enclosing, Element field) {
        res.put(enclosing, singletonList(field));
    }

    private void add(HashMap<Element, List<Element>> res, Element enclosing, Element field) {
        res.get(enclosing).add(field);
    }

    private List<Element> singletonList(Element element) {
        List<Element> list = new ArrayList<>();
        list.add(element);
        return list;
    }
}
