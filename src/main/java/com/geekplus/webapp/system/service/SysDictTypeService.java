package com.geekplus.webapp.system.service;

import com.geekplus.webapp.system.entity.SysDictType;
//import com.geekplus.core.Service;
import java.util.List;


/**
 *
 * Created by CodeGenerator on 2023/06/18.
 */
public interface SysDictTypeService {

    /**
    * 增加
    * @param sysDictType
    * @return
    */
    public Integer insertSysDictType(SysDictType sysDictType);

    /**
    * 批量增加
    * @param sysDictTypeList
    * @return
    */
    public Integer batchInsertSysDictTypeList(List<SysDictType> sysDictTypeList);

    /**
    * 删除
    * @param dictId
    */
    public Integer deleteSysDictTypeById(Long dictId);

    /**
    * 批量删除某几个字段
    */
    public Integer deleteSysDictTypeByIds(Long[] dictIds);

    /**
    * 修改
    * @param sysDictType
    */
    public Integer updateSysDictType(SysDictType sysDictType);

    /**
    * 批量修改
    * @param dictIds
    */
    public Integer batchUpdateSysDictTypeList(Long[] dictIds);

    /**
    * 查询全部
    */
    public List<SysDictType> selectSysDictTypeList(SysDictType sysDictType);

    /**
    * 查询全部，用作联合查询使用(在基础上修改即可)
    */
    public List<SysDictType> selectUnionSysDictTypeList(SysDictType sysDictType);

    /**
    * 根据Id查询单条数据
    */
    public SysDictType selectSysDictTypeById(Long dictId);
}
