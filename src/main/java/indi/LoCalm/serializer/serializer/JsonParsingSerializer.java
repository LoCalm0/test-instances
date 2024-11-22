//package indi.LoCalm.serializer.serializer;
//
//import cn.hutool.core.lang.Opt;
//import cn.hutool.core.map.MapUtil;
//import cn.hutool.core.text.CharSequenceUtil;
//import cn.hutool.json.JSONUtil;
//import com.fasterxml.jackson.core.JsonGenerator;
//import com.fasterxml.jackson.databind.JsonSerializer;
//import com.fasterxml.jackson.databind.SerializerProvider;
//import lombok.SneakyThrows;
//
//import java.util.Collections;
//import java.util.Map;
//
///**
// * json解析序列化器
// *
// * @author LoCalm
// */
//public class JsonParsingSerializer extends JsonSerializer<String> {
//
//	@Override
//	@SneakyThrows
//	public void serialize(String value, JsonGenerator gen, SerializerProvider serializers) {
//		gen.writeString(value);
//		if (CharSequenceUtil.isBlank(value)) return;
//		Map<String, Object> map = Opt.ofTry(() -> JSONUtil.parseObj(value).<Map<String, Object>>toBean(Map.class)).orElse(Collections.emptyMap());
//		if (MapUtil.isEmpty(map)) return;
//
//		for (Map.Entry<String, Object> entry : map.entrySet()) {
//			gen.writeObjectField(entry.getKey(), entry.getValue());
//		}
//	}
//
//}
