package com.geekplus.webapp.system.service;

import com.geekplus.webapp.system.entity.SysLoginLog;
//import com.geekplus.core.Service;
import java.util.List;


/**
 * 系统登录日志 系统登录日志
 * Created by CodeGenerator on 2023/06/18.
 */
public interface SysLoginLogService {

    /**
    * 增加
    * @param sysLoginLog
    * @return 系统登录日志
    */
    public Integer insertSysLoginLog(SysLoginLog sysLoginLog);

    /**
    * 批量增加
    * @param sysLoginLogList
    * @return 系统登录日志
    */
    public Integer batchInsertSysLoginLogList(List<SysLoginLog> sysLoginLogList);

    /**
    * 删除
    * @param logId
    */
    public Integer deleteSysLoginLogById(Long logId);

    /**
    * 批量删除某几个字段
    */
    public Integer deleteSysLoginLogByIds(Long[] logIds);

    /**
    * 修改
    * @param sysLoginLog
    */
    public Integer updateSysLoginLog(SysLoginLog sysLoginLog);

    /**
    * 批量修改
    * @param logIds
    */
    public Integer batchUpdateSysLoginLogList(Long[] logIds);

    /**
    * 查询全部
    */
    public List<SysLoginLog> selectSysLoginLogList(SysLoginLog sysLoginLog);

    /**
    * 查询全部，用作联合查询使用(在基础上修改即可)
    */
    public List<SysLoginLog> selectUnionSysLoginLogList(SysLoginLog sysLoginLog);

    /**
    * 根据Id查询单条数据
    */
    public SysLoginLog selectSysLoginLogById(Long logId);

    /**
     *清空表格所有数据 SysLoginLog
     */
    public void cleanTable();
}
