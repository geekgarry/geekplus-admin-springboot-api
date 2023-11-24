package com.geekplus.common.util.text;

import org.apache.commons.lang3.builder.DiffResult;
import org.apache.commons.text.diff.StringsComparator;

import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

/**
 * author     : geekplus
 * email      : geekcjj@gmail.com
 * date       : 11/24/23 02:32
 * description: 文本比较工具
 */
public class TextComparison {

    //旧文本中差异区域的起点
    static int oldStart = 0;
    //旧文本中差异区域的终点
    static int oldEnd = 0;
    //新文本中差异区域的起点
    static int newStart = 0;
    //新文本中差异区域的终点
    static int newEnd = 0;

    //在寻找一块差异区域的终点时，是否是第一次找到终点
    static boolean isFirstGetEnd = true;

    //存储旧文本的每一行
    static List<String> oldLines;
    //存储新文本的每一行
    static List<String> newLines;

    //下面的集合用来存储对比结果
    //存储增加的行在新文本中的行号
    static List<Integer> delLines = new LinkedList<Integer>();
    //存储删除的行在旧文本中的行号
    static List<Integer> addLines = new LinkedList<Integer>();
    //存储修改的行分别在新旧文本中的行号
    static Map<Integer,Integer> updateLines = new HashMap<Integer,Integer>();

    public static void main(String[] args) {

        //作为旧文本
        String path1="F://comparetest/1.txt";
        //作为新文本
        String path2="F://comparetest/2.txt";

        //获取比对结果
        //List<String> differMsgList = getContrastResult(path1,path2);
        List<String> differMsgList = getTextContrastResult("iufud阿四姐男生对女生的","iufud阿四哥男生对女生的");
        //打印出对比结果
        for (String differ : differMsgList){
            System.out.println(differ);
        }
        System.out.println(differMsgList.size()!=0);
        System.out.println(calculate("iufud阿四姐男生对女生的","iufud阿四哥男生对女生的")!=0);
    }

    public static void compareText(String oldText,String newText){

//        StringsComparator comparator = new StringsComparator(oldText, newText);
//        DiffResult diffResult = comparator.compare();
//
//        System.out.println("Text 1: " + oldText);
//        System.out.println("Text 2: " + newText);
//        System.out.println("Differences: ");
//
//        for (DiffComparator diff : diffResult.getDiffs()) {
//            System.out.println("Type: " + diff.getType());
//            System.out.println("Deleted: " + diff.getDeleted());
//            System.out.println("Inserted: " + diff.getInserted());
//            System.out.println("------------------");
//        }
    }

    //实现 Levenshtein 距离算法
    public static int calculate(String str1, String str2) {
        int[][] dp = new int[str1.length() + 1][str2.length() + 1];

        for (int i = 0; i <= str1.length(); i++) {
            dp[i][0] = i;
        }

        for (int j = 0; j <= str2.length(); j++) {
            dp[0][j] = j;
        }

        for (int i = 1; i <= str1.length(); i++) {
            for (int j = 1; j <= str2.length(); j++) {
                if (str1.charAt(i - 1) == str2.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1];
                } else {
                    dp[i][j] = Math.min(Math.min(dp[i - 1][j], dp[i][j - 1]), dp[i - 1][j - 1]) + 1;
                }
            }
        }

