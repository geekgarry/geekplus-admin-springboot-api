package com.geekplus.common.constant;

/**
 * 项目常量
 */
public final class ProjectConstant {
    public static final String JAVA_TEMPLATE_PATH="/generator/template/java/";
    public static final String XML_TEMPLATE_PATH="/generator/template/xml/";
    public static final String VUE_TEMPLATE_PATH="/generator/template/vue/";
    public static final String JS_TEMPLATE_PATH="/generator/template/js/";
    public static final String SQL_TEMPLATE_PATH="/generator/sql/";
    public static final String HTML_TEMPLATE_PATH="/generator/template/html/";
    public static final String JAVA_PATH = "/src/main/java"; //java文件路径
    public static final String XML_PATH = "/src/main/resources/mybatis"; //java文件路径
    public static final String BASE_PACKAGE = "com.geekplus";//生成代码所在的基础包名称，可根据自己公司的项目修改（注意：这个配置修改之后需要手工修改src目录项目默认的包路径，使其保持一致，不然会找不到类）

    public static final String MODEL_PACKAGE = BASE_PACKAGE + ".webapp.function.entity";//生成的Model所在包
    public static final String MAPPER_PACKAGE = BASE_PACKAGE + ".webapp.function.mapper";//生成的Mapper所在包
    public static final String SERVICE_PACKAGE = BASE_PACKAGE + ".webapp.function.service";//生成的Service所在包
    public static final String SERVICE_IMPL_PACKAGE = SERVICE_PACKAGE + ".impl";//生成的ServiceImpl所在包
    public static final String CONTROLLER_PACKAGE = BASE_PACKAGE + ".webapp.function.controller";//生成的Controller所在包
    public static final String TYPE_DATE = "Date";
    public static final String TYPE_BIGDECIMAL = "BigDecimal";
    public static final String TYPE_BIGINTEGER = "BigInteger";

    //public static final String MAPPER_INTERFACE_REFERENCE = BASE_PACKAGE + ".core.Mapper";//Mapper插件基础接口的完全限定名

    public static void main(String[] args) {
        ProjectConstant projectConstant=new ProjectConstant();
        System.out.println(projectConstant.getPackName());
    }
    public String getPackName() {
        Package pack = getClass().getPackage();
        String packName = pack.getName();
        do{
            packName = packName.substring(0,packName.lastIndexOf("."));
            pack = Package.getPackage(packName);
        }while(null != pack);
        return packName;
    }
}
