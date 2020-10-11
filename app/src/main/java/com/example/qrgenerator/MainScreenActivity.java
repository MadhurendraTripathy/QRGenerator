package com.example.qrgenerator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Camera;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Adapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.List;

public class MainScreenActivity extends AppCompatActivity {
    LinearLayout scanIcon,pencilIcon;
    ImageView pencil_icon_img,scan_icon_img;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestPermissions();
        setContentView(R.layout.activity_main_screen);
        scanIcon = findViewById(R.id.scan_icon);
        pencilIcon = findViewById(R.id.pencil_icon);
        scan_icon_img = findViewById(R.id.scan_icon_image);
        pencil_icon_img = findViewById(R.id.pencil_icon_image);
        ViewPager viewPager = findViewById(R.id.viewPager);
        FragmentManager fm = getSupportFragmentManager();
        viewPagerAdapter viewpageradapter = new viewPagerAdapter(fm);
        viewPager.setAdapter(viewpageradapter);
        scan_icon_img.setBackgroundColor(Color.parseColor("#ffffff"));
        viewPager.setCurrentItem(1);

        pencilIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(0);
                switchActionBarColor(0);
            }
        });

        scanIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(1);
                switchActionBarColor(1);
            }
        });
    }

    public class viewPagerAdapter extends FragmentPagerAdapter{
        public viewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            super.destroyItem(container, position, object);
        }

        @Override
        public Fragment getItem(int position) {
            switch(position){
                case 0:{
                    return CreateQRFragment.newInstance();
                }
                case 1:{
                    return ScanQRFragment.newInstance();
                }
            }
            return null;
        }

        @Override
        public int getCount() {
            return 2;
        }

    }

    void requestPermissions(){
        if (ContextCompat.checkSelfPermission(MainScreenActivity.this, Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainScreenActivity.this, new String[]{Manifest.permission.CAMERA},1);
        }
        if (ContextCompat.checkSelfPermission(MainScreenActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainScreenActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},0);
        }
    }

    public void switchActionBarColor(int current_selection){
        switch(current_selection){
            case 0:{
                pencil_icon_img.setBackgroundColor(Color.parseColor("#ffffff"));
                scan_icon_img.setBackgroundColor(0x00000000);
                break;

            }
            case 1:{
                scan_icon_img.setBackgroundColor(Color.parseColor("#ffffff"));
                pencil_icon_img.setBackgroundColor(0x00000000);
                break;
            }
        }
    }

}