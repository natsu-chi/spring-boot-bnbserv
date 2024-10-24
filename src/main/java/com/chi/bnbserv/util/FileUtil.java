package com.chi.bnbserv.util;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.zip.GZIPInputStream;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;

import com.chi.bnbserv.exception.FileNotFoundException;

/**
 * @Description: 檔案處理工具
 */
public class FileUtil {
    private static final int BUFFER_SIZE = 1024;

    public static byte[] getByteFromFileNIO(String fileName) throws IOException {
        File file = new File(fileName);
        if (!file.exists()) {
            // System.out.println(file.getAbsolutePath());
            throw new FileNotFoundException(String.format("指定檔案不存在", fileName));
        }

        FileChannel channel = null;
        FileInputStream fs = null;
        try {
            fs = new FileInputStream(file);
            channel = fs.getChannel();
            ByteBuffer byteBuffer = ByteBuffer.allocate((int) channel.size());
            channel.read(byteBuffer);
            return byteBuffer.array();
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        } finally {
            try {
                if (channel != null)
                    channel.close();
                if (fs != null)
                    fs.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 解壓縮 gzip 檔案
     * 
     * @param input
     * @param output
     */
    public static void decompressGzip(File input, File output) throws IOException {
        try (GZIPInputStream in = new GZIPInputStream(new FileInputStream(input))) {
            try (FileOutputStream out = new FileOutputStream(output)) {
                byte[] buffer = new byte[BUFFER_SIZE];
                int len;
                while ((len = in.read(buffer)) != -1) {
                    out.write(buffer, 0, len);
                }
            }
        }
    }

    /**
     * 解壓縮 tar.gz 到指定資料夾
     * 
     * @param tarGzFile 路徑
     * @param destDir   目的資料夾 (沒有就新增)
     */
    public static void extractTarGZ(File tarGzFile, String destDir) throws IOException {
        GzipCompressorInputStream gzipIn = new GzipCompressorInputStream(new FileInputStream(tarGzFile));
        try (TarArchiveInputStream tarIn = new TarArchiveInputStream(gzipIn)) {
            TarArchiveEntry entry;

            while ((entry = (TarArchiveEntry) tarIn.getNextEntry()) != null) {
                if (entry.isDirectory()) {
                    File f = new File(destDir + "/" + entry.getName());
                    boolean created = f.mkdirs();
                    if (!created) {
                        System.out.printf("Unable to create directory '%s', during extraction of archive contents.\n",
                                f.getAbsolutePath());
                    }
                } else {
                    int count;
                    byte[] data = new byte[BUFFER_SIZE];
                    FileOutputStream fos = new FileOutputStream(destDir + "/" + entry.getName(), false);
                    try (BufferedOutputStream dest = new BufferedOutputStream(fos, BUFFER_SIZE)) {
                        while ((count = tarIn.read(data, 0, BUFFER_SIZE)) != -1) {
                            dest.write(data, 0, count);
                        }
                    }
                }
            }
        }
    }

    /**
     * 追加寫入文字至指定檔案 (無檔案就新增)
     * 
     * @param content  寫入內容
     * @param filePath 檔案路徑
     * @throws IOException
     */
    public static void writeFile(String content, String filePath) throws IOException {
        FileWriter fw;
        BufferedWriter bfw;
        if (new File(filePath).exists()) {
            fw = new FileWriter(filePath);
        } else {
            fw = new FileWriter(filePath, true);
        }
        bfw = new BufferedWriter(fw);
        bfw.write(content); // 寫入資料
        bfw.newLine(); // 新增一行
        bfw.flush();
        bfw.close();
    }

}
