package br.com.enginer.domain.ui.usercase.annotation.instance.paginator;


import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({})
public @interface UIConfig {
	boolean expandable() default false;
	boolean multiSelectable() default false;
	boolean editableAll() default false;
	boolean editable() default false;
	boolean deletable() default false;
}


