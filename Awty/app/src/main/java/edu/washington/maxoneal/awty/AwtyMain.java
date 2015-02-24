package edu.washington.maxoneal.awty;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import static android.app.AlarmManager.RTC_WAKEUP;

public class AwtyMain extends Activity {
    private PendingIntent pendingIntent;
    int interval = 5000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_awty_main);
        final Button button = (Button) findViewById(R.id.button);
        final EditText text = (EditText) findViewById(R.id.editText);
        final EditText text2 = (EditText) findViewById(R.id.editText2);
        final EditText text3 = (EditText) findViewById(R.id.editText3);
        final Intent alarmIntent = new Intent(this, CustomReceiver.class);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (button.getText().equals("START")) {
                    if(!text.getText().toString().matches("")
                            && !text2.getText().toString().matches("")
                            && !text3.getText().toString().matches("")
                            && text2.getText().toString().length() >= 9
                            && text3.getText().toString().charAt(0) != '-'
                            && !text3.getText().toString().equals("0")) {
                        button.setText("STOP");
                        interval = 1000 * Integer.parseInt(text3.getText().toString());
                        String msg = text2.getText().toString() + text.getText().toString();
                        alarmIntent.putExtra("value", msg);
                        pendingIntent = PendingIntent.getBroadcast(AwtyMain.this, 0, alarmIntent, 0);
                        start();
                    }
                } else {
                    button.setText("START");
                    cancel();
                }
            }
        });
    }

    public void start() {
        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        manager.setInexactRepeating(RTC_WAKEUP, System.currentTimeMillis(), interval, pendingIntent);
        Toast.makeText(this, "Alarm Set", Toast.LENGTH_LONG).show();
    }

    public void cancel() {
        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        manager.cancel(pendingIntent);
        pendingIntent.cancel();
        Toast.makeText(this, "Alarm Stopped", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy(){
        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        manager.cancel(pendingIntent);
        pendingIntent.cancel();
    }

    @Override
    public void onBackPressed() {
    }
}