package io.github.aresgtr;

public @interface CustomAnnotation {

    String name() default "Jack";
    int count() default 999;

    String[] tags() default {"Java", "Annotation"};
}
