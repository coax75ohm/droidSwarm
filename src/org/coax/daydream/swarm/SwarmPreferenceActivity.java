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
    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
 
        getFragmentManager().beginTransaction().replace(android.R.id.content, new SwarmPreferenceFragment()).commit();
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
            if(preference instanceof ListPreference) {
                ListPreference listpreference = (ListPreference)preference;
                listpreference.setSummary(listpreference.getEntry());
            }
            if(preference instanceof EditTextPreference) {
                EditTextPreference edittextpreference = (EditTextPreference)preference;
                preference.setSummary(edittextpreference.getText());
            }
        }
    }
}
