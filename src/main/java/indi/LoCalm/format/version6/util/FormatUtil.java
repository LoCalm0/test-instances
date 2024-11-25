//package indi.LoCalm.format.version6.util;
//
//import cn.hutool.core.collection.CollUtil;
//import cn.hutool.core.convert.Convert;
//import cn.hutool.core.date.DatePattern;
//import cn.hutool.core.date.DateTime;
//import cn.hutool.core.date.DateUtil;
//import cn.hutool.core.date.LocalDateTimeUtil;
//import cn.hutool.core.lang.Console;
//import cn.hutool.core.lang.Opt;
//import cn.hutool.core.lang.func.Consumer3;
//import cn.hutool.core.lang.func.LambdaUtil;
//import cn.hutool.core.map.MapUtil;
//import cn.hutool.core.text.CharSequenceUtil;
//import cn.hutool.core.text.StrPool;
//import cn.hutool.core.text.finder.StrFinder;
//import cn.hutool.core.text.split.SplitIter;
//import cn.hutool.core.util.ArrayUtil;
//import cn.hutool.core.util.ClassUtil;
//import cn.hutool.core.util.ReUtil;
//import cn.hutool.core.util.ReflectUtil;
//import com.imtristone.format.annotition.*;
//import com.imtristone.system.cache.DictBizCache;
//import com.imtristone.system.cache.DictCache;
//import com.imtristone.system.cache.SysCache;
//import com.imtristone.system.cache.UserCache;
//import com.imtristone.system.entity.*;
//import org.jetbrains.annotations.Contract;
//import org.jetbrains.annotations.NotNull;
//import org.jetbrains.annotations.Nullable;
//import org.springframework.core.annotation.AnnotationUtils;
//
//import java.io.Serializable;
//import java.lang.annotation.Annotation;
//import java.time.DayOfWeek;
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.time.temporal.TemporalAccessor;
//import java.time.temporal.TemporalUnit;
//import java.time.temporal.WeekFields;
//import java.util.*;
//import java.util.function.Consumer;
//import java.util.function.Function;
//import java.util.function.LongFunction;
//import java.util.stream.Collectors;
//import java.util.stream.Stream;
//
///**
// * 格式化工具
// *
// * @author LoCalm
// */
//public class FormatUtil {
//
//	private FormatUtil() {
//	}
//
//	private static final String GET_SEPARATOR = LambdaUtil.getMethodName(UserFormat::getSeparator);
//	private static final String SET_SEPARATOR = LambdaUtil.getMethodName(UserFormat::setSeparator);
//	private static final String SET_FIELD_NAME = LambdaUtil.getMethodName(UserFormat::setFieldName);
//	private static final String PATTERN = LambdaUtil.getMethodName(DateTimeFormat::pattern);
//	private static final String DAY_OF_WEEK = LambdaUtil.getMethodName(DateTimeFormat::dayOfWeek);
//	private static final String SEPARATOR = LambdaUtil.getMethodName(DateTimeFormat::separator);
//
//	private static final ThreadLocal<Map<String, FieldType>> SET_IDENTITY = ThreadLocal.withInitial(HashMap::new);
//	private static final ThreadLocal<Map<String, String>> SET_GET_FIELD_NAME = ThreadLocal.withInitial(HashMap::new);
//	private static final ThreadLocal<Map<String, ? super Object>> SET_ANNOTATION_VALUE = ThreadLocal.withInitial(HashMap::new);
//
//
//	private static final List<Class<? extends Annotation>> ANNOTATION_TYPES = Arrays.asList(UserFormat.class, DeptFormat.class, DictFormat.class, DictBizFormat.class, DateTimeFormat.class, WeekFormat.class, TimestampFormat.class);
//
//	private static boolean init(Class<?> clazz) {
//		Map<String, String> setGetFieldName = SET_GET_FIELD_NAME.get();
//		Arrays.stream(ClassUtil.getDeclaredFields(clazz)).forEach(field -> Arrays.stream(field.getDeclaredAnnotations()).filter(annotation -> ANNOTATION_TYPES.contains(annotation.annotationType())).forEach(annotation -> {
//			String fieldName = field.getName();
//			Annotation annotationData = AnnotationUtils.getAnnotation(field, annotation.annotationType());
//			String value = ReflectUtil.invoke(annotationData, LambdaUtil.getMethodName(UserFormat::value));
//			if (CharSequenceUtil.isBlank(value)) value = fieldName;
//
//			Objects.requireNonNull(ReflectUtil.getField(clazz, value), value + "字段不存在");
//
//			setGetFieldName.put(fieldName, value);
//
//			assert annotationData != null;
//			processFieldAnnotations(annotationData, fieldName);
//		}));
//		return MapUtil.isEmpty(setGetFieldName);
//	}
//
//	private static void processFieldAnnotations(@NotNull Annotation annotation, String fieldName) {
//		Map<String, FieldType> setIdentity = SET_IDENTITY.get();
//		Map<String, ? super Object> setAnnotationValue = SET_ANNOTATION_VALUE.get();
//		switch (Objects.requireNonNull(FieldType.of(annotation.annotationType().getSimpleName()))) {
//			case USER:
//				setIdentity.put(fieldName, FieldType.USER);
//				setAnnotationValue.put(fieldName, Opt.ofNullable((UserFormat) annotation).map(userFormat -> MapUtil.builder(new HashMap<String, Object>()).put(SET_FIELD_NAME, userFormat.setFieldName().getField()).put(GET_SEPARATOR, Opt.ofBlankAble(userFormat.getSeparator()).orElse(null)).put(SET_SEPARATOR, Opt.ofBlankAble(userFormat.setSeparator()).orElse(Opt.ofBlankAble(userFormat.getSeparator()).orElse(StrPool.COMMA))).build()).orElse(Collections.emptyMap()));
//				break;
//			case DEPT:
//				setIdentity.put(fieldName, FieldType.DEPT);
//				setAnnotationValue.put(fieldName, Opt.ofNullable((DeptFormat) annotation).map(deptFormat -> MapUtil.builder(new HashMap<String, Object>()).put(SET_FIELD_NAME, deptFormat.setFieldName().getField()).put(GET_SEPARATOR, Opt.ofBlankAble(deptFormat.getSeparator()).orElse(null)).put(SET_SEPARATOR, Opt.ofBlankAble(deptFormat.setSeparator()).orElse(Opt.ofBlankAble(deptFormat.getSeparator()).orElse(StrPool.COMMA))).build()).orElse(Collections.emptyMap()));
//				break;
//			case DICT:
//				setIdentity.put(fieldName, FieldType.DICT);
//				setAnnotationValue.put(fieldName, Opt.ofNullable((DictFormat) annotation).map(dictFormat -> Opt.ofBlankAble(dictFormat.code() == DictFormat.Code.EMPTY ? dictFormat.strCode().toLowerCase() : dictFormat.code().name().toLowerCase()).orElseThrow(NullPointerException::new, "code和strCode为空")).get());
//				break;
//			case DICT_BIZ:
//				setIdentity.put(fieldName, FieldType.DICT_BIZ);
//				setAnnotationValue.put(fieldName, Opt.ofNullable((DictBizFormat) annotation).map(dictBizFormat -> Opt.ofBlankAble(dictBizFormat.code() == DictBizFormat.Code.EMPTY ? dictBizFormat.strCode().toLowerCase() : dictBizFormat.code().name().toLowerCase()).orElseThrow(NullPointerException::new, "code和strCode为空")).get());
//				break;
//			case DATE_TIME:
//				setIdentity.put(fieldName, FieldType.DATE_TIME);
//				setAnnotationValue.put(fieldName, Opt.ofNullable((DateTimeFormat) annotation).map(dateTimeFormat -> MapUtil.builder(new HashMap<String, Object>()).put(PATTERN, Opt.ofBlankAble(dateTimeFormat.pattern()).orElse(DatePattern.NORM_DATETIME_PATTERN)).put(DAY_OF_WEEK, dateTimeFormat.dayOfWeek()).put(SEPARATOR, Opt.ofBlankAble(dateTimeFormat.separator()).orElse(StrPool.DASHED)).build()).orElse(Collections.emptyMap()));
//				break;
//			case TIMESTAMP:
//				setIdentity.put(fieldName, FieldType.TIMESTAMP);
//				setAnnotationValue.put(fieldName, Opt.ofNullable((TimestampFormat) annotation).map(timestampFormat -> MapUtil.builder(new HashMap<String, Object>()).put(DAY_OF_WEEK, timestampFormat.dayOfWeek()).put(SEPARATOR, Opt.ofBlankAble(timestampFormat.separator()).orElse(StrPool.DASHED)).build()).orElse(Collections.emptyMap()));
//				break;
//			case WEEK:
//				setIdentity.put(fieldName, FieldType.WEEK);
//				break;
//			default:
//				break;
//		}
//	}
//
//	private static void clear() {
//		SET_IDENTITY.remove();
//		SET_GET_FIELD_NAME.remove();
//		SET_ANNOTATION_VALUE.remove();
//	}
//
//	@Nullable
//	public static <V> V format(V vo) {
//		try {
//			if (null == vo) return null;
//
//			if (init(vo.getClass())) return vo;
//
//			reflectionField(vo, 0, initData(vo));
//			return vo;
//		} finally {
//			clear();
//		}
//	}
//
//	@NotNull
//	public static <V> List<V> format(List<V> vo) {
//		return format(vo, null);
//	}
//
//	@NotNull
//	public static <V> List<V> format(List<V> vo, @Nullable Consumer3<V, Integer, Map<FieldType, Map<?, ?>>> run) {
//		try {
//			if (CollUtil.isEmpty(vo)) return Collections.emptyList();
//
//			if (init(vo.get(0).getClass())) return vo;
//
//			forEach(vo.iterator(), initData(vo), ConsumerBuilder.<V>builder().add(FormatUtil::reflectionField).add(run).build());
//			return vo;
//		} finally {
//			clear();
//		}
//	}
//
//	private static <V> void forEach(@NotNull Iterator<V> iterator, Map<FieldType, Map<?, ?>> data, Consumer3<V, Integer, Map<FieldType, Map<?, ?>>> consumer) {
//		int index = 0;
//		while (iterator.hasNext()) {
//			consumer.accept(iterator.next(), index, data);
//			index++;
//		}
//	}
//
//	@SuppressWarnings("unchecked")
//	private static <V> void reflectionField(V vo, int index, Map<FieldType, Map<?, ?>> data) {
//		Map<String, String> setGetFieldName = SET_GET_FIELD_NAME.get();
//		Map<String, ? super Object> setAnnotationValue = SET_ANNOTATION_VALUE.get();
//		Map<Long, User> userMap = (Map<Long, User>) data.get(FieldType.USER);
//		Map<Long, Dept> deptMap = (Map<Long, Dept>) data.get(FieldType.DEPT);
//		Map<String, Map<String, String>> dictMap = (Map<String, Map<String, String>>) data.get(FieldType.DICT);
//		Map<String, Map<String, String>> dictBizMap = (Map<String, Map<String, String>>) data.get(FieldType.DICT_BIZ);
//		for (Map.Entry<String, FieldType> entry : SET_IDENTITY.get().entrySet()) {
//			Object value = ReflectUtil.getFieldValue(vo, setGetFieldName.get(entry.getKey()));
//			switch (entry.getValue()) {
//				case USER:
//					Opt.ofNullable(userMap.get(Convert.toLong(value))).ifPresentOrElse(user -> ReflectUtil.setFieldValue(vo, entry.getKey(), ((Map<String, Function<User, ?>>) setAnnotationValue.get(entry.getKey())).get(SET_FIELD_NAME).apply(user)), () -> Opt.ofEmptyAble((List<Long>) setAnnotationValue.get(Convert.toStr(value))).ifPresent(ids -> Opt.ofNullable(((Map<String, Object>) setAnnotationValue.get(entry.getKey()))).ifPresent(userFieldMap -> ReflectUtil.setFieldValue(vo, entry.getKey(), MapUtil.valuesOfKeys((Map<Long, User>) data.get(FieldType.USER), ids.iterator()).stream().filter(Objects::nonNull).map(user -> Convert.toStr(((Function<User, ?>) userFieldMap.get(SET_FIELD_NAME)).apply(user))).collect(Collectors.joining((CharSequence) userFieldMap.get(SET_SEPARATOR)))))));
//					break;
//				case DEPT:
//					Opt.ofNullable(deptMap.get(Convert.toLong(value))).ifPresentOrElse(dept -> ReflectUtil.setFieldValue(vo, entry.getKey(), ((Map<String, Function<Dept, ?>>) setAnnotationValue.get(entry.getKey())).get(SET_FIELD_NAME).apply(dept)), () -> Opt.ofEmptyAble((List<Long>) setAnnotationValue.get(Convert.toStr(value))).ifPresent(ids -> Opt.ofNullable(((Map<String, Object>) setAnnotationValue.get(entry.getKey()))).ifPresent(deptFieldMap -> ReflectUtil.setFieldValue(vo, entry.getKey(), MapUtil.valuesOfKeys((Map<Long, Dept>) data.get(FieldType.DEPT), ids.iterator()).stream().filter(Objects::nonNull).map(dept -> Convert.toStr(((Function<Dept, ?>) deptFieldMap.get(SET_FIELD_NAME)).apply(dept))).collect(Collectors.joining((CharSequence) deptFieldMap.get(SET_SEPARATOR)))))));
//					break;
//				case DICT:
//					Opt.ofNullable(dictMap.get(Convert.toStr(setAnnotationValue.get(entry.getKey())))).ifPresent(dict -> Opt.ofNullable(dict.get(Convert.toStr(value))).ifPresent(fieldValue -> ReflectUtil.setFieldValue(vo, entry.getKey(), fieldValue)));
//					break;
//				case DICT_BIZ:
//					Opt.ofNullable(dictBizMap.get(Convert.toStr(setAnnotationValue.get(entry.getKey())))).ifPresent(dict -> Opt.ofNullable(dict.get(Convert.toStr(value))).ifPresent(fieldValue -> ReflectUtil.setFieldValue(vo, entry.getKey(), fieldValue)));
//					break;
//				case DATE_TIME:
//					Opt.ofNullable((Map<String, Object>) setAnnotationValue.get(entry.getKey())).ifPresent(dateTime -> Opt.ofNullable(getTimestamp(value, Convert.toStr(dateTime.get(SEPARATOR)), Convert.convert(DayOfWeek.class, dateTime.get(DAY_OF_WEEK)))).ifPresent(fieldValue -> ReflectUtil.setFieldValue(vo, entry.getKey(), DateUtil.date(fieldValue).toString(Convert.toStr(dateTime.get(PATTERN))))));
//					break;
//				case TIMESTAMP:
//					Opt.ofNullable((Map<String, Object>) setAnnotationValue.get(entry.getKey())).ifPresent(timestamp -> Opt.ofNullable(getTimestamp(value, Convert.toStr(timestamp.get(SEPARATOR)), Convert.convert(DayOfWeek.class, timestamp.get(DAY_OF_WEEK)))).ifPresent(fieldValue -> ReflectUtil.setFieldValue(vo, entry.getKey(), fieldValue)));
//					break;
//				case WEEK:
//					Opt.ofNullable(getTimestamp(value)).ifPresent(fieldValue -> ReflectUtil.setFieldValue(vo, entry.getKey(), weekOfYear(fieldValue)));
//					break;
//				default:
//					break;
//			}
//		}
//	}
//
//	@NotNull
//	private static <V> Map<FieldType, Map<?, ?>> initData(V vo) {
//		Map<FieldType, Map<String, String>> fieldMap = getFieldMapByIdentity();
//		if (MapUtil.isEmpty(fieldMap)) return Collections.emptyMap();
//		Map<FieldType, Map<?, ?>> data = new EnumMap<>(FieldType.class);
//		Opt.ofNullable(fieldMap.get(FieldType.USER)).ifPresent(map -> data.put(FieldType.USER, getEntityMap(getEntityMapBefore(Opt.ofNullable(fieldMap.get(FieldType.USER)).map(Map::keySet).orElse(Collections.emptySet())), Opt.ofNullable(fieldMap.get(FieldType.USER)).map(Map::values).orElse(Collections.emptyList()), vo, UserCache::getUser, UserCache::getUserByIds, User::getId, "获取用户失败")));
//		Opt.ofNullable(fieldMap.get(FieldType.DEPT)).ifPresent(map -> data.put(FieldType.DEPT, getEntityMap(getEntityMapBefore(Opt.ofNullable(fieldMap.get(FieldType.DEPT)).map(Map::keySet).orElse(Collections.emptySet())), Opt.ofNullable(fieldMap.get(FieldType.DEPT)).map(Map::values).orElse(Collections.emptyList()), vo, SysCache::getDept, SysCache::getDeptByIds, Dept::getId, "获取部门失败")));
//		Opt.ofNullable(fieldMap.get(FieldType.DICT)).ifPresent(map -> data.put(FieldType.DICT, getDictMap(map.keySet().toArray(new String[0]), DictCache::getList, Dict::getCode, Dict::getDictKey, Dict::getDictValue)));
//		Opt.ofNullable(fieldMap.get(FieldType.DICT_BIZ)).ifPresent(map -> data.put(FieldType.DICT_BIZ, getDictMap(map.keySet().toArray(new String[0]), DictBizCache::getList, DictBiz::getCode, DictBiz::getDictKey, DictBiz::getDictValue)));
//		return data;
//	}
//
//	@NotNull
//	private static <V> Map<FieldType, Map<?, ?>> initData(List<V> vo) {
//		Map<FieldType, Map<String, String>> fieldMap = getFieldMapByIdentity();
//		if (MapUtil.isEmpty(fieldMap)) return Collections.emptyMap();
//		Map<FieldType, Map<?, ?>> data = new EnumMap<>(FieldType.class);
//
//		Set<Long> userIds = new HashSet<>();
//		Set<Long> deptIds = new HashSet<>();
//
//
//		Consumer<V> userRun = getRun(getEntityMapBefore(Opt.ofNullable(fieldMap.get(FieldType.USER)).map(Map::keySet).orElse(Collections.emptySet())), Opt.ofNullable(fieldMap.get(FieldType.USER)).map(Map::values).orElse(Collections.emptyList()), userIds);
//		Consumer<V> deptRun = getRun(getEntityMapBefore(Opt.ofNullable(fieldMap.get(FieldType.DEPT)).map(Map::keySet).orElse(Collections.emptySet())), Opt.ofNullable(fieldMap.get(FieldType.DEPT)).map(Map::values).orElse(Collections.emptyList()), deptIds);
//
//		vo.parallelStream().forEach(entity -> {
//			for (FieldType fieldType : fieldMap.keySet()) {
//				switch (fieldType) {
//					case USER:
//						userRun.accept(entity);
//						break;
//					case DEPT:
//						deptRun.accept(entity);
//						break;
//					default:
//						break;
//				}
//			}
//		});
//
//		Opt.ofNullable(fieldMap.get(FieldType.USER)).ifPresent(e -> data.put(FieldType.USER, getEntityMapAfter(userIds, UserCache::getUserByIds, User::getId, "获取用户失败")));
//		Opt.ofNullable(fieldMap.get(FieldType.DEPT)).ifPresent(e -> data.put(FieldType.DEPT, getEntityMapAfter(deptIds, SysCache::getDeptByIds, Dept::getId, "获取部门失败")));
//		Opt.ofNullable(fieldMap.get(FieldType.DICT)).ifPresent(map -> data.put(FieldType.DICT, getDictMap(map.keySet().toArray(new String[0]), DictCache::getList, Dict::getCode, Dict::getDictKey, Dict::getDictValue)));
//		Opt.ofNullable(fieldMap.get(FieldType.DICT_BIZ)).ifPresent(map -> data.put(FieldType.DICT_BIZ, getDictMap(map.keySet().toArray(new String[0]), DictBizCache::getList, DictBiz::getCode, DictBiz::getDictKey, DictBiz::getDictValue)));
//		return data;
//	}
//
//	@NotNull
//	public static <V> Consumer<V> getRun(Map<String, String> fieldNameSeparator, Collection<String> getField, Set<Long> ids) {
//		if (CollUtil.isEmpty(getField)) return entity -> {
//		};
//		if (MapUtil.isEmpty(fieldNameSeparator)) {
//			return entity -> getField.forEach(name -> ids.add(Convert.toLong(ReflectUtil.getFieldValue(entity, name))));
//		} else {
//			Map<String, ? super Object> setAnnotationValue = SET_ANNOTATION_VALUE.get();
//			Set<String> filter = getField.stream().filter(field -> !fieldNameSeparator.containsKey(field)).collect(Collectors.toSet());
//			return entity -> {
//				getSeparatorIds(fieldNameSeparator, entity, setAnnotationValue, ids);
//				filter.forEach(name -> ids.add(Convert.toLong(ReflectUtil.getFieldValue(entity, name))));
//			};
//		}
//	}
//
//	public static <V, T> Map<Long, T> getEntityMap(Map<String, String> fieldNameSeparator, Collection<String> getField, V vo, LongFunction<T> retriever, Function<Set<Long>, List<T>> entityRetriever, Function<T, Long> idExtractor, String errorMessage) {
//		if (MapUtil.isEmpty(fieldNameSeparator) && getField.size() == 1) {
//			return Opt.ofNullable(Convert.toLong(ReflectUtil.getFieldValue(vo, getField.iterator().next()))).map(id -> Opt.ofNullable(retriever.apply(id)).map(entity -> Collections.singletonMap(id, entity)).orElse(Collections.emptyMap())).orElse(Collections.emptyMap());
//		} else {
//			Set<Long> ids = new HashSet<>();
//			Map<String, ? super Object> setAnnotationValue = SET_ANNOTATION_VALUE.get();
//			getSeparatorIds(fieldNameSeparator, vo, setAnnotationValue, ids);
//
//			Set<String> filter = getField.stream().filter(field -> !fieldNameSeparator.containsKey(field)).collect(Collectors.toSet());
//			ids.addAll(CollUtil.removeNull(filter.stream().map(name -> Convert.toLong(ReflectUtil.getFieldValue(vo, name))).collect(Collectors.toList())));
//
//			return getEntityMapAfter(ids, entityRetriever, idExtractor, errorMessage);
//		}
//	}
//
//	private static <V> void getSeparatorIds(@NotNull Map<String, String> fieldNameSeparator, V entity, Map<String, ? super Object> setAnnotationValue, Set<Long> ids) {
//		for (Map.Entry<String, String> entry : fieldNameSeparator.entrySet()) {
//			String fieldValue = Convert.toStr(ReflectUtil.getFieldValue(entity, entry.getKey()));
//			if (CharSequenceUtil.isBlank(fieldValue)) continue;
//			List<Long> separatorData = getSeparatorData(fieldValue, entry.getValue());
//			setAnnotationValue.put(fieldValue, separatorData);
//			ids.addAll(separatorData);
//		}
//	}
//
//	@NotNull
//	@SuppressWarnings("unchecked")
//	private static Map<String, String> getEntityMapBefore(Set<String> setFields) {
//		if (CollUtil.isEmpty(setFields)) return Collections.emptyMap();
//		Map<String, String> fieldNameSeparator = new HashMap<>();
//		Map<String, ? super Object> setAnnotationValue = SET_ANNOTATION_VALUE.get();
//		Map<String, String> setGetFieldName = SET_GET_FIELD_NAME.get();
//		setFields.forEach(fieldName -> Opt.ofBlankAble(((Map<String, String>) setAnnotationValue.get(fieldName)).get(GET_SEPARATOR)).ifPresent(separator -> fieldNameSeparator.put(setGetFieldName.get(fieldName), separator)));
//		return fieldNameSeparator;
//	}
//
//	private static <T, E> Map<E, T> getEntityMapAfter(Set<E> ids, Function<Set<E>, List<T>> entityRetriever, Function<T, E> idExtractor, String errorMessage) {
//		CollUtil.removeNull(ids);
//		if (CollUtil.isEmpty(ids)) return Collections.emptyMap();
//		List<T> entities = entityRetriever.apply(ids);
//		if (CollUtil.isNotEmpty(entities)) {
//			return entities.stream().collect(Collectors.toMap(idExtractor, Function.identity()));
//		} else {
//			Console.error(errorMessage);
//		}
//
//		return Collections.emptyMap();
//	}
//
//	@NotNull
//	private static Map<FieldType, Map<String, String>> getFieldMapByIdentity() {
//		Map<FieldType, Map<String, String>> fieldMap = new EnumMap<>(FieldType.class);
//		Map<String, FieldType> setIdentity = SET_IDENTITY.get();
//		Map<String, String> setGetFieldName = SET_GET_FIELD_NAME.get();
//		Map<String, ? super Object> setAnnotationValue = SET_ANNOTATION_VALUE.get();
//		if (Stream.of(FieldType.USER, FieldType.DEPT, FieldType.DICT, FieldType.DICT_BIZ).anyMatch(setIdentity::containsValue)) {
//			for (Map.Entry<String, FieldType> entry : setIdentity.entrySet()) {
//				switch (entry.getValue()) {
//					case USER:
//						Opt.ofNullable(fieldMap.get(FieldType.USER)).ifPresentOrElse(map -> map.put(entry.getKey(), setGetFieldName.get(entry.getKey())), () -> fieldMap.put(FieldType.USER, MapUtil.builder(new HashMap<String, String>()).put(entry.getKey(), setGetFieldName.get(entry.getKey())).build()));
//						break;
//					case DEPT:
//						Opt.ofNullable(fieldMap.get(FieldType.DEPT)).ifPresentOrElse(map -> map.put(entry.getKey(), setGetFieldName.get(entry.getKey())), () -> fieldMap.put(FieldType.DEPT, MapUtil.builder(new HashMap<String, String>()).put(entry.getKey(), setGetFieldName.get(entry.getKey())).build()));
//						break;
//					case DICT:
//						Opt.ofNullable(fieldMap.get(FieldType.DICT)).ifPresentOrElse(map -> map.put(Convert.toStr(setAnnotationValue.get(entry.getKey())), null), () -> fieldMap.put(FieldType.DICT, MapUtil.builder(new HashMap<String, String>()).put(Convert.toStr(setAnnotationValue.get(entry.getKey())), null).build()));
//						break;
//					case DICT_BIZ:
//						Opt.ofNullable(fieldMap.get(FieldType.DICT_BIZ)).ifPresentOrElse(map -> map.put(Convert.toStr(setAnnotationValue.get(entry.getKey())), null), () -> fieldMap.put(FieldType.DICT_BIZ, MapUtil.builder(new HashMap<String, String>()).put(Convert.toStr(setAnnotationValue.get(entry.getKey())), null).build()));
//						break;
//					default:
//						break;
//				}
//			}
//		}
//		return fieldMap;
//	}
//
//	private static <T> Map<String, Map<String, String>> getDictMap(String[] dictField, Function<String, List<T>> entityRetriever, Function<T, String> code, Function<T, String> key, Function<T, String> value) {
//		if (ArrayUtil.isEmpty(dictField)) return Collections.emptyMap();
//		List<T> data = new ArrayList<>();
//		for (String field : dictField) data.addAll(entityRetriever.apply(field));
//		if (CollUtil.isEmpty(data)) return Collections.emptyMap();
//		return data.stream().collect(Collectors.groupingBy(code, Collectors.toMap(key, value)));
//	}
//
//	public static List<Long> getSeparatorData(@NotNull String fieldValueStr, String separator) {
//		if (ReUtil.isMatch(CharSequenceUtil.format("[0-9{}]+", separator), fieldValueStr)) {
//			final SplitIter splitIter = new SplitIter(fieldValueStr, new StrFinder(separator, false), -1, true);
//			return splitIter.toList(Convert::toLong);
//		}
//		return Collections.emptyList();
//	}
//
//	private static class ConsumerBuilder<T> implements Serializable {
//		private ConsumerBuilder() {
//		}
//
//		private final List<Consumer3<T, Integer, Map<FieldType, Map<?, ?>>>> consumers = new ArrayList<>();
//
//		public ConsumerBuilder<T> add(Consumer3<T, Integer, Map<FieldType, Map<?, ?>>> consumer) {
//			if (consumer != null) consumers.add(consumer);
//			return this;
//		}
//
//		@Nullable
//		public Consumer3<T, Integer, Map<FieldType, Map<?, ?>>> build() {
//			if (CollUtil.isEmpty(consumers)) return null;
//			return (value, index, data) -> consumers.forEach(consumer -> consumer.accept(value, index, data));
//		}
//
//		@NotNull
//		@Contract(" -> new")
//		public static <T> ConsumerBuilder<T> builder() {
//			return new ConsumerBuilder<>();
//		}
//	}
//
//	@Contract("null -> null")
//	public static String getUserName(Long userId) {
//		return getUserName(userId, null);
//	}
//
//	@Contract("null,_ -> param2")
//	public static String getUserName(Long userId, String defaultValue) {
//		return getEntityFieldValue(userId, UserCache::getUser, User::getName, defaultValue);
//	}
//
//	@Contract("null -> null")
//	public static String getRealName(Long userId) {
//		return getRealName(userId, null);
//	}
//
//	@Contract("null,_ -> param2")
//	public static String getRealName(Long userId, String defaultValue) {
//		return getEntityFieldValue(userId, UserCache::getUser, User::getRealName, defaultValue);
//	}
//
//	@Contract("null -> null")
//	public static String getDeptName(Long deptId) {
//		return getDeptName(deptId, null);
//	}
//
//	@Contract("null,_ -> param2")
//	public static String getDeptName(Long deptId, String defaultValue) {
//		return getEntityFieldValue(deptId, SysCache::getDept, Dept::getDeptName, defaultValue);
//	}
//
//	@Contract("null -> null")
//	public static String getTenantName(String tenantId) {
//		return getTenantName(tenantId, null);
//	}
//
//	@Contract("null,_ -> param2")
//	public static String getTenantName(String tenantId, String defaultValue) {
//		return getEntityFieldValue(tenantId, SysCache::getTenant, Tenant::getTenantName, defaultValue);
//	}
//
//	@Contract("null,_,_ -> null")
//	public static <T, R, E> E getEntityFieldValue(T value, Function<T, R> cacheRetriever, Function<R, E> nameExtractor) {
//		return getEntityFieldValue(value, cacheRetriever, nameExtractor, null);
//	}
//
//	@Contract("null,_,_,_ -> param4")
//	public static <T, R, E> E getEntityFieldValue(T value, Function<T, R> cacheRetriever, Function<R, E> nameExtractor, E defaultValue) {
//		return Opt.ofNullable(value).map(v -> Opt.ofNullable(cacheRetriever.apply(v)).map(nameExtractor).orElse(defaultValue)).orElse(defaultValue);
//	}
//
//	@Contract("null -> null")
//	public static String weekOfYear(String datetime) {
//		return weekOfYear(datetime, null);
//	}
//
//	@Contract("null,_ -> param2")
//	public static String weekOfYear(String datetime, String defaultValue) {
//		return weekOfYear(datetime, StrPool.DASHED, defaultValue);
//	}
//
//	@Contract("null,_,_ -> param3")
//	public static String weekOfYear(String datetime, String separator, String defaultValue) {
//		return Opt.ofBlankAble(datetime).map(DateUtil::parse).map(DateUtil::toLocalDateTime).map(localDateTime -> weekOfYear(localDateTime, separator, defaultValue)).orElse(defaultValue);
//	}
//
//	@Contract("null -> null")
//	public static String weekOfYear(Long timestamp) {
//		return weekOfYear(timestamp, null);
//	}
//
//	@Contract("null,_ -> param2")
//	public static String weekOfYear(Long timestamp, String defaultValue) {
//		return weekOfYear(timestamp, StrPool.DASHED, defaultValue);
//	}
//
//	@Contract("null,_,_ -> param3")
//	public static String weekOfYear(Long timestamp, String separator, String defaultValue) {
//		return Opt.ofNullable(timestamp).map(DateUtil::date).map(DateUtil::toLocalDateTime).map(localDateTime -> weekOfYear(localDateTime, separator, defaultValue)).orElse(defaultValue);
//	}
//
//	@Contract("null -> null")
//	public static String weekOfYear(LocalDateTime localDateTime) {
//		return weekOfYear(localDateTime, null);
//	}
//
//	@Contract("null,_ -> param2")
//	public static String weekOfYear(LocalDateTime localDateTime, String defaultValue) {
//		return weekOfYear(localDateTime, StrPool.DASHED, defaultValue);
//	}
//
//	@Contract("null,_,_ -> param3")
//	public static String weekOfYear(LocalDateTime localDateTime, String separator, String defaultValue) {
//		return Opt.ofNullable(localDateTime).map(LocalDateTime::toLocalDate).map(localDate -> weekOfYear(localDate, separator, defaultValue)).orElse(defaultValue);
//	}
//
//	@Contract("null -> null")
//	public static String weekOfYear(LocalDate localDate) {
//		return weekOfYear(localDate, null);
//	}
//
//	@Contract("null,_ -> param2")
//	public static String weekOfYear(LocalDate localDate, String defaultValue) {
//		return weekOfYear(localDate, StrPool.DASHED, defaultValue);
//	}
//
//	@Contract("null,_,_ -> param3")
//	public static String weekOfYear(LocalDate localDate, String separator, String defaultValue) {
//		return Opt.ofNullable(localDate).map(date -> date.getYear() + separator + date.get(WeekFields.ISO.weekOfYear())).orElse(defaultValue);
//	}
//
//	@Contract("_,_,_,null -> fail")
//	public static Long[] getStartAndEndTimestamp(int year, int month, int day, TemporalUnit unit) {
//		return getStartAndEndTimestamp(LocalDate.of(year, month, day), unit);
//	}
//
//	@Contract("null,_ -> null")
//	public static Long[] getStartAndEndTimestamp(LocalDateTime localDateTime, TemporalUnit unit) {
//		return getStartAndEndTimestamp(localDateTime, unit, null);
//	}
//
//	@Contract("null,_,_ -> param3")
//	public static Long[] getStartAndEndTimestamp(LocalDateTime localDateTime, TemporalUnit unit, Long[] defaultValue) {
//		return getStartAndEndTimestamp(localDateTime, 1, unit, defaultValue);
//	}
//
//	@Contract("null,_,_,_ -> param4")
//	public static Long[] getStartAndEndTimestamp(LocalDateTime localDateTime, long amountToAdd, TemporalUnit unit, Long[] defaultValue) {
//		return Opt.ofNullable(localDateTime).map(LocalDateTime::toLocalDate).map(localDate -> getStartAndEndTimestamp(localDate, amountToAdd, unit, defaultValue)).orElse(defaultValue);
//	}
//
//	@Contract("null,_ -> null")
//	public static Long[] getStartAndEndTimestamp(LocalDate localDate, TemporalUnit unit) {
//		return getStartAndEndTimestamp(localDate, unit, null);
//	}
//
//	@Contract("null,_,_ -> param3")
//	public static Long[] getStartAndEndTimestamp(LocalDate localDate, TemporalUnit unit, Long[] defaultValue) {
//		return getStartAndEndTimestamp(localDate, 1, unit, defaultValue);
//	}
//
//	@Contract("null,_,_,_ -> param4")
//	public static Long[] getStartAndEndTimestamp(LocalDate localDate, long amountToAdd, TemporalUnit unit, Long[] defaultValue) {
//		return Opt.ofNullable(localDate).map(date -> new Long[]{LocalDateTimeUtil.toEpochMilli(date), LocalDateTimeUtil.toEpochMilli(date.plus(amountToAdd, unit)) - 1}).orElse(defaultValue);
//	}
//
//	@Contract("null -> null")
//	public static Long getTimestamp(Object value) {
//		return getTimestamp(value, null);
//	}
//
//	@Contract("null,_ -> param2")
//	public static Long getTimestamp(Object value, Long defaultValue) {
//		return getTimestamp(value, StrPool.DASHED, DayOfWeek.MONDAY, defaultValue);
//	}
//
//	@Contract("null,_,_ -> param3")
//	public static Long getTimestamp(Object value, String separator, Long defaultValue) {
//		return getTimestamp(value, separator, DayOfWeek.MONDAY, defaultValue);
//	}
//
//	@Contract("null,_,_ -> null")
//	public static Long getTimestamp(Object value, String separator, DayOfWeek dayOfWeek) {
//		return getTimestamp(value, separator, dayOfWeek, null);
//	}
//
//	@Contract("null,_,_,_ -> param4")
//	public static Long getTimestamp(Object value, String separator, DayOfWeek dayOfWeek, Long defaultValue) {
//		if (value == null) {
//			return defaultValue;
//		} else if (value instanceof Date) {
//			return ((Date) value).getTime();
//		} else if (value instanceof Calendar) {
//			return ((Calendar) value).getTimeInMillis();
//		} else if (value instanceof TemporalAccessor) {
//			return LocalDateTimeUtil.toEpochMilli((TemporalAccessor) value);
//		} else if (value instanceof Long) {
//			return Opt.of((Long) value).filter(v -> v >= 0 && v <= 253402271999999L).orElse(defaultValue);
//		} else if (value instanceof CharSequence) {
//			return Opt.ofTry(() -> DateUtil.parse((CharSequence) value)).map(DateTime::toLocalDateTime).map(LocalDateTimeUtil::toEpochMilli).orElse(weekOfYearConvertTimestamp(Convert.toStr(value), separator, dayOfWeek, defaultValue));
//		} else {
//			return defaultValue;
//		}
//	}
//
//	@Contract("null -> null")
//	public static Long weekOfYearConvertTimestamp(String weekOfYear) {
//		return weekOfYearConvertTimestamp(weekOfYear, "-");
//	}
//
//	@Contract("null,_ -> param2")
//	public static Long weekOfYearConvertTimestamp(String weekOfYear, Long defaultValue) {
//		return weekOfYearConvertTimestamp(weekOfYear, "-", defaultValue);
//	}
//
//	@Contract("null,_ -> null")
//	public static Long weekOfYearConvertTimestamp(String weekOfYear, String separator) {
//		return weekOfYearConvertTimestamp(weekOfYear, separator, DayOfWeek.MONDAY);
//	}
//
//	@Contract("null,_,_ -> param3")
//	public static Long weekOfYearConvertTimestamp(String weekOfYear, String separator, Long defaultValue) {
//		return weekOfYearConvertTimestamp(weekOfYear, separator, DayOfWeek.MONDAY, defaultValue);
//	}
//
//	@Contract("null,_,_ -> null")
//	public static Long weekOfYearConvertTimestamp(String weekOfYear, String separator, DayOfWeek dayOfWeek) {
//		return weekOfYearConvertTimestamp(weekOfYear, separator, dayOfWeek, null);
//	}
//
//	@Contract("null,_,_,_ -> param4")
//	public static Long weekOfYearConvertTimestamp(String weekOfYear, String separator, DayOfWeek dayOfWeek, Long defaultValue) {
//		if (CharSequenceUtil.isEmpty(weekOfYear) || CharSequenceUtil.isEmpty(separator) || !weekOfYear.contains(separator) || dayOfWeek == null) {
//			return defaultValue;
//		}
//		String[] split = weekOfYear.split(separator);
//		if (split.length != 2) return defaultValue;
//		Integer year = Convert.toInt(split[0]);
//		Integer week = Convert.toInt(split[1]);
//		if (year == null || week == null) return defaultValue;
//		LocalDate localDate = LocalDate.of(year, 1, 1).plusWeeks(week - 1L);
//
//		if (week != localDate.get(WeekFields.ISO.weekOfYear())) localDate = LocalDate.of(year, 1, 1).plusWeeks(week);
//
//		return LocalDateTimeUtil.toEpochMilli(localDate.with(WeekFields.ISO.dayOfWeek(), dayOfWeek.getValue()));
//	}
//
//}
