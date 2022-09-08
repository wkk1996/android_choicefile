package com.wkk.choicefile.util;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;

/**
 * 从uri中获取文件的工具类
 */
public class UriFileUtil {

    /**
     * 获取拍照后返回的文件
     */
    public static File getImgByCameraFile(Intent data, Uri photoUri, Activity activity) {
        Bitmap bitmap = null;
        if (data != null) {
            if (data.hasExtra("data")) {
                bitmap = data.getParcelableExtra("data");
            }
        }
        if (bitmap == null) {
            if (Build.VERSION.SDK_INT >= 24) {
                try {
                    bitmap = BitmapFactory.decodeStream(activity.getContentResolver().openInputStream(photoUri));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            } else {
                bitmap = BitmapFactory.decodeFile(photoUri.getPath());
            }
        }
        if (bitmap == null) {
            return null;
        }
        File file = ChoiceFileUtil.saveBitmapToLocal(bitmap, activity);
        bitmap.recycle();
        return file;
    }

    /**
     * 获取相册选择返回的文件
     */
    public static File getImgFile(Uri photoUri, Activity activity) {
        Bitmap bitmap;
        if (Build.VERSION.SDK_INT >= 24) {
            bitmap = null;
            try {
                bitmap = BitmapFactory.decodeStream(activity.getContentResolver().openInputStream(photoUri));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            bitmap = BitmapFactory.decodeFile(photoUri.getPath());
        }
        if (bitmap == null) {
            return null;
        }
        File file = ChoiceFileUtil.saveBitmapToLocal(bitmap, activity);
        bitmap.recycle();
        return file;
    }

    /**
     * 获取相册中选择的视频
     */
    public static File getVideoFile(Uri uri, Activity activity) {
        ContentResolver cr = activity.getContentResolver();
        Cursor cursor = cr.query(uri, null, null, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                // 视频ID:MediaStore.Audio.Media._ID
                int videoId = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID));
                // 视频名称：MediaStore.Audio.Media.TITLE
                String title = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.TITLE));
                // 视频路径：MediaStore.Audio.Media.DATA
                String videoPath = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA));
                // 视频时长：MediaStore.Audio.Media.DURATION
                int duration = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION));
                // 视频大小：MediaStore.Audio.Media.SIZE
                long size = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE));
                // 视频缩略图路径：MediaStore.Images.Media.DATA
                String imagePath = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));
                // 缩略图ID:MediaStore.Audio.Media._ID
                int imageId = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID));
                try {
                    InputStream inputStream = activity.getContentResolver().openInputStream(uri);
                    return saveToLocal(inputStream, videoPath, activity);
                } catch (FileNotFoundException e) {
                    L.e(e);
                }
                return null;
            }
            cursor.close();
        }
        return null;
    }


    //**********************************************************************************************

    private static File saveToLocal(InputStream inputStream, String path, Context context) {
        String defFileName = "wkk.mp4";
        // 保存文件到本地
        File file;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            file = new File(context.getDataDir(), getName(path, defFileName));
        } else {
            file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), getName(path, defFileName));
        }
        if (file.exists()) {
            file.delete();
        }
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);

            byte[] b = new byte[1024 * 1024];
            int n;
            while ((n = inputStream.read(b)) != -1) {
                fileOutputStream.write(b, 0, n);
            }
            fileOutputStream.flush();
            fileOutputStream.close();
            inputStream.close();
        } catch (Exception e) {
            L.e(e);
        }
        return file;
    }

    private static String getName(String path, String defName) {
        if (path.lastIndexOf("/") == -1) {
            return defName;
        }
        return path.substring(path.lastIndexOf("/"));
    }

}