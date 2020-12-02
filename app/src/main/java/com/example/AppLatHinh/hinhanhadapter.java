package com.example.AppLatHinh;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.List;

public class hinhanhadapter extends BaseAdapter {
    private Context context;
    private int layout;
    private List<hinhanh> hinhanhList;

    public hinhanhadapter(Context context, int layout, List<hinhanh> hinhanhList) {
        this.context = context;
        this.layout = layout;
        this.hinhanhList = hinhanhList;
    }

    @Override
    public int getCount() {
        return hinhanhList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }
    private class viewholder{
        ImageView imghinh;

    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        viewholder holder;
        if(view==null)
        {
            holder=new viewholder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view=inflater.inflate(layout,null);
            holder.imghinh=(ImageView) view.findViewById(R.id.imageviewhinhanh);
            view.setTag(holder);
        }
        else
            holder = (viewholder) view.getTag();
        hinhanh hinhAnh=hinhanhList.get(i);
        holder.imghinh.setImageResource(hinhAnh.getId());
        return view;
    }
}
