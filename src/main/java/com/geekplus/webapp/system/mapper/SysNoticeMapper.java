package com.geekplus.webapp.system.mapper;

import com.geekplus.webapp.system.entity.SysNotice;
import java.util.List;

/**
 *
 * Created by CodeGenerator on 2023/06/18.
 */

public interface SysNoticeMapper {

    /**
    * 增加
    * @param sysNotice
    * @return
    */
    Integer insertSysNotice(SysNotice sysNotice);

    /**
    * 批量增加
    * @param sysNoticeList
    * @return
    */
    public int batchInsertSysNoticeList(List<SysNotice> sysNoticeList);

    /**
    * 删除
    * @param noticeId
    */
    Integer deleteSysNoticeById(Long noticeId);

    /**
    * 批量删除
    */
    Integer deleteSysNoticeByIds(Long[] noticeIds);

    /**
    * 修改
    * @param sysNotice
    */
    Integer updateSysNotice(SysNotice sysNotice);

    /**
    * 批量修改魔偶几个字段
    * @param noticeIds
    */
    Integer batchUpdateSysNoticeList(Long[] noticeIds);

    /**
    * 查询全部
    */
    List<SysNotice> selectSysNoticeList(SysNotice sysNotice);

    /**
    * 查询全部,联合查询使用
    */
    List<SysNotice> selectUnionSysNoticeList(SysNotice sysNotice);

    /**
    * 根据Id查询单条数据
    */
    SysNotice selectSysNoticeById(Long noticeId);
}
