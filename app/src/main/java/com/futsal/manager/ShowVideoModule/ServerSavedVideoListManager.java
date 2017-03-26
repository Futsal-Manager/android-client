package com.futsal.manager.ShowVideoModule;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.futsal.manager.DataModelModule.EachCardViewItems;
import com.futsal.manager.DataModelModule.RecyclerAdapter;
import com.futsal.manager.R;

import java.util.ArrayList;
import java.util.List;

import static com.futsal.manager.DefineManager.CALLED_BY_SERVER_SAVED_LIST_ACTIVITY;

/**
 * Created by stories2 on 2017. 3. 26..
 */

public class ServerSavedVideoListManager extends Activity {

    RecyclerView recyFunctionList;
    RecyclerView.LayoutManager recyFunctionListLayoutManager;
    List<EachCardViewItems> eachCardViewItems;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.server_saved_video_list_manager);

        recyFunctionList = (RecyclerView) findViewById(R.id.recyFunctionList);

        recyFunctionList.setHasFixedSize(true);
        recyFunctionListLayoutManager = new LinearLayoutManager(this);
        recyFunctionList.setLayoutManager(recyFunctionListLayoutManager);

        eachCardViewItems = new ArrayList<>();
        eachCardViewItems.add(new EachCardViewItems(R.drawable.img_0829, "test1"));
        eachCardViewItems.add(new EachCardViewItems(R.drawable.img_0829, "test2"));

        recyFunctionList.setAdapter(new RecyclerAdapter(getApplicationContext(),eachCardViewItems,R.layout.futsal_manager_main, CALLED_BY_SERVER_SAVED_LIST_ACTIVITY));
    }
}
