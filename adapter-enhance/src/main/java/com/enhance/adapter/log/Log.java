package com.enhance.adapter.log;


/**
 * @Description:
 * @Author: huangmin
 * @Date: 2020/9/4 11:14 AM
 */
public class Log {
    private static final String TAG = Log.class.getSimpleName();
    public static boolean isPrint = true;

    public static void d(String msg) {
        if (!isPrint) {
            return;
        }
        String name = getFunctionName();
        if (name != null) {
            android.util.Log.d(TAG, name + " - " + msg);
        } else {
            android.util.Log.d(TAG, msg);
        }
    }

    public static void i(String msg) {
        if (!isPrint) {
            return;
        }
        String name = getFunctionName();
        if (name != null) {
            android.util.Log.i(TAG, name + " - " + msg);
        } else {
            android.util.Log.i(TAG, msg);
        }
    }

    public static void w(String msg) {
        if (!isPrint) {
            return;
        }
        String name = getFunctionName();
        if (name != null) {
            android.util.Log.w(TAG, name + " - " + msg);
        } else {
            android.util.Log.w(TAG, msg);
        }
    }

    public static void e(String msg) {
        if (!isPrint) {
            return;
        }
        String name = getFunctionName();
        if (name != null) {
            android.util.Log.e(TAG, name + " - " + msg);
        } else {
            android.util.Log.e(TAG, msg);
        }
    }

    /**
     * 获取方法名
     * Get method name
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
            if (st.getClassName().equals(Log.class.getName())) {
                continue;
            }
            return "[ "
                    + Thread.currentThread().getName() + ": "
                    + st.getFileName().replace(".java", "") + ":" + st.getLineNumber() + " ]";
        }
        return null;
    }
}