        return dp[str1.length()][str2.length()];
    }

    //生成当前字符串的hash值
    public static String generate(String text) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] hashBytes = md.digest(text.getBytes());

            StringBuilder sb = new StringBuilder();
            for (byte b : hashBytes) {
                sb.append(String.format("%02x", b));
            }

            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
      * @Author geekplus
      * @Description //比对文本，并收集整理对比结果
      * @Param 文本内容
      * @Throws
      * @Return {@link }
      */
    public static List<String> getTextContrastResult(String oldText,String newText){

        //读取文件
        oldLines = Arrays.asList(oldText.split("(\\.|\\n)"));
        newLines = Arrays.asList(newText.split("(\\.|\\n)"));

        //调用对比方法
        compare();

        List<String> differMsgList = new ArrayList<String>();

        for(int lineNum : delLines){
            differMsgList.add("旧文本中删除了第"+(lineNum+1)+"行，内容是:"+oldLines.get(lineNum));
        }
        for(int lineNum : addLines){
            differMsgList.add("新文本中增加了第"+(lineNum+1)+"行，内容是:"+newLines.get(lineNum));
        }

        for(int oldNum : updateLines.keySet()){
            differMsgList.add("旧文本中的第"+(oldNum+1)+"行，内容是:"+oldLines.get(oldNum)
                    +"，修改为新文本中的第"+(updateLines.get(oldNum)+1)+"行，内容是:"+newLines.get(updateLines.get(oldNum)));
        }

        return differMsgList;
    }

    /**
      * @Author geekplus
      * @Description //比对文本，并收集整理对比结果,参数为文本路径
      * @Param 文本路径 path1，path2
      * @Throws
      * @Return {@link }
      */
    public static List<String> getContrastResult(String path1,String path2){
        //读取文件
        oldLines = readFile(path1);
        newLines = readFile(path2);

        //调用对比方法
        compare();

        List<String> differMsgList = new ArrayList<String>();

        for(int lineNum : delLines){
            differMsgList.add("旧文本中删除了第"+(lineNum+1)+"行，内容是:"+oldLines.get(lineNum));
        }
        for(int lineNum : addLines){
            differMsgList.add("新文本中增加了第"+(lineNum+1)+"行，内容是:"+newLines.get(lineNum));
        }

        for(int oldNum : updateLines.keySet()){
            differMsgList.add("旧文本中的第"+(oldNum+1)+"行，内容是:"+oldLines.get(oldNum)
                    +"，修改为新文本中的第"+(updateLines.get(oldNum)+1)+"行，内容是:"+newLines.get(updateLines.get(oldNum)));
        }

        return differMsgList;
    }

    //此方法用于读取文件，并且去除空行
    static public List<String> readFile(String path) {

        BufferedReader reader = null;
        File file = new File(path);
        if(!file.exists()) {
            System.out.println("文件不存在");
        }
        String tempStr;
        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "utf-8"));

            List<String> lines=new ArrayList<>();
            while ((tempStr = reader.readLine()) != null) {
                //读取文本时，每一行采用行号+行文本内容键值对的形式进行存储，行号作为该行的唯一标识
                if(!tempStr.trim().equals("")){
                    lines.add(tempStr);
                }
            }
            return lines;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }finally {
            if(reader!=null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    //准备方法，计算两个字符串相同字符的数量
    static public int numJewelsInStones(String J, String S) {
        J=J.trim();
        S=S.trim();
        char[] Ja = J.toCharArray();
        char[] Sa = S.toCharArray();
        int r = 0;
        for (int i = 0;i < Ja.length ; i ++){
            for(int j = 0; j < Sa.length; j++){
                if(Ja[i] == Sa[j])
                    r ++;
            }
        }
        return r;
    }


    /**
     * 逐行对比文本，找到第一个不同或者相同的行
     * 其中的oldLines和newLines为成员属性，表示旧文本的每一行和新文本的每一行
     * type：是找第一个内容相同的行还是不同的行，true为找相同，false为找不同
     * oldLineStart：旧文本从第几行开始
     * newLineStart：新文本从第几行开始
     * 返回true表示寻找成功，返回false表示寻找失败
     */

    static public boolean compareLines(boolean type,int oldLineStart,int newLineStart){
        if(oldLineStart >= oldLines.size() || newLineStart >= newLines.size()){
            return  false;
        }

        //行号计数器
        int lineCount = 0;
        //开始逐行比对两个文本
        int oldLineNumber,newLineNubmer;
        while ((oldLineNumber=oldLineStart+lineCount)<oldLines.size() && (newLineNubmer=newLineStart+lineCount)<newLines.size()){
            //分别取出新旧文本中的一行
            String lineOld = oldLines.get(oldLineNumber);
            String lineNew = newLines.get(newLineNubmer);


            //下面代码中的oldEnd、oldStart、newEnd、newStart为实例属性，
            //分别表示旧文本差异区域的起终点和新文本差异区域的起终点

            //找到完全相同的两行，其可以作为差异区域的终点
            if(type && lineOld.equals(lineNew)){
                //如果是第一次找到终点，先记录在oldEnd、newEnd两个属性中
                if(isFirstGetEnd){
                    oldEnd = oldLineNumber;
                    newEnd = newLineNubmer;
                    isFirstGetEnd = false;
                    //如果不是第一次找到，比较哪个终点与起点最近，取最近的终点
                }else if(newLineNubmer<newEnd){
                    oldEnd = oldLineNumber;
                    newEnd = newLineNubmer;
                }
                return true;
            }
            //找到差异的两行，其可以作为差异区域的起点
            if(!type && !lineOld.equals(lineNew)){
                oldStart = oldLineNumber;
                newStart = newLineNubmer;
                return true;
            }
            lineCount++;
        }
        //到文本的最后还没找到，返回false
        return false;
    }


    //在新旧文本寻找差异区域的起点，oldLines和newLines分别为存储新旧文本行内容的Map集合
    static public boolean getDifferenceAreaStart() {
        return compareLines(false,oldEnd,newEnd);
    }


    //寻找差异区域的终点，也就是新旧文本重新复合的点。
    static public boolean getDifferenceAreaEnd() {
        //重置为true
        isFirstGetEnd = true;
        //标记是否找到终点
        boolean haveEnd = false;
        //moveLines为文本下移的行数
        int moveLines = 0;
        int oldLineNumber=oldStart,newLineNubmer=newStart;
        while ((oldLineNumber<oldLines.size() || newLineNubmer < newLines.size())
        ){
            //newStart为0时不移动文本，newStart大于0时尝试以移动文本的方式来找终点
            if(compareLines(true,oldLineNumber,newStart) || compareLines(true,oldStart,newLineNubmer)){
                haveEnd = true;
            }
            moveLines ++;
            oldLineNumber = oldStart + moveLines;
            newLineNubmer = newStart + moveLines;
        }
        return haveEnd;
    }

    //找出差异区域内哪些是修改的行
    //参数n表示我们需要找的修改前后的行有几对
    public static Map<Integer, Integer> getUpdateLines(int n) {

        Map<Integer, Integer> resultMap=new HashMap();
        //准备数组，用来储存组队两行的重复字符个数
        int[] repeatCounts = new int[(oldEnd-oldStart)*(newEnd-newStart)];

        //用来储存组队两行的重复字符个数和行之间的对应关系
        Map<String,Integer> contAndLines = new HashMap<String,Integer>();

        int num = 0;
        for(int i = oldStart; i < oldEnd ; i++){
            for(int j = newStart ; j <newEnd ;j++){
                int count=numJewelsInStones(oldLines.get(i),newLines.get(j));
                repeatCounts[num] = count;
                contAndLines.put(String.valueOf(i)+":"+String.valueOf(j),count);
                num++;
            }
        }

        //对数组进行升序
        Arrays.sort(repeatCounts);
        //标记已经找到的修行前后对应行的数量
        int lineCount = 0;
        out:
        for(int i = repeatCounts.length-1 ; i>=(repeatCounts.length - n);i--){
            for(String lineInfo : contAndLines.keySet()){
                if(contAndLines.get(lineInfo).intValue() == repeatCounts[i]){
                    String[] lineNumA=lineInfo.split(":");
                    resultMap.put(Integer.valueOf(lineNumA[0]),Integer.valueOf(lineNumA[1]));
                    if(++lineCount >= n){
                        break out;
                    }
                }
            }
        }
        return resultMap;
    }


    //分析文本的变化类型，存入结果集合中
    public static void analChangeType() {

        //下面开始分析差异区域的变化类型，然后按照类型进行处理
        //oldEnd、oldStart、newEnd、newStart为实例属性，
        //分别表示旧文本差异区域的起终点和新文本差异区域的起终点
        int oldNumDiff = oldEnd-oldStart;
        int newNumDiff = newEnd-newStart;

        //纯修改
        if(oldNumDiff == newNumDiff){
            int number=oldEnd-oldStart;
            for(int i = 0 ;i<number ;i++){
                updateLines.put(oldStart+i,newStart+i);
            }
        }else if(oldNumDiff > newNumDiff){
            if(newEnd==newStart){
                //纯删除
                for(int i=oldStart;i<oldEnd;i++) {
                    //取出被删除的行，存入集合
                    delLines.add(i);
                }
            }else {
                //删除加修改
                //计算修改的行数
                int updateNum=newNumDiff;
                //获取修改的行，getUpdateLines为获取修改行对于关系的方法，
                // 返回的Map中存储的是修改前后的行号
                Map<Integer, Integer> changeLineMap=getUpdateLines(updateNum);
                updateLines.putAll(changeLineMap);
                //获取删除的行
                for(int lineNum = oldStart ;lineNum <oldEnd ; lineNum ++){
                    if(!changeLineMap.containsKey(lineNum)){
                        delLines.add(lineNum);
                    }
                }
            }
        }else {
            if(oldEnd==oldStart){
                //纯新增
                for(int i=newStart;i<newEnd;i++) {
                    addLines.add(i);
                }
            }else {
                //新增加修改
                //此时修改的行数是：
                int number=oldNumDiff;
                //获取修改的旧文本行号与新文本行号组成键值对的集合
                Map<Integer, Integer> changeLineMap = getUpdateLines(number);
                updateLines.putAll(changeLineMap);
                //获取新增的行
                for(int lineNum = newStart ;lineNum <newEnd ; lineNum ++){
                    if(!changeLineMap.values().contains(lineNum)){
                        addLines.add(lineNum);
                    }
                }
            }
        }
    }



    //递归对比文本
    public static void compare(){
        //如果能找到差异区域的起点
        if(getDifferenceAreaStart()){
            //也能找到差异区域的终点
            if(getDifferenceAreaEnd()){
                analChangeType();
                compare();
            }else {
                //如果找不到差异区域的终点，说明从起点开始下文全是差异区域
                oldEnd = oldLines.size();
                newEnd = newLines.size();
                analChangeType();
            }
        }
    }
}
