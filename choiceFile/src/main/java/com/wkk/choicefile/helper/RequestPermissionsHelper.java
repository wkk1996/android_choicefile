package com.wkk.choicefile.helper;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


class RequestPermissionsHelper {

    public static final int ACTION_REQUEST_PERMISSIONS = 0x001;
    /**
     * 所需的所有权限信息
     */
    private static final String[] NEEDED_PERMISSIONS = new String[]{
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
    };

    public static void requestPermissions(Activity activity) {
        if (!checkPermissions(activity)) {
            ActivityCompat.requestPermissions(activity, NEEDED_PERMISSIONS, ACTION_REQUEST_PERMISSIONS);
        }
    }

    /**
     * 权限检查
     *
     * @return 是否全部被允许
     */
    public static boolean checkPermissions(Context context) {
        boolean allGranted = false;
        try {
            allGranted = true;
            for (String neededPermission : RequestPermissionsHelper.NEEDED_PERMISSIONS) {
                allGranted &= ContextCompat.checkSelfPermission(context, neededPermission) == PackageManager.PERMISSION_GRANTED;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return allGranted;
    }

}