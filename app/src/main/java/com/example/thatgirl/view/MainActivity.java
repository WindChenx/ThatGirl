package com.example.thatgirl.view;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import de.hdodenhof.circleimageview.CircleImageView;

import android.Manifest;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.thatgirl.R;
import com.example.thatgirl.base.activity.BaseActivity;
import com.example.thatgirl.base.view.BaseView;
import com.example.thatgirl.model.SpStorage;
import com.example.thatgirl.utils.FileUtils;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity implements BaseView {
    private static final String TAG = "MainActivity";
    private Toolbar mToolbar;
    private DrawerLayout mDrawerLayout;
    private CircleImageView mLoginIconView;
    private NavigationView mNavigationView;

    private String[] mPermissions = {
            Manifest.permission.READ_EXTERNAL_STORAGE
    };

    @Override
    protected int getLayout() {
        return R.layout.activity_main;
    }

    private final int PICK_DEFAULT_COVER = 1001;

    @Override
    protected void onViewCreated() {
        requestPermission();
        mToolbar = findViewById(R.id.toolbar);
        mLoginIconView = findViewById(R.id.login_icon);
        //toolbar   https://blog.csdn.net/qq_32059827/article/details/53635489
        //https://blog.csdn.net/qq_43278826/article/details/106493420
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        mDrawerLayout = findViewById(R.id.drawerlayout);
        mNavigationView = findViewById(R.id.navView);
        if (null != getSupportActionBar()) {
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu);
        }
        mLoginIconView = mNavigationView.getHeaderView(0).findViewById(R.id.login_icon);
        mLoginIconView.setOnClickListener(v -> {
            //获取图片uri的区别  https://www.jianshu.com/p/c5f207f8cce6
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            startActivityForResult(intent, PICK_DEFAULT_COVER);
        });
        String path = SpStorage.getInstance().getPhotoPath();
        if (path != null) {
            //String 转uri https://www.cnblogs.com/wukong1688/p/10738176.html
            mLoginIconView.setImageBitmap(BitmapFactory.decodeFile(path));
        }

    }

    //不申请权限获取到图片Uri为null
    private void requestPermission() {
        List<String> permissionsToRequire = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissionsToRequire.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissionsToRequire.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!permissionsToRequire.isEmpty()) {
            ActivityCompat.requestPermissions(this, mPermissions, 0);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_DEFAULT_COVER) {
            Uri dataUri = data.getData();
            String path = FileUtils.getFileAbsolutePath(this, dataUri);
            Bitmap bitmap = BitmapFactory.decodeFile(path);
            mLoginIconView.setImageBitmap(bitmap);
            SpStorage.getInstance().setPhotoPath(path);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 0) {
            for (int result : grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "You must allow all the permissions.", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        }
    }

    @Override
    protected void initData() {
        getSupportFragmentManager().beginTransaction()
                .add(R.id.main_fragment, ShowFragment.newInstance())
                .commit();

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
        }
        return true;
    }

    @Override
    protected void initView() {
    }

    @Override
    protected void onClick() {

    }

}
