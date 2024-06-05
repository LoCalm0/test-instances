//package indi.LoCalm.format.version1.util;
//
//import cn.hutool.core.bean.BeanUtil;
//import cn.hutool.core.collection.CollUtil;
//import cn.hutool.core.convert.Convert;
//import cn.hutool.core.date.DateUtil;
//import cn.hutool.core.date.LocalDateTimeUtil;
//import cn.hutool.core.lang.func.LambdaUtil;
//import cn.hutool.core.map.MapUtil;
//import cn.hutool.core.text.CharPool;
//import cn.hutool.core.text.CharSequenceUtil;
//import cn.hutool.core.text.StrPool;
//import cn.hutool.core.text.finder.StrFinder;
//import cn.hutool.core.text.split.SplitIter;
//import cn.hutool.core.util.ClassUtil;
//import cn.hutool.core.util.ObjectUtil;
//import cn.hutool.core.util.ReUtil;
//import cn.hutool.core.util.ReflectUtil;
//import com.imtristone.format.annotition.DeptFormat;
//import com.imtristone.format.annotition.UserFormat;
//import com.imtristone.system.cache.SysCache;
//import com.imtristone.system.cache.UserCache;
//import com.imtristone.system.entity.Dept;
//import com.imtristone.system.entity.Tenant;
//import com.imtristone.system.entity.User;
//import org.jetbrains.annotations.Contract;
//import org.jetbrains.annotations.NotNull;
//import org.jetbrains.annotations.Nullable;
//import org.springblade.core.mp.base.BaseEntity;
//import org.springframework.core.annotation.AnnotationUtils;
//
//import java.lang.annotation.Annotation;
//import java.lang.reflect.Field;
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.time.temporal.ChronoUnit;
//import java.time.temporal.WeekFields;
//import java.util.*;
//import java.util.function.Function;
//import java.util.function.LongFunction;
//import java.util.stream.Collectors;
//
///**
// * 格式工具
// *
// * @author LoCalm
// */
//public class FormatUtil {
//
//	private FormatUtil() {
//	}
//
//	private static final Map<String, String> USER_SET_GET_FIELD = new HashMap<>();
//	private static final Map<String, String> DEPT_SET_GET_FIELD = new HashMap<>();
//	private static final Map<String, String> USER_FIELD_SEPARATOR = new HashMap<>();
//	private static final Map<String, String> DEPT_FIELD_SEPARATOR = new HashMap<>();
//	private static final StringBuilder STR_BUILDER = new StringBuilder();
//	private static final String ID = LambdaUtil.getFieldName(BaseEntity::getId);
//	private static final List<Class<? extends Annotation>> ANNOTATION_TYPES = Arrays.asList(UserFormat.class, DeptFormat.class);
//
//	private static void init(Class<?> clazz) {
//		Arrays.stream(ClassUtil.getDeclaredFields(clazz)).forEach(field -> Arrays.stream(field.getDeclaredAnnotations()).filter(annotation -> ANNOTATION_TYPES.contains(annotation.annotationType())).forEach(annotation -> {
//			Annotation userOrDeptAnnotation = AnnotationUtils.getAnnotation(field, annotation.annotationType());
//			String fieldName = field.getName();
//			String value = ReflectUtil.invoke(userOrDeptAnnotation, LambdaUtil.getMethodName(UserFormat::value));
//			String separator = ReflectUtil.invoke(userOrDeptAnnotation, LambdaUtil.getMethodName(UserFormat::separator));
//			if (CharSequenceUtil.isBlank(value)) value = fieldName;
//
//			ExportUtil.typeConversionException(field.getType(), fieldName);
//			Objects.requireNonNull(ReflectUtil.getField(clazz, value), value + "字段不存在");
//
//			Map<String, String> targetField = (userOrDeptAnnotation instanceof UserFormat) ? USER_SET_GET_FIELD : DEPT_SET_GET_FIELD;
//			targetField.put(fieldName, value);
//			if (CharSequenceUtil.isNotEmpty(separator)) {
//				Map<String, String> targetSeparator = (userOrDeptAnnotation instanceof UserFormat) ? USER_FIELD_SEPARATOR : DEPT_FIELD_SEPARATOR;
//				targetSeparator.put(fieldName, separator);
//			}
//		}));
//	}
//
//	private static void clear() {
//		USER_SET_GET_FIELD.clear();
//		DEPT_SET_GET_FIELD.clear();
//		USER_FIELD_SEPARATOR.clear();
//		DEPT_FIELD_SEPARATOR.clear();
//	}
//
//	/**
//	 * @see com.imtristone.format.utils.FormatUtil#format(V)
//	 * @deprecated
//	 */
//	@Deprecated
//	@Contract("null, _ -> null")
//	public static <V, E> V setUserName(E entity, @NotNull V vo) {
//		if (ObjectUtil.isNull(entity)) return null;
//
//		Class<?> voClass = vo.getClass();
//		BeanUtil.copyProperties(entity, vo);
//
//		Field createUserName = ReflectUtil.getField(voClass, "createUserName");
//		Field updateUserName = ReflectUtil.getField(voClass, "updateUserName");
//
//		if (ObjectUtil.isNotNull(createUserName)) {
//			setEntityFieldValue(LambdaUtil.getFieldName(BaseEntity::getCreateUser), vo, createUserName, UserCache::getUser, LambdaUtil.getFieldName(User::getName));
//		}
//		if (ObjectUtil.isNotNull(updateUserName)) {
//			setEntityFieldValue(LambdaUtil.getFieldName(BaseEntity::getUpdateUser), vo, updateUserName, UserCache::getUser, LambdaUtil.getFieldName(User::getName));
//		}
//
//		return vo;
//	}
//
//	/**
//	 * @see com.imtristone.format.utils.FormatUtil#format(V)
//	 * @deprecated
//	 */
//	@Deprecated
//	private static <V> void setEntityFieldValue(String getValueName, V vo, Field setValueName, @NotNull LongFunction<?> cacheRetriever, String getMethodName) {
//		Long value = Convert.toLong(ReflectUtil.getFieldValue(vo, getValueName));
//		if (ObjectUtil.isNull(value)) return;
//		Optional<?> entityValue = Optional.ofNullable(cacheRetriever.apply(value));
//		entityValue.ifPresent(val -> ReflectUtil.setFieldValue(vo, setValueName, ReflectUtil.getFieldValue(val, getMethodName)));
//	}
//
//	@Contract("null -> null")
//	public static <V> V format(V vo) {
//		if (ObjectUtil.isNull(vo)) return null;
//
//		init(vo.getClass());
//
//		if (MapUtil.isEmpty(USER_SET_GET_FIELD) && MapUtil.isEmpty(DEPT_SET_GET_FIELD)) {
//			return vo;
//		}
//
//		processFieldValues(vo, USER_SET_GET_FIELD, USER_FIELD_SEPARATOR, UserCache::getUserByIds, User::getRealName, FormatUtil::getUserName);
//		processFieldValues(vo, DEPT_SET_GET_FIELD, DEPT_FIELD_SEPARATOR, SysCache::getDeptByIds, Dept::getDeptName, FormatUtil::getDeptName);
//
//		clear();
//		return vo;
//	}
//
//	@NotNull
//	public static <V> List<V> format(List<V> vo) {
//		return format(vo, null);
//	}
//
//	@NotNull
//	public static <V> List<V> format(List<V> vo, @Nullable CollUtil.Consumer<V> run) {
//		if (CollUtil.isEmpty(vo)) return Collections.emptyList();
//
//		init(vo.get(0).getClass());
//
//		if (MapUtil.isEmpty(USER_SET_GET_FIELD) && MapUtil.isEmpty(DEPT_SET_GET_FIELD)) {
//			return vo;
//		}
//
//		CollUtil.Consumer<V> execute = createProcessor(vo, run);
//		if (execute != null) {
//			CollUtil.forEach(vo, execute);
//		}
//		clear();
//		return vo;
//	}
//
//	private static <V, T> void processFieldValues(V vo, @NotNull Map<String, String> setGetField, Map<String, String> fieldSeparator, Function<List<Long>, List<T>> entityListRetriever, Function<T, String> nameExtractor, LongFunction<String> getEntity) {
//		for (Map.Entry<String, String> entry : setGetField.entrySet()) {
//			Object fieldValue = ReflectUtil.getFieldValue(vo, entry.getValue());
//			if (fieldValue == null) continue;
//			String separator = fieldSeparator.get(entry.getKey());
//			if (CharSequenceUtil.isEmpty(separator)) {
//				ReflectUtil.setFieldValue(vo, entry.getKey(), getEntity.apply(Convert.toLong(fieldValue)));
//			} else {
//				getEntityName(fieldValue, separator, vo, entry.getValue(), entityListRetriever, nameExtractor);
//			}
//		}
//	}
//
//	private static List<Long> getSeparatorData(@NotNull String fieldValueStr, String separator) {
//		if (ReUtil.isMatch(CharSequenceUtil.format("[0-9{}]+", separator), fieldValueStr)) {
//			final SplitIter splitIter = new SplitIter(fieldValueStr, new StrFinder(separator, false), -1, true);
//			return CollUtil.removeNull(splitIter.toList(Convert::toLong));
//		}
//		return Collections.emptyList();
//	}
//
//
//	private static <V, T> void getEntityName(Object fieldValue, String separator, V vo, String fieldName, Function<List<Long>, List<T>> entityListRetriever, Function<T, String> nameExtractor) {
//		List<Long> userIds = getSeparatorData(Convert.toStr(fieldValue), separator);
//		if (CollUtil.isEmpty(userIds)) return;
//
//		List<T> apply = entityListRetriever.apply(userIds);
//		if (CollUtil.isEmpty(apply)) return;
//		ReflectUtil.setFieldValue(vo, fieldName, apply.stream().map(nameExtractor).collect(Collectors.joining(separator)));
//	}
//
//	@NotNull
//	private static <V> String indexIdAndField(V value, int index, String setField) {
//		try {
//			return STR_BUILDER.append(Convert.toStr(ReflectUtil.getFieldValue(value, ID)))
//				.append(CharPool.BRACKET_START)
//				.append(index)
//				.append(CharPool.BRACKET_END)
//				.append(setField)
//				.toString();
//		} finally {
//			STR_BUILDER.setLength(0);
//		}
//	}
//
//	private static <T> List<Long> getSeparatorAllIds(@NotNull List<T> vo, Map<String, String> setGetField, Map<String, String> fieldSeparator) {
//		List<Long> ids = new ArrayList<>();
//		for (int i = 0; i < vo.size(); i++) {
//			for (Map.Entry<String, String> entry : setGetField.entrySet()) {
//				Object fieldValue = ReflectUtil.getFieldValue(vo.get(i), entry.getValue());
//				if (fieldValue == null) continue;
//				String separator = fieldSeparator.get(entry.getKey());
//
//				if (CharSequenceUtil.isEmpty(separator)) {
//					ids.add(Convert.toLong(fieldValue));
//				} else {
//					String fieldValueStr = Convert.toStr(fieldValue);
//					ids.addAll(getSeparatorData(fieldValueStr, separator));
//					fieldSeparator.put(indexIdAndField(vo.get(i), i, entry.getKey()), fieldValueStr);
//				}
//			}
//		}
//		return ids.stream().distinct().collect(Collectors.toList());
//	}
//
//	@Nullable
//	private static <V, T> CollUtil.Consumer<V> getProcessor(boolean separatorEmpty, List<V> vo, Map<String, String> setGetField, Map<String, String> fieldSeparator, Function<List<Long>, List<T>> entityListRetriever, Function<T, String> nameExtractor) {
//		if (separatorEmpty) return null;
//
//		List<Long> separatorIds = getSeparatorAllIds(vo, setGetField, fieldSeparator);
//		if (CollUtil.isEmpty(separatorIds)) return null;
//		List<T> apply = entityListRetriever.apply(separatorIds);
//
//		if (CollUtil.isEmpty(apply)) return null;
//		return ((value, index) -> {
//			for (Map.Entry<String, String> entry : setGetField.entrySet()) {
//				String fieldValueStr = fieldSeparator.get(indexIdAndField(value, index, entry.getKey()));
//				String separator = fieldSeparator.get(entry.getKey());
//				if (CharSequenceUtil.isEmpty(separator)) {
//					Long id = Convert.toLong(ReflectUtil.getFieldValue(value, entry.getValue()));
//					ReflectUtil.setFieldValue(value, entry.getKey(), apply.stream().filter(item -> Objects.equals(Convert.toLong(ReflectUtil.getFieldValue(item, ID)), id)).map(nameExtractor).collect(Collectors.joining()));
//				} else {
//					List<Long> separatorData = getSeparatorData(fieldValueStr, separator);
//					if (CollUtil.isNotEmpty(separatorData)) {
//						ReflectUtil.setFieldValue(value, entry.getKey(), apply.stream().filter(item -> separatorData.contains(Convert.toLong(ReflectUtil.getFieldValue(item, ID)))).map(nameExtractor).collect(Collectors.joining(separator)));
//					}
//				}
//			}
//		});
//	}
//
//	private static <V> CollUtil.Consumer<V> createProcessor(List<V> vo, CollUtil.Consumer<V> run) {
//		boolean userSeparatorEmpty = MapUtil.isEmpty(USER_FIELD_SEPARATOR);
//		boolean deptSeparatorEmpty = MapUtil.isEmpty(DEPT_FIELD_SEPARATOR);
//
//		return ConsumerBuilder.<V>builder().add(run)
//			.add(getProcessor(userSeparatorEmpty, vo, USER_SET_GET_FIELD, USER_FIELD_SEPARATOR, UserCache::getUserByIds, User::getRealName))
//			.add(getProcessor(deptSeparatorEmpty, vo, DEPT_SET_GET_FIELD, DEPT_FIELD_SEPARATOR, SysCache::getDeptByIds, Dept::getDeptName))
//			.addAll(processMapFields(!userSeparatorEmpty, vo, USER_SET_GET_FIELD))
//			.addAll(processMapFields(!deptSeparatorEmpty, vo, DEPT_SET_GET_FIELD))
//			.build();
//	}
//
//	private static <V> List<CollUtil.Consumer<V>> processMapFields(boolean separatorNotEmpty, List<V> vo, Map<String, String> setGetField) {
//		if (separatorNotEmpty || MapUtil.isEmpty(setGetField)) return Collections.emptyList();
//
//		Map<Long, String> entityMap = ExportUtil.getEntityMap(vo, setGetField.values().toArray(new String[0]), User::getId, User::getRealName, UserCache::getUserByIds, "获取用户失败");
//		if (MapUtil.isEmpty(entityMap)) return Collections.emptyList();
//
//		return setGetField.entrySet().stream()
//			.map(entry -> (CollUtil.Consumer<V>) (value, index) -> ReflectUtil.setFieldValue(value, entry.getKey(), entityMap.getOrDefault(Convert.toLong(ReflectUtil.getFieldValue(value, entry.getValue())), Convert.toStr(ReflectUtil.getFieldValue(value, entry.getKey())))))
//			.collect(Collectors.toList());
//	}
//
//	@Contract("null -> null")
//	public static String getUserName(Long userId) {
//		return getEntityFieldValue(userId, UserCache::getUser, LambdaUtil.getFieldName(User::getName));
//	}
//
//	@Contract("null -> null")
//	public static String getDeptName(Long deptId) {
//		return getEntityFieldValue(deptId, SysCache::getDept, LambdaUtil.getFieldName(Dept::getDeptName));
//	}
//
//	@Contract("null -> null")
//	public static String getTenantName(String tenantId) {
//		return getEntityFieldValue(tenantId, SysCache::getTenant, LambdaUtil.getFieldName(Tenant::getTenantName));
//	}
//
//	@Nullable
//	private static <T, R> String getEntityFieldValue(T value, Function<T, R> cacheRetriever, String fieldName) {
//		if (ObjectUtil.isNull(value)) return null;
//		return Optional.ofNullable(cacheRetriever.apply(value)).map(entity -> Convert.toStr(ReflectUtil.getFieldValue(entity, fieldName))).orElse(Convert.toStr(value));
//	}
//
//	@Nullable
//	public static String weekOfYear(Long date) {
//		if (ObjectUtil.isNull(date)) return null;
//		LocalDateTime localDateTime = DateUtil.date(date).toLocalDateTime();
//		int year = localDateTime.getYear();
//		int week = localDateTime.get(WeekFields.ISO.weekOfYear());
//		return year + StrPool.DASHED + week;
//	}
//
//	@NotNull
//	public static long[] getStartAndEndTimestamp(int year, int month, int days, @NotNull ChronoUnit unit) {
//		LocalDate startDate = LocalDate.of(year, month, days);
//		LocalDate endDate = startDate.plus(1, unit);
//		return new long[]{LocalDateTimeUtil.toEpochMilli(startDate), LocalDateTimeUtil.toEpochMilli(endDate) - 1};
//	}
//
//}
