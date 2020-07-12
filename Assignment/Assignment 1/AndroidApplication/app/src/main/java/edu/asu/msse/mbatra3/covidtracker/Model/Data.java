package edu.asu.msse.mbatra3.covidtracker.Model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class Data {

    public boolean initDB(String dbName, Context context)
    {
        boolean isDbInit=false;
        try{
            SQLiteDatabase db=SQLiteDatabase.openOrCreateDatabase(dbName,null);
            String sql="CREATE TABLE IF NOT EXISTS "+dbName+"( XCOORDINATE VARCHAR," +
                    "YCOORDINATE VARCHAR, TIMESTAMP VARCHAR)";
            db.execSQL(sql);
            isDbInit= true;

        }
        catch (Exception E)
        {
            E.printStackTrace();
        }
        return isDbInit;
    }

    public boolean insertData(String x,String y,String timeStamp)
    {

        try {

        }catch(Exception e){
            e.printStackTrace();
        }
        return false;
    }

}
