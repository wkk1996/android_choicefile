package com.wkk.choicefile.util;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.os.Build;
import android.os.Environment;
import android.view.Gravity;

import com.wkk.choicefile.R;
import com.wkk.choicefile.dto.VideoDto;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Hashtable;


public class ChoiceFileUtil {

    /**
     * 获取视频第一帧
     * imageView.setImageBitmap(createVideoThumbnail(urlPath,MediaStore.Images.Thumbnails.MINI_KIND));
     */
    public static VideoDto createVideoThumbnail(String filePath, int kind) {
        Bitmap bitmap = null;
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {
            if (filePath.startsWith("http://")
                    || filePath.startsWith("https://")
                    || filePath.startsWith("widevine://")) {
                retriever.setDataSource(filePath, new Hashtable<>());
            } else {
                retriever.setDataSource(filePath);
            }
            bitmap = retriever.getFrameAtTime(0, MediaMetadataRetriever.OPTION_CLOSEST_SYNC); //retriever.getFrameAtTime(-1);
            int duration = Integer.parseInt(retriever.extractMetadata
                    (MediaMetadataRetriever.METADATA_KEY_DURATION)) / 1000;//除以 1000 返回是秒
            VideoDto videoDto = new VideoDto();
            videoDto.setBitmap(bitmap);
            videoDto.setTime(duration);
            return videoDto;
        } catch (RuntimeException ex) {
            ex.printStackTrace();
        } finally {
            try {
                retriever.release();
            } catch (RuntimeException ex) {
                // Ignore failures while cleaning up.
                ex.printStackTrace();
            }
        }
        return new VideoDto();
    }

    public static String getBasePath() {
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/cache/");
        file.mkdirs();
        return file.getAbsolutePath();
    }


    public static Dialog getDialogBottom(int res, Activity activity) {
        Dialog dialog = new Dialog(activity, R.style.customDialog);
        dialog.setContentView(res);
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.show();
        return dialog;
    }

    public static File saveBitmapToLocal(Bitmap bitmap, Context context) {
        // 保存文件到本地
        File file;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            file = new File(context.getDataDir(), System.currentTimeMillis() + ".png");
        } else {
            file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), System.currentTimeMillis() + ".png");
        }
        saveBitmapToLocal(bitmap, file);
        return file;
    }

    private static void saveBitmapToLocal(Bitmap bitmap, File file) {
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();
        } catch (Exception e) {
            L.d(e);
        }
    }

}