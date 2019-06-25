package com.cosin.shareagenda.view;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.cosin.shareagenda.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * TODO: document your custom view class.
 */
public class WeekEventsView extends FriendsEventsView implements View.OnTouchListener {
    private Date firstDay;
    private String[] name;
    private TextPaint mTextPaint;
    private float mEvtTextHeight;
    private int LocationX;

    public WeekEventsView(Context context) {
        super(context);
        init(null, 0);
    }

    public WeekEventsView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public WeekEventsView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    public void setCld(Date firstDay, int quarter, boolean[] evt, String[] name) {
        this.firstDay = firstDay;
        this.quarter = quarter;
        this.evt = evt;
        this.name = name;
    }

    @Override
    protected void init(AttributeSet attrs, int defStyle) {
        super.init(attrs, defStyle);

        Resources resources = getResources();
        // Set up a default TextPaint object
        mTextPaint = new TextPaint();
        mTextPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        mTextPaint.setTextSize(resources.getDimension(R.dimen.week_event_text_size));
        mTextPaint.setColor(resources.getColor(R.color.txt_black));

        mEvtTextHeight = resources.getDimension(R.dimen.week_event_text_size);
        mEvtTextHeight = mEvtTextHeight + (eventHigh - mEvtTextHeight) / 2 - 6;

        setOnTouchListener(this);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (eventWidth == 0) {
            eventWidth = (getWidth() - timeWidth) / evt.length;
        }

        for (int i = 0; i < name.length; i++) {
            if (evt[i]) {
                canvas.drawText(
                        name[i],
                        timeWidth + i * eventWidth + eventWidth / 2,
                        mEvtTextHeight,
                        mTextPaint);
            }
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            LocationX = (int)event.getRawX();
        }
        return false;
    }

    @Override
    public boolean onLongClick(View view) {
        int w = (LocationX - timeWidth) / eventWidth;
        Calendar cal = Calendar.getInstance();
        cal.setTime(firstDay);
        cal.add(Calendar.DATE, w);
        SimpleDateFormat sdf = new SimpleDateFormat("d/M EEEE");
        Toast.makeText(getContext(), String.format("w= %s  t= %s",sdf.format(cal.getTime()),getTimeString()), Toast.LENGTH_SHORT).show();
        return true;
    }
}
