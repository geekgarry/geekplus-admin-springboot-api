package com.geekplus.webapp.tool.generator.mapper;

import com.geekplus.webapp.tool.generator.entity.TableColumnInfo;
import java.util.List;

/**
 * 生成表数据列表 生成表数据列表
 * Created by CodeGenerator on 2023/08/07.
 */
public interface GenTableColumnMapper {

    /**
    * 增加
    * @param tableColumnInfo
    * @return 生成表数据列表
    */
    Integer insertGenTableColumn(TableColumnInfo tableColumnInfo);

    /**
    * 批量增加
    * @param tableColumnInfoList
    * @return
    */
    public int batchInsertGenTableColumnList(List<TableColumnInfo> tableColumnInfoList);

    /**
    * 删除
    * @param columnId
    */
    Integer deleteGenTableColumnById(Integer columnId);

    /**
    * 批量删除
    */
    Integer deleteGenTableColumnByIds(Integer[] columnIds);

    /**
    * 修改
    * @param tableColumnInfo
    */
    Integer updateGenTableColumn(TableColumnInfo tableColumnInfo);

    /**
    * 批量修改魔偶几个字段
    * @param columnIds
    */
    Integer batchUpdateGenTableColumnList(Integer[] columnIds);

    /**
     *清空表格所有数据 gen_table
     */
    void cleanTable();

    /**
    * 查询全部
    */
    List<TableColumnInfo> selectGenTableColumnList(TableColumnInfo tableColumnInfo);

    /**
    * 查询全部,联合查询使用
    */
    List<TableColumnInfo> selectUnionGenTableColumnList(TableColumnInfo tableColumnInfo);

    /**
    * 根据Id查询单条数据
    */
    TableColumnInfo selectGenTableColumnById(Integer columnId);
}
