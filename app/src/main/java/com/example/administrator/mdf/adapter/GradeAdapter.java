package com.example.administrator.mdf.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.mdf.R;

import java.util.List;
import java.util.Map;


public class GradeAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private List<Map<String, Object>> list;
    private Context context;

    public GradeAdapter(Context context, List<Map<String,Object>> list) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.grade_vi_item, null);
            viewHolder.tv_item_main_grid = (TextView) convertView.findViewById(R.id.tv_item_main_grid);
            viewHolder.iv_item_main_gird = (ImageView) convertView.findViewById(R.id.iv_item_main_gird);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tv_item_main_grid.setText((String) list.get(position).get("name"));
        viewHolder.iv_item_main_gird.setBackgroundResource((Integer) list.get(position).get("pic"));
        return convertView;
    }

    private class ViewHolder {
        public ImageView iv_item_main_gird;
        public TextView tv_item_main_grid;
    }
}
