package com.example.android.elderlyapp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.android.elderlyapp.AddMedicine;
import com.example.android.elderlyapp.AlarmReceiver;
import com.example.android.elderlyapp.MedicineListActivity;
import com.example.android.elderlyapp.R;
import com.example.android.elderlyapp.UpdateMedicineList;
import com.example.android.elderlyapp.core.DBAdapter;
import com.squareup.picasso.Picasso;

import java.util.Calendar;

import static android.content.Context.ALARM_SERVICE;
import static android.content.Context.MODE_PRIVATE;
import static com.example.android.elderlyapp.AddMedicine.setAlarm;

/**
 * Created by Darsh_Shah on 03-07-2017.
 */

public class MedicineAddingFragment extends Fragment {

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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.activity_add_medicine,container,false);
        imageView= (ImageView) view.findViewById(R.id.gimg);
        textView= (TextView) view.findViewById(R.id.gtv);
        timeTv= (TextView) view.findViewById(R.id.tv_time);
        pickerTime= (TimePicker) view.findViewById(R.id.timePicker);
        mednameEt= (EditText) view.findViewById(R.id.et_medname);




        MedAndTime= (Button) view.findViewById(R.id.MedAndTime);


        Update= (Button) view.findViewById(R.id.update);
        Update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(),UpdateMedicineList.class);
                startActivity(intent);
            }
        });

        int hour;
        int minute;
        Calendar now=Calendar.getInstance();
        pickerTime.setCurrentHour(now.get(Calendar.HOUR_OF_DAY));
        pickerTime.setCurrentMinute(now.get(Calendar.MINUTE));
        buttonSetAlarm = (Button)view.findViewById(R.id.setalarm);
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
                    Toast.makeText(getActivity(),
                            "Invalid Date/Time",
                            Toast.LENGTH_LONG).show();
                }else{
                    RQS_1++;
                    setAlarm(getActivity(),cal,RQS_1+"",med_name);
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
                Intent intent=new Intent(getActivity(),MedicineListActivity.class);
                startActivity(intent);
            }

        });



        return view;
    }



    public static void setAlarm(Context context, Calendar targetCal, String RQS_1, String med_name){

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
        alarmManager = (AlarmManager)context.getSystemService(ALARM_SERVICE);
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
