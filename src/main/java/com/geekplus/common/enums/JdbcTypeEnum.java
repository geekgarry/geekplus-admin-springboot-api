/**
 * author     : geekplus
 * email      : geekcjj@gmail.com
 * date       : 7/31/23 10:56
 * description: 做什么的？
 */
package com.geekplus.common.enums;

public enum JdbcTypeEnum {
    ARRAY(2003),
    BIT(-7),
    TINYINT(-6),
    SMALLINT(5),
    INTEGER(4),
    BIGINT(-5),
    FLOAT(6),
    REAL(7),
    DOUBLE(8),
    NUMERIC(2),
    DECIMAL(3),
    CHAR(1),
    VARCHAR(12),
    LONGVARCHAR(-1),
    DATE(91),
    TIME(92),
    TIMESTAMP(93),
    BINARY(-2),
    VARBINARY(-3),
    LONGVARBINARY(-4),
    NULL(0),
    OTHER(1111),
    BLOB(2004),
    CLOB(2005),
    BOOLEAN(16),
    CURSOR(-10),
    UNDEFINED(-2147482648),
    NVARCHAR(-9),
    NCHAR(-15),
    NCLOB(2011),
    STRUCT(2002),
    JAVA_OBJECT(2000),
    DISTINCT(2001),
    REF(2006),
    DATALINK(70),
    ROWID(-8),
    LONGNVARCHAR(-16),
    SQLXML(2009),
    DATETIMEOFFSET(-155),
    TIME_WITH_TIMEZONE(2013),
    TIMESTAMP_WITH_TIMEZONE(2014);

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    private Integer code;

    JdbcTypeEnum(int i) {
        this.code = i;
    }
}
