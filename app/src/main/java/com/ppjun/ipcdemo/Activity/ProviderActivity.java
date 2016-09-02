package com.ppjun.ipcdemo.Activity;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.ppjun.ipcdemo.R;
import com.ppjun.ipcdemo.aidl.Book;
import com.ppjun.ipcdemo.contentprovider.BookProvider;

/**
 * @Package :com.ppjun.ipcdemo.Activity
 * @Description :
 * @Author :Rc3
 * @Created at :2016/9/2 10:04.
 */
public class ProviderActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Uri uri= BookProvider.BOOK_CONTENT_URI;
        ContentValues values=new ContentValues();
        values.put("_id",4);
        values.put("name","艺术");
        getContentResolver().insert(uri,values);

        Cursor bookCursor=getContentResolver().query(uri,new String[]{"_id","name"},null,null,null);
           while(bookCursor.moveToNext()){
               Book book=new Book(bookCursor.getInt(0),bookCursor.getString(1));
               Log.i("TAG",book.toString());
           }
        bookCursor.close();


        Uri userUri=BookProvider.USER_CONTENT_URI;
        ContentValues userValues=new ContentValues();
        userValues.put("_id",3);
        userValues.put("name","小美女g");
        userValues.put("age",12);
        getContentResolver().insert(userUri,userValues);

        Cursor UserCursor=getContentResolver().query(userUri,new String[]{"_id","name","age"},null,null,null);
        while(UserCursor.moveToNext()){
            User user=new User(UserCursor.getInt(0),UserCursor.getString(1),UserCursor.getInt(2));
            Log.i("TAG",user.toString());
        }
        UserCursor.close();
    }
}
