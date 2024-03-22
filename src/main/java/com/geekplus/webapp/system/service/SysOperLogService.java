package com.geekplus.webapp.system.service;

import com.geekplus.webapp.system.entity.SysOperLog;
import org.apache.ibatis.annotations.MapKey;
//import com.geekplus.core.Service;
import java.io.IOException;
import java.util.List;
import java.util.Map;


/**
 * 系统操作日志 系统操作日志
 * Created by CodeGenerator on 2023/06/18.
 */
public interface SysOperLogService {

    /**
    * 增加
    * @param sysOperLog
    * @return 系统操作日志
    */
    public Integer insertSysOperLog(SysOperLog sysOperLog);

    /**
    * 批量增加
    * @param sysOperLogList
    * @return 系统操作日志
    */
    public Integer batchInsertSysOperLogList(List<SysOperLog> sysOperLogList);

    /**
    * 删除
    * @param operId
    */
    public Integer deleteSysOperLogById(Long operId);

    /**
    * 批量删除某几个字段
    */
    public Integer deleteSysOperLogByIds(Long[] operIds);

    /**
    * 修改
    * @param sysOperLog
    */
    public Integer updateSysOperLog(SysOperLog sysOperLog);

    /**
    * 批量修改
    * @param operIds
    */
    public Integer batchUpdateSysOperLogList(Long[] operIds);

    /**
    * 查询全部
    */
    public List<SysOperLog> selectSysOperLogList(SysOperLog sysOperLog);

    /**
    * 查询全部，用作联合查询使用(在基础上修改即可)
    */
    public List<SysOperLog> selectUnionSysOperLogList(SysOperLog sysOperLog);

    /**
    * 根据Id查询单条数据
    */
    public SysOperLog selectSysOperLogById(Long operId);

    /**
     * 查询访问地址的统计数量
     */
    List<Map<String,Object>> selectWebVisitorCount();

    /**
     *清空表格所有数据 OperLog
     */
    public void cleanTable();
}
