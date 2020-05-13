package com.my.taskmanage.adapter;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.my.taskmanage.R;
import com.my.taskmanage.entity.TaskEntity;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TaskAdapter  extends RecyclerView.Adapter<TaskAdapter.ViewHolder>{
    private Context mContext;
    private List<TaskEntity> mList;
    
    public TaskAdapter(Context context, List<TaskEntity> list) {
        mContext = context;
        mList = list;
    }
    
    public void setData(List<TaskEntity> mList){
        this.mList=mList;
    }
    
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view1= LayoutInflater.from(mContext).inflate(R.layout.rv_item,parent,false);
        ViewHolder holder = new ViewHolder(view1);
        return holder;
    }
    
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.tvName.setText(mList.get(position).getName());
        holder.tvDes.setText(mList.get(position).getDes());
        String photoPath=mList.get(position).getPic();
        Bitmap bitmap= BitmapFactory.decodeFile(photoPath,null);
        holder.iv.setImageBitmap(bitmap);
    
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onClick(position);
                }
            }
        });
    }
    
    @Override
    public int getItemCount() {
        return mList==null?0:mList.size();
    }
    
    class ViewHolder extends RecyclerView.ViewHolder{
        ImageView iv;
        TextView tvName;
        TextView tvDes;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            iv=itemView.findViewById(R.id.iv);
            tvName=itemView.findViewById(R.id.tv_name);
            tvDes=itemView.findViewById(R.id.tv_des);
        }
    }
    
    
    //第一步 定义接口
    public interface OnItemClickListener {
        void onClick(int position);
    }
    
    private OnItemClickListener listener;
    
    //第二步， 写一个公共的方法
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
    
}
