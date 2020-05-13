package com.my.taskmanage.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.my.taskmanage.R;
import com.my.taskmanage.db.DbHelper;
import com.my.taskmanage.entity.TaskEntity;

import org.xutils.DbManager;
import org.xutils.ex.DbException;

import androidx.annotation.Nullable;

public class TaskDetailsActivity extends Activity {
    private TextView tvName;
    private TextView tvDes;
    private TextView tvCategory;
    private TextView tvUrgent;
    private TextView tvImportant;
    private ImageView iv;
    private Button btnDelete;
    private Button btnCancel;
    
    private int id;
    
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_details);
        id=getIntent().getIntExtra("id",0);
        initView();
        initData();
        initEvent();
    }
    
    private void initEvent() {
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DbManager dbManager = DbHelper.getInstance().getDbManager();
                try {
                    TaskEntity entity=dbManager.findById(TaskEntity.class,id);
                    dbManager.delete(entity);
                    Toast.makeText(TaskDetailsActivity.this,"delete successfully",Toast.LENGTH_SHORT).show();
                    finish();
                } catch (DbException e) {
                    e.printStackTrace();
                }
            }
        });
        
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        
    }
    
    private void initData() {
        DbManager dbManager = DbHelper.getInstance().getDbManager();
        try {
            TaskEntity entity=dbManager.findById(TaskEntity.class,id);
            String name=entity.getName();
            String des=entity.getDes();
            String category=entity.getCategory();
            String pic=entity.getPic();
            boolean isUrgent=entity.isUrgent();
            boolean isImportant=entity.isImportant();
            
            tvName.setText("Task Name:"+name);
            tvDes.setText("Task Description:"+des);
            tvCategory.setText("Category:"+category);
            if (isImportant){
                tvImportant.setText("Important");
            }else {
                tvImportant.setText("Not Important");
            }
            if (isUrgent){
                tvUrgent.setText("Urgent");
            }else {
                tvUrgent.setText("Not Urgent");
            }
            
            Bitmap bitmap= BitmapFactory.decodeFile(pic,null);
            iv.setImageBitmap(bitmap);
        } catch (DbException e) {
            e.printStackTrace();
        }
    
    }
    
    private void initView() {
        tvName = (TextView) findViewById(R.id.tv_name);
        tvDes = (TextView) findViewById(R.id.tv_des);
        tvCategory = (TextView) findViewById(R.id.tv_category);
        tvUrgent = (TextView) findViewById(R.id.tv_urgent);
        tvImportant = (TextView) findViewById(R.id.tv_important);
        iv = (ImageView) findViewById(R.id.iv);
        btnDelete = (Button) findViewById(R.id.btn_delete);
        btnCancel = (Button) findViewById(R.id.btn_cancel);
    }
}
