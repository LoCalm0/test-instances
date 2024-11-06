//package indi.LoCalm.format.version6.annotition;
//
//import com.imtristone.system.entity.Dept;
//import org.springframework.core.annotation.AliasFor;
//
//import java.lang.annotation.ElementType;
//import java.lang.annotation.Retention;
//import java.lang.annotation.RetentionPolicy;
//import java.lang.annotation.Target;
//import java.util.function.Function;
//
///**
// * 部门格式化
// *
// * @author LoCalm
// */
//@Target(ElementType.FIELD)
//@Retention(RetentionPolicy.RUNTIME)
//public @interface DeptFormat {
//
//	@AliasFor("getFieldName")
//	String value() default "";
//
//	@AliasFor("value")
//	String getFieldName() default "";
//
//	DeptField setFieldName() default DeptField.DEPT_NAME;
//
//	String getSeparator() default "";
//
//	String setSeparator() default "";
//
//	/**
//	 * Dept 字段
//	 */
//	enum DeptField {
//		ID(Dept::getId),
//		TENANT_ID(Dept::getTenantId),
//		PARENT_ID(Dept::getParentId),
//		FACTORY_ID(Dept::getFactoryId),
//		DEPT_NO(Dept::getDeptNo),
//		DEPT_NAME(Dept::getDeptName),
//		FULL_NAME(Dept::getFullName),
//		ANCESTORS(Dept::getAncestors),
//		DEPT_CATEGORY(Dept::getDeptCategory),
//		SORT(Dept::getSort),
//		REMARK(Dept::getRemark),
//		IS_DELETED(Dept::getIsDeleted);
//
//		private final Function<Dept, ?> field;
//
//		DeptField(Function<Dept, ?> field) {
//			this.field = field;
//		}
//
//		public Function<Dept, ?> getField() {
//			return field;
//		}
//	}
//}
