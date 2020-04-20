package com.example.qrgenerator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Parcelable;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;
import androidmads.library.qrgenearator.QRGSaver;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},0);
        }
        final ImageView image = findViewById(R.id.image);
        final EditText text = findViewById(R.id.text);
        final Button savebtn = findViewById(R.id.savebtn);
        final Button sharebtn  = findViewById(R.id.sharebtn);
        final String savePath = Environment.getExternalStorageDirectory().getPath() + "/MyQRCode/";
        text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String inputValue;
                inputValue = text.getText().toString();
                if(!inputValue.trim().equals("")){
                    WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);
                    Display display = manager.getDefaultDisplay();
                    Point point = new Point();
                    display.getSize(point);
                    int width = point.x;
                    int height = point.y;
                    int smallerDimension = width < height ? width : height;
                    smallerDimension = smallerDimension * 3 / 4;
                    QRGEncoder qrgEncoder = new QRGEncoder(inputValue, null, QRGContents.Type.TEXT, smallerDimension);
                    try {
                        Bitmap bitmap;
                        bitmap = qrgEncoder.encodeAsBitmap();
                        image.setImageBitmap(bitmap);
                    }
                    catch (Exception e){
                        //Toast.makeText(this, ""+e.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    image.setImageResource(R.drawable.ic_launcher_background);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        savebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean success;
                String result;
                //if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
                try {

                    //Toast.makeText(MainActivity.this, ""+savePath, Toast.LENGTH_LONG).show();
                    String inputValue = text.getText().toString().trim();
                    WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);
                    Display display = manager.getDefaultDisplay();
                    Point point = new Point();
                    display.getSize(point);
                    int width = point.x;
                    int height = point.y;
                    int smallerDimension = width < height ? width : height;
                    smallerDimension = smallerDimension * 3 / 4;
                    QRGEncoder qrgEncoder = new QRGEncoder(inputValue, null, QRGContents.Type.TEXT, smallerDimension);
                    Bitmap bitmap;
                    bitmap = qrgEncoder.encodeAsBitmap();
                    image.setImageBitmap(bitmap);
                    success = QRGSaver.save(savePath, inputValue, bitmap, QRGContents.ImageType.IMAGE_JPEG);
                    result = success ? "Image Saved to \n"+savePath : "Image Not Saved";
                    Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        sharebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(text.getText().toString().trim().length()>0){
                    BitmapDrawable bitmapDrawable = (BitmapDrawable) image.getDrawable();
                    Bitmap bitmap1 = bitmapDrawable.getBitmap();
                    String bitmap_path = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap1, "" + text.getText().toString().trim(), null);
                    Uri uri = Uri.parse(bitmap_path);
                    Intent share = new Intent(Intent.ACTION_SEND);
                    share.setType("image/png");
                    share.putExtra(Intent.EXTRA_STREAM, uri);
                    startActivity(Intent.createChooser(share, "Share Using : "));
                }
                else{
                    Toast.makeText(MainActivity.this, "Nothing To Share", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
