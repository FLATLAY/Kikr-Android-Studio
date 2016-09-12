package com.kikr.adapter;

/**
 * Created by Tycho on 10/13/2015.
 */

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kikr.R;

import java.util.ArrayList;

public class SearchProductAdapter extends RecyclerView.Adapter<SearchProductAdapter.ViewHolder> {
    OnItemClickListener onItemClickListener;
    ArrayList<String> categoriesName;
    int[] categoryImages = {R.drawable.ic_category_clothings, R.drawable.ic_category_shoes, R.drawable.ic_category_jewelry,
            R.drawable.ic_category_sports, R.drawable.ic_category_personalcare,
            R.drawable.ic_category_babyproducts, R.drawable.ic_category_electronics, R.drawable.toys_icon,
            R.drawable.ic_category_computers, R.drawable.ic_category_pets, R.drawable.ic_category_musicalinstruments, R.drawable.ic_category_videogames};
    Context mContext;

    public SearchProductAdapter(Context context, ArrayList<String> categoriesName) {
        super();
        this.categoriesName = categoriesName;
        this.mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.rv_adapter_categories_row, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {

        try {
            viewHolder.tvCategoryName.setText(capitalizeEachWord(categoriesName.get(position).trim()));

            viewHolder.tvProductCount.setText("500+ items");
            viewHolder.tvProductCount.setVisibility(View.GONE);
            viewHolder.category_image.setImageResource(categoryImages[position]);
        //    setbackColor(viewHolder.row, position);
        } catch (Exception ex) {

        }
    }

    private void setbackColor(View view, int index) {
        switch (index % 8) {

            case 0:


                view.setBackgroundColor(Color.parseColor("#106d61"));
                view.setAlpha(1.0f);

                break;


            case 1:
                view.setBackgroundColor(Color.parseColor("#19a391"));
                view.setAlpha(1.0f);


                break;

            case 2:
                view.setBackgroundColor(Color.parseColor("#179787"));
                view.setAlpha(1.0f);


                break;

            case 3:
                view.setBackgroundColor(Color.parseColor("#1aa896"));
                view.setAlpha(1.0f);


                break;

            case 4:
                view.setBackgroundColor(Color.parseColor("#4fbeb0"));
                view.setAlpha(1.0f);


                break;

            case 5:
                view.setBackgroundColor(Color.parseColor("#179787"));
                view.setAlpha(1.0f);

                break;

            case 6:
                view.setBackgroundColor(Color.parseColor("#169383"));
                view.setAlpha(1.0f);

                break;

            case 7:
                view.setBackgroundColor(Color.parseColor("#19a190"));
                view.setAlpha(1.0f);


                break;

            default:

                break;

        }
    }

    @Override
    public int getItemCount() {
        return categoriesName.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ImageView category_image;
        public TextView tvCategoryName;
        public TextView tvProductCount;
        public View view;
        RelativeLayout row;

        public ViewHolder(View itemView) {
            super(itemView);
            category_image = (ImageView) itemView.findViewById(R.id.category_image);
            tvCategoryName = (TextView) itemView.findViewById(R.id.category_name);
            tvProductCount = (TextView) itemView.findViewById(R.id.product_count);
            row = (RelativeLayout) itemView.findViewById(R.id.top_layout);
            view = itemView;
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(view, getAdapterPosition());
            }
        }
    }

    private StringBuilder capitalizeEachWord(String s) {
        StringBuilder result = new StringBuilder(s.length());
        String[] words = s.split("\\s");
        for (int i = 0, l = words.length; i < l; ++i) {
            if (i > 0) result.append(" ");
            result.append(Character.toUpperCase(words[i].charAt(0)))
                    .append(words[i].substring(1));

        }
        return result;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void SetOnItemClickListener(final OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

}
