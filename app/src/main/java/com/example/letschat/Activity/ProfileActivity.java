package com.example.letschat.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.letschat.R;
import com.example.letschat.model.ProfileModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {
CircleImageView profileImg;
EditText name;
Button create;
FirebaseAuth auth;
FirebaseDatabase database;
FirebaseStorage storage;

Uri selectedImage;
String imageuri;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        profileImg=findViewById(R.id.photo);
        name=findViewById(R.id.name);
        create=findViewById(R.id.button_create);

        //instance
        auth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        storage=FirebaseStorage.getInstance();

        //Image selection
        profileImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageChooser();
            }
        });


        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String n=name.getText().toString();
                if(!n.isEmpty()){
                    //upload image to firebase storage

                    DatabaseReference databaseReference=database.getReference().child("Profile").child(auth.getUid());
                    StorageReference reference=storage.getReference().child("upload").child(auth.getUid());
                    if(selectedImage!=null){
                       reference.putFile(selectedImage).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                           @Override
                           public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                               if(task.isSuccessful()){
                                   reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                       @Override
                                       public void onSuccess(Uri uri) {
                                          imageuri=uri.toString();
                                          String phone=auth.getCurrentUser().getPhoneNumber();
                                          ProfileModel profileModel=new ProfileModel(n,phone, auth.getUid(), imageuri);
                                          databaseReference.setValue(profileModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                                              @Override
                                              public void onComplete(@NonNull Task<Void> task) {
                                                  Log.e("CHECK",profileModel.getName()+" "+profileModel.getPhoneNum());

                                                    if(task.isSuccessful()){
                                                        startActivity(new Intent(ProfileActivity.this, MainActivity.class));
                                                        finish();
                                                    }else{
                                                        Toast.makeText(ProfileActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                    }
                                              }
                                          });
                                       }


                                   });
                               }else{
                                   Toast.makeText(ProfileActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                               }
                           }
                       });
                    }else{
                         String phone=auth.getCurrentUser().getPhoneNumber();
                         ProfileModel profileModel=new ProfileModel(n,phone, auth.getUid());
                         databaseReference.setValue(profileModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                             @Override
                             public void onComplete(@NonNull Task<Void> task) {
                                      if(task.isSuccessful()){
                                          startActivity(new Intent(ProfileActivity.this,MainActivity.class));
                                          Log.e("CHECK",profileModel.getName()+" "+profileModel.getPhoneNum());
                                          finish();
                                      }else{
                                          Toast.makeText(ProfileActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                      }
                             }
                         });

                    }
                }

            }
        });


    }
    void imageChooser(){
        // create an instance of the
        // intent of the type image
        Intent i=new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);

        startActivityForResult(Intent.createChooser(i,"Select Picture"),10);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==10){
            if(data!=null){
                selectedImage=data.getData();
                profileImg.setImageURI(selectedImage);
            }
        }
    }

}
