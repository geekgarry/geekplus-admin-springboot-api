package com.geekplus.webapp.tool.generator.controller;

import com.geekplus.common.annotation.Log;
import com.geekplus.common.core.controller.BaseController;
import com.geekplus.common.domain.Result;
import com.geekplus.common.enums.BusinessType;
import com.geekplus.common.page.PageDataInfo;
import com.geekplus.common.util.file.FileCompressUtils;
import com.geekplus.webapp.tool.generator.CodeGenerateByTemplate;
import com.geekplus.webapp.tool.generator.entity.TableColumnInfo;
import com.geekplus.webapp.tool.generator.entity.TableInfo;
import com.geekplus.webapp.tool.generator.service.GenCodeService;
import com.geekplus.webapp.tool.generator.service.GenTableColumnService;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
  * @Author geekplus
  * @Description //代码生成
  * @Param
  * @Throws
  * @Return {@link }
  */
@Slf4j
@RestController
@RequestMapping("/generator")
public class GenCodeController extends BaseController {

    @Resource
    private GenCodeService genCodeService;
    @Resource
    private GenTableColumnService genTableColumnService;

    /**
     * 查询所有非系统核心数据表
     */
    @GetMapping("/getAllTable")
    public Result getAllTable() {
        return Result.success(genCodeService.getAllTable());
    }

    /**
     * 获取数据表详情信息
     */
    @GetMapping("/getTableInfo")
    public Result getTableInfo(String tableName) {
        return Result.success(genCodeService.getTableInfoByName(tableName));
    }

    /**
     * 增加代码生成数据表 代码生成业务表
     */
    @PostMapping("/table/genTable")
    public Result addTableInfo(@RequestBody TableInfo tableInfo) {
        if(genCodeService.selectGenTableList(tableInfo).size()>0){
            return Result.error("已经导入过数据表！");
        }
        List<TableColumnInfo> tableColumnInfoList=new ArrayList<>();
        if(genCodeService.insertGenTable(CodeGenerateByTemplate.buildGenerateTableInfo(tableInfo))>0){
            Long tableId=tableInfo.getTableId();
            tableColumnInfoList=genCodeService.getTableColumnList(tableInfo.getTableName());
            tableColumnInfoList.forEach(tableColumnInfo -> {
                tableColumnInfo.setTableId(tableId.intValue());
            });
        }
        return genTableColumnService.batchInsertGenTableColumnList(tableColumnInfoList)>0?Result.success():Result.error();
    }

    /**
     * 增加 代码生成业务表
     */
    @PostMapping("/table/add")
    public Result add(@RequestBody TableInfo tableInfo) {
        return toResult(genCodeService.insertGenTable(tableInfo));
    }

    /**
     * 增加 代码生成业务表
     */
    @PostMapping("/table/batchAdd")
    public Result batchAdd(@RequestBody List<TableInfo> tableInfo) {
        return toResult(genCodeService.batchInsertGenTableList(tableInfo));
    }

    /**
     * 删除 代码生成业务表
     */
    @GetMapping("/table/delete")
    public Result remove(@RequestParam Long tableId) {
        return toResult(genCodeService.deleteGenTableById(tableId));
    }

    /**
     * 批量删除 代码生成业务表
     */
    @DeleteMapping("/table/{tableIds}")
    public Result remove(@PathVariable Long[] tableIds) {
        return toResult(genCodeService.deleteGenTableByIds(tableIds));
    }

    /**
     * 更新 代码生成业务表
     */
    @PostMapping("/table/update")
    public Result edit(@RequestBody TableInfo tableInfo) {
        return toResult(genCodeService.updateGenTable(tableInfo));
    }

    /**
     * 单条数据详情 代码生成业务表
     */
    @GetMapping("/table/detail")
    public Result detail(@RequestParam Long tableId) {
        TableInfo tableInfo = genCodeService.selectGenTableById(tableId);
        return Result.success(tableInfo);
    }

    /**
     * 条件查询所有 代码生成业务表
     */
    @GetMapping("/table/listAll")
    public PageDataInfo listAll(TableInfo tableInfo) {
        //PageHelper.startPage(page, size);
        List<TableInfo> list = genCodeService.selectGenTableList(tableInfo);
        PageDataInfo rspData = new PageDataInfo();
        rspData.setCode(200);
        rspData.setMsg("查询成功");
        rspData.setRows(list);
        rspData.setTotal(new PageInfo(list).getTotal());
        //PageInfo pageInfo = new PageInfo(list);
        return rspData;
    }

    /**
     * 条件查询所有 代码生成业务表
     */
    @GetMapping("/table/list")
    //public PageDataInfo list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size,GenTable genTable) {
    public PageDataInfo list(TableInfo tableInfo) {
        //PageHelper.startPage(pageParam.getPageNum(), pageParam.getPageSize());
        startPage();
        List<TableInfo> list = genCodeService.selectGenTableList(tableInfo);
        //PageInfo pageInfo = new PageInfo(list);
        return getDataTable(list);
    }

    /**
     * 生成代码直接查询数据库表格生成，information_schema
     */
    @Log(title = "代码生成", businessType = BusinessType.GENCODE)
    @GetMapping("/download/{tableName}")
    public void downloadByTableSchema(HttpServletResponse response, @PathVariable("tableName") String tableName)
    {
        try {
            List<Map<String,byte[]>> byStreamTemplate = genCodeService.downloadCode(tableName);
            FileCompressUtils.downloadZipStream(response,byStreamTemplate,"geekplus");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 生成代码（下载方式），gen_table表格信息
     */
    @Log(title = "代码生成", businessType = BusinessType.GENCODE)
    @GetMapping("/downloadByGenTable/{tableIds}")
    public void downloadByGenTable(HttpServletResponse response,@PathVariable Long[] tableIds)
    {
        List<TableInfo> tableInfoList = genCodeService.selectGenTableByIds(tableIds);
        try {
            List<Map<String,byte[]>> byStreamTemplate=null;
            if(tableInfoList.size()>1){
                byStreamTemplate = genCodeService.downloadCodeByTable(tableInfoList);
            }else {
                byStreamTemplate = genCodeService.downloadCodeByTable(tableInfoList.get(0));
            }
            FileCompressUtils.downloadZipStream(response,byStreamTemplate,"geekplus");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 清空 系统登录日志
     */
    //@RequiresPermissions("tool:genTable:clean")
    @DeleteMapping("/genTable/clean")
    public Result cleanAll() {
        genCodeService.cleanTable();
        genTableColumnService.cleanTable();
        return Result.success();
    }
}
