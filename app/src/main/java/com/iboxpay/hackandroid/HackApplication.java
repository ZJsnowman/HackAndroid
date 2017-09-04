package com.iboxpay.hackandroid;

import android.app.Application;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangjun on 2017/9/4.
 */

public class HackApplication extends Application {

    private List<String> HackedProcesses = new ArrayList();

    public void add(String processName) {

        HackedProcesses.add(processName);
    }

    public void clear() {
        HackedProcesses.clear();
    }

    public boolean isHacked(String processName) {
        return HackedProcesses.contains(processName);
    }
}
