package com.futsal.manager.ShowVideoModule;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
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
import com.futsal.manager.ShareYourMemoryModule.UploadVideoBackground;

import java.util.List;

import static android.content.Context.ACTIVITY_SERVICE;
import static com.futsal.manager.DefineManager.APP_DOMAIN;
import static com.futsal.manager.DefineManager.LIBRARY_TYPE_CHECK;
import static com.futsal.manager.DefineManager.LIBRARY_TYPE_EDIT;
import static com.futsal.manager.DefineManager.LIBRARY_TYPE_NOT_CHECK;
import static com.futsal.manager.DefineManager.LIBRARY_TYPE_SHARE;
import static com.futsal.manager.DefineManager.LOG_LEVEL_DEBUG;
import static com.futsal.manager.DefineManager.LOG_LEVEL_ERROR;
import static com.futsal.manager.DefineManager.LOG_LEVEL_INFO;
import static com.futsal.manager.DefineManager.LOG_LEVEL_WARN;
import static com.futsal.manager.DefineManager.NO_ACTION;
import static com.futsal.manager.DefineManager.VIDEO_EDIT_REQUEST_STATUS;

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

        final EachGridViewItemModel indexOfGridViewItemData = gridViewItemData.get(position);

        layoutSubBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogManager.PrintLog("EachGridViewItem", "onClick", "sub relative layout click", LOG_LEVEL_DEBUG);
                switch (indexOfGridViewItemData.GetSubBtnType()) {
                    case LIBRARY_TYPE_EDIT:
                        Context context = indexOfGridViewItemData.GetMediaScanContext();
                        if(IsServiceRunning(context) != true && VIDEO_EDIT_REQUEST_STATUS == NO_ACTION) {
                            Intent videoUploadService = new Intent(context, UploadVideoBackground.class);
                            LogManager.PrintLog("EachGridViewItem", "onClick", "video path: " + indexOfGridViewItemData.GetVideoOriginName(), LOG_LEVEL_INFO);
                            videoUploadService.putExtra("videoSavedPath", indexOfGridViewItemData.GetVideoOriginName());
                            context.startService(videoUploadService);
                        }
                        else {
                            LogManager.PrintLog("EachGridViewItem", "onClick", "Service already running", LOG_LEVEL_WARN);
                        }
                        break;
                    case LIBRARY_TYPE_SHARE:
                        ScanVideo("Share video", indexOfGridViewItemData.GetVideoOriginName(), indexOfGridViewItemData.GetMediaScanContext());
                        break;
                    default:
                        break;
                }
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

    boolean IsServiceRunning(Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)){
            if(APP_DOMAIN.equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public void ScanVideo(final String title, String videoSavedPath, final Context mediaScanContext) {
        MediaScannerConnection.scanFile(mediaScanContext, new String[]{videoSavedPath},
                null, new MediaScannerConnection.MediaScannerConnectionClient() {
                    @Override
                    public void onMediaScannerConnected() {

                    }

                    @Override
                    public void onScanCompleted(String path, Uri uri) {
                        try {
                            LogManager.PrintLog("EachGridViewItem", "onScanCompleted", "path: " + path, LOG_LEVEL_INFO);
                            Intent videoLinkShare = new Intent(Intent.ACTION_SEND);
                            videoLinkShare.setType("text/plain");
                            videoLinkShare.putExtra(Intent.EXTRA_SUBJECT, title);
                            videoLinkShare.putExtra(Intent.EXTRA_TEXT, path);
                            videoLinkShare = Intent.createChooser(videoLinkShare, "Choose one");
                            videoLinkShare.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            videoLinkShare.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            mediaScanContext.startActivity(videoLinkShare);
                        }
                        catch (Exception err) {
                            LogManager.PrintLog("EachGridViewItem", "onScanCompleted", "Error: " + err.getMessage(), LOG_LEVEL_ERROR);
                        }
                    }
                });
    }
}
