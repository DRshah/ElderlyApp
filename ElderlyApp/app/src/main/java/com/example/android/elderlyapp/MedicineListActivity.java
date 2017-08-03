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
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.elderlyapp.core.DBAdapter;
import com.example.android.elderlyapp.core.Medicine;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MedicineListActivity extends AppCompatActivity {

    private ImageView imageView;
    private TextView textView;
    SharedPreferences preferences;
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<Medicine> arrayList=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine_list);
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
        recyclerView.addOnItemTouchListener(new RecyclerAdapter(MedicineListActivity.this, recyclerView, new RecyclerAdapter.ClickListener() {
            @Override
            public void OnClick(View view, int position) {
                Toast.makeText(getApplicationContext(),"On Long click enabled.",Toast.LENGTH_LONG).show();
            }

            @Override
            public void OnLongClick(final View view, final int position) {
                AlertDialog.Builder alert = new AlertDialog.Builder(
                        MedicineListActivity.this);
                alert.setTitle("Alert!!");
                alert.setMessage("Are you sure to delete record");
                alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                            //DBAdapter dbAdapter=new DBAdapter();
                        boolean res=db.deleteMedicine(arrayList.get(position).getTime());
                        //Toast.makeText(getApplicationContext(),"The record has been ddeleted"+arrayList.get(position).getTime(),Toast.LENGTH_LONG).show();
                        if(res==true) {
                            Intent i=new Intent(MedicineListActivity.this,AddMedicine.class);
                            Toast.makeText(getApplicationContext(), "The record has been ddeleted", Toast.LENGTH_LONG).show();
                            startActivity(i);
                        }
                        else
                            Toast.makeText(getApplicationContext(),"Error deleting record",Toast.LENGTH_LONG).show();

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
}
