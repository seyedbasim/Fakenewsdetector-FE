package com.example.fakenewsdetectorv2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.util.Linkify;
import android.view.View;
import android.widget.Button;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.example.fakenewsdetectorv2.notification.CHANNEL_1_ID;

public class MainActivity extends AppCompatActivity {
    private NotificationManagerCompat notificationManager;
    private TextView mTextViewResult,mTextViewResult2,mTextViewResult3;
    String myResponse = "";
    JSONObject jsonObj;
    //ClipData clipdata = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextViewResult = findViewById(R.id.text_view_result);
        mTextViewResult2 = findViewById(R.id.text_view_result2);
        mTextViewResult3 = findViewById(R.id.text_view_result3);
        //-----------------------------------------------------------------
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent mainserviceintent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        notificationManager = NotificationManagerCompat.from(this);
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_1_ID)
               .setSmallIcon(R.drawable.ic_baseline_open_in_new_24)
               .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setContentIntent(mainserviceintent)
               .build();
        notificationManager.notify(1,notification);
        //-----------------------------------------------------------------
        //ClipboardManager clipboardmanager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        //clipdata = clipboardmanager.getPrimaryClip();
        //if(clipdata != null) {
        //    clipdata = clipboardmanager.getPrimaryClip();
        //    ClipData.Item item = clipdata.getItemAt(0);
        //    item = clipdata.getItemAt(0);
        //    String S1 = item.getText().toString();
        //   getrequest(S1);
        //}

    }
    public void startStartedService(View view) {

        //Intent intent = new Intent(MainActivity.this, MainService.class);
        //intent.putExtra("sleepTime", 10);
        //startService(intent);

        ClipboardManager clipboardmanager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);

        ClipData clipdata = clipboardmanager.getPrimaryClip();
        ClipData.Item item = clipdata.getItemAt(0);

        String para = item.getText().toString();

        getrequest(para);

    }

    public void stopStartedService(View view) {
        Intent intent1 = new Intent(MainActivity.this, MainService.class);
        stopService(intent1);
    }

    public void openBrowser(View view) throws JSONException {
        if(myResponse != "") {
            jsonObj = new JSONObject(myResponse);
            String myURL = jsonObj.getString("url");
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(myURL));
            if (myURL.startsWith("http")) {
                startActivity(browserIntent);
            } else {
                mTextViewResult.setText("Source Not Found!");
               // mTextViewResult2.setText("");
               // mTextViewResult3.setText("");
            }
        }else{
            mTextViewResult.setText("Clipboard is Empty");
           // mTextViewResult2.setText("");
           // mTextViewResult3.setText("");
        }
    }

    public void getrequest(String S){
        OkHttpClient client = new OkHttpClient();
        String url = "https://fndv1apim.azure-api.net/fakenewsdetector/Function01?paragraph="+S;
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Ocp-Apim-Subscription-Key", "702e082130904db8a1787d6d0929c519")
                .addHeader("Ocp-Apim-Trace", "true")
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
                        float f=Float.parseFloat(S_ratio);
                        //String key_phrase = jsonObj.getString("Key_phrase");
                        //String language = jsonObj.getString("language");
                        String S_url = jsonObj.getString("url");
                        if(S_url.startsWith("http")){
                            String[] arrOfStr = S_url.split("/", 2);
                            String[] arrOfStr2 = arrOfStr[1].split("/",2);
                            String[] arrOfStr3 = arrOfStr2[1].split("/",2);
                            String Source = arrOfStr3[0];
                            if(f>0.5){
                                mTextViewResult.setText(Source);
                            }else{
                                mTextViewResult.setText("Not Exact Match");
                            }

                        }else{
                            mTextViewResult.setText(S_url);
                        }
                        //  if (S_url.startsWith("http")) {
                           // mTextViewResult.setText(S_ratio);
                         //   mTextViewResult2.setText(key_phrase);
                        //    mTextViewResult3.setText(language);
                     //   } else {
                         //   mTextViewResult.setText("Fake News");
                          //  mTextViewResult2.setText("");
                         //   mTextViewResult3.setText("");
                    //    }
                        //String myURL = myResponse;
                        //mTextViewResult.setText(myURL);
                        //float f=Float.parseFloat(S_ratio);
                        //f = f*100;
                        //String ratio= Float.toString(f);



                        //String Details = "Source: " + S_url; //+ "\nKey Phrases:" + key_phrase + "\nLanguage:" + language;

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                        }
                    });
                }
            }
        });

    }


}