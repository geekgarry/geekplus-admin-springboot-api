package com.geekplus.webapp.tool.generator.utils;

import com.geekplus.webapp.tool.generator.mapper.TableMapper;
import com.geekplus.webapp.tool.generator.entity.TableColumnInfo;
import com.geekplus.webapp.tool.generator.entity.TableInfo;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @program: spring-boot-api-project-seed
 * @description: 代码生成工具类
 * @author: GarryChan
 * @create: 2020-11-18 11:31
 **/
public class MybatisUtil {
    //factory实例化的过程是一个比较耗费性能的过程.
    //保证有且只有一个factory
    private static SqlSessionFactory factory;
    private static ThreadLocal<SqlSession> tl = new ThreadLocal<>();
    private static String CONFIG_PATH= "mybatis/mybatis.xml";

    static{
        try {
            InputStream is = Resources.getResourceAsStream(CONFIG_PATH);
            factory = new SqlSessionFactoryBuilder().build(is);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    /**
     * 获取SqlSession的方法
     */
    public static SqlSession getSqlSession(){
        SqlSession session = tl.get();
        if(session==null){
            tl.set(factory.openSession());
        }
        return tl.get();
    }

    /*
     * 获取数据库访问链接
     */
    public static SqlSession getSqlSession2() {
        SqlSession session = null;
        try {
            InputStream stream = Resources.getResourceAsStream(CONFIG_PATH);
            // 可以根据配置的相应环境读取相应的数据库环境
            // SqlSessionFactory factory = new SqlSessionFactoryBuilder().build(
            // stream, "development");
            SqlSessionFactory factory = new SqlSessionFactoryBuilder().build(stream);
            session = factory.openSession();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return session;
    }
    public static void closeSqlSession(SqlSession sqlSession){
        if(sqlSession!=null){
            sqlSession.close();
        }
        SqlSession session = tl.get();
        if(session!=null){
            session.close();
        }
        tl.set(null);
    }
    public static List<String> getTableNameList(){
        List<String> tableNameList = null;
        SqlSession sqlSession = MybatisUtil.getSqlSession();
        TableMapper tableMapper = sqlSession.getMapper(TableMapper.class);
        tableNameList= tableMapper.listTableName();
        MybatisUtil.closeSqlSession(null);
        return tableNameList;
    }
    //查询所有表
    public static List<TableInfo> getTableInfoList(String[] tableNames){
        List<TableInfo> tableList = null;
        SqlSession sqlSession = MybatisUtil.getSqlSession();
        TableMapper tableMapper = sqlSession.getMapper(TableMapper.class);
        if(tableNames!=null){
            tableList = tableMapper.listTableByNames(tableNames);
        }else {
            tableList= tableMapper.listTable();
        }
        MybatisUtil.closeSqlSession(null);
        return tableList;
    }
    public static List<TableInfo> getTableInfoList(){
        List<TableInfo> tableList = new ArrayList<>();
        SqlSession sqlSession = MybatisUtil.getSqlSession();
        TableMapper tableMapper = sqlSession.getMapper(TableMapper.class);
        List<String> tableNames= tableMapper.listTableName();
        for(String tableName:tableNames) {
            TableInfo tableInfo=null;
            tableInfo= tableMapper.selectTableByName(tableName);
            //System.out.println(tableDao.selectTableByName(tableName).getTableName());
            if(tableMapper.selectTablePkColumnList(tableName).size()>=1){
                tableInfo.setPkColumn(tableMapper.selectTablePkColumnList(tableName).get(0));
            }
            //tableInfo.setPkColumn(tableDao.selectColumnTableByPk(tableName).get(0));
            tableInfo.setAllColumns(tableMapper.listTableColumn(tableName));
            tableList.add(tableInfo);
        }
        MybatisUtil.closeSqlSession(null);
        return tableList;
    }
    public static TableInfo getTableByName(String tableName){
        TableInfo tableInfo = null;
        SqlSession sqlSession = MybatisUtil.getSqlSession();
        TableMapper tableMapper = sqlSession.getMapper(TableMapper.class);
        tableInfo = tableMapper.selectTableByName(tableName);
        //System.out.println(tableDao.selectTableByName(tableName));
        if(tableMapper.selectTablePkColumnList(tableName).size()>=1){
            tableInfo.setPkColumn(tableMapper.selectTablePkColumnList(tableName).get(0));
        }
        //tableInfo.setPkColumn(tableDao.selectColumnTableByPk(tableName).get(0));
        tableInfo.setAllColumns(tableMapper.listTableColumn(tableName));
        MybatisUtil.closeSqlSession(null);
        return tableInfo;
    }
    public static List<TableColumnInfo> getColumnTableList(String tableName){
        List<TableColumnInfo> tableList = null;
        SqlSession sqlSession = MybatisUtil.getSqlSession();
        TableMapper tableMapper = sqlSession.getMapper(TableMapper.class);
        tableList = tableMapper.listTableColumn(tableName);
        MybatisUtil.closeSqlSession(null);
        return tableList;
    }

    public static void main(String[] args) {
        //System.out.println("数据表的信息 = [tableList:" + getTableInfoList().get(0).getPkColumn().get(0).getJavaField() + "]");
        System.out.println("表的详细数据 = [" + JSONObjectUtil.objectToJson(getTableByName("gen_table")) + "]");
    }
}
