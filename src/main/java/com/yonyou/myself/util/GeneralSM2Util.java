package com.yonyou.myself.util;

import com.ufgov.sm.SM2Utils;

import java.util.Map;

/**
 * @version 1.0
 * @Author :liyanan
 * @Date : 2021/2/23 16:36
 * @Description : TODO
 ***/
public class GeneralSM2Util {

    public static String SM2Pub="";

    public static String SM2Pri="";

    /**
     * 自定义SM2算法生成秘钥对
     * */
  static {
        Map keyPair = SM2Utils.generateKeyPair();
        if (SM2Pub==""||"".equals(SM2Pub)) {
            setSM2Pub(keyPair.get(SM2Utils.PUBLIC_KEY).toString());
        }
        if (SM2Pri==""||"".equals(SM2Pri)) {
            setSM2Pri(keyPair.get(SM2Utils.PRIVATE_KEY).toString());
        }
        System.err.println( SM2Pri+"  _   "+SM2Pub);
    }

    public static String getSM2Pub() {
        return SM2Pub;
    }

    public static void setSM2Pub(String SM2Pub) {
        GeneralSM2Util.SM2Pub = SM2Pub;
    }

    public static String getSM2Pri() {
        return SM2Pri;
    }

    public static void setSM2Pri(String SM2Pri) {
        GeneralSM2Util.SM2Pri = SM2Pri;
    }
}
