package com.geekplus.webapp.system.service.impl;

import com.geekplus.common.core.socket.WebSocketServer;
import com.geekplus.webapp.system.mapper.SysNoticeMapper;
import com.geekplus.webapp.system.entity.SysNotice;
import com.geekplus.webapp.system.service.SysNoticeService;
//import com.geekplus.core.AbstractService;
import com.geekplus.webapp.tool.generator.utils.JSONObjectUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;


/**
 * Created by CodeGenerator on 2023/06/18.
 */
@Service
@Transactional
public class SysNoticeServiceImpl implements SysNoticeService {
    @Resource
    private SysNoticeMapper sysNoticeMapper;

    @Resource
    private WebSocketServer webSocketServer;
    /**
    * 增加
    * @param sysNotice
    * @return
    */
    public Integer insertSysNotice(SysNotice sysNotice){
        Map<String,Object> map=new HashMap<>();
        map.put("notifyMsg","新增系统通知");
        map.put("systemNotice","add");
        map.put("type","notifyMsg");
        webSocketServer.sendMessageAll(JSONObjectUtil.objectToJson(map));
        return sysNoticeMapper.insertSysNotice(sysNotice);
    }

    /**
    * 批量增加
    * @param sysNoticeList
    * @return
    */
    public Integer batchInsertSysNoticeList(List<SysNotice> sysNoticeList){
        return sysNoticeMapper.batchInsertSysNoticeList(sysNoticeList);
    }

    /**
    * 删除
    * @param noticeId
    */
    public Integer deleteSysNoticeById(Long noticeId){
        return sysNoticeMapper.deleteSysNoticeById(noticeId);
    }

    /**
    * 批量删除
    */
    public Integer deleteSysNoticeByIds(Long[] noticeIds){
        return sysNoticeMapper.deleteSysNoticeByIds(noticeIds);
    }

    /**
    * 修改
    * @param sysNotice
    */
    public Integer updateSysNotice(SysNotice sysNotice){

        return sysNoticeMapper.updateSysNotice(sysNotice);
    }

    /**
    * 批量修改某几个字段
    * @param noticeIds
    */
    public Integer batchUpdateSysNoticeList(Long[] noticeIds){
        return sysNoticeMapper.batchUpdateSysNoticeList(noticeIds);
    }

    /**
    * 查询全部
    */
    public List<SysNotice> selectSysNoticeList(SysNotice sysNotice){
        return sysNoticeMapper.selectSysNoticeList(sysNotice);
    }

    /**
    * 查询全部,用于联合查询，在此基础做自己的定制改动
    */
    public List<SysNotice> selectUnionSysNoticeList(SysNotice sysNotice){
        return sysNoticeMapper.selectUnionSysNoticeList(sysNotice);
    }

    /**
    * 根据Id查询单条数据
    */
    public SysNotice selectSysNoticeById(Long noticeId){
        return sysNoticeMapper.selectSysNoticeById(noticeId);
    }
}
