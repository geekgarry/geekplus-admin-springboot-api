package com.geekplus.webapp.system.mapper;

import com.geekplus.webapp.system.entity.SysConfig;
import java.util.List;

/**
 *
 * Created by CodeGenerator on 2023/06/18.
 */

public interface SysConfigMapper {

    /**
    * 增加
    * @param sysConfig
    * @return
    */
    Integer insertSysConfig(SysConfig sysConfig);

    /**
    * 批量增加
    * @param sysConfigList
    * @return
    */
    public int batchInsertSysConfigList(List<SysConfig> sysConfigList);

    /**
    * 删除
    * @param configId
    */
    Integer deleteSysConfigById(Integer configId);

    /**
    * 批量删除
    */
    Integer deleteSysConfigByIds(Integer[] configIds);

    /**
    * 修改
    * @param sysConfig
    */
    Integer updateSysConfig(SysConfig sysConfig);

    /**
    * 批量修改魔偶几个字段
    * @param configIds
    */
    Integer batchUpdateSysConfigList(Integer[] configIds);

    /**
    * 查询全部
    */
    List<SysConfig> selectSysConfigList(SysConfig sysConfig);

    /**
    * 查询全部,联合查询使用
    */
    List<SysConfig> selectUnionSysConfigList(SysConfig sysConfig);

    /**
    * 根据Id查询单条数据
    */
    SysConfig selectSysConfigById(Integer configId);
}
