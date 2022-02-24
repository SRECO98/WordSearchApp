package com.example.wordsearch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity {

    EditText mFullName, mEmail, mPassword, mPhone;
    Button mRegisterButton;
    TextView mLoginBtn;
    FirebaseAuth fAuth;
    ProgressBar progressBar;
    public static final String TAG = "tag";
    String nickOfUser, emailOfUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mFullName = findViewById(R.id.editTextName);
        mEmail = findViewById(R.id.editTextMail);
        mPassword = findViewById(R.id.editTextPw);
        mPhone = findViewById(R.id.editTextPhone);
        mRegisterButton = findViewById(R.id.buttonLogin);
        mLoginBtn = findViewById(R.id.textViewGoToRegister);

        nickOfUser = mFullName.getText().toString().trim();
        emailOfUser = mEmail.getText().toString().trim();

        fAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.progressBar);
//provjerava da li je korisnik vec prijavljen
        if(fAuth.getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(), VerifyUser.class));
            finish();
        }
    }

    public void buttonRegisterMylseft(View view) {
        String email = mEmail.getText().toString().trim();
        String password = mPassword.getText().toString().trim();
        if(TextUtils.isEmpty(email)){
            mEmail.setError("Email is Required.");
            return;
        }
        if(TextUtils.isEmpty(password)){
            mPassword.setError("Password is Required.");
            return;
        }
        if(password.length() < 6){
            mPassword.setError("Password must have at least 6 characters.");
            return;
        }

        progressBar.setVisibility(View.VISIBLE); //Namjesta da progressbard postane vidljiv jer je korisnik unio sve podatke kako treba.
//addOnComplete provjerava da li je uspjela registracija
        fAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                //uspjesna registracija korisnika
                if(task.isSuccessful()){
                    //verifikacija emaila
                    FirebaseUser fUser = fAuth.getCurrentUser();
                    fUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(RegisterActivity.this, "Verification email has been sent", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d(TAG, "onFailure: Email not send " + e.getMessage());
                        }
                    });
                    Toast.makeText(RegisterActivity.this, "User Created.", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), VerifyUser.class));
                }else{ //neuspjesna registracija korisnika
                    Toast.makeText(RegisterActivity.this, "Error ! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
    }

    public void goToLogin(View view) {
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
    }
}