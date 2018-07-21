package com.example.itb.gpsclient;


import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import android.widget.Toast;

public class Shutdownreciever extends BroadcastReceiver {
    String phoneNo;
    String message;
    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_SHUTDOWN.equals(intent.getAction())) {
            Toast.makeText(context, "Phone is about to shutown,tracking will be stopped!", Toast.LENGTH_LONG).show();
        }
            phoneNo = "0899546163";
            message = "test";
            try {
                SmsManager smsManager = SmsManager.getDefault();

                PendingIntent sentPI;
                String SENT = "SMS_SENT";

                sentPI = PendingIntent.getBroadcast(context, 0,new Intent(SENT), 0);

                smsManager.sendTextMessage(phoneNo, null, message, sentPI, null);
                Toast.makeText(context, "SMS sent.",
                        Toast.LENGTH_LONG).show();

            } catch (Exception e) {
                Toast.makeText(context,
                        "Sending SMS failed.",
                        Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }

        }




}

