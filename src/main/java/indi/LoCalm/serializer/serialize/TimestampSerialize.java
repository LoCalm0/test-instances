//package indi.LoCalm.serializer.serialize;
//
//import cn.hutool.core.text.StrPool;
//import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
//import com.fasterxml.jackson.databind.annotation.JsonSerialize;
//import com.imtristone.format.util.serializer.TimestampSerializer;
//
//import java.lang.annotation.ElementType;
//import java.lang.annotation.Retention;
//import java.lang.annotation.RetentionPolicy;
//import java.lang.annotation.Target;
//import java.time.DayOfWeek;
//
///**
// * 时间戳序列化
// *
// * @author LoCalm
// */
//@Target({ElementType.FIELD})
//@Retention(RetentionPolicy.RUNTIME)
//@JacksonAnnotationsInside
//@JsonSerialize(using = TimestampSerializer.class, nullsUsing = TimestampSerializer.class)
//public @interface TimestampSerialize {
//
//	String prefix() default TimestampSerializer.PREFIX;
//
//	String suffix() default TimestampSerializer.SUFFIX;
//
//	String jsonFieldName() default "";
//
//	DayOfWeek dayOfWeek() default DayOfWeek.MONDAY;
//
//	String separator() default StrPool.DASHED;
//
//}
