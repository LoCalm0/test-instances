//package indi.LoCalm.serializer;
//
//import com.fasterxml.jackson.core.JsonGenerator;
//import com.fasterxml.jackson.databind.JsonSerializer;
//import com.fasterxml.jackson.databind.SerializerProvider;
//
//import java.io.IOException;
//
///**
// * Long 转 String 序列化程序
// *
// * @author LoCalm
// */
//public class LongToStringSerializer extends JsonSerializer<Long> {
//
//    @Override
//    public void serialize(Long value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
//        if (value != null) {
//            String strValue = String.valueOf(value);
//            if (strValue.length() > 16) {
//                // 如果长度大于16, 则将Long转换为字符串类型并写入JSON
//                gen.writeString(strValue);
//            } else {
//                // 如果长度小于等于16, 则将Long作为数字类型写入JSON
//                gen.writeNumber(value);
//            }
//        }
//    }
//
//}
