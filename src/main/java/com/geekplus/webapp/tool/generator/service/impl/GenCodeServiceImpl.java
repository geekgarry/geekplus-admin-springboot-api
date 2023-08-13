/**
 * author     : geekplus
 * email      : geekcjj@gmail.com
 * date       : 7/17/23 02:50
 * description: 做什么的？
 */
package com.geekplus.webapp.tool.generator.service.impl;

import com.geekplus.common.util.file.FileCompressUtils;
import com.geekplus.webapp.system.service.UserTokenService;
import com.geekplus.webapp.tool.generator.CodeGenerateByTemplate;
import com.geekplus.webapp.tool.generator.entity.TableColumnInfo;
import com.geekplus.webapp.tool.generator.entity.TableInfo;
import com.geekplus.webapp.tool.generator.mapper.TableMapper;
import com.geekplus.webapp.tool.generator.service.GenCodeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class GenCodeServiceImpl implements GenCodeService {
    @Resource
    private TableMapper tableMapper;
    @Resource
    private UserTokenService tokenService;

    /**
     * @Author geekplus
     * @Description //获取非系统核心数据表
     * @Param
     * @Throws
     * @Return {@link }
     */
    @Override
    public List<TableInfo> getAllTable() {
        return tableMapper.listTable();
    }

    /**
     * @Author geekplus
     * @Description //获取数据库所有数据表
     * @Param
     * @Throws
     * @Return {@link }
     */
    @Override
    public List<TableInfo> getAllListTable() {
        return tableMapper.listAllTable();
    }

    /**
     * 根据表名查询数据表信息
     */
    @Override
    public TableInfo getTableInfoByName(String tableName) {
        return tableMapper.selectTableByName(tableName);
    }

    @Override
    public List<TableColumnInfo> getTablePkColumnList(String tableName) {
        return tableMapper.selectTablePkColumnList(tableName);
    }

    @Override
    public List<TableColumnInfo> getTableColumnList(String tableName) {
        return tableMapper.listTableColumn(tableName);
    }

    /**
     * 增加
     * @param tableInfo
     * @return 代码生成业务表
     */
    public Integer insertGenTable(TableInfo tableInfo){
        return tableMapper.insertGenTable(tableInfo);
    }

    /**
     * 批量增加
     * @param tableInfoList
     * @return 代码生成业务表
     */
    public Integer batchInsertGenTableList(List<TableInfo> tableInfoList){
        return tableMapper.batchInsertGenTableList(tableInfoList);
    }

    /**
     * 删除
     * @param tableId
     */
    public Integer deleteGenTableById(Long tableId){
        return tableMapper.deleteGenTableById(tableId);
    }

    /**
     * 批量删除
     */
    public Integer deleteGenTableByIds(Long[] tableIds){
        return tableMapper.deleteGenTableByIds(tableIds);
    }

    /**
     * 修改
     * @param tableInfo
     */
    public Integer updateGenTable(TableInfo tableInfo){
        return tableMapper.updateGenTable(tableInfo);
    }

    /**
     * 批量修改某几个字段
     * @param tableIds
     */
    public Integer batchUpdateGenTableList(Long[] tableIds){
        return tableMapper.batchUpdateGenTableList(tableIds);
    }

    /**
     * 查询全部
     */
    public List<TableInfo> selectGenTableList(TableInfo tableInfo){
        return tableMapper.selectGenTableList(tableInfo);
    }

    /**
     * 查询全部,用于联合查询，在此基础做自己的定制改动
     */
    public List<TableInfo> selectUnionGenTableList(TableInfo tableInfo){
        return tableMapper.selectUnionGenTableList(tableInfo);
    }

    /**
     * 根据Id查询单条数据
     */
    public TableInfo selectGenTableById(Long tableId){
        return tableMapper.selectGenTableById(tableId);
    }

    @Override
    public List<TableInfo> selectGenTableByIds(Long[] tableIds) {
        return tableMapper.selectGenTableByIds(tableIds);
    }

    @Override
    public List<Map<String,byte[]>> downloadCode(String tableName) {
        //使用字节流来合并模板,并且将文件输出流直接给压缩,多文件可以使用同一个流,只需要重置
        //ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        //ZipOutputStream zipOutputStream=new ZipOutputStream(outputStream);
        TableInfo tableInfo=getTableInfo(tableName);
        List<Map<String,byte[]>> byStreamTemplate= null;
        try {
            byStreamTemplate = CodeGenerateByTemplate.genOneCodeByStreamTemplate(tableInfo);
            //FileCompressUtils.downloadZipStream(response,byStreamTemplate,"geekplus");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return byStreamTemplate;
    }

    @Override
    public List<Map<String, byte[]>> downloadCodeByTable(TableInfo tableInfo) {
        TableInfo tbInfo=new TableInfo();
        tbInfo = getTableInfoByGenTable(tableInfo);
        List<Map<String,byte[]>> byStreamTemplate= null;
        try {
            byStreamTemplate = CodeGenerateByTemplate.genOneCodeByStreamTemplate(tbInfo);
            //FileCompressUtils.downloadZipStream(response,byStreamTemplate,"geekplus");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return byStreamTemplate;
    }

    @Override
    public List<Map<String, byte[]>> downloadCodeByTable(List<TableInfo> tableInfoList) {
        List<Map<String,byte[]>> byStreamTemplate= null;
        List<TableInfo> tableList=new ArrayList<>();
        for (TableInfo tableInfo:tableInfoList) {
            TableInfo tbInfo = getTableInfoByGenTable(tableInfo);
            tableList.add(tbInfo);
        }
        try {
            byStreamTemplate = CodeGenerateByTemplate.genCodeByStreamTemplate(tableList);
            //FileCompressUtils.downloadZipStream(response,byStreamTemplate,"geekplus");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return byStreamTemplate;
    }

    /**
     *清空表格所有数据 gen_table
     */
    @Override
    public void cleanTable() {
        tableMapper.cleanTable();
    }

    /**
     * @Author geekplus
     * @Description //直接查询数据库中表格信息，数据表格信息合成，information_schema
     */
    public TableInfo getTableInfo(String tableName){
        TableInfo tableInfo = new TableInfo();
        tableInfo=tableMapper.selectTableByName(tableName);;
        if(tableMapper.selectTablePkColumnList(tableName).size()>=1){
            tableInfo.setPkColumn(tableMapper.selectTablePkColumnList(tableName).get(0));
        }
        //tableInfo.setPkColumn(tableDao.selectColumnTableByPk(tableName).get(0));
        tableInfo.setAllColumns(tableMapper.listTableColumn(tableName));
        tableInfo.setCreateBy(tokenService.getSysUserName());
        //System.out.println("表的列详细数据 = [" + JSONObjectUtil.objectToJson(tableInfo.getAllColumns()) + "]");
        return CodeGenerateByTemplate.buildGenerateTableInfo(tableInfo);
    }

    /**
     * @Author geekplus
     * @Description //数据表格信息合成，gen_table
     */
    public TableInfo getTableInfoByGenTable(TableInfo tableInfo){
        String tableName=tableInfo.getTableName();
        if(tableMapper.selectTablePkColumnList(tableName).size()>=1){
            tableInfo.setPkColumn(tableMapper.selectTablePkColumnList(tableName).get(0));
        }
        //System.out.println("表的详细数据 = [" + JSONObjectUtil.objectToJson(tableInfo) + "]");
        //tableInfo.setPkColumn(tableDao.selectColumnTableByPk(tableName).get(0));
        tableInfo.setAllColumns(tableMapper.listTableColumn(tableName));
        //System.out.println("表的列详细数据 = [" + JSONObjectUtil.objectToJson(tableInfo.getAllColumns()) + "]");
        return tableInfo;
    }
}
