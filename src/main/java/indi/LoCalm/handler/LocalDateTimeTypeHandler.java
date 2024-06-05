//package indi.LoCalm.handler;

//import org.apache.ibatis.type.BaseTypeHandler;
//import org.apache.ibatis.type.JdbcType;
//import org.apache.ibatis.type.MappedJdbcTypes;
//import org.apache.ibatis.type.MappedTypes;
//
//import java.sql.*;
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.time.LocalTime;
//import java.util.Objects;
//
//
//@MappedJdbcTypes({JdbcType.TIMESTAMP})
//@MappedTypes({LocalDateTime.class, LocalDate.class, LocalTime.class})
//public class LocalDateTimeTypeHandler extends BaseTypeHandler<LocalDateTime> {
//
//    @Override
//    public void setNonNullParameter(PreparedStatement ps, int i, LocalDateTime parameter, JdbcType jdbcType) throws SQLException {
//        Timestamp timestamp = Timestamp.valueOf(parameter);
//        ps.setObject(i, timestamp);
//    }
//
//    @Override
//    public LocalDateTime getNullableResult(ResultSet rs, String columnName) throws SQLException {
//        Timestamp timestamp = rs.getTimestamp(columnName);
//        return Objects.nonNull(timestamp) ? timestamp.toLocalDateTime() : null;
//    }
//
//    @Override
//    public LocalDateTime getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
//        Timestamp timestamp = rs.getTimestamp(columnIndex);
//        return Objects.nonNull(timestamp) ? timestamp.toLocalDateTime() : null;
//    }
//
//    @Override
//    public LocalDateTime getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
//        Timestamp timestamp = cs.getTimestamp(columnIndex);
//        return Objects.nonNull(timestamp) ? timestamp.toLocalDateTime() : null;
//    }
//}