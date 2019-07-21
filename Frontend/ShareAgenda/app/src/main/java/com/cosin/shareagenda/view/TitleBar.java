package com.cosin.shareagenda.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.cosin.shareagenda.R;
import com.cosin.shareagenda.activity.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * TitleBar
 */
public class TitleBar extends Toolbar {
    private String titleText;
    private Drawable titleDrawable;

    // bind views
    @BindView(R.id.toolbar_left_button1)
    public ImageView leftButtonImage;
    @BindView(R.id.toolbar_title1)
    public TextView titleTextView;

    public TitleBar(Context context) {
        super(context);
        init(null, 0);
    }

    public TitleBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public TitleBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.title_bar,this,true);
        ButterKnife.bind(this, view);

        setContentInsetsRelative(0, 0);

        // Load attributes
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.TitleBar, defStyle, 0);

        titleText = a.getString(R.styleable.TitleBar_titleText);

        if (a.hasValue(R.styleable.TitleBar_titleDrawable)) {
            titleDrawable = a.getDrawable(R.styleable.TitleBar_titleDrawable);
            titleDrawable.setCallback(this);
        }

        a.recycle();
    }

    public void setContext(final BaseActivity context) {
        leftButtonImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.openDrawer();
            }
        });
    }

    public void showLeftImage() {
        if (leftButtonImage != null) {
            leftButtonImage.setVisibility(View.VISIBLE);
        }
    }

    public void hideLeftImage() {
        if (leftButtonImage != null) {
            leftButtonImage.setVisibility(View.GONE);
        }
    }

    public void hide() {
        this.setVisibility(View.GONE);
    }

    //
    public void setLetfButtonOnClickListerner(OnClickListener listerner) {
        if (leftButtonImage != null && listerner != null) {
            leftButtonImage.setOnClickListener(listerner);
        }
    }

    /**
     * Gets the example string attribute value.
     *
     * @return The example string attribute value.
     */
    public String getTileText() {
        return titleText;
    }

    /**
     * Sets the view's example string attribute value. In the example view, this string
     * is the text to draw.
     *
     * @param title The example string attribute value to use.
     */
    public void setTitleText(String title) {
        titleText = title;
        titleTextView.setText(title);
    }
}
