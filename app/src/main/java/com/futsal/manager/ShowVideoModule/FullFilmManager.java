package com.futsal.manager.ShowVideoModule;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.GridView;

import com.futsal.manager.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by stories2 on 2017. 5. 27..
 */

public class FullFilmManager extends Activity {

    GridView fullFilmGridView;
    List<EachGridViewItemModel> fullFilmList;
    FloatingActionButton floatingActionBtnNewMemory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.full_film_manager);

        fullFilmGridView = (GridView)findViewById(R.id.gridFullFilm);
        floatingActionBtnNewMemory = (FloatingActionButton)findViewById(R.id.floatingActionBtnNewMemory);

        fullFilmList = new ArrayList<EachGridViewItemModel>();
        fullFilmList.add(new EachGridViewItemModel());
        fullFilmList.add(new EachGridViewItemModel());
        fullFilmList.add(new EachGridViewItemModel());
        fullFilmList.add(new EachGridViewItemModel());
        fullFilmList.add(new EachGridViewItemModel());
        fullFilmList.add(new EachGridViewItemModel());
        fullFilmList.add(new EachGridViewItemModel());
        fullFilmList.add(new EachGridViewItemModel());
        fullFilmList.add(new EachGridViewItemModel());
        fullFilmList.add(new EachGridViewItemModel());
        fullFilmList.add(new EachGridViewItemModel());

        EachGridViewItem fullFilmGridViewAdapater = new EachGridViewItem(fullFilmList, getApplicationContext(), R.layout.library_video_manager_item);

        fullFilmGridView.setAdapter(fullFilmGridViewAdapater);

        floatingActionBtnNewMemory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v, "Make New Memory", Snackbar.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
