package com.futsal.manager.ShowVideoModule;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.futsal.manager.R;

import java.util.List;

/**
 * Created by stories2 on 2017. 5. 27..
 */

public class EachGridViewItem extends BaseAdapter {

    List<EachGridViewItemModel> gridViewItemData;
    LayoutInflater layoutInflater;
    int layoutId;

    public EachGridViewItem(List<EachGridViewItemModel> gridViewItemData, Context context, int layoutId) {
        this.gridViewItemData = gridViewItemData;
        this.layoutId = layoutId;
        layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return gridViewItemData.size();
    }

    @Override
    public Object getItem(int position) {
        return gridViewItemData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
            convertView = layoutInflater.inflate(layoutId, null);
        }
        ImageView imgVideoThumbnail = (ImageView) convertView.findViewById(R.id.imgVideoThumbnail);
        TextView txtVideoName = (TextView) convertView.findViewById(R.id.txtVideoName);
        return convertView;
    }
}
