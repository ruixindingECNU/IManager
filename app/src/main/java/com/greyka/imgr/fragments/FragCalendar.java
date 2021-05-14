package com.greyka.imgr.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.greyka.imgr.R;
import com.greyka.imgr.adapters.myRecyclerViewAdapter;
import com.greyka.imgr.utilities.myUtils;

public class FragCalendar extends Fragment {

    String[] memos = {"asd", "bds", "a", "bds", "a", "bds", "a", "bds", "a", "bds", "a", "bds", "a", "bds", "a", "bds", "a", "bds", "a", "bds", "a", "bds", "a", "bds", "a", "bds", "a"};

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_calendar, container, false);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        CardView calenderCard = view.findViewById(R.id.Calendar_card);
        View memo = view.findViewById(R.id.memo);
        myUtils.myViewMover calenderCardMover = new myUtils.myViewMover(calenderCard);
        myUtils.myViewMover memoMover = new myUtils.myViewMover(memo);
        myUtils.myDensityHelper density = new myUtils.myDensityHelper(this.requireContext());
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.requireContext()));
        recyclerView.setAdapter(new myRecyclerViewAdapter(memos));
        calenderCard.setOnTouchListener((v, event) -> {
            calenderCardMover.move(event, 0, 0, -v.getHeight() + density.dp2px(90), 0);
            memo.layout(memo.getLeft(), calenderCard.getBottom() + density.dp2px(10), memo.getRight(), memo.getBottom());
            recyclerView.layout(recyclerView.getLeft(), recyclerView.getTop(), recyclerView.getRight(), memo.getHeight() - density.dp2px(15));
            return true;
        });
        CalendarView calendarView = view.findViewById(R.id.calendarView);
        calendarView.setOnDateChangeListener((view1, year, month, dayOfMonth) -> myUtils.myToastHelper.showText(view1.getContext(), +year + "年" + month + "月" + dayOfMonth + "日", Toast.LENGTH_SHORT));

        super.onViewCreated(view, savedInstanceState);
    }
}
