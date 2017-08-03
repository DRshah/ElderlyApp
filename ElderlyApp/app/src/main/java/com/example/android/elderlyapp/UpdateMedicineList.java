package com.example.android.elderlyapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.android.elderlyapp.core.DBAdapter;
import com.example.android.elderlyapp.core.Medicine;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;

public class UpdateMedicineList extends AppCompatActivity {

    private TextView textView;
    private ImageView imageView;
    SharedPreferences preferences;
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<Medicine> arrayList=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_medicine_list);
        recyclerView= (RecyclerView) findViewById(R.id.recyclerID);
        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        imageView= (ImageView) findViewById(R.id.gimg);
        textView= (TextView) findViewById(R.id.gtv);
        preferences=getSharedPreferences("GoogleInfo",MODE_PRIVATE);
        String name=preferences.getString("name","");
        String photo=preferences.getString("photo","");
        Log.d("NAME",name);
        textView.setText(name);
        Uri pp= Uri.parse(photo);
        Picasso.with(this).load(pp).into(imageView);
        final DBAdapter db=new DBAdapter(this);
        arrayList=db.getMedicines();
        adapter=new RecyclerAdapter(arrayList);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnItemTouchListener(new RecyclerAdapter(UpdateMedicineList.this, recyclerView, new RecyclerAdapter.ClickListener() {
            @Override
            public void OnClick(View view, int position) {
                Toast.makeText(getApplicationContext(),"On Long click enabled.",Toast.LENGTH_LONG).show();
            }

            @Override
            public void OnLongClick(final View view, final int position) {
                //LayoutInflater factory=LayoutInflater.from(getApplicationContext());
                //final View textViewEntry=factory.inflate(0)
                String rq = arrayList.get(position).getRqs();
                AlertDialog.Builder alert = new AlertDialog.Builder(UpdateMedicineList.this);
                alert.setTitle("Alert!!");
                final TimePicker picker=new TimePicker(UpdateMedicineList.this);
                alert.setView(picker);
                alert.setMessage("Are you sure you want to update the record ?");
                alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                            Calendar current = Calendar.getInstance();

                            int year=current.get(Calendar.YEAR);
                            int month=current.get(Calendar.MONTH);
                            int dom=current.get(Calendar.DAY_OF_MONTH);

                            Calendar cal = Calendar.getInstance();
                            cal.set(year,
                                    month,
                                    dom,
                                    picker.getCurrentHour(),
                                    picker.getCurrentMinute(),
                                    00);

                            if(cal.compareTo(current) <= 0){
                                //The set Date/Time already passed
                                Toast.makeText(getApplicationContext(),
                                        "Invalid Date/Time",
                                        Toast.LENGTH_LONG).show();
                            }else{
                                boolean b=db.updateList(arrayList.get(position).getTime(),cal.getTime()+"");
                                String rqs =arrayList.get(position).getRqs();
                                String name =arrayList.get(position).getMedicine();

                                if(b==true) {
                                    Toast.makeText(getApplicationContext(), "Row Updated", Toast.LENGTH_SHORT).show();
                                    AddMedicine.setAlarm(UpdateMedicineList.this,cal,rqs,name);
                                    Intent intent=new Intent(UpdateMedicineList.this,AddMedicine.class);
                                    startActivity(intent);
                                    //how to cancel the exixsting alarm ??
                                }
                                    else
                                    Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_SHORT).show();
                            }

                        }
                });
                alert.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                alert.show();

            }
        }));
    }



    public void CancelAlarm(){

    }

}
