package org.coax.daydream.swarm;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.preference.PreferenceManager;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class SwarmView extends SurfaceView implements SurfaceHolder.Callback {

    private int width;
    private int height;
    private Canvas canvas;
    private SurfaceHolder holder;
    private SharedPreferences sharedpreferences;

    private int BEES;           // number of bees
    private int REPEAT;         // number of time positions recorded
    private int BEE_ACC;        // maximum acceleration of bees
    private int WASP_ACC;       // maximum acceleration of wasp
    private int BEE_VEL;        // maximum bee velocity
    private int WASP_VEL;       // maximum wasp velocity
    private int BORDER;         // wasp won't go closer than this to the edges
    private int DELAY;          // millisecond delay between updates
    private int LWIDTH;         // line stroke width
    private boolean AALIAS;     // use anti-aliasing
    private String WASP_COLOR;  // wasp color
    private String BEE_COLOR;   // bee color
    private String BACK_COLOR;  // background color

    int[] wasp_x;
    int[] wasp_y;
    int wasp_xv;
    int wasp_yv;

    int[][] bee_x;
    int[][] bee_y;
    int[] bee_xv;
    int[] bee_yv;

    Paint wasp_p, bee_p;

    int dx, dy, distance;

    public SwarmView(Context context) {
        super(context);

        sharedpreferences = PreferenceManager.getDefaultSharedPreferences(context);
        holder = getHolder();
        holder.addCallback(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder sh) {

    	new Thread() {
            @Override
            public void run() {

                canvas = null;

                BEES       = Integer.parseInt(sharedpreferences.getString("BEES", "20"));
                // add one here since the preference is for number of segments, not points
                REPEAT     = Integer.parseInt(sharedpreferences.getString("TRAIL", "2")) + 1;
                BEE_ACC    = Integer.parseInt(sharedpreferences.getString("BEE_ACC", "3"));
                WASP_ACC   = Integer.parseInt(sharedpreferences.getString("WASP_ACC", "5"));
                BEE_VEL    = Integer.parseInt(sharedpreferences.getString("BEE_VEL", "11"));
                WASP_VEL   = Integer.parseInt(sharedpreferences.getString("WASP_VEL", "12"));
                BORDER     = Integer.parseInt(sharedpreferences.getString("BORDER", "50"));
                DELAY      = Integer.parseInt(sharedpreferences.getString("DELAY", "40"));
                LWIDTH     = Integer.parseInt(sharedpreferences.getString("LWIDTH", "1"));
                AALIAS     = sharedpreferences.getBoolean("AALIAS", false);
                WASP_COLOR = sharedpreferences.getString("WASP_COLOR", "Red");
                BEE_COLOR  = sharedpreferences.getString("BEE_COLOR", "Yellow");
                BACK_COLOR = sharedpreferences.getString("BACK_COLOR", "Black");

                // initialize wasp
                wasp_x = new int[2];
                wasp_y = new int[2];
                wasp_x[0] = (int)(Math.random() * (width - 2 * BORDER)) + BORDER;
                wasp_y[0] = (int)(Math.random() * (height - 2 * BORDER)) + BORDER;
                wasp_x[1] = wasp_x[0];
                wasp_y[1] = wasp_y[0];

                wasp_xv = 0;
                wasp_yv = 0;

                wasp_p = new Paint();
                wasp_p.setColor(Color.parseColor(WASP_COLOR));
                wasp_p.setStrokeCap(Paint.Cap.ROUND);
                wasp_p.setStrokeWidth(LWIDTH);
                wasp_p.setAntiAlias(AALIAS);
                
                // initialize bees
                int[][] bee_x = new int[BEES][REPEAT];
                int[][] bee_y = new int[BEES][REPEAT];
                int[] bee_xv  = new int[BEES];
                int[] bee_yv  = new int[BEES];
                for(int i = 0; i < BEES; i++) {
                    bee_x[i][0] = (int)(Math.random() * width);
                    bee_y[i][0] = (int)(Math.random() * height);

                    for(int j = 1; j < REPEAT; j++) {
                        bee_x[i][j] = bee_x[i][j-1];
                        bee_y[i][j] = bee_y[i][j-1];
                    }

                    bee_xv[i] = randPlusMinus(7);
                    bee_yv[i] = randPlusMinus(7);
                }

                bee_p = new Paint();
                bee_p.setColor(Color.parseColor(BEE_COLOR));
                bee_p.setStrokeCap(Paint.Cap.ROUND);
                bee_p.setStrokeWidth(LWIDTH);
                bee_p.setAntiAlias(AALIAS);

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
                        for(int j = REPEAT - 1; j > 0; j--) {
                            bee_x[i][j] = bee_x[i][j-1];
                            bee_y[i][j] = bee_y[i][j-1];
                        }

                        // accelerate
                        dx = wasp_x[1] - bee_x[i][1];
                        dy = wasp_y[1] - bee_y[i][1];
                        distance = Math.abs(dx) + Math.abs(dy);  // approximation
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
                    canvas.drawColor(Color.parseColor(BACK_COLOR));

                    //bees
                    for(int i = 0; i < BEES; i++) {
                        for(int j = REPEAT - 1; j > 0; j--) {
                            canvas.drawLine(bee_x[i][j-1], bee_y[i][j-1], bee_x[i][j], bee_y[i][j], bee_p);
                        }
                    }

                    // wasp, draw last so it isn't obscured by bees
                    canvas.drawLine(wasp_x[0], wasp_y[0], wasp_x[1], wasp_y[1], wasp_p);

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
         // end of range = ±ceiling(r/2)
         // truncated distribution: zero on end of range, double on zero
         //return (int)((Math.random() * r) - (r / 2D));
         // rounded distribution: half on end of range, normal on zero
         return (int)Math.round((Math.random() * r) - (r / 2D));
    }
}
