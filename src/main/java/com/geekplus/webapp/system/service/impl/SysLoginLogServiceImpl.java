package com.geekplus.webapp.system.service.impl;

import com.geekplus.webapp.system.mapper.SysLoginLogMapper;
import com.geekplus.webapp.system.entity.SysLoginLog;
import com.geekplus.webapp.system.service.SysLoginLogService;
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
public class SysLoginLogServiceImpl implements SysLoginLogService {
    @Resource
    private SysLoginLogMapper sysLoginLogMapper;

    /**
    * 增加
    * @param sysLoginLog
    * @return 系统登录日志
    */
    public Integer insertSysLoginLog(SysLoginLog sysLoginLog){
        return sysLoginLogMapper.insertSysLoginLog(sysLoginLog);
    }

    /**
    * 批量增加
    * @param sysLoginLogList
    * @return 系统登录日志
    */
    public Integer batchInsertSysLoginLogList(List<SysLoginLog> sysLoginLogList){
        return sysLoginLogMapper.batchInsertSysLoginLogList(sysLoginLogList);
    }

    /**
    * 删除
    * @param logId
    */
    public Integer deleteSysLoginLogById(Long logId){
        return sysLoginLogMapper.deleteSysLoginLogById(logId);
    }

    /**
    * 批量删除
    */
    public Integer deleteSysLoginLogByIds(Long[] logIds){
        return sysLoginLogMapper.deleteSysLoginLogByIds(logIds);
    }

    /**
    * 修改
    * @param sysLoginLog
    */
    public Integer updateSysLoginLog(SysLoginLog sysLoginLog){
        return sysLoginLogMapper.updateSysLoginLog(sysLoginLog);
    }

    /**
    * 批量修改某几个字段
    * @param logIds
    */
    public Integer batchUpdateSysLoginLogList(Long[] logIds){
        return sysLoginLogMapper.batchUpdateSysLoginLogList(logIds);
    }

    /**
    * 查询全部
    */
    public List<SysLoginLog> selectSysLoginLogList(SysLoginLog sysLoginLog){
        return sysLoginLogMapper.selectSysLoginLogList(sysLoginLog);
    }

    /**
    * 查询全部,用于联合查询，在此基础做自己的定制改动
    */
    public List<SysLoginLog> selectUnionSysLoginLogList(SysLoginLog sysLoginLog){
        return sysLoginLogMapper.selectUnionSysLoginLogList(sysLoginLog);
    }

    /**
    * 根据Id查询单条数据
    */
    public SysLoginLog selectSysLoginLogById(Long logId){
        return sysLoginLogMapper.selectSysLoginLogById(logId);
    }

    /**
     *清空表格所有数据 SysLoginLog
     */
    @Override
    public void cleanTable() {
        sysLoginLogMapper.cleanTable();
    }
}
