package com.geekplus.webapp.tool.generator;

import com.geekplus.common.constant.Constant;
import com.geekplus.common.constant.ProjectConstant;
import com.geekplus.webapp.tool.generator.entity.TableInfo;
import com.geekplus.webapp.tool.generator.utils.GenUtil;
import com.geekplus.webapp.tool.generator.utils.MybatisUtil;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @program: spring-boot-project-mybatis
 * @description: 根据模板生成代码，不用mybatis提供的生成方法
 * @author: GarryChan
 * @create: 2020-11-27 11:30
 **/
@Slf4j
public class CodeGenerateByTemplate {
    private static final String PROJECT_PATH = System.getProperty("user.dir");//项目在硬盘上的基础路径
    private static final String TEMPLATE_FILE_JAVA_PATH = PROJECT_PATH + "/src/main/resources/generator/template/java/";//模板位置
    private static final String TEMPLATE_FILE_XML_PATH = PROJECT_PATH + "/src/main/resources/generator/template/xml/";//模板位置
    private static final String TEMPLATE_FILE_VUE_PATH = PROJECT_PATH + "/src/main/resources/generator/template/vue/";//模板位置
    private static final String TEMPLATE_FILE_JS_PATH = PROJECT_PATH + "/src/main/resources/generator/template/js/";//模板位置
    private static final String JAVA_PATH = "/src/main/java"; //java文件路径
    private static final String RESOURCES_PATH = "/src/main/resources";//资源文件路径

    private static final String PACKAGE_PATH_MODEL = GenUtil.packageConvertPath(ProjectConstant.MODEL_PACKAGE);//生成的model存放路径
    private static final String PACKAGE_PATH_MAPPER = GenUtil.packageConvertPath(ProjectConstant.MAPPER_PACKAGE);//生成的mapper存放路径
    private static final String PACKAGE_PATH_SERVICE = GenUtil.packageConvertPath(ProjectConstant.SERVICE_PACKAGE);//生成的Service存放路径
    private static final String PACKAGE_PATH_SERVICE_IMPL = GenUtil.packageConvertPath(ProjectConstant.SERVICE_IMPL_PACKAGE);//生成的Service实现存放路径
    private static final String PACKAGE_PATH_CONTROLLER = GenUtil.packageConvertPath(ProjectConstant.CONTROLLER_PACKAGE);//生成的Controller存放路径

    private static final String AUTHOR = "CodeGenerator";//@author
    private static final String CURRENT_DATE = new SimpleDateFormat("yyyy/MM/dd").format(new Date());//@date
    private static final String DOWNLOAD_FILE_NAME="geekplus.zip";

