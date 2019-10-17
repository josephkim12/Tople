package com.example.project_4t_tople.customView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.project_4t_tople.R;
import com.example.project_4t_tople.Response.CalendarResponse;
import com.example.project_4t_tople.activity.Calendar_ReadActivity;
import com.example.project_4t_tople.activity.Calendar_WriteActivity;
import com.example.project_4t_tople.adapter.CalendarAdapter;
import com.example.project_4t_tople.adapter.MyGridAdapter;
import com.example.project_4t_tople.model.Calendar;
import com.example.project_4t_tople.model.Events;
import com.example.project_4t_tople.model.Mypage;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CustomCalendarView extends LinearLayout {
    private static Mypage mypage_item;
    private static String user_id;

    ImageButton nextButton, prevButton;
    TextView CurrentDate;
    GridView gridView;
    String currwntDate;
    private static final int MAX_CALENDAR_DAYS = 42;
    java.util.Calendar calendar = java.util.Calendar.getInstance(Locale.KOREAN);
    Context context;
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM");
    SimpleDateFormat dayFormat = new SimpleDateFormat("dd", Locale.KOREAN);
    SimpleDateFormat monthFormat = new SimpleDateFormat("MM", Locale.KOREAN);
    SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy", Locale.KOREAN);
    SimpleDateFormat eventDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREAN);

    MyGridAdapter myGridAdapter;

    AsyncHttpClient client;
    List<Calendar> calendar_List;
    ListView calendar_listView;
    CalendarAdapter calendarAdapter;
    CalendarResponse calendarResponse;
    Calendar calendar_item;
    String year, month, str_year, str_month;

    //    AlertDialog alertDialog;
    List<Date> dates = new ArrayList<>();
    List<Events> eventsList = new ArrayList<>();

    //    fragment에서 값을 전달 받아서 item값 가져오기
    public static void CustomCalendar_date(Mypage custom_item, String custom_user_id) {
        mypage_item = custom_item;
        user_id = custom_user_id;
    }

    public CustomCalendarView(final Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        IntializeLayout();
        setUpCalendar();

        prevButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar.add(java.util.Calendar.MONTH, -1);
                setUpCalendar();

                str_year = yearFormat.format(calendar.getTime());
                str_month = monthFormat.format(calendar.getTime());

                getCalendar_list();
            }
        });
        nextButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar.add(java.util.Calendar.MONTH, 1);
                setUpCalendar();
                str_year = yearFormat.format(calendar.getTime());
                str_month = monthFormat.format(calendar.getTime());

                getCalendar_list();
            }
        });

        // 달력 이벤트 처리
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final View detailDialog = LayoutInflater.from(parent.getContext()).inflate(R.layout.calendar_dialog, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setCancelable(true);
                builder.setView(detailDialog);
//                달력은 6 x 7로써 배열에 들어가 있다 그러므로 position을 통해서 0 ~ 41까지 하는데 이를 요일을 알아내는 방법
                int week = (position % 7) + 1;
                // 요일을 뿌려주기 위해서 사용
                String dayOfWeek = "";
                switch (week) {
                    case 1:
                        dayOfWeek = "일요일";
                        break;
                    case 2:
                        dayOfWeek = "월요일";
                        break;
                    case 3:
                        dayOfWeek = "화요일";
                        break;
                    case 4:
                        dayOfWeek = "수요일";
                        break;
                    case 5:
                        dayOfWeek = "목요일";
                        break;
                    case 6:
                        dayOfWeek = "금요일";
                        break;
                    case 7:
                        dayOfWeek = "토요일";
                        break;
                }
                final String date = eventDateFormat.format(dates.get(position));
                final String month = monthFormat.format(dates.get(position));
                final String year = yearFormat.format(dates.get(position));
                final String dayOfMonth = dayFormat.format(dates.get(position));

                // 날짜 객체 초기화
                FloatingActionButton Detail_add = detailDialog.findViewById(R.id.Detail_add);
                TextView Detail_day = detailDialog.findViewById(R.id.Detail_day);
                TextView Detail_year = detailDialog.findViewById(R.id.Detail_year);
                TextView Detail_month = detailDialog.findViewById(R.id.Detail_month);
                TextView Detail_week = detailDialog.findViewById(R.id.Detail_week);

                // 날짜 객체 설정
                Detail_day.setText(dayOfMonth + "일");
                Detail_year.setText(year + "년");
                Detail_month.setText(month + "월");
                Detail_week.setText(dayOfWeek);

                Detail_add.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // 화면 전환
                        Intent intent = new Intent(context, Calendar_WriteActivity.class);
                        intent.putExtra("moimcode", mypage_item.getMoimcode());
                        intent.putExtra("year", year);
                        intent.putExtra("month", month);
                        intent.putExtra("day", dayOfMonth);
                        context.startActivity(intent);
                    }
                });
                builder.show();
            }
        });

        calendar_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                calendar_item = calendarAdapter.getItem(position);

                Intent intent = new Intent(context, Calendar_ReadActivity.class);
                intent.putExtra("calendar_item", calendar_item);
                intent.putExtra("mypage_item", mypage_item);
                intent.putExtra("user_id", user_id);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                context.startActivity(intent);
            }
        });
    }


    // custom에서 초기화를 여기서 해줘야 한다.
    private void IntializeLayout() {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.calendar_layout, this);
        nextButton = view.findViewById(R.id.nextBtn);
        prevButton = view.findViewById(R.id.previousBtn);
        CurrentDate = view.findViewById(R.id.current_Date);
        gridView = view.findViewById(R.id.gridview);
        client = new AsyncHttpClient();
        calendar_List = new ArrayList<>();
        calendar_listView = findViewById(R.id.Calendar_listView);
        calendarAdapter = new CalendarAdapter(context, R.layout.calendar_item, calendar_List);

        calendar_listView.setAdapter(calendarAdapter);

        calendarResponse = new CalendarResponse(calendarAdapter);

        getCalendar_list();
    }

    // 달력 년 월을 보여주는 toolbar 기능을 하는 곳에 대한 설정
    private void setUpCalendar() {
        // 달력 중앙 년월 초기화
        currwntDate = dateFormat.format(calendar.getTime());
        // 달력 중앙 년월 작성
        CurrentDate.setText(currwntDate);
        // 달력 일자 초기화
        dates.clear();
        java.util.Calendar monthCalendar = (java.util.Calendar) calendar.clone();
        monthCalendar.set(java.util.Calendar.DAY_OF_MONTH, 1);
        int FirstDayofMonth = monthCalendar.get(java.util.Calendar.DAY_OF_WEEK) - 1;
        monthCalendar.add(java.util.Calendar.DAY_OF_MONTH, -FirstDayofMonth);

        while (dates.size() < MAX_CALENDAR_DAYS) {
            dates.add(monthCalendar.getTime());
            monthCalendar.add(java.util.Calendar.DAY_OF_MONTH, 1);
        }
        myGridAdapter = new MyGridAdapter(context, dates, calendar, eventsList);
        gridView.setAdapter(myGridAdapter);
    }

    private void getCalendar_list() {
        if(!calendarAdapter.isEmpty()){
            calendarAdapter.clear();
        }
        calendarDate();
        if ((str_year == null) || (str_month == null)) {
            String URL = "http://175.198.87.149:8080/moim.4t.spring/selectScheduleMonth.tople";
            RequestParams params = new RequestParams();
            params.put("sch_moimcode", mypage_item.getMoimcode());
            params.put("sch_year", year);
            params.put("sch_month", month);
            client.post(URL, params, calendarResponse);
        } else {
            String URL = "http://175.198.87.149:8080/moim.4t.spring/selectScheduleMonth.tople";
            RequestParams params = new RequestParams();
            params.put("sch_moimcode", mypage_item.getMoimcode());
            params.put("sch_year", str_year);
            params.put("sch_month", str_month);
            client.post(URL, params, calendarResponse);
        }
        calendarAdapter.notifyDataSetChanged();
    }

    private void calendarDate() {
        // 시스템의 오늘 날짜 년, 월 구하기
        Date TodayDate = java.util.Calendar.getInstance().getTime();
        SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy", Locale.getDefault());
        SimpleDateFormat monthFormat = new SimpleDateFormat("MM", Locale.getDefault());

        year = yearFormat.format(TodayDate);
        month = monthFormat.format(TodayDate);
    }
}
