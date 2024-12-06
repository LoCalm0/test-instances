package indi.LoCalm;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

/**
 * 验证变量名称
 */
public class VerifyVariableName {


    public static void main(String[] args) {
        System.out.println(isValidVariableName("a_a"));

    }

    private static final List<String> MYSQL_KEYWORDS = Arrays.asList("modify", "low_priority", "references", "year", "leading", "insensitive", "character", "trailing", "utc_time", "databases", "using", "outer", "require", "then", "each", "as", "slow", "left", "unique", "except", "schema", "accessible", "long", "smallint", "into", "default", "dual", "partition", "varying", "by", "where", "xor", "escape", "current_time", "key", "timestamp", "iterate", "set", "column", "procedure", "right", "union", "asc", "call", "varbinary", "longblob", "geometry", "describe", "to", "linestring", "citext", "localtime", "declare", "enclosed", "continue", "leave", "loop", "elseif", "hour_second", "signal", "multipoint", "add", "mediumtext", "check", "collate", "schemas", "decimal", "desc", "drop", "bool", "asensitive", "for", "show", "revoke", "update", "purge", "not", "zerofill", "load", "straight_join", "json", "ignore", "mediumint", "varchar", "false", "start", "mediumblob", "with", "time", "window", "grant", "day_second", "explain", "select", "release", "usage", "optionally", "delayed", "convert", "localtimestamp", "bit", "when", "else", "lock", "text", "join", "spatial", "if", "write", "between", "case", "sqlstate", "order", "year_month", "having", "natural", "in", "double", "tinytext", "tinyint", "index", "is", "multilinestring", "sensitive", "enum", "multipolygon", "exit", "current_date", "binary", "analyze", "sqlwarning", "force", "interval", "geometrycollection", "primary", "regexp", "range", "sqlexception", "infile", "restrict", "recursive", "foreign", "out", "distinctrow", "fulltext", "table", "current_user", "linear", "second_microsecond", "change", "utc_timestamp", "trigger", "kill", "day_minute", "rlike", "polygon", "rename", "hour_minute", "fetch", "char", "exists", "constraint", "high_priority", "return", "date", "before", "precision", "modifies", "replace", "integer", "float", "while", "uuid", "datetime", "optimize", "limit", "create", "from", "tinyblob", "alter", "group", "all", "like", "cascade", "real", "inner", "separator", "both", "condition", "blob", "null", "true", "day_hour", "option", "longtext", "keys", "outfile", "values", "numeric", "distinct", "insert", "resignal", "delete", "point", "sql", "database", "and", "current_timestamp", "bigint", "terminated", "on", "or", "cross", "match", "int", "inout", "boolean", "utc_date");
    private static final String VAR_NAME_PATTERN = "^[a-zA-Z_$][a-zA-Z0-9_$]*$";

    // Java 保留关键字列表
    private static final String[] JAVA_KEYWORDS = {
            "abstract", "assert", "boolean", "break", "byte", "case", "catch", "char",
            "class", "const", "continue", "default", "do", "double", "else", "enum",
            "extends", "final", "finally", "float", "for", "goto", "if", "implements",
            "import", "instanceof", "int", "interface", "long", "native", "new",
            "null", "package", "private", "protected", "public", "return", "short",
            "static", "strictfp", "super", "switch", "synchronized", "this", "throw",
            "throws", "transient", "try", "void", "volatile", "while"
    };

    /**
     * 校验变量名是否合法
     *
     * @param varName 变量名
     * @return 如果变量名合法返回 true，否则返回 false
     */
    public static boolean isValidVariableName(String varName) {
        if (varName == null || varName.isEmpty()) {
            return false; // 变量名不能为空
        }

        // 使用正则表达式检查格式
        if (!Pattern.matches(VAR_NAME_PATTERN, varName)) {
            return false; // 变量名不符合 Java 标识符规则
        }

        // 检查是否是 Java 关键字
        for (String keyword : JAVA_KEYWORDS) {
            if (varName.equals(keyword)) {
                return false; // 变量名是 Java 关键字
            }
        }

        return true; // 合法变量名
    }

}
