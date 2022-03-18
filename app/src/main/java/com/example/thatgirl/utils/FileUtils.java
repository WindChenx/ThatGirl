package com.example.thatgirl.utils;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.SystemClock;
import android.os.storage.StorageManager;
import android.os.storage.StorageVolume;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.channels.FileChannel;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;


/**
 * @author xiao
 */
public class FileUtils {
    private static final String TAG = "FileUtils";

    /**
     * 判断sdcrad是否已经安装
     *
     * @return boolean true安装 false 未安装
     */
    public static boolean isSDCardMounted() {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }

    /**
     * 得到sdcard的路径
     *
     * @return
     */
    public static String getSDCardRoot() {
        System.out.println(isSDCardMounted() + Environment.getExternalStorageState());
        if (isSDCardMounted()) {
            return Environment.getExternalStorageDirectory().getAbsolutePath();
        }
        return "";
    }

    /**
     * 创建文件的路径及文件
     *
     * @param path     路径，方法中以默认包含了sdcard的路径，path格式是"/path...."
     * @param filename 文件的名称
     * @return 返回文件的路径，创建失败的话返回为空
     */
    public static String createMkdirsAndFiles(String path, String filename) {
        if (TextUtils.isEmpty(path)) {
            throw new RuntimeException("路径为空");
        }
        path = getSDCardRoot() + path;
        File file = new File(path);
        if (!file.exists()) {
            try {
                file.mkdirs();
            } catch (Exception e) {
                throw new RuntimeException("创建文件夹不成功");
            }
        }
        File f = new File(file, filename);
        if (!f.exists()) {
            try {
                f.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException("创建文件不成功");
            }
        }
        return f.getAbsolutePath();
    }

