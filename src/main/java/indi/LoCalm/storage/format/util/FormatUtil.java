//package indi.LoCalm.storage.format.util;
//
//
//import cn.hutool.core.bean.BeanUtil;
//import cn.hutool.core.convert.Convert;
//import cn.hutool.core.lang.func.LambdaUtil;
//import cn.hutool.core.util.ObjectUtil;
//import cn.hutool.core.util.ReflectUtil;
//import com.imtristone.system.cache.SysCache;
//import com.imtristone.system.cache.UserCache;
//import com.imtristone.system.entity.Dept;
//import com.imtristone.system.entity.Tenant;
//import com.imtristone.system.entity.User;
//import org.jetbrains.annotations.Contract;
//import org.jetbrains.annotations.NotNull;
//import org.springblade.core.mp.base.BaseEntity;
//
//import java.lang.reflect.Field;
//import java.util.Optional;
//import java.util.function.Function;
//import java.util.function.LongFunction;
//
///**
// * @author LoCalm
// */
//public final class FormatUtil {
//
//	private FormatUtil() {
//	}
//
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
//	private static <V> void setEntityFieldValue(String getValueName, V vo, Field setValueName, @NotNull LongFunction<?> cacheRetriever, String getMethodName) {
//		Long value = Convert.toLong(ReflectUtil.getFieldValue(vo, getValueName));
//		if (ObjectUtil.isNull(value)) return;
//		Optional<?> entityValue = Optional.ofNullable(cacheRetriever.apply(value));
//		entityValue.ifPresent(val -> ReflectUtil.setFieldValue(vo, setValueName, ReflectUtil.getFieldValue(val, getMethodName)));
//	}
//
//	public static String getUserName(Long userId) {
//		return getEntityFieldValue(userId, UserCache::getUser, LambdaUtil.getFieldName(User::getName));
//	}
//
//	public static String getDeptName(Long deptId) {
//		return getEntityFieldValue(deptId, SysCache::getDept, LambdaUtil.getFieldName(Dept::getDeptName));
//	}
//
//	public static String getTenantName(String tenantId) {
//		return getEntityFieldValue(tenantId, SysCache::getTenant, LambdaUtil.getFieldName(Tenant::getTenantName));
//	}
//
//	public static <T, R> String getEntityFieldValue(T value, Function<T, R> cacheRetriever, String fieldName) {
//		return Optional.ofNullable(value).map(cacheRetriever)
//			.flatMap(entityValue -> Optional.ofNullable(ReflectUtil.getField(entityValue.getClass(), fieldName)).map(field -> Convert.toStr(ReflectUtil.getFieldValue(entityValue, field))))
//			.orElse(Convert.toStr(value));
//	}
//
//
//}
