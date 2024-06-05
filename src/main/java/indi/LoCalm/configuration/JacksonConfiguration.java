//package indi.LoCalm.configuration;

//import com.fanh.common.config.serializer.JavaTimeModule;
//import com.fasterxml.jackson.core.json.JsonReadFeature;
//import com.fasterxml.jackson.databind.DeserializationFeature;
//import com.fasterxml.jackson.databind.MapperFeature;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.databind.SerializationFeature;
//import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

//import java.text.SimpleDateFormat;
//import java.time.ZoneId;
//import java.util.Locale;
//import java.util.TimeZone;

///**
// * ObjectMapper JOSN 配置
// *
// * @author LoCalm
// */
////用于标识这是一个配置类，Spring会在应用程序上下文中查找并加载这个类
//@Configuration
//public class JacksonConfiguration {

//    @Bean
//    //只有当容器中不存在ObjectMapper类型的bean时, 才会创建这个bean
//    @ConditionalOnMissingBean(ObjectMapper.class)
//    public ObjectMapper objectMapper(Jackson2ObjectMapperBuilder builder) {
//        // 只有java.util.Date类型会受到这个日期格式的影响
//        builder.simpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        // 使用Jackson2ObjectMapperBuilder创建一个ObjectMapper对象，并禁用XML映射
//        ObjectMapper objectMapper = builder.createXmlMapper(false).build();
//        // 使用默认的语言环境, 即JVM所在操作系统的语言环境
//        //objectMapper.setLocale(Locale.CHINA);//设置语言为中国
//        // 日期序列化为ISO-8601日期字符串，而不是时间戳
//        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
//        // 设置时区为系统默认时区
//        objectMapper.setTimeZone(TimeZone.getTimeZone(ZoneId.systemDefault()));
//        // 设置日期格式为"yyyy-MM-dd HH:mm:ss", 与中国本地化匹配
//        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA));
//        // 允许未转义的控制字符
//        objectMapper.configure(JsonReadFeature.ALLOW_UNESCAPED_CONTROL_CHARS.mappedFeature(), true);
//        // 允许反斜杠转义任意字符
//        objectMapper.configure(JsonReadFeature.ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER.mappedFeature(), true);
//        // 忽略空对象的序列化
//        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
//        // 忽略在JSON中存在但Java对象中不存在的属性
//        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
//        // 允许使用单引号作为字符串分隔符
//        objectMapper.configure(JsonReadFeature.ALLOW_SINGLE_QUOTES.mappedFeature(), true);
//        // 配置ObjectMapper以忽略重复的模块注册
//        objectMapper.configure(MapperFeature.IGNORE_DUPLICATE_MODULE_REGISTRATIONS, true);
//        // 注册模块
//        objectMapper.registerModule(JavaTimeModule.INSTANCE);
//        // 查找并注册所有可用的模块
//        objectMapper.findAndRegisterModules();
//        return objectMapper;
//    }

//}
