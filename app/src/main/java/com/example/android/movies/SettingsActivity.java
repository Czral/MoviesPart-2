package com.example.android.movies;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_settings);

    }

    public static class SearchPreferenceFragment extends PreferenceFragment
            implements Preference.OnPreferenceChangeListener {

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            PreferenceManager.setDefaultValues(getActivity().getApplication(), R.xml.settings, false);
            addPreferencesFromResource(R.xml.settings);

            Preference genre = findPreference(getString(R.string.genre));
            bindPreferenceToObject(genre);
            genre.setDefaultValue(1);

            Preference keyword = findPreference(getString(R.string.keyword));
            bindPreferenceToObject(keyword);

            Preference sortBy = findPreference(getString(R.string.sort_by));
            bindPreferenceToObject(sortBy);
            sortBy.setDefaultValue(1);
        }

        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {

            String search = newValue.toString();

            if (preference instanceof ListPreference) {

                ListPreference sortByPreference = (ListPreference) preference;
                ListPreference genrePreference = (ListPreference) preference;

                if (sortByPreference.findIndexOfValue(search) > 0) {
                    int prefIndexSort = ((ListPreference) preference).findIndexOfValue(search);
                    if (prefIndexSort > 0) {
                        CharSequence[] sort_by = ((ListPreference) preference).getEntries();
                        preference.setSummary(sort_by[prefIndexSort]);
                    }
                } else {

                    preference.setSummary(search);
                }

                if (genrePreference.findIndexOfValue(search) > 0) {

                    int prefIndex = ((ListPreference) preference).findIndexOfValue(search);
                    if (prefIndex > 0) {

                        CharSequence[] genre = ((ListPreference) preference).getEntries();
                        preference.setSummary(genre[prefIndex]);
                        if (genre[prefIndex].equals(getString(R.string.all))) {

                            search = "";
                            preference.setSummary(search);
                        }
                    } else {

                        preference.setSummary(search);
                    }
                }
            } else {

                preference.setSummary(search);
            }


            return true;
        }

        private void bindPreferenceToObject(Preference preference) {

            preference.setOnPreferenceChangeListener(this);
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(preference.getContext());
            String string = sharedPreferences.getString(preference.getKey(), "");
            onPreferenceChange(preference, string);
        }

    }

}
