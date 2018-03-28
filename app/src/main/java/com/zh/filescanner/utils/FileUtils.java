package com.zh.filescanner.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: hehe
 * @Time: 2018/3/28 16:28
 * @Desc:
 */

public class FileUtils {

    /**
     * 根据后缀名获取file
     *
     * @param context
     * @param extension
     */
    public static List<File> getSpecificTypeOfFileList(Context context, String[] extension) {
        List<File> files;
        //从外存中获取
        Uri fileUri = MediaStore.Files.getContentUri("external");
        //筛选列，这里只筛选了：文件路径和不含后缀的文件名
        String[] projection = new String[]{
                MediaStore.Files.FileColumns.DATA, MediaStore.Files.FileColumns.TITLE, MediaStore.Files.FileColumns.MIME_TYPE
        };
        //构造筛选语句
        StringBuffer selection = new StringBuffer();
        for (int i = 0; i < extension.length; i++) {
            if (i != 0) {
                selection.append(" OR ");
            }
            selection.append(MediaStore.Files.FileColumns.DATA + " LIKE '%." + extension[i] + "'");
        }
        //按时间递增顺序对结果进行排序;待会从后往前移动游标就可实现时间递减
        String sortOrder = MediaStore.Files.FileColumns.DATE_MODIFIED;
        //获取内容解析器对象
        ContentResolver resolver = context.getContentResolver();
        try {
            //获取游标
            Cursor cursor = resolver.query(fileUri, projection, selection.toString(), null, sortOrder);
            if (cursor == null) {
                return new ArrayList<>();
            } else {
                //游标从最后开始往前递减，以此实现时间递减顺序（最近访问的文件，优先显示）
                files = new ArrayList<>();
                if (cursor.moveToLast()) {
                    do {
                        //输出文件的完整路径
                        String data = cursor.getString(0);
                        File file = new File(data);
                        if (file.exists() && file.isFile() && file.length() > 0) {
                            files.add(file);
                        }
                    } while (cursor.moveToPrevious());
                }
                cursor.close();
                return files;
            }
        } catch (SecurityException e) {
            return new ArrayList<>();
        }

    }

}
