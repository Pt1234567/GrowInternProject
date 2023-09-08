package com.example.letschat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import com.example.letschat.adapter.recyclerAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.letschat.model.ProfileModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.ktx.Firebase;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
FirebaseAuth auth;
RecyclerView recyclerView;
recyclerAdapter adapter;
FirebaseDatabase database;
ArrayList<ProfileModel> profileModelArrayList;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       
        database=FirebaseDatabase.getInstance();
        auth=FirebaseAuth.getInstance();

        DatabaseReference reference=database.getReference().child("Profile");
        profileModelArrayList=new ArrayList<>();
        //Memory allocation in arraylist
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                      ProfileModel profile=dataSnapshot.getValue(ProfileModel.class);
                      profileModelArrayList.add(profile);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        recyclerView=findViewById(R.id.User_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter=new recyclerAdapter(MainActivity.this,profileModelArrayList);
        recyclerView.setAdapter(adapter);


    }
}