package com.geekplus.common.util.poi;

import com.geekplus.common.util.json.JsonEscapeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.List;

/**
 * @program: tdqqxt
 * @description: 树形结构数据
 * @author: GarryChan
 * @create: 2020-12-21 14:48
 **/
public class TestTreePoi {
  static Logger logger= LoggerFactory.getLogger(TestTreePoi.class);

  public static void main(String[] args) {
    String path="C:/Users/GEEKCJJ/Documents/WeChat Files/wxid_v6an8sk9ytsu22/FileStorage/File/2020-12/三组.xls";
    List<?> list=AreaTreeExcelUtil.readTreeExcelData(path);
//    try {
//      readTreeExcel(path);
//    } catch (IOException e) {
//      e.printStackTrace();
//    }
    System.out.println("要导入的excel数据"+ JsonEscapeUtil.beanToJson(list));
    //saveJsonFile(list,"E:/home/tdxx.json");
  }
  public static void saveJsonFile(List<?> list, String fileName){
    try {
      // 保证创建一个新文件
      File file = new File(fileName);
      if (!file.getParentFile().exists())
      { // 如果父目录不存在，创建父目录
        file.getParentFile().mkdirs();
      }
      if (file.exists()) { // 如果已存在,删除旧文件
        file.delete();
      }
      file.createNewFile();
      String jsonString= JsonEscapeUtil.beanToJson(list);
      // 将格式化后的字符串写入文件
      Writer write = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");
      write.write(jsonString);
      write.flush();
      write.close();
    } catch (IOException e) {
      e.printStackTrace();
    }

  }
}
