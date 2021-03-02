package com.yonyou.myself.log4j;

import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @version 1.0
 * @Author :liyanan
 * @Date : 2021/2/24 14:12
 * @Description : TODO
 ***/
public class Log4jFilter {
/*    *//**
     * 日志脱敏 开关
     *//*
    private static String LOG_FILTER_SWITH = "false";
    *//**
     * 日志脱敏关键字
     *//*
    private static String LOG_FILTER_KEYS = null;*/

    static {
        // 加载配置文件
        try {
            // 直接在本类中使用main调用时用 Properties.class.getResourceAsStream("/log4j_filter.properties");
            //InputStream in =Properties.class.getClassLoader().getResourceAsStream("log4j_filter.properties");
            //Properties p = new Properties();
            //p.load(in);
            //LOG_FILTER_SWITH = p.getProperty("log4j.filter.swith");
            //LOG_FILTER_KEYS = p.getProperty("log4j.filter.keys");
            ResourceBundle bundle = ResourceBundle.getBundle("log4j_filter");
          /*  LOG_FILTER_SWITH = bundle.getString("log4j.filter.swith");
            LOG_FILTER_KEYS = bundle.getString("log4j.filter.keys");*/
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

/*
    *//**
     * 处理日志字符串，返回脱敏后的字符串
     * @param message
     * @return
     *//*
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
                            subStr = tuomin(subStr);
                            ///////////////////////////
                            msg = msg.substring(0, valueStart) + subStr + msg.substring(valueEnd);
                        }
                    } while (index != -1);

                }
            }
        }

        return msg;
    }

    *//**
     * 判断从字符串msg获取的key值是否为单词 ， index为key在msg中的索引值
     * @return
     *//*
    private static boolean isWordChar(String msg, String key, int index) {
        Pattern pattern = Pattern.compile("[0-9a-zA-Z]");
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

    *//**
     * 获取value值的开始位置
     * @param msg        要查找的字符串
     * @param valueStart 查找的开始位置
     * @return
     *//*
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

    *//**
     * 获取value值的结束位置
     *
     * @return
     *//*
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
    }*/

    private static String tuomin(String submsg) {

        StringBuffer sbResult = new StringBuffer();
        if (submsg != null && submsg.length() > 0) {
            int len = submsg.length();
            if (len > 8) { //8位以上的    隐掉中间4位
                for (int i = len - 1; i >= 0; i--) {
                    if (len - i < 5 || len - i > 8) {
                        sbResult.insert(0, submsg.charAt(i));
                    } else {
                        sbResult.insert(0, '*');
                    }
                }
            } else { //8位以下的全部使用 *
                for (int i = 0; i < len; i++) {
                    sbResult.append('*');
                }
            }
        }
        return sbResult.toString();
    }

}
