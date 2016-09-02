package com.ppjun.ipcdemo.Activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.ppjun.ipcdemo.R;
import com.ppjun.ipcdemo.service.MessengerService;

public class MainActivity extends AppCompatActivity {

    private Messenger mMessenger;
    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mMessenger=new Messenger(service);

            Message msg=Message.obtain(null,1);
           Bundle b=new Bundle();
            b.putString("msg","from client");
            msg.setData(b);

            msg.replyTo=getReplyMessenger;
            try {
                mMessenger.send(msg);
            } catch (RemoteException e) {
                e.printStackTrace();
            }



        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };


    private Messenger getReplyMessenger=new Messenger(new ClientMessengerHandler());

   public  class ClientMessengerHandler extends Handler{

       @Override
       public void handleMessage(Message msg) {
           super.handleMessage(msg);
           if(msg.what==2){

               Log.i("msg",msg.getData().getString("msg"));
           }
       }
   }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bindService(new Intent(MainActivity.this, MessengerService.class), mConnection, Context.BIND_AUTO_CREATE);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(mConnection);
    }
}
