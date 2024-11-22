//package indi.LoCalm.serializer.serializer;
//
//import cn.hutool.core.annotation.AnnotationUtil;
//import cn.hutool.core.lang.Opt;
//import cn.hutool.core.text.CharSequenceUtil;
//import cn.hutool.core.util.ReflectUtil;
//import com.fasterxml.jackson.core.JsonGenerator;
//import com.fasterxml.jackson.core.JsonStreamContext;
//import com.fasterxml.jackson.databind.BeanProperty;
//import com.fasterxml.jackson.databind.JsonSerializer;
//import com.fasterxml.jackson.databind.SerializerProvider;
//import com.fasterxml.jackson.databind.ser.ContextualSerializer;
//import com.imtristone.format.annotition.serialize.WeekSerialize;
//import com.imtristone.format.util.FormatUtil;
//import lombok.SneakyThrows;
//
//import java.io.NotSerializableException;
//
///**
// * 周序列化器
// *
// * @author LoCalm
// */
//public class WeekSerializer extends JsonSerializer<Object> implements ContextualSerializer {
//
//	private String jsonFieldName;
//
//	public static final String PREFIX = "";
//	public static final String SUFFIX = "Label";
//
//	@Override
//	@SneakyThrows
//	public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) {
//		gen.writeObject(value);
//
//		if (value != null || CharSequenceUtil.isNotBlank(jsonFieldName)) {
//			gen.writeObjectField(jsonFieldName, value == null ? null : Opt.ofNullable(FormatUtil.getTimestamp(value)).map(FormatUtil::weekOfYear).orElse(null));
//		} else {
//			JsonStreamContext outputContext = gen.getOutputContext();
//			this.init(outputContext.getCurrentValue().getClass(), outputContext.getCurrentName());
//			gen.writeStringField(jsonFieldName, null);
//		}
//	}
//
//	@Override
//	public JsonSerializer<Object> createContextual(SerializerProvider prov, BeanProperty property) {
//		this.init(property.getMember().getDeclaringClass(), property.getName());
//		return this;
//	}
//
//	@SneakyThrows
//	private void init(Class<?> clazz, String fieldName) {
//		WeekSerialize weekSerialize = AnnotationUtil.getAnnotation(ReflectUtil.getField(clazz, fieldName), WeekSerialize.class);
//
//		this.jsonFieldName = Opt.ofBlankAble(CharSequenceUtil.trim(weekSerialize.jsonFieldName())).orElse(Opt.ofBlankAble(CharSequenceUtil.trim(weekSerialize.prefix())).orElse(PREFIX) + fieldName + Opt.ofBlankAble(CharSequenceUtil.trim(weekSerialize.suffix())).orElse(SUFFIX));
//
//		if (ReflectUtil.getField(clazz, this.jsonFieldName) != null) {
//			throw new NotSerializableException(CharSequenceUtil.format("{}不可序列化为{}", fieldName, this.jsonFieldName));
//		}
//	}
//
//}
