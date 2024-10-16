//package indi.LoCalm.format.version3.util;
//
//import cn.hutool.core.collection.CollUtil;
//import cn.hutool.core.convert.Convert;
//import cn.hutool.core.date.DatePattern;
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
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.time.temporal.TemporalUnit;
//import java.time.temporal.WeekFields;
//import java.util.*;
//import java.util.function.Function;
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
//	private static final Map<FieldType, Map<?, ?>> DATA = new EnumMap<>(FieldType.class);
//
//	private static final Map<String, FieldType> SET_IDENTITY = new HashMap<>();
//	private static final Map<String, String> SET_GET_FIELD_NAME = new HashMap<>();
//	private static final Map<String, ? super Object> SET_ANNOTATION_VALUE = new HashMap<>();
//	private static final List<Class<? extends Annotation>> ANNOTATION_TYPES = Arrays.asList(UserFormat.class, DeptFormat.class, TenantFormat.class, DictFormat.class, DictBizFormat.class, DateTimeFormat.class, WeekFormat.class);
//
//	private static void init(Class<?> clazz) {
//		clear();
//		Arrays.stream(ClassUtil.getDeclaredFields(clazz)).forEach(field -> Arrays.stream(field.getDeclaredAnnotations()).filter(annotation -> ANNOTATION_TYPES.contains(annotation.annotationType())).forEach(annotation -> {
//			String fieldName = field.getName();
//			Annotation annotationData = AnnotationUtils.getAnnotation(field, annotation.annotationType());
//			String value = ReflectUtil.invoke(annotationData, LambdaUtil.getMethodName(UserFormat::value));
//			if (CharSequenceUtil.isBlank(value)) value = fieldName;
//
//			Objects.requireNonNull(ReflectUtil.getField(clazz, value), value + "字段不存在");
//
//			SET_GET_FIELD_NAME.put(fieldName, value);
//
//			assert annotationData != null;
//			processFieldAnnotations(annotationData, fieldName);
//		}));
//	}
//
//	private static void processFieldAnnotations(@NotNull Annotation annotation, String fieldName) {
//		switch (annotation.annotationType().getSimpleName()) {
//			case "UserFormat":
//				SET_IDENTITY.put(fieldName, FieldType.USER);
//				SET_ANNOTATION_VALUE.put(fieldName, Opt.ofNullable((UserFormat) annotation).map(userFormat -> MapUtil.builder(new HashMap<String, String>()).put(SET_FIELD_NAME, Opt.ofBlankAble(userFormat.setFieldName()).orElse(LambdaUtil.getFieldName(User::getRealName))).put(GET_SEPARATOR, Opt.ofBlankAble(userFormat.getSeparator()).orElse(null)).put(SET_SEPARATOR, Opt.ofBlankAble(userFormat.setSeparator()).orElse(Opt.ofBlankAble(userFormat.getSeparator()).orElse(null))).build()).orElse(Collections.emptyMap()));
//				break;
//			case "DeptFormat":
//				SET_IDENTITY.put(fieldName, FieldType.DEPT);
//				SET_ANNOTATION_VALUE.put(fieldName, Opt.ofNullable((DeptFormat) annotation).map(deptFormat -> MapUtil.builder(new HashMap<String, String>()).put(SET_FIELD_NAME, Opt.ofBlankAble(deptFormat.setFieldName()).orElse(LambdaUtil.getFieldName(Dept::getDeptName))).put(GET_SEPARATOR, Opt.ofBlankAble(deptFormat.getSeparator()).orElse(null)).put(SET_SEPARATOR, Opt.ofBlankAble(deptFormat.setSeparator()).orElse(Opt.ofBlankAble(deptFormat.getSeparator()).orElse(null))).build()).orElse(Collections.emptyMap()));
//				break;
//			case "TenantFormat":
//				SET_IDENTITY.put(fieldName, FieldType.TENANT);
//				SET_ANNOTATION_VALUE.put(fieldName, Opt.ofNullable((TenantFormat) annotation).map(TenantFormat::setFieldName).orElse(LambdaUtil.getFieldName(Tenant::getTenantName)));
//				break;
//			case "DictFormat":
//				SET_IDENTITY.put(fieldName, FieldType.DICT);
//				SET_ANNOTATION_VALUE.put(fieldName, Opt.ofNullable((DictFormat) annotation).map(DictFormat::code).map(Enum::name).map(String::toLowerCase).get());
//				break;
//			case "DictBizFormat":
//				SET_IDENTITY.put(fieldName, FieldType.DICT_BIZ);
//				SET_ANNOTATION_VALUE.put(fieldName, Opt.ofNullable((DictBizFormat) annotation).map(DictBizFormat::code).map(Enum::name).map(String::toLowerCase).get());
//				break;
//			case "DateTimeFormat":
//				SET_IDENTITY.put(fieldName, FieldType.DATE);
//				SET_ANNOTATION_VALUE.put(fieldName, Opt.ofNullable((DateTimeFormat) annotation).map(DateTimeFormat::pattern).orElse(DatePattern.NORM_DATETIME_PATTERN));
//				break;
//			case "WeekFormat":
//				SET_IDENTITY.put(fieldName, FieldType.WEEK);
//				break;
//			default:
//				break;
//		}
//	}
//
//	private static void clear() {
//		DATA.clear();
//		SET_IDENTITY.clear();
//		SET_GET_FIELD_NAME.clear();
//		SET_ANNOTATION_VALUE.clear();
//	}
//
//	public static synchronized <V> V format(V vo) {
//		if (null == vo) return null;
//
//		init(vo.getClass());
//
//		if (MapUtil.isEmpty(SET_IDENTITY)) return vo;
//
//		initData(vo);
//		reflectionField(vo, 0, DATA);
//		return vo;
//	}
//
//	@NotNull
//	public static <V> List<V> format(List<V> vo) {
//		return format(vo, null);
//	}
//
//	@NotNull
//	public static synchronized <V> List<V> format(List<V> vo, @Nullable Consumer3<V, Integer, Map<FieldType, Map<?, ?>>> run) {
//		if (CollUtil.isEmpty(vo)) return Collections.emptyList();
//
//		init(vo.get(0).getClass());
//
//		if (MapUtil.isEmpty(SET_IDENTITY)) return vo;
//
//		initData(vo);
//		forEach(vo.iterator(), ConsumerBuilder.<V>builder().add(FormatUtil::reflectionField).add(run).build());
//		return vo;
//	}
//
//	private static <V> void forEach(Iterator<V> iterator, Consumer3<V, Integer, Map<FieldType, Map<?, ?>>> consumer) {
//		if (iterator == null) return;
//
//		int index = 0;
//		while (iterator.hasNext()) {
//			consumer.accept(iterator.next(), index, DATA);
//			index++;
//		}
//	}
//
//	@SuppressWarnings("unchecked")
//	private static <V> void reflectionField(V vo, int index, Map<FieldType, Map<?, ?>> data) {
//		for (Map.Entry<String, FieldType> entry : SET_IDENTITY.entrySet()) {
//			Object value = ReflectUtil.getFieldValue(vo, SET_GET_FIELD_NAME.get(entry.getKey()));
//			switch (entry.getValue()) {
//				case USER:
//					Opt.ofNullable(DATA.get(FieldType.USER).get(Convert.toLong(value))).ifPresentOrElse(user -> ReflectUtil.setFieldValue(vo, entry.getKey(), ReflectUtil.getFieldValue(user, ((Map<String, String>) SET_ANNOTATION_VALUE.get(entry.getKey())).get(SET_FIELD_NAME))), () -> Opt.ofEmptyAble((List<Long>) SET_ANNOTATION_VALUE.get(Convert.toStr(value))).ifPresent(ids -> Opt.ofNullable(((Map<String, String>) SET_ANNOTATION_VALUE.get(entry.getKey()))).ifPresent(userMap -> ReflectUtil.setFieldValue(vo, entry.getKey(), MapUtil.valuesOfKeys((Map<Long, User>) DATA.get(FieldType.USER), ids.iterator()).stream().map(user -> Convert.toStr(ReflectUtil.getFieldValue(user, userMap.get(SET_FIELD_NAME)))).collect(Collectors.joining(userMap.get(SET_SEPARATOR)))))));
//					break;
//				case DEPT:
//					Opt.ofNullable(DATA.get(FieldType.DEPT).get(Convert.toLong(value))).ifPresentOrElse(dept -> ReflectUtil.setFieldValue(vo, entry.getKey(), ReflectUtil.getFieldValue(dept, ((Map<String, String>) SET_ANNOTATION_VALUE.get(entry.getKey())).get(SET_FIELD_NAME))), () -> Opt.ofEmptyAble((List<Long>) SET_ANNOTATION_VALUE.get(Convert.toStr(value))).ifPresent(ids -> Opt.ofNullable(((Map<String, String>) SET_ANNOTATION_VALUE.get(entry.getKey()))).ifPresent(deptMap -> ReflectUtil.setFieldValue(vo, entry.getKey(), MapUtil.valuesOfKeys((Map<Long, Dept>) DATA.get(FieldType.DEPT), ids.iterator()).stream().map(dept -> Convert.toStr(ReflectUtil.getFieldValue(dept, deptMap.get(SET_FIELD_NAME)))).collect(Collectors.joining(deptMap.get(SET_SEPARATOR)))))));
//					break;
//				case TENANT:
//					Opt.ofNullable(DATA.get(FieldType.TENANT).get(Convert.toStr(value))).ifPresent(tenant -> ReflectUtil.setFieldValue(vo, entry.getKey(), ReflectUtil.getFieldValue(tenant, Convert.toStr(SET_ANNOTATION_VALUE.get(entry.getKey())))));
//					break;
//				case DICT:
//					Opt.ofNullable((Map<String, String>) DATA.get(FieldType.DICT).get(Convert.toStr(SET_ANNOTATION_VALUE.get(entry.getKey())))).ifPresent(dict -> Opt.ofNullable(dict.get(Convert.toStr(value))).ifPresent(fieldValue -> ReflectUtil.setFieldValue(vo, entry.getKey(), fieldValue)));
//					break;
//				case DICT_BIZ:
//					Opt.ofNullable((Map<String, String>) DATA.get(FieldType.DICT_BIZ).get(Convert.toStr(SET_ANNOTATION_VALUE.get(entry.getKey())))).ifPresent(dict -> Opt.ofNullable(dict.get(Convert.toStr(value))).ifPresent(fieldValue -> ReflectUtil.setFieldValue(vo, entry.getKey(), fieldValue)));
//					break;
//				case DATE:
//					Opt.ofNullable(Convert.toLong(value)).ifPresent(fieldValue -> ReflectUtil.setFieldValue(vo, entry.getKey(), DateUtil.date(fieldValue).toString(Convert.toStr(SET_ANNOTATION_VALUE.get(entry.getKey())))));
//					break;
//				case WEEK:
//					Opt.ofNullable(Convert.toLong(value)).ifPresent(fieldValue -> ReflectUtil.setFieldValue(value, entry.getKey(), weekOfYear(fieldValue)));
//					break;
//				default:
//					break;
//			}
//		}
//	}
//
//	private static <V> void initData(V vo) {
//		Map<FieldType, Map<String, String>> fieldMap = getFieldMapByIdentity();
//		Opt.ofNullable(fieldMap.get(FieldType.USER)).ifPresent(map -> DATA.put(FieldType.USER, getEntityMap(vo, Convert::toLong, map.keySet(), new HashSet<>(map.values()), User::getId, UserCache::getUserByIds, UserCache::getUser, "获取用户失败")));
//		Opt.ofNullable(fieldMap.get(FieldType.DEPT)).ifPresent(map -> DATA.put(FieldType.DEPT, getEntityMap(vo, Convert::toLong, map.keySet(), new HashSet<>(map.values()), Dept::getId, SysCache::getDeptByIds, SysCache::getDept, "获取部门失败")));
//		Opt.ofNullable(fieldMap.get(FieldType.TENANT)).ifPresent(map -> DATA.put(FieldType.TENANT, getEntityMap(vo, Convert::toStr, null, map.keySet(), Tenant::getTenantId, SysCache::getTenantByTenantIds, SysCache::getTenant, "获取租户失败")));
//		Opt.ofNullable(fieldMap.get(FieldType.DICT)).ifPresent(map -> DATA.put(FieldType.DICT, getDictMap(map.keySet().toArray(new String[0]), DictCache::getDict, Dict::getCode, Dict::getDictKey, Dict::getDictValue)));
//		Opt.ofNullable(fieldMap.get(FieldType.DICT_BIZ)).ifPresent(map -> DATA.put(FieldType.DICT_BIZ, getDictMap(map.keySet().toArray(new String[0]), DictBizCache::getDict, DictBiz::getCode, DictBiz::getDictKey, DictBiz::getDictValue)));
//	}
//
//	private static <V> void initData(List<V> vo) {
//		Map<FieldType, Map<String, String>> fieldMap = getFieldMapByIdentity();
//		Opt.ofNullable(fieldMap.get(FieldType.USER)).ifPresent(map -> DATA.put(FieldType.USER, getEntityMap(vo, Convert::toLong, map.keySet(), new HashSet<>(map.values()), User::getId, UserCache::getUserByIds, "获取用户失败")));
//		Opt.ofNullable(fieldMap.get(FieldType.DEPT)).ifPresent(map -> DATA.put(FieldType.DEPT, getEntityMap(vo, Convert::toLong, map.keySet(), new HashSet<>(map.values()), Dept::getId, SysCache::getDeptByIds, "获取部门失败")));
//		Opt.ofNullable(fieldMap.get(FieldType.TENANT)).ifPresent(map -> DATA.put(FieldType.TENANT, getEntityMap(vo, Convert::toStr, null, map.keySet(), Tenant::getTenantId, SysCache::getTenantByTenantIds, "获取租户失败")));
//		Opt.ofNullable(fieldMap.get(FieldType.DICT)).ifPresent(map -> DATA.put(FieldType.DICT, getDictMap(map.keySet().toArray(new String[0]), DictCache::getDict, Dict::getCode, Dict::getDictKey, Dict::getDictValue)));
//		Opt.ofNullable(fieldMap.get(FieldType.DICT_BIZ)).ifPresent(map -> DATA.put(FieldType.DICT_BIZ, getDictMap(map.keySet().toArray(new String[0]), DictBizCache::getDict, DictBiz::getCode, DictBiz::getDictKey, DictBiz::getDictValue)));
//	}
//
//	@NotNull
//	@SuppressWarnings("unchecked")
//	private static Map<String, String> getEntityMapBefore(Set<String> setFields) {
//		if (CollUtil.isEmpty(setFields)) return Collections.emptyMap();
//		Map<String, String> fieldNameSeparator = new HashMap<>();
//		setFields.forEach(fieldName -> Opt.ofBlankAble(((Map<String, String>) SET_ANNOTATION_VALUE.get(fieldName)).get(GET_SEPARATOR)).ifPresent(separator -> fieldNameSeparator.put(SET_GET_FIELD_NAME.get(fieldName), separator)));
//		return fieldNameSeparator;
//	}
//
//	private static <T, E> Map<E, T> getEntityMapAfter(@NotNull Function<List<E>, List<T>> entityRetriever, @NotNull List<E> ids, Function<T, E> idExtractor, String errorMessage) {
//		List<T> entities = entityRetriever.apply(ids.stream().distinct().collect(Collectors.toList()));
//		if (CollUtil.isNotEmpty(entities)) {
//			return entities.stream().collect(Collectors.toMap(idExtractor, e -> e));
//		} else {
//			Console.error(errorMessage);
//		}
//
//		return Collections.emptyMap();
//	}
//
//	private static <V, T, E> Map<E, T> getEntityMap(List<V> data, Function<Object, E> convert, Set<String> setFields, Set<String> getFields, Function<T, E> idExtractor, Function<List<E>, List<T>> entityRetriever, String errorMessage) {
//		if (CollUtil.isEmpty(getFields)) return Collections.emptyMap();
//
//		Map<String, String> fieldNameSeparator = getEntityMapBefore(setFields);
//
//		List<E> ids = new ArrayList<>();
//		if (MapUtil.isEmpty(fieldNameSeparator)) {
//			ids.addAll(getValue(data, convert, getFields));
//		} else {
//			ids.addAll(getSeparatorAllIds(data, convert, fieldNameSeparator, getFields));
//		}
//
//		CollUtil.removeNull(ids);
//		if (CollUtil.isEmpty(ids)) return Collections.emptyMap();
//
//		return getEntityMapAfter(entityRetriever, ids, idExtractor, errorMessage);
//	}
//
//	private static <V, T, E> Map<E, T> getEntityMap(V data, Function<Object, E> convert, Set<String> setFields, Set<String> getFields, Function<T, E> idExtractor, Function<List<E>, List<T>> entityRetriever, Function<E, T> retriever, String errorMessage) {
//		if (CollUtil.isEmpty(getFields)) return Collections.emptyMap();
//
//		Map<String, String> fieldNameSeparator = getEntityMapBefore(setFields);
//
//		if (MapUtil.isEmpty(fieldNameSeparator) && getFields.size() == 1) {
//			return Opt.ofNullable(convert.apply(ReflectUtil.getFieldValue(data, getFields.iterator().next()))).map(id -> Opt.ofNullable(retriever.apply(id)).map(entity -> Collections.singletonMap(id, entity)).orElse(Collections.emptyMap())).orElse(Collections.emptyMap());
//		}
//		List<E> ids = new ArrayList<>(CollUtil.removeNull(getSeparatorAllIds(Collections.singletonList(data), convert, fieldNameSeparator, getFields).stream().distinct().collect(Collectors.toList())));
//		if (CollUtil.isEmpty(ids)) return Collections.emptyMap();
//
//		return getEntityMapAfter(entityRetriever, ids, idExtractor, errorMessage);
//	}
//
//	@NotNull
//	private static Map<FieldType, Map<String, String>> getFieldMapByIdentity() {
//		Map<FieldType, Map<String, String>> fieldMap = new EnumMap<>(FieldType.class);
//		if (Stream.of(FieldType.USER, FieldType.DEPT, FieldType.DICT, FieldType.DICT_BIZ).anyMatch(SET_IDENTITY::containsValue)) {
//			for (Map.Entry<String, FieldType> entry : SET_IDENTITY.entrySet()) {
//				switch (entry.getValue()) {
//					case USER:
//						Opt.ofNullable(fieldMap.get(FieldType.USER)).ifPresentOrElse(map -> map.put(entry.getKey(), SET_GET_FIELD_NAME.get(entry.getKey())), () -> fieldMap.put(FieldType.USER, MapUtil.builder(new HashMap<String, String>()).put(entry.getKey(), SET_GET_FIELD_NAME.get(entry.getKey())).build()));
//						break;
//					case DEPT:
//						Opt.ofNullable(fieldMap.get(FieldType.DEPT)).ifPresentOrElse(map -> map.put(entry.getKey(), SET_GET_FIELD_NAME.get(entry.getKey())), () -> fieldMap.put(FieldType.DEPT, MapUtil.builder(new HashMap<String, String>()).put(entry.getKey(), SET_GET_FIELD_NAME.get(entry.getKey())).build()));
//						break;
//					case TENANT:
//						Opt.ofNullable(fieldMap.get(FieldType.TENANT)).ifPresentOrElse(map -> map.put(Convert.toStr(SET_GET_FIELD_NAME.get(entry.getKey())), null), () -> fieldMap.put(FieldType.TENANT, MapUtil.builder(new HashMap<String, String>()).put(Convert.toStr(SET_GET_FIELD_NAME.get(entry.getKey())), null).build()));
//						break;
//					case DICT:
//						Opt.ofNullable(fieldMap.get(FieldType.DICT)).ifPresentOrElse(map -> map.put(Convert.toStr(SET_ANNOTATION_VALUE.get(entry.getKey())), null), () -> fieldMap.put(FieldType.DICT, MapUtil.builder(new HashMap<String, String>()).put(Convert.toStr(SET_ANNOTATION_VALUE.get(entry.getKey())), null).build()));
//						break;
//					case DICT_BIZ:
//						Opt.ofNullable(fieldMap.get(FieldType.DICT_BIZ)).ifPresentOrElse(map -> map.put(Convert.toStr(SET_ANNOTATION_VALUE.get(entry.getKey())), null), () -> fieldMap.put(FieldType.DICT_BIZ, MapUtil.builder(new HashMap<String, String>()).put(Convert.toStr(SET_ANNOTATION_VALUE.get(entry.getKey())), null).build()));
//						break;
//					default:
//						break;
//				}
//			}
//		}
//		return fieldMap;
//	}
//
//	private static <T, E> List<E> getValue(@NotNull List<T> data, Function<Object, E> convert, Set<String> nameField) {
//		return data.stream().flatMap(entity -> nameField.stream().map(name -> convert.apply(ReflectUtil.getFieldValue(entity, name)))).collect(Collectors.toList());
//	}
//
//	private static <T> Map<String, Map<String, String>> getDictMap(String[] dictField, Function<String[], List<T>> entityRetriever, Function<T, String> code, Function<T, String> key, Function<T, String> value) {
//		if (ArrayUtil.isEmpty(dictField)) return Collections.emptyMap();
//		List<T> data = entityRetriever.apply(dictField);
//		if (CollUtil.isEmpty(data)) return Collections.emptyMap();
//		return data.stream().collect(Collectors.groupingBy(code, Collectors.toMap(key, value)));
//	}
//
//	private static <E> List<E> getSeparatorData(@NotNull String fieldValueStr, Function<Object, E> convert, String separator) {
//		if (ReUtil.isMatch(CharSequenceUtil.format("[0-9{}]+", separator), fieldValueStr)) {
//			final SplitIter splitIter = new SplitIter(fieldValueStr, new StrFinder(separator, false), -1, true);
//			return splitIter.toList(convert::apply);
//		}
//		return Collections.emptyList();
//	}
//
//	@NotNull
//	private static <V, E> List<E> getSeparatorAllIds(@NotNull List<V> data, Function<Object, E> convert, Map<String, String> fieldNameSeparator, Set<String> fields) {
//		if (CollUtil.isEmpty(fieldNameSeparator)) return getValue(data, convert, fields);
//		List<E> ids = new ArrayList<>();
//		for (V vo : data) {
//			for (Map.Entry<String, String> entry : fieldNameSeparator.entrySet()) {
//				String fieldValue = Convert.toStr(ReflectUtil.getFieldValue(vo, entry.getKey()));
//				if (CharSequenceUtil.isBlank(fieldValue)) continue;
//				List<E> separatorData = getSeparatorData(fieldValue, convert, entry.getValue());
//				SET_ANNOTATION_VALUE.put(fieldValue, separatorData);
//				ids.addAll(separatorData);
//			}
//		}
//
//		Set<String> filter = fields.stream().filter(field -> !fieldNameSeparator.containsKey(field)).collect(Collectors.toSet());
//		if (CollUtil.isNotEmpty(filter)) ids.addAll(getValue(data, convert, filter));
//		return ids;
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
//	@Nullable
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
//	public static String weekOfYear(Long date) {
//		return Opt.ofNullable(date).map(value -> DateUtil.date(value).toLocalDateTime()).map(localDateTime -> localDateTime.getYear() + StrPool.DASHED + localDateTime.get(WeekFields.ISO.weekOfYear())).orElse(null);
//	}
//
//	@NotNull
//	public static long[] getStartAndEndTimestamp(@NotNull LocalDateTime dateTime, @NotNull TemporalUnit unit) {
//		return Opt.of(dateTime).map(LocalDateTime::toLocalDate).map(date -> getStartAndEndTimestamp(date, unit)).get();
//	}
//
//	@NotNull
//	public static long[] getStartAndEndTimestamp(@NotNull LocalDate date, @NotNull TemporalUnit unit) {
//		return getStartAndEndTimestamp(date, 1, unit);
//	}
//
//	@NotNull
//	public static long[] getStartAndEndTimestamp(@NotNull LocalDate date, long amountToAdd, @NotNull TemporalUnit unit) {
//		return new long[]{LocalDateTimeUtil.toEpochMilli(date), LocalDateTimeUtil.toEpochMilli(date.plus(amountToAdd, unit)) - 1};
//	}
//
//	@NotNull
//	public static long[] getStartAndEndTimestamp(int year, int month, int day, @NotNull TemporalUnit unit) {
//		return Opt.of(LocalDate.of(year, month, day)).map(date -> getStartAndEndTimestamp(date, unit)).get();
//	}
//
//}
