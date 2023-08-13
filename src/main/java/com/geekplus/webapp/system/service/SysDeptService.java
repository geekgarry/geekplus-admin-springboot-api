package com.geekplus.webapp.system.service;

import com.geekplus.webapp.system.entity.SysDept;

import java.util.List;

//import com.geekplus.core.Service;


/**
 * 部门表 部门表
 * Created by CodeGenerator on 2023/07/17.
 */
public interface SysDeptService {

    /**
     * 增加
     * @param sysDept
     * @return 部门表
     */
    public Integer insertSysDept(SysDept sysDept);

    /**
     * 批量增加
     * @param sysDeptList
     * @return 部门表
     */
    public Integer batchInsertSysDeptList(List<SysDept> sysDeptList);

    /**
     * 删除
     * @param deptId
     */
    public Integer deleteSysDeptById(Long deptId);

    /**
     * 批量删除某几个字段
     */
    public Integer deleteSysDeptByIds(Long[] deptIds);

    /**
     * 修改
     * @param sysDept
     */
    public Integer updateSysDept(SysDept sysDept);

    /**
     * 批量修改
     * @param deptIds
     */
    public Integer batchUpdateSysDeptList(Long[] deptIds);

    /**
     * 查询全部
     */
    public List<SysDept> selectSysDeptList(SysDept sysDept);

    /**
     * 查询部门树菜单
     */
    public List<SysDept> getSysDeptTreeList();

    /**
     * 查询全部，用作联合查询使用(在基础上修改即可)
     */
    public List<SysDept> selectUnionSysDeptList(SysDept sysDept);

    /**
     * 根据Id查询单条数据
     */
    public SysDept selectSysDeptById(Long deptId);

    /**
     * 根据部门Id查询是否还有用户
     */
    Integer checkDeptIsExistUser(Long deptId);

    /**
     * 查询部门下是否还有子部门
     */
    Integer hasChildDeptByDeptId(Long deptId);

    /**
     * 查询部门下是否还有子部门
     */
    Integer hasChildDeptByDeptId2(Long deptId);
}
