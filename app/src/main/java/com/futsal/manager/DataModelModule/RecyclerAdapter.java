package com.futsal.manager.DataModelModule;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.futsal.manager.DefineManager;
import com.futsal.manager.LogModule.LogManager;
import com.futsal.manager.R;

import java.util.List;

/**
 * Created by stories2 on 2017. 3. 26..
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {
    Context context;
    List<EachCardViewItems> items;
    int item_layout;
    public RecyclerAdapter(Context context, List<EachCardViewItems> items, int item_layout) {
        this.context=context;
        this.items=items;
        this.item_layout=item_layout;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.each_card_view_item,null);
        return new ViewHolder(v);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final EachCardViewItems item=items.get(position);
        Drawable drawable=context.getResources().getDrawable(item.getImage());
        try {
            holder.title.setText(item.getTitle());
            holder.image.setBackground(drawable);
            LogManager.PrintLog("RecyclerAdapter", "onBindViewHolder", "added", DefineManager.LOG_LEVEL_INFO);
        }
        catch (Exception err) {
            LogManager.PrintLog("RecyclerAdapter", "onBindViewHolder", "err: " + err.getMessage(), DefineManager.LOG_LEVEL_ERROR);
        }
        holder.cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(context,item.getTitle(),Toast.LENGTH_SHORT).show();
                Snackbar.make(v, item.getTitle(), Snackbar.LENGTH_SHORT).show();
                switch (position) {
                    case DefineManager.MAKE_NEW_VIDEO_ITEM:
                        break;
                    case DefineManager.SHOW_VIDEO_ITEM:
                        break;
                    default:
                        break;
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView title;
        CardView cardview;

        public ViewHolder(View itemView) {
            super(itemView);
            image=(ImageView)itemView.findViewById(R.id.imageTitle);
            title=(TextView)itemView.findViewById(R.id.txtTitle);
            cardview=(CardView)itemView.findViewById(R.id.cardview);
        }
    }
}