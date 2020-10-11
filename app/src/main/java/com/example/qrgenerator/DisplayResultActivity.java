package com.example.qrgenerator;

import androidx.appcompat.app.AppCompatActivity;

import android.app.SearchManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class DisplayResultActivity extends AppCompatActivity {
    TextView content_display_area;
    Boolean isURL = false;
    String qr_content;
    Button copy, web_search;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_result);
        copy = findViewById(R.id.copy_btn);
        web_search = findViewById(R.id.websearch_btn);
        content_display_area = findViewById(R.id.display_content_text_view);
        qr_content = getIntent().getStringExtra("content");
        content_display_area.setText(qr_content.toString());
        String initial_4_chars = qr_content.substring(0,4);

        if(initial_4_chars.equals("www.") || initial_4_chars.equals("http")){
            isURL = true;
            web_search.setText("VISIT WEB-PAGE");
            if(initial_4_chars.equals("www.")){
                qr_content = "http:"+qr_content;
            }
        }
        copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("QR Contents",content_display_area.getText().toString());
                clipboard.setPrimaryClip(clip);
                Toast.makeText(DisplayResultActivity.this, "Contents copied to clipboard", Toast.LENGTH_LONG).show();
            }
        });
        web_search.setOnClickListener(new View.OnClickListener() {
            private String query = qr_content;
            @Override
            public void onClick(View v) {
                if(isURL) {
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(qr_content.toLowerCase()));
                    startActivity(i);
                }
                else{
                    Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
                    intent.putExtra(SearchManager.QUERY, query); // query contains search string
                    startActivity(intent);
                }
            }
        });
    }
}