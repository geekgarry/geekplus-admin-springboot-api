<?xml version="1.0" encoding="UTF-8"?>

<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">

<mapper namespace="${basePackage}.webapp.${moduleName}.mapper.${modelNameUpperCamel}Mapper">
    <resultMap id="BaseResultMap" type="${basePackage}.webapp.${moduleName}.entity.${modelNameUpperCamel}">
        <#list allColumn as column>
            <result property="${column.smallColumnName}" column="${column.columnName}" jdbcType="${column.dictType}" />
        <#--jdbcType="${column.dictType}"-->
        </#list>
    </resultMap>

    <!--${functionName}-->
    <!--基础查询语句-->
    <sql id="baseSelectVo">
        select <#if allColumn?exists>
            <#list allColumn as column>${column.columnName}<#if allColumnCount != column.sort>,</#if></#list>
        </#if> from ${tableName}
    </sql>

    <!--基础查询语句2,用作联合查询使用-->
    <sql id="select${modelNameUpperCamel}Vo">
        select <#if allColumn?exists>
            <#list allColumn as column>${tableAlias}.${column.columnName}<#if allColumnCount != column.sort>,</#if></#list>
        </#if> from ${tableName} ${tableAlias}
    </sql>

    <!--数据查询操作SQL(非联合查询)-->
    <select id="select${modelNameUpperCamel}List" parameterType="${modelNameUpperCamel}" resultMap="BaseResultMap">
        <include refid="baseSelectVo"/>
        <where>
            <#if allColumn?exists>
                <#list allColumn as column>
                    <if test="${column.smallColumnName?uncap_first} !=null <#if column.javaType == 'String'> and ${column.smallColumnName?uncap_first} != ''</#if>">
                        AND ${column.columnName} = ${r'#'}{${column.smallColumnName?uncap_first},jdbcType=${column.dictType}}
                    </if>
                </#list>
            </#if>
        </where>
    </select>

    <!--数据联合查询操作SQL(联合查询) javaType-->
    <select id="selectUnion${modelNameUpperCamel}List" parameterType="${modelNameUpperCamel}" resultMap="BaseResultMap">
        <include refid="select${modelNameUpperCamel}Vo"/>
        <where>
            <#if allColumn?exists>
                <#list allColumn as column>
                    <if test="${column.smallColumnName?uncap_first} !=null <#if column.javaType == 'String'> and ${column.smallColumnName?uncap_first} != ''</#if>">
                        AND ${tableAlias}.${column.columnName} = ${r'#'}{${column.smallColumnName?uncap_first},jdbcType=${column.dictType}}
                    </if>
                </#list>
            </#if>
        </where>
    </select>

    <!--单条数据或详情查询操作SQL-->
    <select id="select${modelNameUpperCamel}ById" parameterType="${pkColumn.javaType}" resultMap="BaseResultMap">
        <include refid="baseSelectVo"/>
        where
        <#list allColumn as column>
            <#if column.isPk=='1'>
                ${column.columnName} = ${r'#'}{${column.smallColumnName}}
            </#if>
        </#list>
    </select>

    <!--添加操作SQL-->
    <insert id="insert${modelNameUpperCamel}" parameterType="${modelNameUpperCamel}" <#if pkColumn.isIncrement=='1'> useGeneratedKeys="true" keyProperty="${pkColumn.smallColumnName}"</#if>>
        insert into ${tableName}
        <trim prefix="(" suffix=")" suffixOverrides=",">
            <#if pkColumn.isIncrement=='1'>
                <#list allColumn as column>
                    <#if column.columnName!=pkColumn.columnName>
                        <if test="${column.smallColumnName} != null<#if column.javaType == 'String'> and ${column.smallColumnName} != ''</#if>">${column.columnName},</if>
                    </#if>
                </#list>
            <#else>
                <#list allColumn as column>
                    <if test="${column.smallColumnName} != null<#if column.javaType == 'String'> and ${column.smallColumnName} != ''</#if>">${column.columnName},</if>
                </#list>
            </#if>
        </trim>
        <trim prefix="values (" suffix=")" suffixOverrides=",">
            <#if pkColumn.isIncrement=='1'>
                <#list allColumn as column>
                    <#if column.columnName!=pkColumn.columnName>
                    <if test="${column.smallColumnName} != null<#if column.javaType == 'String'> and ${column.smallColumnName} != ''</#if>">${r'#'}{${column.smallColumnName}},</if>
                    </#if>
                </#list>
            <#else>
                <#list allColumn as column>
                    <if test="${column.smallColumnName} != null<#if column.javaType == 'String'> and ${column.smallColumnName} != ''</#if>">${r'#'}{${column.smallColumnName}},</if>
                </#list>
            </#if>
        </trim>
    </insert>

    <!--批量添加操作SQL-->
    <insert id="batchInsert${modelNameUpperCamel}List" parameterType="java.util.List" <#if pkColumn.isIncrement=='1'> useGeneratedKeys="true" keyProperty="${pkColumn.smallColumnName}"</#if>>
        insert into ${tableName}
        (<#if pkColumn.isIncrement=='1'>
        <#list allColumn as column><#if column.columnName!=pkColumn.columnName>${column.columnName}<#if allColumnCount != column.sort>,</#if></#if></#list>
    <#else>
        <#list allColumn as column>${column.columnName}<#if allColumnCount != column.sort>,</#if></#list>
        </#if>
        )
        values
        <foreach collection="list" item="item" index="index" separator=",">
            (<#if pkColumn.isIncrement=='1'>
            <#list allColumn as column><#if column.columnName!=pkColumn.columnName>${r'#'}{${r'item.'}${column.smallColumnName}}<#if allColumnCount != column.sort>,</#if></#if></#list>
        <#else>
            <#list allColumn as column>${r'#'}{${r'item.'}${column.smallColumnName}}<#if allColumnCount != column.sort>,</#if></#list>
            </#if>
            )
        </foreach>
    </insert>

    <!--删除操作SQL-->
    <delete id="delete${modelNameUpperCamel}ById" parameterType="${pkColumn.javaType}">
        delete FROM ${tableName} where ${pkColumn.columnName} = ${r'#'}{${pkColumn.smallColumnName}}
    </delete>

    <!--逻辑删除-->
    <delete id="delete${modelNameUpperCamel}ById2" parameterType="${pkColumn.javaType}">
        update ${tableName} set del_flag='2' where ${pkColumn.columnName} = ${r'#'}{${pkColumn.smallColumnName}}
    </delete>

    <!--删除操作SQL-->
    <delete id="delete${modelNameUpperCamel}ByIds" parameterType="${pkColumn.javaType}">
        delete FROM ${tableName} where ${pkColumn.columnName} in
        <foreach item="${pkColumn.smallColumnName}" collection="array" open="(" separator="," close=")">
            ${r'#'}{${pkColumn.smallColumnName}}
        </foreach>
    </delete>

    <!--逻辑批量删除-->
    <delete id="delete${modelNameUpperCamel}ByIds2" parameterType="${pkColumn.javaType}">
        update ${tableName} set del_flag='2' where ${pkColumn.columnName} in
        <foreach item="${pkColumn.smallColumnName}" collection="array" open="(" separator="," close=")">
            ${r'#'}{${pkColumn.smallColumnName}}
        </foreach>
    </delete>

    <!--更新操作SQL-->
    <update id="update${modelNameUpperCamel}" parameterType="${modelNameUpperCamel}">
        update ${tableName}
        <trim prefix="SET" suffixOverrides=",">
            <#list allColumn as column>
                <#if column.isPk !='1'>
                    <if test="${column.smallColumnName} != null <#if column.javaType == 'String' > and ${column.smallColumnName} != ''</#if>">${column.columnName} = ${r'#'}{${column.smallColumnName}},</if>
                </#if>
            </#list>
        </trim>
        where ${pkColumn.columnName} = ${r'#'}{${pkColumn.smallColumnName}}
    </update>

    <!--批量更新某个字段-->
    <update id="batchUpdate${modelNameUpperCamel}List" >
        update ${tableName} set
        <#if allColumn?exists>
            <#list allColumn as column>${column.columnName}=''<#if allColumnCount != column.sort>,</#if></#list>
        </#if>
        where ${pkColumn.columnName} in
        <foreach collection="array" item="item"  open="(" close=")" separator=",">
            ${r'#'}{item}
        </foreach>
    </update>
</mapper>
