package com.harinder.birthdaywisher;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText phoneNumber, message, hours, minutes, seconds;
    Button send;
    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS =0 ;
    String phonep, messagep;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        phoneNumber = findViewById(R.id.phoneNumber);
        message = findViewById(R.id.message);
        send = findViewById(R.id.send);
        hours = findViewById(R.id.hours);
        minutes = findViewById(R.id.minutes);
        seconds = findViewById(R.id.seconds);


        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.SEND_SMS)) {

            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.SEND_SMS},
                        MY_PERMISSIONS_REQUEST_SEND_SMS);
            }
        }

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendSMSMessage();
            }
        });

    }

    protected void sendSMSMessage(){
        phonep = phoneNumber.getText().toString();
        messagep = message.getText().toString();

        startAlert();

//        SmsManager smsManager = SmsManager.getDefault();
//        smsManager.sendTextMessage(phonep, null, messagep, null, null);
//        Toast.makeText(getApplicationContext(), "SMS sent.",
//                Toast.LENGTH_LONG).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_SEND_SMS: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    Toast.makeText(getApplicationContext(),
                            "SMS faild, please try again.", Toast.LENGTH_LONG).show();
                    return;
                }
            }
        }

    }

    public void startAlert(){
        EditText text = findViewById(R.id.time);
        int hoursToSeconds = Integer.parseInt(hours.getText().toString());
        int minToSeconds = Integer.parseInt(minutes.getText().toString());
        int secToSeconds = Integer.parseInt(seconds.getText().toString());
        int i = (hoursToSeconds * 3600)+(minToSeconds * 60)+secToSeconds;
        Intent intent = new Intent(this, MyBroadcastReceiver.class);
        intent.putExtra("phone", phonep);
        intent.putExtra("message", messagep);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this.getApplicationContext(), 234324243, intent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()
                + (i * 1000), pendingIntent);
        Toast.makeText(this, "Alarm set in " + i + " seconds",Toast.LENGTH_LONG).show();
    }

}
