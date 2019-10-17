package com.example.project_4t_tople.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.project_4t_tople.R;
import com.example.project_4t_tople.customView.CustomCalendarView;
import com.example.project_4t_tople.model.Mypage;

public class CalendarFragment extends Fragment {
    // 객체 설정
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    //    캘린더 리스트 뷰 설정
    String user_id;
    Mypage item;
    CustomCalendarView customCalendarView;

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public CalendarFragment() {
        // Required empty public constructor
    }

    public static CalendarFragment newInstance(String param1, String param2) {
        CalendarFragment fragment = new CalendarFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        // intent와 bundle로 가져온 데이터
        if (getArguments() != null) {
            user_id = getArguments().getString("user_id");
            item = (Mypage) getArguments().getSerializable("item");
        }
        CustomCalendarView.CustomCalendar_date(item, user_id);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        이유는 모르지만 fragment에서는 View를 통해서 layout을 초기화 해줘야 한다.
        final View view = inflater.inflate(R.layout.fragment_calendar, container, false);
//        달력 객체 초기화
        customCalendarView = (CustomCalendarView) view.findViewById(R.id.custom_calendar_view);

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
