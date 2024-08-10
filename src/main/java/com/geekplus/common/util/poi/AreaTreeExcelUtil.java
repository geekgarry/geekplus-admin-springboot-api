package com.geekplus.common.util.poi;

import com.github.pagehelper.util.StringUtil;
import com.geekplus.common.util.string.StringUtils;
import com.geekplus.common.util.json.JsonEscapeUtil;
//import org.apache.poi.hssf.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.*;

import static org.apache.poi.ss.usermodel.CellType.BOOLEAN;

/**
 * @program: tdqqxt
 * @description: 发包方承包方土地信息树形excel数据提取
 * @author: GarryChan
 * @create: 2020-12-25 17:22
 **/
public class AreaTreeExcelUtil {
  static Logger logger= LoggerFactory.getLogger(TestTreePoi.class);
  /**
   * 工作薄对象
   */
  private static Workbook wb;
  //测试，通过path
  public static List<?> readTreeExcelData(String path){
    List<?> list=new ArrayList<>();
    File file = null;
    InputStream input = null;
    if (path != null && path.length() > 7) {
      // 判断文件是否是Excel(2003、2007)
      String suffix = path.substring(path.lastIndexOf("."), path.length());
      file = new File(path);
      try {
        input = new FileInputStream(file);
        wb = WorkbookFactory.create(input);
      } catch (IOException e) {
        System.out.println("未找到指定的文件！");
      }
    }
    return getContent(wb.getSheetAt(1));
  }
  //根据file
  public static List<Map> readTreeExcelData(InputStream input){
//    /List<Map> list=new ArrayList<>();
    //File file = null;
    //InputStream input = null;
//    if (path != null && path.length() > 7) {
//      // 判断文件是否是Excel(2003、2007)
//      String suffix = path.substring(path.lastIndexOf("."), path.length());
//      file = new File(path);
//      try {
//        input = new FileInputStream(file);
//        wb = WorkbookFactory.create(input);
//      } catch (IOException | InvalidFormatException e) {
//        System.out.println("未找到指定的文件！");
//      }
//    }
    try {
      //input = new FileInputStream(file);
      wb = WorkbookFactory.create(input);
    } catch (IOException e) {
      System.out.println("未找到指定的文件！");
    }
    return getContent(wb.getSheetAt(1));
  }

