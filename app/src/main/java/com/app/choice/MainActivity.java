package com.app.choice;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.wkk.choicefile.callback.ChoiceFileCallBack;
import com.wkk.choicefile.helper.ChoiceFileHelper;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    ChoiceFileHelper choiceFileHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        choiceFileHelper = new ChoiceFileHelper(this);

        ImageView ivImg = findViewById(R.id.iv_img);

        findViewById(R.id.bt_1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choiceFileHelper.choicePicture();
                choiceFileHelper.setChoiceFileCallBack(new ChoiceFileCallBack() {
                    @Override
                    public void onFileResult(File file) {
                        ivImg.setImageBitmap(BitmapFactory.decodeFile(file.getAbsolutePath()));
                    }
                });
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        choiceFileHelper.onActivityResult(requestCode, resultCode, data);
    }
}