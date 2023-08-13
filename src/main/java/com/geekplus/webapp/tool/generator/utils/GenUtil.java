package com.geekplus.webapp.tool.generator.utils;

import com.geekplus.common.constant.ProjectConstant;
import com.geekplus.common.util.StringUtils;
import com.geekplus.webapp.tool.generator.entity.TableColumnInfo;
import com.geekplus.webapp.tool.generator.entity.TableInfo;

import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 * @program: spring-boot-project-mybatis
 * @description: 代码生成的操作以及一些字段属性以及表名的一些操作
 * @author: GarryChan
 * @create: 2020-11-27 10:45
 **/
public class GenUtil {
    /**
     * 首字母大写
     * @param str
     * @return
     */
    public static String capitaFirstLetter(String str) {
        if (null == str) {
            return null;
        } else if ("".equals(str)) {
            return str;
        }
        return Character.toTitleCase(str.charAt(0)) + str.substring(1);
    }

    /**
     * 首字母小写
     * @param str
     * @return
     */
    public static String capitaFirstLowerCase(String str) {
        if (null == str) {
            return null;
        } else if ("".equals(str)) {
            return str;
        }
        return Character.toLowerCase(str.charAt(0)) + str.substring(1);
    }

    /**
     * 把表名中的_替换掉，把被_隔开的首字母都变为大写
     * @param tableName
     * @return
     */
    public static String mapUnderscoreToCamelCase(String tableName){
        StringBuilder sb = new StringBuilder();
        // 清除sb缓存
        sb.setLength(0);
        //String tableNameNew = tableName.contains("tb_") ? tableName.replaceFirst("tb_", "") : tableName;
        String tableNameNew =tableName;
        if(tableName.contains("tb_")){
            tableNameNew=tableName.replaceFirst("tb_", "");
        }
        String [] tableNameArray =  tableNameNew.split("_");
        Integer length = tableNameArray.length;
        // 重新赋值
        for(int i=0;i<length;i++){
            sb.append((tableNameArray[i].charAt(0)+"").toUpperCase());
            sb.append(tableNameArray[i].substring(1));
        }
        return sb.toString();
    }

    /**
     * 以_分割表名为数组，取最后一个
     * @param tableName
     * @return
     */
    public static String getLastBusinessName(String tableName){
        StringBuilder sb = new StringBuilder();
        // 清除sb缓存
        sb.setLength(0);
        String tableNameNew = tableName.contains("tb_") ? tableName.replaceFirst("tb_", "") : tableName;
        String [] tableNameArray =  tableNameNew.split("_");
        Integer length = tableNameArray.length;
        // 重新赋值
        sb.append(tableNameArray[length-1]);
//        sb.append(tableNameArray[0]);
//        for(int i=1;i<length;i++){
//            sb.append((tableNameArray[i].charAt(0)+"").toUpperCase());
//            sb.append(tableNameArray[i].substring(1));
//        }
        return sb.toString();
    }

    /**
     * 以_分割表名为数组，去掉第一个元素组成新字符串，且组成的新字符串首字母小写，用于api接口URL
     * @param tableName
     * @return
     */
    public static String getApiMethodLowerCaseName(String tableName){
        if (null == tableName) {
            return null;
        } else if ("".equals(tableName)) {
            return tableName;
        }
        String strNam=mapTableNameToVueJSBusinessName(tableName);
        if(Character.isLowerCase(strNam.charAt(0))){
            return strNam;
        }
        return Character.toLowerCase(strNam.charAt(0)) + strNam.substring(1);
    }

