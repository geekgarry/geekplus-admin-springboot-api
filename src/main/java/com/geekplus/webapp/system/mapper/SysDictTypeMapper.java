package com.geekplus.webapp.system.mapper;

import com.geekplus.webapp.system.entity.SysDictType;
import java.util.List;

/**
 *
 * Created by CodeGenerator on 2023/06/18.
 */

public interface SysDictTypeMapper {

    /**
    * 增加
    * @param sysDictType
    * @return
    */
    Integer insertSysDictType(SysDictType sysDictType);

    /**
    * 批量增加
    * @param sysDictTypeList
    * @return
    */
    public int batchInsertSysDictTypeList(List<SysDictType> sysDictTypeList);

    /**
    * 删除
    * @param dictId
    */
    Integer deleteSysDictTypeById(Long dictId);

    /**
    * 批量删除
    */
    Integer deleteSysDictTypeByIds(Long[] dictIds);

    /**
    * 修改
    * @param sysDictType
    */
    Integer updateSysDictType(SysDictType sysDictType);

    /**
    * 批量修改魔偶几个字段
    * @param dictIds
    */
    Integer batchUpdateSysDictTypeList(Long[] dictIds);

    /**
    * 查询全部
    */
    List<SysDictType> selectSysDictTypeList(SysDictType sysDictType);

    /**
    * 查询全部,联合查询使用
    */
    List<SysDictType> selectUnionSysDictTypeList(SysDictType sysDictType);

    /**
    * 根据Id查询单条数据
    */
    SysDictType selectSysDictTypeById(Long dictId);
}
