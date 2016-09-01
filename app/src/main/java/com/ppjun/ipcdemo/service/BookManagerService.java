package com.ppjun.ipcdemo.service;

import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.support.annotation.Nullable;

import com.ppjun.ipcdemo.aidl.Book;
import com.ppjun.ipcdemo.aidl.IBookManager;
import com.ppjun.ipcdemo.aidl.IOnNewBookArrivedListener;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @Package :com.ppjun.ipcdemo.service
 * @Description :
 * @Author :Rc3
 * @Created at :2016/8/31 17:10.
 */
public class BookManagerService extends Service {

    private CopyOnWriteArrayList<Book> mBookList = new CopyOnWriteArrayList<>();
    private AtomicBoolean mIsServiceDestoryed = new AtomicBoolean(false);
    private RemoteCallbackList<IOnNewBookArrivedListener> mListenerList=new RemoteCallbackList<IOnNewBookArrivedListener>();
    private Binder mBinder = new IBookManager.Stub() {

        @Override
        public List<Book> getBookList() throws RemoteException {

            return mBookList;
        }

        @Override
        public void addBook(Book book) throws RemoteException {
            mBookList.add(book);
        }

        @Override
        public void registerListener(IOnNewBookArrivedListener listener) throws RemoteException {

                mListenerList.register(listener);


        }

        @Override
        public void unregisterListener(IOnNewBookArrivedListener listener) throws RemoteException {


                mListenerList.unregister(listener);


        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        mBookList.add(new Book(1, "Android"));
        mBookList.add(new Book(2, "IOS"));
        new Thread(new ServiceWorker()).start();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
       int check=checkCallingOrSelfPermission("com.ppjun.ipcdemo.permission.ACCESS_BOOK_SERVICE");
        if(check== PackageManager.PERMISSION_DENIED){
            return  null;
        }
        return mBinder;
    }



    private void onNewBookArrived(Book book)throws RemoteException{
        mBookList.add(book);
        final int N= mListenerList.beginBroadcast();//注意mListenerList.beginBroadcast();不能写在for循环里面，会报错beginBroadcast() called while already in a broadcast
        for (int i = 0; i < N; i++) {
            IOnNewBookArrivedListener listener=mListenerList.getBroadcastItem(i);
            if(listener!=null){
                listener.onNewBookArrived(book);

            }


        }
        mListenerList.finishBroadcast();
    }



    private class ServiceWorker implements Runnable{

        @Override
        public void run() {
            while (!mIsServiceDestoryed.get()){
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                int bookId=mBookList.size()+1;
                Book newBook=new Book(bookId,"new book#"+bookId);
                try {
                    onNewBookArrived(newBook);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
