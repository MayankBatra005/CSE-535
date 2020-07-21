package edu.asu.msse.mbatra3.covidtracker.utilities;

import android.content.Context;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;

import edu.asu.msse.mbatra3.covidtracker.Model.Data;

public class FileIoOperations {

    public String readDbFile(Context context) throws Exception{
        // path     context.getFilesDir().getPath()+"/"+dbName+".db"
        File myFile = new File(context.getFilesDir().getPath()+"/"+ Data.getInstance()
                .getDbName()+".db");
        FileInputStream fin = null;
        fin = new FileInputStream(myFile);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] b = new byte[1024];
        int bytesRead;
        while ((bytesRead=fin.read(b)) != -1) {
            bos.write(b, 0, bytesRead);
        }
        byte[] fileContent = bos.toByteArray();
        String s = new String(fileContent);
        Log.i("DB in String form",s);
        return s;
    }
}
