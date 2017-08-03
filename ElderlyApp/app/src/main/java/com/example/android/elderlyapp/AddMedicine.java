package com.example.android.elderlyapp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.content.Context;

import com.example.android.elderlyapp.core.DBAdapter;
import com.example.android.elderlyapp.core.GoogleInfo;
import com.example.android.elderlyapp.core.Medicine;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Array;
import java.net.URI;
import java.util.ArrayList;
import java.util.Calendar;

public class AddMedicine extends AppCompatActivity {
    private EditText mednameEt;
    private TextView timeTv;
    private TimePicker pickerTime;
    private AlarmManager alarmManager;
    private Button buttonSetAlarm;
    private int RQS_1=1;
    private Button MedAndTime;
    private Button Update;
    private ImageView imageView;
    private TextView textView;
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_medicine);
        imageView= (ImageView) findViewById(R.id.gimg);
        textView= (TextView) findViewById(R.id.gtv);
        timeTv= (TextView) findViewById(R.id.tv_time);
        pickerTime= (TimePicker) findViewById(R.id.timePicker);
        mednameEt= (EditText) findViewById(R.id.et_medname);

        preferences=getSharedPreferences("GoogleInfo",MODE_PRIVATE);
        String name=preferences.getString("name","");
        String photo=preferences.getString("photo","");
        Log.d("NAME",name);
        textView.setText(name);
        Uri pp= Uri.parse(photo);
        Picasso.with(this).load(pp).into(imageView);



        MedAndTime= (Button) findViewById(R.id.MedAndTime);


        Update= (Button) findViewById(R.id.update);
        Update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AddMedicine.this,UpdateMedicineList.class);
                startActivity(intent);
            }
        });

        int hour;
        int minute;
        Calendar now=Calendar.getInstance();
        pickerTime.setCurrentHour(now.get(Calendar.HOUR_OF_DAY));
        pickerTime.setCurrentMinute(now.get(Calendar.MINUTE));
        buttonSetAlarm = (Button)findViewById(R.id.setalarm);
        buttonSetAlarm.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View arg0) {
                String med_name = mednameEt.getText().toString();
                Calendar current = Calendar.getInstance();

                int year=current.get(Calendar.YEAR);
                int month=current.get(Calendar.MONTH);
                int dom=current.get(Calendar.DAY_OF_MONTH);

                Calendar cal = Calendar.getInstance();
                cal.set(year,
                        month,
                        dom,
                        pickerTime.getCurrentHour(),
                        pickerTime.getCurrentMinute(),
                        00);

                if(cal.compareTo(current) <= 0){
                    //The set Date/Time already passed
                    Toast.makeText(getApplicationContext(),
                            "Invalid Date/Time",
                            Toast.LENGTH_LONG).show();
                }else{
                    RQS_1++;
                    setAlarm(AddMedicine.this,cal,RQS_1+"",med_name);
                }

            }});








                MedAndTime.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        ArrayList<Medicine> medicines=new ArrayList<Medicine>();
//                        medicines=dbAdapter.getMedicines();;;
//                        for(int i=0;i<medicines.size();i++){
//                            String name=medicines.get(i).getMedicine();
//                            String time=medicines.get(i).getTime();
//                            Log.d("MediName",name+"--"+time);
                        Intent intent=new Intent(AddMedicine.this,MedicineListActivity.class);
                        startActivity(intent);
                        }

                });




    }


    public static void setAlarm(Context context,Calendar targetCal,String RQS_1,String med_name){

        DBAdapter dbAdapter=new DBAdapter(context);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        //String temp = (String) timeTv.getText();
        String time = targetCal.getTime()+"";
        boolean flag = dbAdapter.insertMedicine(med_name, time,RQS_1+"");
//        ArrayList a=new ArrayList();
//        a=dbAdapter.getMedicines();
//        for(int i=0;i<a.size();i++){
//            Log.d(i+"",a.get(i).toString());
//        }
        if (flag)
        Toast.makeText(context,"Alarm is set => " + targetCal.getTime(),Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(context, AlarmReceiver.class);
        //intent.putExtra("CLK",time);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, Integer.parseInt(RQS_1), intent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        //alarmManager.set(AlarmManager.RTC_WAKEUP, targetCal.getTimeInMillis(), pendingIntent);
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP,targetCal.getTimeInMillis(),AlarmManager.INTERVAL_DAY,pendingIntent);
//        alarmManager .setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
//                SystemClock.elapsedRealtime() + AlarmManager.INTERVAL_DAY,
//                AlarmManager.INTERVAL_DAY,pendingIntent);
//        alarmManager.cancel(pendingIntent); // cancel any existing alarms
//        alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
//                SystemClock.elapsedRealtime() + AlarmManager.INTERVAL_DAY,
//                AlarmManager.INTERVAL_DAY, pendingIntent);
    }


}


//    public void onSave(View view){
//        Calendar calendar = Calendar.getInstance();
//        calendar.set(Calendar.HOUR_OF_DAY, timePicker.getCurrentHour());
//        calendar.set(Calendar.MINUTE, timePicker.getCurrentMinute());
//        Intent intent = new Intent(this, AlarmReceiver.class);
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
//        long t;
//        t=(calendar.getTimeInMillis()-(calendar.getTimeInMillis()%60000));
//        if(System.currentTimeMillis()>t)
//        {
//            if (calendar.AM_PM == 0)
//                t = t + (1000*60*60*12);
//            else
//                t = t + (1000*60*60*24);
//        }
//        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, t, 10000, pendingIntent);
//        if(timeTv.getText().equals("--")) {
//            Toast.makeText(this, "Please select time", Toast.LENGTH_LONG).show();
//        }
//        else if(mednameEt.getText().equals(null)){
//            Toast.makeText(this, "Please Enter Medicine name", Toast.LENGTH_LONG).show();
//        }
//        else{
//            String name = String.valueOf(mednameEt.getText());
//            String temp = (String) timeTv.getText();
//            String time = temp.substring(0, 4) + ":00";
//            System.out.println(time);
//            boolean flag = dbAdapter.insertMedicine(name, time);
//            if (flag)
//                Toast.makeText(this, "Reminder set!", Toast.LENGTH_LONG).show();
//            timeTv.setText("--");
//        }
//    }
//}
