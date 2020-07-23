package edu.asu.msse.mbatra3.covidtracker.Model;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

public class Data {
    String dbName;
    SQLiteDatabase db=null;
//    private static String DB_PATH = Context.getFilesDir().getPath();

    public SQLiteDatabase getDb() {
        return db;
    }

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

    public void setDbName(Context context,String dbName) {
       // this.dbName = context.getFilesDir().getPath()+"/"+dbName+".db";
        this.dbName=dbName;
        Log.i("dbname",this.dbName);
    }

    // dbname and table name are same
    public boolean initDB(Context context)
        {
        // Add primary key and unique sr no auto increment
       try{
            db=context.openOrCreateDatabase(context.getFilesDir().getPath()+"/"+dbName+".db",
                    context.MODE_PRIVATE,null);
            String sql="CREATE TABLE IF NOT EXISTS "+Data.getInstance().getDbName()+
                    "( XCOORDINATE VARCHAR," +
                    "YCOORDINATE VARCHAR, TIMESTAMP VARCHAR,id INTEGER PRIMARY KEY)";
            db.execSQL(sql);
           boolean status=true;
//           Log.i("DB init status",""+status);
            return true;

        }
        catch (Exception E)
        {
            E.printStackTrace();
        }
        return false;
    }

    public boolean insertData(String x,String y,String timeStamp)
    {
        if(db==null)
        {
            return false;
        }else {

            try {
                String sql = "INSERT INTO " + Data.getInstance().getDbName() + "" +
                        "(XCOORDINATE,YCOORDINATE,TIMESTAMP) VALUES ('" + x + "','" + y + "','"
                        + timeStamp + "')";
                db.execSQL(sql);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    // fetching data for specific coordinates
    public String fetchData(String x,String y)
    {
        String result,sql;
        result="EMPTY";

        sql="SELECT * FROM "+Data.getInstance().getDbName()+" WHERE XCOORDINATE = "+x+
            " AND YCOORDINATE = "+y;
        String countSql="SELECT COUNT(*) FROM "+Data.getInstance().getDbName();
        Cursor counter=db.rawQuery(countSql,null);
        int count=counter.getCount();
       // String[] results= new String[count];
        ArrayList<String> results= new ArrayList<>();
        Cursor c=db.rawQuery(sql,null);
        int xIndex,yIndex;
        xIndex=c.getColumnIndex("XCOORDINATE");
        yIndex=c.getColumnIndex("YCOORDINATE");
        c.moveToFirst();
        int counting=c.getCount();
        int j=0;
        while(j<counting)
        {
         //   result=c.getString(xIndex)+" "+c.getString(yIndex);
            results.add(c.getString(xIndex)
                    +"&&"+c.getString(yIndex));
            Log.i("internal Results",results.get(j));
            c.moveToNext();
            j++;
        }

       int i=0;
        while(i<results.size())
        {
            Log.i("Result "+i,results.get(i));
        i++;
        }

        if (result.equals("EMPTY")){
        result="No Result Found";}

        Log.i("Count",""+count);
        return result;

    }

    // fetching all data points
    public ArrayList<String> fetchData2()
    {
        ArrayList<String> results= new ArrayList<>();
        String sql=" SELECT * FROM "+Data.getInstance().getDbName();
        Cursor c=db.rawQuery(sql,null);
        int xIndex,yIndex;
        xIndex=c.getColumnIndex("XCOORDINATE");
        yIndex=c.getColumnIndex("YCOORDINATE");
        c.moveToFirst();
        int counting=c.getCount();
        int j=0;
        while(j<counting)
        {
            //   result=c.getString(xIndex)+" "+c.getString(yIndex);
            results.add(c.getString(xIndex)
                    +"&&"+c.getString(yIndex));
            Log.i("internal Results",results.get(j));
            c.moveToNext();
            j++;
        }

        return results;
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
