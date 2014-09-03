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
    private static String pref_num_bees;
    private static String def_num_bees;
    private static String min_num_bees;
    private static String max_num_bees;

    private static String pref_bee_trail;
    private static String def_bee_trail;
    private static String min_bee_trail;
    private static String max_bee_trail;

    private static String pref_bee_acc;
    private static String def_bee_acc;
    private static String min_bee_acc;
    private static String max_bee_acc;

    private static String pref_wasp_acc;
    private static String def_wasp_acc;
    private static String min_wasp_acc;
    private static String max_wasp_acc;

    private static String pref_bee_vel;
    private static String def_bee_vel;
    private static String min_bee_vel;
    private static String max_bee_vel;

    private static String pref_wasp_vel;
    private static String def_wasp_vel;
    private static String min_wasp_vel;
    private static String max_wasp_vel;

    private static String pref_border;
    private static String def_border;
    private static String min_border;
    private static String max_border;

    private static String pref_delay;
    private static String def_delay;
    private static String min_delay;
    private static String max_delay;

    private static String pref_lwidth;
    private static String def_lwidth;
    private static String min_lwidth;
    private static String max_lwidth;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        getFragmentManager().beginTransaction().replace(android.R.id.content, new SwarmPreferenceFragment()).commit();

        // TODO: find a cleaner way to do this from XML
        pref_num_bees  = getResources().getString(R.string.pref_num_bees);
        def_num_bees   = getResources().getString(R.string.def_num_bees);
        min_num_bees   = getResources().getString(R.string.min_num_bees);
        max_num_bees   = getResources().getString(R.string.max_num_bees);

        pref_bee_trail = getResources().getString(R.string.pref_bee_trail);
        def_bee_trail  = getResources().getString(R.string.def_bee_trail);
        min_bee_trail  = getResources().getString(R.string.min_bee_trail);
        max_bee_trail  = getResources().getString(R.string.max_bee_trail);

        pref_bee_acc   = getResources().getString(R.string.pref_bee_acc);
        def_bee_acc    = getResources().getString(R.string.def_bee_acc);
        min_bee_acc    = getResources().getString(R.string.min_bee_acc);
        max_bee_acc    = getResources().getString(R.string.max_bee_acc);

        pref_wasp_acc  = getResources().getString(R.string.pref_wasp_acc);
        def_wasp_acc   = getResources().getString(R.string.def_wasp_acc);
        min_wasp_acc   = getResources().getString(R.string.min_wasp_acc);
        max_wasp_acc   = getResources().getString(R.string.max_wasp_acc);

        pref_bee_vel   = getResources().getString(R.string.pref_bee_vel);
        def_bee_vel    = getResources().getString(R.string.def_bee_vel);
        min_bee_vel    = getResources().getString(R.string.min_bee_vel);
        max_bee_vel    = getResources().getString(R.string.max_bee_vel);

        pref_wasp_vel  = getResources().getString(R.string.pref_wasp_vel);
        def_wasp_vel   = getResources().getString(R.string.def_wasp_vel);
        min_wasp_vel   = getResources().getString(R.string.min_wasp_vel);
        max_wasp_vel   = getResources().getString(R.string.max_wasp_vel);

        pref_border    = getResources().getString(R.string.pref_border);
        def_border     = getResources().getString(R.string.def_border);
        min_border     = getResources().getString(R.string.min_border);
        max_border     = getResources().getString(R.string.max_border);

        pref_delay     = getResources().getString(R.string.pref_delay);
        def_delay      = getResources().getString(R.string.def_delay);
        min_delay      = getResources().getString(R.string.min_delay);
        max_delay      = getResources().getString(R.string.max_delay);

        pref_lwidth    = getResources().getString(R.string.pref_lwidth);
        def_lwidth     = getResources().getString(R.string.def_lwidth);
        min_lwidth     = getResources().getString(R.string.min_lwidth);
        max_lwidth     = getResources().getString(R.string.max_lwidth);
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
            if(edittextpreference.getDialogTitle().equals(pref_num_bees)) {
                if(edittextpreference.getText().equals("")) {
                    edittextpreference.setText(def_num_bees);
                    return;
                }
                if(Integer.parseInt(edittextpreference.getText()) < Integer.parseInt(min_num_bees)) {
                    edittextpreference.setText(min_num_bees);
                    return;
                }
                if(Integer.parseInt(edittextpreference.getText()) > Integer.parseInt(max_num_bees)) {
                    edittextpreference.setText(max_num_bees);
                    return;
                }
                return;
            }

            if(edittextpreference.getDialogTitle().equals(pref_bee_trail)) {
                if(edittextpreference.getText().equals("")) {
                    edittextpreference.setText(def_bee_trail);
                    return;
                }
                if(Integer.parseInt(edittextpreference.getText()) < Integer.parseInt(min_bee_trail)) {
                    edittextpreference.setText(min_bee_trail);
                    return;
                }
                if(Integer.parseInt(edittextpreference.getText()) > Integer.parseInt(max_bee_trail)) {
                    edittextpreference.setText(max_bee_trail);
                    return;
                }
                return;
            }

            if(edittextpreference.getDialogTitle().equals(pref_bee_acc)) {
                if(edittextpreference.getText().equals("")) {
                    edittextpreference.setText(def_bee_acc);
                    return;
                }
                if(Integer.parseInt(edittextpreference.getText()) < Integer.parseInt(min_bee_acc)) {
                    edittextpreference.setText(min_bee_acc);
                    return;
                }
                if(Integer.parseInt(edittextpreference.getText()) > Integer.parseInt(max_bee_acc)) {
                    edittextpreference.setText(max_bee_acc);
                    return;
                }
                return;
            }

            if(edittextpreference.getDialogTitle().equals(pref_wasp_acc)) {
                if(edittextpreference.getText().equals("")) {
                    edittextpreference.setText(def_wasp_acc);
                    return;
                }
                if(Integer.parseInt(edittextpreference.getText()) < Integer.parseInt(min_wasp_acc)) {
                    edittextpreference.setText(min_wasp_acc);
                    return;
                }
                if(Integer.parseInt(edittextpreference.getText()) > Integer.parseInt(max_wasp_acc)) {
                    edittextpreference.setText(max_wasp_acc);
                    return;
                }
                return;
            }

            if(edittextpreference.getDialogTitle().equals(pref_bee_vel)) {
                if(edittextpreference.getText().equals("")) {
                    edittextpreference.setText(def_bee_vel);
                    return;
                }
                if(Integer.parseInt(edittextpreference.getText()) < Integer.parseInt(min_bee_vel)) {
                    edittextpreference.setText(min_bee_vel);
                    return;
                }
                if(Integer.parseInt(edittextpreference.getText()) > Integer.parseInt(max_bee_vel)) {
                    edittextpreference.setText(max_bee_vel);
                    return;
                }
                return;
            }

            if(edittextpreference.getDialogTitle().equals(pref_wasp_vel)) {
                if(edittextpreference.getText().equals("")) {
                    edittextpreference.setText(def_wasp_vel);
                    return;
                }
                if(Integer.parseInt(edittextpreference.getText()) < Integer.parseInt(min_wasp_vel)) {
                    edittextpreference.setText(min_wasp_vel);
                    return;
                }
                if(Integer.parseInt(edittextpreference.getText()) > Integer.parseInt(max_wasp_vel)) {
                    edittextpreference.setText(max_wasp_vel);
                    return;
                }
                return;
            }

            if(edittextpreference.getDialogTitle().equals(pref_border)) {
                if(edittextpreference.getText().equals("")) {
                    edittextpreference.setText(def_border);
                    return;
                }
                if(Integer.parseInt(edittextpreference.getText()) < Integer.parseInt(min_border)) {
                    edittextpreference.setText(min_border);
                    return;
                }
                if(Integer.parseInt(edittextpreference.getText()) > Integer.parseInt(max_border)) {
                    edittextpreference.setText(max_border);
                    return;
                }
                return;
            }

            if(edittextpreference.getDialogTitle().equals(pref_delay)) {
                if(edittextpreference.getText().equals("")) {
                    edittextpreference.setText(def_delay);
                    return;
                }
                if(Integer.parseInt(edittextpreference.getText()) < Integer.parseInt(min_delay)) {
                    edittextpreference.setText(min_delay);
                    return;
                }
                if(Integer.parseInt(edittextpreference.getText()) > Integer.parseInt(max_delay)) {
                    edittextpreference.setText(max_delay);
                    return;
                }
                return;
            }

            if(edittextpreference.getDialogTitle().equals(pref_lwidth)) {
                if(edittextpreference.getText().equals("")) {
                    edittextpreference.setText(def_lwidth);
                    return;
                }
                if(Integer.parseInt(edittextpreference.getText()) < Integer.parseInt(min_lwidth)) {
                    edittextpreference.setText(min_lwidth);
                    return;
                }
                if(Integer.parseInt(edittextpreference.getText()) > Integer.parseInt(max_lwidth)) {
                    edittextpreference.setText(max_lwidth);
                    return;
                }
                return;
            }

        }
    }
}
