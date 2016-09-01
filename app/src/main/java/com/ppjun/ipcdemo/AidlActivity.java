package com.ppjun.ipcdemo;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;

import com.ppjun.ipcdemo.aidl.Book;
import com.ppjun.ipcdemo.aidl.IBookManager;
import com.ppjun.ipcdemo.aidl.IOnNewBookArrivedListener;
import com.ppjun.ipcdemo.service.BookManagerService;

import java.util.List;

/**
 * @Package :com.ppjun.ipcdemo
 * @Description :
 * @Author :Rc3
 * @Created at :2016/8/31 17:50.
 */
public class AidlActivity extends Activity {
    IBookManager mRemoteBookManager;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {

                Log.i("msg", msg.obj.toString());
            }
        }
    };
    private IOnNewBookArrivedListener mOnNewBookArrivedListener = new IOnNewBookArrivedListener.Stub() {

        @Override
        public void onNewBookArrived(Book book) throws RemoteException {
            mHandler.obtainMessage(1, book).sendToTarget();
        }
    };
    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            IBookManager bookManager = IBookManager.Stub.asInterface(service);
            try {
                mRemoteBookManager = bookManager;
                Book book3 = new Book(3, "android开发艺术");
                bookManager.addBook(book3);
                List<Book> list = bookManager.getBookList();
                for (int i = 0; i < list.size(); i++) {
                    Log.i("msg", list.get(i).toString());
                }
                mRemoteBookManager.registerListener(mOnNewBookArrivedListener);
            } catch (RemoteException e) {
                e.printStackTrace();
            }

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mRemoteBookManager = null;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bindService(new Intent(AidlActivity.this, BookManagerService.class), mConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onDestroy() {


        if (mRemoteBookManager != null && mRemoteBookManager.asBinder().isBinderAlive()) {
            try {
                Log.i("TAG","  unregister"+mRemoteBookManager);
                mRemoteBookManager.unregisterListener(mOnNewBookArrivedListener);
            } catch (RemoteException e) {
                e.printStackTrace();
            }

        }
        unbindService(mConnection);
        super.onDestroy();
    }

}
