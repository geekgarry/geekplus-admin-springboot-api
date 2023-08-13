package com.geekplus.webapp.system.service;

import com.geekplus.webapp.system.entity.SysDictData;
//import com.geekplus.core.Service;
import java.util.List;


/**
 *
 * Created by CodeGenerator on 2023/06/18.
 */
public interface SysDictDataService {

    /**
    * 增加
    * @param sysDictData
    * @return
    */
    public Integer insertSysDictData(SysDictData sysDictData);

    /**
    * 批量增加
    * @param sysDictDataList
    * @return
    */
    public Integer batchInsertSysDictDataList(List<SysDictData> sysDictDataList);

    /**
    * 删除
    * @param dictCode
    */
    public Integer deleteSysDictDataById(Long dictCode);

    /**
    * 批量删除某几个字段
    */
    public Integer deleteSysDictDataByIds(Long[] dictCodes);

    /**
    * 修改
    * @param sysDictData
    */
    public Integer updateSysDictData(SysDictData sysDictData);

    /**
    * 批量修改
    * @param dictCodes
    */
    public Integer batchUpdateSysDictDataList(Long[] dictCodes);

    /**
    * 查询全部
    */
    public List<SysDictData> selectSysDictDataList(SysDictData sysDictData);

    /**
    * 查询全部，用作联合查询使用(在基础上修改即可)
    */
    public List<SysDictData> selectUnionSysDictDataList(SysDictData sysDictData);

    /**
    * 根据Id查询单条数据
    */
    public SysDictData selectSysDictDataById(Long dictCode);
}
