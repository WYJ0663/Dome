package com.example.ContentProviderTest;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
/**
 *
 * @author chenzheng_java
 * @description 通过访问内容提供者进行增删查改.注意本程序中为了方便阅读， 
 * 在需要数据库列名的地方直接写上了数据库中字段的名称，实际上这是不合理的， 
 * 作为内容提供者的使用者，我们不可能在使用这个内容提供者之前先去了解sqlite 
 * 中表的结构。比较适宜的做法是，在内容提供者中将愿意提供给外部访问的字段名称（列名） 
 * 定义为string final 的常量！ 
 */
public class MyActivity extends Activity {
    private final static String tag = "通知";
    private TextView textView;
    String result = "结果:/n";
    ContentResolver reslover;
    Uri uri;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        /**
         * 这里我们一定要搞清楚，uri的内容到底和内容提供者中哪个地方一一对应 
         * 在MyContentProvider中我们有如下片段 
         * uriMatcher.addURI(authority, "path_chenzheng", 1);// 代表当前表中的所有的记录 
         uriMatcher.addURI(authority, "path_chenzheng/#", 2);// 代表当前表中的某条特定的记录，记录id便是#处得数字 
         其中authority为cn.com.chenzheng_java.hello。 
         */
        uri = Uri.parse("content://cn.com.chenzheng_java.hello/path_chenzheng");

        /**
         * 内容提供者是什么？内容提供者相当于一个封装好了增删改查操作的接口，这个接口有一把锁，只有携带钥匙的访问者才能访问。 
         * ContentResolver是什么？ContentResolver是一个开锁匠，他携带者钥匙(钥匙上有标签显示他是那个门得钥匙，如path_chenzheng) 
         * 去寻找内容提供者，然后访问内容提供者的增删查改方法 
         * 我们这里调用contentResolver的增删查改就相当于将任务交给了锁匠， 
         * 然后让锁匠去找能打开的内容提供者，并且执行里面相应的方法，并将结果返回. 
         * ContentResolver的好处在于，我们可以无视CotentProvider的具体实现，无论contentProvider里面是如何实现的，我想执行 
         * 某一个操作时，所要书写的代码都是一样的。 
         */
        reslover = this.getContentResolver();
        textView = (TextView) findViewById(R.id.textView);

        Button insertButton = (Button) findViewById(R.id.insertButton);
        insertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insert(reslover, uri);
            }
        });

        Button deleteButton = (Button) findViewById(R.id.deleteButton);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete(reslover, uri);
            }
        });

        Button updateButton = (Button) findViewById(R.id.updateButton);
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update(reslover, uri);
            }
        });

        Button queryButton = (Button) findViewById(R.id.queryButton);
        queryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                query(reslover, uri);
            }
        });

    }

    private void insert(ContentResolver resolver, Uri uri) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", "张小凡");
        contentValues.put("age", 22);
        contentValues.put("isMan", true);
        Uri uri2 = resolver.insert(uri, contentValues);
        Log.i(tag, "插入成功！");
        result += "成功插入了一条记录,uri为" + uri2;
        textView.setText(result);
        result = "";
    }

    private void update(ContentResolver resolver, Uri uri) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("age", 122);
        int number = resolver.update(uri, contentValues, null, null);
        Log.i(tag, "更新成功！");
        result += "成功更新了" + number+"条记录";
        textView.setText(result);
        result = "";

    }
    private void delete(ContentResolver resolver, Uri uri) {
        String where = " 1=1 and isMan=?";
        //这里要注意哦，sqlite数据库中是没有boolean的，true会被转成1存储  
        String[] selectionArgs = new String[] { "1" };
        int number = resolver.delete(uri, where, selectionArgs);
        Log.i(tag, "删除成功！");
        textView.setText(result + "成功删除了" + number + "条记录");
        result = "";
    }

    private void query(ContentResolver resolver, Uri uri) {

        String[] projection = new String[] { "id", "name", "age", "isMan" };
        Cursor cursor = resolver.query(uri, projection, null, null, null);
        int count = cursor.getCount();
        Log.i(tag, "总记录数" + count);

        int idIndex = cursor.getColumnIndex("id");
        int nameIndex = cursor.getColumnIndex("name");
        int ageIndex = cursor.getColumnIndex("age");
        int isManIndex = cursor.getColumnIndex("isMan");

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            int id = cursor.getInt(idIndex);
            String name = cursor.getString(nameIndex);
            int age = cursor.getInt(ageIndex);
            int isMan = cursor.getInt(isManIndex);
            Log.i(tag, "id=" + id + " name=" + name + " age=" + age + " isMan="
                    + isMan);
            result += "id=" + id + " name=" + name + " age=" + age + " isMan="
                    + isMan;
            cursor.moveToNext();
        }

        textView.setText(result);
        result = "";

    }

} 