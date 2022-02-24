package com.example.wordsearch;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class PlayerVsPlayer extends AppCompatActivity {

    public final static int NUMBER_OF_WORDS = 100;

    GridAdapter gridAdapter;
    GridView gridView;
    Button checkButton;
    TextView timerTextView;
    int vrijemeZaLevelUMinutama = 1;
    boolean provjeraDaLiJeTajmerProsao = false;

    int sumOfTotalPoints = 0;
    ArrayList<String> word = new ArrayList<>();

    int[] array;

    String [] colorShades = new String[] {"#FF10E6BC", "#84DE1C", "#83AC55", "#7FE906", "#4B8B02", "#6A834D", "#2B4310", "#006633", "#99FF99", "#193300", "#33FF99", "#00CC66", "#CCFF99", "#003319", "#00FF80", "#FF10E6BC", "#84DE1C", "#83AC55", "#7FE906", "#4B8B02", "#6A834D", "#2B4310", "#006633", "#99FF99", "#193300", "#33FF99", "#00CC66", "#CCFF99", "#003319", "#00FF80", "#FF10E6BC", "#84DE1C", "#83AC55", "#7FE906", "#4B8B02", "#6A834D", "#2B4310", "#006633", "#99FF99", "#193300", "#33FF99", "#00CC66", "#CCFF99", "#003319", "#00FF80"};
    int counterForShades = 0;

    char[] arrayLetters = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O',
            'P', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'E', 'T', 'A', 'I', 'O', 'N', 'S', 'H', 'R'};

    int [] arrayOfUsedLetters = new int [300];
    String[] arrayOfCurrentLetters = new String[100];
    int[] arrayOfNumbers = new int[100];
    String [][]matrixOfCurrentLetters = new String[10][10];

    View container;

    int firstLetter;
    int directionOfWord;
    int blueElement;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_vs_player);

        timerMethod();

        for (int i = 0; i < arrayOfUsedLetters.length; i++) {
            arrayOfUsedLetters[i] = 102;//Stavljanje svih elemenata u nizu na 102 jer tabela ima maximum 100 lokacija.
        }

        timerTextView = findViewById(R.id.timerTextViewPlayerVsPlayer);
        checkButton = findViewById(R.id.checkWordButton);

        Random rand = new Random();
        for (int i = 0; i < arrayOfNumbers.length; i++) {
            arrayOfNumbers[i] = rand.nextInt(33);
            arrayOfCurrentLetters[i] = String.valueOf(arrayLetters[arrayOfNumbers[i]]);
        }

        for (int j = 0; j < 100;) {
            for (int i = 0; i < 10; i++) {
                for (int u = 0; u < 10; u++,j++) {
                    matrixOfCurrentLetters[i][u] = arrayOfCurrentLetters[j];
                }
            }
        }

        gridView = findViewById(R.id.grid);

        gridAdapter = new GridAdapter(PlayerVsPlayer.this, arrayOfCurrentLetters);
        gridView.setAdapter(gridAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) { //adapterView tabela, view - jedno polje, i - 0-99, l - row uvijek 0

                if(provjeraDaLiJeTajmerProsao){
                    Toast.makeText(getApplicationContext(), "You can try again by pressing Replay.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if((colorShades.length - 1) == counterForShades){
                    counterForShades = 0;
                }
                for (int u = 0; u < arrayOfUsedLetters.length; u++) {
                    if(arrayOfUsedLetters[u] == i)
                        return;
                }
                //gridView.setNumColumns(9);
                word.add(arrayOfCurrentLetters[i]);

                if (brojac > 1 && blueElement == i) {
                    colorChangeAllField(i, view);
                }

                if (brojac == 1) {
                    boolean check = check(array, i);
                    if (check) {
                        view.setBackgroundColor(Color.parseColor(colorShades[counterForShades]));
                        changeColor2(firstLetter, i, array);
                    }
                }
                if (brojac == 0 && (i == 0 || i == 9 || i == 90 || i == 99)) {
                    firstLetter = i;
                    if (i == 0) {
                        array = new int[]{(i + 1), (i + 10), (i + 11)};
                        colorChange(i, i + 1, i + 10, i + 11);
                    } else if (i == 9) {
                        array = new int[]{(i - 1), (i + 9), (i + 10)};
                        colorChange(i, i - 1, i + 9, i + 10);
                    } else if (i == 90) {
                        array = new int[]{(i - 10), (i - 9), (i + 1)};
                        colorChange(i, i - 10, i - 9, i + 1);
                    } else {
                        array = new int[]{(i - 11), (i - 10), (i - 1)};
                        colorChange(i, i - 11, i - 10, i - 1);
                    }
                    view.setBackgroundColor(Color.parseColor(colorShades[counterForShades]));
                } else if (brojac == 0 && !(((i % 10) == 0) || ((i % 10) == 9))) {
                    if (i < 10) {
                        firstLetter = i;
                        array = new int[]{(i - 1), (i + 1), (i + 9), (i + 10), (i + 11)};
                        colorChange(i, i - 1, i + 1, i + 9, i + 10, i + 11);
                        view.setBackgroundColor(Color.parseColor(colorShades[counterForShades]));
                    } else if (i > 89) {
                        firstLetter = i;
                        array = new int[]{(i - 11), (i - 10), (i - 9), (i - 1), (i + 1)};
                        colorChange(i, i - 11, i - 10, i - 9, i - 1, i + 1);
                        view.setBackgroundColor(Color.parseColor(colorShades[counterForShades]));
                    } else {
                        firstLetter = i;
                        array = new int[]{(i - 11), (i - 10), (i - 9), (i - 1), (i + 1), (i + 9), (i + 10), (i + 11)};
                        colorChange(i, i - 11, i - 10, i - 9, i - 1, i + 1, i + 9, i + 10, i + 11);
                        view.setBackgroundColor(Color.parseColor(colorShades[counterForShades]));
                    }
                } else if (brojac == 0 && i % 10 == 0) {
                    firstLetter = i;
                    array = new int[]{(i - 10), (i - 9), (i + 1), (i + 10), (i + 11)};
                    colorChange(i, i - 10, i - 9, i + 1, i + 10, i + 11);
                    view.setBackgroundColor(Color.parseColor(colorShades[counterForShades]));
                } else if (brojac == 0 && i % 10 == 9) {
                    firstLetter = i;
                    array = new int[]{(i - 11), (i - 10), (i - 1), (i + 9), (i + 10)};
                    colorChange(i, i - 11, i - 10, i - 1, i + 9, i + 10);
                    view.setBackgroundColor(Color.parseColor(colorShades[counterForShades]));
                }
            }
        });

    }

    int counterForLetters = 0;

    public void colorChangeAllField(int position, View view) {
        addLetterToUsedLetters(position);
        int check = 0;
        switch (directionOfWord) {
            case 1:
                blueElement = position - 11;
                view.setBackgroundColor(Color.parseColor(colorShades[counterForShades]));
                if (!((position < 10) || (position % 10 == 0))) {
                    view = gridView.getChildAt(blueElement);
                    for (int i = 0; i < arrayOfUsedLetters.length; i++) {
                        if(arrayOfUsedLetters[i] == 150)  //CHECK BROJI KOJA JE RIJEC PO REDU DA BI PREBACILO BOJU POLJA KAO I TA RIJEC.
                            check++;
                        if(blueElement != arrayOfUsedLetters[i])
                            view.setBackgroundColor(Color.parseColor("#79040C"));
                        else{
                            view.setBackgroundColor(Color.parseColor(colorShades[check]));
                            break;
                        }
                    }
                }
                brojac++;
                break;
            case 2:
                blueElement = position - 10;
                view.setBackgroundColor(Color.parseColor(colorShades[counterForShades]));
                if (!(position < 10)) {
                    view = gridView.getChildAt( blueElement);
                    for (int i = 0; i < arrayOfUsedLetters.length; i++) {
                        if(arrayOfUsedLetters[i] == 150)
                            check++;
                        if(blueElement != arrayOfUsedLetters[i])
                            view.setBackgroundColor(Color.parseColor("#79040C"));
                        else{
                            view.setBackgroundColor(Color.parseColor(colorShades[check]));
                            break;
                        }
                    }
                }
                brojac++;
                break;
            case 3:
                blueElement = position - 9;
                view.setBackgroundColor(Color.parseColor(colorShades[counterForShades]));
                if (!(position < 10 || position % 10 == 9)) {
                    view = gridView.getChildAt(blueElement);
                    for (int i = 0; i < arrayOfUsedLetters.length; i++) {
                        if(arrayOfUsedLetters[i] == 150)
                            check++;
                        if(blueElement != arrayOfUsedLetters[i])
                            view.setBackgroundColor(Color.parseColor("#79040C"));
                        else{
                            view.setBackgroundColor(Color.parseColor(colorShades[check]));
                            break;
                        }
                    }
                }
                brojac++;
                break;
            case 4:
                blueElement = position - 1;
                view.setBackgroundColor(Color.parseColor(colorShades[counterForShades]));
                if (!(position % 10 == 0)) {
                    view = gridView.getChildAt(blueElement);
                    for (int i = 0; i < arrayOfUsedLetters.length; i++) {
                        if(arrayOfUsedLetters[i] == 150)
                            check++;
                        if(blueElement != arrayOfUsedLetters[i])
                            view.setBackgroundColor(Color.parseColor("#79040C"));
                        else{
                            view.setBackgroundColor(Color.parseColor(colorShades[check]));
                            break;
                        }
                    }
                }
                brojac++;
                break;
            case 5:
                blueElement = position + 1;
                view.setBackgroundColor(Color.parseColor(colorShades[counterForShades]));
                if (!(position % 10 == 9)) {
                    view = gridView.getChildAt(blueElement);
                    for (int i = 0; i < arrayOfUsedLetters.length; i++) {
                        if(arrayOfUsedLetters[i] == 150)
                            check++;
                        if(blueElement != arrayOfUsedLetters[i])
                            view.setBackgroundColor(Color.parseColor("#79040C"));
                        else{
                            view.setBackgroundColor(Color.parseColor(colorShades[check]));
                            break;
                        }
                    }
                }
                brojac++;
                break;
            case 6:
                blueElement = position + 9;
                view.setBackgroundColor(Color.parseColor(colorShades[counterForShades]));
                if (!(position % 10 == 0 || position > 89)) {
                    view = gridView.getChildAt(blueElement);
                    for (int i = 0; i < arrayOfUsedLetters.length; i++) {
                        if(arrayOfUsedLetters[i] == 150)
                            check++;
                        if(blueElement != arrayOfUsedLetters[i])
                            view.setBackgroundColor(Color.parseColor("#79040C"));
                        else{
                            view.setBackgroundColor(Color.parseColor(colorShades[check]));
                            break;
                        }
                    }
                }
                brojac++;
                break;
            case 7:
                blueElement = position + 10;
                view.setBackgroundColor(Color.parseColor(colorShades[counterForShades]));
                if (!(position > 89)) {
                    view = gridView.getChildAt(blueElement);
                    for (int i = 0; i < arrayOfUsedLetters.length; i++) {
                        if(arrayOfUsedLetters[i] == 150)
                            check++;
                        if(blueElement != arrayOfUsedLetters[i])
                            view.setBackgroundColor(Color.parseColor("#79040C"));
                        else{
                            view.setBackgroundColor(Color.parseColor(colorShades[check]));
                            break;
                        }
                    }
                }
                brojac++;
                break;
            case 8:
                blueElement = position + 11;
                view.setBackgroundColor(Color.parseColor(colorShades[counterForShades]));
                if (!(position > 89 || position % 10 == 9)) {
                    view = gridView.getChildAt(blueElement);
                    for (int i = 0; i < arrayOfUsedLetters.length; i++) {
                        if(arrayOfUsedLetters[i] == 150)
                            check++;
                        if(blueElement != arrayOfUsedLetters[i])
                            view.setBackgroundColor(Color.parseColor("#79040C"));
                        else{
                            view.setBackgroundColor(Color.parseColor(colorShades[check]));
                            break;
                        }
                    }
                }
                brojac++;
                break;
        }
    }


    int secondLetter;
    public void changeColor2(int firstPosition, int position, int[] array) {

        addLetterToUsedLetters(position);
        View viewAround;
        int check;
        for (int i = 0; i < array.length; i++) {
            viewAround = gridView.getChildAt(array[i]);
            check = 0;
            if (position != array[i]) {
                for (int u = 0; u < arrayOfUsedLetters.length; u++) {
                    if(arrayOfUsedLetters[u] == 150)
                        check++;
                    if(array[i] == arrayOfUsedLetters[u]){
                        viewAround.setBackgroundColor(Color.parseColor(colorShades[check]));//BOJI NAKON STISKANJA DRUGOG SLOVA OKO PRVOG SVE U ZELENO koji su iskoristeni
                        break;
                    }else {
                        viewAround.setBackgroundColor(Color.BLACK);
                    }
                }
            }
        }
        int perm = firstPosition - position;
        secondLetter = position;
        switch (perm) {
            case 11:
                position = firstPosition - 22;
                directionOfWord = 1;
                break;
            case 10:
                position = firstPosition - 20;
                directionOfWord = 2;
                break;
            case 9:
                position = firstPosition - 18;
                directionOfWord = 3;
                break;
            case 1:
                position = firstPosition - 2;
                directionOfWord = 4;
                break;
            case -1:
                position = firstPosition + 2;
                directionOfWord = 5;
                break;
            case -9:
                position = firstPosition + 18;
                directionOfWord = 6;
                break;
            case -10:
                position = firstPosition + 20;
                directionOfWord = 7;
                break;
            case -11:
                position = firstPosition + 22;
                directionOfWord = 8;
                break;
        }
        blueElement = position;
        View view = gridView.getChildAt(blueElement);

        if(border == 0) {
            if ((blueElement < 0 || blueElement > 99)) {
                return;
            } else {
                for (int i = 0; i < arrayOfUsedLetters.length; i++) {
                    if (blueElement != arrayOfUsedLetters[i])
                        view.setBackgroundColor(Color.parseColor("#79040C"));
                    else {
                        view.setBackgroundColor(Color.parseColor(colorShades[counterForShades]));
                        break;
                    }
                }
            }
        }
        if((blueElement % 10 == 0 || blueElement % 10 == 9)) {
            border++;
        }
    }

    int border = 0;

    public boolean check(int[] array, int position) {
        for (int i = 0; i < array.length; i++) {
            if (array[i] == position) {
                brojac++;
                return true;
            }
        }
        return false;
    }

    int brojac = 0;
    int [] arrayHolder = new int[9];
    public void colorChange(int k, int... position) {
        for (int i = 0; i < position.length; i++) {
            arrayHolder[i] = position[i];
        }
        arrayHolder[8] = position.length;
        addLetterToUsedLetters(k);
        View view;
        int check;
        for (int i = 0; i < position.length; i++) {
            view = gridView.getChildAt(position[i]);
            check = 0;
            for (int u = 0; u < arrayOfUsedLetters.length; u++) {
                if ( !(arrayOfUsedLetters[u] == position[i]) ){     // t i e   e
                    if(arrayOfUsedLetters[u] == 150)
                        check++;
                    view.setBackgroundColor(Color.parseColor("#79040C"));
                }else{
                    if(counterForShades > 0)
                        view.setBackgroundColor(Color.parseColor(colorShades[check]));
                    else
                        view.setBackgroundColor(Color.parseColor(colorShades[counterForShades]));
                    break;
                }
            }
        }
        brojac++;
    }

    String [] words = new String[NUMBER_OF_WORDS];
    int counterForWords = 0;
    public void checkButton(View view) {
        addLetterToUsedLetters(150);
        int check2;
        counterForShades++;
        if(brojac < 2) {
            for (int i = 0; i < arrayHolder.length; i++) {
                check2 = 0;
                for (int u = 0; u < arrayOfUsedLetters.length; u++) {
                    if (arrayOfUsedLetters[u] == 150)
                        check2++;
                    if (!(arrayOfUsedLetters[u] == arrayHolder[i])) {
                        container = gridView.getChildAt(arrayHolder[i]);
                        container.setBackgroundColor(Color.BLACK);
                    } else {
                        container = gridView.getChildAt(arrayHolder[i]);
                        container.setBackgroundColor(Color.parseColor(colorShades[check2]));
                        break;
                    }
                }
            }

            boolean check = false;
            for (int i = 0; i < arrayOfUsedLetters.length; i++) {   //z  == z   check true
                if(arrayOfUsedLetters[i] == firstLetter){
                    check = true;
                    break;
                }
            }

            if(check){
                if(brojac == 1) {
                    for (int i = 0; i < arrayOfUsedLetters.length; i++) {
                        if (firstLetter == arrayOfUsedLetters[i]) {
                            for (int u = i; u < arrayOfUsedLetters.length - 1; u++) {  //IZBACUJE SLOVO IZ ISKORISTENOG NIZA
                                arrayOfUsedLetters[u] = arrayOfUsedLetters[u + 1];
                            }
                        }
                    }
                    container = gridView.getChildAt(firstLetter);
                    container.setBackgroundColor(Color.BLACK);
                }
            }
        }
        for (int i = 0; i < word.size(); i++) {
            if(words[counterForWords] == null)
                words[counterForWords] = "";
            words[counterForWords] = words[counterForWords] + word.get(i);  //words[0] = PRVA RIJEC
        }
        word.clear();  //BRISE RIJEC IZ LISTE
        if(words[counterForWords] != null) {
            checkIsWordInFile(words[counterForWords], this);
            //Toast.makeText(getApplicationContext(), words[counterForWords] , Toast.LENGTH_LONG).show(); Ovo je radi testiranja, nije potrebno
            if(brojac > 2) {
                if(checkIsWordCorrect)
                    Toast.makeText(getApplicationContext(), (brojac * 2) + " points for you!", Toast.LENGTH_SHORT).show();
                else {
                    Toast.makeText(getApplicationContext(), "Word doesn't exist. Find another one!", Toast.LENGTH_LONG).show();
                    View view2 = gridView.getChildAt(firstLetter);
                    view2.setBackgroundColor(Color.BLACK);
                    int forCase = firstLetter - secondLetter;
                    switch (forCase){
                        case -11:
                            for (int i = 1, p = 11; i < brojac; i++, p = p + 11) {
                                view = gridView.getChildAt(firstLetter + p);
                                view.setBackgroundColor(Color.BLACK);
                                for (int k = 0; k < arrayOfUsedLetters.length; k++) {
                                    if ( (firstLetter == arrayOfUsedLetters[k] || (firstLetter + p) == arrayOfUsedLetters[k]) ) {
                                        for (int u = k; u < arrayOfUsedLetters.length - brojac; u++) {  //IZBACUJE SLOVO IZ ISKORISTENOG NIZA
                                            arrayOfUsedLetters[u] = arrayOfUsedLetters[u + brojac];
                                        }
                                        k = 0;
                                    }
                                }
                            }
                            break;
                        case -10:
                            for (int i = 1, p = 10; i < brojac; i++, p = p + 10) {
                                view = gridView.getChildAt(firstLetter + p);
                                view.setBackgroundColor(Color.BLACK);
                                for (int k = 0; k < arrayOfUsedLetters.length; k++) {
                                    if ( (firstLetter == arrayOfUsedLetters[k] || (firstLetter + p) == arrayOfUsedLetters[k]) ) {
                                        for (int u = k; u < arrayOfUsedLetters.length - brojac; u++) {  //IZBACUJE SLOVO IZ ISKORISTENOG NIZA
                                            arrayOfUsedLetters[u] = arrayOfUsedLetters[u + brojac];
                                        }
                                        k = 0;
                                    }
                                }
                            }
                            break;
                        case -9:
                            for (int i = 1, p = 9; i < brojac; i++, p = p + 9) {
                                view = gridView.getChildAt(firstLetter + p);
                                view.setBackgroundColor(Color.BLACK);
                                for (int k = 0; k < arrayOfUsedLetters.length; k++) {
                                    if ( (firstLetter == arrayOfUsedLetters[k] || (firstLetter + p) == arrayOfUsedLetters[k]) ) {
                                        for (int u = k; u < arrayOfUsedLetters.length - brojac; u++) {  //IZBACUJE SLOVO IZ ISKORISTENOG NIZA
                                            arrayOfUsedLetters[u] = arrayOfUsedLetters[u + brojac];
                                        }
                                        k = 0;
                                    }
                                }
                            }
                            break;
                        case -1:
                            for (int i = 1, p = 1; i < brojac; i++, p++) {
                                view = gridView.getChildAt(firstLetter + p);
                                view.setBackgroundColor(Color.BLACK);
                                for (int k = 0; k < arrayOfUsedLetters.length; k++) {
                                    if ( (firstLetter == arrayOfUsedLetters[k] || (firstLetter + p) == arrayOfUsedLetters[k]) ) {
                                        for (int u = k; u < arrayOfUsedLetters.length - brojac; u++) {  //IZBACUJE SLOVO IZ ISKORISTENOG NIZA
                                            arrayOfUsedLetters[u] = arrayOfUsedLetters[u + brojac];
                                        }
                                        k = 0;
                                    }
                                }
                            }
                            break;
                        case 1:
                            for (int i = 1, p = -1; i < brojac; i++, p--) {
                                view = gridView.getChildAt(firstLetter + p);
                                view.setBackgroundColor(Color.BLACK);
                                for (int k = 0; k < arrayOfUsedLetters.length; k++) {
                                    if ( (firstLetter == arrayOfUsedLetters[k] || (firstLetter + p) == arrayOfUsedLetters[k]) ) {
                                        for (int u = k; u < arrayOfUsedLetters.length - brojac; u++) {  //IZBACUJE SLOVO IZ ISKORISTENOG NIZA
                                            arrayOfUsedLetters[u] = arrayOfUsedLetters[u + brojac];
                                        }
                                        k = 0;
                                    }
                                }
                            }
                            break;
                        case 9:
                            for (int i = 1, p = -9; i < brojac; i++, p = p - 9) {
                                view = gridView.getChildAt(firstLetter + p);
                                view.setBackgroundColor(Color.BLACK);
                                for (int k = 0; k < arrayOfUsedLetters.length; k++) {
                                    if ( (firstLetter == arrayOfUsedLetters[k] || (firstLetter + p) == arrayOfUsedLetters[k]) ) {
                                        for (int u = k; u < arrayOfUsedLetters.length - brojac; u++) {  //IZBACUJE SLOVO IZ ISKORISTENOG NIZA
                                            arrayOfUsedLetters[u] = arrayOfUsedLetters[u + brojac];
                                        }
                                        k = 0;
                                    }
                                }
                            }
                            break;
                        case 10:
                            for (int i = 1, p = -10; i < brojac; i++, p = p - 10) {
                                view = gridView.getChildAt(firstLetter + p);
                                view.setBackgroundColor(Color.BLACK);
                                for (int k = 0; k < arrayOfUsedLetters.length; k++) {
                                    if ( (firstLetter == arrayOfUsedLetters[k] || (firstLetter + p) == arrayOfUsedLetters[k]) ) {
                                        for (int u = k; u < arrayOfUsedLetters.length - brojac; u++) {  //IZBACUJE SLOVO IZ ISKORISTENOG NIZA
                                            arrayOfUsedLetters[u] = arrayOfUsedLetters[u + brojac];
                                        }
                                        k = 0;
                                    }
                                }
                            }
                            break;
                        case 11:
                            for (int i = 1, p = -11; i < brojac; i++, p = p - 11) {
                                view = gridView.getChildAt(firstLetter + p);
                                view.setBackgroundColor(Color.BLACK);
                                for (int k = 0; k < arrayOfUsedLetters.length; k++) {
                                    if ( (firstLetter == arrayOfUsedLetters[k] || (firstLetter + p) == arrayOfUsedLetters[k]) ) {
                                        for (int u = k; u < arrayOfUsedLetters.length - brojac; u++) {  //IZBACUJE SLOVO IZ ISKORISTENOG NIZA
                                            arrayOfUsedLetters[u] = arrayOfUsedLetters[u + brojac];
                                        }
                                        k = 0;
                                    }
                                }
                            }
                            break;
                    }
                }
                checkIsWordCorrect = false;
            }
            counterForWords++;  // Povecava se kad korisnik stisne checkButton i krene nova rijec da se crta.
        }
        container = gridView.getChildAt(blueElement);
        int check = 0;
        if(blueElement > -1 && blueElement < 100) {
            container.setBackgroundColor(Color.BLACK);
            for (int i = 0; i < arrayOfUsedLetters.length; i++) {
                if(arrayOfUsedLetters[i] == 150)
                    check++;
                if ((arrayOfUsedLetters[i] == blueElement)) {
                    container.setBackgroundColor(Color.parseColor(colorShades[check]));
                    break;
                }
            }
        }
        if(brojac == 0){
            Toast.makeText(getApplicationContext(), "Please, find a word!", Toast.LENGTH_SHORT).show();
        }else if(brojac == 1 || brojac == 2){
            Toast.makeText(getApplicationContext(), "C'mon, word must have 3 letters at least!", Toast.LENGTH_SHORT).show();
        }
        if(brojac == 2){
            View colorBlackView = gridView.getChildAt(firstLetter);
            colorBlackView.setBackgroundColor(Color.BLACK);
            colorBlackView = gridView.getChildAt(secondLetter);
            colorBlackView.setBackgroundColor(Color.BLACK);


            for (int i = 0; i < arrayOfUsedLetters.length; i++) {
                if ( (firstLetter == arrayOfUsedLetters[i] || secondLetter == arrayOfUsedLetters[i]) ) {
                    for (int u = i; u < arrayOfUsedLetters.length - 2; u++) {  //IZBACUJE SLOVO IZ ISKORISTENOG NIZA
                        arrayOfUsedLetters[u] = arrayOfUsedLetters[u + 2];
                        arrayOfUsedLetters[(u+1)] = arrayOfUsedLetters[u + 2];
                    }
                    i = 0;
                }
            }
        }
        brojac = 0; // NOVA RIJEC
        border = 0;

    }

    //--------------------------------------------------------------------------------
    public void addLetterToUsedLetters(int position){
        for(;true;){
            arrayOfUsedLetters[counterForLetters++] = position;
            break;
        }
    }
    boolean checkIsWordCorrect = false;
    public void checkIsWordInFile(String word, Context context){
        try {
            InputStream file = context.getResources().openRawResource(R.raw.rijeci);
            BufferedReader br = new BufferedReader(new InputStreamReader(file));
            String line = br.readLine();
            while (line != null) {
                if (word.equalsIgnoreCase(line)) {
                    checkIsWordCorrect = true;
                    sumOfTotalPoints = sumOfTotalPoints + (word.length() * 2);
                }
                line = br.readLine();
            }
            file.close();
        }catch (IOException exception){
            exception.printStackTrace();
        }
    }

    public boolean controlTimerReset = false;
    CountDownTimer countDownTimer;
    public void timerMethod() {
        //Restartuje timer na pocetak ako korisnik predjen level ili ide na replay, kontrolise se pomocu varijable controlTimerReset
        if (countDownTimer != null && controlTimerReset) {
            countDownTimer.cancel();
        }
        //VRIJEME ZA PRELAZAK LEVELA------------------------------------------
        long duration = TimeUnit.MINUTES.toMillis(vrijemeZaLevelUMinutama);
        countDownTimer = new CountDownTimer(duration, 1000) {
            @Override
            public void onTick(long l) {
                //promjena pri svakoj sekundi
                String sDuration = String.format(Locale.ENGLISH, "%02d : %02d"
                        , TimeUnit.MILLISECONDS.toMinutes(l)
                        , TimeUnit.MILLISECONDS.toSeconds(l) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(l)));

                if (l < 11000) { // ako je ispod 11 sekundi ispusuje timer zutom bojom
                    timerTextView.setTextColor(Color.YELLOW);
                }
                timerTextView.setText(sDuration);
            }

            @Override
            public void onFinish() {
                timerTextView.setTextColor(Color.RED);
            }
        }.start();
    }
    //-----------------KADA KORISNIK KLIKNE NA BACK DUGME
    @Override
    public void onBackPressed() {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Main Menu")
                .setMessage("Exit game ?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        startActivity(new Intent(PlayerVsPlayer.this, VerifyUser.class)); // Vraca na mainmenu
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //
                    }
                })
                .show();
    }

    public void buttonRematch(View view) {
    }

    public void buttonResign(View view) {
    }
}