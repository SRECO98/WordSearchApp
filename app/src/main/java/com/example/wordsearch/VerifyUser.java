package com.example.wordsearch;

import static com.example.wordsearch.DatabaseClass.currentLevelInGame;
import static com.example.wordsearch.RegisterActivity.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class VerifyUser extends AppCompatActivity {

    TextView verifyMessage;
    Button buttonVerify, buttonExit, buttonStart, buttonSignOut, buttonVsPlayer, buttonRankSystem;
    FirebaseAuth auth;
    DatabaseClass databaseClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_user);

        verifyMessage = findViewById(R.id.textViewVerify);
        buttonVerify = findViewById(R.id.buttonVerify);
        buttonStart = findViewById(R.id.buttonPlaySolo);
        buttonVsPlayer = findViewById(R.id.buttonVsPlayer);
        buttonRankSystem = findViewById(R.id.buttonRankSystem);
        buttonSignOut = findViewById(R.id.buttonSignOut);
        buttonExit = findViewById(R.id.buttonExitGame);

        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
//Provjerava da li je email verifikovan

        //Ispisuje iz baze level prije prikaza main aktivitija da bi se ucitalo na vrijeme.
        databaseClass = new DatabaseClass();
        databaseClass.ispisIzBaze();

        if(!user.isEmailVerified()){
            buttonVerify.setVisibility(View.VISIBLE);
            verifyMessage.setVisibility(View.VISIBLE);
            buttonStart.setVisibility(View.GONE);

            buttonVerify.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //verifikacija emaila
                    user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(view.getContext(), "Verification email has been sent", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d(TAG, "onFailure: Email not send " + e.getMessage());
                        }
                    });
                }
            });
        }
        buttonExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.exit(0);
                finish();
            }
        });
        buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(view.getContext().getApplicationContext(), MainActivity.class));
            }
        });
        buttonSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                finish();
            }
        });
        buttonVsPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), PlayerVsPlayer.class));
            }
        });
    }
}