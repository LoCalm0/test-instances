//package indi.LoCalm.format.version6.annotition;
//
//import org.springframework.core.annotation.AliasFor;
//
//import java.lang.annotation.ElementType;
//import java.lang.annotation.Retention;
//import java.lang.annotation.RetentionPolicy;
//import java.lang.annotation.Target;
//
///**
// * 字典格式化
// *
// * @author LoCalm
// */
//@Target(ElementType.FIELD)
//@Retention(RetentionPolicy.RUNTIME)
//public @interface DictFormat {
//
//	@AliasFor("fieldName")
//	String value() default "";
//
//	@AliasFor("value")
//	String fieldName() default "";
//
//	Code code() default Code.EMPTY;
//
//	String strCode() default "";
//
//	/**
//	 * 字典 code
//	 */
//	enum Code {
//		EMPTY,
//	}
//
//}
