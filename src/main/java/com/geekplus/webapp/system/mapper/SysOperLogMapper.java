package com.geekplus.webapp.system.mapper;

import com.geekplus.webapp.system.entity.SysOperLog;
import org.apache.ibatis.annotations.MapKey;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 系统操作日志 系统操作日志
 * Created by CodeGenerator on 2023/06/18.
 */

public interface SysOperLogMapper {

    /**
    * 增加
    * @param sysOperLog
    * @return 系统操作日志
    */
    Integer insertSysOperLog(SysOperLog sysOperLog);

    /**
    * 批量增加
    * @param sysOperLogList
    * @return
    */
    public int batchInsertSysOperLogList(List<SysOperLog> sysOperLogList);

    /**
    * 删除
    * @param operId
    */
    Integer deleteSysOperLogById(Long operId);

    /**
    * 批量删除
    */
    Integer deleteSysOperLogByIds(Long[] operIds);

    /**
    * 修改
    * @param sysOperLog
    */
    Integer updateSysOperLog(SysOperLog sysOperLog);

    /**
    * 批量修改魔偶几个字段
    * @param operIds
    */
    Integer batchUpdateSysOperLogList(Long[] operIds);

    /**
    * 查询全部
    */
    List<SysOperLog> selectSysOperLogList(SysOperLog sysOperLog);

    /**
    * 查询全部,联合查询使用
    */
    List<SysOperLog> selectUnionSysOperLogList(SysOperLog sysOperLog);

    /**
    * 根据Id查询单条数据
    */
    SysOperLog selectSysOperLogById(Long operId);

    /**
      * 查询访问地址的统计数量
      */
    @MapKey("name")
    List<Map<String,Object>> selectWebViewCount();

    /**
     *清空表格所有数据 OperLog
     */
    void cleanTable();
}
