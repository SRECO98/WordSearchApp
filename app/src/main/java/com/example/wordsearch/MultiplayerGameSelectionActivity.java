/*
package com.example.wordsearch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MultiplayerGameSelectionActivity extends AppCompatActivity {

    Button buttonFindOpponent,buttonFindOpponent2;
    ProgressBar progressBarLoadingPLayer;
    String keyValue = null;
    boolean isCodeMaker = true, codeFound = false, checkTemp = true;
    String code = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiplayer_game_selection);

        buttonFindOpponent = findViewById(R.id.buttonFindOpponent);
        buttonFindOpponent2 = findViewById(R.id.buttonFindOpponent2);
        progressBarLoadingPLayer = findViewById(R.id.progressBarLoadingPlayers);

        buttonFindOpponent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                code = null;
                codeFound = false;
                checkTemp = true;
                keyValue = null;
                code = "123";
                if(code != null && code != ""){
                    isCodeMaker = true;
                    FirebaseDatabase.getInstance().getReference().child("codes").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            boolean check = isValueAvailable(snapshot, code);
                            new Handler().postDelayed(new Runnable(){
                                @Override
                                public void run(){
                                    if(check){
                                        progressBarLoadingPLayer.setVisibility(View.GONE);
                                    }else{
                                        FirebaseDatabase.getInstance().getReference().child("codes").push().setValue(code);
                                        isValueAvailable(snapshot, code);
                                        checkTemp = false;
                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                accepted();
                                                Toast.makeText(getApplicationContext(), "Please dont go back.", Toast.LENGTH_SHORT).show();
                                            }
                                        }, 300);
                                    }
                                }
                            }, 2000);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }else{
                    progressBarLoadingPLayer.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), "Please enter a valid code.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        buttonFindOpponent2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                code = null;
                codeFound = false;
                checkTemp = true;
                keyValue = null;
                code = "123";
                if(code != null && code!= ""){
                    progressBarLoadingPLayer.setVisibility(View.GONE);
                    isCodeMaker = false;
                    FirebaseDatabase.getInstance().getReference().child("codes").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            Boolean data = isValueAvailable(snapshot, code);
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    if(data){
                                        codeFound = true;
                                        accepted();
                                        progressBarLoadingPLayer.setVisibility(View.GONE);
                                    }else{
                                        progressBarLoadingPLayer.setVisibility(View.GONE);
                                        Toast.makeText(getApplicationContext(), "Invalid code", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }, 2000);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }else{
                    Toast.makeText(getApplicationContext(), "pLEASE ENTER A VALID CODE", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void accepted(){
        startActivity(new Intent(getApplicationContext(), PlayerVsPlayer.class));
    }

    public boolean isValueAvailable(DataSnapshot dataSnapshot, String code){
        Iterable<DataSnapshot> data = dataSnapshot.getChildren();
        for(DataSnapshot dataS : data){
            String value = dataS.getValue().toString();
            if(value == code){
                keyValue = dataS.getKey().toString();
                return true;
            }
        }
        return true;
    }
}
*/
