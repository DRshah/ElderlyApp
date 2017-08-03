package com.example.android.elderlyapp;

/**
 * Created by Karan on 10-10-2016.
 */import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.widget.Toast;

import com.example.android.elderlyapp.core.DBAdapter;

public class AlarmReceiver extends BroadcastReceiver
{
    DBAdapter dbAdapter;
    @Override
    public void onReceive(Context context, Intent intent)
    {
        //Bundle bundle=intent.getExtras();

//        Uri alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
//        if (alarmUri == null)
//        {
//            alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//        }
//        Ringtone ringtone = RingtoneManager.getRingtone(context, alarmUri);
//        ringtone.play();
        //Toast.makeText(context, "Alarm! Wake up! Wake up!", Toast.LENGTH_LONG).show();
        Vibrator vibrator= (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(5000);
        Intent i = new Intent(context, AlertDialogActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);

//        dbAdapter=new DBAdapter(context);
//        if(bundle!=null) {
//            String nm=bundle.getString("CLK");
//            boolean flag = dbAdapter.deleteMedicine(nm);
//        }
//        else {
//            System.out.print("Cannot delete from DB");
//        }

    }
}