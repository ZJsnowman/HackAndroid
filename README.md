# 什么是 Activity 劫持
如果在启动一个 Activity 时，给它加入一个标志位 FLAG_ACTIVITY_NEW_TASK，就能使它置于栈顶并立马
呈现给用户。但是这样的设计却有一个缺陷。如果这个 Activity 是用于盗号的伪装 Activity 呢？
在 Android 系统当中，程序可以枚举当前运行的进程而不需要声明其他权限，这样子我们就可以写一个程序，
启动一个后台的服务，这个服务不断地扫描当前运行的进程，当发现目标进程启动时，就启动一个伪装的
 Activity。如果这个 Activity 是登录界面，那么就可以从中获取用户的账号密码。

 一个运行在后台的服务可以做到如下两点：
 1. 决定哪一个 activity 运行在前台  
 2. 运行自己 app 的 activity 到前台


 这样，恶意的开发者就可以对应程序进行攻击了，对于有登陆界面的应用程序，他们可以伪造一个一模一样
 的界面，普通用户根本无法识别是真的还是假。用户输入用户名和密码之后，恶意程序就可以悄无声息的
 把用户信息上传到服务器了。这样是非常危险的。
 
# 如何劫持的
参考 HackService

ps:5.0 以上上述劫持方法失效,hack程序不在前台的时暂时获取不到当前运行 activity.

# 如何反劫持
使用 AntiHijackingUtil 即可.
用法很简单，只需要在需要使用检测方法的Activity的 onStop()方法中调用工具类的 checkActivity()方法，接收返回
的 boolean值进行判断即可，下面是一个简单示例：
```
@Override
protected void onStop() {
    super.onStop();
    boolean safe = AntiHijackingUtil.checkActivity(this);
    if (safe){
        Toast.markText(this, "安全", Toast.LENGTH_LONG).show;
    } else {
        Toast.makeText(this, "不安全", Toast.LENGTH_LONG).show;
    }
}
```

