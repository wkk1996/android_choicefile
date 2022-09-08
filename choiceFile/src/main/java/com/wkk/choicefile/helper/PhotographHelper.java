package com.wkk.choicefile.helper;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import com.wkk.choicefile.util.UriFileUtil;
import com.wkk.choicefile.util.ChoiceFileUtil;

import java.io.File;

/**
 * 拍照工具类
 */
public class PhotographHelper {

    private final Activity activity;
    private File photoFile;
    private Uri photoUri;
    public static final int REQUEST_CODE_CAMERA = 1996123;

    public PhotographHelper(Activity activity) {
        this.activity = activity;
    }

    public void startCamera() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            photoUri = get29MediaFileUri();
        } else if (Build.VERSION.SDK_INT >= 24) {
            photoUri = get24MediaFileUri();
        } else {
            photoUri = getMediaFileUri();
        }
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //添加这一句表示对目标应用临时授权该Uri所代表的文件
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
        activity.startActivityForResult(intent, REQUEST_CODE_CAMERA);
    }

    public File getFile(Intent data) {
        File file = new File(photoUri.getPath());
        if (!file.exists() || file.length() <= 0) {
            file = photoFile;
            if (file == null || !file.exists() || file.length() <= 0) {
                file = UriFileUtil.getImgByCameraFile(data, photoUri, activity);
            }
        }
        if (file == null || !file.exists() || file.length() == 0) {
            Toast.makeText(activity, "操作失败", Toast.LENGTH_LONG).show();
            return null;
        } else {
            return file;
        }
    }

    private Uri get29MediaFileUri() {
        String status = Environment.getExternalStorageState();
        // 判断是否有SD卡,优先使用SD卡存储,当没有SD卡时使用手机存储
        if (status.equals(Environment.MEDIA_MOUNTED)) {
            return activity.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new ContentValues());
        } else {
            return activity.getContentResolver().insert(MediaStore.Images.Media.INTERNAL_CONTENT_URI, new ContentValues());
        }
    }

    /**
     * 版本24以上 获取拍照uri
     */
    private Uri get24MediaFileUri() {
        File mediaFile = new File(ChoiceFileUtil.getBasePath(), "IMG_" + System.currentTimeMillis() + ".png");
        photoFile = mediaFile;
        return FileProvider.getUriForFile(activity, activity.getPackageName() + ".fileprovider", mediaFile);
    }

    /**
     * 获取拍照uri 版本24一下
     */
    private Uri getMediaFileUri() {
        File mediaFile = new File(ChoiceFileUtil.getBasePath(), "IMG_" + System.currentTimeMillis() + ".png");
        photoFile = mediaFile;
        return Uri.fromFile(mediaFile);
    }

}