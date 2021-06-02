package com.greyka.imgr.fragments;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.greyka.imgr.R;
import com.greyka.imgr.adapters.PlanDialogMemberAdapter;
import com.greyka.imgr.data.Data;
import com.greyka.imgr.data.Data.Task;
import com.greyka.imgr.dialogs.TaskListSelector;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class FragPlanList extends Fragment implements PlanDialogMemberAdapter.OnItemClickListener {

    public static final String ARG_OBJECT = "object";
    private static List<Data.Plan> planList;
    private Context context;
    private RecyclerView rv_selector_branch;
    private PlanDialogMemberAdapter mSelectorBranchAdapter;
    //private static int mPosition;
    private View view;
    private TaskListSelector taskListSelector;

    public FragPlanList(Context context) {
        super();
        Log.d("aaa", "vbreage");
        this.context = context;

    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_plan_list, container, false);
    }

    private void InitViews(View view) {
        rv_selector_branch = (RecyclerView) view.findViewById(R.id.plan_recycle);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        rv_selector_branch.setLayoutManager(layoutManager);
        mSelectorBranchAdapter = new PlanDialogMemberAdapter(planList, getContext());
        mSelectorBranchAdapter.setOnItemClickListener(this);
        rv_selector_branch.setAdapter(mSelectorBranchAdapter);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        this.view = view;
        refreshPlanList();
        InitViews(view);
    }

    public void showSelectorDialog(long plan_id) {
        List<Task> taskList = getTaskInPlan(plan_id);
        taskListSelector = new TaskListSelector(getActivity(), getFragmentManager(), taskList);
        taskListSelector.setCancelable(true);
        taskListSelector.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        taskListSelector.show();
    }

    @Override
    public void onItemClick(int position, int itemType) {
        //mPosition=position;
        //planList.get(position).getPlan_id();
        //请求plan_id的不重复的taskList
        if (itemType == 1) {
            showSelectorDialog(planList.get(position).getPlan_id());
        }
    }

    public void refreshPlanList() {
        planList = Data.Plan.planList;//new ArrayList<>();//获取任务
        myComparator_plan cmp = new myComparator_plan();
        Collections.sort(planList, cmp);
        Log.d("ref", "plan");
    }

    private List<Task> getTaskInPlan(long plan_id) {//根据id获取任务
        return new ArrayList<>();
    }

    class myComparator_plan implements Comparator {

        @Override
        public int compare(Object t1, Object t2) {
            Data.Plan T1 = (Data.Plan) t1;
            Data.Plan T2 = (Data.Plan) t2;
            if (T1.getCompleted() != T2.getCompleted()) {
                return T1.getCompleted() - T2.getCompleted();
            }
            return T1.getPlan_date().compareTo(T2.getPlan_date());
        }
    }
}

