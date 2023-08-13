package com.geekplus.webapp.system.service;

import com.geekplus.webapp.system.entity.SysNotice;
//import com.geekplus.core.Service;
import java.util.List;


/**
 *
 * Created by CodeGenerator on 2023/06/18.
 */
public interface SysNoticeService {

    /**
    * 增加
    * @param sysNotice
    * @return
    */
    public Integer insertSysNotice(SysNotice sysNotice);

    /**
    * 批量增加
    * @param sysNoticeList
    * @return
    */
    public Integer batchInsertSysNoticeList(List<SysNotice> sysNoticeList);

    /**
    * 删除
    * @param noticeId
    */
    public Integer deleteSysNoticeById(Long noticeId);

    /**
    * 批量删除某几个字段
    */
    public Integer deleteSysNoticeByIds(Long[] noticeIds);

    /**
    * 修改
    * @param sysNotice
    */
    public Integer updateSysNotice(SysNotice sysNotice);

    /**
    * 批量修改
    * @param noticeIds
    */
    public Integer batchUpdateSysNoticeList(Long[] noticeIds);

    /**
    * 查询全部
    */
    public List<SysNotice> selectSysNoticeList(SysNotice sysNotice);

    /**
    * 查询全部，用作联合查询使用(在基础上修改即可)
    */
    public List<SysNotice> selectUnionSysNoticeList(SysNotice sysNotice);

    /**
    * 根据Id查询单条数据
    */
    public SysNotice selectSysNoticeById(Long noticeId);
}
