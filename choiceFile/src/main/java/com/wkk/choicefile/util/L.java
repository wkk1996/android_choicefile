package com.wkk.choicefile.util;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

/**
 * Created by wkk on 2017/5/25/025.
 */
public class L {
    private static final String TAG = "wkk_log";
    private static final String LINE_SEPARATOR = System.getProperty("line.separator");

    private static void log(String log, String callInfo, String lv) {
        if (log == null) {
            log = "null";
        }
        if ((log.startsWith("{") && log.endsWith("}")) || (log.startsWith("[") && log.endsWith("]"))) {
            logJson(log, callInfo, lv);
            return;
        }
        switch (lv) {
            case "D":
                android.util.Log.d(TAG, callInfo + " " + log);
                break;
            case "I":
                android.util.Log.i(TAG, callInfo + " " + log);
                break;
            case "W":
                android.util.Log.w(TAG, callInfo + " " + log);
                break;
            case "E":
                android.util.Log.e(TAG, callInfo + " " + log);
                break;
        }
    }

    public static void d(Object log) {
        String callInfo = getCallInfo();
        log(getLog(log), callInfo, "D");
    }

    public static void i(Object log) {
        String callInfo = getCallInfo();
        log(getLog(log), callInfo, "I");
    }

    public static void w(Object log) {
        String callInfo = getCallInfo();
        log(getLog(log), callInfo, "W");
    }

    public static void e(Object log) {
        String callInfo = getCallInfo();
        log(getLog(log), callInfo, "E");
    }

    //**********************************************************************************************
    public static void d(Object log, boolean callLine) {
        String callInfo = getCallInfo();
        if (callLine) {
            log(getLog(log), callInfo, "D");
        } else {
            log(getLog(log), "", "D");
        }
    }

    public static void i(Object log, boolean callLine) {
        String callInfo = getCallInfo();
        if (callLine) {
            log(getLog(log), callInfo, "I");
        } else {
            log(getLog(log), "", "I");
        }
    }

    public static void w(Object log, boolean callLine) {
        String callInfo = getCallInfo();
        if (callLine) {
            log(getLog(log), callInfo, "W");
        } else {
            log(getLog(log), "", "W");
        }
    }

    public static void e(Object log, boolean callLine) {
        String callInfo = getCallInfo();
        if (callLine) {
            log(getLog(log), callInfo, "E");
        } else {
            log(getLog(log), "", "E");
        }
    }

    //**********************************************************************************************


    private static String getLog(Object log) {
        if (log instanceof Throwable) {
            Throwable throwable = (Throwable) log;
            Writer writer = new StringWriter();
            PrintWriter printWriter = new PrintWriter(writer);
            throwable.printStackTrace(printWriter);
            Throwable cause = throwable.getCause();
            while (cause != null) {
                cause.printStackTrace(printWriter);
                cause = cause.getCause();
            }
            printWriter.close();
            return writer.toString();
        }
        return String.valueOf(log);
    }

    private static void logJson(String json, String callInfo, String lv) {
        String message;
        try {
            if (json.startsWith("{")) {
                JSONObject jsonObject = new JSONObject(json);
                message = jsonObject.toString(4);//最重要的方法，就一行，返回格式化的json字符串，其中的数字4是缩进字符数
            } else if (json.startsWith("[")) {
                JSONArray jsonArray = new JSONArray(json);
                message = jsonArray.toString(4);
            } else {
                message = json;
            }
        } catch (JSONException e) {
            message = json;
        }
        log("╔═══════════════════════════════════════════════════════════════════════════════════════", callInfo, lv);
        String[] lines = message.split(LINE_SEPARATOR);
        for (String line : lines) {
            log("║ " + line, callInfo, lv);
        }
        log("╚═══════════════════════════════════════════════════════════════════════════════════════", callInfo, lv);
    }

    private static String getCallInfo() {
        StackTraceElement[] stacktrace = Thread.currentThread().getStackTrace();
        StackTraceElement e = stacktrace[4];
        return "(" + e.getFileName() + ":" + e.getLineNumber() + ")";
    }

}