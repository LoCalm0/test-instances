//package indi.LoCalm.serializer.serialize;
//
//import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
//import com.fasterxml.jackson.databind.annotation.JsonSerialize;
//import com.imtristone.format.annotition.DictFormat;
//import com.imtristone.format.util.serializer.DictSerializer;
//
//import java.lang.annotation.ElementType;
//import java.lang.annotation.Retention;
//import java.lang.annotation.RetentionPolicy;
//import java.lang.annotation.Target;
//
///**
// * 字典序列化
// *
// * @author LoCalm
// */
//@Target({ElementType.FIELD})
//@Retention(RetentionPolicy.RUNTIME)
//@JacksonAnnotationsInside
//@JsonSerialize(using = DictSerializer.class, nullsUsing = DictSerializer.class)
//public @interface DictSerialize {
//
//	DictFormat.Code code() default DictFormat.Code.EMPTY;
//
//	String strCode() default "";
//
//    String prefix() default DictSerializer.PREFIX;
//
//    String suffix() default DictSerializer.SUFFIX;
//
//    String jsonFieldName() default "";
//
//}
