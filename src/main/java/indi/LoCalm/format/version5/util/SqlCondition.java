//package indi.LoCalm.format.version5.util;
//
//import cn.hutool.core.builder.Builder;
//import cn.hutool.core.lang.Console;
//import cn.hutool.core.lang.func.Func1;
//import cn.hutool.core.lang.func.LambdaUtil;
//import cn.hutool.core.text.CharPool;
//import cn.hutool.core.text.CharSequenceUtil;
//import cn.hutool.core.text.StrPool;
//import cn.hutool.core.util.ReflectUtil;
//import org.jetbrains.annotations.Contract;
//import org.jetbrains.annotations.NotNull;
//
//import java.lang.reflect.Field;
//import java.util.*;
//import java.util.stream.Collectors;
//
///**
// * sql条件
// *
// * @author LoCalm
// */
//public class SqlCondition implements Builder<String> {
//
//    private final StringBuilder sql = new StringBuilder();
//
//    private static final String GT = ">";
//    private static final String EQ = "=";
//    private static final String LT = "<";
//    private static final String NE = LT + GT;
//    private static final String LE = LT + EQ;
//    private static final String GE = GT + EQ;
//    private static final String SINGLE_QUOTE = "'";
//    private static final String PERCENT_SIGN = "%";
//
//    private static final String AND = "and";
//    private static final String OR = "or";
//    private static final String NOT = "not";
//    private static final String LIKE = "like";
//    private static final String IN = "in";
//    private static final String IS = "is";
//    private static final String NULL = "null";
//    private static final String LEFT_BRACKET = "(";
//    private static final String RIGHT_BRACKET = ")";
//    private static final String ORDER = "order";
//    private static final String BY = "by";
//    private static final String ASC = "asc";
//    private static final String DESC = "desc";
//
//    private SqlCondition setValue(Object value) {
//        return this.append(CharPool.SPACE).append(SINGLE_QUOTE).append(value).append(SINGLE_QUOTE);
//    }
//
//    private SqlCondition setLikeValue(Object value) {
//        return this.append(CharPool.SPACE).append(SINGLE_QUOTE).append(PERCENT_SIGN).append(value).append(PERCENT_SIGN).append(SINGLE_QUOTE);
//    }
//
//    public SqlCondition and() {
//        return and(true);
//    }
//
//    public SqlCondition and(boolean condition) {
//        if (condition) this.append(CharPool.SPACE).append(AND).append(CharPool.SPACE);
//        return this;
//    }
//
//    public SqlCondition or() {
//        return or(true);
//    }
//
//    public SqlCondition or(boolean condition) {
//        if (condition) this.append(CharPool.SPACE).append(OR).append(CharPool.SPACE);
//        return this;
//    }
//
//    public <T> SqlCondition gt(Func1<T, ?> func, Object value) {
//        return this.gt(true, func, value);
//    }
//
//    public <T> SqlCondition gt(boolean condition, Func1<T, ?> func, Object value) {
//        return this.gt(condition, LambdaUtil.getFieldName(func), value);
//    }
//
//    public SqlCondition gt(String column, Object value) {
//        return gt(true, column, value);
//    }
//
//    public SqlCondition gt(boolean condition, String column, Object value) {
//        if (condition) this.append(column).append(CharPool.SPACE).append(GT).setValue(value);
//        return this;
//    }
//
//    public <T> SqlCondition lt(Func1<T, ?> func, Object value) {
//        return this.lt(true, func, value);
//    }
//
//    public <T> SqlCondition lt(boolean condition, Func1<T, ?> func, Object value) {
//        return this.lt(condition, LambdaUtil.getFieldName(func), value);
//    }
//
//    public SqlCondition lt(String column, Object value) {
//        return this.lt(true, column, value);
//    }
//
//    public SqlCondition lt(boolean condition, String column, Object value) {
//        if (condition) this.append(column).append(CharPool.SPACE).append(LT).setValue(value);
//        return this;
//    }
//
//    public <T> SqlCondition eq(Func1<T, ?> func, Object value) {
//        return this.eq(true, func, value);
//    }
//
//    public <T> SqlCondition eq(boolean condition, Func1<T, ?> func, Object value) {
//        return this.eq(condition, LambdaUtil.getFieldName(func), value);
//    }
//
//    public SqlCondition eq(String column, Object value) {
//        return this.eq(true, column, value);
//    }
//
//    public SqlCondition eq(boolean condition, String column, Object value) {
//        if (condition) this.append(column).append(CharPool.SPACE).append(EQ).setValue(value);
//        return this;
//    }
//
//    public <T> SqlCondition ne(Func1<T, ?> func, Object value) {
//        return this.ne(true, func, value);
//    }
//
//    public <T> SqlCondition ne(boolean condition, Func1<T, ?> func, Object value) {
//        return this.ne(condition, LambdaUtil.getFieldName(func), value);
//    }
//
//    public SqlCondition ne(String column, Object value) {
//        return this.ne(true, column, value);
//    }
//
//    public SqlCondition ne(boolean condition, String column, Object value) {
//        if (condition) this.append(column).append(CharPool.SPACE).append(NE).setValue(value);
//        return this;
//    }
//
//    public <T> SqlCondition le(Func1<T, ?> func, Object value) {
//        return this.le(true, func, value);
//    }
//
//    public <T> SqlCondition le(boolean condition, Func1<T, ?> func, Object value) {
//        return this.le(condition, LambdaUtil.getFieldName(func), value);
//    }
//
//    public SqlCondition le(String column, Object value) {
//        return this.le(true, column, value);
//    }
//
//    public SqlCondition le(boolean condition, String column, Object value) {
//        if (condition) this.append(column).append(CharPool.SPACE).append(LE).setValue(value);
//        return this;
//    }
//
//    public <T> SqlCondition ge(Func1<T, ?> func, Object value) {
//        return this.ge(true, func, value);
//    }
//
//    public <T> SqlCondition ge(boolean condition, Func1<T, ?> func, Object value) {
//        return this.ge(condition, LambdaUtil.getFieldName(func), value);
//    }
//
//    public SqlCondition ge(String column, Object value) {
//        return this.ge(true, column, value);
//    }
//
//    public SqlCondition ge(boolean condition, String column, Object value) {
//        if (condition) this.append(column).append(CharPool.SPACE).append(GE).setValue(value);
//        return this;
//    }
//
//    public <T> SqlCondition between(Func1<T, ?> func, Object value1, Object value2) {
//        return this.between(true, func, value1, value2);
//    }
//
//    public <T> SqlCondition between(boolean condition, Func1<T, ?> func, Object value1, Object value2) {
//        return this.between(condition, LambdaUtil.getFieldName(func), value1, value2);
//    }
//
//    public SqlCondition between(String column, Object value1, Object value2) {
//        return this.between(true, column, value1, value2);
//    }
//
//    public SqlCondition between(boolean condition, String column, Object value1, Object value2) {
//        if (condition) {
//            this.append(column).append(CharPool.SPACE).append(GE).setValue(value1).and().append(column).append(CharPool.SPACE).append(LE).setValue(value2);
//        }
//        return this;
//    }
//
//    public <T> SqlCondition notBetween(Func1<T, ?> func, Object value1, Object value2) {
//        return this.notBetween(true, func, value1, value2);
//    }
//
//    public <T> SqlCondition notBetween(boolean condition, Func1<T, ?> func, Object value1, Object value2) {
//        return this.notBetween(condition, LambdaUtil.getFieldName(func), value1, value2);
//    }
//
//    public SqlCondition notBetween(String column, Object value1, Object value2) {
//        return this.notBetween(true, column, value1, value2);
//    }
//
//    public SqlCondition notBetween(boolean condition, String column, Object value1, Object value2) {
//        if (condition) this.append(NOT).between(column, value1, value2);
//        return this;
//    }
//
//    public <T> SqlCondition like(Func1<T, ?> func, Object value) {
//        return this.like(true, func, value);
//    }
//
//    public <T> SqlCondition like(boolean condition, Func1<T, ?> func, Object value) {
//        return this.like(condition, LambdaUtil.getFieldName(func), value);
//    }
//
//    public SqlCondition like(String column, Object value) {
//        return this.like(true, column, value);
//    }
//
//    public SqlCondition like(boolean condition, String column, Object value) {
//        if (condition) {
//            this.append(column).append(CharPool.SPACE).append(LIKE).setLikeValue(value);
//        }
//        return this;
//    }
//
//    public <T> SqlCondition notLike(Func1<T, ?> func, Object value) {
//        return this.notLike(true, func, value);
//    }
//
//    public <T> SqlCondition notLike(boolean condition, Func1<T, ?> func, Object value) {
//        return this.notLike(condition, LambdaUtil.getFieldName(func), value);
//    }
//
//    public SqlCondition notLike(String column, Object value) {
//        return this.notLike(true, column, value);
//    }
//
//    public SqlCondition notLike(boolean condition, String column, Object value) {
//        if (condition) {
//            this.append(NOT).like(column, value);
//        }
//        return this;
//    }
//
//    public <T> SqlCondition in(Func1<T, ?> func, @NotNull Object... values) {
//        return this.in(true, func, values);
//    }
//
//    public <T> SqlCondition in(boolean condition, Func1<T, ?> func, @NotNull Object... values) {
//        return this.in(condition, LambdaUtil.getFieldName(func), values);
//    }
//
//    public SqlCondition in(String column, @NotNull Object... values) {
//        return this.in(true, column, values);
//    }
//
//    public SqlCondition in(boolean condition, String column, @NotNull Object... values) {
//        if (condition) {
//            this.append(column).append(CharPool.SPACE).append(IN).append(CharPool.SPACE).append(LEFT_BRACKET);
//
//            int length = values.length;
//            for (int i = 0; i < values.length; i++) {
//                if ((i + 1) == length) {
//                    this.setValue(values[i]);
//                } else {
//                    this.setValue(values[i]).append(StrPool.COMMA);
//                }
//            }
//
//            this.append(CharPool.SPACE).append(RIGHT_BRACKET);
//        }
//        return this;
//    }
//
//    public <T> SqlCondition in(Func1<T, ?> func, @NotNull Collection<?> values) {
//        return this.in(true, func, values);
//    }
//
//    public <T> SqlCondition in(boolean condition, Func1<T, ?> func, @NotNull Collection<?> values) {
//        return this.in(condition, LambdaUtil.getFieldName(func), values);
//    }
//
//    public SqlCondition in(String column, @NotNull Collection<?> values) {
//        return this.in(true, column, values);
//    }
//
//    public SqlCondition in(boolean condition, String column, @NotNull Collection<?> values) {
//        if (condition) {
//            this.append(column).append(CharPool.SPACE).append(IN).append(CharPool.SPACE).append(LEFT_BRACKET);
//
//            int i = 1;
//            int size = values.size();
//            Iterator<?> iterator = values.iterator();
//            while (iterator.hasNext()) {
//                if (i == size) {
//                    this.setValue(iterator.next());
//                } else {
//                    this.setValue(iterator.next()).sql.append(StrPool.COMMA);
//                }
//                i++;
//            }
//
//            this.append(CharPool.SPACE).append(RIGHT_BRACKET);
//        }
//        return this;
//
//    }
//
//    public <T> SqlCondition isNull(Func1<T, ?> func) {
//        return this.isNull(true, func);
//    }
//
//    public <T> SqlCondition isNull(boolean condition, Func1<T, ?> func) {
//        return this.isNull(condition, LambdaUtil.getFieldName(func));
//    }
//
//    public SqlCondition isNull(String column) {
//        return this.isNull(true, column);
//    }
//
//    public SqlCondition isNull(boolean condition, String column) {
//        if (condition) {
//            this.append(column).append(CharPool.SPACE).append(IS).append(CharPool.SPACE).append(NULL);
//        }
//        return this;
//    }
//
//    public <T> SqlCondition isNotNull(Func1<T, ?> func) {
//        return this.isNotNull(true, func);
//    }
//
//    public <T> SqlCondition isNotNull(boolean condition, Func1<T, ?> func) {
//        return this.isNotNull(condition, LambdaUtil.getFieldName(func));
//    }
//
//    public SqlCondition isNotNull(String column) {
//        return this.isNotNull(true, column);
//    }
//
//    public SqlCondition isNotNull(boolean condition, String column) {
//        if (condition) {
//            this.append(column).append(CharPool.SPACE).append(IS).append(CharPool.SPACE).append(NOT).append(CharPool.SPACE).append(NULL);
//        }
//        return this;
//    }
//
//    public SqlCondition orderByAsc(@NotNull Object... column) {
//        this.append(CharPool.SPACE).append(ORDER).append(CharPool.SPACE).append(BY).append(CharPool.SPACE);
//
//        int length = column.length;
//        for (int i = 0; i < column.length; i++) {
//            if ((i + 1) == length) {
//                this.append(column[i]);
//            } else {
//                this.append(column[i]).append(StrPool.COMMA);
//            }
//        }
//
//        return this.append(CharPool.SPACE).append(ASC);
//    }
//
//
//    public SqlCondition orderByAsc(@NotNull Collection<?> values) {
//        this.append(CharPool.SPACE).append(ORDER).append(CharPool.SPACE).append(BY).append(CharPool.SPACE);
//
//        int i = 1;
//        int size = values.size();
//        Iterator<?> iterator = values.iterator();
//        while (iterator.hasNext()) {
//            if (i == size) {
//                this.append(iterator.next());
//            } else {
//                this.append(iterator.next()).sql.append(StrPool.COMMA);
//            }
//            i++;
//        }
//
//        return this.append(CharPool.SPACE).append(ASC);
//    }
//
//    public <T> SqlCondition orderByAsc(@NotNull Func1<T, ?>... column) {
//        Set<String> fieldNames = new HashSet<>();
//        for (Func1<T, ?> field : column) fieldNames.add(LambdaUtil.getFieldName(field));
//        return orderByAsc(fieldNames);
//    }
//
//
//    public SqlCondition orderByDesc(@NotNull Object... column) {
//        this.append(CharPool.SPACE).append(ORDER).append(CharPool.SPACE).append(BY).append(CharPool.SPACE);
//
//        int length = column.length;
//        for (int i = 0; i < column.length; i++) {
//            if ((i + 1) == length) {
//                this.append(column[i]);
//            } else {
//                this.append(column[i]).append(StrPool.COMMA);
//            }
//        }
//
//        return this.append(CharPool.SPACE).append(DESC);
//    }
//
//
//    public SqlCondition orderByDesc(@NotNull Collection<?> values) {
//        this.append(CharPool.SPACE).append(ORDER).append(CharPool.SPACE).append(BY).append(CharPool.SPACE);
//
//        int i = 1;
//        int size = values.size();
//        Iterator<?> iterator = values.iterator();
//        while (iterator.hasNext()) {
//            if (i == size) {
//                this.append(iterator.next());
//            } else {
//                this.append(iterator.next()).sql.append(StrPool.COMMA);
//            }
//            i++;
//        }
//
//        return this.append(CharPool.SPACE).append(DESC);
//    }
//
//    public <T> SqlCondition orderByDesc(@NotNull Func1<T, ?>... column) {
//        Set<String> fieldNames = new HashSet<>();
//        for (Func1<T, ?> field : column) fieldNames.add(LambdaUtil.getFieldName(field));
//        return orderByDesc(fieldNames);
//    }
//
//
//    @Override
//    public String build() {
//        String build = this.sql.toString();
//        Console.log(build);
//        return build;
//    }
//
//    @NotNull
//    @Contract(" -> new")
//    public static SqlCondition builder() {
//        return new SqlCondition();
//    }
//
//    public static SqlCondition of(CharSequence sql) {
//        return of(true, sql);
//    }
//
//    public static SqlCondition of(boolean condition, CharSequence sql) {
//        if (condition && (CharSequenceUtil.isNotBlank(sql))) {
//            return builder().append(sql);
//        }
//        return builder();
//    }
//
//    private SqlCondition append(Object value) {
//        this.sql.append(value);
//        return this;
//    }
//
//    public static String selectKeys(Class<?> clazz) {
//        return selectKeys(clazz, Collections.emptyList());
//    }
//
//    @SafeVarargs
//    public static <T> String selectKeys(Class<?> clazz, Func1<T, ?>... excludeFields) {
//        Set<String> fieldNames = new HashSet<>();
//        for (Func1<T, ?> excludeField : excludeFields) fieldNames.add(LambdaUtil.getFieldName(excludeField));
//        return selectKeys(clazz, fieldNames);
//    }
//
//    public static String selectKeys(Class<?> clazz, Collection<String> excludeFields) {
//        return Arrays.stream(ReflectUtil.getFields(clazz)).map(Field::getName).filter(name -> !excludeFields.contains(name)).collect(Collectors.joining(StrPool.COMMA));
//    }
//
//}
