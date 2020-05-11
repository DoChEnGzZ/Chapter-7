package com.bytedance.videoplayer;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ture_main_activity extends AppCompatActivity {

    public Intent intent;
    public Intent intent2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ture_main_activity);
        Uri uri=Uri.parse(getVideoPath(R.raw.bytedance));
        intent=new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(uri,"video/*");
        Button button=findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(intent);
            }
        });
    }
    private String getVideoPath(int resId) {
        return "android.resource://" + this.getPackageName() + "/" + resId;
    }
}