    /**
     * 生成Java文件
     * @param fileName
     * @param templateName
     * @param map
     */
    public static void generateJavaFile(String fileName, String templateName, Map<String, Object> map){
        try {
            Configuration config = getJavaConfiguration();
            //config.setTemplateLoader(new ClassTemplateLoader(CodeGenerateByTemplate.class, "/"));
            try{
                BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName), "UTF-8"));
                Template template = config.getTemplate(templateName);
                template.process(map, out);
                out.close();
            }catch(Exception e){
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 生成XML文件
     * @param fileName
     * @param templateName
     * @param map
     */
    public static void generateXmlFile(String fileName, String templateName, Map<String, Object> map){
        try {
            Configuration config = getMapperXmlConfiguration();
            //config.setTemplateLoader(new ClassTemplateLoader(CodeGenerateByTemplate.class, "/"));
            try{
                BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName), "UTF-8"));
                Template template = config.getTemplate(templateName);
                template.process(map, out);
                out.close();
            }catch(Exception e){
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 生成VUE文件
     * @param fileName
     * @param templateName
     * @param map
     */
    public static void generateVueFile(String fileName, String templateName, Map<String, Object> map){
        try {
            Configuration config = getVueConfiguration();
            //config.setTemplateLoader(new ClassTemplateLoader(CodeGenerateByTemplate.class, "/"));
            try{
                BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName), "UTF-8"));
                Template template = config.getTemplate(templateName);
                template.process(map, out);
                out.close();
            }catch(Exception e){
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 生成VUE文件
     * @param fileName
     * @param templateName
     * @param map
     */
    public static void generateJsFile(String fileName, String templateName, Map<String, Object> map){
        try {
            Configuration config = getJsConfiguration();
            //config.setTemplateLoader(new ClassTemplateLoader(CodeGenerateByTemplate.class, "/"));
            try{
                BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName), "UTF-8"));
                Template template = config.getTemplate(templateName);
                template.process(map, out);
                out.close();
            }catch(Exception e){
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 生成代码文件
     * @param fileName
     * @param templateName
     * @param map
     */
    public static byte[] downloadCodeFile(String fileName, String templateName, Map<String, Object> map){
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            Configuration config = getMapperXmlConfiguration();
            //config.setTemplateLoader(new ClassTemplateLoader(CodeGenerateByTemplate.class, "/"));
            try{
                FileOutputStream fos = new FileOutputStream(fileName);
                BufferedWriter out = new BufferedWriter(new OutputStreamWriter(fos, "UTF-8"));
                Template template = config.getTemplate(templateName);
                template.process(map, out);
                // 添加一个新的ZipEntry到压缩包中
                ZipEntry zipEntry = new ZipEntry(GenUtil.getGenerateFileName(fileName, map));
                // 创建压缩文件
                ZipOutputStream zipOut = new ZipOutputStream(outputStream);
                zipOut.putNextEntry(zipEntry);
                zipOut.write(out.toString().getBytes(Constant.UTF8));
                zipOut.closeEntry();
                zipOut.close();
                out.close();
            }catch(Exception e){
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return outputStream.toByteArray();
    }

    public static List<String[]> getTemplateList(){
        List<String[]> templates = new ArrayList<String[]>();
        //1.mapper 这些是创建生成文件的目录
        String mapperPath=PROJECT_PATH+JAVA_PATH+PACKAGE_PATH_MAPPER;
        //createDir(mapperPath);
        templates.add(new String[]{mapperPath,"Mapper.java","mapper.ftl"});
        //2.service
        String servicePath=PROJECT_PATH+JAVA_PATH+PACKAGE_PATH_SERVICE;
        //createDir(servicePath);
        templates.add(new String[]{servicePath,"Service.java","service.ftl"});
        //3.serviceImpl
        String serviceImplPath=PROJECT_PATH+JAVA_PATH+PACKAGE_PATH_SERVICE_IMPL;
        //createDir(serviceImplPath);
        templates.add(new String[]{serviceImplPath,"ServiceImpl.java","service-impl.ftl"});
        //4.entity
        String entityPath=PROJECT_PATH+JAVA_PATH+PACKAGE_PATH_MODEL;
        //createDir(entityPath);
        templates.add(new String[]{entityPath,".java","entity.ftl"});
        //5.controller
        String controllerPath=PROJECT_PATH+JAVA_PATH+PACKAGE_PATH_CONTROLLER;
        //createDir(controllerPath);
        templates.add(new String[]{controllerPath,"Controller.java","controller.ftl"});
        //6.mapperXml
        String mapperXMLPath=PROJECT_PATH+RESOURCES_PATH+"/mybatis/system";
        //createDir(mapperXMLPath);
        templates.add(new String[]{mapperXMLPath,"Mapper.xml","mapper-xml.ftl"});
        return templates;
    }

    /**
     * 创建文件目录
     * @param path
     */
    public static void createDir(String path){
        if(null != path && !"".equals(path)){
            File file = new File(path);
            file.mkdirs();
        }
    }
//    public static void initDirName(){
//        //1.mapper
//        //String poDir = params.getOsdir() + File.separatorChar + "po";
//        String mapperPath=PROJECT_PATH+JAVA_PATH+PACKAGE_PATH_MAPPER;
//        createDir(mapperPath);
//        //2.service
//        String servicePath=PROJECT_PATH+JAVA_PATH+PACKAGE_PATH_SERVICE;
//        createDir(servicePath);
//        //3.serviceImpl
//        String serviceImplPath=PROJECT_PATH+JAVA_PATH+PACKAGE_PATH_SERVICE_IMPL;
//        createDir(serviceImplPath);
//        //4.model
//        String modelPath=PROJECT_PATH+JAVA_PATH+PACKAGE_PATH_MODEL;
//        createDir(modelPath);
//        //5.controller
//        String controllerPath=PROJECT_PATH+JAVA_PATH+PACKAGE_PATH_CONTROLLER;
//        createDir(controllerPath);
//        //6.xml
//        String mapperXMLPath=PROJECT_PATH+RESOURCES_PATH+"/mapper";
//        createDir(mapperXMLPath);
//    }
    /**
     * <p>
     * 读取控制台内容
     * </p>
     */
    public static String scanner(String tip) {
        Scanner scanner = new Scanner(System.in);
        StringBuilder help = new StringBuilder();
        // String result=null;
        help.append("请输入" + tip + "：");
        System.out.println(help.toString());
        if (scanner.hasNext()) {
            String ipt = scanner.next();
            if (StringUtils.isNotEmpty(ipt)) {
                return ipt;
            }
            scanner.close();
            // return result;
        }
        try {
            throw new Exception("请输入正确的" + tip + "！");
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    /**
     * 生成Java代码
     * @param table
     */
    public static void genCodeFileByTemplate(TableInfo table) {
        String javaClassName = GenUtil.capitaFirstLetter(GenUtil
                .mapTableNameToVarName(table.getTableName()));
        Map<String, Object> map = new HashMap<>();
        String tableComment=null;
        if(table.getTableComment()!=null||table.getTableComment()!=""){
            tableComment=table.getTableComment();
        } else if(table.getTableComment()==""||table.getTableComment()!=null){
            tableComment="【请填写功能的名称或完成的事项】";
        }
        map.put("tableName",table.getTableName());
        map.put("tableAlias", GenUtil.getFirstChar(table.getTableName()));
        map.put("title", tableComment);
        map.put("author", AUTHOR);
        map.put("date", CURRENT_DATE);
        map.put("basePackage", ProjectConstant.BASE_PACKAGE);
        map.put("baseRequestMapping", GenUtil.getRequestMapping(table.getTableName()));
        map.put("modelNameUpperCamel", javaClassName);
        map.put("modelNameLowerCamel", GenUtil
                .mapTableNameToVarName(table.getTableName()));
        map.put("functionName", tableComment);
        map.put("pkColumn",table.getPkColumn());
        map.put("allColumn",table.getAllColumns());
        map.put("allColumnCount",table.getAllColumns().size());
        map.put("importList",GenUtil.getImportList(table.getAllColumns()));
        //1.mapper 这些是创建生成文件的目录
        String mapperPath=PROJECT_PATH+JAVA_PATH+PACKAGE_PATH_MAPPER;
        createDir(mapperPath);
        //2.service
        String servicePath=PROJECT_PATH+JAVA_PATH+PACKAGE_PATH_SERVICE;
        createDir(servicePath);
        //3.serviceImpl
        String serviceImplPath=PROJECT_PATH+JAVA_PATH+PACKAGE_PATH_SERVICE_IMPL;
        createDir(serviceImplPath);
        //4.model
        String modelPath=PROJECT_PATH+JAVA_PATH+PACKAGE_PATH_MODEL;
        createDir(modelPath);
        //5.controller
        String controllerPath=PROJECT_PATH+JAVA_PATH+PACKAGE_PATH_CONTROLLER;
        createDir(controllerPath);
        //6.mapperXml
        String mapperXMLPath=PROJECT_PATH+RESOURCES_PATH+"/mybatis/function";
        createDir(mapperXMLPath);

        // 1.mapper 这些是生成代码文件
        String mapperName = mapperPath + File.separatorChar + javaClassName + "Mapper.java";
        generateJavaFile(mapperName, "mapper.ftl", map);
        //downloadJavaFile(mapperName, "mapper.ftl", map);
        // 2.service
        String serviceName = servicePath + File.separatorChar + javaClassName + "Service.java";
        generateJavaFile(serviceName, "service.ftl", map);
        //downloadJavaFile(serviceName, "service.ftl", map);
        // 3.serviceImpl
        String serviceImplName = serviceImplPath + File.separatorChar + javaClassName + "ServiceImpl.java";
        generateJavaFile(serviceImplName, "service-impl.ftl", map);
        //downloadJavaFile(serviceImplName, "service-impl.ftl", map);
        // 4.model
        String modelName = modelPath + File.separatorChar + javaClassName + ".java";
        generateJavaFile(modelName, "entity.ftl", map);
        //downloadJavaFile(modelName, "entity.ftl", map);
        // 5.controller
        String controllerName = controllerPath + File.separatorChar + javaClassName + "Controller.java";
        generateJavaFile(controllerName, "controller.ftl", map);
        //downloadJavaFile(controllerName, "controller.ftl", map);
        // 6.mapperXml
        String mapperXMLName = mapperXMLPath + File.separatorChar + javaClassName + "Mapper.xml";
        generateXmlFile(mapperXMLName, "mapper-xml.ftl", map);
    }

    /**
     * 生成代码
     * @param table
     */
    public static void genCodeByTemplate(HttpServletResponse response, TableInfo table, ByteArrayOutputStream outputStream) throws Exception {
        String javaClassName = GenUtil.capitaFirstLetter(GenUtil
                .mapTableNameToVarName(table.getTableName()));
        Map<String, Object> map = new HashMap<>();
        String tableComment=null;
        if(table.getTableComment()!=null||table.getTableComment()!=""){
            tableComment=table.getTableComment();
        } else if(table.getTableComment()==""||table.getTableComment()!=null){
            tableComment="【请填写功能的名称或完成的事项】";
        }
        map.put("tableName",table.getTableName());
        map.put("tableAlias", GenUtil.getFirstChar(table.getTableName()));
        map.put("title", tableComment);
        map.put("author", AUTHOR);
        map.put("date", CURRENT_DATE);
        map.put("basePackage", ProjectConstant.BASE_PACKAGE);
        map.put("baseRequestMapping", GenUtil.getRequestMapping(table.getTableName()));
        map.put("modelNameUpperCamel", javaClassName);
        map.put("modelNameLowerCamel", GenUtil
                .mapTableNameToVarName(table.getTableName()));
        map.put("functionName", tableComment);
        map.put("pkColumn",table.getPkColumn());
        map.put("allColumn",table.getAllColumns());
        map.put("allColumnCount",table.getAllColumns().size());
        map.put("importList",GenUtil.getImportList(table.getAllColumns()));
        //将流转为字节数组,方便压缩输出
        List<Map<String, byte[]>> compressByteList = new ArrayList<>();
        // 获取模板列表
        List<String[]> templates=getTemplateList();
        for (String[] templateItem : templates)
        {
            //组建压缩信息交给压缩工具类,这里可以使用一个对象,为了简单使用了map
            Map<String, byte[]> compressByte = new HashMap<>();
            //createDir(templateItem[0]);
            // codeFileName 这些是生成代码文件
            String codeFileName = templateItem[0] + File.separatorChar + javaClassName + templateItem[1];
            //downloadCodeFile(codeFileName, "mapper.ftl", map);
            //ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            try {
                Configuration config;
                if(templateItem[2].equals("mapper-xml.ftl")) {
                    config = getMapperXmlConfiguration();
                }else{
                    config = getJavaConfiguration();
                }
                /**
                 * 加载模板
                 */
                File file=new File(codeFileName);
                //config.setTemplateLoader(new ClassTemplateLoader(CodeGenerateByTemplate.class, "/"));
                try{
                    OutputStreamWriter writer = new OutputStreamWriter(outputStream);
//                    FileOutputStream fos = new FileOutputStream(file);
//                    OutputStreamWriter osw= new OutputStreamWriter(fos, "UTF-8");
//                    BufferedWriter out = new BufferedWriter(osw);
                    Template template = config.getTemplate(templateItem[2]);
                    template.process(map, writer);
                    compressByte.put(codeFileName,outputStream.toByteArray());
                    compressByteList.add(compressByte);
//                    int num=0;
//                    InputStream inputStream = new FileInputStream(file);
//                    byte[] buffer = new byte[inputStream.available()+1024];
                    /**
                     * 将数据读取到缓冲区中，再将缓冲区中数据传输出去
                     */
//                    while((num=inputStream.read())!=-1){
//                        zip.write(buffer,0,num);
//                    }
                    outputStream.reset();
                    writer.flush();
                    writer.close();
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
            catch (IOException e)
            {
                log.error("渲染模板失败，表名：" + table.getTableName(), e);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //FileCompressUtils.downloadZipStream(response,compressByteList,"geekplus");
    }

    /**
     * 生成单个表代码
     * @param table
     */
    public static List<Map<String,byte[]>> genOneCodeByStreamTemplate(TableInfo table) {
        List<Map<String, byte[]>> compressByteList = new ArrayList<>();
        //使用字节流来合并模板,并且将文件输出流直接给压缩,多文件可以使用同一个流,只需要重置
        //ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        //OutputStreamWriter writer = new OutputStreamWriter(outputStream);
        getTemplateCodeStream(table,compressByteList);
        return compressByteList;
    }

    /**
     * 生成多个表的代码
     * @param tableList
     */
    public static List<Map<String,byte[]>> genCodeByStreamTemplate(List<TableInfo> tableList) {
        List<Map<String, byte[]>> compressByteList = new ArrayList<>();
        //使用字节流来合并模板,并且将文件输出流直接给压缩,多文件可以使用同一个流,只需要重置
        //ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        //OutputStreamWriter writer = new OutputStreamWriter(outputStream);
        for (TableInfo table:tableList) {
            getTemplateCodeStream(table,compressByteList);
        }
        return compressByteList;
    }

    //获取模版生成的代码的字节map集合
    public static void getTemplateCodeStream(TableInfo table,List<Map<String, byte[]>> compressByteList){
        //使用字节流来合并模板,并且将文件输出流直接给压缩,多文件可以使用同一个流,只需要重置
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        OutputStreamWriter writer = new OutputStreamWriter(outputStream);
        String javaClassName=table.getModelName();
        String vueJsFileName=table.getBusinessName();//业务名，也是vueName
        String moduleName=table.getModuleName();//模块名
        String fileS=File.separator;
        Map<String,Object> templateObject=getTemplateObject(table);
        //1.mapper 这些是创建生成文件的目录PROJECT_PATH+
        String mapperPath=JAVA_PATH+GenUtil.packageConvertPath(table.getPackageName()+".mapper");
        //createDir(mapperPath);
        //2.service
        String servicePath=JAVA_PATH+GenUtil.packageConvertPath(table.getPackageName()+".service");
        //3.serviceImpl
        String serviceImplPath=JAVA_PATH+GenUtil.packageConvertPath(table.getPackageName()+".service.impl");
        //4.model
        String modelPath=JAVA_PATH+GenUtil.packageConvertPath(table.getPackageName()+".entity");
        //5.controller
        String controllerPath=JAVA_PATH+GenUtil.packageConvertPath(table.getPackageName()+".controller");
        //6.mapperXml
        String mapperXMLPath=RESOURCES_PATH+"/mybatis/"+moduleName;
        //7.vue文件
        String vuePath="/views/"+moduleName;
        //8.vue.js文件
        String vueJsPath="/api/"+moduleName;
        //9.菜单权限sql文件
        String sqlPath="/sql/"+moduleName;
        //将流转为字节数组,方便压缩输出
        try{
            Configuration configJava = getJavaConfiguration();
            Configuration configXml = getMapperXmlConfiguration();
            Configuration configVue = getVueConfiguration();
            Configuration configJs = getJsConfiguration();
            Configuration configSql = getSqlConfiguration();
            // 1.mapper 这些是生成代码文件
            String mapperName = mapperPath + File.separatorChar + javaClassName + "Mapper.java";
            Template template1 = configJava.getTemplate("mapper.ftl");
            template1.process(templateObject, writer);
            byte[] bytes1=outputStream.toByteArray();
            //组建压缩信息交给压缩工具类,这里可以使用一个对象,为了简单使用了map
            Map<String, byte[]> compressByte1 = new HashMap<>();
            compressByte1.put(mapperName,bytes1);
            compressByteList.add(compressByte1);
            outputStream.reset();
            writer.flush();
            // 2.service
            String serviceName = servicePath + File.separatorChar + javaClassName + "Service.java";
            Template template2 = configJava.getTemplate("service.ftl");
            template2.process(templateObject, writer);
            byte[] bytes2=outputStream.toByteArray();
            //组建压缩信息交给压缩工具类,这里可以使用一个对象,为了简单使用了map
            Map<String, byte[]> compressByte2 = new HashMap<>();
            compressByte2.put(serviceName,bytes2);
            compressByteList.add(compressByte2);
            outputStream.reset();
            writer.flush();
            // 3.serviceImpl
            String serviceImplName = serviceImplPath + File.separatorChar + javaClassName + "ServiceImpl.java";
            Template template3 = configJava.getTemplate("service-impl.ftl");
            template3.process(templateObject, writer);
            byte[] bytes3=outputStream.toByteArray();
            //组建压缩信息交给压缩工具类,这里可以使用一个对象,为了简单使用了map
            Map<String, byte[]> compressByte3 = new HashMap<>();
            compressByte3.put(serviceImplName,bytes3);
            compressByteList.add(compressByte3);
            outputStream.reset();
            writer.flush();
            // 4.model
            String modelName = modelPath + File.separatorChar + javaClassName + ".java";
            Template template4 = configJava.getTemplate("entity.ftl");
            template4.process(templateObject, writer);
            byte[] bytes4=outputStream.toByteArray();
            //组建压缩信息交给压缩工具类,这里可以使用一个对象,为了简单使用了map
            Map<String, byte[]> compressByte4 = new HashMap<>();
            compressByte4.put(modelName,bytes4);
            compressByteList.add(compressByte4);
            outputStream.reset();
            writer.flush();
            // 5.controller
            String controllerName = controllerPath + File.separatorChar + javaClassName + "Controller.java";
            Template template5 = configJava.getTemplate("controller.ftl");
            template5.process(templateObject, writer);
            byte[] bytes5=outputStream.toByteArray();
            //组建压缩信息交给压缩工具类,这里可以使用一个对象,为了简单使用了map
            Map<String, byte[]> compressByte5 = new HashMap<>();
            compressByte5.put(controllerName,bytes5);
            compressByteList.add(compressByte5);
            outputStream.reset();
            writer.flush();
            // 6.mapperXml
            String mapperXMLName = mapperXMLPath + File.separatorChar + javaClassName + "Mapper.xml";
            /**
             * 加载模板
             */
            Template template6 = configXml.getTemplate("mapper-xml.ftl");
            template6.process(templateObject, writer);
            /**
             * 将数据读取到缓冲区中，再将缓冲区中数据传输出去
             */
            byte[] bytes6=outputStream.toByteArray();
            //组建压缩信息交给压缩工具类,这里可以使用一个对象,为了简单使用了map
            Map<String, byte[]> compressByte6 = new HashMap<>();
            compressByte6.put(mapperXMLName,bytes6);
            compressByteList.add(compressByte6);
            outputStream.reset();
            writer.flush();
            // 7.Vue页面
            String vueViewsName = vuePath + File.separatorChar + vueJsFileName + File.separatorChar+ "index.vue";
            /**
             * 加载模板
             */
            Template template7 = configVue.getTemplate("vue.ftl");
            template7.process(templateObject, writer);
            /**
             * 将数据读取到缓冲区中，再将缓冲区中数据传输出去
             */
            byte[] bytes7=outputStream.toByteArray();
            //组建压缩信息交给压缩工具类,这里可以使用一个对象,为了简单使用了map
            Map<String, byte[]> compressByte7 = new HashMap<>();
            compressByte7.put(vueViewsName,bytes7);
            compressByteList.add(compressByte7);
            outputStream.reset();
            writer.flush();
            // 8.Vue的Js文件
            String vueJsName = vueJsPath + File.separatorChar + vueJsFileName + ".js";
            /**
             * 加载模板
             */
            Template template8 = configJs.getTemplate("vue-js.ftl");
            template8.process(templateObject, writer);
            /**
             * 将数据读取到缓冲区中，再将缓冲区中数据传输出去
             */
            byte[] bytes8=outputStream.toByteArray();
            //组建压缩信息交给压缩工具类,这里可以使用一个对象,为了简单使用了map
            Map<String, byte[]> compressByte8 = new HashMap<>();
            compressByte8.put(vueJsName,bytes8);
            compressByteList.add(compressByte8);
            outputStream.reset();
            writer.flush();
            // 9.菜单权限sql文件
            String sqlFileName = sqlPath + File.separatorChar + vueJsFileName + ".sql";
            /**
             * 加载模板
             */
            Template template9 = configSql.getTemplate("sql.ftl");
            template9.process(templateObject, writer);
            /**
             * 将数据读取到缓冲区中，再将缓冲区中数据传输出去
             */
            byte[] bytes9=outputStream.toByteArray();
            //组建压缩信息交给压缩工具类,这里可以使用一个对象,为了简单使用了map
            Map<String, byte[]> compressByte9 = new HashMap<>();
            compressByte9.put(sqlFileName,bytes9);
            compressByteList.add(compressByte9);
            outputStream.reset();
            //FileCompressUtils.downloadZipStream(response,compressByteList,"geekplus");
            //FileCompressUtils.genCode(response,compressByteList);
            writer.close();
        }catch (Exception e){
            log.error("渲染模板失败，表名：" + table.getTableName());
            e.printStackTrace();
        }
    }

    public static Map<String,Object> getTemplateObject(TableInfo table){
        String javaClassName = GenUtil.capitaFirstLetter(GenUtil
                .mapTableNameToVarName(table.getTableName()));
        String vueComponentName = GenUtil.mapTableNameToVueJSBusinessName(table.getTableName());
        Map<String, Object> map = new HashMap<>();
        String tableComment=null;
        if(table.getTableComment()!=null||table.getTableComment()!=""){
            tableComment=table.getTableComment();
        } else if(table.getTableComment()==""||table.getTableComment()!=null){
            tableComment="[功能名称]";
        }
        map.put("componentName",vueComponentName);
        map.put("moduleName",table.getModuleName());//模块名
        map.put("className",table.getClassName());
        map.put("jsMethodName",table.getBusinessName());
        map.put("businessName",table.getBusinessName());
        map.put("permissionPrefix",table.getModuleName()+':'+table.getBusinessName());
        map.put("tableName",table.getTableName());
        map.put("tableAlias", GenUtil.getFirstChar(table.getTableName()));
        map.put("title", tableComment);
        map.put("author", table.getFunctionAuthor());
        map.put("date", new SimpleDateFormat("yyyy/MM/dd").format(table.getCreateTime()));
        map.put("basePackage", table.getBasePackageName());
        map.put("baseRequestMapping", GenUtil.getRequestMapping(table.getTableName()));
        map.put("modelNameUpperCamel", table.getModelName());
        map.put("modelNameLowerCamel", GenUtil
                .mapTableNameToVarName(table.getTableName()));
        map.put("functionName", tableComment);
        map.put("pkColumn",table.getPkColumn());
        map.put("allColumn",table.getAllColumns());
        map.put("allColumnCount",table.getAllColumns().size());
        map.put("importList",GenUtil.getImportList(table.getAllColumns()));
        return map;
    }

    public static TableInfo buildGenerateTableInfo(TableInfo tableInfo){
        String javaClassName = GenUtil.capitaFirstLetter(GenUtil
                .mapTableNameToVarName(tableInfo.getTableName()));
        String modelName= GenUtil
                .mapTableNameToVarName(tableInfo.getTableName());
        if(tableInfo.getTableComment()==null||("").equals(tableInfo.getTableComment())){
            tableInfo.setTableComment("【请填写功能的名称或完成的事项】");
        }
        tableInfo.setBasePackageName(ProjectConstant.BASE_PACKAGE);
        tableInfo.setPackageName(ProjectConstant.BASE_PACKAGE+".webapp.function");
        tableInfo.setClassName(javaClassName);
        tableInfo.setFunctionAuthor(AUTHOR);
        tableInfo.setFunctionName(tableInfo.getTableComment());
        tableInfo.setBusinessName(GenUtil.capitaFirstLowerCase(GenUtil.
                mapTableNameToVueJSBusinessName(tableInfo.getTableName())));
        tableInfo.setModelName(javaClassName);
        tableInfo.setTplCategory("crud");
        tableInfo.setModuleName("function");
        tableInfo.setCreateTime(new Date());
        return tableInfo;
    }
    /**
     * 生成代码
     */
    public static void generateCode() {
        // 生成全部文件
        List<TableInfo> tables = MybatisUtil.getTableInfoList();
        for (TableInfo table : tables) {
            genCodeFileByTemplate(table);
        }
    }

    /**
     * 生成代码
     */
    public static void generateCode(String tableName) {
        TableInfo table=MybatisUtil.getTableByName(tableName);
        genCodeFileByTemplate(table);
    }

    /**
     * 生成代码
     */
    public static void generateCode(String[] tableNames) {
        for(String tableName:tableNames) {
            TableInfo table = MybatisUtil.getTableByName(tableName);
            genCodeFileByTemplate(table);
        }
    }

    public static void main(String[] args) {
        generateCode(scanner("要操作的表名"));
        //generateCode();
    }

    private static Configuration getJavaConfiguration() throws IOException {
        Configuration cfg = new Configuration(Configuration.VERSION_2_3_32);
        //如何获取resource下的文件？【注意：即使打成jar包也得有效】
//        ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
//        Resource resources = resourcePatternResolver.getResource("classpath:"+ProjectConstant.JAVA_TEMPLATE_PATH);
        //基于文件系统new File(TEMPLATE_FILE_JAVA_PATH)
//        cfg.setDirectoryForTemplateLoading(resources.getFile());
        //类路径的方法其实这个方法是根据类加载路径来判断的，最终会执行以下代码：CodeGenerateByTemplate.class.getClassLoader().getResource("/generator/template/java/");
        cfg.setClassForTemplateLoading(CodeGenerateByTemplate.class,ProjectConstant.JAVA_TEMPLATE_PATH);
        cfg.setDefaultEncoding("UTF-8");
        //cfg.setTemplateExceptionHandler(TemplateExceptionHandler.IGNORE_HANDLER);
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        cfg.setCacheStorage(new freemarker.cache.MruCacheStorage(20, 250));
        return cfg;
    }

    private static Configuration getMapperXmlConfiguration() throws IOException {
        Configuration cfg = new Configuration(Configuration.VERSION_2_3_32);
        //如何获取resource下的文件？【注意：即使打成jar包也得有效】
//        ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
//        Resource resources = resourcePatternResolver.getResource("classpath:"+ProjectConstant.XML_TEMPLATE_PATH);
        //基于文件系统new File(TEMPLATE_FILE_XML_PATH)
//        cfg.setDirectoryForTemplateLoading(resources.getFile());
        //类路径的方法其实这个方法是根据类加载路径来判断的，最终会执行以下代码：CodeGenerateByTemplate.class.getClassLoader().getResource("/generator/template/xml/");
        cfg.setClassForTemplateLoading(CodeGenerateByTemplate.class,ProjectConstant.XML_TEMPLATE_PATH);
        cfg.setDefaultEncoding("UTF-8");
        //cfg.setTemplateExceptionHandler(TemplateExceptionHandler.IGNORE_HANDLER);
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        cfg.setCacheStorage(new freemarker.cache.MruCacheStorage(20, 250));
        return cfg;
    }

    private static Configuration getVueConfiguration() throws IOException {
        Configuration cfg = new Configuration(Configuration.VERSION_2_3_32);
        //如何获取resource下的文件？【注意：即使打成jar包也得有效】
//        ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
//        Resource resources = resourcePatternResolver.getResource("classpath:"+ProjectConstant.VUE_TEMPLATE_PATH);
        //基于文件系统new File(TEMPLATE_FILE_VUE_PATH)
//        cfg.setDirectoryForTemplateLoading(resources.getFile());
        //类路径的方法其实这个方法是根据类加载路径来判断的，最终会执行以下代码：CodeGenerateByTemplate.class.getClassLoader().getResource("/generator/template/xml/");
        cfg.setClassForTemplateLoading(CodeGenerateByTemplate.class,ProjectConstant.VUE_TEMPLATE_PATH);
        cfg.setDefaultEncoding("UTF-8");
        //cfg.setTemplateExceptionHandler(TemplateExceptionHandler.IGNORE_HANDLER);
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        cfg.setCacheStorage(new freemarker.cache.MruCacheStorage(20, 250));
        return cfg;
    }

    private static Configuration getJsConfiguration() throws IOException {
        Configuration cfg = new Configuration(Configuration.VERSION_2_3_32);
        //如何获取resource下的文件？【注意：即使打成jar包也得有效】
//        ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
//        Resource resources = resourcePatternResolver.getResource("classpath:"+ProjectConstant.JS_TEMPLATE_PATH);
        //基于文件系统new File(TEMPLATE_FILE_JS_PATH)
//        cfg.setDirectoryForTemplateLoading(resources.getFile());
        //类路径的方法其实这个方法是根据类加载路径来判断的，最终会执行以下代码：CodeGenerateByTemplate.class.getClassLoader().getResource("/generator/template/xml/");
        cfg.setClassForTemplateLoading(CodeGenerateByTemplate.class,ProjectConstant.JS_TEMPLATE_PATH);
        cfg.setDefaultEncoding("UTF-8");
        //cfg.setTemplateExceptionHandler(TemplateExceptionHandler.IGNORE_HANDLER);
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        cfg.setCacheStorage(new freemarker.cache.MruCacheStorage(20, 250));
        return cfg;
    }

    private static Configuration getSqlConfiguration() throws IOException {
        Configuration cfg = new Configuration(Configuration.VERSION_2_3_32);
        //如何获取resource下的文件？【注意：即使打成jar包也得有效】
//        ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
//        Resource resources = resourcePatternResolver.getResource("classpath:"+ProjectConstant.JS_TEMPLATE_PATH);
        //基于文件系统new File(TEMPLATE_FILE_JS_PATH)
//        cfg.setDirectoryForTemplateLoading(resources.getFile());
        //类路径的方法其实这个方法是根据类加载路径来判断的，最终会执行以下代码：CodeGenerateByTemplate.class.getClassLoader().getResource("/generator/template/xml/");
        cfg.setClassForTemplateLoading(CodeGenerateByTemplate.class,ProjectConstant.SQL_TEMPLATE_PATH);
        cfg.setDefaultEncoding("UTF-8");
        //cfg.setTemplateExceptionHandler(TemplateExceptionHandler.IGNORE_HANDLER);
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        cfg.setCacheStorage(new freemarker.cache.MruCacheStorage(20, 250));
        return cfg;
    }
}
