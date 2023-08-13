/**
 * author     : geekplus
 * email      : geekcjj@gmail.com
 * date       : 2/21/23 11:50
 * description: 做什么的？
 */
package com.geekplus.common.test;

import java.util.HashMap;
import java.util.Map;

public class VaribaleModify {
    int a=1;
    Map map=new HashMap<String,Object>();
    {
        map.put("b","1111");
    }
    void reset(){
        a=2;
        map.put("b","2222");
        System.out.printf("a="+a);
        System.out.printf("b="+map.get("b"));
    }

    public static void main(String[] args) {
        String str="画一个古风美女的画";
        VaribaleModify varibaleModify=new VaribaleModify();
        varibaleModify.reset();
        System.out.println(str.contains("画"));
    }
}
