package com.benson.mansoft_charles.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.benson.mansoft_charles.shoolmanportal.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class FactoryAdapter extends RecyclerView.Adapter<FactoryAdapter.ViewHolder> {
    public Context context ;
    public List<TableBean> tableBeanList;
    private OnItemListener onItemListener ;

    public static final String TAG = "FactoryAdapter";

    public FactoryAdapter(Context context, List<TableBean> tableBeanList, OnItemListener onItemListener) {
        this.context = context;
        this.tableBeanList = tableBeanList;
        this.onItemListener = onItemListener ;
    }

    @NonNull
    @Override
    public FactoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.eexams, viewGroup, false);
        return new ViewHolder(view, onItemListener);
    }

    @Override
    public void onBindViewHolder(@NonNull FactoryAdapter.ViewHolder viewHolder, int position) {
        TableBean tableBean = tableBeanList.get(position);
        viewHolder.itemName.setText(tableBean.getName());
        viewHolder.circleImageView.setImageResource(tableBean.getImage());

    }

    @Override
    public int getItemCount() {
        return tableBeanList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
          public TextView itemName;
          public CircleImageView circleImageView ;
          public OnItemListener onItemListener ;
        public ViewHolder(@NonNull View itemView, OnItemListener onItemListener) {
            super(itemView);
            itemName = itemView.findViewById(R.id.filename_tv);
            circleImageView = itemView.findViewById(R.id.pdf_iv);
            this.onItemListener = onItemListener ;
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
          onItemListener.onItemClicked(getAdapterPosition());
        }
    }

    public interface OnItemListener{
        void onItemClicked(int position);
    }
}