    /**
     * 把内容写入文件
     *
     * @param path 文件路径
     * @param text 内容
     */
    public static void write2File(String path, String text, boolean append) {
        BufferedWriter bw = null;
        try {
            //1.创建流对象  
            bw = new BufferedWriter(new FileWriter(path, append));
            //2.写入文件  
            bw.write(text);
            //换行刷新  
            bw.newLine();
            bw.flush();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //4.关闭流资源  
            if (bw != null) {
                try {
                    bw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static boolean isSdcardExist(Context context) {
        String path = getSdcardPath(context);
        if (path != null) {
            File file = new File(path);
            if (file.canRead()) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    public static boolean isOtgExist(Context context) {
        String path = getOtgPath(context);
        if (path != null) {
            File file = new File(path);
            if (file.canRead()) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }


    public static String getInternalStoragePath() {
        return Environment.getExternalStorageDirectory().getAbsolutePath();
    }

    public static String getSdcardPath(Context context) {
        return getRemovableVolumePath(context, false);
    }

    public static String getOtgPath(Context context) {
        return getRemovableVolumePath(context, true);
    }

    public static String getRemovableVolumePath(Context context, boolean isOTG) {
        if (Build.VERSION.SDK_INT >= 23) {
            return getRemovableVolumePath23(context, isOTG);
        } else {
            return getRemovableVolumePath22(context, isOTG);
        }
    }

    private static String getRemovableVolumePath22(Context context, boolean isOTG) {
        StorageManager manager = (StorageManager) context.getSystemService(Context.STORAGE_SERVICE);
        Class<?>[] paramClasses = {};
        try {
            Method getVolumeList = manager.getClass().getMethod("getVolumeList", paramClasses);
            Object[] params = {};
            StorageVolume[] volumes = (StorageVolume[]) getVolumeList.invoke(manager, params);
            for (StorageVolume volume : volumes) {
                Method getPathMethod = volume.getClass().getMethod("getPath", paramClasses);
                Method isRemovableMethod = volume.getClass().getMethod("isRemovable", paramClasses);
                boolean isRemovable = (Boolean) isRemovableMethod.invoke(volume, params);
                if (isRemovable) {
                    String path = (String) getPathMethod.invoke(volume, params);
                    Log.i("ibasso", "mount_path=" + path);
                    if (isOTG) {
                        if (path.indexOf("usb_storage") > -1 || path.indexOf("usbotg") > -1) {
                            return path;
                        }
                    } else {
                        if (path.indexOf("external_sd") > -1) {
                            return path;
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    private static String getRemovableVolumePath23(Context context, boolean isOTG) {
        try {
            Class<StorageManager> storageManagerClass = StorageManager.class;
            StorageManager storageManager = (StorageManager) context.getSystemService(Context.STORAGE_SERVICE);
            Method getVolumesMethod = storageManagerClass.getMethod("getVolumes");

            Class<?> volumeInfoClass = Class.forName("android.os.storage.VolumeInfo");
            Method getVolumePathMethod = volumeInfoClass.getMethod("getPath");
            Method getVolumeDiskMethod = volumeInfoClass.getMethod("getDisk");
            Method isMountedReadableMethod = volumeInfoClass.getMethod("isMountedReadable");

            Class<?> diskInfoClass = Class.forName("android.os.storage.DiskInfo");
            Method isSdMethod = diskInfoClass.getMethod("isSd");
            Method isUsbMethod = diskInfoClass.getMethod("isUsb");

            List<Object> volumes = (List<Object>) getVolumesMethod.invoke(storageManager);

            for (Object volumeInfo : volumes) {
                File file = (File) getVolumePathMethod.invoke(volumeInfo);
                Log.e("ibasso", "file=" + file);
                if (file != null && file.canRead()) {
                    Object diskInfo = getVolumeDiskMethod.invoke(volumeInfo);
                    if (diskInfo != null) {
                        boolean isSd = (Boolean) isSdMethod.invoke(diskInfo);
                        boolean isUsb = (Boolean) isUsbMethod.invoke(diskInfo);
                        boolean isMountedReadable = (Boolean) isMountedReadableMethod.invoke(volumeInfo);
                        Log.e("ibasso", "isSd=" + isSd + " isUsb" + isUsb + "isMountedReadable " + isMountedReadable);
                        if (isUsb && isOTG) {
                            if (isMountedReadable) {
                                return file.getPath();
                            } else {
                                return null;
                            }
                        } else if (isSd && !isOTG) {
                            if (isMountedReadable) {
                                return file.getPath();
                            } else {
                                return null;
                            }
                        }
                    } else {
                        Log.e("ibasso", "diskInfo == null:" + true);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 根据Uri获取图片绝对路径
     *
     * @param context context
     * @param uri     uri
     */
    public static String getFileAbsolutePath(Context context, Uri uri) {
        if (context == null || uri == null) {
            return null;
        }
        // DocumentProvider
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && DocumentsContract.isDocumentUri(context, uri)) {
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                } else if ("home".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/documents/" + split[1];
                } else {
                    return "/storage/" + type + "/" + split[1];
                }
            } else if (isDownloadsDocument(uri)) {
                // DownloadsProvider
                final String id = DocumentsContract.getDocumentId(uri);
                if (TextUtils.isEmpty(id)) {
                    return null;
                }
                if (id.startsWith("raw:")) {
                    return id.substring(4);
                }
                String[] contentUriPrefixesToTry = new String[]{
                        "content://downloads/public_downloads",
                        "content://downloads/my_downloads",
                        "content://downloads/all_downloads"
                };
                for (String contentUriPrefix : contentUriPrefixesToTry) {
                    try {
                        Uri contentUri = ContentUris.withAppendedId(Uri.parse(contentUriPrefix), Long.valueOf(id));
                        String path = getDataColumn(context, contentUri, null, null);
                        if (path != null) {
                            return path;
                        }
                    } catch (Exception ignore) {
                    }
                }
                try {
                    String path = getDataColumn(context, uri, null, null);
                    if (path != null) {
                        return path;
                    }
                } catch (Exception ignore) {
                }
                // path could not be retrieved using ContentResolver, therefore copy file to accessible cache using streams
                return null;
            } else if (isMediaDocument(uri)) {
                // MediaProvider
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri;
                switch (type.toLowerCase(Locale.ENGLISH)) {
                    case "image":
                        contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                        break;
                    case "video":
                        contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                        break;
                    case "audio":
                        contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                        break;
                    default:
                        contentUri = MediaStore.Files.getContentUri("external");
                        break;
                }
                final String selection = MediaStore.MediaColumns._ID + "=?";
                final String[] selectionArgs = new String[]{split[1]};
                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        } else if (ContentResolver.SCHEME_CONTENT.equalsIgnoreCase(uri.getScheme())) {
            // MediaStore (and general)
            // Return the remote address
            if (isGooglePhotosUri(uri)) {
                return uri.getLastPathSegment();
            }
            return getDataColumn(context, uri, null, null);
        } else if (ContentResolver.SCHEME_FILE.equalsIgnoreCase(uri.getScheme())) {
            // File
            return uri.getPath();
        }
        return null;
    }

    /**
     * 通过游标获取当前文件路径
     *
     * @param context       context
     * @param uri           uri
     * @param selection     selection
     * @param selectionArgs selectionArgs
     * @return 路径，未找到返回null
     */
    public static String getDataColumn(Context context, @NonNull Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        String column = MediaStore.Images.Media.DATA;
        String[] projection = {column};
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } catch (Exception ignore) {
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return null;
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    /**
     * 复制单个文件
     *
     * @param oldPath String 原文件路径
     * @param newPath String 复制后路径
     * @return <code>true</code> if and only if the file was copied;
     * <code>false</code> otherwise
     */
    public static boolean copyFile(String oldPath, String newPath) {
        FileInputStream fileInputStream = null;
        FileOutputStream fileOutputStream = null;
        FileChannel in = null;
        FileChannel out = null;
        try {
            File oldFile = new File(oldPath);
            File newFile = new File(newPath);
            if (!oldFile.exists() || !oldFile.isFile() || !oldFile.canRead()) {
                return false;
            }
            if (newFile.exists()) {
                newFile.delete();
            }
            fileInputStream = new FileInputStream(oldPath);
            fileOutputStream = new FileOutputStream(newPath);
            in = fileInputStream.getChannel();
            out = fileOutputStream.getChannel();
            in.transferTo(0, in.size(), out);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (fileInputStream != null) {
                    fileInputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    public static void deleteFile(String defaultCoverPath) {
        if (defaultCoverPath == null) {
            return;
        }
        File file = new File(defaultCoverPath);
        if (file.exists()) {
            boolean result = file.delete();
            if (!result) {
                Log.e(TAG, "delete file:" + defaultCoverPath + " error!");
            }
        }
    }

    /**
     * 设置节点的值
     */
    public static void setSysValue(String filePath, int value) {
        try {
            FileWriter fileWriter = new FileWriter(filePath);
            fileWriter.write("" + value);
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void setSysValue(String filePath, String value) {
        try {
            FileWriter fileWriter = new FileWriter(filePath);
            fileWriter.write(value);
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String saveBitmap(Bitmap bitmap, String path) {
        File file = new File(path + "compress");
        if (file.exists()) {
            file.delete();
        }
        FileOutputStream out;
        try {
            out = new FileOutputStream(file);
            if (bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)) {
                out.flush();
                out.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file.getAbsolutePath();
    }

    public static String compressBitmap(String path) {
        if (path != null) {
            Bitmap bitmap = null;
            if (!new File(path + "compress").exists()) {
                bitmap = decodeSampledBitmapFromPath(path, 300, 300);
            } else {
                return path + "compress";
            }
            if (bitmap != null) {
                return saveBitmap(bitmap, path);
            }
        }
        return path;
    }

    public static Bitmap decodeSampledBitmapFromPath(String path, int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(path, options);
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;


        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }
}
