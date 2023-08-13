package com.geekplus.webapp.system.mapper;

import com.geekplus.webapp.system.entity.SysDept;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 部门表 部门表
 * Created by CodeGenerator on 2023/07/17.
 */

public interface SysDeptMapper {

    /**
     * 增加
     * @param sysDept
     * @return 部门表
     */
    Integer insertSysDept(SysDept sysDept);

    /**
     * 批量增加
     * @param sysDeptList
     * @return
     */
    public int batchInsertSysDeptList(List<SysDept> sysDeptList);

    /**
     * 删除
     * @param deptId
     */
    Integer deleteSysDeptById(Long deptId);

    /**
     * 批量删除
     */
    Integer deleteSysDeptByIds(Long[] deptIds);

    /**
     * 修改
     * @param sysDept
     */
    Integer updateSysDept(SysDept sysDept);

    /**
     * 修改所在部门的父级部门状态
     *
     * @param sysDept 部门
     */
    public void updateDeptStatus(SysDept sysDept);

    /**
     * 修改父级部门下的所有部门的状态
     *
     * @param sysDept 部门
     */
    public void updateChildDeptStatus(SysDept sysDept);

    /**
     * 修改子元素关系
     *
     * @param sysDepts 子元素
     * @return 结果
     */
    public int updateDeptChildren(@Param("depts") List<SysDept> sysDepts);

    /**
     * 批量修改魔偶几个字段
     * @param deptIds
     */
    Integer batchUpdateSysDeptList(Long[] deptIds);

    /**
     * 查询全部
     */
    List<SysDept> selectSysDeptList(SysDept sysDept);

    /**
     * 根据ID查询所有子部门
     *
     * @param deptId 部门ID
     * @return 部门列表
     */
    public List<SysDept> selectChildrenDeptById(Long deptId);

    /**
     * 查询全部,联合查询使用
     */
    List<SysDept> selectUnionSysDeptList(SysDept sysDept);

    /**
     * 部门树查询使用
     */
    List<SysDept> selectSysDeptTreeList();

    /**
     * 根据Id查询单条数据
     */
    SysDept selectSysDeptById(Long deptId);

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
