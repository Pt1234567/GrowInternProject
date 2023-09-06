package com.example.letschat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.mukeshsolanki.OtpView;

public class OtpActivity extends AppCompatActivity {
    EditText o1,o2,o3,o4,o5,o6;
    Button verify_btn;
    String verificationId;
    FirebaseAuth auth;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        o1=findViewById(R.id.otp1);
        o2=findViewById(R.id.otp2);
        o3=findViewById(R.id.otp3);
        o4=findViewById(R.id.otp4);
        o5=findViewById(R.id.otp5);
        o6=findViewById(R.id.otp6);

               auth=FirebaseAuth.getInstance();
               Intent intent=getIntent();
               verificationId=intent.getStringExtra("VerificationId");
               verify_btn=findViewById(R.id.button_verify);

        verify_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String otp=o1.getText().toString().trim()+o2.getText().toString().trim()+o3.getText().toString().trim()+o4.getText().toString().trim()
                        +o5.getText().toString().trim()+o6.getText().toString().trim();
                if(otp.isEmpty() || otp.length()!=6){
                    Toast.makeText(OtpActivity.this, "Please enter OTP", Toast.LENGTH_SHORT).show();
                }else{
                    verifyOTP(otp);
                }
            }
        });

    }
    private void verifyOTP(String otp){
        PhoneAuthCredential credential= PhoneAuthProvider.getCredential(verificationId,otp);

        signInWithCredential(credential);
    }
    private void signInWithCredential(PhoneAuthCredential credential){
        auth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                startActivity(new Intent(OtpActivity.this,ProfileActivity.class));
                                finish();
                            }else{
                                Toast.makeText(OtpActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                    }
                });
    }
}