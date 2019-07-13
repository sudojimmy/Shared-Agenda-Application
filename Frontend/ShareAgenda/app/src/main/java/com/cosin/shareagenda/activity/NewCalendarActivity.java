package com.cosin.shareagenda.activity;

import android.content.Intent;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Toast;

import com.alamkanak.weekview.DateTimeInterpreter;
import com.alamkanak.weekview.MonthLoader;
import com.alamkanak.weekview.WeekView;
import com.alamkanak.weekview.WeekViewEvent;
import com.cosin.shareagenda.R;
import com.cosin.shareagenda.access.net.CallbackHandler;
import com.cosin.shareagenda.dialog.DialogReceiver;
import com.cosin.shareagenda.dialog.SendEventRequestDialog;
import com.cosin.shareagenda.entity.DisplayableEvent;
import com.cosin.shareagenda.model.ApiClient;
import com.cosin.shareagenda.model.ApiErrorResponse;
import com.cosin.shareagenda.util.CalendarEventBiz;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import types.Event;
import types.GetEventMonthlyResponse;

import static com.cosin.shareagenda.access.net.CallbackHandler.HTTP_FAILURE;
import static com.cosin.shareagenda.access.net.CallbackHandler.SUCCESS;

public class NewCalendarActivity extends MainTitleActivity implements WeekView.EventClickListener,
        MonthLoader.MonthChangeListener, WeekView.EventLongPressListener, WeekView.EmptyViewLongPressListener,
        WeekView.EmptyViewClickListener, WeekView.AddEventClickListener, WeekView.DropListener, DialogReceiver {
    public static final String OWNER_ACCOUNT_ID = "OWNER_ACCOUNT_ID";
    private static final String CALENDAR_ACTIVITY_TITLE = "My Calendar";
    private String ownerAccountId;
    private WeekView mWeekView;
    private int currentMonth = CalendarEventBiz.getCurrentMonth() + 1;
    private int currentYear = CalendarEventBiz.getCurrentYear();
    private List<WeekViewEvent> events = new ArrayList<>();
    private boolean skipAction = false;

    @Override
    protected void initView() {
        ownerAccountId = getIntent().getStringExtra(OWNER_ACCOUNT_ID);
        super.initView();

        mWeekView = findViewById(R.id.weekView);

        mWeekView.setOnEventClickListener(this);

        mWeekView.setMonthChangeListener(this);

        mWeekView.setEventLongPressListener(this);

        mWeekView.setEmptyViewLongPressListener(this);

        mWeekView.setEmptyViewClickListener(this);

        mWeekView.setAddEventClickListener(this);

        mWeekView.setDropListener(this);

        setupDateTimeInterpreter(false);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_calendar_new;
    }

    @Override
    protected void loadData() {
        ApiClient.getEventMonthly(ownerAccountId, currentMonth, currentYear, new CallbackHandler(handler));
    }

    Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(android.os.Message message) {
            final Gson gson = new Gson();
            switch (message.what) {
                case SUCCESS:
                    String body = (String) message.obj;
                    GetEventMonthlyResponse resp = gson.fromJson(body, GetEventMonthlyResponse.class);
                    events.clear();
                    for (Event e : resp.getEventList()) {
                        DisplayableEvent de = new DisplayableEvent(e);
                        events.add(de);
                    }
                    skipAction = true;
                    mWeekView.notifyDatasetChanged();
                    break;
                case HTTP_FAILURE:
                    ApiErrorResponse errorResponse = gson.fromJson((String) message.obj, ApiErrorResponse.class);
                    Toast.makeText(NewCalendarActivity.this, errorResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    break;
                default:
                    Toast.makeText(NewCalendarActivity.this, (String) message.obj, Toast.LENGTH_SHORT).show();
            }
        }
    };

    /**
     * Set up a date time interpreter which will show short date values when in week view and long
     * date values otherwise.
     *
     * @param shortDate True if the date values should be short.
     */
    private void setupDateTimeInterpreter(final boolean shortDate) {
        mWeekView.setDateTimeInterpreter(new DateTimeInterpreter() {
            @Override
            public String interpretDate(Calendar date) {
                SimpleDateFormat weekdayNameFormat = new SimpleDateFormat("EEE", Locale.getDefault());
                String weekday = weekdayNameFormat.format(date.getTime());
                SimpleDateFormat format = new SimpleDateFormat(" M/d", Locale.getDefault());

                if (shortDate)
                    weekday = String.valueOf(weekday.charAt(0));
                return weekday.toUpperCase() + format.format(date.getTime());
            }

            @Override
            public String interpretTime(int hour, int minutes) {
                String strMinutes = String.format("%02d", minutes);
                if (hour > 11) {
                    return (hour - 12) + ":" + strMinutes + " PM";
                } else {
                    if (hour == 0) {
                        return "12:" + strMinutes + " AM";
                    } else {
                        return hour + ":" + strMinutes + " AM";
                    }
                }
            }
        });
    }

    protected String getEventTitle(Calendar time) {
        return String.format("Event of %02d:%02d %s/%d", time.get(Calendar.HOUR_OF_DAY), time.get(Calendar.MINUTE), time.get(Calendar.MONTH) + 1, time.get(Calendar.DAY_OF_MONTH));
    }

    @Override
    public void onEventClick(WeekViewEvent event, RectF eventRect) {
        Toast.makeText(this, "Clicked " + event.getName(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onEventLongPress(WeekViewEvent event, RectF eventRect) {
        loadData();
    }

    @Override
    public void onEmptyViewLongPress(Calendar time) {
        mWeekView.notifyDatasetChanged();
    }

    public WeekView getWeekView() {
        return mWeekView;
    }

    @Override
    public void onEmptyViewClicked(Calendar date) {
        Toast.makeText(this, "Empty view" + " clicked: " + getEventTitle(date), Toast.LENGTH_SHORT).show();
    }

    @Override
    public List<? extends WeekViewEvent> onMonthChange(int newYear, int newMonth) {
        if (skipAction) {
            skipAction = false;
            return events;
        }
        currentMonth = newMonth;
        currentYear = newYear;
        loadData();
        return events;
    }

    @Override
    public void onAddEventClicked(Calendar startTime, Calendar endTime) {
        new SendEventRequestDialog(this,
                "My New Event",
                CalendarEventBiz.calendarToDateString(startTime),
                CalendarEventBiz.calendarToTimeString(startTime)).show();
    }

    @Override
    public void onDrop(View view, Calendar date) {
        Toast.makeText(this, "View dropped to " + date.toString(), Toast.LENGTH_SHORT).show();
    }

    @Override
    protected String titleName() {
        return ownerAccountId == null ? CALENDAR_ACTIVITY_TITLE : ownerAccountId;
    }

    @Override
    public void receive(Object ret) {
        // to create user event by cal and quarter
        Intent intent = new Intent(this, CreateEventActivity.class);
        this.startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }

    public boolean isFriendCalendar() {
        return ownerAccountId != null;
    }
}