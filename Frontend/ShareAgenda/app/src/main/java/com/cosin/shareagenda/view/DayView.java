package com.cosin.shareagenda.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.PathEffect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ScrollView;

import java.util.ArrayList;

import types.Event;

public class DayView extends View implements View.OnTouchListener {

    private Paint p = new Paint();
    private Paint textp;
    private Paint roundRectP;
    private int parentWidth = 0;
    private int parentHeight = 0;
    private int X_OFFSET = 5;
    private int Y_OFFSET = 5;
    private int HOUR_BLOCK_HEIGHT = 60;
    private int font_max_width;
    private ScrollView _scrollView;
    private int least_time_in_hours = 24*60;//6 * 60
    private RectF _rects[];
    private int font_height;
    private Context context;
    private ArrayList<Event>events = new ArrayList<>();

    public void setEvent(ArrayList<Event>events) {
        this.events= events;
    }

    public DayView(Context context) {
        super(context);
        this.context = context;
        init();
    }
    public DayView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }
    public DayView(Context context, AttributeSet attrs,
                                 int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        init();
    }
    private void init(){
        //Creating Paint Objects
        //setting color
        //setting Fonts
        this.setOnTouchListener(this);
        calculateRectValues();
    }

    private void calculateRectValues() {
        // Calculating the Rects to draw in the paint this includes x,y points starting of the rect and width and height of the rectangle.
        int font_max_width = 200;
        for(int i=0;i<events.size();i++)
        _rects[i] = new RectF(font_max_width, convertTimetoSeconds(events.get(i).getStartTime()),
                font_max_width , convertTimetoSeconds(events.get(i).getEndTime()));

    }

    private int convertTimetoSeconds(String _startTime) {
        // TODO Auto-generated method stub
        int total = Integer.parseInt(_startTime.substring(0,_startTime.indexOf(":"))) * 60;
        total += Integer.parseInt(_startTime.substring(_startTime.indexOf(":")+1, _startTime.length()));
        return total;
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // TODO Auto-generated method stub
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        parentWidth = MeasureSpec.getSize(widthMeasureSpec);
        parentHeight = MeasureSpec.getSize(heightMeasureSpec);
        calculateRectValues();
        setMeasuredDimension(600,24 * HOUR_BLOCK_HEIGHT);
    }


    public void draw(Canvas canvas) {
        // TODO Auto-generated method stub
        super.draw(canvas);

        for(int i=0;i<25;i++)
        {
            String preString = "";
            String poststring = "";

            if(i == 12)
            {
                preString = "Noon";
                poststring = "";

            }
            else if(i%12 == 0)
            {
                preString = "12";
                poststring = " AM";
            }
            else if(i<12)
            {
                preString = i+"";
                poststring = " AM";
            }
            else
            {
                preString = i%12+"";
                poststring = " PM";
            }
            canvas.drawText(preString, X_OFFSET+3, i * HOUR_BLOCK_HEIGHT + font_height, p);
            canvas.drawText(poststring, X_OFFSET+p.measureText(preString), i * HOUR_BLOCK_HEIGHT + font_height, p);
            p.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
            p.setColor(Color.parseColor("#cbcaca"));
            p.setStrokeWidth(0.2f);
            p.setPathEffect(new DashPathEffect(new float[] {1,2}, 0));
            canvas.drawLine(font_max_width, i * (HOUR_BLOCK_HEIGHT)+ font_height/2+HOUR_BLOCK_HEIGHT/2, parentWidth-8, i * (HOUR_BLOCK_HEIGHT)+ font_height/2+HOUR_BLOCK_HEIGHT/2, p);
            p.setColor(Color.parseColor("#f1f1f1"));
            p.setPathEffect(new PathEffect());
            p.setStrokeWidth(0.2f);
            canvas.drawLine(font_max_width, i * HOUR_BLOCK_HEIGHT+ font_height/2, parentWidth-8, i * HOUR_BLOCK_HEIGHT+ font_height/2, p);

        }
        for(int j=0;j<events.size();j++)
        {
            canvas.drawRoundRect(_rects[j], 3, 3, roundRectP);
            canvas.drawText(events.get(j).getEventname(), _rects[j].left+X_OFFSET,_rects[j].top + font_height + Y_OFFSET, textp);
        }
    }


    public boolean onTouch(View v, MotionEvent event) {
        // TODO Auto-generated method stub
        int x = (int) event.getX();
        int y = (int) event.getY();
        for(int j=0;j<events.size();j++)
        {
            if(_rects[j].contains(x, y))
            {
                //Selected J appointmrnt
            }
        }
        return true;
    }
}

