package com.example.thatgirl.adapter;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.thatgirl.R;
import com.example.thatgirl.entity.Girl;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class GirlAdapter extends RecyclerView.Adapter<GirlAdapter.ViewHolder> {
    private Context context;
    private List<Girl.DataBean> girlList;
    private static AdapterView.OnItemClickListener mItemClick;


    public static void setItemClick(AdapterView.OnItemClickListener itemClick) {
        mItemClick = itemClick;
    }



    public GirlAdapter(Context context,List<Girl.DataBean> girlList) {
        this.girlList = girlList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(context==null){
            context=parent.getContext();
        }
        View view= LayoutInflater.from(context).inflate(R.layout.girl_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Girl.DataBean girlBean=girlList.get(position);
        holder.textView_girl.setText(girlBean.getDesc());
        Glide.with(context).load(girlBean.getUrl()).into(holder.imageView_gril);
    }

    @Override
    public int getItemCount() {
        return girlList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView_gril;
        TextView textView_girl;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView_gril=itemView.findViewById(R.id.girl_image);
            textView_girl=itemView.findViewById(R.id.girl_text);
        }
    }



}
