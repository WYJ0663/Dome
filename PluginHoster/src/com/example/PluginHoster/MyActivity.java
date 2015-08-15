package com.example.PluginHoster;

import android.app.Activity;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;
import dalvik.system.DexClassLoader;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class MyActivity extends Activity {

    Class localClass;
    private Object instance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

           loadResources();

        //      setContentView(R.layout.main);
             LoadAPK(savedInstanceState);
        String path = Environment.getExternalStorageDirectory() + "/DynamicLoadHost/";
        String filename = "plugin.apk";

        System.out.println(path + filename);

        File file = new File(path + filename);

        if (file.exists()) {
            Log.e("Y", "文件存在---");
        } else {
            Log.e("Y", "文件不存在");
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        Method start;
        try {
            start = localClass.getMethod("onStart");
            start.invoke(instance);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        Method resume;
        try {
            resume = localClass.getMethod("onResume");
            resume.invoke(instance);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Method pause;
        try {
            pause = localClass.getMethod("onPause");
            pause.invoke(instance);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        try {
            Method stop = localClass.getMethod("onStop");
            stop.invoke(instance);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            Method des = localClass.getMethod("onDestroy");
            des.invoke(instance);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void LoadAPK(Bundle bundle) {
        String path = Environment.getExternalStorageDirectory() + "/DynamicLoadHost/";
        String filename = "plugin.apk";

        System.out.println(path + filename);
        // 4.1以后不能够将optimizedDirectory设置到sd卡目录， 否则抛出异常.
        File optimizedDirectoryFile = getDir("dex", 0);
        DexClassLoader classLoader = new DexClassLoader(path + filename, optimizedDirectoryFile.getAbsolutePath(),
                null, getClassLoader());

        try {
            // 通过反射机制调用， 包名为com.example.loaduninstallapkdemo, 类名为UninstallApkActivity
            localClass = classLoader.loadClass("com.example.Plugin.MyActivity");
            Constructor constructor = localClass.getConstructor(new Class[]{});
            instance = constructor.newInstance(new Object[]{});

            // 获取sayHello方法
            Method helloMethod = localClass.getMethod("test", null);
            helloMethod.setAccessible(true);
            helloMethod.invoke(instance, null);


            Method localMethodSetActivity = localClass.getMethod("setActivity", Activity.class);
            localMethodSetActivity.setAccessible(true);
            localMethodSetActivity.invoke(instance, MyActivity.this);


            Method onCreate = localClass.getDeclaredMethod("onCreate", new Class[]{Bundle.class});
            onCreate.setAccessible(true);

            onCreate.invoke(instance, new Object[]{bundle});

        } catch (Exception e) {
            Toast.makeText(MyActivity.this, "没有找到插件", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }


    AssetManager mAssetManager;
    Resources mResources;
    Resources.Theme mTheme;


    protected void loadResources() {
        String path = Environment.getExternalStorageDirectory() + "/DynamicLoadHost/";
        String filename = "plugin.apk";
        String mDexPath = path + filename;

        System.out.println(mDexPath);
        try {
            AssetManager assetManager = AssetManager.class.newInstance();
            Method addAssetPath = assetManager.getClass().getMethod("addAssetPath", String.class);
            addAssetPath.invoke(assetManager, mDexPath);
            mAssetManager = assetManager;
        } catch (Exception e) {
            e.printStackTrace();
        }
        Resources superRes = super.getResources();
        mResources = new Resources(mAssetManager, superRes.getDisplayMetrics(),
                superRes.getConfiguration());
        mTheme = mResources.newTheme();
        mTheme.setTo(super.getTheme());
    }

    //Assets资源
    @Override
    public AssetManager getAssets() {
        return mAssetManager == null ? super.getAssets() : mAssetManager;
    }

    //Resources资源
    @Override
    public Resources getResources() {
        return mResources == null ? super.getResources() : mResources;
    }


}