package com.yonyou.myself.util;

import com.alibaba.fastjson.JSON;
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
            value = value.substring(0,3) + addLableForNum("",length-6,"*")+ value.substring(length -3);
        }
        return value;
    }

    /**
     * 标签内容替换
     * */
    public static  String tagReplace(String source,String tag){
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

}
