package com.example.thatgirl.view;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.ResponseBody;
import pub.devrel.easypermissions.EasyPermissions;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.Manifest;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;

import com.example.thatgirl.R;
import com.example.thatgirl.adapter.GirlAdapter;
import com.example.thatgirl.base.activity.BaseActivity;
import com.example.thatgirl.base.view.BaseView;
import com.example.thatgirl.contract.IShowGrilContract;
import com.example.thatgirl.entity.Girl;
import com.example.thatgirl.model.SpStorage;
import com.example.thatgirl.model.https.api.ApiService;
import com.example.thatgirl.model.https.api.RetrofitFactory;
import com.example.thatgirl.utils.FileUtils;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity implements BaseView, EasyPermissions.PermissionCallbacks {
    private static final String TAG = "MainActivity";
    private Toolbar mToolbar;
    private DrawerLayout mDrawerLayout;
    private CircleImageView mLoginIconView;
    private NavigationView mNavigationView;

//    private ShowContentPresenter showContentPresenter;

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
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, PICK_DEFAULT_COVER);
        });
        String path = SpStorage.getInstance().getPhotoPath();
        if ( path != null) {
            //String 转uri https://www.cnblogs.com/wukong1688/p/10738176.html
            mLoginIconView.setImageBitmap(BitmapFactory.decodeFile(path));
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        //没有读权限去请求
        if (!EasyPermissions.hasPermissions(this, mPermissions)) {
            EasyPermissions.requestPermissions(this, "获取存储权限", 500, mPermissions);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_DEFAULT_COVER) {
            Uri dataUri = data.getData();
            String path = getPath(this, dataUri);
            Log.d("TAG","--------"+dataUri+"---"+path);

            Bitmap bitmap = BitmapFactory.decodeFile(path);
            mLoginIconView.setImageBitmap(bitmap);
            SpStorage.getInstance().setPhotoPath(path);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[] {
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context The context.
     * @param uri The Uri to query.
     * @param selection (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        finish();
    }
}
