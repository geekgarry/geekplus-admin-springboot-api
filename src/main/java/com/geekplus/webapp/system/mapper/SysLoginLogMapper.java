package com.geekplus.webapp.system.mapper;

import com.geekplus.webapp.system.entity.SysLoginLog;
import java.util.List;

/**
 * 系统登录日志 系统登录日志
 * Created by CodeGenerator on 2023/06/18.
 */

public interface SysLoginLogMapper {

    /**
    * 增加
    * @param sysLoginLog
    * @return 系统登录日志
    */
    Integer insertSysLoginLog(SysLoginLog sysLoginLog);

    /**
    * 批量增加
    * @param sysLoginLogList
    * @return
    */
    public int batchInsertSysLoginLogList(List<SysLoginLog> sysLoginLogList);

    /**
    * 删除
    * @param logId
    */
    Integer deleteSysLoginLogById(Long logId);

    /**
    * 批量删除
    */
    Integer deleteSysLoginLogByIds(Long[] logIds);

    /**
    * 修改
    * @param sysLoginLog
    */
    Integer updateSysLoginLog(SysLoginLog sysLoginLog);

    /**
    * 批量修改魔偶几个字段
    * @param logIds
    */
    Integer batchUpdateSysLoginLogList(Long[] logIds);

    /**
    * 查询全部
    */
    List<SysLoginLog> selectSysLoginLogList(SysLoginLog sysLoginLog);

    /**
    * 查询全部,联合查询使用
    */
    List<SysLoginLog> selectUnionSysLoginLogList(SysLoginLog sysLoginLog);

    /**
    * 根据Id查询单条数据
    */
    SysLoginLog selectSysLoginLogById(Long logId);

    /**
     *清空表格所有数据 SysLoginLog
     */
    void cleanTable();
}
