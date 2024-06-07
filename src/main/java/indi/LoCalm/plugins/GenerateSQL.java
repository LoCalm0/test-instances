package indi.LoCalm.plugins;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.Console;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.google.common.collect.ImmutableMap;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.*;

/**
 * @author LoCalm
 */
public class GenerateSQL {

    private static final String TABLE = "IM_BS_CUSTOMER";
    private static final String INSERT_INTO = "INSERT INTO";

    public static void main(String[] args) {
        Map<String, String> fieldMap = ImmutableMap.ofEntries(
                MapUtil.entry("customer_no", "客户代码"),
                MapUtil.entry("customer_name", "客户名称")
        );
        Map<String, Object> constantMap = ImmutableMap.ofEntries(
                MapUtil.entry("id", IdUtil.getSnowflakeNextId()),
                MapUtil.entry("tenant_id", addSingleQuotationMarks("709495")),
                MapUtil.entry("is_deleted", 0),
                MapUtil.entry("create_user", 1767789521801375745L),
                MapUtil.entry("create_time", System.currentTimeMillis())
        );

        String field0 = clearTheParentheses(Convert.toStr(fieldMap.keySet()));
        String field1 = clearTheParentheses(Convert.toStr(constantMap.keySet()));
        String field = CharSequenceUtil.isEmpty(field1) ? field0 : field1 + ", " + field0;
        StringBuilder start = CharSequenceUtil.builder(INSERT_INTO).append(" ").append(TABLE).append("(").append(field).append(")").append(" VALUES");
        StringBuilder builder = new StringBuilder(start);
        run(null, ",", builder, "D:/IM/备份/客户.xlsx", fieldMap, constantMap);
        String toString = modifyTheEnding(builder.toString().trim(), ",", ";");
        Console.log(toString);
    }

    private static void run(@Nullable String start, String end, StringBuilder builder, String excelUrl, Map<String, String> fieldMap, Map<String, Object> constantMap) {
        if (MapUtil.isEmpty(fieldMap)) return;

        List<Map<String, Object>> maps = readerExcelInfo(excelUrl);
        int size = removeIsEmptyValue(maps.get(0)).keySet().size();

        for (Map<String, Object> map : maps) {
            Map<String, Object> removeMap = removeIsEmptyValue(map);
            if (size != removeMap.size()) {
                throw new RuntimeException("key长度不一致");
            }
            theMiddlePartAssemblesTheSQL(start, end, builder, removeMap, fieldMap, constantMap);
        }
    }

    /**
     * 中间部分组装 SQL
     */
    private static void theMiddlePartAssemblesTheSQL(String start, String end, StringBuilder build, Map<String, Object> data, @NotNull Map<String, String> fieldMap, @NotNull Map<String, Object> constantMap) {
        StringBuilder builder = new StringBuilder();
        if (CharSequenceUtil.isNotEmpty(start)) {
            builder = new StringBuilder(start);
        }
        builder.append("(");

        List<Object> fieldValue = MapUtil.valuesOfKeys(data, fieldMap.values().iterator());
        for (int i = 0; i < fieldValue.size(); i++) {
//            if (i == 0) continue; //i为0不加单引号
            Object value = fieldValue.get(i);
            fieldValue.set(i, addSingleQuotationMarks(value));
        }

        String constantValue = clearTheParentheses(Convert.toStr(constantMap.values()));
        String strFieldValue = clearTheParentheses(Convert.toStr(fieldValue));
        if (MapUtil.isEmpty(constantMap)) {
            builder.append(strFieldValue);
        } else {
            builder.append(constantValue);
            builder.append(", ").append(strFieldValue);
        }
        builder.append(")").append(end).append("\n");
        build.append(builder);
    }


    @Contract(pure = true)
    public static @NotNull String addSingleQuotationMarks(Object str) {
        return "\'" + str + "\'";
    }

    /**
     * 清除开头和结尾中括号
     *
     * @return 清除开头和结尾中括号
     */
    private static @NotNull String clearTheParentheses(@NotNull String value) {
        return value.replaceFirst("\\[", "").replaceFirst("\\]", "");
    }

    /**
     * 修改结尾
     *
     * @param str    数据
     * @param end    结尾
     * @param revise 修改值
     * @return 如果结尾为end修改为revise
     * <p>如果不满足不做修改</p>
     */
    private static @NotNull String modifyTheEnding(@NotNull String str, String end, String revise) {
        if (str.endsWith(end)) {
            str = str.substring(0, str.length() - 1) + revise;
        }
        return str;
    }

    /**
     * 读取Excel信息
     *
     * @param excelUrl 文件地址
     * @return Map的列表
     */
    private static List<Map<String, Object>> readerExcelInfo(@NotNull String excelUrl) {
        List<Map<String, Object>> result = new ArrayList<>();
        if (excelUrl.isEmpty()) {
            return Collections.emptyList();
        }
        try {
            ExcelReader excelReader = ExcelUtil.getReader(new File(excelUrl));
            //读取为Map列表，默认第一行为标题行，Map中的key为标题，value为标题对应的单元格值。
            result = excelReader.readAll();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


    /**
     * 去除Map中值为空的键值对<br>
     * 注意：此方法在传入的Map上直接修改。
     *
     * @param <K> key的类型
     * @param <V> value的类型
     * @param map Map
     * @return map
     * @see ObjectUtil#isEmpty(Object)
     */
    private static <K, V> Map<K, V> removeIsEmptyValue(Map<K, V> map) {
        if (MapUtil.isEmpty(map)) {
            return map;
        }

        final Iterator<Map.Entry<K, V>> iter = map.entrySet().iterator();
        Map.Entry<K, V> entry;
        while (iter.hasNext()) {
            entry = iter.next();
            if (ObjectUtil.isEmpty(entry.getValue())) {
                iter.remove();
            }
        }

        return map;
    }
}
