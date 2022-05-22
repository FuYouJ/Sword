package com.fuyouj.sword.database.processor;

import java.io.IOException;
import java.io.Writer;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;

import com.thingworks.jarvis.persistent.object.annotation.DataObject;

import net.thingworks.jarvis.utils.type.Lists2;
import net.thingworks.jarvis.utils.type.Maps2;
import net.thingworks.jarvis.utils.type.Objects2;

public class DataObjectProcessor extends AbstractProcessor {
    private Filer filer;
    private Messager messager;
    private Map<String, String> collectionNameAndClassMapper;

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return new HashSet<>(Lists2.items(DataObject.class.getName()));
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.RELEASE_12;
    }

    @Override
    public synchronized void init(final ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        this.filer = processingEnv.getFiler();
        this.messager = processingEnv.getMessager();
        this.collectionNameAndClassMapper = Maps2.empty();
    }

    @Override
    public boolean process(final Set<? extends TypeElement> annotations, final RoundEnvironment roundEnv) {
        final Set<? extends Element> dataObjectElements = roundEnv.getElementsAnnotatedWith(DataObject.class);

        if (roundEnv.processingOver()) {
            try {
                final JavaFileObject sourceFile = filer.createSourceFile(
                        "com.thingworks.jarvis.persistent.object.AnnotationProcessedDataObjectMapperProvider");

                final Writer writer = sourceFile.openWriter();

                writer.write("package com.thingworks.jarvis.persistent.object;\n");
                writer.write('\n');
                writer.write("import net.thingworks.jarvis.utils.type.ClassUtils;\n");
                writer.write("import java.util.HashMap;\n");
                writer.write("import java.util.Map;\n");
                writer.write("import com.thingworks.jarvis.persistent.object.DataObjectMapperProvider;\n");
                writer.write('\n');
                writer.write("public class AnnotationProcessedDataObjectMapperProvider "
                        + "implements DataObjectMapperProvider {\n");
                writer.write("  private static final Map<String, Class<?>> mapper;\n");
                writer.write("  static {\n");

                writer.write("    mapper = new HashMap<>();\n");
                for (Map.Entry<String, String> entry : collectionNameAndClassMapper.entrySet()) {
                    writer.write(String.format(
                            "    mapper.put(\"%s\", ClassUtils.getClass(\"%s\"));\n", entry.getKey(), entry.getValue())
                    );
                }
                writer.write("  }\n");

                writer.write('\n');

                writer.write("  @Override\n");
                writer.write("  public Map<String, Class<?>> getCollectionMapper() {\n");
                writer.write("    return mapper;\n");
                writer.write("  }\n");
                writer.write("}");
                writer.flush();
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            messager.printMessage(Diagnostic.Kind.WARNING, Objects2.asJsonString(collectionNameAndClassMapper));
            messager.printMessage(Diagnostic.Kind.WARNING, "processing over");
            return false;
        }

        for (Element dataObjectElement : dataObjectElements) {
            this.basicAnnotationTargetCheck(dataObjectElement);

            final Name qualifiedName = ((TypeElement) dataObjectElement).getQualifiedName();
            final String collectionName = dataObjectElement.getAnnotation(DataObject.class).collection();

            this.duplicateAnnotationCheck(collectionNameAndClassMapper, collectionName, qualifiedName);

            collectionNameAndClassMapper.put(collectionName, qualifiedName.toString());
        }

        return false;
    }

    private void basicAnnotationTargetCheck(final Element dataObjectElement) {
        if (!(dataObjectElement instanceof TypeElement)) {
            processingEnv.getMessager().printMessage(
                    Diagnostic.Kind.ERROR, "DataObjectMustBeAnnotatedAtClass");
            return;
        }

        if (((TypeElement) dataObjectElement).getNestingKind().isNested()) {
            processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR,
                    "DataObjectMustBeAnnotatedAtTopLevelOfClass");
        }
    }

    private void duplicateAnnotationCheck(final Map<String, String> collectionNameAndClassMapper,
                                          final String collectionName,
                                          final Name qualifiedName) {
        if (collectionNameAndClassMapper.containsKey(collectionName)) {
            processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR,
                    String.format(
                            "[%s] has been registered by [%s], but [%s] attempts to register again",
                            collectionName,
                            collectionNameAndClassMapper.get(collectionName),
                            qualifiedName
                    ));
        }
    }

}
