package com.chi.bnbserv.util;

import java.io.*;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import com.chi.bnbserv.exception.FileNotFoundException;

/**
 * @Description: 檔案處理工具
 */
public class FileUtil {
    public static byte[] getByteFromFileNIO(String fileName) throws IOException {
        File file = new File(fileName);
        System.out.println(file.getAbsolutePath());
        if(!file.exists()) {
            throw new FileNotFoundException(String.format("指定檔案不存在", fileName));
        }

        FileChannel channel = null;
        FileInputStream fs = null;
        try {
            fs = new FileInputStream(file);
            channel = fs.getChannel();
            ByteBuffer byteBuffer = ByteBuffer.allocate((int)channel.size());
            channel.read(byteBuffer);
            return byteBuffer.array();
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        } finally {
            try {
                if(channel!= null) channel.close();
                if(fs!= null) fs.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
