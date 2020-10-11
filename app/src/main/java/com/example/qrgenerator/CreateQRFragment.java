package com.example.qrgenerator;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;
import androidmads.library.qrgenearator.QRGSaver;

import android.view.WindowManager;
import android.widget.Toast;

import static androidx.core.content.ContextCompat.getSystemService;

public class CreateQRFragment extends Fragment {
    ImageView pencil_icon_img,scan_icon_img;
    View pencilIcon,scanIcon;
    EditText text;

    public static CreateQRFragment newInstance() {
        CreateQRFragment fragment = new CreateQRFragment();
        return fragment;
    }

    @Override
    public void setUserVisibleHint(boolean isVisible) {
        super.setUserVisibleHint(isVisible);
        if(!isVisible){
            if(scan_icon_img!=null && pencil_icon_img!=null){
                scan_icon_img.setBackgroundColor(Color.parseColor("#ffffff"));
                pencil_icon_img.setBackgroundColor(0x00000000);
            }
            Log.d("QRG Visiblity : ","Gone");
        }
        else{
            Log.d("QRG Visiblity : ","Visible");
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.generate_qr_fragment, container, false);
        pencilIcon = view.findViewById(R.id.pencil_icon);
        scanIcon = view.findViewById(R.id.scan_icon);
        final ImageView image = view.findViewById(R.id.image);
        text = view.findViewById(R.id.text);
        final Button savebtn = view.findViewById(R.id.savebtn);
        final Button sharebtn  = view.findViewById(R.id.sharebtn);
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
                    Display display = getActivity().getWindowManager().getDefaultDisplay();
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
                    image.setImageResource(R.drawable.defaultpic);
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
                if(ActivityCompat.checkSelfPermission(getContext(),Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(getActivity(), "Storage Permission Required to Save the QR", Toast.LENGTH_SHORT).show();
                    ((MainScreenActivity)getActivity()).requestPermissions();
                }
                else if (text.getText().toString().trim().equals("")) {
                    Toast.makeText(getActivity(), "Enter Data", Toast.LENGTH_LONG).show();
                }
                else{
                    try {
                        //Toast.makeText(MainActivity.this, ""+savePath, Toast.LENGTH_LONG).show();
                        String inputValue = text.getText().toString().trim();
                        Display display = getActivity().getWindowManager().getDefaultDisplay();
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
                        result = success ? "Image Saved to \n" + savePath : "Image Not Saved";
                        Toast.makeText(getActivity(), result, Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        sharebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(text.getText().toString().trim().length()>0){
                    BitmapDrawable bitmapDrawable = (BitmapDrawable) image.getDrawable();
                    Bitmap bitmap1 = bitmapDrawable.getBitmap();
                    String bitmap_path = MediaStore.Images.Media.insertImage(getActivity().getContentResolver(), bitmap1, "" + text.getText().toString().trim(), null);
                    Uri uri = Uri.parse(bitmap_path);
                    Intent share = new Intent(Intent.ACTION_SEND);
                    share.setType("image/png");
                    share.putExtra(Intent.EXTRA_STREAM, uri);
                    startActivity(Intent.createChooser(share, "Share Using : "));
                }
                else{
                    Toast.makeText(getActivity(), "Nothing To Share", Toast.LENGTH_SHORT).show();
                }
            }
        });
        scan_icon_img = getActivity().findViewById(R.id.scan_icon_image);
        pencil_icon_img = getActivity().findViewById(R.id.pencil_icon_image);
        return view;
    }

    @Override
    public void onResume(){
        super.onResume();

    }
    @Override
    public  void onDestroy(){
        super.onDestroy();

    }
}