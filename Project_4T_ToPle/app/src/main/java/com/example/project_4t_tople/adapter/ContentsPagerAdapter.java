package com.example.project_4t_tople.adapter;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.project_4t_tople.fragment.BoardFragment;
import com.example.project_4t_tople.fragment.CalendarFragment;
import com.example.project_4t_tople.fragment.InforFragment;
import com.example.project_4t_tople.fragment.PhotoFragment;

public class ContentsPagerAdapter extends FragmentStatePagerAdapter {

    private int mPageCount;

    Intent intent;

    public ContentsPagerAdapter(FragmentManager fm, int pageCount) {
        super(fm);
        this.mPageCount = pageCount;
    }

    public void setIntent(Intent intent) {
        this.intent = intent;
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }

    @Override
    public Fragment getItem(int position) {
        Bundle bundle = new Bundle();
        bundle.putString("user_id", intent.getStringExtra("user_id")); // Key, Value
        bundle.putSerializable("item", intent.getSerializableExtra("item")); // Key, Value

        switch (position) {
            case 0:
                InforFragment inforFragment = new InforFragment();
                inforFragment.setArguments(bundle);
                return inforFragment;

            case 1:
                BoardFragment boardFragment = new BoardFragment();
                boardFragment.setArguments(bundle);
                return boardFragment;

            case 2:
                PhotoFragment photoFragment = new PhotoFragment();
                photoFragment.setArguments(bundle);
                return photoFragment;

            case 3:
                CalendarFragment calendarFragment = new CalendarFragment();
                calendarFragment.setArguments(bundle);
                return calendarFragment;

            default:
                return null;

        }

    }


    @Override

    public int getCount() {


        return mPageCount;
    }

}
