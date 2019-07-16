package com.cosin.shareagenda.access.net;

import android.os.Handler;
import android.os.Message;

import com.cosin.shareagenda.api.ApiErrorResponse;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class CallbackHandler implements Callback {
    private Handler handler;
    public static final int SUCCESS = 0;
    public static final int EXECUTE_FAILURE = -1;
    public static final int HTTP_FAILURE = -2;

    public CallbackHandler(final Handler handler) {
        this.handler = handler;
    }
    @Override
    public void onFailure(Call call, IOException e) {
        Message msg = handler.obtainMessage(EXECUTE_FAILURE);
        msg.obj = e.getMessage();
        handler.sendMessage(msg);
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        Message msg = handler.obtainMessage(response.isSuccessful() ? SUCCESS : HTTP_FAILURE);
        String body = response.body().string();
        if (body.equals("")) {
            final Gson gson = new Gson();
            // TODO currently 403 will cause Null Ptr Exception. Backend need to add msg to 403 response.
            msg.obj = gson.toJson(new ApiErrorResponse(response.code(), "Unknown Error"));
        } else {
            msg.obj = body;
        }
        handler.sendMessage(msg);
    }
}