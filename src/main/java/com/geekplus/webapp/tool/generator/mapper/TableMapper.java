package com.geekplus.webapp.tool.generator.mapper;

import com.geekplus.webapp.tool.generator.entity.TableColumnInfo;
import com.geekplus.webapp.tool.generator.entity.TableInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @ClassName: TableDao
 * @Description:
 * @author: geekcjj
 * @date: 2018年7月26日 上午11:07:41
 */
public interface TableMapper {

	//@Select("select TABLE_NAME,table_comment, create_time, update_time from information_schema.TABLES where TABLE_SCHEMA=(select database())")
	/**
	  * @Author geekplus
	  * @Description //查询所有数据表
	  */
	List<String> listTableName();

	/**
	 * @Author geekplus
	 * @Description //查询所有数据表,非系统表等
	 */
	List<TableInfo> listTable();

	/**
	 * @Author geekplus
	 * @Description //查询所有数据表
	 */
	List<TableInfo> listAllTable();
	/**
	 * @Author geekplus
	 * @Description //根据表明查询数据表信息
	 */
	TableInfo selectTableByName(@Param(value = "tableName") String tableName);
	/**
	 * @Author geekplus
	 * @Description //根据表明查询所有数据表
	 */
	List<TableInfo> listTableByNames(@Param(value = "tableName") String[] tableName);
	/**
	 * @Author geekplus
	 * @Description //根据表明查询所有数据表的列
	 */
	List<TableColumnInfo> listTableColumn(@Param(value = "tableName") String tableName);
	/**
	 * @Author geekplus
	 * @Description //根据表明查询所有数据表的主键列
	 */
	List<TableColumnInfo> selectTablePkColumnList(@Param(value = "tableName") String tableName);
	/**
	 * @Author geekplus
	 * @Description //根据表明查询数据表的主键列
	 */
	TableColumnInfo selectTableOnePkColumn(@Param(value = "tableName") String tableName);

	/**
	 * 增加
	 * @param tableInfo
	 * @return 代码生成业务表
	 */
	Integer insertGenTable(TableInfo tableInfo);

	/**
	 * 批量增加
	 * @param tableInfoList
	 * @return
	 */
	public int batchInsertGenTableList(List<TableInfo> tableInfoList);

	/**
	 * 删除
	 * @param tableId
	 */
	Integer deleteGenTableById(Long tableId);

	/**
	 * 批量删除
	 */
	Integer deleteGenTableByIds(Long[] tableIds);

	/**
	 * 修改
	 * @param tableInfo
	 */
	Integer updateGenTable(TableInfo tableInfo);

	/**
	 * 批量修改魔偶几个字段
	 * @param tableIds
	 */
	Integer batchUpdateGenTableList(Long[] tableIds);

	/**
	 *清空表格所有数据 gen_table
	 */
	void cleanTable();

	/**
	 * 查询全部
	 */
	List<TableInfo> selectGenTableList(TableInfo tableInfo);

	/**
	 * 查询全部,联合查询使用
	 */
	List<TableInfo> selectUnionGenTableList(TableInfo tableInfo);

	/**
	 * 根据Id查询单条数据
	 */
	TableInfo selectGenTableById(Long tableId);

	/**
	 * 根据Id查询单条数据
	 */
	List<TableInfo> selectGenTableByIds(Long[] tableId);
}
