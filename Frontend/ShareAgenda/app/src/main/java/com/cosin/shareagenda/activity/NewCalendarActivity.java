package com.cosin.shareagenda.activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;

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
import com.cosin.shareagenda.model.Model;
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
    // intent extra parameter name
    public static final String CALENDAR_TARGET_ID = "CALENDAR_TARGET_ID";
    public static final String CALENDAR_ACTIVITY_TYPE = "CALENDAR_ACTIVITY_TYPE";
    public static final String CALENDAR_ACTIVITY_TITLE = "CALENDAR_ACTIVITY_TITLE";
    // intent extra parameter constant
    private static final String CALENDAR_ACTIVITY_TITLE_DEFAULT = "My Calendar";
    public static final String GROUP_CALENDAR = "GROUP_CALENDAR";
    public static final String FRIEND_CALENDAR = "FRIEND_CALENDAR";
    // intent extra parameter value
    private String calendarTargetId;
    private String calendarType;
    private String calendarTitle;

    private WeekView mWeekView;
    private int currentMonth = CalendarEventBiz.getCurrentMonth() + 1;
    private int currentYear = CalendarEventBiz.getCurrentYear();
    private List<WeekViewEvent> events = new ArrayList<>();
    private boolean skipAction = false;
    private String selectedDate;
    private String selectedTime;

    private void loadIntentExtra() {
        calendarTargetId = getIntent().getStringExtra(CALENDAR_TARGET_ID);
        calendarType = getIntent().getStringExtra(CALENDAR_ACTIVITY_TYPE);
        calendarTitle = getIntent().getStringExtra(CALENDAR_ACTIVITY_TITLE);
        if (calendarTitle == null) {
            calendarTitle = CALENDAR_ACTIVITY_TITLE_DEFAULT;
        }
    }

    @Override
    protected void initView() {
        loadIntentExtra();
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
        if (!onOtherCalendar()) {
            ApiClient.getEventMonthly(Model.model.getUser().getAccountId(), currentMonth, currentYear, new CallbackHandler(handler));
        } else if (calendarType.equals(GROUP_CALENDAR)) {
            for (String targetId : Model.model.getCurrentGroup().getMembers()) {
                ApiClient.getEventMonthly(targetId, currentMonth, currentYear, new CallbackHandler(handler));
            }
        } else if (calendarType.equals(FRIEND_CALENDAR)) {
            ApiClient.getEventMonthly(calendarTargetId, currentMonth, currentYear, new CallbackHandler(handler));
        }
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
                return ((hour + 11) % 12 + 1) // 0 => 12, 1 => 1, 24 => 12
                        + (hour < 12 ? ":00 AM" : ":00 PM");  // round down minutes
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
        Toast.makeText(this, "Event Long Press", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onEmptyViewLongPress(Calendar time) {
        DatePickerFragment newFragment = new DatePickerFragment();
        newFragment.withWeekView(mWeekView);
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        WeekView weekView;
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            int year = CalendarEventBiz.getCurrentYear();
            int month = CalendarEventBiz.getCurrentMonth();
            int day = CalendarEventBiz.getCurrentDayOfMonth();

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            Calendar date = Calendar.getInstance();
            date.set(year, month, day);
            weekView.goToDate(date);
        }

        public void withWeekView(WeekView mWeekView) {
            weekView = mWeekView;
        }
    }

    public WeekView getWeekView() {
        return mWeekView;
    }

    @Override
    public void onEmptyViewClicked(Calendar date) {
        selectedDate = CalendarEventBiz.calendarToDateString(startTime);
        selectedTime = CalendarEventBiz.calendarToTimeString(startTime);
        new SendEventRequestDialog(this, "My New Event", selectedDate, selectedTime).show();
        
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
        selectedDate = CalendarEventBiz.calendarToDateString(startTime);
        selectedTime = CalendarEventBiz.calendarToTimeString(startTime);
        new SendEventRequestDialog(this, "My New Event", selectedDate, selectedTime).show();
    }

    @Override
    public void onDrop(View view, Calendar date) {
        Toast.makeText(this, "View dropped to " + date.toString(), Toast.LENGTH_SHORT).show();
    }

    @Override
    protected String titleName() {
        return calendarTitle;
    }

    @Override
    public void receive(Object ret) {
        // to create user event by cal and quarter
        Intent intent = new Intent(this, CreateEventActivity.class);
        intent.putExtra(CreateEventActivity.SELECTED_DATE, selectedDate);
        intent.putExtra(CreateEventActivity.SELECTED_TIME, selectedTime);
        this.startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }

    public boolean onOtherCalendar() {
        return calendarTargetId != null;
    }
}