    /**
     * 以_分割表名为数组，如果长度大于三去掉第一个元素组成新字符串,等于二则直接组成
     * 每段首字母大写组成新字符串，表示为Js方法名或者是Vue组件名，业务名
     * @param tableName
     * @return
     */
    public static String mapTableNameToVueJSBusinessName(String tableName){
        StringBuilder sb = new StringBuilder();
        // 清除sb缓存
        sb.setLength(0);
        String tableNameNew = tableName.contains("tb_") ? tableName.replaceFirst("tb_", "") : tableName;
        String [] tableNameArray =  tableNameNew.split("_");
        Integer length = tableNameArray.length;
        if(length>2){
            for(int i=1;i<length;i++){
                sb.append((tableNameArray[i].charAt(0)+"").toUpperCase());
                sb.append(tableNameArray[i].substring(1));
            }
        }else if(length==2){
            // 重新赋值
            for(int i=0;i<length;i++){
                sb.append((tableNameArray[i].charAt(0)+"").toUpperCase());
                sb.append(tableNameArray[i].substring(1));
            }
        }else{
            sb.append(tableNameNew);
        }
        return sb.toString();
    }

    /**
     * 把表名中的_替换掉，把被_隔开的首字母都变为大写，但第一段的首字母为小写
     * @param tableName
     * @return
     */
    public static String mapTableNameToVarName(String tableName){
        StringBuilder sb = new StringBuilder();

        // 清除sb缓存
        sb.setLength(0);
//        String tableNameNew =tableName;
//        if(tableName.contains("tb_")){
//            tableNameNew=tableName.replaceFirst("tb_", "");
//        }
        String tableNameNew = tableName.replaceFirst("tb_", "");
        String [] tableNameArray =  tableNameNew.split("_");
        Integer length = tableNameArray.length;
        // 重新赋值
        sb.append(tableNameArray[0]);
        for(int i=1;i<length;i++){
            sb.append((tableNameArray[i].charAt(0)+"").toUpperCase());
            sb.append(tableNameArray[i].substring(1));
        }
        return sb.toString();
    }
    /**
     * 将表中列名去下划线且下划线后每一段首字母大写其他字母小写
     * @param columnName 表中列名
     * @return java类属性名
     */
    public static String columnNameToProName(String columnName) {
        if (columnName == null) {
            return null;
        }
        StringBuilder fieldName = new StringBuilder();
        boolean toUpper = false;
        for (int i = 0; i < columnName.length(); i++) {
            char ch = columnName.charAt(i);
            if (ch == '_') {
                toUpper = true;
            } else if (toUpper == true) {
                fieldName.append(Character.toUpperCase(ch));
                toUpper = false;
            } else {
                fieldName.append(Character.toLowerCase(ch));
            }
        }
        return capitaFirstLetter(fieldName.toString());
    }

