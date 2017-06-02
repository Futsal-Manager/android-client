package com.futsal.manager.ShowVideoModule;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.futsal.manager.DefineManager;
import com.futsal.manager.LogModule.LogManager;
import com.futsal.manager.R;

import java.util.List;

import static com.futsal.manager.DefineManager.LIBRARY_TYPE_CHECK;
import static com.futsal.manager.DefineManager.LIBRARY_TYPE_EDIT;
import static com.futsal.manager.DefineManager.LIBRARY_TYPE_NOT_CHECK;
import static com.futsal.manager.DefineManager.LIBRARY_TYPE_SHARE;
import static com.futsal.manager.DefineManager.LOG_LEVEL_DEBUG;

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

        RelativeLayout layoutSubBtn = (RelativeLayout) convertView.findViewById(R.id.layoutSubBtn);
        ImageView imgVideoThumbnail = (ImageView) convertView.findViewById(R.id.imgVideoThumbnail);
        ImageView imgSubBtn = (ImageView) convertView.findViewById(R.id.imgSubBtn);
        TextView txtVideoName = (TextView) convertView.findViewById(R.id.txtVideoName);
        TextView txtVideoTime = (TextView) convertView.findViewById(R.id.txtVideoTime);

        EachGridViewItemModel indexOfGridViewItemData = gridViewItemData.get(position);

        layoutSubBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogManager.PrintLog("EachGridViewItem", "onClick", "sub relative layout click", LOG_LEVEL_DEBUG);
            }
        });

        String fileName = indexOfGridViewItemData.GetVideoName();
        LogManager.PrintLog("EachGridViewItem", "getView", "file name: " + fileName + " pos: " + position, DefineManager.LOG_LEVEL_INFO);
        txtVideoName.setText(fileName);

        Bitmap videoThumbnailImage = indexOfGridViewItemData.GetThumbnailImage();
        if(videoThumbnailImage != null) {
            imgVideoThumbnail.setImageBitmap(videoThumbnailImage);
        }
        String videoDurationTime = indexOfGridViewItemData.GetVideoDurationTime();
        txtVideoTime.setText(videoDurationTime);

        switch (indexOfGridViewItemData.GetSubBtnType()) {
            case LIBRARY_TYPE_EDIT:
                imgSubBtn.setImageResource(R.drawable.shape_12);
                break;
            case LIBRARY_TYPE_SHARE:
                imgSubBtn.setImageResource(R.drawable.shape_11);
                break;
            case LIBRARY_TYPE_CHECK:
                imgSubBtn.setImageResource(R.drawable.shape_13_copy_1);
                break;
            case LIBRARY_TYPE_NOT_CHECK:
                imgSubBtn.setImageResource(R.drawable.shape_13_copy);
                break;
            default:
                break;
        }

        return convertView;
    }
}
