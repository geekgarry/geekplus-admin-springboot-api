package com.geekplus.webapp.tool.generator.service;

import com.geekplus.webapp.tool.generator.entity.TableColumnInfo;
//import com.geekplus.core.Service;
import java.util.List;


/**
 * 生成表数据列表 生成表数据列表
 * Created by CodeGenerator on 2023/08/07.
 */
public interface GenTableColumnService {

    /**
    * 增加
    * @param genTableColumn
    * @return 生成表数据列表
    */
    public Integer insertGenTableColumn(TableColumnInfo genTableColumn);

    /**
    * 批量增加
    * @param genTableColumnList
    * @return 生成表数据列表
    */
    public Integer batchInsertGenTableColumnList(List<TableColumnInfo> genTableColumnList);

    /**
    * 删除
    * @param columnId
    */
    public Integer deleteGenTableColumnById(Integer columnId);

    /**
    * 批量删除某几个字段
    */
    public Integer deleteGenTableColumnByIds(Integer[] columnIds);

    /**
    * 修改
    * @param genTableColumn
    */
    public Integer updateGenTableColumn(TableColumnInfo genTableColumn);

    /**
    * 批量修改
    * @param columnIds
    */
    public Integer batchUpdateGenTableColumnList(Integer[] columnIds);

    /**
    * 查询全部
    */
    public List<TableColumnInfo> selectGenTableColumnList(TableColumnInfo genTableColumn);

    /**
    * 查询全部，用作联合查询使用(在基础上修改即可)
    */
    public List<TableColumnInfo> selectUnionGenTableColumnList(TableColumnInfo genTableColumn);

    /**
    * 根据Id查询单条数据
    */
    public TableColumnInfo selectGenTableColumnById(Integer columnId);

    /**
     *清空表格所有数据 gen_table
     */
    public void cleanTable();
}
