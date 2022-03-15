package com.example.thatgirl;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.thatgirl.base.activity.BaseActivity;

public class DetailActivity extends BaseActivity {
    private String mUrl;
    private ImageView mDetailImageView;

    @Override
    protected int getLayout() {
        return R.layout.activity_image_details;
    }

    @Override
    protected void onViewCreated() {
        mUrl = getIntent().getStringExtra("image_detail_url");
        mDetailImageView = findViewById(R.id.ivImage);
    }

    @Override
    protected void initData() {
        if (mUrl != null) {
            Glide.with(this).load(mUrl).into(mDetailImageView);
        }
    }

    @Override
    protected void onClick() {

    }
}
