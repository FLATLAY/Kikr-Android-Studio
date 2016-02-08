package com.kikr.adapter;

/**
 * Created by Tycho on 10/13/2015.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kikr.R;

import java.util.ArrayList;

public class SearchProductAdapter extends RecyclerView.Adapter<SearchProductAdapter.ViewHolder> {
    OnItemClickListener onItemClickListener;
    ArrayList<String> categoriesName;
    int[] categoryImages = {R.drawable.ic_category_clothings, R.drawable.ic_category_shoes, R.drawable.ic_category_jewelry,
            R.drawable.ic_category_sports, R.drawable.ic_category_personalcare,
            R.drawable.ic_category_babyproducts, R.drawable.ic_category_electronics, R.drawable.ic_category_videogames,
            R.drawable.ic_category_computers, R.drawable.ic_category_pets, R.drawable.ic_category_musicalinstruments};
Context mContext;
    public SearchProductAdapter(Context context,ArrayList<String> categoriesName) {
        super();
        this.categoriesName = categoriesName;
        this.mContext=context;
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

        viewHolder.tvCategoryName.setText(capitalizeEachWord(categoriesName.get(position).trim()));

        viewHolder.tvProductCount.setText("500+ items");
        viewHolder.category_image.setImageResource(categoryImages[position]);
    }

    @Override
    public int getItemCount() {
        return categoriesName.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public ImageView category_image;
        public TextView tvCategoryName;
        public TextView tvProductCount;
public View view;
        public ViewHolder(View itemView) {
            super(itemView);
            category_image = (ImageView) itemView.findViewById(R.id.category_image);
            tvCategoryName = (TextView) itemView.findViewById(R.id.category_name);
            tvProductCount = (TextView) itemView.findViewById(R.id.product_count);
            view=itemView;
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
        for(int i=0,l=words.length;i<l;++i) {
            if(i>0) result.append(" ");
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
