package com.futsal.manager.MakeNewMemoryModule;

import android.hardware.Camera;

/**
 * Created by stories2 on 2017. 6. 22..
 */

public class MakeNewMemoryManagerCameraResolutionListItem {

    Camera.Size availableCameraVideoRecordResolution;
    boolean isSelected;

    public void SetIsSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    public boolean GetIsSelected() {
        return isSelected;
    }

    public void SetAvailableCameraVideoRecordResolution(Camera.Size availableCameraVideoRecordResolution) {
        this.availableCameraVideoRecordResolution = availableCameraVideoRecordResolution;
    }

    public Camera.Size GetAvailableCameraVideoRecordResolution() {
        return availableCameraVideoRecordResolution;
    }
}
