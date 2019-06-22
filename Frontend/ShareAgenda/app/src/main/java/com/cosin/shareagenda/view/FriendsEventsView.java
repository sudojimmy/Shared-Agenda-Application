package com.cosin.shareagenda.view;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.cosin.shareagenda.R;

import java.util.Locale;

/**
 * TODO: document your custom view class.
 */
public class FriendsEventsView extends View implements View.OnLongClickListener {
    private int quarter;
    private boolean[] evt;

    private int timeWidth;
    private int eventWidth = 0;
    private int eventHigh;

    //private TextPaint mTextPaint;
    private Paint paint;
    private float mTextHeight;
    private int clrText;
    private int clrRect;
    private int clrLine;

    public FriendsEventsView(Context context) {
        super(context);
        init(null, 0);
    }

    public FriendsEventsView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public FriendsEventsView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    public void setCld(int quarter, boolean[] evt) {
        this.quarter = quarter;
        this.evt = evt;
    }

    private void init(AttributeSet attrs, int defStyle) {
        Resources resources = getContext().getResources();
        timeWidth = (int)resources.getDimension(R.dimen.friends_events_time);
        eventHigh = (int)resources.getDimension(R.dimen.friends_events_height);
        mTextHeight = (int)resources.getDimension(R.dimen.friends_events_text_height);

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setTextAlign(Paint.Align.LEFT);
        paint.setTextSize(mTextHeight);
        paint.setStrokeWidth(2);

        clrText = resources.getColor(R.color.evtTime);
        clrRect = resources.getColor(R.color.evtBlock);
        clrLine = resources.getColor(R.color.evtLine);

        setOnLongClickListener(this);
    }

    private String getTimeString() {
        int h = quarter / 4;
        int m = quarter % 4;
        return String.format(Locale.ENGLISH, "%2d:%02d", h, m * 15);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (eventWidth == 0) {
            eventWidth = (getWidth() - timeWidth) / evt.length;
        }
        // TODO: consider storing these as member variables to reduce
        // allocations per draw cycle.

        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();

        paint.setColor(clrLine);
        canvas.drawLine(0, 0, getRight(), 0, paint);

        paint.setColor(clrText);
        canvas.drawText(getTimeString(),
                paddingLeft + 4,
                paddingTop + mTextHeight + 4,
                paint);

        paint.setColor(clrRect);
        for (int i = 0; i < evt.length; i++){
            if (evt[i]) {
                canvas.drawRect(
                        timeWidth + i * eventWidth,
                        0,
                        timeWidth + (i + 1) * eventWidth,
                        eventHigh,
                        paint);
            }
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

        return getPaddingTop() + eventHigh + getPaddingBottom();
    }

    @Override
    public boolean onLongClick(View view) {
        Toast.makeText(getContext(), getTimeString(), Toast.LENGTH_SHORT).show();
        return true;
    }
}
