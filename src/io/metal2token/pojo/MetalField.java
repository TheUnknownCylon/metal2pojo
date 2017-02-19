package io.metal2token.pojo;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.util.Optional;

import io.metal2token.DefaultConverter;
import io.parsingdata.metal.token.Cho;
import io.parsingdata.metal.token.Def;
import io.parsingdata.metal.token.Token;

/**
 * Indicates that a field inside a {@link MetalPojo} maps to a field in a
 * {@link Token}.
 * 
 * This field can be used to:
 * <ul>
 * <li>map a {@link Def} with a non-empty name. In this case, the name of the
 * {@link Field} that has been annotated with this annotation should match the
 * definition in the token.
 * <li>map a {@link MetalPojo} to a sub-{@link Token} in the main {@link Token}.
 * <li>map an anonymous {@link Cho}. In that case the type of the {@link Field}
 * should be {@link Optional}. Anonymous: the `name`-field should not be set.
 * </ul>
 */
@Retention(RUNTIME)
@Target(FIELD)
public @interface MetalField {

	/**
	 * Optional class that converts a ParseValue to another object.
	 * 
	 * The converter must be an instance of {@link PojoConverter}. If not, a
	 * {@link ClassCastException} will be thrown.
	 * 
	 * @return Class of the converter to be used.
	 */
	Class<?> converter() default DefaultConverter.class;
}
