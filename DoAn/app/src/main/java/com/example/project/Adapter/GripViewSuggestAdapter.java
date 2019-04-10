package com.example.project.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;

import java.util.List;

import com.example.project.Common.Common;
import com.example.project.MainActivity;

public class GripViewSuggestAdapter extends BaseAdapter {

    private List<String> suggestSource;
    private Context context;
    private MainActivity mainActivity;

    public GripViewSuggestAdapter(List<String> suggestSource, Context context, MainActivity mainActivity) {
        this.suggestSource = suggestSource;
        this.context = context;
        this.mainActivity = mainActivity;
    }

    @Override
    public int getCount() {
        return suggestSource.size();
    }

    @Override
    public Object getItem(int position) {
        return suggestSource.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Button button;
        if (convertView == null) {
            if (suggestSource.get(position).equals("null")) {
                button = new Button(context);
                button.setLayoutParams(new GridView.LayoutParams(85, 85));
                button.setPadding(8, 8, 8, 8);
                button.setBackgroundColor(Color.DKGRAY);
            } else
                button = new Button(context);
            button.setLayoutParams(new GridView.LayoutParams(85, 85));
            button.setPadding(8, 8, 8, 8);
            button.setBackgroundColor(Color.DKGRAY);
            button.setTextColor(Color.YELLOW);
            button.setText(suggestSource.get(position));
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //If correct answer contains character user selected
                    if (String.valueOf(mainActivity.answer).contains(suggestSource.get(position))) {
                        char compare = suggestSource.get(position).charAt(0); //Get char

                        for (int i = 0; i < mainActivity.answer.length; i++) {
                            if (compare == mainActivity.answer[i]) {
                                Common.user_submit_answer[i] = compare;
                            }
                        }

                        //Update UI
                        GripViewAnswerAdapter answerAdapter = new GripViewAnswerAdapter(Common.user_submit_answer, context);
                        mainActivity.gripViewAnswer.setAdapter(answerAdapter);
                        answerAdapter.notifyDataSetChanged();

                        //Remove from suggest source
                        mainActivity.suggestSource.set(position, " ");
                        mainActivity.suggestAdapter = new GripViewSuggestAdapter(mainActivity.suggestSource, context, mainActivity);
                        mainActivity.gripViewSuggest.setAdapter(mainActivity.suggestAdapter);
                        mainActivity.suggestAdapter.notifyDataSetChanged();
                    }
                    //esle
                    else {
                        //Remove from suggest source
                        mainActivity.suggestSource.set(position, " ");
                        mainActivity.suggestAdapter = new GripViewSuggestAdapter(mainActivity.suggestSource, context, mainActivity);
                        mainActivity.gripViewSuggest.setAdapter(mainActivity.suggestAdapter);
                        mainActivity.suggestAdapter.notifyDataSetChanged();
                    }
                }
            });
        } else
            button = (Button) convertView;
        return button;
    }
}
