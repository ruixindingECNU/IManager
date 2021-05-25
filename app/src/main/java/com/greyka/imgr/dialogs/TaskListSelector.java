package com.greyka.imgr.dialogs;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import com.greyka.imgr.R;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.greyka.imgr.activities.Timer;
import com.greyka.imgr.adapters.DialogMemberAdapter;
import com.greyka.imgr.data.Data.Task;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jie on 2018/9/9.
 */

public class TaskListSelector extends Dialog implements DialogMemberAdapter.OnItemClickListener{
    private List<Task> taskList = new ArrayList<>();    //选择列表的数据
    private Task task = new Task();
    private Context context;
    private static int mPosition;

    private DialogMemberAdapter mSelectorBranchAdapter;
    private RecyclerView rv_selector_branch;
    private TodayTaskDialog todayTaskDialog;
    private TaskItemDialog taskItemDialog;
    private ImageView editTitle;


    public TaskListSelector(Context context, List<Task> mSimpleListItemEntity) {
        super(context);
        this.context = context;
        this.taskList = mSimpleListItemEntity;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.task_dialog);
        this.setCanceledOnTouchOutside(true); // 点击外部会消失
        InitViews();
    }

    private void InitViews() {

        rv_selector_branch = (RecyclerView) findViewById(R.id.task_selector);
        LinearLayoutManager layoutmanager = new LinearLayoutManager(getContext());
        rv_selector_branch.setLayoutManager(layoutmanager);
        mSelectorBranchAdapter = new DialogMemberAdapter(taskList);
        mSelectorBranchAdapter.setOnItemClickListener(this);
        rv_selector_branch.setAdapter(mSelectorBranchAdapter);
    }


    /**
     * adpter里面的checkbox监听接口
     * @param position item的位置
     *                 改变元数据集的内容
     */
    @Override
    public void onItemClick(int position) {
       // todayTaskDialog = new TodayTaskDialog();
       // final FragmentActivity myActivity=(FragmentActivity) context;
        //todayTaskDialog.show(myActivity.getSupportFragmentManager(), "TaskDialog"); ;
        mPosition=position;
        taskItemDialog = new TaskItemDialog(this,context,taskList.get(position));
        taskItemDialog.setCancelable(false);
        taskItemDialog.show();
        Activity myActivity=(Activity)context;
        LayoutInflater inflater = LayoutInflater.from(myActivity);
        View viewDialog = inflater.inflate(R.layout.task_info, null);

        Display display = myActivity.getWindowManager().getDefaultDisplay();
        int width = display.getWidth();
        int height = display.getHeight();
//设置dialog的宽高为屏幕的宽高
        ViewGroup.LayoutParams layoutParams = new  ViewGroup.LayoutParams(width, height);
        taskItemDialog.setContentView(viewDialog, layoutParams);
        taskItemDialog.InitViews();




    }
    public void UpdateViews(Task task_edited){
        Log.d("myActivity",task_edited.getTask_name());
        mSelectorBranchAdapter.UpdateItem(mPosition,task_edited);
        this.InitViews();
       // this.show();
    }






}
