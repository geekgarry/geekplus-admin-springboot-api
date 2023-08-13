package com.geekplus.webapp.system.controller;

import com.geekplus.common.core.controller.BaseController;
import com.geekplus.common.domain.Result;
import com.geekplus.common.myexception.BusinessException;
import com.geekplus.webapp.system.entity.SysDept;
import com.geekplus.webapp.system.service.SysDeptService;
import com.geekplus.common.page.PageDataInfo;
import com.github.pagehelper.PageInfo;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 部门表 部门表
 * Created by CodeGenerator on 2023/07/17.
 */
@RestController
@RequestMapping("/sys/dept")
public class SysDeptController extends BaseController {
    @Resource
    private SysDeptService sysDeptService;

    /**
     * 增加 部门表
     */
    @RequiresPermissions("system:dept:add")
    @PostMapping("/add")
    public Result add(@RequestBody SysDept sysDept) {
        return toResult(sysDeptService.insertSysDept(sysDept));
    }

    /**
     * 增加 部门表
     */
    @RequiresPermissions("system:dept:batchAdd")
    @PostMapping("/batchAdd")
    public Result batchAdd(@RequestBody List<SysDept> sysDept) {
    return toResult(sysDeptService.batchInsertSysDeptList(sysDept));
    }

    /**
     * 删除 部门表
     */
    @RequiresPermissions("system:dept:delete")
    @GetMapping("/delete")
    public Result removeSysDept(@RequestParam Long deptId) {
        return toResult(sysDeptService.deleteSysDeptById(deptId));
    }

    /**
     * 批量删除 部门表
     */
    @RequiresPermissions("system:dept:delete")
    @DeleteMapping("/{deptId}")
    public Result remove(@PathVariable Long deptId) {
        return toResult(sysDeptService.deleteSysDeptById(deptId));
    }

    /**
     * 更新 部门表
     */
    @RequiresPermissions("system:dept:update")
    @PostMapping("/update")
    public Result edit(@RequestBody SysDept sysDept) {
        return toResult(sysDeptService.updateSysDept(sysDept));
    }

    /**
     * 单条数据详情 部门表
     */
    @RequiresPermissions("system:dept:detail")
    @GetMapping("/detail")
    public Result detail(@RequestParam Long deptId) {
        SysDept sysDept = sysDeptService.selectSysDeptById(deptId);
        return Result.success(sysDept);
    }

    /**
     * 条件查询所有 部门表
     */
    @RequiresPermissions("system:dept:listAll")
    @GetMapping("/listAll")
    public PageDataInfo listAll(SysDept sysDept) {
        //PageHelper.startPage(page, size);
        List<SysDept> list = sysDeptService.selectSysDeptList(sysDept);
        PageDataInfo rspData = new PageDataInfo();
        rspData.setCode(200);
        rspData.setMsg("查询成功");
        rspData.setRows(list);
        rspData.setTotal(new PageInfo(list).getTotal());
        //PageInfo pageInfo = new PageInfo(list);
        return rspData;
    }

    /**
     * 条件查询所有 部门表
     */
    @RequiresPermissions("system:dept:list")
    //public PageDataInfo list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size,SysDept sysDept) {
    @GetMapping("/list")
    public PageDataInfo list(SysDept sysDept) {
        //PageHelper.startPage(pageParam.getPageNum(), pageParam.getPageSize());
        startPage();
        List<SysDept> list = sysDeptService.selectSysDeptList(sysDept);
        //PageInfo pageInfo = new PageInfo(list);
        return getDataTable(list);
    }

    /**
     * 部门树查询 部门表
     */
    @GetMapping("/getSysDeptTree")
    public Result getSysDeptTreeList() {
        //PageHelper.startPage(pageParam.getPageNum(), pageParam.getPageSize());
        List<SysDept> list = sysDeptService.getSysDeptTreeList();
        //PageInfo pageInfo = new PageInfo(list);
        return Result.success(list);
    }
}
