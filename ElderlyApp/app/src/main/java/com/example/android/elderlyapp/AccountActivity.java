package com.example.android.elderlyapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

public class AccountActivity extends AppCompatActivity {

    private Button button,Mbutton,Sbutton;
    private FirebaseAuth mAuth;
    SharedPreferences preferences;
    private TextView textView;
    private ImageView imageView;
    private  FirebaseAuth.AuthStateListener mAuthstateListener;//what should the user do after sign out
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        button= (Button) findViewById(R.id.logout);
        Mbutton= (Button) findViewById(R.id.Medicine);
        Sbutton= (Button) findViewById(R.id.SOS);
        imageView= (ImageView) findViewById(R.id.gimg);
        textView= (TextView) findViewById(R.id.gtv);
        preferences=getSharedPreferences("GoogleInfo",MODE_PRIVATE);
        String name=preferences.getString("name","");
        String photo=preferences.getString("photo","");
        Log.d("NAME",name);
        textView.setText(name);
        Uri pp= Uri.parse(photo);
        Picasso.with(this).load(pp).into(imageView);


        mAuth=FirebaseAuth.getInstance();
        Mbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             Intent intent=new Intent(AccountActivity.this,AddMedicine.class);
                startActivity(intent);
            }
        });

        Sbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AccountActivity.this,SOSActivity.class);
                startActivity(intent);
                //Toast.makeText(getApplicationContext(),"SOS Activity",Toast.LENGTH_SHORT).show();
            }
        });


        //button= (Button) findViewById(logout);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mAuth.signOut();
            }
        });


        mAuthstateListener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser()==null){
                    Toast.makeText(getApplicationContext(),"You have been successfully logged out.",Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(AccountActivity.this,LoginActivity.class));
                }
            }
        };
    }


    @Override
    protected void onStart(){
        super.onStart();
        mAuth.addAuthStateListener(mAuthstateListener);
    }
}
