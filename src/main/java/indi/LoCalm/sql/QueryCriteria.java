package indi.LoCalm.sql;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.Opt;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.json.JSONUtil;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class QueryCriteria {

    /**
     * @author LoCalm
     */
    private List<Object> filter(List<Object> list, Map<String, IdentityAndValue> fieldIdentityAndValue) {
        return list.stream().filter(item -> {
            Map<String, Object> map = Opt.ofTry(() -> JSONUtil.parseObj("").<Map<String, Object>>toBean(Map.class)).orElse(Collections.emptyMap());
            if (MapUtil.isEmpty(map)) return true;
            for (Map.Entry<String, IdentityAndValue> entry : fieldIdentityAndValue.entrySet()) {
                if (!this.queryCriteria(map.get(entry.getKey()), entry.getValue())) return false;
            }
            return true;
        }).collect(Collectors.toList());
    }

    /**
     * 查询条件
     *
     * @param value            数据值
     * @param identityAndValue 条件标识和条件值
     * @return 条件是否通过
     * @author LoCalm
     */
    private boolean queryCriteria(Object value, IdentityAndValue identityAndValue) {
        switch (identityAndValue.getIdentity()) {
            case EQUAL:
            case DATE_EQUAL:
                return Objects.equals(value, identityAndValue.getValue());
            case NOT_EQUAL:
                return !Objects.equals(value, identityAndValue.getValue());
            case LIKE:
                return CharSequenceUtil.contains(Convert.toStr(value), Convert.toStr(identityAndValue.getValue()));
            case NOT_LIKE:
                return !CharSequenceUtil.contains(Convert.toStr(value), Convert.toStr(identityAndValue.getValue()));
            case LIKE_LEFT:
                return CharSequenceUtil.startWith(Convert.toStr(value), Convert.toStr(identityAndValue.getValue()));
            case LIKE_RIGHT:
                return CharSequenceUtil.endWith(Convert.toStr(value), Convert.toStr(identityAndValue.getValue()));
            case GE:
            case DATE_GE:
                return Opt.ofNullable(this.compareTo(value, identityAndValue.getValue())).map(i -> i != -1).orElse(false);
            case LE:
            case DATE_LE:
                return Opt.ofNullable(this.compareTo(value, identityAndValue.getValue())).map(i -> i != 1).orElse(false);
            case GT:
            case DATE_GT:
                return Opt.ofNullable(this.compareTo(value, identityAndValue.getValue())).map(i -> i == 1).orElse(false);
            case LT:
            case DATE_LT:
                return Opt.ofNullable(this.compareTo(value, identityAndValue.getValue())).map(i -> i == -1).orElse(false);
            case NULL:
                return value == null;
            case NOT_NULL:
                return value != null;
            case NUMBER_IN:
                return Opt.ofEmptyAble(Convert.toList(Long.TYPE, identityAndValue.getValue())).map(arr -> arr.contains(Convert.toLong(value))).orElse(true);
            case STRING_IN:
                return Opt.ofEmptyAble(Convert.toList(String.class, identityAndValue.getValue())).map(arr -> arr.contains(Convert.toStr(value))).orElse(true);
            case DECIMAL_IN:
                return Opt.ofEmptyAble(Convert.toList(BigDecimal.class, identityAndValue.getValue())).map(arr -> arr.contains(Convert.toBigDecimal(value))).orElse(true);
            case BETWEEN:
                return Opt.ofEmptyAble(Convert.toList(Object.class, identityAndValue.getValue())).map(values -> values.size() == 2 && this.compareTo(value, values.get(0)) >= 0 && this.compareTo(value, values.get(1)) <= 0).orElse(false);
            default:
                return true;
        }
    }

    private Integer compareTo(Object value, Object identityValue) {
        if (value == null || identityValue == null) return null;
        return Opt.ofTry(() -> ReflectUtil.<Integer>invoke(value, "compareTo", identityValue)).orElse(null);
    }

}
