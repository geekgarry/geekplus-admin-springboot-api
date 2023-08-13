package com.geekplus.webapp.tool.generator.entity;

import com.geekplus.common.domain.BaseEntity;

import java.util.Date;
import java.util.List;

/**
 * @program: spring-boot-project-mybatis
 * @description: 数据表的详细信息
 * @author: GarryChan
 * @create: 2020-11-27 09:23
 **/
public class TableInfo extends BaseEntity {

    /**
    表的ID
     */
    private Long tableId;
    /**
    表的名字
     */
    private String tableName;
    /**
    表的注释
     */
    private String tableComment;
    /**
     类名
     */
    private String className;
    /**
    实体类名
     */
    private String modelName;
    /**
    使用的模板，操作类型，curd表操作，tree树操作
     */
    private String tplCategory;
    /**
    包名
     */
    private String packageName;
    /**
    基础包名
     */
    private String basePackageName;
    /**
    模块名，也就是这个功能名
     */
    private String moduleName;
    /**
    业务名
     */
    private String businessName;
    /**
    功能名
     */
    private String functionName;
    /**
    功能作者
     */
    private String functionAuthor;
    /**
    主键的列
     */
    private TableColumnInfo pkColumn;
    /**
    其他
     */
    private String other;

    /**
     创建者
     */
    private String createBy;
    /**
     创建时间
     */
    private Date createTime;
    /**
     更新者
     */
    private String updateBy;
    /**
     更新时间
     */
    private Date updateTime;
    /**
     备注
     */
    private String remark;

    /**
     所有的列
     */
    private List<TableColumnInfo> allColumns;

    public Long getTableId() {
        return tableId;
    }

    public void setTableId(Long tableId) {
        this.tableId = tableId;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getTableComment() {
        return tableComment;
    }

    public void setTableComment(String tableComment) {
        this.tableComment = tableComment;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public String getTplCategory() {
        return tplCategory;
    }

    public void setTplCategory(String tplCategory) {
        this.tplCategory = tplCategory;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getBasePackageName() {
        return basePackageName;
    }

    public void setBasePackageName(String basePackageName) {
        this.basePackageName = basePackageName;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public String getFunctionName() {
        return functionName;
    }

    public void setFunctionName(String functionName) {
        this.functionName = functionName;
    }

    public String getFunctionAuthor() {
        return functionAuthor;
    }

    public void setFunctionAuthor(String functionAuthor) {
        this.functionAuthor = functionAuthor;
    }

    public TableColumnInfo getPkColumn() {
        return pkColumn;
    }

    public void setPkColumn(TableColumnInfo pkColumn) {
        this.pkColumn = pkColumn;
    }

    public String getOther() {
        return other;
    }

    public void setOther(String other) {
        this.other = other;
    }

    @Override
    public String getCreateBy() {
        return createBy;
    }

    @Override
    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    @Override
    public Date getCreateTime() {
        return createTime;
    }

    @Override
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String getUpdateBy() {
        return updateBy;
    }

    @Override
    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    @Override
    public Date getUpdateTime() {
        return updateTime;
    }

    @Override
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String getRemark() {
        return remark;
    }

    @Override
    public void setRemark(String remark) {
        this.remark = remark;
    }

    public List<TableColumnInfo> getAllColumns() {
        return allColumns;
    }

    public void setAllColumns(List<TableColumnInfo> allColumns) {
        this.allColumns = allColumns;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
