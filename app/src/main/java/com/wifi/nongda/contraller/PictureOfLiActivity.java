package com.wifi.nongda.contraller;

import android.app.Activity;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.wifi.nongda.MainActivity;
import com.wifi.nongda.R;
import com.wifi.nongda.view.DrawXY;
import com.wifi.nongda.view.XYChartBuilder;

import org.achartengine.GraphicalActivity;
import org.achartengine.GraphicalView;

/**
 * Created by 段碧伟 on 2016/1/28.
 */
public class PictureOfLiActivity extends Activity {
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
        XYChartBuilder builder = new XYChartBuilder(this,li);
        View view = builder.getView();
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.picture);
        view.invalidate();
        linearLayout.addView(view);
    }


}
