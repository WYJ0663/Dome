package com.example.Plugin;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


public class MyActivity extends Activity {
    /**
     * Called when the activity is first created.
     */

    public String sayHello() {
        return "Hello, this apk is not installed";
    }


    private Activity otherActivity;

    public void test() {
        Log.i("sys", "测试方法执行了");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // 测试DexClassLoader 动态加载未安装Apk中的类
        TextView t = new TextView(otherActivity);
        t.setText("我是测试插件");
        otherActivity.setContentView(t);// R.layout.frist_activity_main

        t.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(otherActivity, "点击插件", Toast.LENGTH_SHORT).show();
            }
        });
        Log.i("sys", "Fragment项目启动了");
    }

    public void setActivity(Activity paramActivity) {
        Log.d("sys", "setActivity...");
        this.otherActivity = paramActivity;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.i("sys", "OnSaveInstance被调了");
    }

    @Override
    public void onStart() {
        Log.i("sys", "onStart被调了");
        // TODO Auto-generated method stub
        super.onStart();
    }

    @Override
    public void onResume() {
        Log.i("sys", "onResume被调了");
        // TODO Auto-generated method stub
        super.onResume();
    }

    @Override
    public void onPause() {
        Log.i("sys", "onPause被调了");
        // TODO Auto-generated method stub
        super.onPause();
    }

    @Override
    public void onStop() {
        Log.i("sys", "onStop被调了");
        // TODO Auto-generated method stub
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.i("sys", "onDestroy被调了");
        // TODO Auto-generated method stub
        super.onDestroy();
    }
}
