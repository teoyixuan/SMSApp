package sg.edu.rp.c346.smsapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Button btn, btn2;
    EditText etTo, etContent;
    BroadcastReceiver br;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkPermission();

        btn = findViewById(R.id.button);
        etTo = findViewById(R.id.editTextTo);
        etContent = findViewById(R.id.editTextContent);
        btn2 = findViewById(R.id.button2);

        br = new MessageReceiver();

        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        filter.addAction("android.provider.Telephony.SMS_RECEIVED");
        this.registerReceiver(br, filter);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String phoneTo = etTo.getText().toString();
                String content = etContent.getText().toString();
                SmsManager smsManager = SmsManager.getDefault();

                if(phoneTo.contains(",")){
                    String[] parts = phoneTo.split(",");
                    for(int i = 0; i < parts.length; i++){
                        String part = parts[i];
                        smsManager.sendTextMessage(part, null, content, null, null);
                        etContent.setText(null);
                        Toast.makeText(MainActivity.this, "Message sent", Toast.LENGTH_LONG).show();
                    }
                }
                else {
                    smsManager.sendTextMessage(phoneTo, null, content, null, null);
                    etContent.setText(null);
                    Toast.makeText(MainActivity.this, "Message sent", Toast.LENGTH_LONG).show();
                }
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phoneTo = etTo.getText().toString();
                String content = etContent.getText().toString();

                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setType("vnd.android-dir/mms-sms");
                intent.setData(Uri.parse("sms:" + phoneTo));
                intent.putExtra("sms_body", content);
                startActivity(intent);
            }
        });
    }

    private void checkPermission() {

        int permissionSendSMS = ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS);

        int permissionRecvSMS = ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS);

        if (permissionSendSMS != PackageManager.PERMISSION_GRANTED &&
                permissionRecvSMS != PackageManager.PERMISSION_GRANTED) {
            String[] permissionNeeded = new String[]{Manifest.permission.SEND_SMS,
                    Manifest.permission.RECEIVE_SMS};
            ActivityCompat.requestPermissions(this, permissionNeeded, 1);
        }


    }
}
