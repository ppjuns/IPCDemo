package com.ppjun.ipcdemo.contentprovider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

import com.ppjun.ipcdemo.db.DbOpenHelper;

/**
 * @Package :com.ppjun.ipcdemo.contentprovider
 * @Description :
 * @Author :Rc3
 * @Created at :2016/9/2 09:53.
 */
public class BookProvider extends ContentProvider {
    public static final String AUTHORITY = "com.ppjun.ipcdemo.provider.BookProvider";//标识
    public static final Uri BOOK_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/book");//链接book表的uri
    public static final Uri USER_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/user");//链接user表的uri
    public static final int BOOK_URI_CODE = 0;//bookuri的标记
    public static final int USER_URI_CODE = 1;//user uri的标记
    private static final String TAG = BookProvider.class.getSimpleName();//log时用到的TAG
    private static final UriMatcher mUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);//用来转换表

    static {
        mUriMatcher.addURI(AUTHORITY, "book", BOOK_URI_CODE);
        mUriMatcher.addURI(AUTHORITY, "user", USER_URI_CODE);  //把2个表加到转换器中
    }

    private Context mContext;
    private SQLiteDatabase mDb;

    @Override
    public boolean onCreate() {
        mContext=getContext();
        //并不推荐在这里ui main进行耗时操作
        initProviderData();
        return true;
    }

    private void initProviderData() {

        mDb = new DbOpenHelper(mContext,"",null,0).getWritableDatabase();
        if(mDb==null){
            return;
        }
        mDb.execSQL("delete from " + DbOpenHelper.BOOK_TABLE_NAME);
        mDb.execSQL("delete from " + DbOpenHelper.USER_TABLE_NAME);
        mDb.execSQL("insert into book values(1,'android');");
        mDb.execSQL("insert into book values(2,'ios');");
        mDb.execSQL("insert into book values(3,'html5');");
        mDb.execSQL("insert into user values(1,'apple',20);");
        mDb.execSQL("insert into user values(2,'bear',25);");
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Log.i("TAG", "query thread " + Thread.currentThread().getName());
        String table = getTableName(uri);
        if (table == null) {
            throw new IllegalArgumentException("unsupport uri" + uri);
        }

        return mDb.query(table, projection, selection, selectionArgs, null, null, sortOrder, null);
    }

    private String getTableName(Uri uri) {
        String tableName = null;
        switch (mUriMatcher.match(uri)) {
            case BOOK_URI_CODE:
                tableName = DbOpenHelper.BOOK_TABLE_NAME;
                break;
            case USER_URI_CODE:
                tableName = DbOpenHelper.USER_TABLE_NAME;
                break;
            default:
                break;
        }
        return tableName;

    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        String table = getTableName(uri);
        if (table == null) {
            throw new IllegalArgumentException("unsupport uri" + uri);
        }
        mDb.insert(table, null, values);
        mContext.getContentResolver().notifyChange(uri, null);

        return uri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        String table = getTableName(uri);
        if (table == null) {
            throw new IllegalArgumentException("unsupport uri" + uri);
        }
        int count = mDb.delete(table, selection, selectionArgs);
        if (count > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return count;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        String table = getTableName(uri);
        if (table == null) {
            throw new IllegalArgumentException("unsupport uri" + uri);
        }
        int row = mDb.update(table, values, selection, selectionArgs);
        if(row>0){
            getContext().getContentResolver().notifyChange(uri,null);
        }

        return row;
    }
}
