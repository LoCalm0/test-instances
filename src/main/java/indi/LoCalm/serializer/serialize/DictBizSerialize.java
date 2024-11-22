//package indi.LoCalm.serializer.serialize;
//
//import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
//import com.fasterxml.jackson.databind.annotation.JsonSerialize;
//import com.imtristone.format.util.serializer.DictBizSerializer;
//
//import java.lang.annotation.ElementType;
//import java.lang.annotation.Retention;
//import java.lang.annotation.RetentionPolicy;
//import java.lang.annotation.Target;
//
///**
// * 业务字典序列化
// *
// * @author LoCalm
// */
//@Target({ElementType.FIELD})
//@Retention(RetentionPolicy.RUNTIME)
//@JacksonAnnotationsInside
//@JsonSerialize(using = DictBizSerializer.class, nullsUsing = DictBizSerializer.class)
//public @interface DictBizSerialize {
//
//	String code();
//
//	String prefix() default DictBizSerializer.PREFIX;
//
//	String suffix() default DictBizSerializer.SUFFIX;
//
//	String jsonFieldName() default "";
//
//}
