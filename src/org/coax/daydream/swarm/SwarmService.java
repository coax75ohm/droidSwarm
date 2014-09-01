package org.coax.daydream.swarm;

import android.service.dreams.DreamService;

public class SwarmService extends DreamService {
    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        
        setInteractive(false);
        setFullscreen(true);
        setContentView(new SwarmView(this));
    }
}