  /**
   * 获取Excel内容
   * @param sheet
   * @return
   */
  public static List<Map> getContent(Sheet sheet) {
    List<Map> list = new ArrayList<>();
    List<Map> cbfList = new ArrayList<>();
    List<Map> dkxxList = new ArrayList<>();
    List<Map> familyList = new ArrayList<>();
    List<Map> cbfList98=new ArrayList<>();
    List<Integer> cbfRowsIndex=new ArrayList<Integer>();
    Map map=new HashMap();
    List<String> titles=new ArrayList<>();
    // Excel数据总行数
    int rowCount = sheet.getLastRowNum();//getPhysicalNumberOfRows();
    //int columnCount = sheet.getRow(2).getPhysicalNumberOfCells();////获取不为空的总列数 getPhysicalNumberOfCells()
    int columnCount =sheet.getRow(2).getLastCellNum() - sheet.getRow(2).getFirstCellNum();
    for(int x=0;x<sheet.getRow(2).getPhysicalNumberOfCells();x++){
      //titles.add(getValue(sheet.getRow(0).getCell(x)));
      if (StringUtils.isNotNull(getCellValue(sheet.getRow(2),x) != null))
      {
        String value =getCellValue(sheet.getRow(2),x).toString();
        //System.out.println(value);
        titles.add(value);
      }
      else
      {
        titles.add(null);
      }
    }
    System.out.println("数据字段列表"+ JsonEscapeUtil.beanToJson(titles));
    // 遍历首行前三列为发包方数据信息,固定第五行开始为数据行
    Row rowFbf =sheet.getRow(5);
    Map mapFbf=new HashMap();
    for (int j = 0; j <=2; j++) {
      Cell cell = rowFbf.getCell(j);
      if (cell != null && cell.getCellType() != CellType.BLANK) {
        mapFbf.put(titles.get(j), getCellValue(rowFbf, j).toString());
      }
    }
//    List<Map> fbf = new ArrayList<Map>();
//    fbf.add(mapFbf);
//    Map fbfMap = new HashMap();
//    fbfMap.put("fbf",fbf);
    list.add(mapFbf);
    //下面开始进行正式的数据提取，嵌套lsit,Map形式展示数据
    for (int i = 5; i<=rowCount; i++){
      Row cbfRow=sheet.getRow(i);
      Map mapCbf=new HashMap();
      int cbfRowIndex = 0;
      for(int j=3;j<=8;j++){
        if(StringUtil.isNotEmpty(getCellValue(cbfRow,j).toString())){
          cbfRowIndex=i;
          mapCbf.put(titles.get(j),getCellValue(cbfRow,j).toString());
          mapCbf.put("cbfRowIndex",cbfRowIndex);
          //System.out.println(getCellValue(row,3).toString());
        }
      }
      //System.out.println("承包方数据的列数："+titles.indexOf("gsshr"));
      for(int j=titles.indexOf("cbfdcrq");j<=titles.indexOf("gsshr");j++){
        //if(StringUtil.isNotEmpty(getCellValue(cbfRow,j).toString())){
        mapCbf.put(titles.get(j),getCellValue(cbfRow,j).toString());
        //}
      }
      //System.out.println(cbfids);
      //下面时提取98年承包方信息数据
      int cbf98index= titles.indexOf("yhzmc98");
      Map map98=new HashMap();
      for(int x=cbf98index;x<=columnCount;x++){
        if(StringUtil.isNotEmpty(getCellValue(cbfRow,x).toString())){
          mapCbf.put(titles.get(x),getCellValue(cbfRow,x).toString());
        }
      }
      //cbfList98.add(map98);
      int columnIndex=titles.indexOf("cbfmc");
      Map dkMap =new HashMap();
      for(int n=titles.indexOf("sfkbdk");n<=titles.indexOf("dkbdh");n++){
        //if(StringUtil.isNotEmpty(getCellValue(cbfRow, n).toString())){
        dkMap.put(titles.get(n), getCellValue(cbfRow, n).toString());
        dkMap.put("dkRowIndex",i);
        //}
      }
      dkxxList.add(dkMap);
      //System.out.println("第" + (i+1) + "条承包方地块数据："+dkMap);
      Map familiyMap=new HashMap();
      for(int z=titles.indexOf("cyxm");z<=titles.indexOf("cybdrq");z++){
        familiyMap.put(titles.get(z),getCellValue(cbfRow,z));
        familiyMap.put("familyRowIndex",i);
      }
      familyList.add(familiyMap);
      //System.out.println("第" + (i+1) + "条家庭成员数据："+familyList);
      if(mapCbf.get("elcbdkzs")!=null&&mapCbf.get("cbfRowIndex")!="0") {
        //System.out.println("承包方数据行："+mapCbf.get("cbfRowIndex"));
        cbfList.add(mapCbf);
        //System.out.println("第" + (i+1) + "条承包方数据：" + JsonUtil.beanToJson(cbfList));
      }
    }
    //System.out.println("总行数："+sheet.getLastRowNum());
    //System.out.println("承包方所有数据："+cbfList);
    List<Map> allCbfList =cbfList;
    for(int y=0;y<cbfList.size();y++){
      List dkList=new ArrayList<>();
      List famList=new ArrayList<>();
      Map cbfMap=cbfList.get(y);
      //System.out.println("每个承包方的Map数据："+cbfMap);
      int cbfRowNum=Integer.parseInt(String.valueOf(cbfMap.get("cbfRowIndex")));
      //System.out.println("数据："+cbfRowNum);
      if(y==cbfList.size()-1){
        int allRowCount=sheet.getLastRowNum();
        for(int m=cbfRowNum-5;m<=allRowCount-5;m++){
          //System.out.println("承包方每一行的序号:"+m);
          //System.out.println("总行数"+allRowCount);
          int dkRowIndex=Integer.parseInt(String.valueOf(dkxxList.get(m).get("dkRowIndex")));
          if(m==(dkRowIndex-5)){
            if(StringUtil.isNotEmpty(dkxxList.get(m).get("dklb").toString())) {
              dkxxList.get(m).remove("dkRowIndex");
              dkList.add(dkxxList.get(m));
            }
            //dkList.addAll(dkxxList);
          }
          int familyRowIndex=Integer.parseInt(String.valueOf(familyList.get(m).get("familyRowIndex")));
          if(m==(familyRowIndex-5)){
            if(StringUtil.isNotEmpty(familyList.get(m).get("cyxm").toString())) {
              familyList.get(m).remove("familyRowIndex");
              famList.add(familyList.get(m));
            }
            //famList.addAll(familyList);
          }
        }
      }else{
        Map cbfMap2=cbfList.get(y+1);
        //System.out.println("本条数据："+cbfMap);
        //System.out.println("下一条数据："+cbfMap2);
        int cbfRowNum2=Integer.parseInt(String.valueOf(cbfMap2.get("cbfRowIndex")));
        for(int m=cbfRowNum-5;m<cbfRowNum2-5;m++){
          //System.out.println("承包方每一行的详细"+m);
          //System.out.println("所有地块信息的总数："+dkxxList.size());
          int dkRowIndex=Integer.parseInt(String.valueOf(dkxxList.get(m).get("dkRowIndex")));
          //System.out.println("地块信息的行数："+dkRowIndex);
          if(m==(dkRowIndex-5)){
            if(StringUtil.isNotEmpty(dkxxList.get(m).get("dklb").toString())) {
              dkxxList.get(m).remove("dkRowIndex");
              dkList.add(dkxxList.get(m));
            }
            //dkList.addAll(dkxxList);
            //System.out.println("map数据"+dkxxList.get(m));
          }
          int familyRowIndex=Integer.parseInt(String.valueOf(familyList.get(m).get("familyRowIndex")));
          if(m==(familyRowIndex-5)){
            if(StringUtil.isNotEmpty(familyList.get(m).get("cyxm").toString())){
              familyList.get(m).remove("familyRowIndex");
              famList.add(familyList.get(m));
            }
            //famList.addAll(familyList);
            //System.out.println("map数据"+famList);
          }
        }
      }
      //去除列表中为空的项
      //famList.removeAll(Collections.singleton(""));
      //dkList.removeAll(Collections.singleton(""));
      cbfMap.put("jtcyxx",famList);
      cbfMap.put("dkxx",dkList);
      cbfList.get(y).putAll(cbfMap);
      cbfList.get(y).remove("cbfRowIndex");
      System.out.println("承包方数据带家庭成员和地块信息："+cbfMap);
    }
    //cbfList =getCbfList(cbfList,sheet,dkxxList,familyList);
    //去除隶属列表中为空的项
    cbfList.removeAll(Collections.singleton(null));
    map.put("allcbf",cbfList);
    list.add(map);
    //System.out.println("第" + i + "条承包方数据：" + JsonUtil.beanToJson(list));
    logger.info("所有数据："+list);
    return list;
  }

  /**
   * 获取单元格的值
   * @param row
   * @param column
   * @return
   */
  public static Object getCellValue(Row row, int column)
  {
    if (row == null)
    {
      return row;
    }
    Object val = "";
    try
    {
      Cell cell = row.getCell(column);
      if (cell != null)
      {
        if (cell.getCellType() == CellType.NUMERIC || cell.getCellType() == CellType.FORMULA)
        {
          val = cell.getNumericCellValue();
          if (DateUtil.isCellDateFormatted(cell))
          {
            val = DateUtil.getJavaDate((Double) val); // POI Excel 日期格式转换
          }
          else
          {
            if ((Double) val % 1 > 0)
            {
              val = new DecimalFormat("0.00").format(val);
            }
            else
            {
              val = new DecimalFormat("0").format(val);
            }
          }
        }
        else if (cell.getCellType() == CellType.STRING)
        {
          val = cell.getStringCellValue();
        }
        else if (cell.getCellType() == BOOLEAN)
        {
          val = cell.getBooleanCellValue();
        }
        else if (cell.getCellType() == CellType.ERROR)
        {
          val = cell.getErrorCellValue();
        }

      }
    }
    catch (Exception e)
    {
      return val;
    }
    return val;
  }
}
