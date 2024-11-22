//package indi.LoCalm.serializer.serializer;
//
//import cn.hutool.core.annotation.AnnotationUtil;
//import cn.hutool.core.lang.Opt;
//import cn.hutool.core.text.CharSequenceUtil;
//import cn.hutool.core.text.StrPool;
//import cn.hutool.core.util.ReflectUtil;
//import com.fasterxml.jackson.core.JsonGenerator;
//import com.fasterxml.jackson.core.JsonStreamContext;
//import com.fasterxml.jackson.databind.BeanProperty;
//import com.fasterxml.jackson.databind.JsonSerializer;
//import com.fasterxml.jackson.databind.SerializerProvider;
//import com.fasterxml.jackson.databind.ser.ContextualSerializer;
//import com.imtristone.format.annotition.serialize.TimestampSerialize;
//import com.imtristone.format.util.FormatUtil;
//import lombok.SneakyThrows;
//
//import java.io.NotSerializableException;
//import java.time.DayOfWeek;
//
///**
// * 时间戳序列化器
// *
// * @author LoCalm
// */
//public class TimestampSerializer extends JsonSerializer<Object> implements ContextualSerializer {
//
//	private String jsonFieldName;
//	private DayOfWeek dayOfWeek;
//	private String separator;
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
//			gen.writeObjectField(jsonFieldName, value == null ? null : FormatUtil.getTimestamp(value, separator, dayOfWeek));
//		} else {
//			JsonStreamContext outputContext = gen.getOutputContext();
//			this.init(outputContext.getCurrentValue().getClass(), outputContext.getCurrentName(), false);
//			gen.writeStringField(jsonFieldName, null);
//		}
//	}
//
//	@Override
//	public JsonSerializer<Object> createContextual(SerializerProvider prov, BeanProperty property) {
//		this.init(property.getMember().getDeclaringClass(), property.getName(), true);
//		return this;
//	}
//
//	@SneakyThrows
//	private void init(Class<?> clazz, String fieldName, boolean flag) {
//		TimestampSerialize timestampSerialize = AnnotationUtil.getAnnotation(ReflectUtil.getField(clazz, fieldName), TimestampSerialize.class);
//
//		this.jsonFieldName = Opt.ofBlankAble(CharSequenceUtil.trim(timestampSerialize.jsonFieldName())).orElse(Opt.ofBlankAble(CharSequenceUtil.trim(timestampSerialize.prefix())).orElse(PREFIX) + fieldName + Opt.ofBlankAble(CharSequenceUtil.trim(timestampSerialize.suffix())).orElse(SUFFIX));
//
//		if (ReflectUtil.getField(clazz, this.jsonFieldName) != null) {
//			throw new NotSerializableException(CharSequenceUtil.format("{}不可序列化为{}", fieldName, this.jsonFieldName));
//		}
//
//		if (flag) {
//			dayOfWeek = timestampSerialize.dayOfWeek();
//			separator = Opt.ofBlankAble(CharSequenceUtil.trim(timestampSerialize.separator())).orElse(StrPool.DASHED);
//		}
//	}
//
//}
