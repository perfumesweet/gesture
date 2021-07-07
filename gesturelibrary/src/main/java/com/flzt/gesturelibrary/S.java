package com.flzt.gesturelibrary;

import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;


public class S {

    public static void s(Object... msg) {
        if (BuildConfig.DEBUG) {

            String[] info = getAutoJumpLogInfo();

            StringBuilder sb = new StringBuilder();
            for (Object str : msg) {
                sb.append("  ").append(str);
            }

            if (sb.length() < 56)
                Log.d("bag", String.format("%-56s", sb) + " (*_*) " + info[2]);
            else {
                Log.d("bag", " (*_*) " + info[2] + "\n" + sb);
            }
        }

    }


    /**
     * 获取打印信息所在方法名，行号等信息
     *
     * @return String[]
     */
    private static String[] getAutoJumpLogInfo() {
        String[] info = new String[]{"", "", ""};
        StackTraceElement[] elements = Thread.currentThread().getStackTrace();
        if (elements.length < 5) {
            Log.e("bag", "Stack is too shallow!!!");
        } else {

            info[0] = elements[4].getClassName().substring(
                    elements[4].getClassName().lastIndexOf(".") + 1);
            info[1] = elements[4].getMethodName();
//            info[2] = "at " + elements[4];
            info[2] = "(" + info[0].replaceAll("\\$[0-9]+", "") + ".java:" + elements[4].getLineNumber() + ") "
                    + elements[4].getClassName().replaceFirst("[a-z]*.[a-z]*.[a-z]*.", "")
                    + "." + info[1];
        }

        return info;
    }


    /**
     * 改变文本颜色
     */

    private static CharSequence changColor(String str, int start, int end, int color) {
        SpannableStringBuilder styleMsg = new SpannableStringBuilder(str);
        if (start < 0)
            start = 0;
        if (end < 0)
            end = str.length();
        styleMsg.setSpan(new ForegroundColorSpan(color), start, end, Spannable.SPAN_INCLUSIVE_INCLUSIVE);

        return styleMsg;
    }
}
