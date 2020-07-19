package edu.asu.msse.mbatra3.covidtracker.Model;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class Data {
    String dbName;
    SQLiteDatabase db;

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

    // dbname and table name are same
    public SQLiteDatabase initDB(Context context)
        {
        // Add primary key and unique sr no auto increment
       try{
            db=SQLiteDatabase.openOrCreateDatabase(dbName,null);
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

    public boolean insertData(String x,String y,String timeStamp)
    {
        if(db==null)
        {
            return false;
        }else {

            try {
                String sql = "INSERT INTO" + Data.getInstance().getDbName() + "" +
                        "(XCOORDINATE,YCOORDINATE,TIMESTAMP) VALUES ('" + x + "','" + y + "','"
                        + timeStamp + "')";
                db.execSQL(sql);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    public String fetchData(String x,String y)
    {
        String result,sql;
        result="EMPTY";
        sql="SELECT * FROM "+Data.getInstance().getDbName()+"WHERE XCOORDINATE="+x+
            "AND YCOORDINATE="+y;
        Cursor c=db.rawQuery(sql,null);
        int xIndex,yIndex;
        xIndex=c.getColumnIndex("XCOORDINATE");
        yIndex=c.getColumnIndex("YCOORDINATE");
        c.moveToFirst();
        while(c!=null)
        {
            result=c.getString(xIndex)+" "+c.getString(yIndex);
            c.moveToNext();
        }
        if (result.equals("EMPTY")){
        result="No Result Found";}
        return result;
    }

    public boolean closeDb()
    {
        if(db==null){
            return true;
        }else{
            db.close();
        return true;
        }
    }
}
