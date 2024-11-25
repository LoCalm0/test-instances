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
//import com.imtristone.format.annotition.DictBizFormat;
//import com.imtristone.format.annotition.serialize.DictBizSerialize;
//import com.imtristone.system.cache.DictBizCache;
//import com.imtristone.system.entity.DictBiz;
//import lombok.SneakyThrows;
//
//import java.io.NotSerializableException;
//import java.util.Collections;
//import java.util.Map;
//import java.util.stream.Collectors;
//
///**
// * 业务字典序列化器
// *
// * @author LoCalm
// */
//public class DictBizSerializer extends JsonSerializer<Object> implements ContextualSerializer {
//
//	private String jsonFieldName;
//	private Map<String, String> dictBiz = Collections.emptyMap();
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
//			gen.writeStringField(jsonFieldName, value == null ? null : dictBiz.get(Convert.toStr(value)));
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
//		DictBizSerialize dictBizSerialize = AnnotationUtil.getAnnotation(ReflectUtil.getField(clazz, fieldName), DictBizSerialize.class);
//
//		String code = Opt.ofBlankAble(dictBizSerialize.code() == DictBizFormat.Code.EMPTY ? CharSequenceUtil.trim(dictBizSerialize.strCode()).toLowerCase() : dictBizSerialize.code().name().toLowerCase()).orElseThrow(NullPointerException::new, "code和strCode为空");
//
//		this.jsonFieldName = Opt.ofBlankAble(CharSequenceUtil.trim(dictBizSerialize.jsonFieldName())).orElse(Opt.ofBlankAble(CharSequenceUtil.trim(dictBizSerialize.prefix())).orElse(PREFIX) + fieldName + Opt.ofBlankAble(CharSequenceUtil.trim(dictBizSerialize.suffix())).orElse(SUFFIX));
//
//		if (ReflectUtil.getField(clazz, this.jsonFieldName) != null) {
//			throw new NotSerializableException(CharSequenceUtil.format("{}不可序列化为{}", fieldName, this.jsonFieldName));
//		}
//
//		if (flag) {
//			this.dictBiz = Opt.ofEmptyAble(DictBizCache.getList(code)).map(list -> list.stream().collect(Collectors.toMap(DictBiz::getDictKey, DictBiz::getDictValue))).orElse(Collections.emptyMap());
//		}
//	}
//
//}
