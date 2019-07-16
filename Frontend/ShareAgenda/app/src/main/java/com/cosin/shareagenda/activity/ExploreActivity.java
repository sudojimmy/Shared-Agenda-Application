package com.cosin.shareagenda.activity;

import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cosin.shareagenda.R;
import com.cosin.shareagenda.access.net.CallbackHandler;
import com.cosin.shareagenda.adapter.ExploreEventsAdapter;
import com.cosin.shareagenda.api.ApiClient;
import com.cosin.shareagenda.api.ApiErrorResponse;
import com.google.gson.Gson;

import java.util.ArrayList;

import types.Event;
import types.ExploreEventResponse;

import static com.cosin.shareagenda.access.net.CallbackHandler.HTTP_FAILURE;
import static com.cosin.shareagenda.access.net.CallbackHandler.SUCCESS;

public class ExploreActivity extends MainTitleActivity {

    ArrayList<Event> events;
    private SearchView searchBar;
    private ExploreEventsAdapter eventAdapter;

    @Override
    protected int getContentView() {
        return R.layout.activity_explore;
    }

    @Override
    protected void loadData() {}

    @Override
    protected void initView() {
        super.initView();

        RecyclerView rvEvent = findViewById(R.id.rvEvents);
        rvEvent.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        rvEvent.setLayoutManager(layoutManager);
        eventAdapter = new ExploreEventsAdapter();
        rvEvent.setAdapter(eventAdapter);
        searchBar = findViewById(R.id.search);
        searchBar.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ExploreActivity.this, "CLICK", Toast.LENGTH_SHORT).show();
            }
        });
        searchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                ApiClient.exploreEvent(s, new CallbackHandler(handler));
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
    }

    @Override
    protected String titleName() {
        return "Explore Event";
    }

    Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(android.os.Message message) {
            final Gson gson = new Gson();
            switch (message.what) {
                case SUCCESS:
                    String body = (String) message.obj;
                    ExploreEventResponse resp = gson.fromJson(body, ExploreEventResponse.class);
                    eventAdapter.setEventList(resp.getEventList());
                    break;
                case HTTP_FAILURE:
                    ApiErrorResponse errorResponse = gson.fromJson((String) message.obj, ApiErrorResponse.class);
                    Toast.makeText(ExploreActivity.this, errorResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    break;
                default:
                    Toast.makeText(ExploreActivity.this, (String) message.obj, Toast.LENGTH_SHORT).show();
            }
        }
    };
}
