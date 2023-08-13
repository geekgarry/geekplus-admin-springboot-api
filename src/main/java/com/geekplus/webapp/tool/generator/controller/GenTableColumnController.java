package com.geekplus.webapp.tool.generator.controller;

import com.geekplus.common.core.controller.BaseController;
import com.geekplus.common.domain.Result;
import com.geekplus.webapp.tool.generator.entity.TableColumnInfo;
import com.geekplus.webapp.tool.generator.service.GenTableColumnService;
import com.geekplus.common.page.PageDataInfo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 生成表数据列表 生成表数据列表
 * Created by CodeGenerator on 2023/08/07.
 */
@RestController
@RequestMapping("/generator/genTableColumn")
public class GenTableColumnController extends BaseController {
    @Resource
    private GenTableColumnService genTableColumnService;

    /**
     * 增加 生成表数据列表
     */
    @PostMapping("/add")
    public Result add(@RequestBody TableColumnInfo genTableColumn) {
        return toResult(genTableColumnService.insertGenTableColumn(genTableColumn));
    }

    /**
     * 增加 生成表数据列表
     */
    @PostMapping("/batchAdd")
    public Result batchAdd(@RequestBody List<TableColumnInfo> genTableColumn) {
    return toResult(genTableColumnService.batchInsertGenTableColumnList(genTableColumn));
    }

    /**
     * 删除 生成表数据列表
     */
    @GetMapping("/delete")
    public Result remove(@RequestParam Integer columnId) {
        return toResult(genTableColumnService.deleteGenTableColumnById(columnId));
    }

    /**
     * 批量删除 生成表数据列表
     */
    @DeleteMapping("/{columnIds}")
    public Result remove(@PathVariable Integer[] columnIds) {
        return toResult(genTableColumnService.deleteGenTableColumnByIds(columnIds));
    }

    /**
     * 更新 生成表数据列表
     */
    @PostMapping("/update")
    public Result edit(@RequestBody TableColumnInfo genTableColumn) {
        return toResult(genTableColumnService.updateGenTableColumn(genTableColumn));
    }

    /**
     * 单条数据详情 生成表数据列表
     */
    @GetMapping("/detail")
    public Result detail(@RequestParam Integer columnId) {
        TableColumnInfo genTableColumn = genTableColumnService.selectGenTableColumnById(columnId);
        return Result.success(genTableColumn);
    }

    /**
     * 条件查询所有 生成表数据列表
     */
    @GetMapping("/listAll")
    public PageDataInfo listAll(TableColumnInfo genTableColumn) {
        //PageHelper.startPage(page, size);
        List<TableColumnInfo> list = genTableColumnService.selectGenTableColumnList(genTableColumn);
        PageDataInfo rspData = new PageDataInfo();
        rspData.setCode(200);
        rspData.setMsg("查询成功");
        rspData.setRows(list);
        rspData.setTotal(new PageInfo(list).getTotal());
        //PageInfo pageInfo = new PageInfo(list);
        return rspData;
    }

    /**
     * 条件查询所有 生成表数据列表
     */
    @GetMapping("/list")
    //public PageDataInfo list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size,GenTableColumn genTableColumn) {
    public PageDataInfo list(TableColumnInfo genTableColumn) {
        //PageHelper.startPage(pageParam.getPageNum(), pageParam.getPageSize());
        startPage();
        List<TableColumnInfo> list = genTableColumnService.selectGenTableColumnList(genTableColumn);
        //PageInfo pageInfo = new PageInfo(list);
        return getDataTable(list);
    }

    /**
     * 清空 系统登录日志
     */
    //@RequiresPermissions("tool:genTable:clean")
    @DeleteMapping("/clean")
    public Result cleanAll() {
        genTableColumnService.cleanTable();
        return Result.success();
    }
}
