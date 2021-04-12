package com.example.fakenewsdetectorv2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.os.Bundle;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class Result extends AppCompatActivity {
    String myResponse = "";
    JSONObject jsonObj;
    ClipData clipdata;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        ClipboardManager clipboardmanager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        clipdata = clipboardmanager.getPrimaryClip();
        if(clipdata != null) {
            clipdata = clipboardmanager.getPrimaryClip();
            ClipData.Item item = clipdata.getItemAt(0);
            item = clipdata.getItemAt(0);
            String S1 = item.getText().toString();
            getrequest(S1);
        }

    }
    public void getrequest(String S){
        OkHttpClient client = new OkHttpClient();
        String url = "https://fakenewsdetector.azurewebsites.net/api/Function01?paragraph="+S;
        Request request = new Request.Builder()
                .url(url)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    myResponse = response.body().string();
                    try {
                        jsonObj = new JSONObject(myResponse);
                        String S_ratio = jsonObj.getString("ratio");
                        String key_phrase = jsonObj.getString("Key_phrase");
                        String language = jsonObj.getString("language");
                        String S_url = jsonObj.getString("url");


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    Result.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                        }
                    });
                }
            }
        });

    }
}