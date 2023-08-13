package com.geekplus.webapp.tool.generator.service.impl;

import com.geekplus.webapp.tool.generator.mapper.GenTableColumnMapper;
import com.geekplus.webapp.tool.generator.entity.TableColumnInfo;
import com.geekplus.webapp.tool.generator.service.GenTableColumnService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

import javax.annotation.Resource;


/**
 * Created by CodeGenerator on 2023/08/07.
 */
@Service
@Transactional
public class GenTableColumnServiceImpl implements GenTableColumnService {
    @Resource
    private GenTableColumnMapper genTableColumnMapper;

    /**
    * 增加
    * @param tableColumnInfo
    * @return 生成表数据列表
    */
    public Integer insertGenTableColumn(TableColumnInfo tableColumnInfo){
        return genTableColumnMapper.insertGenTableColumn(tableColumnInfo);
    }

    /**
    * 批量增加
    * @param tableColumnInfoList
    * @return 生成表数据列表
    */
    public Integer batchInsertGenTableColumnList(List<TableColumnInfo> tableColumnInfoList){
        return genTableColumnMapper.batchInsertGenTableColumnList(tableColumnInfoList);
    }

    /**
    * 删除
    * @param columnId
    */
    public Integer deleteGenTableColumnById(Integer columnId){
        return genTableColumnMapper.deleteGenTableColumnById(columnId);
    }

    /**
    * 批量删除
    */
    public Integer deleteGenTableColumnByIds(Integer[] columnIds){
        return genTableColumnMapper.deleteGenTableColumnByIds(columnIds);
    }

    /**
    * 修改
    * @param tableColumnInfo
    */
    public Integer updateGenTableColumn(TableColumnInfo tableColumnInfo){
        return genTableColumnMapper.updateGenTableColumn(tableColumnInfo);
    }

    /**
    * 批量修改某几个字段
    * @param columnIds
    */
    public Integer batchUpdateGenTableColumnList(Integer[] columnIds){
        return genTableColumnMapper.batchUpdateGenTableColumnList(columnIds);
    }

    /**
    * 查询全部
    */
    public List<TableColumnInfo> selectGenTableColumnList(TableColumnInfo tableColumnInfo){
        return genTableColumnMapper.selectGenTableColumnList(tableColumnInfo);
    }

    /**
    * 查询全部,用于联合查询，在此基础做自己的定制改动
    */
    public List<TableColumnInfo> selectUnionGenTableColumnList(TableColumnInfo tableColumnInfo){
        return genTableColumnMapper.selectUnionGenTableColumnList(tableColumnInfo);
    }

    /**
    * 根据Id查询单条数据
    */
    public TableColumnInfo selectGenTableColumnById(Integer columnId){
        return genTableColumnMapper.selectGenTableColumnById(columnId);
    }

    /**
     *清空表格所有数据 gen_table
     */
    @Override
    public void cleanTable() {
        genTableColumnMapper.cleanTable();
    }
}
