//package indi.LoCalm.serializer.serialize;
//
//import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
//import com.fasterxml.jackson.databind.annotation.JsonSerialize;
//import com.imtristone.format.annotition.UserFormat;
//import com.imtristone.format.util.serializer.UserSerializer;
//
//import java.lang.annotation.ElementType;
//import java.lang.annotation.Retention;
//import java.lang.annotation.RetentionPolicy;
//import java.lang.annotation.Target;
//
///**
// * 用户序列化
// *
// * @author LoCalm
// */
//@Target({ElementType.FIELD})
//@Retention(RetentionPolicy.RUNTIME)
//@JacksonAnnotationsInside
//@JsonSerialize(using = UserSerializer.class, nullsUsing = UserSerializer.class)
//public @interface UserSerialize {
//
//	String prefix() default UserSerializer.PREFIX;
//
//	String suffix() default UserSerializer.SUFFIX;
//
//	String jsonFieldName() default "";
//
//	String getSeparator() default "";
//
//	String setSeparator() default "";
//
//	UserFormat.UserField field() default UserFormat.UserField.REAL_NAME;
//
//}
