package com.example.PluginHost;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import dalvik.system.DexClassLoader;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class MyActivity extends Activity {
    /**
     * Called when the activity is first created.
     */

    private Button mButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        mButton = (Button) findViewById(R.id.start);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadUninstallApk();
            }
        });

    }


    /**
     * @return void
     * @throws
     * @Title: loadUninstallApk
     * @Description: 动态加载未安装的apk
     */
    private void loadUninstallApk() {
        String path = Environment.getExternalStorageDirectory() + File.separator+"/DynamicLoadHost/";
        String filename = "plugin.apk";

        // 4.1以后不能够将optimizedDirectory设置到sd卡目录， 否则抛出异常.
        File optimizedDirectoryFile = getDir("dex", 0);
        DexClassLoader classLoader = new DexClassLoader(path + filename, optimizedDirectoryFile.getAbsolutePath(),
                null, getClassLoader());

        try {
            // 通过反射机制调用， 包名为com.example.loaduninstallapkdemo, 类名为UninstallApkActivity
            Class mLoadClass = classLoader.loadClass("com.example.Plugin.MyActivity");
            Constructor constructor = mLoadClass.getConstructor(new Class[]{});
            Object testActivity = constructor.newInstance(new Object[]{});

            // 获取sayHello方法
            Method helloMethod = mLoadClass.getMethod("sayHello", null);
            helloMethod.setAccessible(true);
            Object content = helloMethod.invoke(testActivity, null);
            Toast.makeText(MyActivity.this, content.toString(), Toast.LENGTH_LONG).show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
