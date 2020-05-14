package com.example.astro;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

public class OverviewActivity extends AppCompatActivity {

    private ViewPager2 myViewPager2;
    private ViewPagerFragmentAdapter myAdapter;
    private SharedViewModel sharedViewModel;
    private long cityID;

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(DatabaseHelper.CITY_ID, cityID);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                finish(); // ciekawe jak to działa xd
//                sharedViewModel = null;
            } else {
                sharedViewModel = new ViewModelProvider(this).get(SharedViewModel.class);
                cityID = extras.getLong(DatabaseHelper.CITY_ID);

                DBManager dbManager = new DBManager(getApplicationContext());
                dbManager.open();

                // pobierz z database wszystko i zaaktulizuj sharedViewModel kurwa mać
                UtilAstro.fetchFromDatabaseAndUpdateViewModel(cityID, dbManager, sharedViewModel);

                dbManager.close();
            }
        } else {
            cityID = savedInstanceState.getLong(DatabaseHelper.CITY_ID);
            sharedViewModel = (SharedViewModel) new ViewModelProvider(this).get(SharedViewModel.class);
        }

        myViewPager2 = findViewById(R.id.view_pager2);
        myAdapter = new ViewPagerFragmentAdapter(getSupportFragmentManager(), getLifecycle());
    }


    @Override
    protected void onStart() {
        super.onStart();
        // dodaj fragmenty
        myAdapter.addFragment(todayFragment.newInstance());
        myViewPager2.setAdapter(myAdapter);
    }
}
