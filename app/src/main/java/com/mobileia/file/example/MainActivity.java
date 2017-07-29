package com.mobileia.file.example;

import android.Manifest;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.kbeanie.multipicker.api.ImagePicker;
import com.kbeanie.multipicker.api.Picker;
import com.kbeanie.multipicker.api.callbacks.ImagePickerCallback;
import com.kbeanie.multipicker.api.entity.ChosenImage;
import com.mobileia.core.Mobileia;
import com.mobileia.file.MobileiaFile;
import com.mobileia.file.rest.RestGenerator;

import java.io.File;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ImagePicker mImagePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Configurar Mobileia Lab
        Mobileia.getInstance().setAppId(4);

        Dexter.withActivity(this)
                .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {

                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

                    }
                })
                .check();
        createImagePicker();
    }

    public void onClick(View view){
        mImagePicker.pickImage();
    }

    private void createImagePicker(){
        mImagePicker = new ImagePicker(this);
        mImagePicker.setImagePickerCallback(new ImagePickerCallback() {
            @Override
            public void onImagesChosen(List<ChosenImage> list) {
                System.out.println("Mobileia: Se seleccionop una imagen");
                System.out.println(list.get(0).getOriginalPath());
                File file = new File(list.get(0).getOriginalPath());
                MobileiaFile.getInstance().upload(file, new RestGenerator.OnFileUpload() {
                    @Override
                    public void onSuccess(com.mobileia.file.entity.File file) {
                        System.out.println("Mobileia: Success file: " + file.name);
                        System.out.println("Mobileia: Success file: " + file.path);
                        System.out.println("Mobileia: Success file: " + file.type);
                    }

                    @Override
                    public void onError() {

                    }
                });
            }

            @Override
            public void onError(String s) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK) {
            if(requestCode == Picker.PICK_IMAGE_DEVICE) {
                if(mImagePicker == null) {
                    createImagePicker();
                }
                mImagePicker.submit(data);
            }
        }
    }
}
