package com.futsal.manager.DevModule;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.futsal.manager.DefineManager;
import com.futsal.manager.LogModule.LogManager;
import com.futsal.manager.R;

import static com.futsal.manager.DefineManager.BLUETOOTH_SEND_SPEED;
import static com.futsal.manager.DefineManager.BLUR_MODE_OPTION;
import static com.futsal.manager.DefineManager.LOG_LEVEL_DEBUG;
import static com.futsal.manager.DefineManager.MAXIMUM_DETECT_COLOR_H;
import static com.futsal.manager.DefineManager.MAXIMUM_DETECT_COLOR_S;
import static com.futsal.manager.DefineManager.MAXIMUM_DETECT_COLOR_V;
import static com.futsal.manager.DefineManager.MINIMUM_DETECT_COLOR_H;
import static com.futsal.manager.DefineManager.MINIMUM_DETECT_COLOR_S;
import static com.futsal.manager.DefineManager.MINIMUM_DETECT_COLOR_V;
import static com.futsal.manager.DefineManager.PICTURE_RESOLUTION_SETTING;
import static com.futsal.manager.DefineManager.RECORD_RESOLUTION_SETTING;
import static com.futsal.manager.DefineManager.VIDEO_RECORD_BIT_RATE;

/**
 * Created by stories2 on 2017. 5. 2..
 */

public class DevelopModeManager extends Activity {

    TextView txtMinH, txtMinS, txtMinV, txtMaxH, txtMaxS, txtMaxV, txtbluetoothSendSpeed;
    CheckBox checkBlur;
    SeekBar seekMinH, seekMinS, seekMinV, seekMaxH, seekMaxS, seekMaxV, seekBluetoothSendSpeed;
    Spinner spinPictureSize, spinRecordSize;
    EditText etxtBitRate;
    String[] screenResolutionList = new String[]{
            "320 X 240", "640 X 480", "1280 X 720", "1920 X 1080"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.develop_mode_manager);

        txtMinH = (TextView) findViewById(R.id.txtMinH);
        txtMinS = (TextView) findViewById(R.id.txtMinS);
        txtMinV = (TextView) findViewById(R.id.txtMinV);
        txtMaxH = (TextView) findViewById(R.id.txtMaxH);
        txtMaxS = (TextView) findViewById(R.id.txtMaxS);
        txtMaxV = (TextView) findViewById(R.id.txtMaxV);
        txtbluetoothSendSpeed = (TextView) findViewById(R.id.txtbluetoothSendSpeed);

        checkBlur = (CheckBox) findViewById(R.id.checkBlur);

        seekMinH = (SeekBar) findViewById(R.id.seekMinH);
        seekMinS = (SeekBar) findViewById(R.id.seekMinS);
        seekMinV = (SeekBar) findViewById(R.id.seekMinV);
        seekMaxH = (SeekBar) findViewById(R.id.seekMaxH);
        seekMaxS = (SeekBar) findViewById(R.id.seekMaxS);
        seekMaxV = (SeekBar) findViewById(R.id.seekMaxV);
        seekBluetoothSendSpeed = (SeekBar) findViewById(R.id.seekBluetoothSendSpeed);

        spinPictureSize = (Spinner)findViewById(R.id.spinPictureSize);
        spinRecordSize = (Spinner)findViewById(R.id.spinRecordSize);

        etxtBitRate = (EditText)findViewById(R.id.etxtBitRate);

