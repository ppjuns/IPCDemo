package com.ppjun.ipcdemo.service;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * @Package :com.ppjun.ipcdemo.service
 * @Description :
 * @Author :Rc3
 * @Created at :2016/8/31 14:53.
 */
public class MessengerService extends Service {

    private Messenger mMessenger = new Messenger(new MessengerHandler());

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mMessenger.getBinder();
    }

    public class MessengerHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {

            if (msg.what == 1) {

                Log.i("msg", msg.getData().getString("msg"));
                Log.i("msg", "client");
                Messenger recall = msg.replyTo;
                Message m = Message.obtain(null, 2);
                Bundle b = new Bundle();
                b.putString("msg", "from server");
                m.setData(b);
                try {
                    recall.send(m);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            } else {
                super.handleMessage(msg);
            }

        }
    }
}
