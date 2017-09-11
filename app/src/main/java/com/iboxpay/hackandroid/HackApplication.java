package com.iboxpay.hackandroid;

import android.app.Application;
import com.avos.avoscloud.AVOSCloud;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangjun on 2017/9/4.
 */

public class HackApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        // 初始化参数依次为 this, AppId, AppKey
        AVOSCloud.initialize(this, "wRjV2GkEc3EoLBrIxollfTV2-gzGzoHsz", "5DbaYzRTLT2n1FCRq5Ov23qQ");
        AVOSCloud.setDebugLogEnabled(true);
    }

    private List<String> HackedProcesses = new ArrayList();

    public void add(String activityName) {

        HackedProcesses.add(activityName);
    }

    public void clear() {
        HackedProcesses.clear();
    }

    public boolean isHacked(String activityName) {
        return HackedProcesses.contains(activityName);
    }
}
