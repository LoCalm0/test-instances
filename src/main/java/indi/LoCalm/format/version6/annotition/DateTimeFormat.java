//package indi.LoCalm.format.version6.annotition;
//
//import cn.hutool.core.date.DatePattern;
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
// * 日期时间格式化
// *
// * @author LoCalm
// */
//@Target(ElementType.FIELD)
//@Retention(RetentionPolicy.RUNTIME)
//public @interface DateTimeFormat {
//
//	@AliasFor("fieldName")
//	String value() default "";
//
//	@AliasFor("value")
//	String fieldName() default "";
//
//	String pattern() default DatePattern.NORM_DATETIME_PATTERN;
//
//	DayOfWeek dayOfWeek() default DayOfWeek.MONDAY;
//
//	String separator() default StrPool.DASHED;
//
//}
