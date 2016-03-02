package com.wifi.nongda.Datebase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import com.wifi.nongda.model.DataInfo;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;


/**
 * Created by 段碧伟 on 2015/12/22.
 */
public class DataDB extends SQLiteOpenHelper{
    private static final String TAG = "database";
    private static final String DATABASE_NAME="RECORD.db";
    private static final String TABLE_NAME = "Record_table";
    private static final String YAQIANG="ya_qiang";
    private static final String LI="li";
    private static final String SHEN_DU="shen_du";
    private static final String RECORD_ID="id";
    private static final int VERSION= 1;
    private Context mContext;
    public DataDB(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
        mContext = context;
    }
    @Override
    public void onCreate(SQLiteDatabase db){
        String sql = "create table " + TABLE_NAME + " ("  + RECORD_ID + " INTEGER primary key autoincrement, " +
                  LI + " real, " + SHEN_DU + " real," + YAQIANG + " real )";
        db.execSQL(sql);
        Toast.makeText(mContext,"建立数据库表",Toast.LENGTH_LONG).show();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "DROP TABLE IF EXISTS " + TABLE_NAME;
        db.execSQL(sql);
        onCreate(db);
    }


    public void insert(DataInfo d){
          SQLiteDatabase db = this.getWritableDatabase();
          ContentValues cv = new ContentValues();
          cv.put(LI, d.getLi()); // 收支String 类型  sqlite用 text类型
          cv.put(SHEN_DU, d.getShenDu()); // 金额 float                      real
          cv.put(YAQIANG, d.getYaQiang()); // 种类 String                    text
          db.insert("Record_table", null, cv);

    }

    public void delete(UUID id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("info", "id ?", new String[]{id.toString()});
    }

    public ArrayList<DataInfo> query() {
        ArrayList<DataInfo> dataInfos = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, new String [] {LI, SHEN_DU, YAQIANG}
        , null, null, null, null,  null );

        if (cursor != null) {
            while (cursor.moveToNext()) {
                DataInfo d = new DataInfo();
                d.setLi(cursor.getFloat(cursor.getColumnIndex(LI)));
                d.setShenDu(cursor.getFloat(cursor.getColumnIndex(SHEN_DU)));
                d.setYaQiang(cursor.getFloat(cursor.getColumnIndex(YAQIANG)));
                dataInfos.add(d);
            }

        }
        return dataInfos;
    }
}
