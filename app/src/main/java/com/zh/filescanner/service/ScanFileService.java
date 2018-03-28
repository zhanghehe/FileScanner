package com.zh.filescanner.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;

import com.zh.filescanner.event.ScanFileEvent;

import org.greenrobot.eventbus.EventBus;

import java.io.File;

/**
 * @Author: hehe
 * @Time: 2018/3/28 16:30
 * @Desc:
 */
public class ScanFileService extends IntentService {

    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, ScanFileService.class);
        return intent;
    }

    public ScanFileService() {
        super("ScanFileService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            scanSdCard();
            EventBus.getDefault().post(new ScanFileEvent(ScanFileEvent.TypeScanned));
        }
    }

    private void scanSdCard() {
        try {
            folderScan(Environment.getExternalStorageDirectory().getAbsolutePath());
        } catch (Exception e) {

        }
    }

    private void fileScan(String file) {
        sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + file)));
    }

    private void folderScan(String path) {
        File file = new File(path);

        if (file.exists() && file.isDirectory()) {
            File[] array = file.listFiles();
            if (array != null) {
                for (int i = 0; i < array.length; i++) {
                    File f = array[i];

                    if (f.isFile() && !f.isHidden()) {//FILE TYPE
                        String name = f.getName();

                        if (name.endsWith(".pdf")
                                || name.endsWith(".doc") || name.endsWith(".docx")
                                || name.endsWith(".xls") || name.endsWith(".xlsx")
                                || name.endsWith(".ppt") || name.endsWith(".pptx")
                                || name.endsWith(".wps") || name.endsWith(".pages") || name.endsWith(".key") || name.endsWith(".numbers")) {
                            fileScan(f.getAbsolutePath());
                        }
                    } else {//FOLDER TYPE
                        if (!f.isHidden()) {
                            folderScan(f.getAbsolutePath());
                        }
                    }
                }
            }
        }
    }
}
