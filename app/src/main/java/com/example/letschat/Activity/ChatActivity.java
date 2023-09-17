package com.example.letschat.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.letschat.R;
import com.example.letschat.adapter.MessageAdapter;
import com.example.letschat.model.Message;
import com.example.letschat.model.ProfileModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ChatActivity extends AppCompatActivity {
    CircleImageView Toolbar_img;
    TextView Toolbar_name, Status;
    EditText messageBox;
    ImageView sendButton;
    String SenderID;
    FirebaseAuth firebaseAuth;
    ImageView back_btn;
    FirebaseDatabase database;
    String senderRoom, receiverRoom;
    String receiverId;
    RecyclerView msgAdpter;
    ArrayList<Message> messageArrayList;
    MessageAdapter adapter;
    String fcmToken;
    TextView time_send, time_receive;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        back_btn = findViewById(R.id.back);

        Toolbar_img = findViewById(R.id.chat_user_img);
        Toolbar_name = findViewById(R.id.chat_username);
        messageBox = findViewById(R.id.messageBox);
        sendButton = findViewById(R.id.sendImg);
        firebaseAuth = FirebaseAuth.getInstance();
        Status = findViewById(R.id.status);
        time_send = findViewById(R.id.send_time);
        time_receive = findViewById(R.id.recieve_time);

        firebaseAuth = FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser() == null) {
            // User is not authenticated, handle this case (e.g., redirect to login screen)
            startActivity(new Intent(ChatActivity.this, PhoneVerifyActivity.class));
            finish();
            return; // Exit the method to avoid further execution
        }
        SenderID = firebaseAuth.getUid();


        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ChatActivity.this, MainActivity.class));
                finish();
            }
        });
        database = FirebaseDatabase.getInstance();


        Intent i = getIntent();
        String name = i.getStringExtra("name");
        String uri = i.getStringExtra("image");
        receiverId = i.getStringExtra("uid");
        fcmToken = i.getStringExtra("fcm");

        messageArrayList = new ArrayList<>();
        SenderID = firebaseAuth.getUid();

        senderRoom = SenderID + receiverId;
        receiverRoom = receiverId + SenderID;


        msgAdpter = findViewById(R.id.msgAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ChatActivity.this);
        linearLayoutManager.setStackFromEnd(true);
        msgAdpter.setLayoutManager(linearLayoutManager);
        adapter = new MessageAdapter(ChatActivity.this, messageArrayList);
        msgAdpter.setAdapter(adapter);

        Toolbar_name.setText(name);
        Picasso.get().load(uri).into(Toolbar_img);

        //sendMessage

        //becoz sender room and receiver room were declared here we were not able to go to chatActivity
//        senderRoom=SenderID+receiverId;
//        receiverRoom=receiverId+SenderID;


        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = messageBox.getText().toString();

                messageBox.setText("");
                sendNotifications(msg);
                Date date = new Date();

                Message messagess = new Message(msg, SenderID, date.getTime());
                database = FirebaseDatabase.getInstance();
                database.getReference().child("chats").child(senderRoom).child("messages").push()
                        .setValue(messagess).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                //time_send.setText(String.valueOf(date.getTime()));
                                database.getReference().child("chats").child(receiverRoom).child("messages")
                                        .push().setValue(messagess).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                //time_receive.setText(String.valueOf(date.getTime()));
                                            }
                                        });
                            }
                        });
            }
        });


// DatabaseReference chatReference=database.getReference().child("Profile").child(senderRoom).child("messages"); this is wrong we created child("Profile") necuse of whichwe were not able to see chat on ui
        DatabaseReference chatReference = database.getReference().child("chats").child(senderRoom).child("messages");
        //Add message in the arrayList
        chatReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messageArrayList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Message msgModel = dataSnapshot.getValue(Message.class);
                    messageArrayList.add(msgModel);

                }
                int newPosition = messageArrayList.size() - 1;
                adapter.notifyItemInserted(newPosition); // Notify the adapter about the new message
                msgAdpter.scrollToPosition(newPosition);
                Log.e("FIREBASE_DATA_GET", "Message: " + String.valueOf(messageArrayList.size()));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        setStatus();
    }

    private void sendNotifications(String msg) {
        Log.d("NOTIFICATION_GET", "Sending notification...");
        FirebaseDatabase.getInstance().getReference("Profile").child(FirebaseAuth.getInstance().getUid()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    ProfileModel currProfile = task.getResult().getValue(ProfileModel.class);
                    try {
                        JSONObject jsonObject = new JSONObject();
                        JSONObject notificationObj = new JSONObject();

                        notificationObj.put("title", currProfile.getName());
                        notificationObj.put("body", msg);

                        JSONObject dataObj = new JSONObject();
                        dataObj.put("uid", currProfile.getUid());

                        jsonObject.put("notification", notificationObj);
                        jsonObject.put("data", dataObj);
                        jsonObject.put("to", fcmToken);

                        callApi(jsonObject);

                    } catch (Exception e) {

                    }
                }
            }
        });
    }

    void callApi(JSONObject jsonObject) {
        Log.d("Notification", "Calling API...");
        ///**IMPORTANT**///

        MediaType JSON = MediaType.get("application/json; charset=utf-8");
        OkHttpClient client = new OkHttpClient();
        String url = "https://fcm.googleapis.com/fcm/send";
        RequestBody body = RequestBody.create(jsonObject.toString(), JSON);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .header("Authorization", "Bearer AAAAylJJp4c:APA91bExKBYgxBOzYbI1Jyg72ew6KlSq8sLF29P8h4hjdq1St2-uiaDz8MskIB4KEM2-HL7X-tkXNVq4fnValb7pmQOgAFmh3YQkMeFK5woRIGRVDwV69Lp8AJjs8k4PZjOUm4-5N08S")
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {

            }
        });
    }

    void setStatus() {
        if (SenderID != null && receiverId != null) {
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Profile");
            DatabaseReference currUserRef = userRef.child(SenderID);

            // Update the user's status to "Online"
            currUserRef.child("status").setValue("Online");

            // When the user logs out or closes the app, update the status to "Offline"
            if (currUserRef != null) {  //becoz isme null pointer execption araha tha
                // Set the user's status to "Offline" on disconnect
                currUserRef.child("status").onDisconnect().setValue("Offline");
            } else {
                Log.e("ChatActivity", "currUserRef is null");
            }

            DatabaseReference receiverRef = database.getInstance().getReference("Profile").child(receiverId).child("status");
            receiverRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String statuss = snapshot.getValue(String.class);
                    Status.setText(statuss);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }
}

