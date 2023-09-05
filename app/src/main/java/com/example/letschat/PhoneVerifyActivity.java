package com.example.letschat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.util.AndroidUtilsLight;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class PhoneVerifyActivity extends AppCompatActivity {
EditText phoneNum;

Button SendBtn;
       //to verify the code
FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_verify);
      phoneNum=findViewById(R.id.phoneNo);
      SendBtn=findViewById(R.id.sendButton);
      auth=FirebaseAuth.getInstance();
     SendBtn.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
             String phonenum=phoneNum.getText().toString().trim();
             if(phonenum.isEmpty()){
                 Toast.makeText(PhoneVerifyActivity.this, "Please enter the number", Toast.LENGTH_SHORT).show();
             }else if(phonenum.length()!=10){
                 Toast.makeText(PhoneVerifyActivity.this, "please enter valid number", Toast.LENGTH_SHORT).show();
             }else{
                 String Num="+91"+phonenum;
                 SendOtp(Num);
             }
         }
     });

         }
         void SendOtp(String num){
             PhoneAuthOptions options=
                     PhoneAuthOptions.newBuilder()
                             .setPhoneNumber(num)
                             .setTimeout(60L,TimeUnit.SECONDS)
                             .setActivity(this)
                             .setCallbacks(callbacks)
                             .build();
             PhoneAuthProvider.verifyPhoneNumber(options);
         }

         PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks=
                 new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
             @Override
             public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

             }

             @Override
             public void onVerificationFailed(@NonNull FirebaseException e) {
                 Toast.makeText(PhoneVerifyActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
             }

                     @Override
                     public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                         super.onCodeSent(s, forceResendingToken);
                         Intent intent=new Intent(PhoneVerifyActivity.this,OtpActivity.class);
                         intent.putExtra("VerificationId",s);
                         startActivity(intent);
                         finish();
                         Toast.makeText(PhoneVerifyActivity.this, "OTP sent successfully", Toast.LENGTH_SHORT).show();
                     }
                 };

}