package com.yonyou.myself.util;

import com.ufgov.sm.SM4Utils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @version 1.0
 * @Author :liyanan
 * @Date : 2021/2/23 16:53
 * @Description : TODO
 ***/
class GeneralSM2UtilTest {

    @Test
    void sm2GetKey() {
        String  pub="04D5D508C5C9CD6AEAFC7E4811E69500BA3C41E753DD2B9AD0C268EC912BFCBB85E9A5BED087EAEE03FB54F2162F596DE53E02C03372CAB8906B3F9AB70B0ED5ED";

        String  pri="0080A44DF48C017A91CAC141A408DC4813D0FE69CB984FF0E138855C4D4134127D";
     /*   System.out.println(GeneralSM2Util.SM2Pub);
        System.out.println(GeneralSM2Util.SM2Pri);*/
        System.out.println(SM4Utils.encryptData_ECB("11","1234567890123456"));
    }
}