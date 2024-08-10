package com.geekplus.webapp.tool.generator.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.text.StringEscapeUtils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: spring-boot-project-mybatis
 * @description: JSON操作工具类
 * @author: GarryChan
 * @create: 2020-11-28 11:57
 **/
public class JSONObjectUtil {
    // 定义jackson对象
    private static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 将对象转换成json字符串。
     * <p>Title: pojoToJson</p>
     * <p>Description: </p>
     * @param data
     * @return
     */
    public static String objectToJson(Object data) {
        try {
            String string = objectMapper.writeValueAsString(data);
            return string;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将json结果集转化为对象
     * @param jsonData json数据
     * @param beanType 对象中的object类型
     * @param <T>
     * @return
     */
    public static <T> T jsonToPojo(String jsonData, Class<T> beanType) {
        try {
            T t = objectMapper.readValue(jsonData, beanType);
            return t;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将json结果集转化为对象
     * @param jsonData json数据
     * @return
     */
    public static Map jsonToMap(String jsonData) {
        //第一种方式
//        Map maps = (Map)JSON.parse(jsonData);
//        System.out.println("这个是用JSON类来解析JSON字符串!!!");
//        for (Object map : maps.entrySet()){
//            System.out.println(((Map.Entry)map).getKey()+"     " + ((Map.Entry)map).getValue());
//        }
//        //第二种方式
//        Map mapTypes = JSON.parseObject(jsonData);
//        System.out.println("这个是用JSON类的parseObject来解析JSON字符串!!!");
//        for (Object obj : mapTypes.keySet()){
//            System.out.println("key为："+obj+"值为："+mapTypes.get(obj));
//        }
//        //第三种方式
//        Map mapType = JSON.parseObject(jsonData,Map.class);
//        System.out.println("这个是用JSON类,指定解析类型，来解析JSON字符串!!!");
//        for (Object obj : mapType.keySet()){
//            System.out.println("key为："+obj+"值为："+mapType.get(obj));
//        }
//        //第四种方式
//        /**
//         * JSONObject是Map接口的一个实现类
//         */
//        Map json = (Map) JSONObject.parse(jsonData);
//        System.out.println("这个是用JSONObject类的parse方法来解析JSON字符串!!!");
//        for (Object map : json.entrySet()){
//            System.out.println(((Map.Entry)map).getKey()+"  "+((Map.Entry)map).getValue());
//        }
//        //第五种方式
//        /**
//         * JSONObject是Map接口的一个实现类
//         */
        JSONObject jsonObject = JSONObject.parseObject(jsonData);
//        System.out.println("这个是用JSONObject的parseObject方法来解析JSON字符串!!!");
//        for (Object map : json.entrySet()){
//            System.out.println(((Map.Entry)map).getKey()+"  "+((Map.Entry)map).getValue());
//        }
//        //第六种方式
//        /**
//         * JSONObject是Map接口的一个实现类
//         */
//        Map mapObj = JSONObject.parseObject(jsonData,Map.class);
//        System.out.println("这个是用JSONObject的parseObject方法并执行返回类型来解析JSON字符串!!!");
//        for (Object map: json.entrySet()){
//            System.out.println(((Map.Entry)map).getKey()+"  "+((Map.Entry)map).getValue());
//        }
        return jsonObject;
    }

    /**
     * 根据前端数据返回实体类对象
     * @return
     */
    public <T> T returnObject(String data, Class<T> ClassName){

        //现将前端数据格式化
        JSONObject dataFormat = dataFormat(data);

        Object javaObject = JSON.toJavaObject(dataFormat, ClassName);
        T cast = ClassName.cast(javaObject);
        return cast;
    }

    private JSONObject dataFormat(String data) {
        String info = StringEscapeUtils.unescapeHtml4(data);
        com.alibaba.fastjson.JSONObject parseObject = com.alibaba.fastjson.JSONObject.parseObject(info);
        return parseObject;
    }

    /**
     * 将json数据转换成pojo对象list
     * <p>Title: jsonToList</p>
     * <p>Description: </p>
     * @param jsonData
     * @param beanType
     * @return
     */
    public static <T>List<T> jsonToList(String jsonData, Class<T> beanType) {
        JavaType javaType = objectMapper.getTypeFactory().constructParametricType(List.class, beanType);
        try {
            List<T> list = objectMapper.readValue(jsonData, javaType);
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
    /**
     * 实体类转Map
     * @param object
     * @return
     */
    public static Map<String, Object> entityToMap(Object object) {
        Map<String, Object> map = new HashMap();
        for (Field field : object.getClass().getDeclaredFields()){
            try {
                boolean flag = field.isAccessible();
                field.setAccessible(true);
                Object o = field.get(object);
                map.put(field.getName(), o);
                field.setAccessible(flag);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return map;
    }

    /**
     * Map转实体类
     * @param map 需要初始化的数据，key字段必须与实体类的成员名字一样，否则赋值为空
     * @param entity 需要转化成的实体类
     * @return
     */
    public static <T> T mapToEntity(Map<String, Object> map, Class<T> entity) {
        T t = null;
        try {
            t = entity.newInstance();
            for(Field field : entity.getDeclaredFields()) {
                if (map.containsKey(field.getName())) {
                    boolean flag = field.isAccessible();
                    field.setAccessible(true);
                    Object object = map.get(field.getName());
                    if (object!= null && field.getType().isAssignableFrom(object.getClass())) {
                        field.set(t, object);
                    }
                    field.setAccessible(flag);
                }
            }
            return t;
        } catch (InstantiationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return t;
    }
}
