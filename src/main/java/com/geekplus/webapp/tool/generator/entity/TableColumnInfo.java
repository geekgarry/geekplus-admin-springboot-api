package com.geekplus.webapp.tool.generator.entity;

import com.geekplus.common.domain.BaseEntity;
import com.geekplus.common.util.StringUtils;
import com.geekplus.common.util.translate.TranslatorUtil;
import com.geekplus.webapp.tool.generator.utils.GenUtil;

import javax.validation.constraints.NotBlank;

/**
 * @program: spring-boot-project-mybatis
 * @description: 表的字段的详细信息
 * @author: GarryChan
 * @create: 2020-11-27 09:14
 **/
public class TableColumnInfo extends BaseEntity {
    private Integer columnId;
    /** 数据库字段名称 **/
    private String columnName;
    /** 数据库字段类型 **/
    private String columnType;
    /** 数据库字段类型 **/
    private String columnDataType;
    /** 数据库字段首字母小写且去掉下划线字符串 **/
    private String smallColumnName;
    /** 数据库字段的属性名，去掉下划线每一段首字母全部都是大写 **/
    private String bigColumnName;
    /** 数据库字段注释 **/
    private String columnComment;
    /** 数据库字段长度 **/
    private int columnLength;
    /** 数据表字段的Java类型 **/
    private String javaType;
    /** JAVA字段名 */
    @NotBlank(message = "Java属性不能为空")
    private String javaField;
    /** 是否主键（1是） */
    private String isPk;
    /** 是否自增（1是） */
    private String isIncrement;
    /** 是否必填（1是） */
    private String isRequired;
    /** 字典类型 */
    private String dictType;
    /** 是否列表字段（1是） */
    private String isList;

    /** 是否查询字段（1是） */
    private String isQuery;

    /** 查询方式（EQ等于、NE不等于、GT大于、LT小于、LIKE模糊、BETWEEN范围） */
    private String queryType;

    /** 显示类型（input文本框、textarea文本域、select下拉框、checkbox复选框、radio单选框、datetime日期控件） */
    private String htmlType;

    /** 排序 */
    private Integer sort;

    /** 关联表ID */
    private Integer tableId;

    public Integer getColumnId() {
        return columnId;
    }

    public void setColumnId(Integer columnId) {
        this.columnId = columnId;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getColumnType() {
        return columnType;
    }

    public void setColumnType(String columnType) {
        this.columnType = columnType;
    }

    public String getColumnDataType() {
        return GenUtil.getDbColumnType(getColumnType());
    }

    public void setColumnDataType(String columnDataType) {
        this.columnDataType = columnDataType;
    }

    public String getSmallColumnName() {
        //字段名驼峰开头小写
        return GenUtil.columnNameToVarName(getColumnName());
    }

    public void setSmallColumnName(String smallColumnName) {
        this.smallColumnName = smallColumnName;
    }

    public String getBigColumnName() {
        //字段名驼峰大写
        return GenUtil.columnNameToProName(getColumnName());
    }

    public void setBigColumnName(String bigColumnName) {
        this.bigColumnName = bigColumnName;
    }

    public String getColumnComment() {
        if(columnCommentIsEmpty(columnComment)){
            return getTranslateColumnName(getSmallColumnName());
        }
        return columnComment;
    }

    public void setColumnComment(String columnComment) {
        this.columnComment = columnComment;
    }

    public int getColumnLength() {
        return columnLength;
    }

    public void setColumnLength(int columnLength) {
        this.columnLength = columnLength;
    }

    public String getJavaType() {
        /*
        getJavaType()中做一些字段处理的操作
         */
        String dbColumnType= getColumnDataType();
        return GenUtil.dbTypeToJavaType(dbColumnType);
    }

    public void setJavaType(String javaType) {
        this.javaType = javaType;
    }

    public String getJavaField() {
        //字段名驼峰大写处理
        return GenUtil.columnNameToProName(getColumnName());
    }

    public void setJavaField(String javaField) {
        this.javaField = javaField;
    }

    public String getIsPk() {
        return isPk;
    }

    public void setIsPk(String isPk) {
        this.isPk = isPk;
    }

    public String getIsIncrement() {
        return isIncrement;
    }

    public void setIsIncrement(String isIncrement) {
        this.isIncrement = isIncrement;
    }

    public String getIsRequired() {
        return isRequired;
    }

    public void setIsRequired(String isRequired) {
        this.isRequired = isRequired;
    }

    public String getDictType() {
        String dbColumnType= getColumnDataType();
        return GenUtil.mybatisType(dbColumnType);
    }

    public void setDictType(String dictType) {
        this.dictType = dictType;
    }
    public String getIsList() {
        return isList;
    }

    public void setIsList(String isList) {
        this.isList = isList;
    }

    public String getIsQuery() {
        return isQuery;
    }

    public void setIsQuery(String isQuery) {
        this.isQuery = isQuery;
    }

    public String getQueryType() {
        return queryType;
    }

    public void setQueryType(String queryType) {
        this.queryType = queryType;
    }

    public String getHtmlType() {
        return htmlType;
    }

    public void setHtmlType(String htmlType) {
        this.htmlType = htmlType;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Integer getTableId() {
        return tableId;
    }

    public void setTableId(Integer tableId) {
        this.tableId = tableId;
    }

    public boolean isSuperColumn()
    {
        return isSuperColumn(this.javaField);
    }

    //判断列表注释是否为空
    public boolean columnCommentIsEmpty(String columnComment){
        return StringUtils.isEmpty(columnComment);
    }

    //获取字段翻译中文
    public String getTranslateColumnName(String smallColumnName){
        return TranslatorUtil.translate(smallColumnName);
    }

    public static boolean isSuperColumn(String javaField)
    {
        return StringUtils.equalsAnyIgnoreCase(javaField,
                // BaseEntity
                "createBy", "createTime", "updateBy", "updateTime", "remark",
                // TreeEntity
                "parentName", "parentId", "orderNum", "ancestors");
    }
}
