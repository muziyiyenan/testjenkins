package com.yonyou.myself.util;

import java.io.Serializable;

/**
 * @version 1.0
 * @Author :liyanan
 * @Date : 2021/2/22 14:27
 * @Description : TODO
 ***/
public class BaseInfo implements Serializable {

    private static final long serialVersionUID = -3340492053093744832L;

    @DesensitizedAnnotation(type=TypeEnum.PERSON_NAME)
    private  String custName;

    @DesensitizedAnnotation(type=TypeEnum.PERSON_CERT_NO)
    private String certNo;
}
