package org.coax.daydream.swarm;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class SwarmView extends SurfaceView implements SurfaceHolder.Callback {

    private int width;
    private int height;
    private SurfaceHolder holder;
    private Canvas canvas;
    
    private static final int BEES     = 20;	// number of bees
    private static final int REPEAT   = 3;	// number of time positions recorded
    private static final int BEE_ACC  = 3;	// acceleration of bees
    private static final int WASP_ACC = 5;	// maximum acceleration of wasp
    private static final int BEE_VEL  = 11;	// maximum bee velocity
    private static final int WASP_VEL = 12;	// maximum wasp velocity
    private static final int BORDER   = 50;	// wasp won't go closer than this to the edges
    private static final int DELAY    = 40;	// millisecond delay between updates
    
    int[] wasp_x = new int[2];
    int[] wasp_y = new int[2];
    int wasp_xv;
    int wasp_yv;
    
    int[][] bee_x = new int[BEES][REPEAT];
    int[][] bee_y = new int[BEES][REPEAT];
    int[] bee_xv  = new int[BEES];
    int[] bee_yv  = new int[BEES];
    
    Paint wasp_p, bee_p;

    int dx, dy, distance;
    
    public SwarmView(Context context) {
        super(context);
        
        holder = getHolder();
        holder.addCallback(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder sh) {

    	new Thread() {
            @Override
            public void run() {
        
            	canvas = null;

                // initialize wasp
            	wasp_x[0] = (int)(Math.random() * (float)((width - 2 * BORDER) + BORDER));
                wasp_y[0] = (int)(Math.random() * (float)((height - 2 * BORDER) + BORDER));
                wasp_x[1] = wasp_x[0];
                wasp_y[1] = wasp_y[0];
                
                wasp_xv = 0;
                wasp_yv = 0;
                
                wasp_p = new Paint();
                //wasp_p.setAntiAlias(true);
                wasp_p.setColor(Color.RED);

                // initialize bees
                for(int i = 0; i < BEES; i++) {
                	bee_x[i][0] = (int)(Math.random() * (float)width);
                	bee_y[i][0] = (int)(Math.random() * (float)height);
                	bee_x[i][1] = bee_x[i][0];
                	bee_y[i][1] = bee_y[i][0];
                	
                	bee_xv[i] = randPlusMinus(7);
                	bee_yv[i] = randPlusMinus(7);
                }

                bee_p = new Paint();
                //bee_p.setAntiAlias(true);
                bee_p.setColor(Color.YELLOW);

                // animation loop
                while (true) {
                    canvas = holder.lockCanvas();
                	 
                	if (canvas == null) {
                        break;
                    }
                	
                	// <=- wasp -=>
                	// age the arrays
                	wasp_x[1] = wasp_x[0];
                	wasp_y[1] = wasp_y[0];
                	
                	// accelerate
                	wasp_xv += randPlusMinus(WASP_ACC);
                	wasp_yv += randPlusMinus(WASP_ACC);
                	
                	// speed limit checks
                	if (wasp_xv > WASP_VEL) {
                		wasp_xv = WASP_VEL;
                	}
                	if (wasp_xv < -WASP_VEL) {
                		wasp_xv = -WASP_VEL;
                	}
                	if (wasp_yv > WASP_VEL) {
                		wasp_yv = WASP_VEL;
                	}
                	if (wasp_yv < -WASP_VEL) {
                		wasp_yv = -WASP_VEL;
                	}

                	// move
                	wasp_x[0] = wasp_x[1] + wasp_xv;
                	wasp_y[0] = wasp_y[1] + wasp_yv;

                	// bounce checks
                	if((wasp_x[0] < BORDER) || (wasp_x[0] > (width - BORDER - 1))) {
                		wasp_xv = -wasp_xv;
                		wasp_x[0] += wasp_xv;
                	}
                	if((wasp_y[0] < BORDER) || (wasp_y[0] > (height - BORDER - 1))) {
                		wasp_yv = -wasp_yv;
                		wasp_y[0] += wasp_yv;
                	}
                	
                	// don't let things settle down
                	bee_xv[(int)(Math.random() * BEES)] += randPlusMinus(3);
                	bee_yv[(int)(Math.random() * BEES)] += randPlusMinus(3);
                	
                	// <=- bees -=>
                    for(int i = 0; i < BEES; i++) {
                    	// age the arrays
                    	bee_x[i][2] = bee_x[i][1];
                    	bee_x[i][1] = bee_x[i][0];
                    	bee_y[i][2] = bee_y[i][1];
                    	bee_y[i][1] = bee_y[i][0];
                    	
                    	// accelerate
                    	dx = wasp_x[1] - bee_x[i][1];
                    	dy = wasp_y[1] - bee_y[i][1];
                    	distance = Math.abs(dx) + Math.abs(dy);	// approximation
                    	if(distance < 1) {
                    		distance = 1;
                    	}
                    	bee_xv[i] += (dx * BEE_ACC) / distance;
                		bee_yv[i] += (dy * BEE_ACC) / distance;
                	
                    	// speed limit checks
                    	if (bee_xv[i] > BEE_VEL) {
                    		bee_xv[i] = BEE_VEL;
                    	}
                    	if (bee_xv[i] < -BEE_VEL) {
                    		bee_xv[i] = -BEE_VEL;
                    	}
                    	if (bee_yv[i] > BEE_VEL) {
                    		bee_yv[i] = BEE_VEL;
                    	}
                    	if (bee_yv[i] < -BEE_VEL) {
                    		bee_yv[i] = -BEE_VEL;
                    	}
                    	
                    	// move
                    	bee_x[i][0] = bee_x[i][1] + bee_xv[i];
                    	bee_y[i][0] = bee_y[i][1] + bee_yv[i];
                    }
                    
                	// erase previous, draw current
                	canvas.drawColor(Color.BLACK);
                	                	
                    // wasp
                	canvas.drawLine(wasp_x[0], wasp_y[0], wasp_x[1], wasp_y[1], wasp_p);
                	
                	//bees
                	for(int i = 0; i < BEES; i++) {
                		canvas.drawLine(bee_x[i][1], bee_y[i][1], bee_x[i][2], bee_y[i][2], bee_p);
                		canvas.drawLine(bee_x[i][0], bee_y[i][0], bee_x[i][1], bee_y[i][1], bee_p);
                    }
                	
                    // all done
                    holder.unlockCanvasAndPost(canvas);
                    
                    // rest for a bit
                    try {
                        Thread.sleep(DELAY);
                    } catch (Exception e) {
                        e.getLocalizedMessage();
                    }
                }
            }
        }.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder sh, int format, int width, int height) {
        this.width = width;
        this.height = height;
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder sh) { }
    
    private int randPlusMinus(int r) {
        float fr = (float)r;
    	return (int)((Math.random() * fr) - (fr / 2F));
    }
}
