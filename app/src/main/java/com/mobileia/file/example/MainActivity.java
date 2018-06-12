package com.mobileia.file.example;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
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

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import id.zelory.compressor.Compressor;
import me.echodev.resizer.Resizer;

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
                final File file = new File(list.get(0).getOriginalPath());
                MobileiaFile.getInstance().upload(file, new RestGenerator.OnFileUpload() {
                    @Override
                    public void onSuccess(com.mobileia.file.entity.File file) {
                        System.out.println("Mobileia: Success file: " + file.name);
                        System.out.println("Mobileia: Success file: " + file.path);
                        System.out.println("Mobileia: Success file: " + file.type);
                    }

                    @Override
                    public void onError() {
                        System.out.println("Mobileia: No se pudo seubir la imagen");
                    }
                });


                /*Glide.with(MainActivity.this).load(file).into(new SimpleTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {

                        Bitmap bitmap;
                        if(resource.getIntrinsicWidth() <= 0 || resource.getIntrinsicHeight() <= 0) {
                            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888); // Single color bitmap will be created of 1x1 pixel
                        } else {
                            bitmap = Bitmap.createBitmap(resource.getIntrinsicWidth(), resource.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
                        }

                        Canvas canvas = new Canvas(bitmap);
                        resource.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
                        resource.draw(canvas);
                        // Bitmap listo
                        File saveFile = storeImage(bitmap, new Date().getTime() + "_image");
                        if(saveFile == null){
                            return;
                        }

                        MobileiaFile.getInstance().upload(saveFile, new RestGenerator.OnFileUpload() {
                            @Override
                            public void onSuccess(com.mobileia.file.entity.File file) {
                                System.out.println("Mobileia: Success file: " + file.name);
                                System.out.println("Mobileia: Success file: " + file.path);
                                System.out.println("Mobileia: Success file: " + file.type);
                            }

                            @Override
                            public void onError() {
                                System.out.println("Mobileia: No se pudo seubir la imagen");
                            }
                        });


                        //File compressedImageFile = new Compressor(MainActivity.this).compressToFile(file);

                        File resizedImage = new Resizer(MainActivity.this)
                                .setTargetLength(1080)
                                .setQuality(80)
                                .setOutputFormat("JPEG")
                                .setOutputFilename(new Date().getTime() + "_image")
                                //.setOutputDirPath(storagePath)
                                .setSourceImage(bitmap)
                                .getResizedFile();



                    }
                });*/



            }

            @Override
            public void onError(String s) {

            }
        });
    }

    private File storeImage(Bitmap imageData, String filename) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        imageData.compress(Bitmap.CompressFormat.JPEG, 80, bytes);

        File ExternalStorageDirectory = Environment.getExternalStorageDirectory();
        File file = new File(ExternalStorageDirectory + File.separator + filename);

        FileOutputStream fileOutputStream = null;
        try {
            file.createNewFile();
            fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(bytes.toByteArray());

            ContentResolver cr = getContentResolver();
            String imagePath = file.getAbsolutePath();
            String name = file.getName();
            String description = "My bitmap created by Android-er";
            String savedURL = MediaStore.Images.Media
                    .insertImage(cr, imagePath, name, description);

            Toast.makeText(MainActivity.this,
                    savedURL,
                    Toast.LENGTH_LONG).show();

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            if(fileOutputStream != null){
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }

        return file;


        //get path to external storage (SD card)
        /*String iconsStoragePath = Environment.getExternalStorageDirectory() + "/ShowProde/images/";
        File sdIconStorageDir = new File(iconsStoragePath);

        //create storage directories, if they don't exist
        sdIconStorageDir.mkdirs();

        try {
            String filePath = sdIconStorageDir.toString() + filename;
            FileOutputStream fileOutputStream = new FileOutputStream(filePath);

            BufferedOutputStream bos = new BufferedOutputStream(fileOutputStream);

            //choose another format if PNG doesn't suit you
            imageData.compress(Bitmap.CompressFormat.JPEG, 80, bos);

            bos.flush();
            bos.close();

        } catch (FileNotFoundException e) {
            Log.w("TAG", "Error saving image file: " + e.getMessage());
            return null;
        } catch (IOException e) {
            Log.w("TAG", "Error saving image file: " + e.getMessage());
            return null;
        }

        return sdIconStorageDir;*/
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
