package com.yonyou.myself.util;

import com.alibaba.fastjson.JSON;
import com.ufgov.sm.SM4Utils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import java.lang.reflect.Field;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @version 1.0
 * @Author :liyanan
 * @Date : 2021/2/22 14:32
 * @Description : 日志脱敏工具类
 ***/
public class DesensitizedUtils {

    private static final Logger logger=Logger.getLogger(DesensitizedUtils.class);
    /**
     * 日志脱敏 开关
     */
    private static String LOG_FILTER_SWITH = "false";
    /**
     * 日志脱敏关键字
     */
    private static String LOG_FILTER_KEYS = null;

    static {
        // 加载配置文件
        try {
            ResourceBundle bundle = ResourceBundle.getBundle("log4j");
            LOG_FILTER_SWITH = bundle.getString("log4j.filter.swith");
            LOG_FILTER_KEYS = bundle.getString("log4j.filter.keys");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    private static final Map<String, TypeEnum> annotationMaps = new HashMap<>();
    /**类加载时装配脱敏字段*/
    static{
        try{
            Class<?> clazz=Class.forName(BaseInfo.class.getName());
            Field[] fields =clazz.getDeclaredFields();
            for (int i = 0; i <fields.length ; i++) {
                fields[i].setAccessible(true);
                DesensitizedAnnotation annotation=fields[i].getAnnotation(DesensitizedAnnotation.class);
                if (annotation!=null){
                    TypeEnum typeEnum=  annotation.type();
                    String name=fields[i].getName();
                    annotationMaps.put(name,typeEnum);
                }
                
            }
        }catch (ClassNotFoundException e){
            logger.error("类加载时装配脱敏字段异常，异常信息：{"+e+"}");
        }
    }

    public static String getConverent(Map<String,Object> object){
        try{
             // 1.处理Map数据类型
            if (object instanceof Map) {
                HashMap<String, Object> reqMap = (HashMap) object;
                Iterator<String> iterator = annotationMaps.keySet().iterator();
                iterator.forEachRemaining(annotationName -> {
                    if (reqMap.keySet().contains(annotationName)) {
                        doconverentForMap(reqMap, annotationName);
                    }
                });
                return JSON.toJSONString(reqMap);
            }
                return JSON.toJSONString(object);
        }catch(Exception e){
            logger.error("日志脱敏处理失败，回滚，详细信息:"+e);
            return JSON.toJSONString(object);
        }
    }

    /**
     * 脱敏数据源为Map时处理方式
     *
     * @param reqMap
     * @param annotationName
     * @return
     */
    private static void doconverentForMap(HashMap<String, Object> reqMap, String annotationName) {
        String value = String.valueOf(reqMap.get(annotationName));
        if (StringUtils.isNotEmpty(value)) {
            value = doConverentByType(value, annotationName);
        }
        reqMap.put(annotationName, value);
    }


    /**
     * 根据不同注解类型处理不同字段
     *
     * @param value
     * @param annotationName
     * @return
     */
    private static String doConverentByType(String value, String annotationName) {
        TypeEnum typeEnum = annotationMaps.get(annotationName);
        switch (typeEnum) {
            case PERSON_NAME:
                value = getStringByLength(value);
                break;
            case PERSON_CERT_NO:
                value = getStringByLength(value);
            default:
                value = getStringByLength(value);
        }
        return value;
    }

    /**
     * 根据value长度取值(切分)
     *
     * @param value
     * @return
     */
    private static String getStringByLength(String value) {
        int length = value.length();
        if (length == 2){
            value = value.substring(0, 1) + "*";
        }else if (length == 3){
            value = value.substring(0,1) + "*" + value.substring(length -1);
        }else if (length > 3 && length <= 5){
            value = value.substring(0,1) + "**" + value.substring(length -2);
        }else if (length > 5 && length <= 7){
            value = value.substring(0,2) + "***" + value.substring(length -2);
        }else if (length > 7){
           // value = value.substring(0,3) + "*****" + value.substring(length -3);
            String zj=value.substring(3,value.length()-3);
            SM4Utils.encryptData_ECB(zj,"1234567890123456");
           // value = value.substring(0,3) + addLableForNum("",length-6,"*")+ value.substring(length -3);
            value = value.substring(0,3) + SM4Utils.encryptData_ECB(zj,"1234567890123456")+ value.substring(length -3);
        }
        System.out.println(value);
        return value;
    }

    /**
     * 标签内容替换
     * */
    public static  String tagReplace(String source,String tag){
        logger.error("测试日志内容1111");
        //就差一个问号的问题
        String tagRule= "<"+tag+">(.*?)</"+tag+">";
        Pattern p = Pattern.compile(tagRule);
        Matcher m = p.matcher(source);
        while(m.find()){
            source= source.replaceAll(m.group(1),getStringByLength(m.group(1)));
       }
        return source;
    }

    /**
     * 字符类型左补任意字符
     * @param source       待补字符串
     * @param length 整个字符符的长度
     * @param lable 待补的字符
     * @return 补充完的字符串感觉
     */
    public static  String  addLableForNum(String source,int length,String lable){

      if (length==1){
          return lable;
      } else{
          source=lable+source;
          if (source.length()<length) {
             return addLableForNum(source, length, lable);
          }
          return source;
      }
    }

    public static String  encDataNum(String constant ){
        logger.info("测试数据处理数据加载情况111111");
        logger.error("错误日志输出信息111");
        String input="11111zhjpmg zhongguo,./tyuytr12341234  kss中文試試";
        for (char c : input.toCharArray()) {
            System.out.println(c);
            if (Character.getType(c) == Character.OTHER_LETTER) {
                System.out.println("中文");
            } else if (Character.isDigit(c)) {
                System.out.println("数字");
            } else if (Character.isLetter(c)) {
                System.out.println("英文字母");
            } else {
                System.out.println("其他字符");
            }
        }
        return input;
    }

    /**
     * 处理日志字符串，返回脱敏后的字符串 处理类型是key:value类型的数据
     * @param message
     * @return
     */
    public static String invokeMsg(final String message) {
        String msg = new String(message);
        if ("true".equals(LOG_FILTER_SWITH)) {
            //处理字符串
            if (LOG_FILTER_KEYS != null && LOG_FILTER_KEYS.length() > 0) {
                String[] keyArr = LOG_FILTER_KEYS.split(",");
                for (String key : keyArr) {
                    // 找key
                    int index = -1;
                    do {
                        index = msg.indexOf(key, index + 1);
                        if (index != -1) {
                            // 判断key是否为单词字符
                            if (isWordChar(msg, key, index)) {
                                continue;
                            }
                            // 确定是单词无疑....................................
                            // 寻找值的开始位置.................................
                            int valueStart = getValueStartIndex(msg, index + key.length());
                            //查找值的结束位置（逗号，分号）........................
                            int valueEnd = getValuEndEIndex(msg, valueStart);
                            // 对获取的值进行脱敏
                            String subStr = msg.substring(valueStart, valueEnd);
                            subStr = getStringByLength(subStr);
                            ///////////////////////////
                            msg = msg.substring(0, valueStart) + subStr + msg.substring(valueEnd);
                        }
                    } while (index != -1);

                }
            }
        }

        return msg;
    }

    /**
     * 判断从字符串msg获取的key值是否为单词 ， index为key在msg中的索引值
     * @return
     */

    private  static  Pattern pattern = Pattern.compile("[0-9a-zA-Z]");
    private static boolean isWordChar(String msg, String key, int index) {
        // 必须确定key是一个单词............................
        //判断key前面一个字符
        if (index != 0) {
            char preCh = msg.charAt(index - 1);
            Matcher match = pattern.matcher(preCh + "");
            if (match.matches()) {
                return true;
            }
        }
        //判断key后面一个字符
        char nextCh = msg.charAt(index + key.length());
        Matcher match = pattern.matcher(nextCh + "");
        if (match.matches()) {
            return true;
        }
        return false;

    }

    /**
     * 获取value值的开始位置
     * @param msg        要查找的字符串
     * @param valueStart 查找的开始位置
     * @return
     */
    private static int getValueStartIndex(String msg, int valueStart) {
        // 寻找值的开始位置.................................
        do {
            char ch = msg.charAt(valueStart);
            // key 与 value的分隔符
            if (ch == ':' || ch == '=') {
                valueStart++;
                ch = msg.charAt(valueStart);
                if (ch == '"') {
                    valueStart++;
                }
                break;    //找到值的开始位置
            } else {
                valueStart++;
            }

        } while (true);
        return valueStart;
    }

    /**
     * 获取value值的结束位置
     * @return
     */
    private static int getValuEndEIndex(String msg, int valueEnd) {
        do {
            if (valueEnd == msg.length()) {
                break;
            }
            char ch = msg.charAt(valueEnd);
            // 引号时，判断下一个值是结束，分号还是逗号决定是否为值的结束
            if (ch == '"') {
                if (valueEnd + 1 == msg.length()) {
                    break;
                }
                char nextCh = msg.charAt(valueEnd + 1);
                if (nextCh == ';' || nextCh == ',') {
                    // 去掉前面的 \  处理这种形式的数据 "account_num\\\":\\\"6230958600001008\\\"
                    while (valueEnd > 0) {
                        char preCh = msg.charAt(valueEnd - 1);
                        if (preCh != '\\') {
                            break;
                        }
                        valueEnd--;
                    }
                    break;
                } else {
                    valueEnd++;
                }
            } else if (ch == ';' || ch == ',') {
                break;
            } else {
                valueEnd++;
            }
        } while (true);
        return valueEnd;
    }


}
