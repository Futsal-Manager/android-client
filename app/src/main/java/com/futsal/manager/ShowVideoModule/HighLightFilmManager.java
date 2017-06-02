package com.futsal.manager.ShowVideoModule;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.GridView;

import com.futsal.manager.EmbeddedCommunicationModule.EmbeddedSystemFinder;
import com.futsal.manager.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by stories2 on 2017. 5. 27..
 */

public class HighLightFilmManager extends Activity {

    GridView highLightGridView;
    List<EachGridViewItemModel> highLightFilmList;
    FloatingActionButton floatingActionBtnNewMemory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.high_light_film_manager);

        highLightGridView = (GridView)findViewById(R.id.gridHighLightFilm);
        floatingActionBtnNewMemory = (FloatingActionButton)findViewById(R.id.floatingActionBtnNewMemory);

        highLightFilmList = new ArrayList<EachGridViewItemModel>();
        highLightFilmList.add(new EachGridViewItemModel());
        highLightFilmList.add(new EachGridViewItemModel());
        highLightFilmList.add(new EachGridViewItemModel());

        EachGridViewItem fullFilmGridViewAdapater = new EachGridViewItem(highLightFilmList, getApplicationContext(), R.layout.library_video_manager_item);

        highLightGridView.setAdapter(fullFilmGridViewAdapater);

        floatingActionBtnNewMemory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent recordVideoLayout = new Intent(getApplicationContext(), EmbeddedSystemFinder.class);
                startActivity(recordVideoLayout);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
