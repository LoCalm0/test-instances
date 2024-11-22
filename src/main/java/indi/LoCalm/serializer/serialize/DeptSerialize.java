//package indi.LoCalm.serializer.serialize;
//
//import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
//import com.fasterxml.jackson.databind.annotation.JsonSerialize;
//import com.imtristone.format.annotition.DeptFormat;
//import com.imtristone.format.util.serializer.DeptSerializer;
//
//import java.lang.annotation.ElementType;
//import java.lang.annotation.Retention;
//import java.lang.annotation.RetentionPolicy;
//import java.lang.annotation.Target;
//
///**
// * 部门序列化
// *
// * @author LoCalm
// */
//@Target({ElementType.FIELD})
//@Retention(RetentionPolicy.RUNTIME)
//@JacksonAnnotationsInside
//@JsonSerialize(using = DeptSerializer.class, nullsUsing = DeptSerializer.class)
//public @interface DeptSerialize {
//
//    String prefix() default DeptSerializer.PREFIX;
//
//    String suffix() default DeptSerializer.SUFFIX;
//
//    String jsonFieldName() default "";
//
//    String getSeparator() default "";
//
//    String setSeparator() default "";
//
//    DeptFormat.DeptField field() default DeptFormat.DeptField.DEPT_NAME;
//
//}
