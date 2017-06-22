package com.futsal.manager.MakeNewMemoryModule;

import android.content.Context;
import android.database.DataSetObserver;
import android.hardware.Camera;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.futsal.manager.R;

import java.util.ArrayList;

/**
 * Created by stories2 on 2017. 6. 22..
 */

public class MakeNewMemoryManagerCameraResolutionListAdapter extends BaseAdapter {

    ArrayList<MakeNewMemoryManagerCameraResolutionListItem> cameraResolutionList = new ArrayList<MakeNewMemoryManagerCameraResolutionListItem>();

    public MakeNewMemoryManagerCameraResolutionListAdapter() {
        super();
    }

    @Override
    public boolean hasStableIds() {
        return super.hasStableIds();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        MakeNewMemoryManagerCameraResolutionListItem indexOfScreenResoltuion;
        indexOfScreenResoltuion = cameraResolutionList.get(position);

        Context context = parent.getContext();

        if(convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.make_new_memory_manager_camera_resolution_list_item, parent, false);
        }

        TextView txtVideoResolution = (TextView) convertView.findViewById(R.id.txtVideoResolution);
        String textOfScreenResolution = CameraSizeToString(indexOfScreenResoltuion.GetAvailableCameraVideoRecordResolution());

        txtVideoResolution.setText(textOfScreenResolution);

        if(indexOfScreenResoltuion.GetIsSelected() == true) {
            txtVideoResolution.setTextColor(context.getResources().getColor(R.color.dirtyRed));
        }
        else {
            txtVideoResolution.setTextColor(context.getResources().getColor(R.color.whiteGray));
        }

        return convertView;
    }

    String CameraSizeToString(Camera.Size availableScreenRecordResolution) {
        if(availableScreenRecordResolution != null) {
            return "" + availableScreenRecordResolution.width + " X " + availableScreenRecordResolution.height;
        }
        return "Error!";
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {
        super.registerDataSetObserver(observer);
    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {
        super.unregisterDataSetObserver(observer);
    }

    @Override
    public int getCount() {
        return cameraResolutionList.size();
    }

    @Override
    public Object getItem(int position) {
        return cameraResolutionList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    @Override
    public void notifyDataSetInvalidated() {
        super.notifyDataSetInvalidated();
    }

    @Override
    public boolean areAllItemsEnabled() {
        return super.areAllItemsEnabled();
    }

    @Override
    public boolean isEnabled(int position) {
        return super.isEnabled(position);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return super.getDropDownView(position, convertView, parent);
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public int getViewTypeCount() {
        return super.getViewTypeCount();
    }

    @Override
    public boolean isEmpty() {
        return super.isEmpty();
    }

    public void AddItem(Camera.Size availableCameraVideoRecordResolution, boolean isSelected) {
        MakeNewMemoryManagerCameraResolutionListItem makeNewMemoryManagerCameraResolutionListItem;
        makeNewMemoryManagerCameraResolutionListItem = new MakeNewMemoryManagerCameraResolutionListItem();

        makeNewMemoryManagerCameraResolutionListItem.SetAvailableCameraVideoRecordResolution(availableCameraVideoRecordResolution);
        makeNewMemoryManagerCameraResolutionListItem.SetIsSelected(isSelected);
    }
}
