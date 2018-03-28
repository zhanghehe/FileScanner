package com.zh.filescanner;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.zh.filescanner.event.ScanFileEvent;
import com.zh.filescanner.service.ScanFileService;
import com.zh.filescanner.utils.FileUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class MainActivity extends AppCompatActivity {
    private LinearLayout ll;
    private Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EventBus.getDefault().register(this);
        ll = (LinearLayout) findViewById(R.id.ll);
        btn = (Button) findViewById(R.id.btn);
        startService(ScanFileService.newIntent(MainActivity.this));
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        FileUtils.getSpecificTypeOfFileList(MainActivity.this,new String[]{"doc"});
                    }
                }).start();
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void sacnFileFinish(ScanFileEvent event) {
        Snackbar.make(ll, "扫描完成", Snackbar.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
