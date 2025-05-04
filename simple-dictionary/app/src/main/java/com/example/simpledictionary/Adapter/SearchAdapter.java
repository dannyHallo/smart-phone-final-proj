package com.example.simpledictionary.Adapter;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.simpledictionary.R;

import java.util.List;

class SearchViewHolder extends RecyclerView.ViewHolder {

    public TextView obj, basic_trans, spec_trans, english_trans;
    public LinearLayout back_linear;

    public SearchViewHolder(@NonNull View itemView) {
        super(itemView);
        obj = (TextView) itemView.findViewById(R.id.obj);
        basic_trans = (TextView) itemView.findViewById(R.id.basic_trans);
        spec_trans = (TextView) itemView.findViewById(R.id.spec_trans);
        english_trans = (TextView) itemView.findViewById(R.id.english_trans);
        back_linear = (LinearLayout) itemView.findViewById(R.id.back_linear);
    }
}

public class SearchAdapter extends RecyclerView.Adapter<SearchViewHolder> {
    private Context context;
    private List<com.example.simpledictionary.Model.words> words;

    public SearchAdapter(Context context, List<com.example.simpledictionary.Model.words> words) {
        this.context = context;
        this.words = words;
    }

    @NonNull
    @Override
    public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.layout_item, parent, false);
        return new SearchViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchViewHolder holder, int position) {
        String current_name = words.get(position).getObj();
        String current_trans = words.get(position).getBasic_trans();

        if (current_name.contains(" ")) {
            holder.obj.setText(current_name.toUpperCase().replace(" ", "\n"));
            holder.basic_trans.setText(current_trans.replace(",", "\n")
                    .replace("；", "\n").replace(" ", ""));
        } else {
            holder.obj.setText(words.get(position).getObj());
            holder.basic_trans.setText(words.get(position).getBasic_trans());
        }
        holder.spec_trans.setText(words.get(position).getSpec_trans());
        holder.english_trans.setText(words.get(position).getEnglish_trans());
        holder.back_linear.setBackgroundColor(context.getResources().getColor(R.color.colorWhite));
        Dye(current_name, holder);

    }

    public void Dye(String word_name, SearchViewHolder holder) {
        if (word_name.contains("l") || word_name.contains("L")) {
            holder.back_linear.setBackgroundColor(context.getResources().getColor(R.color.colorForLover));
        } else if (word_name.contains("h") || word_name.contains("H")) {
            holder.back_linear.setBackgroundColor(context.getResources().getColor(R.color.colorForHills));
        } else if (word_name.contains("w") || word_name.contains("W")) {
            holder.back_linear.setBackgroundColor(context.getResources().getColor(R.color.colorForWater));
        } else if (word_name.contains("g") || word_name.contains("G")) {
            holder.back_linear.setBackgroundColor(context.getResources().getColor(R.color.colorForGrass));
        } else if (word_name.contains("i") || word_name.contains("I")) {
            holder.back_linear.setBackgroundColor(context.getResources().getColor(R.color.colorForIce));
        } else if (word_name.contains("d") || word_name.contains("D")) {
            holder.back_linear.setBackgroundColor(context.getResources().getColor(R.color.colorForDancer));
        } else if (word_name.contains("a") || word_name.contains("A")) {
            holder.back_linear.setBackgroundColor(context.getResources().getColor(R.color.colorForApple));
        } else if (word_name.contains("e") || word_name.contains("E")) {
            holder.back_linear.setBackgroundColor(context.getResources().getColor(R.color.colorForElectric));
        }
    }

    @Override
    public int getItemCount() {
        return words.size();
    }

}