    /**
     * 将表中列名去下划线且下划线后第一段字符串首字母小写其他每段首字母都大写
     * @param columnName 表中列名
     * @return java类属性名
     */
    public static String columnNameToVarName(String columnName) {
        if (columnName == null) {
            return null;
        }
        StringBuilder fieldName = new StringBuilder();
        boolean toUpper = false;
        for (int i = 0; i < columnName.length(); i++) {
            char ch = columnName.charAt(i);
            if (ch == '_') {
                toUpper = true;
            } else if (toUpper == true) {
                fieldName.append(Character.toUpperCase(ch));
                toUpper = false;
            } else {
                fieldName.append(Character.toLowerCase(ch));
            }
        }
        return fieldName.toString();
    }
    /**
     * 将数据库类型转换成java类型
     * @param columnType 数据库类型
     * @return java类型
     */
    public static String dbTypeToJavaType(String columnType) {
        String type = "";
        if (columnType == null || columnType.trim().equals("")) {
            return null;
        }
        if (columnType.equals("VARCHAR")||columnType.equals("varchar")) {
            type = "String";
        } else if (columnType.equals("DATE")||columnType.equals("date")) {
            type = "Date";
        } else if (columnType.equals("YEAR")||columnType.equals("year")) {
            type = "Date";
        } else if (columnType.equals("DATETIME")||columnType.equals("TIMESTAMP")||columnType.equals("TIME")) {
            type = "Date";
        } else if (columnType.equals("datetime")||columnType.equals("timestamp")||columnType.equals("time")) {
            type = "Date";
        } else if (columnType.equals("CHAR")||columnType.equals("char")) {
            type = "String";
        } else if (columnType.equals("INT")||columnType.equals("int")) {
            type = "Integer";
        } else if (columnType.equals("INT UNSIGNED")||columnType.equals("int unsigned")) {
            type = "Integer";
        } else if (columnType.equals("TINYINT")||columnType.equals("tinyint")) {
            type = "Integer";
        } else if (columnType.equals("BIGINT")||columnType.equals("bigint")) {
            type = "Long";
        } else if (columnType.equals("MEDIUMINT")||columnType.equals("mediumint")) {
            type = "Long";
        } else if (columnType.equals("SMALLINT")||columnType.equals("smallint")) {
            type = "Integer";
        } else if (columnType.equals("TINYTEXT")||columnType.equals("tinytext")) {
            type = "String";
        } else if (columnType.equals("MEDIUMTEXT")||columnType.equals("mediumtext")) {
            type = "String";
        } else if (columnType.equals("TEXT")||columnType.equals("text")) {
            type = "String";
        } else if (columnType.equals("LONGTEXT")||columnType.equals("longtext")) {
            type = "String";
        } else if (columnType.equals("MULTILINESTRING")||columnType.equals("multilinestring")) {
            type = "String";
        } else if (columnType.equals("LINESTRING")||columnType.equals("linestring")) {
            type = "String";
        } else if (columnType.equals("GEOMETRY")||columnType.equals("geometry")) {
            type = "String";
        } else if (columnType.equals("GEOMETRYCOLLECTION")||columnType.equals("geometrycollection")) {
            type = "String";
        } else if (columnType.equals("POLYGON")||columnType.equals("polygon")) {
            type = "String";
        } else if (columnType.equals("POINT")||columnType.equals("point")) {
            type = "String";
        } else if (columnType.equals("MEDIUMBLOB")||columnType.equals("mediumblob")) {
            type = "byte[]";
        } else if (columnType.equals("LONGBLOB")||columnType.equals("longblob")) {
            type = "byte[]";
        } else if (columnType.equals("TINYBLOB")||columnType.equals("tinyblob")) {
            type = "byte[]";
        } else if (columnType.equals("DOUBLE")||columnType.equals("double")) {
            type = "Double";
        } else if (columnType.equals("FLOAT")||columnType.equals("float")) {
            type = "Float";
        } else if (columnType.equals("DECIMAL")||columnType.equals("decimal")) {
            type = "BigDecimal";
        } else if (columnType.equals("NUMERIC")||columnType.equals("numeric")) {
            type = "Long";
        } else if (columnType.equals("ENUM")||columnType.equals("enum")) {
            type = "String";
        } else if (columnType.equals("LONGVARCHAR")||columnType.equals("longvarchar")) {
            type = "String";
        } else if (columnType.equals("BIT")||columnType.equals("bit")) {
            type = "byte[]";
        } else {
            System.out.println("columnType[" + columnType + "]");
            type = null;
        }
        return type;
    }
    /**
     * 将数据库类型转换成mybatis配置文件类型
     * @param sqlTypeName 数据库类型
     * @return mybatis配置文件类型
     */
    public static String mybatisType(String sqlTypeName){
        String type = "";
        if (sqlTypeName == null || sqlTypeName.trim().equals("")) {
            return null;
        }
        if(sqlTypeName.equals("varchar") || sqlTypeName.equals("text")){
            type = "VARCHAR";
        }else if(sqlTypeName.equals("tinyblob") || sqlTypeName.equals("blob") || sqlTypeName.equals("mediumblob")){
            type = "BLOB";
        } else if (sqlTypeName.equals("blob")) {
            type = "BLOB";
        } else if (sqlTypeName.equals("clob")) {
            type = "CLOB";
        } else if (sqlTypeName.equals("float")) {
            type = "FLOAT";
        } else if (sqlTypeName.equals("double")) {
            type = "DOUBLE";
        } else if (sqlTypeName.contains("int") || sqlTypeName.contains("INT")) {
            type = "INTEGER";
        } else if (sqlTypeName.contains("TEXT") || sqlTypeName.contains("text")) {
            type = "VARCHAR";
        } else if (sqlTypeName.equals("NUMERIC") || sqlTypeName.equals("numeric")) {
            type = "NUMERIC";
        } else if (sqlTypeName.equals("REAL") || sqlTypeName.equals("real")) {
            type = "REAL";
        } else if (sqlTypeName.equals("SET") || sqlTypeName.equals("set")) {
            type = "SET";
        } else if (sqlTypeName.equals("BINARY") || sqlTypeName.equals("binary")) {
            type = "BINARY";
        } else if (sqlTypeName.equals("MULTILINESTRING")||sqlTypeName.equals("multilinestring")) {
            type = "VARCHAR";
        } else if (sqlTypeName.equals("LINESTRING")||sqlTypeName.equals("linestring")) {
            type = "VARCHAR";
        } else if (sqlTypeName.equals("GEOMETRY")||sqlTypeName.equals("geometry")) {
            type = "VARCHAR";
        } else if (sqlTypeName.equals("GEOMETRYCOLLECTION")||sqlTypeName.equals("geometrycollection")) {
            type = "VARCHAR";
        } else if (sqlTypeName.equals("POLYGON")||sqlTypeName.equals("polygon")) {
            type = "VARCHAR";
        } else if (sqlTypeName.equals("bigint")) {
            type = "INTEGER";
        } else if (sqlTypeName.equals("tinyint")) {
            type = "INTEGER";
        } else if (sqlTypeName.equals("smallint")) {
            type = "INTEGER";
        } else if (sqlTypeName.equals("char")) {
            type = "CHAR";
        } else if (sqlTypeName.equals("int")) {
            type = "INTEGER";
        } else if (sqlTypeName.equals("datetime") || sqlTypeName.equals("date") || sqlTypeName.equals("timestamp")) {
            type = "TIMESTAMP";
        } else if (sqlTypeName.equals("time")) {
            type = "DATE";
        } else if (sqlTypeName.equals("decimal")) {
            type = "DECIMAL";
        } else if (sqlTypeName.equals("int unsigned")) {
            type = "INTEGER";
        } else {
            System.out.println("sqlTypeName[" + sqlTypeName + "]");
            type = null;
        }
        return type;
    }
    public static String getFileName(String tableName, String flag){
        String retName;
        switch(flag){
            case "Dto"  : retName = tableName + "Dto"; break;
            case "Vo"  : retName = tableName + "Vo"; break;
            default : retName = null;
        }
        return retName;
    }

