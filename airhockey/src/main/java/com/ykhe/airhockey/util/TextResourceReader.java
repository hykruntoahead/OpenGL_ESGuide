package com.ykhe.airhockey.util;

import android.content.Context;
import android.content.res.Resources;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * author: ykhe
 * date: 20-12-2
 * email: ykhe@grandstream.cn
 * description:
 */
public class TextResourceReader {
    /**
     * 读取res中的文本
     * @param context
     * @param resourceId
     * @return
     */
    public static String readTextFileFromResource(Context context, int resourceId) {
        StringBuilder body = new StringBuilder();
        try {
            InputStream inputStream = context.getResources().openRawResource(resourceId);
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String nextLine;
            while ((nextLine = bufferedReader.readLine()) != null) {
                body.append(nextLine);
                body.append('\n');
            }
        }catch (IOException e){
            throw new RuntimeException("Could not open resource :"+resourceId,e);
        }catch (Resources.NotFoundException exception){
            throw new RuntimeException("Resource not found :"+resourceId,exception);
        }
        return body.toString();
    }
}
