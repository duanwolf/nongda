package com.wifi.nongda.contraller;

import android.app.Activity;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.LinearLayout;

import com.wifi.nongda.Datebase.DataDB;
import com.wifi.nongda.MainActivity;
import com.wifi.nongda.R;
import com.wifi.nongda.model.DataInfo;
import com.wifi.nongda.view.DrawXY;
import com.wifi.nongda.view.XYChartBuilder;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalActivity;
import org.achartengine.GraphicalView;

import android.os.Handler;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by 段碧伟 on 2016/1/28.
 */
public class PictureOfLiActivity extends Activity {
    Handler handler;
    TimerTask task;
    Timer timer;
    int count = 1;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.picture);
    /*    int is_li = (int)getIntent().getSerializableExtra(MainActivity.ISLI);
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.picture);
        final DrawXY xy = new DrawXY(this, is_li);
        xy.invalidate();
        linearLayout.addView(xy); */
        int li = (int) getIntent().getSerializableExtra(MainActivity.ISLI);
        final XYChartBuilder builder = new XYChartBuilder(this,li);
        final View view = builder.getView();
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.picture);
        view.invalidate();
        linearLayout.addView(view);
        DataDB dataDB = new DataDB(this);
        ArrayList<DataInfo> infos = dataDB.query();
        final int sum = infos.size();
        final float column[][] = new float[sum][2];
        switch (li) {
            case 0:
                for (int i = 0 ; i < sum; i++) {
                    column[i][0] = infos.get(i).getShenDu();
                    column[i][1] = infos.get(i).getYaQiang();
                }
                break;
            case 1:
                for (int i = 0 ; i < sum; i++) {
                    column[i][0] = infos.get(i).getLi();
                    column[i][1] = infos.get(i).getShenDu();
                }
                break;
        }
        handler = new Handler() {
            @Override
            public void handleMessage(Message message){
                builder.initData(column[count][0], column[count][1]);
                view.invalidate();
                super.handleMessage(message);
            }

        };
        timer = new Timer();
        task = new TimerTask() {
            @Override
            public void run() {
                Message msg = new Message();
                msg.what = 1;
                count++;
                if (count < sum)
                    handler.sendMessage(msg);
                else System.gc();
            }
        };
        timer.schedule(task, 500, 500);

    }


}
