//package indi.LoCalm.storage.format.util;
//
//import cn.hutool.core.collection.CollUtil;
//import cn.hutool.core.convert.Convert;
//import cn.hutool.core.date.DatePattern;
//import cn.hutool.core.date.DateUtil;
//import cn.hutool.core.lang.Console;
//import cn.hutool.core.map.MapUtil;
//import cn.hutool.core.text.StrPool;
//import cn.hutool.core.util.*;
//import com.imtristone.format.annotition.*;
//import com.imtristone.system.cache.DictBizCache;
//import com.imtristone.system.cache.DictCache;
//import com.imtristone.system.cache.SysCache;
//import com.imtristone.system.cache.UserCache;
//import com.imtristone.system.entity.Dept;
//import com.imtristone.system.entity.Dict;
//import com.imtristone.system.entity.DictBiz;
//import com.imtristone.system.entity.User;
//import org.jetbrains.annotations.NotNull;
//import org.springframework.core.annotation.AnnotationUtils;
//
//import java.lang.annotation.Annotation;
//import java.lang.reflect.Field;
//import java.lang.reflect.Type;
//import java.util.*;
//import java.util.function.Function;
//import java.util.stream.Collectors;
//
///**
// * 导出工具
// *
// * @author LoCalm
// */
//public class ExportUtil {
//	private ExportUtil() {
//	}
//
//	private enum FieldType {
//		DATE, DEPT, USER, WEEK, YES_NO
//	}
//
//	private static final int[] COUNT = new int[2];
//	private static final List<Class<? extends Annotation>> ANNOTATION_TYPES = Arrays.asList(WeekFormat.class, YesNoFormat.class, UserFormat.class, DateFormat.class, DeptFormat.class);
//	private static Map<Long, String> userMap;
//	private static Map<Long, String> deptMap;
//	private static Map<String, String> yesNoMap = Collections.emptyMap();
//	private static final Map<String, FieldType> FIELD_NAME_IDENTITY = new HashMap<>();
//	private static final Map<String, String> FIELD_NAME_DATE_PATTERN = new HashMap<>();
//
//
//	protected static void typeConversionException(Type type, String name) {
//		if (type != String.class) throw new ClassCastException(name + "类型不匹配, 需要String类型");
//	}
//
//	private static <T> boolean initData(List<T> data, boolean isRunAnnotation) {
//		if (isRunAnnotation) {
//			return processAnnotations(data);
//		}
//		return false;
//	}
//
//	private static void clear() {
//		FIELD_NAME_IDENTITY.clear();
//		FIELD_NAME_DATE_PATTERN.clear();
//	}
//
//	private static <T> boolean processAnnotations(@NotNull List<T> data) {
//		return Arrays.stream(ClassUtil.getDeclaredFields(data.get(0).getClass())).flatMap(field -> Arrays.stream(field.getDeclaredAnnotations()).filter(annotation -> ANNOTATION_TYPES.contains(annotation.annotationType())).peek(annotation -> {
//			typeConversionException(field.getType(), field.getName());
//			processFieldAnnotations(annotation.annotationType().getSimpleName(), field);
//		})).findAny().map(value -> {
//			processDataAndCache(data);
//			return true;
//		}).isPresent();
//	}
//
//	private static void processFieldAnnotations(@NotNull String simpleName, @NotNull Field field) {
//		String fieldName = field.getName();
//		switch (simpleName) {
//			case "UserFormat":
//				COUNT[0] += 1;
//				FIELD_NAME_IDENTITY.put(fieldName, FieldType.USER);
//				break;
//			case "DeptFormat":
//				COUNT[1] += 1;
//				FIELD_NAME_IDENTITY.put(fieldName, FieldType.DEPT);
//				break;
//			case "DateFormat":
//				processDateFormatAnnotation(field, fieldName);
//				break;
//			case "WeekFormat":
//				FIELD_NAME_IDENTITY.put(fieldName, FieldType.WEEK);
//				break;
//			case "YesNoFormat":
//				FIELD_NAME_IDENTITY.put(fieldName, FieldType.YES_NO);
//				break;
//			default:
//				break;
//		}
//	}
//
//	private static void processDateFormatAnnotation(Field field, String fieldName) {
//		Optional<DateFormat> annotation = Optional.ofNullable(AnnotationUtils.getAnnotation(field, DateFormat.class));
//		FIELD_NAME_DATE_PATTERN.put(fieldName, annotation.map(DateFormat::value).orElse(DatePattern.NORM_DATETIME_PATTERN));
//		FIELD_NAME_IDENTITY.put(fieldName, FieldType.DATE);
//	}
//
//	private static <T> void processDataAndCache(List<T> data) {
//		extractYesNoMap();
//
//		String[] userField = new String[COUNT[0]];
//		String[] deptField = new String[COUNT[1]];
//
//		processUserAndDeptFields(userField, deptField);
//
//		userMap = getEntityMap(data, userField, User::getId, User::getRealName, UserCache::getUserByIds, "获取用户失败");
//		deptMap = getEntityMap(data, deptField, Dept::getId, Dept::getDeptName, SysCache::getDeptByIds, "获取部门失败");
//	}
//
//	protected static <T, E> Map<Long, String> getEntityMap(List<T> data, String[] field, Function<E, Long> idExtractor, Function<E, String> nameExtractor, Function<List<Long>, List<E>> entityRetriever, String errorMessage) {
//		String[] clearEmptyField = ArrayUtil.removeBlank(field);
//		if (ArrayUtil.isEmpty(clearEmptyField)) {
//			return Collections.emptyMap();
//		}
//
//		List<Long> ids = distinct(data, clearEmptyField);
//		CollUtil.removeNull(ids);
//		if (CollUtil.isEmpty(ids)) {
//			return Collections.emptyMap();
//		}
//
//		List<E> entities = entityRetriever.apply(ids);
//		if (CollUtil.isNotEmpty(entities)) {
//			return entities.stream().filter(item -> ObjectUtil.isNotNull(nameExtractor.apply(item))).collect(Collectors.toMap(idExtractor, nameExtractor));
//		} else {
//			Console.error(errorMessage);
//		}
//
//		return Collections.emptyMap();
//	}
//
//	private static void extractYesNoMap() {
//		if (MapUtil.isEmpty(yesNoMap)) {
//			List<Dict> yseNo = DictCache.getList(FieldType.YES_NO.name().toLowerCase());
//			if (CollUtil.isNotEmpty(yseNo)) {
//				yesNoMap = yseNo.stream().collect(Collectors.toMap(Dict::getDictKey, Dict::getDictValue));
//			} else {
//				Console.error("获取字典(是否)失败");
//			}
//		}
//	}
//
//	private static void processUserAndDeptFields(String[] userField, String[] deptField) {
//		if (FIELD_NAME_IDENTITY.containsValue(FieldType.USER) || FIELD_NAME_IDENTITY.containsValue(FieldType.DEPT)) {
//			for (Map.Entry<String, FieldType> entry : FIELD_NAME_IDENTITY.entrySet()) {
//				switch (entry.getValue()) {
//					case USER:
//						userField[--COUNT[0]] = entry.getKey();
//						break;
//					case DEPT:
//						deptField[--COUNT[1]] = entry.getKey();
//						break;
//					default:
//						break;
//				}
//			}
//		}
//	}
//
//	private static <T> List<Long> distinct(@NotNull List<T> data, String[] nameField) {
//		return data.parallelStream().flatMap(entity -> Arrays.stream(nameField).map(name -> Convert.toLong(ReflectUtil.getFieldValue(entity, name)))).distinct().collect(Collectors.toList());
//	}
//
//
//	private static <T> T reflectionField(T data) {
//		for (Map.Entry<String, FieldType> entry : FIELD_NAME_IDENTITY.entrySet()) {
//			Object value = ReflectUtil.getFieldValue(data, entry.getKey());
//			switch (entry.getValue()) {
//				case USER:
//					ReflectUtil.setFieldValue(data, entry.getKey(), userMap.getOrDefault(Convert.toLong(value), null));
//					break;
//				case DEPT:
//					ReflectUtil.setFieldValue(data, entry.getKey(), deptMap.getOrDefault(Convert.toLong(value), Convert.toStr(value)));
//					break;
//				case YES_NO:
//					ReflectUtil.setFieldValue(data, entry.getKey(), yesNoMap.getOrDefault(Convert.toStr(value), null));
//					break;
//				case DATE:
//					processDateField(data, entry.getKey(), value);
//					break;
//				case WEEK:
//					processWeekField(data, entry.getKey(), value);
//					break;
//				default:
//					break;
//			}
//		}
//		return data;
//	}
//
//	private static <T> void processDateField(T data, String fieldName, Object value) {
//		Long date = Convert.toLong(value);
//		if (ObjectUtil.isNotNull(date)) {
//			ReflectUtil.setFieldValue(data, fieldName, DateUtil.date(date).toString(FIELD_NAME_DATE_PATTERN.get(fieldName)));
//		}
//	}
//
//	private static <T> void processWeekField(T data, String fieldName, Object value) {
//		Long longValue = Convert.toLong(value);
//		if (ObjectUtil.isNotNull(longValue)) {
//			ReflectUtil.setFieldValue(data, fieldName, FormatUtil.weekOfYear(longValue));
//		}
//	}
//
//	@NotNull
//	public static <T> List<T> format(List<T> data) {
//		if (CollUtil.isEmpty(data)) return Collections.emptyList();
//		if (initData(data, true)) {
//			try {
//				return data.parallelStream().map(ExportUtil::reflectionField).collect(Collectors.toList());
//			} finally {
//				clear();
//			}
//		} else {
//			return data;
//		}
//	}
//
//	@NotNull
//	public static <T> List<T> format(List<T> data, @NotNull Map<String, String> dict, @NotNull Map<String, Map<String, String>> customizeDict, boolean isRunAnnotation) {
//		if (CollUtil.isEmpty(data)) return Collections.emptyList();
//
//		validateFields(data, dict, customizeDict);
//		initData(data, isRunAnnotation);
//		Map<String, Map<String, String>> dictData = convertDictToMap(dict);
//
//		data.parallelStream().forEach(item -> {
//			if (isRunAnnotation) reflectionField(item);
//
//			processFieldMappings(dictData, dict, item);
//			processFieldMappings(customizeDict, Collections.emptyMap(), item);
//		});
//		clear();
//		return data;
//	}
//
//	private static <T> void validateFields(List<T> data, @NotNull Map<String, String> dict, @NotNull Map<String, Map<String, String>> customizeDict) {
//		dict.values().stream().flatMap(value -> Arrays.stream(value.split(StrPool.COMMA))).forEach(fieldName -> validateFieldType(data.get(0).getClass(), fieldName));
//		customizeDict.keySet().forEach(key -> validateFieldType(data.get(0).getClass(), key));
//	}
//
//	private static void validateFieldType(Class<?> clazz, String fieldName) {
//		Type fieldType = TypeUtil.getFieldType(clazz, fieldName);
//		typeConversionException(fieldType, fieldName);
//	}
//
//	@NotNull
//	private static Map<String, Map<String, String>> convertDictToMap(Map<String, String> dict) {
//		if (MapUtil.isNotEmpty(dict)) {
//			Map<String, Map<String, String>> dictData = new HashMap<>();
//			String[] code = String.join(StrPool.COMMA, dict.keySet()).split(StrPool.COMMA);
//			Map<String, List<DictBiz>> dictionary = DictBizCache.getDict(code);
//			if (MapUtil.isNotEmpty(dictionary)) {
//				dictionary.forEach((key, valueList) -> dictData.put(key, valueList.stream().collect(Collectors.toMap(DictBiz::getDictKey, DictBiz::getDictValue))));
//			} else {
//				Console.error("获取", Arrays.toString(code), "业务字典失败");
//			}
//			return dictData;
//		}
//
//		return Collections.emptyMap();
//	}
//
//	private static <T> void processFieldMappings(@NotNull Map<String, Map<String, String>> dataMap, Map<String, String> dictMap, T item) {
//		dataMap.forEach((key, valueMap) -> {
//			if (MapUtil.isNotEmpty(dictMap)) {
//				for (String fieldName : dictMap.get(key).split(StrPool.COMMA)) {
//					String value = Convert.toStr(ReflectUtil.getFieldValue(item, fieldName));
//					ReflectUtil.setFieldValue(item, fieldName, valueMap.getOrDefault(value, value));
//				}
//			} else {
//				String value = Convert.toStr(ReflectUtil.getFieldValue(item, key));
//				ReflectUtil.setFieldValue(item, key, valueMap.getOrDefault(value, value));
//			}
//		});
//	}
//
//}
