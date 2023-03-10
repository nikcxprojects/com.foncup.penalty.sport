package com.foncup.penalty.sport;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TopAdapter extends RecyclerView.Adapter<TopAdapter.ViewHolder> {

    List<Integer> list;
    Context activity;
    String[] nulls = {"000","00","0","","",""};

    public TopAdapter(Context activity, List<Integer> list) {
        this.activity = activity;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        ViewHolder holder = new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_top, viewGroup, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        viewHolder.position.setText(""+(i+1));
        viewHolder.points.setText(""+nulls[String.valueOf(list.get(i)).length()-1]+""+list.get(i));

    }

    @Override
    public int getItemCount() {
        List<Integer> arrayList = this.list;
        if (arrayList == null) {
            return 0;
        }
        return arrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView points,position;

        public ViewHolder(View view) {
            super(view);
            this.position = view.findViewById(R.id.position);
            this.points = view.findViewById(R.id.points);
        }
    }
}
