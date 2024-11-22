//package indi.LoCalm.serializer.serialize;
//
//import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
//import com.fasterxml.jackson.databind.annotation.JsonSerialize;
//import com.imtristone.format.util.serializer.WeekSerializer;
//
//import java.lang.annotation.ElementType;
//import java.lang.annotation.Retention;
//import java.lang.annotation.RetentionPolicy;
//import java.lang.annotation.Target;
//
///**
// * 周序列化
// *
// * @author LoCalm
// */
//@Target({ElementType.FIELD})
//@Retention(RetentionPolicy.RUNTIME)
//@JacksonAnnotationsInside
//@JsonSerialize(using = WeekSerializer.class, nullsUsing = WeekSerializer.class)
//public @interface WeekSerialize {
//
//	String prefix() default WeekSerializer.PREFIX;
//
//	String suffix() default WeekSerializer.SUFFIX;
//
//	String jsonFieldName() default "";
//
//}
