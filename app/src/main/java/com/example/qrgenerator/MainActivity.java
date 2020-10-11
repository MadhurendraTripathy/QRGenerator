package com.example.qrgenerator;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Parcelable;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Pair;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;
import androidmads.library.qrgenearator.QRGSaver;

public class MainActivity extends AppCompatActivity {
    ImageView qr_image;
    TextView branding_top,branding_bottom;
    Animation top_to_bottom,bottom_to_top,fade_in;
    static int SPLASH_TIME = 850;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        branding_top = findViewById(R.id.branding_top);
        qr_image = findViewById(R.id.qr_image);
        branding_bottom = findViewById(R.id.branding_bottom);
        top_to_bottom= AnimationUtils.loadAnimation(this,R.anim.top_to_bottom_animation);
        bottom_to_top = AnimationUtils.loadAnimation(this,R.anim.bottom_to_top_animation);
        fade_in = AnimationUtils.loadAnimation(this,R.anim.fade_in);
        qr_image.setAnimation(fade_in);
        branding_top.setAnimation(top_to_bottom);
        branding_bottom.setAnimation(bottom_to_top);
        new Handler().postDelayed(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void run() {
                Intent intent = new Intent(MainActivity.this, MainScreenActivity.class);
//                Pair[] pairs = new Pair[3];
//                pairs[0] = new Pair<View,String>(branding_top,"top_to_bottom");
//                pairs[1] = new Pair<View,String>(qr_image,"fade_in");
//                pairs[2] = new Pair<View,String>(branding_bottom,"bottom_to_top"); //just to ensure that logo's don't push down before going up
                if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
//                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this,pairs);
                    startActivity(intent);
                    finish();
                }
                else{
                    startActivity(intent);
                    finish();
                }
            }
        },SPLASH_TIME);
    }
}
