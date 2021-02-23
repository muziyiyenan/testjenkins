package com.yonyou.myself.util;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @version 1.0
 * @Author :liyanan
 * @Date : 2021/2/23 10:14
 * @Description : TODO
 ***/
class DesensitizedUtilsTest {


    @Test
    void tagReplace() {
        String source = "<abc>skksfsf12123</abc><tag>什么</tag><abc>skksfsf12123</abc>" +
                "<tag>1212121212121</tag>";
        Assert.assertEquals("<abc>skksfsf12123</abc><tag>什*</tag><abc>skksfsf12123</abc><tag>121*******121</tag>",DesensitizedUtils.tagReplace(source, "tag"));
    }

    @Test
    void addLableForNum() {
        Assert.assertEquals("*******什么",DesensitizedUtils.addLableForNum("什么",9,"*"));
    }
}