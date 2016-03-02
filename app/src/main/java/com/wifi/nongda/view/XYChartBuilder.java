package com.wifi.nongda.view;

import android.content.Context;
import android.graphics.Color;
import android.view.View;

import com.wifi.nongda.Datebase.DataDB;
import com.wifi.nongda.model.DataInfo;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import java.util.ArrayList;

/**
 * Created by 段碧伟 on 2016/2/5.
 */
public class XYChartBuilder {
    private static final int LI = 0;
    private static final int SHENDU = 1;
    private static final int YAQIANG = 2;
    protected XYMultipleSeriesDataset mDataSet;
    protected XYMultipleSeriesRenderer mRenderer;
    private GraphicalView view;
    int li;
    private Context context;

    public XYChartBuilder(Context context, int li) {
        this.context = context;
        mDataSet = new XYMultipleSeriesDataset();
        mRenderer = new XYMultipleSeriesRenderer();
        this.li = li;

        initData();
        initRenderer();
    }

    protected void initData() {
        String title = "Series" + (mDataSet.getSeriesCount() + 1);
        XYSeries series = new XYSeries(title);
        DataDB helper = new DataDB(context);
        ArrayList<DataInfo> infos = helper.query();
        //int column [][] = new int[][3];
        if (infos.size() > 0)
        for(int i = 0; i < infos.size(); i++) {
            DataInfo di = infos.get(i);
            switch (li) {
                case 0:
                    series.add(di.getShenDu(), di.getYaQiang());
                   /* try {
                        Thread.sleep(1000);
                    }catch (Exception e) {
                        e.printStackTrace();
                    } */
                    break;
                case 1:
                    series.add(di.getLi(), di.getShenDu());
                    break;
            }

        }
        mDataSet.addSeries(series);
    }
    protected void initRenderer() {
        mRenderer.setApplyBackgroundColor(true);
        mRenderer.setBackgroundColor(Color.CYAN);
        mRenderer.setMarginsColor(Color.argb(00, 11, 11, 11));

        mRenderer.setAxesColor(Color.BLUE);
        if (1 == li) {
            mRenderer.setChartTitle("力与深度图");
            mRenderer.setXTitle("力");
            mRenderer.setYTitle("深度");
        }
        else {
            mRenderer.setChartTitle("深度压强图");
            mRenderer.setXTitle("深度");
            mRenderer.setYTitle("压强");
        }
        mRenderer.setChartTitleTextSize(40);
        mRenderer.setAxisTitleTextSize(30);
        mRenderer.setInScroll(true);
        mRenderer.setLabelsColor(Color.BLUE);
        mRenderer.setLabelsTextSize(30);
        mRenderer.setLegendTextSize(30);
        mRenderer.setShowLegend(false);
        mRenderer.setMargins(new int[]{30, 50, 20, 50});
        mRenderer.setPanEnabled(true);
        mRenderer.setZoomEnabled(true);
        mRenderer.setZoomButtonsVisible(true);

        mRenderer.setPointSize(0);
        mRenderer.setYLabelsPadding(5);
        mRenderer.setYLabelsColor(0,Color.BLUE);
        mRenderer.setXLabelsColor(Color.BLUE);
        mRenderer.setXLabelsPadding(5);
        mRenderer.setXLabelsAngle(10);

        XYSeriesRenderer renderer = new XYSeriesRenderer();
        mRenderer.addSeriesRenderer(renderer);
        renderer.setPointStyle(PointStyle.CIRCLE);
        renderer.setFillPoints(true);
        renderer.setDisplayChartValues(false);
        renderer.setDisplayChartValuesDistance(10);
        renderer.setLineWidth(10);

        mRenderer.setClickEnabled(true);
        mRenderer.setSelectableBuffer(10);
    }

    public XYMultipleSeriesDataset getmDataSet() {
        return mDataSet;
    }

    public void setmDataSet(XYMultipleSeriesDataset mDataSet) {
        this.mDataSet = mDataSet;
    }

    public XYMultipleSeriesRenderer getmRenderer() {
        return mRenderer;
    }

    public void setmRenderer(XYMultipleSeriesRenderer mRenderer) {
        this.mRenderer = mRenderer;
    }

    public View getView() {
        view = ChartFactory.getLineChartView(context, mDataSet, mRenderer);
        return view;
    }

    public void repaint() {
        if (view != null) {
            view.repaint();
        }
    }

}
