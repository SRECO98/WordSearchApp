package com.example.wordsearch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    EditText mEmail,mPassword;
    Button mLoginBtn;
    TextView mCreateButton, forgotTextLink;
    ProgressBar progressBar;
    private FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEmail = findViewById(R.id.editTextMail);
        mPassword = findViewById(R.id.editTextPw);
        progressBar = findViewById(R.id.progressBar2);
        fAuth = FirebaseAuth.getInstance();
        mLoginBtn = findViewById(R.id.buttonLogin);
        mCreateButton = findViewById(R.id.textViewGoToRegister);
        forgotTextLink = findViewById(R.id.textViewForgotPw);
//LOGOVANJE U APLIKACIJU
        mLoginBtn.setOnClickListener(view -> {
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

            progressBar.setVisibility(View.VISIBLE);

            //Authenticate user:
            fAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    //Provjerava da li korisnik sa ovakvim pdoacima postoji registrovan  ubazi, ako da prijavljuje se na akaunt, ako ne izbacuje gresku(vraca poruku korisniku)
                    if(task.isSuccessful()){
                        Toast.makeText(LoginActivity.this, "Logged in successfully", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), VerifyUser.class));
                    }else{
                        Toast.makeText(LoginActivity.this, "Error ! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                    }
                }
            });
        });
//Ako korisnik klikne da je zaboravio sifru, salje mu se na njegov mail poruka u kojoj je napisana njegova lozinka.
        forgotTextLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Pravi edittext u koji korisnik upisuje mail
                EditText resetMail = new EditText(view.getContext());
                AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(view.getContext());
                passwordResetDialog.setTitle("Reset password ?");
                passwordResetDialog.setMessage("Enter your Email to receive a reset link.");
                passwordResetDialog.setView(resetMail);
                passwordResetDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //reset password
                        String mail = resetMail.getText().toString();
                        //ako je uspjesno poslata sifra na mail.
                        fAuth.sendPasswordResetEmail(mail).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(LoginActivity.this, "Reset link sent to your mail.", Toast.LENGTH_SHORT).show();
                            }
                            //ako je neuspjesno poslat mail.
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(LoginActivity.this, "Error, reset link is not sent" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
                passwordResetDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //close the dialog
                    }
                });

                passwordResetDialog.create().show();
            }
        });
    }

    public void goToRegister(View view) {
        startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
    }
}