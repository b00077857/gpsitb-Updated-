package com.example.itb.gpsclient;


import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.telephony.SmsManager;
import android.widget.Toast;

public class backgroundService extends Service {
    String phoneNo = "0809383383";
    String message = "Hello";
    @Override
    public void onCreate() {
        super.onCreate();
    }
    @Override
    public void onTaskRemoved(Intent rootIntent)
    {
        phoneNo = "+0899546163";
        message = "The Tracking app is closed!!";
        try {
            SmsManager smsManager = SmsManager.getDefault();

            PendingIntent sentPI;
            String SENT = "SMS_SENT";

            sentPI = PendingIntent.getBroadcast(this, 0,new Intent(SENT), 0);

            smsManager.sendTextMessage(phoneNo, null, message, sentPI, null);
            Toast.makeText(this, "SMS sent.",
                    Toast.LENGTH_LONG).show();

        } catch (Exception e) {
            Toast.makeText(this,
                    "Sending SMS failed.",
                    Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
        Toast.makeText(this,"Tracking removed",Toast.LENGTH_LONG).show();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Toast.makeText(this,"Service started",Toast.LENGTH_LONG).show();

       return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();


    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
