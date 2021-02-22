package com.yonyou.myself.util;

import sun.misc.BASE64Encoder;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @version 1.0
 * @Author :liyanan
 * @Date : 2021/2/21 14:52
 * @Description : TODO
 ***/
public class JavaUtil {

    /**
     * 格式化数据将数据转换成BigDecimal类型数据
     */
    public static BigDecimal getBigDecimal(Object value) {
        BigDecimal ret = BigDecimal.valueOf(0.00);
        if (value != null) {
            if (value instanceof BigDecimal) {
                ret = ((BigDecimal) value).setScale(2,BigDecimal.ROUND_DOWN);
            } else if (value instanceof String) {
                ret = new BigDecimal((String) value).setScale(2,BigDecimal.ROUND_DOWN);
            } else if (value instanceof BigInteger) {
                ret = new BigDecimal((BigInteger) value).setScale(2,BigDecimal.ROUND_DOWN);
            } else if (value instanceof Number) {
                ret = BigDecimal.valueOf(((Number) value).doubleValue()).setScale(2,BigDecimal.ROUND_DOWN);
            } else {
                throw new ClassCastException("Not possible to coerce [" + value + "] from class " + value.getClass() + " into a BigDecimal.");
            }
        }
        return ret;
    }

    /**
     * 数据做除法 a1/a2
     */
    public static BigDecimal divideBigDecimal(Object a1, Object a2) {
        BigDecimal b1 = getBigDecimal(a1);
        BigDecimal b2 = getBigDecimal(a2);
        if (b1==null){
            return BigDecimal.valueOf(0.00);
        }else {
            return b1.divide(b2);
        }

    }