    //拆分表明形成后台Api请求地址
    public static String getRequestMapping(String tableName){
        StringBuilder sb = new StringBuilder();
        // 清除sb缓存
        sb.setLength(0);
        String tableNameNew = tableName.replaceFirst("tb_", "");
        String [] tableNameArray =  tableNameNew.split("_");
        Integer length = tableNameArray.length;
        // 重新赋值
        if(length>2){
            sb.append("/"+tableNameArray[0]);
            sb.append("/"+getApiMethodLowerCaseName(tableNameNew));
        }else if(length==2){
            for(int i=0;i<length;i++){
                sb.append("/"+tableNameArray[i]);
            }
        }else{
            sb.append("/"+tableNameNew);
        }
        return sb.toString();
    }

    /**
     * 获取数据库类型字段
     *
     * @param columnType 列类型
     * @return 截取后的列类型
     */
    public static String getDbColumnType(String columnType)
    {
        if (StringUtils.indexOf(columnType, "(") > 0)
        {
            return StringUtils.substringBefore(columnType, "(");
        }
        else
        {
            return columnType;
        }
    }

    /**
     * 根据列类型获取导入包
     *
     * @param columns 列集合
     * @return 返回需要导入的包列表
     */
    public static HashSet<String> getImportList(List<TableColumnInfo> columns)
    {
        HashSet<String> importList = new HashSet<String>();
        for (TableColumnInfo column : columns)
        {
            if (!column.isSuperColumn() && ProjectConstant.TYPE_DATE.equals(column.getJavaType()))
            {
                importList.add("java.util.Date");
                importList.add("com.fasterxml.jackson.annotation.JsonFormat");
            }
            else if (!column.isSuperColumn() && ProjectConstant.TYPE_BIGDECIMAL.equals(column.getJavaType()))
            {
                importList.add("java.math.BigDecimal");
            }
            else if (!column.isSuperColumn() && ProjectConstant.TYPE_BIGINTEGER.equals(column.getJavaType()))
            {
                importList.add("java.math.BigInteger");
            }
        }
        return importList;
    }

