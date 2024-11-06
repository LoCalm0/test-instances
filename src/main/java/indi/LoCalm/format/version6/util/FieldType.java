//package indi.LoCalm.format.version6.util;
//
//import org.jetbrains.annotations.Contract;
//import org.jetbrains.annotations.Nullable;
//
///**
// * 格式化字段类型
// *
// * @author LoCalm
// */
//public enum FieldType {
//	USER, DEPT, DICT, DICT_BIZ, DATE_TIME, TIMESTAMP, WEEK;
//
//	@Nullable
//	@Contract(pure = true)
//	public static FieldType of(String format) {
//		switch (format) {
//			case "UserFormat":
//				return USER;
//			case "DeptFormat":
//				return DEPT;
//			case "DictFormat":
//				return DICT;
//			case "DictBizFormat":
//				return DICT_BIZ;
//			case "DateTimeFormat":
//				return DATE_TIME;
//			case "TimestampFormat":
//				return TIMESTAMP;
//			case "WeekFormat":
//				return WEEK;
//			default:
//				return null;
//		}
//	}
//
//}
