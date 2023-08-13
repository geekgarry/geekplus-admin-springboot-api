package com.geekplus.webapp.system.service.impl;

import com.geekplus.webapp.system.mapper.SysDictTypeMapper;
import com.geekplus.webapp.system.entity.SysDictType;
import com.geekplus.webapp.system.service.SysDictTypeService;
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
public class SysDictTypeServiceImpl implements SysDictTypeService {
    @Resource
    private SysDictTypeMapper sysDictTypeMapper;

    /**
    * 增加
    * @param sysDictType
    * @return
    */
    public Integer insertSysDictType(SysDictType sysDictType){
        return sysDictTypeMapper.insertSysDictType(sysDictType);
    }

    /**
    * 批量增加
    * @param sysDictTypeList
    * @return
    */
    public Integer batchInsertSysDictTypeList(List<SysDictType> sysDictTypeList){
        return sysDictTypeMapper.batchInsertSysDictTypeList(sysDictTypeList);
    }

    /**
    * 删除
    * @param dictId
    */
    public Integer deleteSysDictTypeById(Long dictId){
        return sysDictTypeMapper.deleteSysDictTypeById(dictId);
    }

    /**
    * 批量删除
    */
    public Integer deleteSysDictTypeByIds(Long[] dictIds){
        return sysDictTypeMapper.deleteSysDictTypeByIds(dictIds);
    }

    /**
    * 修改
    * @param sysDictType
    */
    public Integer updateSysDictType(SysDictType sysDictType){
        return sysDictTypeMapper.updateSysDictType(sysDictType);
    }

    /**
    * 批量修改某几个字段
    * @param dictIds
    */
    public Integer batchUpdateSysDictTypeList(Long[] dictIds){
        return sysDictTypeMapper.batchUpdateSysDictTypeList(dictIds);
    }

    /**
    * 查询全部
    */
    public List<SysDictType> selectSysDictTypeList(SysDictType sysDictType){
        return sysDictTypeMapper.selectSysDictTypeList(sysDictType);
    }

    /**
    * 查询全部,用于联合查询，在此基础做自己的定制改动
    */
    public List<SysDictType> selectUnionSysDictTypeList(SysDictType sysDictType){
        return sysDictTypeMapper.selectUnionSysDictTypeList(sysDictType);
    }

    /**
    * 根据Id查询单条数据
    */
    public SysDictType selectSysDictTypeById(Long dictId){
        return sysDictTypeMapper.selectSysDictTypeById(dictId);
    }
}