        DevLayoutInit devLayoutInit = new DevLayoutInit(this);
        devLayoutInit.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        InitLayoutListener();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        LogManager.PrintLog("DevelopModeManager", "onDestroy", "bit: " + etxtBitRate.getText().toString(), LOG_LEVEL_DEBUG);
    }

    void InitLayoutListener() {

        checkBlur.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                BLUR_MODE_OPTION = isChecked;
            }
        });

        seekMinH.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                MINIMUM_DETECT_COLOR_H = progress;
                txtMinH.setText("H " + MINIMUM_DETECT_COLOR_H);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        seekMinS.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                MINIMUM_DETECT_COLOR_S = progress;
                txtMinS.setText("S " + MINIMUM_DETECT_COLOR_S);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        seekMinV.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                MINIMUM_DETECT_COLOR_V = progress;
                txtMinV.setText("V " + MINIMUM_DETECT_COLOR_V);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        seekMaxH.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                MAXIMUM_DETECT_COLOR_H = progress;
                txtMaxH.setText("H " + MAXIMUM_DETECT_COLOR_H);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        seekMaxS.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                MAXIMUM_DETECT_COLOR_S = progress;
                txtMaxS.setText("S " + MAXIMUM_DETECT_COLOR_S);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        seekMaxV.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                MAXIMUM_DETECT_COLOR_V = progress;
                txtMaxV.setText("V " + MAXIMUM_DETECT_COLOR_V);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        spinPictureSize.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                PICTURE_RESOLUTION_SETTING = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinRecordSize.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                RECORD_RESOLUTION_SETTING = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        etxtBitRate.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if(actionId == EditorInfo.IME_ACTION_DONE) {
                    LogManager.PrintLog("DevelopModeManager", "onEditorAction", "data: " + v.getText().toString(), LOG_LEVEL_DEBUG);
                }
                return false;
            }
        });

        seekBluetoothSendSpeed.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                BLUETOOTH_SEND_SPEED = progress;
                txtbluetoothSendSpeed.setText("" + BLUETOOTH_SEND_SPEED);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    void InitLayoutOptionBasedOnData() {
        checkBlur.setChecked(BLUR_MODE_OPTION);

        seekMinH.setProgress(MINIMUM_DETECT_COLOR_H);
        seekMinS.setProgress(MINIMUM_DETECT_COLOR_S);
        seekMinV.setProgress(MINIMUM_DETECT_COLOR_V);

        seekMaxH.setProgress(MAXIMUM_DETECT_COLOR_H);
        seekMaxS.setProgress(MAXIMUM_DETECT_COLOR_S);
        seekMaxV.setProgress(MAXIMUM_DETECT_COLOR_V);

        ArrayAdapter spinPictureSizeAdapter = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, screenResolutionList);
        ArrayAdapter spinRecordSizeAdapter = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, screenResolutionList);

        spinPictureSize.setAdapter(spinPictureSizeAdapter);
        spinRecordSize.setAdapter(spinRecordSizeAdapter);

        spinPictureSize.setSelection(PICTURE_RESOLUTION_SETTING);
        spinRecordSize.setSelection(RECORD_RESOLUTION_SETTING);

        etxtBitRate.setText("" + VIDEO_RECORD_BIT_RATE);

        txtbluetoothSendSpeed.setText("" + BLUETOOTH_SEND_SPEED);

        seekBluetoothSendSpeed.setProgress(BLUETOOTH_SEND_SPEED);
    }

    public class DevLayoutInit extends AsyncTask<Void, Void, Void> {

        ProgressDialog devLayoutInitProcess;

        public DevLayoutInit(Activity developModeManager) {
            super();
            devLayoutInitProcess = new ProgressDialog(developModeManager);
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                InitLayoutOptionBasedOnData();
                Thread.sleep(1000);
            }
            catch (Exception err) {
                LogManager.PrintLog("DevLayoutInit", "doInBackground", "Error: " + err.getMessage(), DefineManager.LOG_LEVEL_ERROR);
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            try {
                devLayoutInitProcess.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                devLayoutInitProcess.setMessage("프로그램 데이터 로드 중");
                devLayoutInitProcess.setCancelable(false);

                devLayoutInitProcess.show();
            }
            catch (Exception err) {
                LogManager.PrintLog("DevLayoutInit", "onPreExecute", "Error: " + err.getMessage(), DefineManager.LOG_LEVEL_ERROR);
            }
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            try {
                devLayoutInitProcess.dismiss();
            }
            catch (Exception err) {
                LogManager.PrintLog("DevLayoutInit", "onPostExecute", "Error: " + err.getMessage(), DefineManager.LOG_LEVEL_ERROR);
            }
            super.onPostExecute(aVoid);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onCancelled(Void aVoid) {
            super.onCancelled(aVoid);
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }
    }
}
