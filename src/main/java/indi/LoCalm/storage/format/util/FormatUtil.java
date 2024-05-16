//package indi.LoCalm.storage.format.util;
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
//	private static final Map<String, String> USER_FIELD_SEPARATOR = new HashMap<>();
//	private static final Map<String, String> DEPT_SET_GET_FIELD = new HashMap<>();
//	private static final Map<String, String> DEPT_FIELD_SEPARATOR = new HashMap<>();
//	private static final List<Class<? extends Annotation>> ANNOTATION_TYPES = Arrays.asList(UserFormat.class, DeptFormat.class);
//
//	private static void init(Class<?> clazz) {
//		Arrays.stream(ClassUtil.getDeclaredFields(clazz)).forEach(field -> Arrays.stream(field.getDeclaredAnnotations()).filter(annotation -> ANNOTATION_TYPES.contains(annotation.annotationType())).forEach(annotation -> {
//			Annotation userOrDeptAnnotation = AnnotationUtils.getAnnotation(field, annotation.annotationType());
//			String fieldName = field.getName();
//
//
//			String value = ReflectUtil.invoke(userOrDeptAnnotation, LambdaUtil.getMethodName(UserFormat::value));
//			String separator = ReflectUtil.invoke(userOrDeptAnnotation, LambdaUtil.getMethodName(UserFormat::separator));
//			if (CharSequenceUtil.isBlank(value)) value = fieldName;
//
//			ExportUtil.typeConversionException(field.getType(), fieldName);
//			Objects.requireNonNull(ReflectUtil.getField(clazz, value), value + "字段不存在");
//
//			Map<String, String> targetField = (userOrDeptAnnotation instanceof UserFormat) ? USER_SET_GET_FIELD : DEPT_SET_GET_FIELD;
//			targetField.put(fieldName, value);
//
//			if (CharSequenceUtil.isNotEmpty(separator)) {
//				Map<String, String> targetSeparator = (userOrDeptAnnotation instanceof UserFormat) ? USER_FIELD_SEPARATOR : DEPT_FIELD_SEPARATOR;
//				targetSeparator.put(fieldName, separator);
//			}
//
//		}));
//	}
//
//	private static void clear() {
//		USER_SET_GET_FIELD.clear();
//		USER_FIELD_SEPARATOR.clear();
//		DEPT_SET_GET_FIELD.clear();
//		DEPT_FIELD_SEPARATOR.clear();
//	}
//
//	/**
//	 * @see FormatUtil#format(V)
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
//	 * @see FormatUtil#format(V)
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
//
//		CollUtil.Consumer<V> execute = createProcessor(vo, run);
//		if (execute != null) {
//			for (int i = 0; i < vo.size(); i++) {
//				execute.accept(vo.get(i), i);
//			}
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
//			if (getEntityName(fieldValue, separator, vo, entry.getValue(), entityListRetriever, nameExtractor)) {
//				ReflectUtil.setFieldValue(vo, entry.getKey(), getEntity.apply(Convert.toLong(fieldValue)));
//			}
//		}
//	}
//
//	public static List<Long> getSeparatorData(@NotNull String fieldValueStr, String separator) {
//		if (!(fieldValueStr.contains(separator) && ReUtil.isMatch(CharSequenceUtil.format("[0-9{}]+", separator), fieldValueStr))) {
//			return Collections.emptyList();
//		}
//		final SplitIter splitIter = new SplitIter(fieldValueStr, new StrFinder(separator, false), -1, true);
//		List<Long> userIds = splitIter.toList(Convert::toLong);
//		return CollUtil.isNotEmpty(userIds) ? userIds : Collections.emptyList();
//	}
//
//
//	private static <V, T> boolean getEntityName(Object fieldValue, String separator, V vo, String fieldName, Function<List<Long>, List<T>> entityListRetriever, Function<T, String> nameExtractor) {
//		if (CharSequenceUtil.isEmpty(separator)) return true;
//
//		List<Long> userIds = getSeparatorData(Convert.toStr(fieldValue), separator);
//		if (CollUtil.isEmpty(userIds)) return false;
//
//		List<T> apply = entityListRetriever.apply(userIds);
//		if (CollUtil.isEmpty(apply)) return false;
//		ReflectUtil.setFieldValue(vo, fieldName, apply.stream().map(nameExtractor).collect(Collectors.joining(separator)));
//		return false;
//	}
//
//	private static <T> List<Long> getSeparatorIds(@NotNull List<T> vo, Map<String, String> setGetField, Map<String, String> fieldSeparator) {
//		StringBuilder strBuilder = new StringBuilder();
//		List<Long> ids = new ArrayList<>();
//		for (int i = 0; i < vo.size(); i++) {
//			for (Map.Entry<String, String> entry : setGetField.entrySet()) {
//				Object fieldValue = ReflectUtil.getFieldValue(vo, entry.getValue());
//				if (fieldValue == null) continue;
//				String separator = fieldSeparator.get(entry.getKey());
//
//				if (CharSequenceUtil.isEmpty(separator)) {
//					ids.add(Convert.toLong(fieldValue));
//				} else {
//					String fieldValueStr = Convert.toStr(fieldValue);
//					ids.addAll(getSeparatorData(fieldValueStr, separator));
//					String indexIdAndField = strBuilder.append(Convert.toStr(ReflectUtil.getFieldValue(vo.get(i), LambdaUtil.getFieldName(BaseEntity::getId))))
//						.append(CharPool.BRACKET_START)
//						.append(i)
//						.append(CharPool.BRACKET_END)
//						.append(entry.getKey())
//						.toString();
//					fieldSeparator.put(indexIdAndField, fieldValueStr);
//					strBuilder.setLength(0);
//				}
//			}
//		}
//		ids = ids.stream().distinct().collect(Collectors.toList());
//		return CollUtil.isNotEmpty(ids) ? ids : Collections.emptyList();
//	}
//
//	@Nullable
//	private static <V, T> CollUtil.Consumer<V> getProcessor(List<V> vo, Map<String, String> setGetField, Map<String, String> fieldSeparator, Function<List<Long>, List<T>> entityListRetriever, Function<T, String> nameExtractor) {
//		List<Long> separatorIds = getSeparatorIds(vo, setGetField, fieldSeparator);
//		if (CollUtil.isEmpty(separatorIds)) return null;
//		List<T> apply = entityListRetriever.apply(separatorIds);
//
//		if (CollUtil.isEmpty(apply)) return null;
//		StringBuilder strBuilder = new StringBuilder();
//		return ((value, index) -> {
//			String fieldId = LambdaUtil.getFieldName(BaseEntity::getId);
//			for (Map.Entry<String, String> entry : USER_SET_GET_FIELD.entrySet()) {
//				String indexIdAndField = strBuilder.append(Convert.toStr(ReflectUtil.getFieldValue(vo.get(index), fieldId)))
//					.append(CharPool.BRACKET_START)
//					.append(index)
//					.append(CharPool.BRACKET_END)
//					.append(entry.getKey())
//					.toString();
//				String fieldValueStr = USER_FIELD_SEPARATOR.get(indexIdAndField);
//
//				String separator = USER_FIELD_SEPARATOR.get(entry.getKey());
//				List<Long> separatorData = getSeparatorData(fieldValueStr, separator);
//
//				ReflectUtil.setFieldValue(vo.get(index), entry.getKey(), apply.stream().filter(item -> separatorData.contains(Convert.toLong(ReflectUtil.getFieldValue(item, fieldId)))).map(nameExtractor).collect(Collectors.joining(separator)));
//			}
//		});
//	}
//
//	private static <V> CollUtil.Consumer<V> createProcessor(List<V> vo, CollUtil.Consumer<V> run) {
//		boolean userSeparatorNotEmpty = MapUtil.isNotEmpty(USER_FIELD_SEPARATOR);
//		boolean deptSeparatorNotEmpty = MapUtil.isNotEmpty(DEPT_FIELD_SEPARATOR);
//		return ConsumerBuilder.<V>builder().add(run)
//			.add(userSeparatorNotEmpty, getProcessor(vo, USER_SET_GET_FIELD, USER_FIELD_SEPARATOR, UserCache::getUserByIds, User::getRealName))
//			.add(deptSeparatorNotEmpty, getProcessor(vo, DEPT_SET_GET_FIELD, DEPT_FIELD_SEPARATOR, SysCache::getDeptByIds, Dept::getDeptName))
//			.addAll(!userSeparatorNotEmpty, processMapFields(ExportUtil.getEntityMap(vo, USER_SET_GET_FIELD.values().toArray(new String[0]), User::getId, User::getRealName, UserCache::getUserByIds, "获取用户失败"), USER_SET_GET_FIELD))
//			.addAll(!deptSeparatorNotEmpty, processMapFields(ExportUtil.getEntityMap(vo, DEPT_SET_GET_FIELD.values().toArray(new String[0]), Dept::getId, Dept::getDeptName, SysCache::getDeptByIds, "获取部门失败"), DEPT_SET_GET_FIELD))
//			.build();
//	}
//
//	private static <T> List<CollUtil.Consumer<T>> processMapFields(Map<Long, String> map, Map<String, String> fieldMap) {
//		if (MapUtil.isEmpty(map) || MapUtil.isEmpty(fieldMap)) return Collections.emptyList();
//		return fieldMap.entrySet().stream()
//			.map(entry -> (CollUtil.Consumer<T>) (value, index) -> ReflectUtil.setFieldValue(value, entry.getKey(), map.getOrDefault(Convert.toLong(ReflectUtil.getFieldValue(value, entry.getValue())), Convert.toStr(ReflectUtil.getFieldValue(value, entry.getKey())))))
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
