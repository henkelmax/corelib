package de.maxhenkel.corelib.reflection;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

public class ReflectionUtils {

    /**
     * Returns if the provided field has the provided annotation
     *
     * @param field           the field
     * @param annotationClass the annotation class
     * @return if the field has the annotation
     */
    public static boolean hasAnnotation(Field field, Class<? extends Annotation> annotationClass) {
        for (Annotation annotation : field.getAnnotations()) {
            if (annotation.annotationType().equals(annotationClass)) {
                return true;
            }
        }
        return false;
    }

}
