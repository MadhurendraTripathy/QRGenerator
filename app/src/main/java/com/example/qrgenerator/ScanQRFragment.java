package com.example.qrgenerator;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.zxing.Result;

import java.util.List;


public class ScanQRFragment extends Fragment{
    ImageView pencil_icon_img,scan_icon_img;
    CodeScanner mCodeScanner;
    CodeScannerView scannerView;
    View pencilIcon,scanIcon;

    public static ScanQRFragment newInstance() {
        ScanQRFragment fragment = new ScanQRFragment();
        return fragment;
    }

    @Override
    public void setUserVisibleHint(boolean isVisible) {
        super.setUserVisibleHint(isVisible);
        if(!isVisible){
            if(pencil_icon_img!=null && scan_icon_img!=null){
                pencil_icon_img.setBackgroundColor(Color.parseColor("#ffffff"));
                scan_icon_img.setBackgroundColor(0x00000000);
            }
            Log.d("Visiblity : ","Gone");
            if(scannerView!=null){
                mCodeScanner.stopPreview();
                mCodeScanner.releaseResources();
            }
        }
        else{
            Log.d("Visiblity : ","Visible");
            if(scannerView!=null){
                mCodeScanner.startPreview();
                Log.d("Scanner View : ","is not null");
            }
            else{
                Log.d("Scanner View : ","is null");
            }
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            Toast.makeText(getActivity(), "Camera Permission Required to Scan the QR", Toast.LENGTH_SHORT).show();
            ((MainScreenActivity)getActivity()).requestPermissions();
        }
        View view = inflater.inflate(R.layout.scan_qr_fragment, container, false);
        scannerView = view.findViewById(R.id.scannerView);
        mCodeScanner = new CodeScanner(getContext(),scannerView);
        mCodeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull Result result) {
                mCodeScanner.releaseResources();
                Intent intent = new Intent(getActivity(), DisplayResultActivity.class);
                String content = result.getText();
                intent.putExtra("content",content);
                startActivity(intent);
            }
        });
        scan_icon_img = getActivity().findViewById(R.id.scan_icon_image);
        pencil_icon_img = getActivity().findViewById(R.id.pencil_icon_image);
        return scannerView;
    }

    @Override
    public void onResume(){
        super.onResume();
        mCodeScanner.startPreview();
    }

    @Override
    public  void onPause(){
        super.onPause();
        mCodeScanner.releaseResources();
    }

    @Override
    public  void onDestroy(){
        super.onDestroy();
        mCodeScanner.releaseResources();
    }
}
