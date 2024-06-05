//package indi.LoCalm.format.version2.util;
//
//import cn.hutool.core.collection.CollUtil;
//import cn.hutool.core.convert.Convert;
//import cn.hutool.core.date.DatePattern;
//import cn.hutool.core.date.DateUtil;
//import cn.hutool.core.lang.Console;
//import cn.hutool.core.lang.Opt;
//import cn.hutool.core.lang.func.LambdaUtil;
//import cn.hutool.core.map.MapUtil;
//import cn.hutool.core.text.CharSequenceUtil;
//import cn.hutool.core.text.StrPool;
//import cn.hutool.core.util.ArrayUtil;
//import cn.hutool.core.util.ObjectUtil;
//import cn.hutool.core.util.ReflectUtil;
//import cn.hutool.extra.spring.SpringUtil;
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.tristone.common.format.annotition.*;
//import org.jeecg.common.system.vo.DictModelMany;
//import org.jeecg.modules.system.entity.SysDepart;
//import org.jeecg.modules.system.entity.SysUser;
//import org.jeecg.modules.system.mapper.SysDictMapper;
//import org.jeecg.modules.system.service.ISysDepartService;
//import org.jeecg.modules.system.service.ISysUserService;
//import org.jetbrains.annotations.NotNull;
//import org.jetbrains.annotations.Nullable;
//import org.springframework.core.annotation.AnnotationUtils;
//
//import java.lang.annotation.Annotation;
//import java.time.LocalDateTime;
//import java.time.temporal.WeekFields;
//import java.util.*;
//import java.util.function.Function;
//import java.util.stream.Collectors;
//
///**
// * 格式化工具
// *
// * @author LoCalm
// */
//public class FormatUtil {
//
//    private FormatUtil() {
//    }
//
//    private enum FieldType {
//        USER, DEPT, DICT, DATE, WEEK, YES_NO
//    }
//
//    //TODO 缺少缓存(需要先拿缓存)
//    private static final SysDictMapper sysDictMapper = SpringUtil.getBean(SysDictMapper.class);
//    private static final ISysUserService iSysUserService = SpringUtil.getBean(ISysUserService.class);
//    private static final ISysDepartService iSysDepartService = SpringUtil.getBean(ISysDepartService.class);
//
//
//    private static boolean toTimestamp = false;
//    private static final int[] COUNT = new int[3];
//    private static Map<String, String> userMap;
//    private static Map<String, String> deptMap;
//    private static Map<String, Map<String, String>> dictMaps;
//    private static final Map<String, String> YES_NO_MAP = MapUtil.builder(new HashMap<String, String>()).put("0", "否").put("1", "是").build();
//
//    private static final Map<String, String> GET_SET_FIELD_NAME = new HashMap<>();
//    private static final Map<String, FieldType> FIELD_NAME_IDENTITY = new HashMap<>();
//    private static final Map<String, String> FIELD_NAME_ANNOTATION_VALUE = new HashMap<>();
//    private static final List<Class<? extends Annotation>> ANNOTATION_TYPES = Arrays.asList(UserFormat.class, DeptFormat.class, DictFormat.class, DateFormat.class, WeekFormat.class, YesNoFormat.class);
//
//    private static final ObjectMapper objectMapper = SpringUtil.getBean(ObjectMapper.class);
//
//    private static void init(Class<?> clazz, List<Class<? extends Annotation>> whitelist) {
//        Arrays.stream(ReflectUtil.getFields(clazz)).forEach(field -> Arrays.stream(field.getDeclaredAnnotations()).filter(annotation -> whitelist.contains(annotation.annotationType())).forEach(annotation -> {
//            String fieldName = field.getName();
//            Annotation annotationData = AnnotationUtils.getAnnotation(field, annotation.annotationType());
//            String value = ReflectUtil.invoke(annotationData, LambdaUtil.getMethodName(UserFormat::value));
//            if (CharSequenceUtil.isBlank(value)) value = fieldName;
//
//            if (field.getType() != String.class) throw new ClassCastException(fieldName + "类型不匹配, 需要String类型");
//            Objects.requireNonNull(ReflectUtil.getField(clazz, value), value + "字段不存在");
//
//            GET_SET_FIELD_NAME.put(value, fieldName);
//
//            assert annotationData != null;
//            processFieldAnnotations(annotationData, value);
//        }));
//    }
//
//    private static void processFieldAnnotations(@NotNull Annotation annotation, String fieldName) {
//        switch (annotation.annotationType().getSimpleName()) {
//            case "UserFormat":
//                COUNT[0] += 1;
//                FIELD_NAME_IDENTITY.put(fieldName, FieldType.USER);
//                break;
//            case "DeptFormat":
//                COUNT[1] += 1;
//                FIELD_NAME_IDENTITY.put(fieldName, FieldType.DEPT);
//                break;
//            case "DictFormat":
//                COUNT[2] += 1;
//                FIELD_NAME_IDENTITY.put(fieldName, FieldType.DICT);
//                FIELD_NAME_ANNOTATION_VALUE.put(fieldName, Opt.ofNullable((DictFormat) annotation).map(DictFormat::code).map(Enum::name).map(String::toLowerCase).get());
//                break;
//            case "DateFormat":
//                FIELD_NAME_IDENTITY.put(fieldName, FieldType.DATE);
//                FIELD_NAME_ANNOTATION_VALUE.put(fieldName, Opt.ofBlankAble((DateFormat) annotation).map(DateFormat::pattern).orElse(DatePattern.NORM_DATETIME_PATTERN));
//                break;
//            case "WeekFormat":
//                FIELD_NAME_IDENTITY.put(fieldName, FieldType.WEEK);
//                break;
//            case "YesNoFormat":
//                FIELD_NAME_IDENTITY.put(fieldName, FieldType.YES_NO);
//                break;
//            default:
//                break;
//        }
//    }
//
//    private static void clear() {
//        toTimestamp = false;
//        GET_SET_FIELD_NAME.clear();
//        FIELD_NAME_IDENTITY.clear();
//        FIELD_NAME_ANNOTATION_VALUE.clear();
//    }
//
//
//    @Nullable
//    private static <V> V format(V vo) {
//        if (ObjectUtil.isNull(vo)) return null;
//
//        init(vo.getClass(), Collections.singletonList(DateFormat.class));
//
//        if (MapUtil.isEmpty(FIELD_NAME_ANNOTATION_VALUE)) return vo;
//
//        timestampOrDateTime(vo);
//        clear();
//        return vo;
//    }
//
//    public static <V> V dateTimeToTimestamp(V vo) {
//        toTimestamp = true;
//        return format(vo);
//    }
//
//    public static <V> V timestampToDateTime(V vo) {
//        toTimestamp = false;
//        return format(vo);
//    }
//
//
//    @NotNull
//    public static <V> List<V> format(List<V> vo) {
//        return format(vo, null);
//    }
//
//    @NotNull
//    public static <V> List<V> format(List<V> vo, @Nullable CollUtil.Consumer<V> run) {
//        if (CollUtil.isEmpty(vo)) return Collections.emptyList();
//
//        init(vo.get(0).getClass(), ANNOTATION_TYPES);
//
//        if (MapUtil.isEmpty(FIELD_NAME_IDENTITY) && MapUtil.isEmpty(FIELD_NAME_ANNOTATION_VALUE)) {
//            return vo;
//        }
//
//        initData(vo);
//        CollUtil.forEach(vo, ConsumerBuilder.<V>builder().add(FormatUtil::reflectionField).add(run).build());
//        clear();
//        return vo;
//    }
//
//
//    private static <V> void reflectionField(V vo, int i) {
//        for (Map.Entry<String, FieldType> entry : FIELD_NAME_IDENTITY.entrySet()) {
//            Object value = ReflectUtil.getFieldValue(vo, entry.getKey());
//            switch (entry.getValue()) {
//                case USER:
//                    Opt.ofNullable(userMap.get(Convert.toStr(value))).ifPresent(fieldValue -> ReflectUtil.setFieldValue(vo, GET_SET_FIELD_NAME.get(entry.getKey()), fieldValue));
//                    break;
//                case DEPT:
//                    Opt.ofNullable(deptMap.get(Convert.toStr(value))).ifPresent(fieldValue -> ReflectUtil.setFieldValue(vo, GET_SET_FIELD_NAME.get(entry.getKey()), fieldValue));
//                    break;
//                case DICT:
//                    Opt.ofNullable(dictMaps.get(FIELD_NAME_ANNOTATION_VALUE.get(entry.getKey()))).ifPresent(map -> Opt.ofNullable(map.get(Convert.toStr(value))).ifPresent(fieldValue -> ReflectUtil.setFieldValue(vo, GET_SET_FIELD_NAME.get(entry.getKey()), fieldValue)));
//                    break;
//                case DATE:
//                    Opt.ofNullable(Convert.toLong(value)).ifPresent(fieldValue -> ReflectUtil.setFieldValue(vo, GET_SET_FIELD_NAME.get(entry.getKey()), DateUtil.date(fieldValue).toString(FIELD_NAME_ANNOTATION_VALUE.get(entry.getKey()))));
//                    break;
//                case WEEK:
//                    Opt.ofNullable(Convert.toLong(value)).ifPresent(fieldValue -> ReflectUtil.setFieldValue(value, GET_SET_FIELD_NAME.get(entry.getKey()), weekOfYear(fieldValue)));
//                    break;
//                case YES_NO:
//                    Opt.ofNullable(YES_NO_MAP.get(Convert.toStr(value))).ifPresent(fieldValue -> ReflectUtil.setFieldValue(vo, GET_SET_FIELD_NAME.get(entry.getKey()), fieldValue));
//                    break;
//                default:
//                    break;
//            }
//        }
//    }
//
//    private static <V> void initData(List<V> vo) {
//        String[] userField = new String[COUNT[0]];
//        String[] deptField = new String[COUNT[1]];
//        String[] dictField = new String[COUNT[2]];
//
//        getFieldNameByIdentity(userField, deptField, dictField);
//
//        userMap = getEntityMap(vo, userField, SysUser::getId, SysUser::getRealname, iSysUserService::listByIds, "获取用户失败");
//        deptMap = getEntityMap(vo, deptField, SysDepart::getId, SysDepart::getDepartName, iSysDepartService::listByIds, "获取部门失败");
//        dictMaps = getDictMap(dictField);
//    }
//
//    private static void getFieldNameByIdentity(String[] userField, String[] deptField, String[] dictField) {
//        if (FIELD_NAME_IDENTITY.containsValue(FieldType.USER) || FIELD_NAME_IDENTITY.containsValue(FieldType.DEPT) || FIELD_NAME_IDENTITY.containsValue(FieldType.DICT)) {
//            for (Map.Entry<String, FieldType> entry : FIELD_NAME_IDENTITY.entrySet()) {
//                switch (entry.getValue()) {
//                    case USER:
//                        userField[--COUNT[0]] = entry.getKey();
//                        break;
//                    case DEPT:
//                        deptField[--COUNT[1]] = entry.getKey();
//                        break;
//                    case DICT:
//                        dictField[--COUNT[2]] = entry.getKey();
//                        break;
//                    default:
//                        break;
//                }
//            }
//        }
//    }
//
//    private static <V, T> Map<String, String> getEntityMap(List<V> data, String[] field, Function<T, String> idExtractor, Function<T, String> nameExtractor, Function<Set<String>, List<T>> entityRetriever, String errorMessage) {
//        String[] clearEmptyField = ArrayUtil.removeBlank(field);
//        if (ArrayUtil.isEmpty(clearEmptyField)) {
//            return Collections.emptyMap();
//        }
//
//        Set<String> ids = clearEmptyField.length != 1 ? distinct(data, clearEmptyField) : data.stream().map(item -> Convert.toStr(ReflectUtil.getFieldValue(item, clearEmptyField[0]))).collect(Collectors.toSet());
//
//        CollUtil.removeNull(ids);
//        if (CollUtil.isEmpty(ids)) {
//            return Collections.emptyMap();
//        }
//
//        List<T> entities = entityRetriever.apply(ids);
//        if (CollUtil.isNotEmpty(entities)) {
//            return entities.stream().filter(item -> ObjectUtil.isNotNull(nameExtractor.apply(item))).collect(Collectors.toMap(idExtractor, nameExtractor));
//        } else {
//            Console.error(errorMessage);
//        }
//
//        return Collections.emptyMap();
//    }
//
//    private static <T> Set<String> distinct(@NotNull List<T> data, String[] nameField) {
//        return data.stream().flatMap(entity -> Arrays.stream(nameField).map(name -> Convert.toStr(ReflectUtil.getFieldValue(entity, name)))).collect(Collectors.toSet());
//    }
//
//    private static Map<String, Map<String, String>> getDictMap(String[] dictField) {
//        if (ArrayUtil.isEmpty(dictField)) return Collections.emptyMap();
//        List<String> dictCodeList = MapUtil.valuesOfKeys(FIELD_NAME_ANNOTATION_VALUE, Arrays.stream(dictField).iterator()).stream().distinct().collect(Collectors.toList());
//        if (CollUtil.isEmpty(dictCodeList)) return Collections.emptyMap();
//        List<DictModelMany> data = sysDictMapper.queryDictItemsByCodeList(dictCodeList);
//        if (CollUtil.isEmpty(data)) return Collections.emptyMap();
//        return data.stream().collect(Collectors.groupingBy(DictModelMany::getDictCode, Collectors.toMap(DictModelMany::getValue, DictModelMany::getText)));
//    }
//
//    private static <V> void timestampOrDateTime(V vo) {
//        for (Map.Entry<String, String> entry : FIELD_NAME_ANNOTATION_VALUE.entrySet()) {
//            Opt.ofBlankAble(Convert.toStr(ReflectUtil.getFieldValue(vo, toTimestamp ? GET_SET_FIELD_NAME.get(entry.getKey()) : entry.getKey()))).ifPresent(fieldValue -> ReflectUtil.setFieldValue(vo, toTimestamp ? entry.getKey() : GET_SET_FIELD_NAME.get(entry.getKey()), toTimestamp ? DateUtil.parse(fieldValue).getTime() : DateUtil.date(Convert.toLong(fieldValue)).toString(FIELD_NAME_ANNOTATION_VALUE.get(entry.getKey()))));
//        }
//    }
//
//    public static String weekOfYear(Long date) {
//        if (date == null) return null;
//        LocalDateTime localDateTime = DateUtil.date(date).toLocalDateTime();
//        return localDateTime.getYear() + StrPool.DASHED + localDateTime.get(WeekFields.ISO.weekOfYear());
//    }
//
//    public static String toJsonStr(Object obj) {
//        try {
//            return objectMapper.writeValueAsString(obj);
//        } catch (JsonProcessingException e) {
//            Console.error("JSON解析失败");
//            throw new RuntimeException(e);
//        }
//    }
//
//}
