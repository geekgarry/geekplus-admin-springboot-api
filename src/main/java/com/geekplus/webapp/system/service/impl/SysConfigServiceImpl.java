package com.geekplus.webapp.system.service.impl;

import com.geekplus.webapp.system.mapper.SysConfigMapper;
import com.geekplus.webapp.system.entity.SysConfig;
import com.geekplus.webapp.system.service.SysConfigService;
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
public class SysConfigServiceImpl implements SysConfigService {
    @Resource
    private SysConfigMapper sysConfigMapper;

    /**
    * 增加
    * @param sysConfig
    * @return
    */
    public Integer insertSysConfig(SysConfig sysConfig){
        return sysConfigMapper.insertSysConfig(sysConfig);
    }

    /**
    * 批量增加
    * @param sysConfigList
    * @return
    */
    public Integer batchInsertSysConfigList(List<SysConfig> sysConfigList){
        return sysConfigMapper.batchInsertSysConfigList(sysConfigList);
    }

    /**
    * 删除
    * @param configId
    */
    public Integer deleteSysConfigById(Integer configId){
        return sysConfigMapper.deleteSysConfigById(configId);
    }

    /**
    * 批量删除
    */
    public Integer deleteSysConfigByIds(Integer[] configIds){
        return sysConfigMapper.deleteSysConfigByIds(configIds);
    }

    /**
    * 修改
    * @param sysConfig
    */
    public Integer updateSysConfig(SysConfig sysConfig){
        return sysConfigMapper.updateSysConfig(sysConfig);
    }

    /**
    * 批量修改某几个字段
    * @param configIds
    */
    public Integer batchUpdateSysConfigList(Integer[] configIds){
        return sysConfigMapper.batchUpdateSysConfigList(configIds);
    }

    /**
    * 查询全部
    */
    public List<SysConfig> selectSysConfigList(SysConfig sysConfig){
        return sysConfigMapper.selectSysConfigList(sysConfig);
    }

    /**
    * 查询全部,用于联合查询，在此基础做自己的定制改动
    */
    public List<SysConfig> selectUnionSysConfigList(SysConfig sysConfig){
        return sysConfigMapper.selectUnionSysConfigList(sysConfig);
    }

    /**
    * 根据Id查询单条数据
    */
    public SysConfig selectSysConfigById(Integer configId){
        return sysConfigMapper.selectSysConfigById(configId);
    }
}
