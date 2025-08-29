# 概述
该项目是在Android环境下，帮助您快捷的选择文件。
当前支持的选项
1. 拍照获取图片文件
2. 从相册选择图片文件
3. 从相册选择视频文件

# 集成

```
implementation 'io.github.wkk1996:choiceFile:1.0.0'
```

# 使用

AndroidManifest.xml 中增加配置：
```
        <provider
            android:name="androidx.core.content.FileProvider"
            android:authorities="你的包名.fileprovider"
            android:exported="false"
            android:grantUriPermissions="true">
            <meta-data
                android:name="android.support.FILE_PROVIDER_PATHS"
                android:resource="@xml/provider_path" />
        </provider>
```

```
        ChoiceFileHelper choiceFileHelper = new ChoiceFileHelper(this);
        // 文件选择的回调
        choiceFileHelper.setChoiceFileCallBack(new ChoiceFileCallBack() {
            @Override
            public void onFileResult(File file) {

            }
        });

        // 弹出对话框，让用户选择从相册选择图片或者拍照
        choiceFileHelper.choicePicture();
        // 相册选择图片
        choiceFileHelper.getPhoto();
        // 拍照获取图片
        choiceFileHelper.startCamera();
        // 相册选择视频
        choiceFileHelper.getVideo();
```
必要的配置，用于接收选择文件的回调
```
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        choiceFileHelper.onActivityResult(requestCode, resultCode, data);
    }
```
