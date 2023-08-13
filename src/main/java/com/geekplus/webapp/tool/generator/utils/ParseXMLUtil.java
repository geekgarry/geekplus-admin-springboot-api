package com.geekplus.webapp.tool.generator.utils;

import com.google.common.collect.Tables;
import com.zaxxer.hikari.HikariDataSource;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//import org.apache.tomcat.jdbc.pool.DataSource;

/**
 * @program: spring-boot-project-mybatis
 * @description: 解析XML工具
 * @author: GarryChan
 * @create: 2020-11-27 12:02
 **/
public class ParseXMLUtil {
    public static List<Map> mapList = new ArrayList<>();
//    public static DataSource dataSource = new DataSource();
    public static HikariDataSource dataSource = new HikariDataSource();
    public static List<Tables> tableList = new ArrayList<Tables>();
    static{
        parseXml("F:\\injavawetrust\\injavawetrust\\src\\main\\resources\\config.xml");
    }
    private static void parseXml(String xmlResource){
        try {
            SAXReader reader = new SAXReader();
            Document document = reader.read(new File(xmlResource));
            Element root = document.getRootElement();
            List<Element> elements = root.elements();
            for(Element element : elements){
                if("params".equals(element.getName())){
                    List<Element> paramElements = element.elements();
                    for(Element param : paramElements){
                        Map map=new HashMap();
                        if("osdir".equals(param.getName())){
                            map.put("osdir",param.attributeValue("value"));
                        }else if("javapackage".equals(param.getName())){
                            map.put("javapackage",param.attributeValue("value"));
                        }else if("author".equals(param.getName())){
                            map.put("author",param.attributeValue("value"));
                        }else if("project".equals(param.getName())){
                            map.put("project",param.attributeValue("value"));
                        }
                    }
                }else if("dataSource".equals(element.getName())){
                    List<Element> ele = element.elements();
                    for(Element e : ele){
                        if("driver".equals(e.getName())){
                            dataSource.setDriverClassName(e.attributeValue("value"));
                        }else if("url".equals(e.getName())){
                            dataSource.setJdbcUrl(e.attributeValue("value"));
                        }else if("username".equals(e.getName())){
                            dataSource.setUsername(e.attributeValue("value"));
                        }else if("password".equals(e.getName())){
                            dataSource.setPassword(e.attributeValue("value"));
                        }
                    }
                }
            }

        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }
    public static HikariDataSource getDataSource() {
        return dataSource;
    }
    public static void setDataSource(HikariDataSource dataSource) {
        ParseXMLUtil.dataSource = dataSource;
    }
    public static List<Tables> getTableList() {
        return tableList;
    }
    public static void setTableList(List<Tables> tableList) {
        ParseXMLUtil.tableList = tableList;
    }
}
