package com.example.newconectaculturas;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;

import java.io.File;
import java.io.IOException;

public class DownloadPdf extends AsyncTask<String,Void,Void> {
    Context ctx;


    @Override
    protected Void doInBackground(String... strings) {
//        getting name and url from file
        String fileUrl=strings[0];
        String fileName=strings[1];
//        reference for external storage
        String getExternalStorage= Environment.getExternalStorageDirectory().toString();
//        get the file location
        File folder=new File(getExternalStorage,"/Saberes/Textos");
        folder.mkdir();
        File pdffile=new File(folder,fileName);
        try{
            pdffile.createNewFile();
        }catch (IOException e){
            e.printStackTrace();
        }
//        file downloading
        FileDownloader.downloadfile(fileUrl,pdffile);
        return null;
    }
}
