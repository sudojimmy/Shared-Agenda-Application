package com.dg.eventapp.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.dg.eventapp.entity.EventEntity;

import java.util.Locale;


public class EvtView extends View implements View.OnClickListener {
    private EventEntity event;
    private Paint paint;
    private int height;
    private final int unit = 72;

    private int[] clrBar = new int[5];
    private int clrEventText;
    private int clrTimeText;

    public EvtView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public void setEvent(EventEntity event){
        this.event = event;
        height = event.getCount() * unit;
    }

    private void init(){
        paint = new Paint();
        clrBar[0] = Color.rgb(230,244,244);
        clrBar[2] = Color.rgb(237,237,237);
        clrBar[4] = Color.rgb(186,217,211);
        clrBar[1] = 0xffffffff;
        clrBar[3] = 0xffffffff;

        clrEventText = Color.rgb(48,128,127);
        clrTimeText = Color.rgb(65,65,65);
    }
    @Override
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);

        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        if(event.getEventType() == 0){
            if(clrBar[event.getMm()]!=0xffffffff) {
                paint.setColor(clrBar[event.getMm()]);
                canvas.drawRect(0,0,getRight(), height, paint);
            }

            paint.setColor(clrTimeText);
            paint.setTextSize(36);
            canvas.drawText(event.getTime(),8,36, paint);
        }else{
            final int textWidth = 200;
            for(int i = 0; i < event.getCount(); i++){
                int hh = (event.getQuarter()+i) / 4;
                int mm = (event.getQuarter()+i) % 4;

                if(clrBar[mm] != 0xffffffff){
                    paint.setColor(clrBar[mm]);
                    canvas.drawRect(0, i*unit, textWidth, (i+1)*unit, paint);
                }

                paint.setColor(clrTimeText);
                paint.setTextSize(36);
                canvas.drawText(String.format(Locale.ENGLISH,"%2d:%02d", hh, mm * 15),8,36 + i * unit, paint);
            }

            paint.setColor(clrBar[4]);
            canvas.drawRect(textWidth,4, getRight(), height - 5, paint);

            paint.setColor(clrEventText);
            paint.setTextSize(44);
            canvas.drawText(event.getEventName(), 300, height/2 + 16, paint);
        }

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width = MeasureSpec.getSize(widthMeasureSpec);
        setMeasuredDimension(width,height);
    }

    @Override
    public void onClick(View v) {

    }
}
