package com.zh.filescanner.event;

/**
 * @Author: hehe
 * @Time: 2018/3/28 16:37
 * @Desc:
 */

public class ScanFileEvent {
    public static final int TypeScanning = 0;
    public static final int TypeScanned = 1;
    public int type;

    public ScanFileEvent(int type) {
        this.type = type;
    }
}
