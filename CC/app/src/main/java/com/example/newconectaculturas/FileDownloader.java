package com.example.newconectaculturas;

import org.apache.http.params.HttpConnectionParams;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class FileDownloader {
    private static final int MEGABYTE = 1024 * 1024;

    public static void downloadfile(String fileUrl, File directory) {
        try {
//            URL conection
            URL url = new URL(fileUrl);
            HttpURLConnection urlConection = (HttpURLConnection) url.openConnection();
            urlConection.connect();
            //setting inputstream
            InputStream inputStream = urlConection.getInputStream();
            //setting output stream
            FileOutputStream fileOutputStream=new FileOutputStream(directory);
            int totalSize=urlConection.getContentLength();
            //defining an byte array
            byte [] buffer=new byte[MEGABYTE];
            int bufferLength=0;
            while((bufferLength=inputStream.read(buffer))>0){
                fileOutputStream.write(buffer,0,bufferLength);
            }
            fileOutputStream.close();

        }catch (MalformedURLException e){
            e.printStackTrace();
        }catch (FileNotFoundException e){
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
