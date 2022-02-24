package com.example.wordsearch;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class DatabaseClass extends RegisterActivity {
    public static final String TAG1 = "TAG";
    private FirebaseFirestore fStore;
    private FirebaseAuth fAuth;
    String userID;
    DocumentReference document;

    public void upisUbazu(int level){
        //uzimanje UID od trenutnog korsinika
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        if(fAuth.getCurrentUser() != null){
        userID = fAuth.getCurrentUser().getUid();
        //Kreiranje kolekcije
        DocumentReference documentReference = fStore.collection("users").document(userID);
        //namjestanje sta se ubacuje u bazu
        Map<String,Object> user = new HashMap<>();
        user.put("level", level);
        user.put("nick", nickOfUser);
        user.put("mail", emailOfUser);
        //ubacivanje u bazu i ispis potvrde u Log.
        documentReference.set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Log.d(TAG1, "onSucces: user data is saved successfully " + userID);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG1, "onFailure: user data is not saved successfully " + userID);
            }
        });
    }
    }
    public static double currentLevelInGame;
    public void ispisIzBaze(){
        //uzimanje iz baze
        fAuth = FirebaseAuth.getInstance();
        userID = fAuth.getCurrentUser().getUid();
        document = FirebaseFirestore.getInstance().collection("users").document(userID);
        document.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    currentLevelInGame = task.getResult().getDouble("level");
                    Log.d("TAG", "2" + task.isSuccessful() + "  "+ currentLevelInGame);
                }else{
                    //upis u bazu
                    upisUbazu(1);
                    currentLevelInGame = 1;
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("TAG", "Failed to get data from base." + e.getMessage());
            }
        });
    }
}
