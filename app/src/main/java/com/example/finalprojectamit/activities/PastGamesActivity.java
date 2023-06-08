package com.example.finalprojectamit.activities;

import static com.example.finalprojectamit.activities.MainActivity.CURRENT_USER;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.example.finalprojectamit.models.GameAdapter;
import com.example.finalprojectamit.R;

public class PastGamesActivity extends AppCompatActivity {

    DatePicker dpDate;
    EditText etCity;
    RecyclerView rvGames;
    RadioGroup rgSearch;
    boolean isSearchByCity = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_past_games);

        initWidgets(); // connecting widgets
    }


    public void initWidgets() { // connecting widgets
        dpDate = findViewById(R.id.dpDate);

        dpDate.setOnDateChangedListener(new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                filterList();
            }
        });


        etCity = findViewById(R.id.etCity);

        etCity.addTextChangedListener(new TextWatcher() { //text listener in real time.
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { // if the text changes the function work
                filterList(); // calling for a function
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        rgSearch = findViewById(R.id.rgSearch);
        rgSearch.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.rbCity: // if the user presses the city radio button
                    isSearchByCity = true;
                    etCity.setVisibility(View.VISIBLE); // making the search bar visible
                    dpDate.setVisibility(View.GONE); // making the date search bar invisible
                    break;
                case R.id.rbDate: // if the user presses the date radio button
                    isSearchByCity = false;
                    etCity.setVisibility(View.GONE); // making the search bar visible
                    dpDate.setVisibility(View.VISIBLE); // making the date search bar visible
                    Log.d("date", dpDate.toString());
                    break;
            }
            filterList();
        });

        rvGames = findViewById(R.id.rvGames);
        rvGames.setHasFixedSize(false);
        rvGames.setLayoutManager(new LinearLayoutManager(this));
        rvGames.setAdapter(new GameAdapter(this, this, CURRENT_USER.getGames()));
    }

    public String getFormattedDate() { // receiving the date and changing it to the format yy/mm/dd
        int month = dpDate.getMonth() + 1;
        int day = dpDate.getDayOfMonth() + 1;
        if (month < 10) { // if the month is lower than 10 -> add a zero before
            if (day < 10) //if the day is lower than 10 -> add a zero before
                return dpDate.getYear() + "/0" + month + "/0" + dpDate.getDayOfMonth();
            return dpDate.getYear() + "/0" + month + "/" + dpDate.getDayOfMonth();
        }
        else if (day < 10) //if the day is lower than 10 -> add a zero before
            return  dpDate.getYear() + "/" + month + "/0" + dpDate.getDayOfMonth();
        return dpDate.getYear() + "/" + month + "/" + dpDate.getDayOfMonth();
    }

    public void filterList() { //checking if the user wants to search a game by the date or the city
        if (isSearchByCity)
            rvGames.setAdapter(new GameAdapter(this, this, CURRENT_USER.getGamesByCity(etCity.getText().toString().trim())));
        else {
            Log.e("date",getFormattedDate());
            rvGames.setAdapter(new GameAdapter(this, this, CURRENT_USER.getGamesByDate(getFormattedDate())));
        }
    }

}