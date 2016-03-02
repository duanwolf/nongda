package com.wifi.nongda.view;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelXorXfermode;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import com.wifi.nongda.Datebase.DataDB;
import com.wifi.nongda.model.DataInfo;

import java.util.ArrayList;

/**
 * Created by 段碧伟 on 2016/2/2.
 */
public class DrawXY extends View {
    private static final int PADDING_X = 50;
    private static final int PADDING_Y = 100;
    private static final int PADDING_RX = 60;
    private static final int PADDING_RY = 210;
    private static final int X = 0;
    private static final int Y = 1;
    int screenWidth;
    int screenHeight;
    SQLiteDatabase db;
    DataDB helper;
    Context context;
    int isli;
    public DrawXY(Context context, int isli) {
        super(context);
        this.context = context;
        this.isli = isli;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        DisplayMetrics dm = new DisplayMetrics();
        dm = getResources().getDisplayMetrics();

        screenWidth = dm.widthPixels;
        screenHeight = dm.heightPixels;

        Paint p = new Paint();
        p.setStyle(Paint.Style.STROKE);
        p.setStrokeWidth(10);
        canvas.drawLine(PADDING_X, PADDING_Y, screenWidth - PADDING_RX, PADDING_Y, p);
        canvas.drawLine(PADDING_X, PADDING_Y, PADDING_X, screenHeight - PADDING_RY, p);

        Path path = new Path();
        path.moveTo(screenWidth - 60, 70);
        path.lineTo(screenWidth - 60, 130);
        path.lineTo(screenWidth - 20, 100);
        path.close();
        p.setStyle(Paint.Style.FILL);
        canvas.drawPath(path, p);

        path.moveTo(80, screenHeight - 210);
        path.lineTo(20, screenHeight - 210);
        path.lineTo(50, screenHeight - 180);
        path.close();
        canvas.drawPath(path, p);

        float map1[][] = changeToScreen(canvas, p, isli);
        for (int i = 0; i < map1.length - 1; i++){
            canvas.drawLine(map1[i][X], map1[i][Y], map1[i + 1][X], map1[i + 1][Y], p);
        }
    }

    public float[][] changeToScreen(Canvas canvas, Paint p, int isli) {
        helper = new DataDB(context);
        float max_li = 0;
        float max_shendu = 0;
        ArrayList<DataInfo> infos = helper.query();
        float[][] map1 = new float[infos.size()][2];
        for (int i = 0; i < 20; i++) {
            switch (isli) {
                case 0:
                    map1[i][X] = infos.get(i).getLi();
                    map1[i][Y] = infos.get(i).getShenDu();
                case 1:
                    map1[i][X] = infos.get(i).getShenDu();
                    map1[i][Y] = infos.get(i).getYaQiang();
            }

            if (max_li < map1[i][X]) max_li = map1[i][X];
            if (max_shendu < map1[i][Y]) max_shendu = map1[i][Y];
        }
        int max_x = (int) max_li +1;
        int max_y = (int) max_shendu +1;
        float an_x = (float)(screenWidth - 120) / max_x;
        float an_y = (float) (screenHeight - 350)/ max_y;
        for (int i = 0; i < infos.size(); i++) {
            map1[i][X] = map1[i][X] * an_x + 50;
            map1[i][Y] = map1[i][Y] * an_y + 100;
        }
        for (int i = 0; i < 6; i++) {
            canvas.drawLine(PADDING_X + (max_x * an_x / 5) * i,PADDING_Y, PADDING_X + (max_x * an_x / 5) * i,
                    PADDING_Y - 10, p);
            canvas.drawLine(PADDING_X, PADDING_Y + (max_y * an_y / 5) * i, PADDING_X - 10,
                    PADDING_Y + (max_y * an_y / 5) * i, p);
        }
        return map1;
    }
}