    /**
     * 获取当前年度
     */
    public static String getCurrentYear() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
        return sdf.format(new Date());
    }

    /**
     * 获取当前日期
     * yyyyMMdd格式的
     */
    public static String getCurrentDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        return sdf.format(new Date());
    }

    /**
     * 据当前日期几天之前的日期以yyyyMMdd的格式返回
     *
     * @param i 几天之前的天数
     */
    public static String getBeforeDate(int i) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Date now = new Date();
        Date before;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);
        calendar.add(Calendar.DAY_OF_MONTH, i);
        before = calendar.getTime();
        return sdf.format(before);
    }

    /**
     * 日期格式转换 将yyyyMMdd转换成yyyy-MM-dd的格式
     *
     * @param str yyyyMMdd格式的日期
     * @return yyyy-MM-dd格式的日期
     **/
    public static String dateConvertion(String str) {
        Date parse = null;
        String dateString = "";
        try {
            parse = new SimpleDateFormat("yyyyMMdd").parse(str);
            dateString = new SimpleDateFormat("yyyy-MM-dd").format(parse);
        } catch (ParseException e) {
            dateString = null;
        }
        return dateString;
    }

    /**
     * 获取当前时间
     *
     * @return yyyy-MM-dd HH:mm:ss返回日期格式
     */
    public static String getCurrentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date());
    }

    /**
     * 获取当前月份
     * @return
     */
    public static String getCurrentMonth(){
        Calendar cal = Calendar.getInstance();
        int month = cal.get(Calendar.MONTH) + 1;
        return addZeroForNum(month+"",2);
    }

    /**
     * 获取当前月份之前的某个月份值
     * @param i  前几个月
     * @return
     */
    public static String getCurrentMonth(int i){
        SimpleDateFormat format = new SimpleDateFormat("MM");
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date); // 设置为当前时间
        calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) - i); // 设置为上一个月
        date = calendar.getTime();
        String accDate = format.format(date);
        return accDate;
    }

    /**
     * 获取当前月份之前的某个月份值
     * @param i  前几个月
     * @return
     */
    public static String getLastYear(int i){
        SimpleDateFormat format = new SimpleDateFormat("yyyy");
        Calendar calendar = Calendar.getInstance();
        // 设置为当前时间
        calendar.setTime( new Date());
        calendar.add(Calendar.YEAR,-i);
        Date date = calendar.getTime();
        String lastYear = format.format(date);
        return lastYear;
    }
    /**
     * 获取当前时间yyyyMMddHHmmss
     *
     * @return yyyyMMddHHmmss返回没有符号的时间格式
     */
    public static String getCurrentTimeNoSymbol() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        return sdf.format(new Date());
    }

    /**
     * 将时间格式转换成long类型的数据
     *
     * @param dateStr string类型的日期
     * @return long类型数据
     **/
    public static long dateToTimeStamp(String dateStr) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            return sdf.parse(dateStr).getTime() / 1000;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 获取系统long类型的时间值
     * retrun long类型的数据
     */
    public static long getCurrentTimeMillis() {
        return System.currentTimeMillis();
    }

    /**
     * 将 yyyy-MM-dd HH：mm：ss转换为yyyyMMddhhmmss
     */
    public static String dateFormateByDate(String date) {
        String oldcar = "-| |:";
        return date.replaceAll(oldcar, "").trim();

    }

    /**
     * 将long类型的时间值转换为yyyy-MM-dd HH:mm:ss类型的时间值
     *
     * @param time 时间long类型值
     * @return 返回String类型值
     **/
    public static String timeStampToStr(long time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date(time * 1000));
    }

    /**
     * 将long类型的时间值转换为yyyyMMddHHmmss类型的时间值
     *
     * @param time 时间long类型值
     * @return 返回String类型值
     **/
    public static String timeStampToString(long time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        return sdf.format(new Date(time * 1000));
    }

    public static String getAfterDateByDate(String day){
        Calendar c = Calendar.getInstance();
        Date date = null;
        try {
            date = new SimpleDateFormat("yyyyMMdd").parse(day);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        c.setTime(date);
        int day1 = c.get(Calendar.DATE);
        c.set(Calendar.DATE, day1 + 1);
        return new SimpleDateFormat("yyyyMMdd").format(c.getTime());
    }


    /**
     * 日期格式转换
     * yyyymmddhhmmss转为yyyy-mm-dd HH:mm:ss
     *
     * @param date
     * @return
     */
    public static String getFormatDate(String date) {
        String reg = "(\\d{4})(\\d{2})(\\d{2})(\\d{2})(\\d{2})(\\d{2})";
        return date.replaceAll(reg, "$1-$2-$3 $4:$5:$6");
    }

    /**
     * 对象转数组
     *
     * @param obj
     * @return
     */
    public static byte[] toByteArray(Object obj) {
        byte[] bytes = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(obj);
            oos.flush();
            bytes = bos.toByteArray();
            oos.close();
            bos.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return bytes;
    }

    /**
     * 字符类型左补0
     *
     * @param str       待补0的字符串
     * @param strLength 整个字符符的长度
     * @return 补充完的字符串
     */
    public static String addZeroForNum(String str, int strLength) {
        int strLen = str.length();
        if (strLen < strLength) {
            while (strLen < strLength) {
                StringBuffer sb = new StringBuffer();
                sb.append("0").append(str);//左补0
                str = sb.toString();
                strLen = str.length();
            }
        }
        return str;
    }

    /**
     * @param str       原字符串
     * @param strLength 字符串要补长度
     * @param chr       需要补的字符串
     * @Author: liyanan
     * @Description: 右补数据长度
     * @DateTime: 2020/3/2 17:38
     * @Return
     */
    public static String addRightCharForNum(String str, int strLength, String chr) {
        int strLen = str.length();
        if (strLen < strLength) {
            while (strLen < strLength) {
                StringBuffer sb = new StringBuffer();
                //右补数据
                sb.append(str).append(chr);
                str = sb.toString();
                strLen = str.length();
            }
        }
        return str;
    }

    /**
     * 将字符转换成ASCII的字符类型
     *
     * @param text 待转的字符
     * @return 转换成ASCII的字符
     */
    public static String asciiToString(String text) {
        StringBuilder sbu = new StringBuilder();
        String[] line = text.split("\n");
        for (String s : line) {
            String[] chars = s.split(" ");
            for (String char1 : chars) {
                sbu.append((char) Integer.parseInt(char1));
            }
        }
        return sbu.toString();
    }

    public static String[] twoCharToGroup(String number) {
        String[] str;
        int length = number.length();
        int group = length / 2;
        if (0 == length % 2) {
            str = new String[group];
        } else {
            str = new String[group + 1];
        }
        for (int i = 0, j = 0; i < group; i++, j += 2) {
            str[i] = number.substring(j, j + 2);
            if (i == (group - 1)) {
                if (1 == length % 2) {
                    str[i + 1] = number.substring(length - 1, length);
                }
            }
        }
        return str;
    }

    public static String ASCIIToConvert(String value) {
        StringBuffer sbu = new StringBuffer();
        String[] chars = value.split("  ");
        for (int i = 0; i < chars.length; i++) {
            sbu.append((char) Integer.parseInt(chars[i]));
        }
        return sbu.toString();
    }

    /**
     * 字符转16进制字符串
     */
    public static String hex2Str(String hex) {
        StringBuilder sb = new StringBuilder();
        String[] split = hex.split("  ");
        for (String str : split) {
            int i = Integer.parseInt(str, 16);
            sb.append((char) i);
        }
        return sb.toString();
    }



    /**
     * 获取待毫秒的时间
     *
     * @return yyyyMMddHHmmssSSS返回类型的时间
     */
    public static String getCurrTimeStr() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        return sdf.format(new Date());
    }

    /**
     * 获取待毫秒的时间
     *
     * @return yyyy-MM-dd HH:mm:ss SSS返回类型的时间
     */
    public static String getCurrTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS");
        return sdf.format(new Date());
    }

    /**
     * 获取5天前的数据
     **/
    public static String getDateBeforeFive(int i) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, -i);
        Date time = c.getTime();
        String preDay = sdf.format(time);
        return preDay;
    }

    /**
     * 将字符串分组
     *
     * @param list    list待分组的数组
     * @param groupby 分组条件
     * @return 返回Map
     */
    public static Map dealListByUnitGroup(List list, String groupby) {
        Map<String, List<Map>> grouplist = new HashMap();
        for (int i = 0; i < list.size(); i++) {
            Map map = (Map) list.get(i);
            if (grouplist.containsKey(map.get(groupby))) {
                grouplist.get(map.get(groupby)).add(map);
            } else {
                List list1 = new ArrayList();
                list1.add(map);
                grouplist.put((String) map.get(groupby), list1);
            }
        }
        return grouplist;
    }

    /**
     * MD5 64bit Encrypt Methods.
     *
     * @param readyEncryptStr ready encrypt string
     * @return String encrypt result string
     * @throws Exception
     */
    public static String md564bit(String readyEncryptStr) throws Exception {
        MessageDigest md = MessageDigest.getInstance("MD5");
        BASE64Encoder base64Encoder = new BASE64Encoder();
        return base64Encoder.encode(md.digest(readyEncryptStr.getBytes("UTF-8"))) + base64Encoder.encode(md.digest(readyEncryptStr.getBytes("UTF-8")));
    }

    /**
     * 字符串 SHA 加密
     *
     * @param strText
     * @return
     */
    public static String sha256(final String strText) {
        String strResult = null;

        // 是否是有效字符串
        if (strText != null && strText.length() > 0) {
            try {
                // SHA 加密开始
                // 创建加密对象 并传入加密类型
                MessageDigest messageDigest = MessageDigest
                        .getInstance("SHA-256");
                // 传入要加密的字符串
                messageDigest.update(strText.getBytes());
                // 得到 byte
                byte[] byteBuffer = messageDigest.digest();
                // 将 byte 转换为 string
                StringBuffer strHexString = new StringBuffer();
                // 遍历byte buffer
                for (int i = 0; i < byteBuffer.length; i++) {
                    String hex = Integer.toHexString(0xff & byteBuffer[i]);
                    if (hex.length() == 1) {
                        strHexString.append('0');
                    }
                    strHexString.append(hex);
                }
                // 得到返回結果
                strResult = strHexString.toString();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }

        return strResult;
    }

    /**
     * 判断两个日期是不是同一天
     *
     * @param date1 Date()类型的时间格式
     * @param date2 Date()类型的时间格式
     * @return 是否是同一天
     */
    public static boolean isSameDay(Date date1, Date date2) {

        if (date1 != null && date2 != null) {
            Calendar cal1 = Calendar.getInstance();
            cal1.setTime(date1);
            Calendar cal2 = Calendar.getInstance();
            cal2.setTime(date2);
            return isSameDay(cal1, cal2);
        } else {
            throw new IllegalArgumentException("时间不能为null");
        }
    }

    /**
     * 判断两个日期是不是同一天
     *
     * @param cal1 Calendar()类型的时间格式
     * @param cal2 Calendar()类型的时间格式
     * @return 是否是同一天
     */
    public static boolean isSameDay(Calendar cal1, Calendar cal2) {
        if (cal1 != null && cal2 != null) {
            return cal1.get(0) == cal2.get(0) && cal1.get(1) == cal2.get(1) && cal1.get(6) == cal2.get(6);
        } else {
            throw new IllegalArgumentException("日期不能为null");
        }
    }

    /**
     * 将字符串写入某路径下的文件中
     *
     * @param filePath 加文件名的路径
     * @param msg      待写入文件的数据信息
     */
    public static void writeStringToFile(String filePath, String msg) {
        try {
            File file = new File(filePath);
            //判断下文件是否存在
            if (!file.isFile() && !file.exists()) {
                file.createNewFile();
            }
            try(PrintStream ps = new PrintStream(file, "gbk")) {
                // 往文件里写入字符串
                ps.println(msg);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取32位的UUID数据
     */
    public static String getUUID32() {
        return "{" + UUID.randomUUID().toString().toUpperCase() + "}";
    }

    /**
     * 数据类型配置
     */
    public static String getMatchStr(String reg, String str) {
        Pattern pattern = Pattern.compile(reg);
        Matcher matcher = pattern.matcher(str);
        return matcher.replaceAll("");
    }

    /**
     * 获取当前日期是第几周
     *
     * @return
     * @throws Exception
     */
    public static int getThisWeek() throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String str = sdf.format(new Date());
        Date date = sdf.parse(str);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        //第几周DAY_OF_WEEK_IN_MONTH
        int week = calendar.get(Calendar.DAY_OF_WEEK_IN_MONTH);
        //在某月的第几周
        //int week = calendar.get(Calendar.WEEK_OF_MONTH);
        //第几天，从周日开始

        int day = calendar.get(Calendar.DAY_OF_WEEK);
        return week;
    }

    /**
     * 将实体转为map包括父类
     *
     * @param obj
     * @return
     * @throws IllegalAccessException
     */
    public static Map<String, Object> objectToMap(Object obj) throws IllegalAccessException {
        Map<String, Object> map = new HashMap<>();
        Class<?> clazz = obj.getClass();
        for (; clazz != Object.class; clazz = clazz.getSuperclass()) {//向上循环 遍历父类
            Field[] field = clazz.getDeclaredFields();
            for (Field f : field) {
                if (needFilterField(f)) {
                    continue;
                }
                f.setAccessible(true);
                Object value = f.get(obj);
                if (value != null) {
                    map.put(f.getName(), f.get(obj).toString());
                }
            }
        }
        return map;
    }

    /**
     * 过滤不需要属性
     *
     * @param field
     * @return
     */
    private static Boolean needFilterField(Field field) {
        // 过滤静态属性
        if (Modifier.isStatic(field.getModifiers())) {
            return true;
        }
        // 过滤transient 关键字修饰的属性
        if (Modifier.isTransient(field.getModifiers())) {
            return true;
        }
        return false;
    }


    /**
     * 全角字符转换为半角字符
     *
     * @param fullWidthStr 全角字符
     * @return 半角字符
     */

    public static String fullWidth2halfWidth(String fullWidthStr) {
        if (null == fullWidthStr || fullWidthStr.length() == 0) {
            return "";
        }
        char[] charArray = fullWidthStr.toCharArray();
        for (int i = 0; i < charArray.length; i++) {
            int charIntValue = (int) charArray[i];
            if (charIntValue >= 65281 && charIntValue <= 65374) {
                charArray[i] = (char) (charIntValue - 65248);
            } else if (charIntValue == 12288) {
                charArray[i] = (char) 32;
            }
        }
        return new String(charArray);

    }

    /**
     *字符是否是数字和字母
     * */
    public static boolean isWord(char c) {
        String regEx = "^[A-Za-z0-9]+$";
        Pattern pattern = Pattern.compile(regEx);
        Matcher m = pattern.matcher("" + c);
        return m.matches();
    }


    public static String findBankNoByBankName(String bankName,String str){
        String bankNo = "";
        if(!"".equals(bankName) && !"".equals(str)){
            bankName = bankName.substring(0, bankName.indexOf("行")+1);
            String[] s = str.split("#");
            for(String bank : s){
                String[] bankInfo = bank.split("=");
                if(bankInfo[0].contains(bankName)){
                    bankNo = bankInfo[1];
                    break;
                }
            }
        }
        return bankNo;
    }

    /**
     *
     * @param input
     * @return 全角字符转换为半角字符
     */
    public static String ToDBC(String input) {
        char[] c = input.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] > 65280 && c[i] < 65375) {
                c[i] = (char) (c[i] - 65248);
            }
        }

        return new String(c);
    }

    public static boolean limitPaymentTime(String payTime){
        boolean flag = false;
        SimpleDateFormat sdf = new SimpleDateFormat("HH");
        String hours = sdf.format(new Date());
        //String s = "02:00-22:00";
        if(null!=payTime && !"".equals(payTime)){
            String [] s1 = payTime.split("-");
            if(Integer.parseInt(hours)>= Integer.parseInt(s1[0].split(":")[0]) &&
                    Integer.parseInt(hours)<Integer.parseInt(s1[1].split(":")[0])){
                flag = true;
                System.out.println("可缴费");
            }
        }
        return flag;
    }

    public static int daysBetween(String smdate,String bdate){
        SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMdd");
        try {
            Calendar cal = Calendar.getInstance();
            cal.setTime(sdf.parse(smdate));
            long time1 = cal.getTimeInMillis();
            cal.setTime(sdf.parse(bdate));
            long time2 = cal.getTimeInMillis();
            long betweenDays=(time2-time1)/(1000*3600*24);
            return Integer.parseInt(String.valueOf(betweenDays));
        } catch (ParseException ignored) {
        }
        return -999999;
    }

    /**
     * 将一组数据平均分成n组
     * @param source 要分组的数据源
     * @param n      平均分成n组
     * @param <T>
     * @return
     */
    public static <T> List<List<T>> averageAssign(List<T> source, int n) {
        List<List<T>> result = new ArrayList<List<T>>();
        int remainder = source.size() % n;  //(先计算出余数)
        int number = source.size() / n;  //然后是商
        int offset = 0;//偏移量
        for (int i = 0; i < n; i++) {
            List<T> value = null;
            if (remainder > 0) {
                value = source.subList(i * number + offset, (i + 1) * number + offset + 1);
                remainder--;
                offset++;
            } else {
                value = source.subList(i * number + offset, (i + 1) * number + offset);
            }
            result.add(value);
        }
        return result;
    }

    /**
     * 将一组数据固定分组，每组n个元素
     * @param source 要分组的数据源
     * @param n      每组n个元素
     * @param <T>
     * @return
     */
    public static <T> List<List<T>> fixedGrouping(List<T> source, int n) {
        if (null == source || source.size() == 0 || n <= 0) {
            return null;
        }
        List<List<T>> result = new ArrayList<List<T>>();
        int sourceSize = source.size();
        int size = (sourceSize % n) == 0 ? (sourceSize / n) : ((source.size() / n) + 1);
        for (int i = 0; i < size; i++) {
            List<T> subset = new ArrayList<T>();
            for (int j = i * n; j < (i + 1) * n; j++) {
                if (j < sourceSize) {
                    subset.add(source.get(j));
                }
            }
            result.add(subset);
        }
        return result;
    }

}
