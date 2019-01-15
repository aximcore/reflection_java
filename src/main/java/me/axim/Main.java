package me.axim;

import me.axim.annotation.Begin;
import me.axim.annotation.Test;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        Class annotated = Annotated.class;
        Annotation[] annotations = annotated.getAnnotations();
        System.out.println("Class annotations count: " + annotations.length);
        System.out.println("Class full name: " + annotated.getName());
        System.out.println("Class fields: " + Arrays.stream(annotated.getDeclaredFields()).map(field -> field.getName()).collect(Collectors.joining("; ")));

        for (Annotation annotation : annotations) {
            System.out.println(annotation);
        }

        var decMethods = List.of(annotated.getDeclaredMethods());

        // filter by @Begin annotates type
        var beginAnnotatedMethod = decMethods.stream()
                .filter(method -> method.isAnnotationPresent(Begin.class))
                .collect(Collectors.toList());


        System.out.println("Begin annotated method count: " + beginAnnotatedMethod.size());
        System.out.println("Class all declared methods count: " + decMethods.size());

        for (Method method : decMethods) {
            try {
                Object instance = annotated.getDeclaredConstructor().newInstance();
                System.out.println(method.getDeclaredAnnotations()[0] + " [" + Modifier.toString(method.getModifiers()) + "] " + method.getName());

                // filter by @Test annotation
                if (method.getAnnotationsByType(Test.class).length != 0) {

                    // call all @Begin annotated methods
                    beginAnnotatedMethod.forEach(beginMethod -> {
                        try {
                            beginMethod.invoke(instance);
                        } catch (IllegalAccessException | InvocationTargetException e) {
                            e.printStackTrace();
                        }
                    });

                    method.setAccessible(true); // access to private methods
                    method.invoke(instance /*, args... */);
                }

            } catch (IllegalAccessException | InvocationTargetException |
                     InstantiationException | NoSuchMethodException e) {
                e.printStackTrace();
            }
        }

    }
}
