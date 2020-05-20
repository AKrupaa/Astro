package com.example.astro;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class OverviewActivity extends AppCompatActivity {

//    public static Astronomy astronomy;
    private ViewPager2 myViewPager2;
    private ViewPagerFragmentAdapter myAdapter;
    private SharedViewModel sharedViewModel;
    private TabLayout tabLayout;
    private long cityID;
    public static long delay = 100;
    public static final String INPUTED_TIME = "INPUTED_TIME";

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(DatabaseHelper.CITY_ID, cityID);
        outState.putLong(INPUTED_TIME, delay);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);
        tabLayout = findViewById(R.id.tab_layout);

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                finish(); // ciekawe jak to działa xd
//                sharedViewModel = null;
            } else {
                sharedViewModel = new ViewModelProvider(this).get(SharedViewModel.class);
                cityID = extras.getLong(DatabaseHelper.CITY_ID);
                String value = extras.getString(INPUTED_TIME);
                value = value.replaceAll(" ", "");
                delay = Long.parseLong(value) * 60 * 1000;

                DBManager dbManager = new DBManager(getApplicationContext());
                dbManager.open();

                // pobierz z database wszystko i zaaktulizuj sharedViewModel
                UtilAstro.fetchFromDatabaseAndUpdateViewModel(cityID, dbManager, sharedViewModel);

                dbManager.close();
            }
        } else {
            cityID = savedInstanceState.getLong(DatabaseHelper.CITY_ID);
            sharedViewModel = (SharedViewModel) new ViewModelProvider(this).get(SharedViewModel.class);

            delay = savedInstanceState.getLong(INPUTED_TIME)  * 60 * 1000;
        }

//        astronomy = calculateNewInformationForSunAndMoon();
//        sharedViewModel.setAstronomyMutableLiveData(astronomy);

        myViewPager2 = findViewById(R.id.view_pager2);
        myAdapter = new ViewPagerFragmentAdapter(getSupportFragmentManager(), getLifecycle());
        // dodaj fragmenty
        myAdapter.addFragment(TodayFragment.newInstance());
        myAdapter.addFragment(TomorrowFragment.newInstance());
        myAdapter.addFragment(DayAfterTomorrowFragment.newInstance());
        myAdapter.addFragment(MoonFragment.newInstance());
        myAdapter.addFragment(SunFragment.newInstance());

        myViewPager2.setAdapter(myAdapter);

        // TOP BAR
        final String tabTitles[] = {"Current", "Tomorrow", "Day after tomorrow", "Moon", "Sun"};

        new TabLayoutMediator(tabLayout, myViewPager2, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
//                try{
                    tab.setText(tabTitles[position]);
//                } catch (ArrayIndexOutOfBoundsException e) {
//                    Log.e("pierdoli", "mnie to");
//                }
//                        (tab, position) -> tab.setText("OBJECT " + (position + 1)
            }
        }
        ).attach();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}
