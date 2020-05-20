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

public class SunFragment extends Fragment {

    SharedViewModel sharedViewModel;
    private TextView tvSunRise, tvSunRiseAzimuth, tvSunSet, tvSunSetAzimuth, tvSunTwilight, tvSunCivilDawn;
    private boolean WORK = true;

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

    public SunFragment() {
        // Required empty public constructor
    }

    public static SunFragment newInstance(/*String param1, String param2*/) {
        SunFragment fragment = new SunFragment();
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
        View v = inflater.inflate(R.layout.fragment_sun, container, false);
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
        ArrayList<String> sunStringsToTextViews;
        sunStringsToTextViews = astronomy.getMoonInfo();

        // zapisane w Array wartosci TextView (gotowe)
        tvSunRise.setText(sunStringsToTextViews.remove(0));
        tvSunRiseAzimuth.setText(sunStringsToTextViews.remove(0));
        tvSunSet.setText(sunStringsToTextViews.remove(0));
        tvSunSetAzimuth.setText(sunStringsToTextViews.remove(0));
        tvSunTwilight.setText(sunStringsToTextViews.remove(0));
        tvSunCivilDawn.setText(sunStringsToTextViews.remove(0));
    }

    private void binding(View v) {
        tvSunRise = v.findViewById(R.id.sunRise);
        tvSunRiseAzimuth = v.findViewById(R.id.sunRiseAzimuth);
        tvSunSet = v.findViewById(R.id.sunSet);
        tvSunSetAzimuth = v.findViewById(R.id.sunSetAzimuth);
        tvSunTwilight = v.findViewById(R.id.sunTwilight);
        tvSunCivilDawn = v.findViewById(R.id.sunCivilDawn);
    }

    public Astronomy calculateNewInformationForSunAndMoon() {
        Astronomy astronomy = new Astronomy();
        Double lat = sharedViewModel.getCommon().getAsDouble(DatabaseHelper.LAT);
        Double lon = sharedViewModel.getCommon().getAsDouble(DatabaseHelper.LON);
        astronomy.setAstroCalculator(lat, lon);
        return astronomy;
    }
}
