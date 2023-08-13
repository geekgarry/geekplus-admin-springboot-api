package com.geekplus.webapp.system.service.impl;

import com.geekplus.webapp.system.mapper.SysDictDataMapper;
import com.geekplus.webapp.system.entity.SysDictData;
import com.geekplus.webapp.system.service.SysDictDataService;
//import com.geekplus.core.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

import javax.annotation.Resource;


/**
 * Created by CodeGenerator on 2023/06/18.
 */
@Service
@Transactional
public class SysDictDataServiceImpl implements SysDictDataService {
    @Resource
    private SysDictDataMapper sysDictDataMapper;

    /**
    * 增加
    * @param sysDictData
    * @return
    */
    public Integer insertSysDictData(SysDictData sysDictData){
        return sysDictDataMapper.insertSysDictData(sysDictData);
    }

    /**
    * 批量增加
    * @param sysDictDataList
    * @return
    */
    public Integer batchInsertSysDictDataList(List<SysDictData> sysDictDataList){
        return sysDictDataMapper.batchInsertSysDictDataList(sysDictDataList);
    }

    /**
    * 删除
    * @param dictCode
    */
    public Integer deleteSysDictDataById(Long dictCode){
        return sysDictDataMapper.deleteSysDictDataById(dictCode);
    }

    /**
    * 批量删除
    */
    public Integer deleteSysDictDataByIds(Long[] dictCodes){
        return sysDictDataMapper.deleteSysDictDataByIds(dictCodes);
    }

    /**
    * 修改
    * @param sysDictData
    */
    public Integer updateSysDictData(SysDictData sysDictData){
        return sysDictDataMapper.updateSysDictData(sysDictData);
    }

    /**
    * 批量修改某几个字段
    * @param dictCodes
    */
    public Integer batchUpdateSysDictDataList(Long[] dictCodes){
        return sysDictDataMapper.batchUpdateSysDictDataList(dictCodes);
    }

    /**
    * 查询全部
    */
    public List<SysDictData> selectSysDictDataList(SysDictData sysDictData){
        return sysDictDataMapper.selectSysDictDataList(sysDictData);
    }

    /**
    * 查询全部,用于联合查询，在此基础做自己的定制改动
    */
    public List<SysDictData> selectUnionSysDictDataList(SysDictData sysDictData){
        return sysDictDataMapper.selectUnionSysDictDataList(sysDictData);
    }

    /**
    * 根据Id查询单条数据
    */
    public SysDictData selectSysDictDataById(Long dictCode){
        return sysDictDataMapper.selectSysDictDataById(dictCode);
    }
}
