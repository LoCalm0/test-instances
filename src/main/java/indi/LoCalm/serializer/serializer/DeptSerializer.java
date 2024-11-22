//package indi.LoCalm.serializer.serializer;
//
//import cn.hutool.core.annotation.AnnotationUtil;
//import cn.hutool.core.convert.Convert;
//import cn.hutool.core.lang.Opt;
//import cn.hutool.core.text.CharSequenceUtil;
//import cn.hutool.core.util.ReflectUtil;
//import com.fasterxml.jackson.core.JsonGenerator;
//import com.fasterxml.jackson.core.JsonStreamContext;
//import com.fasterxml.jackson.databind.BeanProperty;
//import com.fasterxml.jackson.databind.JsonSerializer;
//import com.fasterxml.jackson.databind.SerializerProvider;
//import com.fasterxml.jackson.databind.ser.ContextualSerializer;
//import com.imtristone.format.annotition.serialize.DeptSerialize;
//import com.imtristone.format.util.FormatUtil;
//import com.imtristone.system.cache.SysCache;
//import com.imtristone.system.entity.Dept;
//import lombok.SneakyThrows;
//
//import java.io.NotSerializableException;
//import java.util.function.Function;
//import java.util.stream.Collectors;
//
///**
// * 部门序列化器
// *
// * @author LoCalm
// */
//public class DeptSerializer extends JsonSerializer<Object> implements ContextualSerializer {
//
//	private String jsonFieldName;
//	private Function<Dept, ?> field;
//	private String getSeparator;
//	private String setSeparator;
//
//	public static final String PREFIX = "";
//	public static final String SUFFIX = "Name";
//
//	@Override
//	@SneakyThrows
//	public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) {
//		gen.writeObject(value);
//
//		if (value != null || CharSequenceUtil.isNotBlank(jsonFieldName)) {
//			gen.writeObjectField(jsonFieldName, value == null ? null : getValue(value));
//		} else {
//			JsonStreamContext outputContext = gen.getOutputContext();
//			this.init(outputContext.getCurrentValue().getClass(), outputContext.getCurrentName(), false);
//			gen.writeStringField(jsonFieldName, null);
//		}
//	}
//
//	private Object getValue(Object value) {
//		if (value instanceof Long) {
//			return FormatUtil.getEntityFieldValue(Convert.toLong(value), SysCache::getDept, field);
//		} else if (value instanceof CharSequence) {
//			if (CharSequenceUtil.isBlank(getSeparator) || CharSequenceUtil.isBlank(Convert.toStr(value))) return null;
//			if (CharSequenceUtil.isBlank(setSeparator)) setSeparator = getSeparator;
//			return FormatUtil.getSeparatorData(Convert.toStr(value), getSeparator).stream().map(id -> Convert.toStr(FormatUtil.getEntityFieldValue(id, SysCache::getDept, field))).collect(Collectors.joining(setSeparator));
//		} else {
//			return null;
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
//		DeptSerialize deptSerialize = AnnotationUtil.getAnnotation(ReflectUtil.getField(clazz, fieldName), DeptSerialize.class);
//
//		this.jsonFieldName = Opt.ofBlankAble(CharSequenceUtil.trim(deptSerialize.jsonFieldName())).orElse(Opt.ofBlankAble(CharSequenceUtil.trim(deptSerialize.prefix())).orElse(PREFIX) + fieldName + Opt.ofBlankAble(CharSequenceUtil.trim(deptSerialize.suffix())).orElse(SUFFIX));
//
//		if (ReflectUtil.getField(clazz, this.jsonFieldName) != null) {
//			throw new NotSerializableException(CharSequenceUtil.format("{}不可序列化为{}", fieldName, this.jsonFieldName));
//		}
//
//		if (flag) {
//			field = deptSerialize.field().getField();
//			getSeparator = deptSerialize.getSeparator();
//			setSeparator = deptSerialize.setSeparator();
//		}
//	}
//
//}
