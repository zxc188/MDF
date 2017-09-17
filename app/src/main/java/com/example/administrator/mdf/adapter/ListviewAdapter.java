package com.example.administrator.mdf.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.administrator.mdf.R;

import java.util.List;
import java.util.Map;

public class ListviewAdapter extends BaseAdapter{
    private Context context;
    private LayoutInflater inflater;
    private List<Map<String, Object>> list;

    public ListviewAdapter(Context context,List<Map<String,Object>> list){
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
        ListviewAdapter.ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ListviewAdapter.ViewHolder();
            convertView = inflater.inflate(R.layout.listview_item , null);
            viewHolder.number = (TextView) convertView.findViewById(R.id.textnum);
            viewHolder.music_name = (TextView) convertView.findViewById(R.id.nmusic_name);
            viewHolder.music_singer = (TextView) convertView.findViewById(R.id.music_singer);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ListviewAdapter.ViewHolder) convertView.getTag();
        }
        viewHolder.number.setText((String) list.get(position).get("num"));
        viewHolder.music_name.setText((String) list.get(position).get("name"));
        viewHolder.music_singer.setText((String) list.get(position).get("singer"));
        return convertView;
    }
    private  class ViewHolder {
        public TextView music_singer;
        public TextView music_name;
        public TextView number;
    }
}
