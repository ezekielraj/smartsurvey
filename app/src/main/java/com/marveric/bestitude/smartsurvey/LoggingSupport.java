package com.marveric.bestitude.smartsurvey;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;

public class LoggingSupport {


    private final String filename = "smartsurvey.log";
    private static String dirname;// = this.getFilesDir();
    private static File file;//
    private static LoggedinActivity liactivity;
    LoggingSupport(LoggedinActivity lia){
        liactivity = lia;
        dirname = "/storage/emulated/0/";//lia.getFilesDir();



        file = new File(liactivity.getFilesDir(),filename);//dirname+ filename);
        if(BuildConfig.DEBUG) Log.i("dirname", dirname.toString());
    }

    public void WriteExcepData(Exception txt){
      //  FileOutputStream outputStream;

        try {
            PrintStream ps = new PrintStream(this.file);
            txt.printStackTrace(ps);
            ps.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public void writeStringData(String s){
        FileOutputStream outputStream;

        try {
            outputStream = liactivity.openFileOutput(filename, Context.MODE_APPEND);
            outputStream.write(s.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
