package com.cosin.shareagenda.view;

import android.util.Log;
import android.view.View;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.Toast;

import com.cosin.shareagenda.R;
import com.cosin.shareagenda.entity.EventEntity;

import java.util.Locale;

public class EventView extends View implements View.OnClickListener {
    private EventEntity event;
    private Paint paint;
    private int mUnit;
    private int mHigh;
    private int[] clrBar = new int[7];
    private int txtTime;
    private int txtEvnt;
    private int timeWidth;
    private int evtName;
    private int leftMargin;
    private ItemViewListener listener;

    public EventView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public EventView(Context context) {
        super(context);
        init();
    }

    public void setEvent(EventEntity event) {
        this.event = event;
        mHigh = mUnit * event.getCount();
    }

    public void setListener(ItemViewListener listener) {
        this.listener = listener;
    }

    private void init() {
        Resources resources = getContext().getResources();
        clrBar[0] = resources.getColor(R.color.evtBar0);
        clrBar[2] = resources.getColor(R.color.evtBar2);
        clrBar[4] = resources.getColor(R.color.evtBlock);
        clrBar[5] = resources.getColor(R.color.evtName);
        clrBar[6] = resources.getColor(R.color.evtTime);
        clrBar[1] = -1;
        clrBar[3] = -1;
        mUnit = resources.getDimensionPixelOffset(R.dimen.event_line_height);
        txtTime = resources.getDimensionPixelOffset(R.dimen.event_text_time_size);
        txtEvnt = resources.getDimensionPixelOffset(R.dimen.event_text_size);
        timeWidth = mUnit * 3;
        evtName = mUnit * 5;
        leftMargin = resources.getDimensionPixelSize(R.dimen.event_left_margin);

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);

        setOnClickListener(this);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        paint.setStyle(Paint.Style.FILL_AND_STROKE);

        if (event.getType() != 0) {

            // time bar
            for (int i = 0; i < event.getCount(); i++) {
                int hh = (event.getQuarter() + i) / 4;
                int mm = (event.getQuarter() + i) % 4;

                if (clrBar[mm] != -1) {
                    paint.setColor(clrBar[mm]);
                    canvas.drawRect(0,i * mUnit,getRight(),(i + 1) * mUnit - 1, paint);
                }
                // time indicator
                paint.setColor(clrBar[6]);
                paint.setTextSize(txtTime);
                canvas.drawText(String.format(Locale.ENGLISH,"%2d:%02d",hh, mm * 15),
                        leftMargin,
                        txtTime + i * mUnit,
                        paint);
            }

            // event block
            paint.setColor(clrBar[4]);
            canvas.drawRect(timeWidth,4, getRight(), mHigh - 5, paint);
            // event name
            paint.setColor(clrBar[5]);
            paint.setTextSize(txtEvnt);
            canvas.drawText(event.getName(), evtName, (mHigh + txtEvnt) / 2 - 6, paint);
        }
        else {
            // empty event bar
            if (clrBar[event.getM()] != -1) {
                paint.setColor(clrBar[event.getM()]);
                canvas.drawRect(0,0, getRight(), mUnit - 1, paint);
            }

            // time indicator
            paint.setColor(clrBar[6]);
            paint.setTextSize(txtTime);
            canvas.drawText(event.getTime(), leftMargin, txtTime, paint);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = measureWidth(widthMeasureSpec);
        int height = measureHeight(heightMeasureSpec);
        setMeasuredDimension(width, height);
    }

    private int measureWidth(int widthMeasureSpec) {
        return MeasureSpec.getSize(widthMeasureSpec);
    }

    private int measureHeight(int heightMeasureSpec) {
        //int mode = MeasureSpec.getMode(heightMeasureSpec);
        //int size = MeasureSpec.getSize(heightMeasureSpec);

        return getPaddingTop() + mHigh + getPaddingBottom();
    }

    @Override
    public void onClick(View view) {
        if (event.getType() == 0) {
            listener.dealwithItem(event.getQuarter());
        } else {
            Toast.makeText(getContext(), event.getLocation(), Toast.LENGTH_SHORT).show();
        }
    }

}
