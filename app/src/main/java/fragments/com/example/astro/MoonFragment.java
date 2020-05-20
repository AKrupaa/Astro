package com.example.astro;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;


public class MoonFragment extends Fragment {
    private SharedViewModel sharedViewModel;
    private boolean WORK = true;
//    static int i = 0;
    private TextView tvMoonRise, tvMoonSet, tvNewMoon, tvFullMoon, tvPhaseOfTheMoon, tvSynodicMonthDay;

    Thread thread = new Thread(new Runnable() {
        @Override
        public void run() {
            while (WORK) {
                try {
                    Astronomy astronomy = calculateNewInformationForSunAndMoon();
                    fillTextViews(astronomy);
                    Thread.sleep(OverviewActivity.delay);
                } catch (InterruptedException e) {
                    Log.e("MOON THREAD TROLL", "InterruptedException");
                } catch (IllegalArgumentException iae) {
                    OverviewActivity.delay = 10000;
                    thread.start();
                }
            }
        }
    });

    public MoonFragment() {
        // Required empty public constructor
    }

    public static MoonFragment newInstance(/*String param1, String param2*/) {
        MoonFragment fragment = new MoonFragment();
        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_moon, container, false);
        binding(v);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        WORK = true;

        try {
            thread.start();
        } catch (IllegalThreadStateException itse) {
            Log.e("IllegalThreadStateEx", "IllegalThreadStateException");
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        WORK = false;
    }

    private void fillTextViews(Astronomy astronomy) {
        ArrayList<String> moonStringsToTextViews;
        moonStringsToTextViews = astronomy.getMoonInfo();

        // zapisane w Array wartosci TextView (gotowe)
        tvMoonRise.setText(moonStringsToTextViews.remove(0));
        tvMoonSet.setText(moonStringsToTextViews.remove(0));
        tvNewMoon.setText(moonStringsToTextViews.remove(0));
        tvFullMoon.setText(moonStringsToTextViews.remove(0));
        tvPhaseOfTheMoon.setText(moonStringsToTextViews.remove(0));
        tvSynodicMonthDay.setText(moonStringsToTextViews.remove(0));
    }

    private void binding(View v) {
        tvMoonRise = v.findViewById(R.id.moonRise);
        tvMoonSet = v.findViewById(R.id.moonSet);
        tvNewMoon = v.findViewById(R.id.nearestNewMoon);
        tvFullMoon = v.findViewById(R.id.nearestFullMoon);
        tvPhaseOfTheMoon = v.findViewById(R.id.phaseOfTheMoon);
        tvSynodicMonthDay = v.findViewById(R.id.synodicMonthDay);
    }

    public Astronomy calculateNewInformationForSunAndMoon() {
        Astronomy astronomy = new Astronomy();
        Double lat = sharedViewModel.getCommon().getAsDouble(DatabaseHelper.LAT);
        Double lon = sharedViewModel.getCommon().getAsDouble(DatabaseHelper.LON);
        astronomy.setAstroCalculator(lat, lon);
        return astronomy;
    }
}
