package edu.asu.msse.mbatra3.covidtracker.Model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class Data {
    String dbName;
    public static Data object;

    private Data()
    {
    }

    public static Data getInstance()
    {
        if(object==null){
            object=new Data();
        }
        return object;
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public SQLiteDatabase initDB(Context context)
    {
       try{
            SQLiteDatabase db=SQLiteDatabase.openOrCreateDatabase(dbName,null);
            String sql="CREATE TABLE IF NOT EXISTS "+Data.getInstance().getDbName()+
                    "( XCOORDINATE VARCHAR," +
                    "YCOORDINATE VARCHAR, TIMESTAMP VARCHAR)";
            db.execSQL(sql);
            return db;

        }
        catch (Exception E)
        {
            E.printStackTrace();
        }
        return null;
    }

    public boolean insertData(String x,String y,String timeStamp,SQLiteDatabase db)
    {

        try {
            String sql="INSERT INTO"+Data.getInstance().getDbName();
        }catch(Exception e){
            e.printStackTrace();
        }
        return false;
    }

}
