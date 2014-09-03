package org.coax.daydream.swarm;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceGroup;

public class SwarmPreferenceActivity extends PreferenceActivity {

    // TODO: find a cleaner way to do this from XML
    private static String def_num_bees;
    private static String min_num_bees;
    private static String max_num_bees;
    private static String def_bee_trail;
    private static String min_bee_trail;
    private static String max_bee_trail;

	@Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        getFragmentManager().beginTransaction().replace(android.R.id.content, new SwarmPreferenceFragment()).commit();

        // TODO: find a cleaner way to do this from XML
        def_num_bees = getResources().getString(R.string.def_num_bees);
        min_num_bees = getResources().getString(R.string.min_num_bees);
        max_num_bees = getResources().getString(R.string.max_num_bees);
        def_bee_trail = getResources().getString(R.string.def_bee_trail);
        min_bee_trail = getResources().getString(R.string.min_bee_trail);
        max_bee_trail = getResources().getString(R.string.max_bee_trail);
    }

    public static class SwarmPreferenceFragment extends PreferenceFragment implements OnSharedPreferenceChangeListener {
        @Override
        public void onCreate(Bundle bundle) {
            super.onCreate(bundle);

            addPreferencesFromResource(R.xml.preferences);
            getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
        }

        @Override
        public void onResume() {
            super.onResume();

            for (int i = 0; i < getPreferenceScreen().getPreferenceCount(); ++i) {
                Preference preference = getPreferenceScreen().getPreference(i);
                if (preference instanceof PreferenceGroup) {
                    PreferenceGroup preferenceGroup = (PreferenceGroup) preference;
                    for (int j = 0; j < preferenceGroup.getPreferenceCount(); ++j) {
                        updatePreference(preferenceGroup.getPreference(j));
                    }
                 }
                 else {
                    updatePreference(preference);
                }
            }
        }

        public void onSharedPreferenceChanged(SharedPreferences sharedpreferences, String key) {
            updatePreference(findPreference(key));
        }

        private void updatePreference(Preference preference) {
            if(preference instanceof EditTextPreference) {
                EditTextPreference edittextpreference = (EditTextPreference)preference;
                EditTextCheckBounds(edittextpreference);
                edittextpreference.setSummary(edittextpreference.getText());
            }

            if(preference instanceof ListPreference) {
                ListPreference listpreference = (ListPreference)preference;
                listpreference.setSummary(listpreference.getEntry());
            }
        }
        
        // TODO: find a cleaner way to do this from XML
        private void EditTextCheckBounds(EditTextPreference edittextpreference) {
            if(edittextpreference.getDialogTitle().equals("Number of Bees")) {
                if(edittextpreference.getText().equals("")) {
                    edittextpreference.setText(def_num_bees);
                }
                if(Integer.parseInt(edittextpreference.getText()) < Integer.parseInt(min_num_bees)) {
                    edittextpreference.setText(min_num_bees);
                }
                if(Integer.parseInt(edittextpreference.getText()) > Integer.parseInt(max_num_bees)) {
                    edittextpreference.setText(max_num_bees);
                }
            }

            if(edittextpreference.getDialogTitle().equals("Bee Trail Length")) {
                if(edittextpreference.getText().equals("")) {
                    edittextpreference.setText(def_bee_trail);
                }
                if(Integer.parseInt(edittextpreference.getText()) < Integer.parseInt(min_bee_trail)) {
                    edittextpreference.setText(min_bee_trail);
                }
                if(Integer.parseInt(edittextpreference.getText()) > Integer.parseInt(max_bee_trail)) {
                    edittextpreference.setText(max_bee_trail);
                }
            }
        }
    }
}
