package com.cosin.shareagenda.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public abstract class BaseDialog extends Dialog {
    protected Context context;
    protected Window window = null;

    public BaseDialog(Context context) {
        super(context);
        this.context = context;
        window = getWindow();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        loadView();

        DisplayMetrics dm = this.getContext().getResources().getDisplayMetrics();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = (int) (dm.widthPixels * 0.8);

        initView();
    }

    protected abstract void loadView();

    protected abstract void initView();

    protected abstract Object dealwithRet();

    public View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ((DialogReceiver)context).receive(dealwithRet());
            dismiss();
        }
    };
}
