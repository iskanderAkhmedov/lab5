package org.example.utils;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Table;
import lombok.experimental.UtilityClass;

import java.util.Optional;

@UtilityClass
public class AnnotationUtils {
    public static String fieldDescription(Class<?> cls, String field) {
        try {
            return Optional.of(cls.getDeclaredField(field))
                    .map(it -> it.getAnnotation(Schema.class))
                    .map(Schema::description)
                    .orElse(null);
        } catch (Exception e) {
            return null;
        }
    }

    public static String getTableNameByEntityClass(Class<?> cls) {
        try {
            return Optional.of(cls.getAnnotation(Table.class))
                    .map(Table::name)
                    .orElse(cls.getSimpleName());
        } catch (Exception e) {
            return null;
        }
    }
}
