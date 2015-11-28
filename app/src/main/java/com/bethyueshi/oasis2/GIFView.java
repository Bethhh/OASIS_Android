package com.bethyueshi.oasis2;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Movie;
import android.util.AttributeSet;
import android.view.View;

import java.io.InputStream;

/**
 * Created by bethyueshi on 11/25/15.
 */

public class GIFView extends View {

    private Movie movie;
    private InputStream src = null;
    private long movieStart;
    public static final int HALF_GIF = 55;

    public GIFView(Context context) {
        super(context);

        src = context.getResources().openRawResource(+R.drawable.t1);
        movie = Movie.decodeStream(src);
    }

    public GIFView(Context context, AttributeSet attrs)
    {
        super(context, attrs);

        src = context.getResources().openRawResource(+R.drawable.t1);
        movie = Movie.decodeStream(src);
    }

    public GIFView(Context context, AttributeSet attrs, int default_style)
    {
        super(context, attrs, default_style);

        src = context.getResources().openRawResource(+R.drawable.t1);
        movie = Movie.decodeStream(src);
    }

    /**
     * Sets the gif source of the GIFView.
     * @param resId
     */
    public void setSrc(int resId){
        src = getResources().openRawResource(+resId);
        movie = Movie.decodeStream(src);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.WHITE);
        super.onDraw(canvas);

        long now = android.os.SystemClock.uptimeMillis();
        if (movieStart == 0) { // first time
            movieStart = now;
        }

        int relTime = (int) ((now - movieStart) % movie.duration());
        movie.setTime(relTime);

        float scaleX = (float)this.getWidth() / (float)movie.width();
        float scaleY = (float)this.getHeight() / (float)movie.height();

        canvas.scale(scaleX, scaleY);

        movie.draw(canvas, this.getWidth() / 2 - HALF_GIF, this.getHeight() / 2 - HALF_GIF);

        this.invalidate();
    }
}