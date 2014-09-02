package org.coax.daydream.swarm;

import android.preference.PreferenceManager;
import android.service.dreams.DreamService;

public class SwarmService extends DreamService {
    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        
        PreferenceManager.getDefaultSharedPreferences(this);
        
        setInteractive(false);
        setFullscreen(true);
        setContentView(new SwarmView(this));
    }
}
