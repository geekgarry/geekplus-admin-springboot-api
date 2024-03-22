package com.geekplus.webapp.system.service.impl;

import com.geekplus.common.core.socket.WebSocketServer;
import com.geekplus.webapp.system.mapper.SysOperLogMapper;
import com.geekplus.webapp.system.entity.SysOperLog;
import com.geekplus.webapp.system.service.SysOperLogService;
//import com.geekplus.core.AbstractService;
import com.geekplus.webapp.tool.generator.utils.JSONObjectUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;


/**
 * Created by CodeGenerator on 2023/06/18.
 */
@Service
@Transactional
public class SysOperLogServiceImpl implements SysOperLogService {
    @Resource
    private SysOperLogMapper sysOperLogMapper;

    @Resource
    private WebSocketServer webSocketServer;

    /**
    * 增加
    * @param sysOperLog
    * @return 系统操作日志
    */
    public Integer insertSysOperLog(SysOperLog sysOperLog) {
        Map<String,Object> map=new HashMap<>();
        map.put("notifyMsg","新增操作日志");
        map.put("operationLog","add");
        map.put("type","notifyMsg");
        webSocketServer.sendMessageAll(JSONObjectUtil.objectToJson(map));
        return sysOperLogMapper.insertSysOperLog(sysOperLog);
    }

    /**
    * 批量增加
    * @param sysOperLogList
    * @return 系统操作日志
    */
    public Integer batchInsertSysOperLogList(List<SysOperLog> sysOperLogList){
        return sysOperLogMapper.batchInsertSysOperLogList(sysOperLogList);
    }

    /**
    * 删除
    * @param operId
    */
    public Integer deleteSysOperLogById(Long operId){
        return sysOperLogMapper.deleteSysOperLogById(operId);
    }

    /**
    * 批量删除
    */
    public Integer deleteSysOperLogByIds(Long[] operIds){
        return sysOperLogMapper.deleteSysOperLogByIds(operIds);
    }

    /**
    * 修改
    * @param sysOperLog
    */
    public Integer updateSysOperLog(SysOperLog sysOperLog){
        return sysOperLogMapper.updateSysOperLog(sysOperLog);
    }

    /**
    * 批量修改某几个字段
    * @param operIds
    */
    public Integer batchUpdateSysOperLogList(Long[] operIds){
        return sysOperLogMapper.batchUpdateSysOperLogList(operIds);
    }

    /**
    * 查询全部
    */
    public List<SysOperLog> selectSysOperLogList(SysOperLog sysOperLog){
        return sysOperLogMapper.selectSysOperLogList(sysOperLog);
    }

    /**
    * 查询全部,用于联合查询，在此基础做自己的定制改动
    */
    public List<SysOperLog> selectUnionSysOperLogList(SysOperLog sysOperLog){
        return sysOperLogMapper.selectUnionSysOperLogList(sysOperLog);
    }

    /**
    * 根据Id查询单条数据
    */
    public SysOperLog selectSysOperLogById(Long operId){
        return sysOperLogMapper.selectSysOperLogById(operId);
    }

    /**
     * 查询访问地址的统计数量
     */
    @Override
    public List<Map<String, Object>> selectWebVisitorCount() {
        return sysOperLogMapper.selectWebViewCount();
    }

    /**
     *清空表格所有数据 OperLog
     */
    @Override
    public void cleanTable() {
        sysOperLogMapper.cleanTable();
    }
}
