package com.example.myapplication;


import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
//import android.preference.CheckBoxPreference;
//import android.preference.ListPreference;
//import android.preference.Preference;
//import android.preference.PreferenceActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.preference.CheckBoxPreference;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import static android.content.Context.MODE_PRIVATE;


public class UserSettingActivity extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.user_pref);
                final SharedPreferences pref =getActivity(). getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        final SharedPreferences.Editor editor = pref.edit();

        final ListPreference listPreference = (ListPreference) findPreference("prefUpdateColor");
        CharSequence currText = listPreference.getEntry();
        CheckBoxPreference checkBoxPreference = (CheckBoxPreference) findPreference("prefNotification");

        checkBoxPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                boolean checked = Boolean.valueOf(newValue.toString());
                editor.putBoolean("notification", checked);
                return true;
            }
        });

        listPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                int index = listPreference.findIndexOfValue(newValue.toString());
                editor.putString("color", (String) listPreference.getEntries()[index]);
                editor.apply();
                View mview = getView().getRootView();
                switch ((String) listPreference.getEntries()[index]) {
                    case "Blue":
                        mview.setBackgroundColor(Color.BLUE);
                        break;
                    case "Red":
                        mview.setBackgroundColor(Color.RED);
                        break;
                    case "White":
                        mview.setBackgroundColor(Color.WHITE);
                        break;
                }
                Toast.makeText(getActivity().getBaseContext(), listPreference.getEntries()[index], Toast.LENGTH_LONG).show();
                return true;
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        SharedPreferences pref = getActivity().getApplicationContext().getSharedPreferences("MyPref", 0);
        String color = pref.getString("color", "White");
//        View mview = findViewById(R.id.main_view);
        View mview = getView().getRootView();
        switch (color) {
            case "Blue":
                mview.setBackgroundColor(Color.BLUE);
                break;
            case "Red":
                mview.setBackgroundColor(Color.RED);
                break;
            case "White":
                mview.setBackgroundColor(Color.WHITE);
                break;
        }
    }
}