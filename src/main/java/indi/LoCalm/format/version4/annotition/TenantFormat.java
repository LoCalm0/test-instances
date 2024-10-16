//package indi.LoCalm.format.version4.annotition;
//
//import com.imtristone.system.entity.Tenant;
//import org.springframework.core.annotation.AliasFor;
//
//import java.lang.annotation.ElementType;
//import java.lang.annotation.Retention;
//import java.lang.annotation.RetentionPolicy;
//import java.lang.annotation.Target;
//import java.util.function.Function;
//
///**
// * 租户格式化
// *
// * @author LoCalm
// */
//@Target(ElementType.FIELD)
//@Retention(RetentionPolicy.RUNTIME)
//public @interface TenantFormat {
//
//	@AliasFor("getFieldName")
//	String value() default "";
//
//	@AliasFor("value")
//	String getFieldName() default "";
//
//	TenantField setFieldName() default TenantField.TENANT_NAME;
//
//	/**
//	 * Tenant 字段
//	 */
//	enum TenantField {
//		TENANT_ID(Tenant::getTenantId),
//		TENANT_NAME(Tenant::getTenantName),
//		DOMAIN_URL(Tenant::getDomainUrl),
//		BACKGROUND_URL(Tenant::getBackgroundUrl),
//		LINKMAN(Tenant::getLinkman),
//		CONTACT_NUMBER(Tenant::getContactNumber),
//		ADDRESS(Tenant::getAddress),
//		ACCOUNT_NUMBER(Tenant::getAccountNumber),
//		EXPIRE_TIME(Tenant::getExpireTime),
//		PACKAGE_ID(Tenant::getPackageId),
//		DATASOURCE_ID(Tenant::getDatasourceId),
//		LICENSE_KEY(Tenant::getLicenseKey),
//		ADMIN_FLAG(Tenant::getAdminFlag);
//
//		private final Function<Tenant, ?> field;
//
//		TenantField(Function<Tenant, ?> field) {
//			this.field = field;
//		}
//
//		public Function<Tenant, ?> getField() {
//			return field;
//		}
//	}
//}
