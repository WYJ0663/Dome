package com.example.ContentProviderServer;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.net.Uri;

/**
 *
 * @author chenzheng_java
 * @description 自定义的内容提供者.
 *  总结下访问内容提供者的主要步骤： 
 * 第一：我们要有一个uri,这就相当于我们的网址，我们有了网址才能去访问具体的网站 
 * 第二：我们去系统中寻找该uri中的authority（可以理解为主机地址）， 
 *      只要我们的内容提供者在manifest.xml文件中注册了，那么系统中就一定存在。 
 * 第三：通过内容提供者内部的uriMatcher对请求进行验证（你找到我了，还不行，我还得看看你有没有权限访问我呢）。 
 * 第四：验证通过后，就可以调用内容提供者的增删查改方法进行操作了 
 */
public class PersonProvider extends ContentProvider {
    // 自己实现的数据库操作帮助类  
    private MyOpenHelper myOpenHelper;

    // 数据库相关类  
    private SQLiteDatabase sqLiteDatabase;

    // uri匹配相关  
    private static UriMatcher uriMatcher;

    // 主机名称(这一部分是可以随便取得)  
    private static final String authority = "cn.com.chenzheng_java.hello";

    // 注册该内容提供者匹配的uri  
    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);  
        /* 
         * path_chenzheng部分的字符串是随便取得，1代表着如果请求的uri与当前加入 
         * 的匹配uri正好吻合，uriMathcher.match()方法返回的值.#代表任意数字，*代表任意字符串 
         */
        uriMatcher.addURI(authority, "path_chenzheng", 1);// 代表当前表中的所有的记录  
        uriMatcher.addURI(authority, "path_chenzheng/#", 2);// 代表当前表中的某条特定的记录，记录id便是#处得数字  
    }

    // 数据表中的列名映射  
    private static final String _id = "id";
    private static final String name = "name";
    private static final String age = "age";
    private static final String isMan = "isMan";

    /**
     * @description 当内容提供者第一次创建时执行
     */
    @Override
    public boolean onCreate() {
        try {
            myOpenHelper = new MyOpenHelper(getContext(), DB_Name, null,
                    Version_1);

        } catch (Exception e) {

            return false;
        }
        return true;
    }

    /**
     * @description 对数据库进行删除操作的时候执行
     *              android.content.ContentUri为我们解析uri相关的内容提供了快捷方便的途径 
     */
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int number = 0;
        sqLiteDatabase = myOpenHelper.getWritableDatabase();
        int code = uriMatcher.match(uri);
        switch (code) {
            case 1:
                number = sqLiteDatabase
                        .delete(Table_Name, selection, selectionArgs);
                break;
            case 2:
                long id = ContentUris.parseId(uri);
            /* 
             * 拼接where子句用三目运算符是不是特烦人啊？ 实际上，我们这里可以用些技巧的. 
             * if(selection==null||"".equals(selection.trim())) selection = 
             * " 1=1 and "; selection+=_id+"="+id; 
             * 拼接where子句中最麻烦的就是and的问题，这里我们通过添加一个1=1这样的恒等式便将问题解决了 
             */
                selection = (selection == null || "".equals(selection.trim())) ? _id
                        + "=" + id
                        : selection + " and " + _id + "=" + id;
                number = sqLiteDatabase
                        .delete(Table_Name, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("异常参数");
        }

        return number;
    }

    /**
     *@description 获取当前内容提供者的MIME类型 集合类型必须添加前缀vnd.android.cursor.dir/（该部分随意） 
     *              单条记录类型添加前缀vnd,android.cursor.item/（该部分随意） 
     *              定义了该方法之后，系统会在第一次请求时进行验证，验证通过则执行crub方法时不再重复进行验证， 
     *              否则如果没有定义该方法或者验证失败，crub方法执行的时候系统会默认的为其添加类型验证代码。 
     */
    @Override
    public String getType(Uri uri) {
        int code = uriMatcher.match(uri);
        switch (code) {
            case 1:
                return "vnd.android.cursor.dir/chenzheng_java";
            case 2:
                return "vnd.android.cursor.item/chenzheng_java";
            default:
                throw new IllegalArgumentException("异常参数");
        }

    }

    /**
     * @description 对数据表进行insert时执行该方法
     */
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        sqLiteDatabase = myOpenHelper.getWritableDatabase();
        int code = uriMatcher.match(uri);
        switch (code) {
            case 1:
                sqLiteDatabase.insert(Table_Name, name, values);
                break;
            case 2:
                long id = sqLiteDatabase.insert(Table_Name, name, values);
                // withAppendId将id添加到uri的最后
                ContentUris.withAppendedId(uri, id);
                break;
            default:
                throw new IllegalArgumentException("异常参数");
        }

        return uri;
    }

    /**
     * 当执行查询时调用该方法 
     */
    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        Cursor cursor = null;
        sqLiteDatabase = myOpenHelper.getReadableDatabase();
        int code = uriMatcher.match(uri);
        switch (code) {
            case 1:
                cursor = sqLiteDatabase.query(Table_Name, projection, selection,
                        selectionArgs, null, null, sortOrder);
                break;
            case 2:
                // 从uri中解析出ID
                long id = ContentUris.parseId(uri);
                selection = (selection == null || "".equals(selection.trim())) ? _id
                        + "=" + id
                        : selection + " and " + _id + "=" + id;
                cursor = sqLiteDatabase.query(Table_Name, projection, selection,
                        selectionArgs, null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("参数错误");
        }

        return cursor;
    }

    /**
     * 当执行更新操作的时候执行该方法 
     */
    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        int num = 0;
        sqLiteDatabase = myOpenHelper.getWritableDatabase();
        int code = uriMatcher.match(uri);
        switch (code) {
            case 1:
                num = sqLiteDatabase.update(Table_Name, values, selection, selectionArgs);
                break;
            case 2:
                long id = ContentUris.parseId(uri);
                selection = (selection == null || "".equals(selection.trim())) ? _id
                        + "=" + id
                        : selection + " and " + _id + "=" + id;
                num = sqLiteDatabase.update(Table_Name, values, selection, selectionArgs);
                break;
            default:
                break;
        }
        return num;
    }

    // 数据库名称  
    private final String DB_Name = "chenzheng_java.db";
    // 数据表名  
    private final String Table_Name = "chenzheng_java";
    // 版本号  
    private final int Version_1 = 1;

    private class MyOpenHelper extends SQLiteOpenHelper {

        public MyOpenHelper(Context context, String name,
                            CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        /**
         * @description 当数据表无连接时创建新的表
         */
        @Override
        public void onCreate(SQLiteDatabase db) {
            String sql = " create table if not exists " + Table_Name
                    + "(id INTEGER,name varchar(20),age integer,isMan boolean)";
            db.execSQL(sql);
        }

        /**
         * @description 当版本更新时触发的方法
         */
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            String sql = " drop table if exists " + Table_Name;
            db.execSQL(sql);
            onCreate(db);
        }

    }
} 