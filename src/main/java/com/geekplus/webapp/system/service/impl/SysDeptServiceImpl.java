package com.geekplus.webapp.system.service.impl;

import com.geekplus.common.constant.UserConstants;
import com.geekplus.common.myexception.BusinessException;
import com.geekplus.common.util.string.StringUtils;
import com.geekplus.webapp.system.mapper.SysDeptMapper;
import com.geekplus.webapp.system.entity.SysDept;
import com.geekplus.webapp.system.service.SysDeptService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;


/**
 * Created by CodeGenerator on 2023/07/17.
 */
@Slf4j
@Service
@Transactional
public class SysDeptServiceImpl implements SysDeptService {
    @Resource
    private SysDeptMapper sysDeptMapper;

    /**
     * 增加
     * @param sysDept
     * @return 部门表
     */
    public Integer insertSysDept(SysDept sysDept){
        SysDept info = sysDeptMapper.selectSysDeptById(sysDept.getParentId());
        // 如果父节点不为正常状态,则不允许新增子节点
        if (!UserConstants.DEPT_NORMAL.equals(info.getStatus()))
        {
            throw new BusinessException("部门停用，不允许新增");
        }
        sysDept.setAncestors(info.getAncestors() + "," + sysDept.getParentId());
        return sysDeptMapper.insertSysDept(sysDept);
    }

    /**
     * 批量增加
     * @param sysDeptList
     * @return 部门表
     */
    public Integer batchInsertSysDeptList(List<SysDept> sysDeptList){
        return sysDeptMapper.batchInsertSysDeptList(sysDeptList);
    }

    /**
     * 删除
     * @param deptId
     */
    public Integer deleteSysDeptById(Long deptId){
        if(sysDeptMapper.checkDeptIsExistUser(deptId)>0){
            throw new BusinessException("当前部门下还有用户，不能删除！");
        }
        if(sysDeptMapper.hasChildDeptByDeptId(deptId)>0){
            throw new BusinessException("当前部门下还有子部门，无法删除！");
        }
        return sysDeptMapper.deleteSysDeptById(deptId);
    }

    /**
     * 批量删除
     */
    public Integer deleteSysDeptByIds(Long[] deptIds){
        return sysDeptMapper.deleteSysDeptByIds(deptIds);
    }

    /**
     * 修改
     * @param sysDept
     */
    public Integer updateSysDept(SysDept sysDept){
        SysDept newParentDept = sysDeptMapper.selectSysDeptById(sysDept.getParentId());
        SysDept oldDept = sysDeptMapper.selectSysDeptById(sysDept.getDeptId());
        if (StringUtils.isNotNull(newParentDept) && StringUtils.isNotNull(oldDept))
        {
            String newAncestors = newParentDept.getAncestors() + "," + newParentDept.getDeptId();
            String oldAncestors = oldDept.getAncestors();
            sysDept.setAncestors(newAncestors);
            updateDeptChildren(sysDept.getDeptId(), newAncestors, oldAncestors);
        }
        int result = sysDeptMapper.updateSysDept(sysDept);
        if (UserConstants.DEPT_NORMAL.equals(sysDept.getStatus()))
        {
            // 如果该部门是启用状态，则启用该部门的所有上级部门
            updateParentDeptStatus(sysDept);
        }
        if(!sysDept.equals(oldDept.getStatus()) && hasChildDeptByDeptId2(oldDept.getDeptId())>0){
            updateChildDeptStatus(sysDept);
        }
        return sysDeptMapper.updateSysDept(sysDept);
    }

    /**
     * 修改该部门的父级部门状态
     *
     * @param sysDept 当前部门
     */
    private void updateParentDeptStatus(SysDept sysDept)
    {
        String updateBy = sysDept.getUpdateBy();
        sysDept = sysDeptMapper.selectSysDeptById(sysDept.getDeptId());
        sysDept.setUpdateBy(updateBy);
        sysDeptMapper.updateDeptStatus(sysDept);
    }

    /**
     * 修改该父级部门下所有部门的状态
     *
     * @param sysDept 当前部门
     */
    private void updateChildDeptStatus(SysDept sysDept)
    {
        String updateBy = sysDept.getUpdateBy();
        sysDept = sysDeptMapper.selectSysDeptById(sysDept.getDeptId());
        sysDept.setUpdateBy(updateBy);
        sysDeptMapper.updateChildDeptStatus(sysDept);
    }

    /**
     * 修改子元素关系
     *
     * @param deptId 被修改的部门ID
     * @param newAncestors 新的父ID集合
     * @param oldAncestors 旧的父ID集合
     */
    public void updateDeptChildren(Long deptId, String newAncestors, String oldAncestors)
    {
        List<SysDept> children = sysDeptMapper.selectChildrenDeptById(deptId);
        for (SysDept child : children)
        {
            child.setAncestors(child.getAncestors().replace(oldAncestors, newAncestors));
        }
        if (children.size() > 0)
        {
            sysDeptMapper.updateDeptChildren(children);
        }
    }

    /**
     * 批量修改某几个字段
     * @param deptIds
     */
    public Integer batchUpdateSysDeptList(Long[] deptIds){
        return sysDeptMapper.batchUpdateSysDeptList(deptIds);
    }

    /**
     * 查询全部
     */
    public List<SysDept> selectSysDeptList(SysDept sysDept){
        return sysDeptMapper.selectSysDeptList(sysDept);
    }

    /**
     * 部门选择树
     */
    public List<SysDept> getSysDeptTreeList(){
        return getParentMenuList(sysDeptMapper.selectSysDeptTreeList());
    }

    /**
     * 查询全部,用于联合查询，在此基础做自己的定制改动
     */
    public List<SysDept> selectUnionSysDeptList(SysDept sysDept){
        return sysDeptMapper.selectUnionSysDeptList(sysDept);
    }

    /**
     * 根据Id查询单条数据
     */
    public SysDept selectSysDeptById(Long deptId){
        return sysDeptMapper.selectSysDeptById(deptId);
    }

    @Override
    public Integer checkDeptIsExistUser(Long deptId) {
        return sysDeptMapper.checkDeptIsExistUser(deptId);
    }

    @Override
    public Integer hasChildDeptByDeptId(Long deptId) {
        return sysDeptMapper.hasChildDeptByDeptId(deptId);
    }

    @Override
    public Integer hasChildDeptByDeptId2(Long deptId) {
        return sysDeptMapper.hasChildDeptByDeptId2(deptId);
    }
    //递归方法，加载菜单为折叠形态
    public List<SysDept> getParentMenuList(List<SysDept> list){
        List<SysDept> entityList=new ArrayList<>();
        list.stream().forEach(item ->{
            //SysMenu menu = lt.next();
            if (item.getParentId() == 0) {
                log.info("==========>数据"+entityList);
                item.setChildren(getChild(list, item.getDeptId()));
                entityList.add(item);
            }
        });
        return entityList;
    }

    public List<SysDept> getChild(List<SysDept> list, Long id){
        List<SysDept> childList=new ArrayList<>();
        for (Iterator<SysDept> iterator = list.iterator(); iterator.hasNext();){
            SysDept item = iterator.next();
            if (item.getParentId().equals(id)){
                log.info("==========>数据"+item);
                item.setChildren(getChild(list, item.getDeptId()));
                childList.add(item);
                log.info("==========>数据"+childList);
            }
        }
        return childList;
    }
}
