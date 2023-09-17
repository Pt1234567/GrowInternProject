package com.example.letschat.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.letschat.R;
import com.example.letschat.model.ProfileModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;

public class SplashActivity extends AppCompatActivity {
FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        FirebaseApp.initializeApp(this);
        auth=FirebaseAuth.getInstance();

        if(getIntent().getExtras()!=null){
            //from notification
            String userId=getIntent().getExtras().getString("uid");
            FirebaseDatabase.getInstance().getReference("Profile").child(userId).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
               if(task.isSuccessful()){
                   ProfileModel profileModel=task.getResult().getValue(ProfileModel.class);
                   Intent intent=new Intent(SplashActivity.this, ChatActivity.class.getClass());
                   startActivity(intent);
                   finish();
               }
                }
            });
        }else{
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if(auth.getCurrentUser()!=null){
                        startActivity(new Intent(SplashActivity.this,MainActivity.class));
                        finish();
                    }else{
                        startActivity(new Intent(SplashActivity.this,PhoneVerifyActivity.class));
                        finish();
                    }

                }
            },2000);
        }

    }
}