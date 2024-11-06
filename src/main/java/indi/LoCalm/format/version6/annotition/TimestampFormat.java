//package indi.LoCalm.format.version6.annotition;
//
//import cn.hutool.core.text.StrPool;
//import org.springframework.core.annotation.AliasFor;
//
//import java.lang.annotation.ElementType;
//import java.lang.annotation.Retention;
//import java.lang.annotation.RetentionPolicy;
//import java.lang.annotation.Target;
//import java.time.DayOfWeek;
//
///**
// * 时间戳格式化
// *
// * @author LoCalm
// */
//@Target(ElementType.FIELD)
//@Retention(RetentionPolicy.RUNTIME)
//public @interface TimestampFormat {
//
//	@AliasFor("DateTimeFormat")
//	String value() default "";
//
//	@AliasFor("value")
//	String fieldName() default "";
//
//	DayOfWeek dayOfWeek() default DayOfWeek.MONDAY;
//
//	String separator() default StrPool.DASHED;
//
//}
