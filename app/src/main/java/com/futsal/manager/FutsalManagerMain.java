package com.futsal.manager;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.futsal.manager.DataModelModule.EachCardViewItems;
import com.futsal.manager.DataModelModule.RecyclerAdapter;
import com.futsal.manager.LogModule.LogManager;

import org.opencv.android.OpenCVLoader;

import java.util.ArrayList;
import java.util.List;

import static com.futsal.manager.DefineManager.CALLED_BY_FUTSAL_MAIN_ACTIVITY;

public class FutsalManagerMain extends AppCompatActivity {

    RecyclerView recyFunctionList;
    RecyclerView.Adapter recyFunctionListAdapter;
    RecyclerView.LayoutManager recyFunctionListLayoutManager;
    List<EachCardViewItems> eachCardViewItems;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.futsal_manager_main);

        recyFunctionList = (RecyclerView) findViewById(R.id.recyFunctionList);

        recyFunctionList.setHasFixedSize(true);
        recyFunctionListLayoutManager = new LinearLayoutManager(this);
        recyFunctionList.setLayoutManager(recyFunctionListLayoutManager);

        eachCardViewItems = new ArrayList<>();
        eachCardViewItems.add(new EachCardViewItems(R.mipmap.ic_launcher, "MAKE NEW VIDEO"));
        eachCardViewItems.add(new EachCardViewItems(R.mipmap.ic_launcher, "SHOW VIDEO"));

        recyFunctionList.setAdapter(new RecyclerAdapter(getApplicationContext(),eachCardViewItems,R.layout.futsal_manager_main, CALLED_BY_FUTSAL_MAIN_ACTIVITY));
    }
}
