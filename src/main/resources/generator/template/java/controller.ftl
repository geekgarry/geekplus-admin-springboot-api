package ${basePackage}.webapp.system.controller;

import com.geekplus.common.core.controller.BaseController;
import ${basePackage}.common.domain.Result;
import ${basePackage}.common.enums.BusinessType;
import ${basePackage}.common.util.poi.ExcelUtil;
import ${basePackage}.webapp.system.entity.${modelNameUpperCamel};
import ${basePackage}.webapp.system.service.${modelNameUpperCamel}Service;
import ${basePackage}.common.page.PageDataInfo;
import ${basePackage}.common.annotation.Log;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * ${title} ${functionName}
 * Created by ${author} on ${date}.
 */
@RestController
@RequestMapping("${baseRequestMapping}")
public class ${modelNameUpperCamel}Controller extends BaseController {
    @Resource
    private ${modelNameUpperCamel}Service ${modelNameLowerCamel}Service;

    /**
     * 增加 ${functionName}
     */
    @PostMapping("/add")
    public Result add(@RequestBody ${modelNameUpperCamel} ${modelNameLowerCamel}) {
        return toResult(${modelNameLowerCamel}Service.insert${modelNameUpperCamel}(${modelNameLowerCamel}));
    }

    /**
     * 增加 ${functionName}
     */
    @PostMapping("/batchAdd")
    public Result batchAdd(@RequestBody List<${modelNameUpperCamel}> ${modelNameLowerCamel}) {
    return toResult(${modelNameLowerCamel}Service.batchInsert${modelNameUpperCamel}List(${modelNameLowerCamel}));
    }

    /**
     * 删除 ${functionName}
     */
    @Log(title = "删除", businessType = BusinessType.DELETE)
    @GetMapping("/delete")
    public Result remove(@RequestParam ${pkColumn.javaType} ${pkColumn.smallColumnName}) {
        return toResult(${modelNameLowerCamel}Service.delete${modelNameUpperCamel}ById(${pkColumn.smallColumnName}));
    }

    /**
     * 批量删除 ${functionName}
     */
    @Log(title = "批量删除", businessType = BusinessType.DELETE)
    @DeleteMapping("/{${pkColumn.smallColumnName}s}")
    public Result remove(@PathVariable ${pkColumn.javaType}[] ${pkColumn.smallColumnName}s) {
        return toResult(${modelNameLowerCamel}Service.delete${modelNameUpperCamel}ByIds(${pkColumn.smallColumnName}s));
    }

    /**
     * 更新 ${functionName}
     */
    @Log(title = "修改", businessType = BusinessType.UPDATE)
    @PostMapping("/update")
    public Result edit(@RequestBody ${modelNameUpperCamel} ${modelNameLowerCamel}) {
        return toResult(${modelNameLowerCamel}Service.update${modelNameUpperCamel}(${modelNameLowerCamel}));
    }

    /**
     * 单条数据详情 ${functionName}
     */
    @GetMapping("/detail")
    public Result detail(@RequestParam ${pkColumn.javaType} ${pkColumn.smallColumnName}) {
        ${modelNameUpperCamel} ${modelNameLowerCamel} = ${modelNameLowerCamel}Service.select${modelNameUpperCamel}ById(${pkColumn.smallColumnName});
        return Result.success(${modelNameLowerCamel});
    }

    /**
     * 条件查询所有 ${functionName}
     */
    @GetMapping("/listAll")
    public PageDataInfo listAll(${modelNameUpperCamel} ${modelNameLowerCamel}) {
        //PageHelper.startPage(page, size);
        List<${modelNameUpperCamel}> list = ${modelNameLowerCamel}Service.select${modelNameUpperCamel}List(${modelNameLowerCamel});
        PageDataInfo rspData = new PageDataInfo();
        rspData.setCode(200);
        rspData.setMsg("查询成功");
        rspData.setRows(list);
        rspData.setTotal(new PageInfo(list).getTotal());
        //PageInfo pageInfo = new PageInfo(list);
        return rspData;
    }

    /**
     * 条件查询所有 ${functionName}
     */
    @GetMapping("/list")
    //public PageDataInfo list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size,${modelNameUpperCamel} ${modelNameLowerCamel}) {
    public PageDataInfo list(${modelNameUpperCamel} ${modelNameLowerCamel}) {
        //PageHelper.startPage(pageParam.getPageNum(), pageParam.getPageSize());
        startPage();
        List<${modelNameUpperCamel}> list = ${modelNameLowerCamel}Service.select${modelNameUpperCamel}List(${modelNameLowerCamel});
        //PageInfo pageInfo = new PageInfo(list);
        return getDataTable(list);
    }

    /**
    * 导出数据字典类型
    */
    @Log(title = "导出数据字典类型", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public Result export(${modelNameUpperCamel} ${modelNameLowerCamel}){
        List<${modelNameUpperCamel}> list = ${modelNameLowerCamel}Service.select${modelNameUpperCamel}List(${modelNameLowerCamel});
        ExcelUtil<${modelNameUpperCamel}> util = new ExcelUtil<${modelNameUpperCamel}>(${modelNameUpperCamel}.class);
        return util.exportExcel(list, "${modelNameLowerCamel}");
    }
}
