package com.yonyou.myself.log4j;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.spi.LoggingEvent;

/**
 * @version 1.0
 * @Author :liyanan
 * @Date : 2021/2/24 16:33
 * @Description : TODO
 ***/
public class DesensitizedConsoleAppender extends AppenderSkeleton {

    @Override
    public void close() {
        if (this.closed) {
            return;
        }
        this.closed = true;
    }

    @Override
    public boolean requiresLayout() {
        return true;
    }

    @Override
    protected void append(LoggingEvent loggingEvent) {
        System.out.println("输出日主"+loggingEvent.getMessage());
        loggingEvent.setProperty("INFO",  loggingEvent.getMessage()+"");
        super.layout.format(loggingEvent);

    }
}
