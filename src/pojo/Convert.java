package pojo;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import example.test.MyMapper;

@Retention(RUNTIME)
@Target(FIELD)
public @interface Convert {
	Class<MyMapper> value();
}
