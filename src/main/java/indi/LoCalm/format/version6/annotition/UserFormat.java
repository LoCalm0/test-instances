//package indi.LoCalm.format.version6.annotition;
//
//import com.imtristone.system.entity.User;
//import org.springframework.core.annotation.AliasFor;
//
//import java.lang.annotation.ElementType;
//import java.lang.annotation.Retention;
//import java.lang.annotation.RetentionPolicy;
//import java.lang.annotation.Target;
//import java.util.function.Function;
//
///**
// * 用户格式化
// *
// * @author LoCalm
// */
//@Target(ElementType.FIELD)
//@Retention(RetentionPolicy.RUNTIME)
//public @interface UserFormat {
//
//	@AliasFor("getFieldName")
//	String value() default "";
//
//	@AliasFor("value")
//	String getFieldName() default "";
//
//	UserField setFieldName() default UserField.REAL_NAME;
//
//	String getSeparator() default "";
//
//	String setSeparator() default "";
//
//
//	/**
//	 * User 字段
//	 */
//	enum UserField {
//		CODE(User::getCode),
//		USER_TYPE(User::getUserType),
//		ACCOUNT(User::getAccount),
//		PASSWORD(User::getPassword),
//		NAME(User::getName),
//		REAL_NAME(User::getRealName),
//		AVATAR(User::getAvatar),
//		EMAIL(User::getEmail),
//		PHONE(User::getPhone),
//		BIRTHDAY(User::getBirthday),
//		SEX(User::getSex),
//		ROLE_ID(User::getRoleId),
//		DEPT_ID(User::getDeptId),
//		POST_ID(User::getPostId),
//		ACCOUNT_ENC_IDX(User::getAccountEncIdx),
//		REALNAME_ENC_IDX(User::getRealnameEncIdx),
//		SECRET_LEVEL(User::getSecretLevel);
//
//		private final Function<User, ?> field;
//
//		UserField(Function<User, ?> field) {
//			this.field = field;
//		}
//
//		public Function<User, ?> getField() {
//			return field;
//		}
//	}
//}
