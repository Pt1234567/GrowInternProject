package com.example.letschat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.letschat.adapter.MessageAdapter;
import com.example.letschat.model.Message;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.ktx.Firebase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity {
CircleImageView Toolbar_img;
TextView Toolbar_name;
EditText messageBox;
ImageView sendButton;
String SenderID;
FirebaseAuth firebaseAuth;
FirebaseDatabase database;
String senderRoom,receiverRoom;
String receiverId;
RecyclerView msgAdpter;
ArrayList<Message> messageArrayList;
MessageAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Toolbar_img=findViewById(R.id.chat_user_img);
        Toolbar_name=findViewById(R.id.chat_username);
        messageBox=findViewById(R.id.messageBox);
        sendButton=findViewById(R.id.sendImg);
        firebaseAuth=FirebaseAuth.getInstance();
        messageArrayList=new ArrayList<>();
        database=FirebaseDatabase.getInstance();

        msgAdpter=findViewById(R.id.msgAdapter);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(ChatActivity.this);
        linearLayoutManager.setStackFromEnd(true);
        msgAdpter.setLayoutManager(linearLayoutManager);
        adapter=new MessageAdapter(ChatActivity.this,messageArrayList);
        msgAdpter.setAdapter(adapter);



        Intent i=getIntent();
        String name=i.getStringExtra("name");
        String uri=i.getStringExtra("image");
        receiverId=i.getStringExtra("uid");


        SenderID=firebaseAuth.getUid();

        senderRoom=SenderID+receiverId;
        receiverRoom=receiverId+SenderID;

        Toolbar_name.setText(name);
        Picasso.get().load(uri).into(Toolbar_img);

        //sendMessage

        DatabaseReference chatReference=database.getReference().child("Profile").child(senderRoom).child("messages");


        //Add message in the arrayList
        chatReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messageArrayList.clear();
                for(DataSnapshot snapshot1:snapshot.getChildren()){
                    Message msgModel=snapshot1.getValue(Message.class);
                    messageArrayList.add(msgModel);
                    Log.d("Firebase Data", "Message: " + msgModel.getMessage());
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
      });
        //becoz sender room and receiver room were declared here we were not able to go to chatActivity
//        senderRoom=SenderID+receiverId;
//        receiverRoom=receiverId+SenderID;


        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg=messageBox.getText().toString();

                messageBox.setText("");
                Date date=new Date();
                Message messagess=new Message(msg,SenderID, date.getTime());
                database=FirebaseDatabase.getInstance();
                database.getReference().child("chats").child("senderRoom").child("messages").push()
                        .setValue(messagess).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                database.getReference().child("chats").child("receiverRoom").child("messages")
                                        .push().setValue(messagess).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {

                                            }
                                        });
                            }
                        });
            }
        });
    }
}