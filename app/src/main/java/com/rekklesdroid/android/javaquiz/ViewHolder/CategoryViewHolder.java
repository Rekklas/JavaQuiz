package com.rekklesdroid.android.javaquiz.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.rekklesdroid.android.javaquiz.R;
import com.rekklesdroid.android.javaquiz.interfaces.RecyclerViewClickListener;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by user on 02.02.2018.
 */

public class CategoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView categoryTitle;
    public ImageView categoryImage;

    private RecyclerViewClickListener itemClickListener;

    public CategoryViewHolder(View itemView) {
        super(itemView);
        categoryTitle = itemView.findViewById(R.id.txt_category_title);
        categoryImage = itemView.findViewById(R.id.imv_category_image);

        itemView.setOnClickListener(this);
    }

    public void setItemClickListener(RecyclerViewClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v, getAdapterPosition(), false);
    }
}
