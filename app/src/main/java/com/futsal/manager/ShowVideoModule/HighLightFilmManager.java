package com.futsal.manager.ShowVideoModule;

import android.app.Activity;
import android.os.Bundle;
import android.widget.GridView;

import com.futsal.manager.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by stories2 on 2017. 5. 27..
 */

public class HighLightFilmManager extends Activity {

    GridView highLightGridView;
    List<EachGridViewItemModel> highLightFilmList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.high_light_film_manager);

        highLightGridView = (GridView)findViewById(R.id.gridHighLightFilm);

        highLightFilmList = new ArrayList<EachGridViewItemModel>();
        highLightFilmList.add(new EachGridViewItemModel());
        highLightFilmList.add(new EachGridViewItemModel());
        highLightFilmList.add(new EachGridViewItemModel());

        EachGridViewItem fullFilmGridViewAdapater = new EachGridViewItem(highLightFilmList, getApplicationContext(), R.layout.library_video_manager_item);

        highLightGridView.setAdapter(fullFilmGridViewAdapater);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
