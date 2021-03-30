package com.enhance.log;

import android.os.Environment;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import de.mindpipe.android.logging.log4j.LogConfigurator;


/**
 * @Description:
 * @Author: huangmin
 * @Date: 2020/4/20 10:11 PM
 */
public class XLog {
    private static Logger logger = Logger.getLogger(XLog.class.getSimpleName());

    public static void init(boolean enable,String fileName){
        final LogConfigurator logConfigurator = new LogConfigurator();
        //设置文件名
        logConfigurator.setFileName(fileName);
        //设置root日志输出级别 默认为DEBUG
        logConfigurator.setRootLevel(enable ? Level.ALL : Level.OFF);
        // 设置日志输出级别
        logConfigurator.setLevel("org.apache", enable ? Level.ALL : Level.OFF);
        //设置 输出到日志文件的文字格式 默认 %d %-5p [%c{2}]-[%L] %m%n
        logConfigurator.setFilePattern("%d %-5p [%c{2}]-[%L] %m%n");
        //设置输出到控制台的文字格式 默认%m%n
        logConfigurator.setLogCatPattern("%m%n");
        //设置总文件大小
        logConfigurator.setMaxFileSize(2 * 1024 * 1024);
        //设置最大产生的文件个数
        logConfigurator.setMaxBackupSize(10);
        //设置所有消息是否被立刻输出 默认为true,false 不输出
        logConfigurator.setImmediateFlush(true);
        //是否本地控制台打印输出 默认为true ，false不输出
        logConfigurator.setUseLogCatAppender(true);
        //设置是否启用文件附加,默认为true。false为覆盖文件
        logConfigurator.setUseFileAppender(true);
        //设置是否重置配置文件，默认为true
        logConfigurator.setResetConfiguration(true);
        //是否显示内部初始化日志,默认为false
        logConfigurator.setInternalDebugging(false);
        logConfigurator.configure();
    }

    public static void init(boolean enable) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd", Locale.US);
        //日志文件的绝对路径
        String fileName = Environment.getExternalStorageDirectory()
                + File.separator
                + "XLog"
                + File.separator
                + formatter.format(new Date())
                + ".log";
        init(enable,fileName);
    }

    public static void d(Object msg) {
        String name = getFunctionName();
        if (name != null) {
            logger.debug(name + " - " + msg);
        } else {
            logger.debug(msg);
        }
    }

    public static void i(Object msg) {
        String name = getFunctionName();
        if (name != null) {
            logger.info(name + " - " + msg);
        } else {
            logger.info(msg);
        }
    }

    public static void w(Object msg) {
        String name = getFunctionName();
        if (name != null) {
            logger.warn(name + " - " + msg);
        } else {
            logger.warn(msg);
        }
    }

    public static void e(Object msg) {
        String name = getFunctionName();
        if (name != null) {
            logger.error(name + " - " + msg);
        } else {
            logger.error(msg);
        }
    }

    /**
     * 获取方法名
     *
     * @return
     */
    private static String getFunctionName() {
        StackTraceElement[] sts = Thread.currentThread().getStackTrace();
        if (sts == null) {
            return null;
        }
        for (StackTraceElement st : sts) {
            if (st.isNativeMethod()) {
                continue;
            }
            if (st.getClassName().equals(Thread.class.getName())) {
                continue;
            }
            if (st.getClassName().equals(XLog.class.getName())) {
                continue;
            }
            return "[ "
                    + Thread.currentThread().getName() + ": "
                    + st.getFileName().replace(".java", "") + ":" + st.getLineNumber() + " ]";
        }
        return null;
    }
}
