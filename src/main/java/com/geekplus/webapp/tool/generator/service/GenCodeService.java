/**
 * author     : geekplus
 * email      : geekcjj@gmail.com
 * date       : 7/17/23 02:41
 * description: 做什么的？
 */
package com.geekplus.webapp.tool.generator.service;

import com.geekplus.webapp.tool.generator.entity.TableColumnInfo;
import com.geekplus.webapp.tool.generator.entity.TableInfo;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

public interface GenCodeService {

    /**
      * @Author geekplus
      * @Description //获取非系统核心数据表
      * @Param
      * @Throws
      * @Return {@link }
      */
    List<TableInfo> getAllTable();

    /**
     * @Author geekplus
     * @Description //获取数据库所有数据表
     * @Param
     * @Throws
     * @Return {@link }
     */
    List<TableInfo> getAllListTable();

    /**
     * 根据表名查询数据表信息
     */
    TableInfo getTableInfoByName(String tableName);

    /**
     * @Author geekplus
     * @Description //根据表名查询主键
     * @Param
     * @Return {@link }
     */
    List<TableColumnInfo> getTablePkColumnList(String tableName);

    /**
     * @Author geekplus
     * @Description //根据表名查询所有列
     * @Param
     * @Return {@link }
     */
    List<TableColumnInfo> getTableColumnList(String tableName);

    /**
     * 增加
     * @param tableInfo
     * @return 代码生成业务表
     */
    public Integer insertGenTable(TableInfo tableInfo);

    /**
     * 批量增加
     * @param tableInfoList
     * @return 代码生成业务表
     */
    public Integer batchInsertGenTableList(List<TableInfo> tableInfoList);

    /**
     * 删除
     * @param tableId
     */
    public Integer deleteGenTableById(Long tableId);

    /**
     * 批量删除某几个字段
     */
    public Integer deleteGenTableByIds(Long[] tableIds);

    /**
     * 修改
     * @param tableInfo
     */
    public Integer updateGenTable(TableInfo tableInfo);

    /**
     * 批量修改
     * @param tableIds
     */
    public Integer batchUpdateGenTableList(Long[] tableIds);

    /**
     * 查询全部
     */
    public List<TableInfo> selectGenTableList(TableInfo tableInfo);

    /**
     * 查询全部，用作联合查询使用(在基础上修改即可)
     */
    public List<TableInfo> selectUnionGenTableList(TableInfo tableInfo);

    /**
     * 根据Id查询单条数据
     */
    public TableInfo selectGenTableById(Long tableId);

    /**
     * 根据Id查询单条数据
     */
    public List<TableInfo> selectGenTableByIds(Long[] tableIds);

    /**
      * @Author geekplus
      * @Description 下载代码，直接通过表名查询数据库中表的信息，information_schema
      * @Return {@link }
      */
    public List<Map<String,byte[]>> downloadCode(String tableName);

    /**
     * @Author geekplus
     * @Description 下载代码,通过GneTable表格信息
     * @Return {@link }
     */
    public List<Map<String,byte[]>> downloadCodeByTable(TableInfo tableInfo);

    /**
     * @Author geekplus
     * @Description 下载代码,通过GneTable表格信息
     * @Return {@link }
     */
    public List<Map<String,byte[]>> downloadCodeByTable(List<TableInfo> tableInfoList);

    /**
     *清空表格所有数据 gen_table
     */
    public void cleanTable();
}
