package com.example.PluginHostFragment;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.Toast;
import dalvik.system.DexClassLoader;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class MyActivity extends Activity {
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        loadResources();
        FrameLayout frameLayout = new FrameLayout(this);
        int id = 999999999;
        frameLayout.setId(id);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);

        frameLayout.setBackgroundColor(Color.parseColor("#dd0000"));
        setContentView(frameLayout, params);


        Fragment fragment = load();


            if (fragment != null) {
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager
                        .beginTransaction();
                fragmentTransaction.replace(id, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

        }
    }

    Class localClass;
    private Object instance;

    public Fragment load() {

        String path = Environment.getExternalStorageDirectory() + File.separator + "/DynamicLoadHost/";
        String filename = "PluginFragment.apk";

        File file = new File(path + filename);

        if (file.exists()) {
            Log.e("Y", "文件存在---");
        } else {
            Log.e("Y", "文件不存在");
        }

        // 4.1以后不能够将optimizedDirectory设置到sd卡目录， 否则抛出异常.
        File optimizedDirectoryFile = getDir("dex", 0);
        DexClassLoader classLoader = new DexClassLoader(path + filename, optimizedDirectoryFile.getAbsolutePath(),
                null, getClassLoader());

        try {
            // 通过反射机制调用， 包名为com.example.loaduninstallapkdemo, 类名为UninstallApkActivity
            localClass = classLoader.loadClass("com.example.PluginFragment.PluginFragment");
            Constructor constructor = localClass.getConstructor(new Class[]{});
            instance = constructor.newInstance(new Object[]{});

            return (Fragment) instance;
        } catch (Exception e) {
            Toast.makeText(MyActivity.this, "没有找到插件", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

        return null;

    }


    AssetManager mAssetManager;
    Resources mResources;
    Resources.Theme mTheme;


    protected void loadResources() {
        String path = Environment.getExternalStorageDirectory() + File.separator + "/DynamicLoadHost/";
        String filename = "PluginFragment.apk";
        String mDexPath = path + filename;

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