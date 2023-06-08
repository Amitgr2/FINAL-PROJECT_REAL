package com.example.finalprojectamit.activities;

import static com.example.finalprojectamit.activities.MainActivity.CURRENT_USER;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.finalprojectamit.R;
import com.example.finalprojectamit.models.FirebaseController;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.Arrays;

public class NewGameActivity extends AppCompatActivity implements View.OnClickListener {

    enum DataKeys { // contains all the fields. The numbers are for their +- buttons.
        THREE_KEY, //0,1
        TWO_KEY, // 2,3
        BLOCK_KEY, // 4,5
        ASSISTS_KEY, // MAX_SCORES_DATA,7
        FOULS_KEY, // 8,9
        BALL_LOOSE_KEY, // 10,11
    }
    final int MAX_SCORES_DATA = 6;
    EditText[] etData;
    EditText etCity;
    int[] arrScores;
    Button[] btnAdds, btnMins;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_game);

        initWidgets();
    }

    public void initWidgets() { // function that connects all the features in the new game screen
        initEditTexts(); //calling for a function

        etCity = findViewById(R.id.etCity);
        findViewById(R.id.btnAddGame).setOnClickListener(this); // setting on click listener to all the add button
        arrScores = new int[MAX_SCORES_DATA]; // new array with all the player's scores
        Arrays.fill(arrScores, 0); // filling the array with 0
        initMinButtons(); //calling for a function
        initAddButtons(); //calling for a function

    }

    public void initMinButtons() { // connecting all the min buttons
        btnMins= new Button[MAX_SCORES_DATA]; // crating a new array
        btnMins[DataKeys.THREE_KEY.ordinal()] = findViewById(R.id.btnMinThree);
        btnMins[DataKeys.TWO_KEY.ordinal()] = findViewById(R.id.btnMinTwo);
        btnMins[DataKeys.BLOCK_KEY.ordinal()] = findViewById(R.id.btnMinBlock);
        btnMins[DataKeys.ASSISTS_KEY.ordinal()] = findViewById(R.id.btnMinAssist);
        btnMins[DataKeys.FOULS_KEY.ordinal()] = findViewById(R.id.btnMinFoul);
        btnMins[DataKeys.BALL_LOOSE_KEY.ordinal()] = findViewById(R.id.btnMinBallLoose);

        for(int i=0; i<btnMins.length; i++ ){
            int finalI = i;
            btnMins[finalI].setOnClickListener(new View.OnClickListener() { // moving over the buttons and controlling them in order to add the right amount of points
                @Override
                public void onClick(View v) {
                    if(finalI == DataKeys.THREE_KEY.ordinal())
                        arrScores[finalI] -= 3;
                    else if(finalI == DataKeys.TWO_KEY.ordinal())
                        arrScores[finalI] -=2;
                    else
                        arrScores[finalI] --;
                    if(arrScores[finalI] < 0 ) {
                        arrScores[finalI] = 0;
                        Toast.makeText(NewGameActivity.this, "score can't be bellow 0", Toast.LENGTH_SHORT).show();
                    }
                    etData[finalI].setText(String.valueOf(arrScores[finalI])); // changes the textview to the right amount of points in real time
                }
            });
        }

    }

    public void uploadGame() { // function that uploads the game
        CURRENT_USER.addGame(arrScores[DataKeys.THREE_KEY.ordinal()], // calling for a function the uploads the game to the current user
                arrScores[DataKeys.TWO_KEY.ordinal()],
                arrScores[DataKeys.BLOCK_KEY.ordinal()],
                arrScores[DataKeys.ASSISTS_KEY.ordinal()],
                arrScores[DataKeys.BALL_LOOSE_KEY.ordinal()],
                arrScores[DataKeys.FOULS_KEY.ordinal()],
                etCity.getText().toString().trim());
        new FirebaseController(this).updateUserToFirebase(CURRENT_USER).addOnSuccessListener(new OnSuccessListener<Void>() { // uploading the game into the firebase
            @Override
            public void onSuccess(Void unused) { // when the function is successful
                Toast.makeText(NewGameActivity.this, "The game uploaded", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    public void initAddButtons() { // connecting all the min buttons
        btnAdds= new Button[MAX_SCORES_DATA]; // crating a new array
        btnAdds[DataKeys.THREE_KEY.ordinal()] = findViewById(R.id.btnAddThree);
        btnAdds[DataKeys.TWO_KEY.ordinal()] = findViewById(R.id.btnAddTwo);
        btnAdds[DataKeys.BLOCK_KEY.ordinal()] = findViewById(R.id.btnAddBlock);
        btnAdds[DataKeys.ASSISTS_KEY.ordinal()] = findViewById(R.id.btnAddAssist);
        btnAdds[DataKeys.FOULS_KEY.ordinal()] = findViewById(R.id.btnAddFoul);
        btnAdds[DataKeys.BALL_LOOSE_KEY.ordinal()] = findViewById(R.id.btnAddBallLoose);

        for(int i=0; i<btnAdds.length; i++) { // moving over the buttons and controlling them in order to add the right amount of points
            int finalI = i;
            btnAdds[finalI].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(finalI == DataKeys.THREE_KEY.ordinal())
                        arrScores[finalI] +=3;
                    else if(finalI == DataKeys.TWO_KEY.ordinal())
                        arrScores[finalI] +=2;
                    else
                        arrScores[finalI] ++;
                    etData[finalI].setText(String.valueOf(arrScores[finalI])); // changes the textview to the right amount of points in real time
                }
            });
        }

    }

    public void initEditTexts() { //connecting all the texts
        etData = new EditText[MAX_SCORES_DATA];
        etData[DataKeys.THREE_KEY.ordinal()] = findViewById(R.id.etThreePoints);
        etData[DataKeys.TWO_KEY.ordinal()] = findViewById(R.id.etTwoPoints);
        etData[DataKeys.BLOCK_KEY.ordinal()] = findViewById(R.id.etBlocks);
        etData[DataKeys.ASSISTS_KEY.ordinal()] = findViewById(R.id.etAssists);
        etData[DataKeys.FOULS_KEY.ordinal()] = findViewById(R.id.etFouls);
        etData[DataKeys.BALL_LOOSE_KEY.ordinal()] = findViewById(R.id.etBallLoose);


        for(int i=0 ; i<etData.length ; i++) { // moving over the editTexts and controlling them in order to add the right amount of points
            int finalI = i;
            etData[finalI].setText("0");
            etData[finalI].addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) { // changes the text at the moment
                    if(s.toString().contains("-")) {
                        arrScores[DataKeys.values()[finalI].ordinal()] = 0;
                        etData[finalI].setText("0");
                        Toast.makeText(NewGameActivity.this, "score can't be bellow 0", Toast.LENGTH_SHORT).show();
                    }
                    else arrScores[DataKeys.values()[finalI].ordinal()] = Integer.parseInt(0 + s.toString());
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        }
    }

    @Override
    public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btnAddGame: // if clicking the add game button
                    Log.d("city", etCity.getText().toString());
                    if(TextUtils.isEmpty(etCity.getText().toString().trim())) // checking if the user has entered a city (won't work if clicking space in the city bar)
                        Toast.makeText(this, "You must enter the city!", Toast.LENGTH_SHORT).show();
                    else
                        uploadGame(); // calling for the upload game function
                    break;
            }
    }
}