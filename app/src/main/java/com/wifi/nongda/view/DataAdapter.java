package com.wifi.nongda.view;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.wifi.nongda.R;
import com.wifi.nongda.model.DataInfo;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by 段碧伟 on 2016/1/28.
 */
public class DataAdapter extends ArrayAdapter<DataInfo> {
    public DataAdapter(Context context,  ArrayList<DataInfo> objects) {
        super(context, 0, objects);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        if (convertView == null)
            convertView = View.inflate(getContext(), R.layout.list_item, null);
            TextView Num = (TextView)convertView.findViewById(R.id.num);
            TextView li = (TextView)convertView.findViewById(R.id.li);
            TextView shendu = (TextView)convertView.findViewById(R.id.shendu);
            TextView yaqiang = (TextView)convertView.findViewById(R.id.yaqiang);
                DataInfo df = getItem(position );
                String s1 = String.valueOf(position+1);
                Num.setText(s1);
                s1 = String.valueOf(df.getLi());
                li.setText(s1);
                s1 = String.valueOf(df.getShenDu());
                shendu.setText(s1);
                s1 = String.valueOf(df.getYaQiang());
                yaqiang.setText(s1);

        return convertView;
    }
}
