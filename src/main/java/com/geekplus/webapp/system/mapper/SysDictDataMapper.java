package com.geekplus.webapp.system.mapper;

import com.geekplus.webapp.system.entity.SysDictData;
import java.util.List;

/**
 *
 * Created by CodeGenerator on 2023/06/18.
 */

public interface SysDictDataMapper {

    /**
    * 增加
    * @param sysDictData
    * @return
    */
    Integer insertSysDictData(SysDictData sysDictData);

    /**
    * 批量增加
    * @param sysDictDataList
    * @return
    */
    public int batchInsertSysDictDataList(List<SysDictData> sysDictDataList);

    /**
    * 删除
    * @param dictCode
    */
    Integer deleteSysDictDataById(Long dictCode);

    /**
    * 批量删除
    */
    Integer deleteSysDictDataByIds(Long[] dictCodes);

    /**
    * 修改
    * @param sysDictData
    */
    Integer updateSysDictData(SysDictData sysDictData);

    /**
    * 批量修改魔偶几个字段
    * @param dictCodes
    */
    Integer batchUpdateSysDictDataList(Long[] dictCodes);

    /**
    * 查询全部
    */
    List<SysDictData> selectSysDictDataList(SysDictData sysDictData);

    /**
    * 查询全部,联合查询使用
    */
    List<SysDictData> selectUnionSysDictDataList(SysDictData sysDictData);

    /**
    * 根据Id查询单条数据
    */
    SysDictData selectSysDictDataById(Long dictCode);
}
