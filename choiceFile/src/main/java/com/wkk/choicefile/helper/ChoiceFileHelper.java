package com.wkk.choicefile.helper;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.ViewGroup;
import android.widget.Toast;

import com.wkk.choicefile.R;
import com.wkk.choicefile.callback.ChoiceFileCallBack;
import com.wkk.choicefile.util.ChoiceFileUtil;
import com.wkk.choicefile.util.HandlerUtils;
import com.wkk.choicefile.util.L;
import com.wkk.choicefile.util.UriFileUtil;

import java.io.File;

/**
 * 选择帮助类
 */
public class ChoiceFileHelper {

    private final Activity activity;
    private final PhotographHelper photographHelper;
    public final static int PICK_PHOTO_DATA = 19961;
    public final static int PICK_VIDEO_DATA = 19962;

    private ChoiceFileCallBack choiceFileCallBack;

    public ChoiceFileHelper(Activity activity) {
        this.activity = activity;
        this.photographHelper = new PhotographHelper(activity);
    }

    /**
     * 选择图片对话框
     */
    public void choicePicture() {
        Dialog dialog = ChoiceFileUtil.getDialogBottom(R.layout.dialog_choice_picture, activity);
        dialog.findViewById(R.id.tv_clear).setOnClickListener(v -> dialog.dismiss());
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        // 拍照
        dialog.findViewById(R.id.tv_photograph).setOnClickListener(v -> {
            dialog.dismiss();
            startCamera();
        });
        // 从相册选择
        dialog.findViewById(R.id.tv_album).setOnClickListener(v -> {
            dialog.dismiss();
            getPhoto();
        });
    }

    /**
     * 接收选择文件的返回内容
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case PhotographHelper.REQUEST_CODE_CAMERA: // 拍照选择图片
                if (resultCode == Activity.RESULT_OK) {
                    File file = photographHelper.getFile(data);
                    if (file != null) {
                        if (choiceFileCallBack != null) {
                            choiceFileCallBack.onFileResult(file);
                        }
                    }
                }
                break;
            case ChoiceFileHelper.PICK_PHOTO_DATA: // 相册选择图片
                if (resultCode == Activity.RESULT_OK) {
                    Uri selectedImage = data.getData();
                    File file = UriFileUtil.getImgFile(selectedImage, activity);
                    if (file == null) {
                        file = getFile(selectedImage);
                    }
                    if (file == null) {
                        Toast.makeText(activity, "操作失败", Toast.LENGTH_LONG).show();
                    } else {
                        if (choiceFileCallBack != null) {
                            choiceFileCallBack.onFileResult(file);
                        }
                    }
                }
                break;
            case ChoiceFileHelper.PICK_VIDEO_DATA: // 相册选择视频
                if (resultCode == Activity.RESULT_OK) {
                    Uri selectedImage = data.getData();
                    new Thread() {
                        @Override
                        public void run() {
                            super.run();
                            File file = UriFileUtil.getVideoFile(selectedImage, activity);
                            if (file == null) {
                                Toast.makeText(activity, "操作失败", Toast.LENGTH_LONG).show();
                            } else {
                                if (choiceFileCallBack != null) {
                                    HandlerUtils.postRunnable(() -> choiceFileCallBack.onFileResult(file));
                                }
                            }
                        }
                    }.start();
                }
                break;
            default:
                break;
        }
    }

    private File getFile(Uri selectedImage) {
        try {
            //获取系统返回的照片的Uri
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            //从系统表中查询指定Uri对应的照片
            Cursor cursor = activity.getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            //获取照片路径
            String paths = cursor.getString(columnIndex);
            cursor.close();
            File file = new File(paths);
            if (!file.exists() || file.length() == 0) {
                return null;
            } else {
                return file;
            }
        } catch (Exception e) {
            L.e(e);
            return null;
        }
    }

    /**
     * 打开相机拍照
     */
    public void startCamera() {
        if (RequestPermissionsHelper.checkPermissions(activity)) {
            photographHelper.startCamera();
        } else {
            RequestPermissionsHelper.requestPermissions(activity);
        }
    }

    /**
     * 相册选择图片
     */
    public void getPhoto() {
        Intent intent = new Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        activity.startActivityForResult(intent, PICK_PHOTO_DATA);
    }

    /**
     * 选择视频
     */
    public void getVideo() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        activity.startActivityForResult(intent, PICK_VIDEO_DATA);
    }

    /**
     * 结果监听
     */
    public void setChoiceFileCallBack(ChoiceFileCallBack choiceFileCallBack) {
        this.choiceFileCallBack = choiceFileCallBack;
    }

}