    /**
     * 获取字段长度
     *
     * @param columnType 列类型
     * @return 截取后的列类型
     */
    public static Integer getColumnLength(String columnType)
    {
        if (StringUtils.indexOf(columnType, "(") > 0)
        {
            String length = StringUtils.substringBetween(columnType, "(", ")");
            return Integer.valueOf(length);
        }
        else
        {
            return 0;
        }
    }

    /**
     * 把表名以_分割转换为每一小段的首字母缩写，用于表的别名
     * @param tableName
     * @return
     */
    public static String getFirstChar(String tableName){
        StringBuilder sb = new StringBuilder();
        // 清除sb缓存
        sb.setLength(0);
        String tableNameNew = tableName.replaceFirst("tb_", "");
        String [] tableNameArray =  tableNameNew.split("_");
        for(String name:tableNameArray){
            sb.append(name.substring(0,1));
        }
        return sb.toString();
    }
    /**
     * 包名处理成路径名
     * @param packageName
     * @return
     */
    public static String packageConvertPath(String packageName) {
        return String.format("/%s", packageName.contains(".") ? packageName.replaceAll("\\.", "/") : packageName);
    }

    /**
     * 获取文件名
     */
    public static String getGenerateFileName(String templateFilePath, Map<String,Object> map)
    {
        // 文件名称
        String fileName = "";
        // 包路径
        String packageName = map.get("basePackage").toString();
        // 模块名
        String moduleName = map.get("basePackage").toString();
        // 大写类名
        String className = map.get("basePackage").toString();
        // 业务名称
        String businessName = map.get("basePackage").toString();

        String javaPath = ProjectConstant.JAVA_PATH + "/" + StringUtils.replace(packageName, ".", "/");
        String mybatisPath = ProjectConstant.XML_PATH + "/" + moduleName;
        String vuePath = "vue";

        if (templateFilePath.contains("entity.ftl"))
        {
            fileName = StringUtils.format("{}/entity/{}.java", javaPath, className);
        }
        else if (templateFilePath.contains("mapper.ftl"))
        {
            fileName = StringUtils.format("{}/mapper/{}Mapper.java", javaPath, className);
        }
        else if (templateFilePath.contains("service.ftl"))
        {
            fileName = StringUtils.format("{}/service/I{}Service.java", javaPath, className);
        }
        else if (templateFilePath.contains("service-impl.ftl"))
        {
            fileName = StringUtils.format("{}/service/impl/{}ServiceImpl.java", javaPath, className);
        }
        else if (templateFilePath.contains("controller.ftl"))
        {
            fileName = StringUtils.format("{}/controller/{}Controller.java", javaPath, className);
        }
        else if (templateFilePath.contains("mapper-xml.ftl"))
        {
            fileName = StringUtils.format("{}/{}Mapper.xml", mybatisPath, className);
        }
//        else if (templateFilePath.contains("sql.vm"))
//        {
//            fileName = businessName + "Menu.sql";
//        }
//        else if (templateFilePath.contains("api.js.vm"))
//        {
//            fileName = StringUtils.format("{}/api/{}/{}.js", vuePath, moduleName, businessName);
//        }
//        else if (templateFilePath.contains("index.vue.vm"))
//        {
//            fileName = StringUtils.format("{}/views/{}/{}/index.vue", vuePath, moduleName, businessName);
//        }
//        else if (templateFilePath.contains("index-tree.vue.vm"))
//        {
//            fileName = StringUtils.format("{}/views/{}/{}/index.vue", vuePath, moduleName, businessName);
//        }
        return fileName;
    }

    public static void main(String[] args) {
        //System.out.println("args = [" + mapTableNameToVarName("login_user") + "]");
        System.out.println(getRequestMapping("fire_company"));
        System.out.println(getFirstChar("tb_mike_company"));
        //System.out.println(getFirstChar("fire_company_hazard"));
    }

}
