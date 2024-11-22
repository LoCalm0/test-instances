//package indi.LoCalm.serializer.serialize;
//
//import cn.hutool.core.date.DatePattern;
//import cn.hutool.core.text.StrPool;
//import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
//import com.fasterxml.jackson.databind.annotation.JsonSerialize;
//import com.imtristone.format.util.serializer.DateTimeSerializer;
//
//import java.lang.annotation.ElementType;
//import java.lang.annotation.Retention;
//import java.lang.annotation.RetentionPolicy;
//import java.lang.annotation.Target;
//import java.time.DayOfWeek;
//
///**
// * 日期时间序列化
// *
// * @author LoCalm
// */
//@Target({ElementType.FIELD})
//@Retention(RetentionPolicy.RUNTIME)
//@JacksonAnnotationsInside
//@JsonSerialize(using = DateTimeSerializer.class, nullsUsing = DateTimeSerializer.class)
//public @interface DateTimeSerialize {
//
//	String prefix() default DateTimeSerializer.PREFIX;
//
//	String suffix() default DateTimeSerializer.SUFFIX;
//
//	String jsonFieldName() default "";
//
//	String pattern() default DatePattern.NORM_DATETIME_PATTERN;
//
//	DayOfWeek dayOfWeek() default DayOfWeek.MONDAY;
//
//	String separator() default StrPool.DASHED;
//
//}
