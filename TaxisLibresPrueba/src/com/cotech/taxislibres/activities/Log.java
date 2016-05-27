package com.cotech.taxislibres.activities;



public class Log {
    static final boolean LOG = false;

    public static void i(String tag, String string) {
        if (LOG) android.util.Log.i(tag, string);
        else	android.util.Log.i("SIN LOG.I: ", "-----");	
    }
    public static void e(String tag, String string) {
        if (LOG) android.util.Log.e(tag, string);
        else	android.util.Log.e("SIN LOG.E: ", "-----");
    }
    public static void d(String tag, String string) {
        if (LOG) android.util.Log.d(tag, string);
        else	android.util.Log.d("SIN LOG.D: ", "-----");
    }
    public static void v(String tag, String string) {
        if (LOG) android.util.Log.v(tag, string);
        else	android.util.Log.v("SIN LOG.V: ", "-----");
    }
    public static void w(String tag, String string) {
        if (LOG) android.util.Log.w(tag, string);
        else	android.util.Log.w("SIN LOG.W: ", "-----"); 
    }}
