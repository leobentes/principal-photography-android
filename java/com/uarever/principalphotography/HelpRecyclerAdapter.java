package com.uarever.principalphotography;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by leobentes on 2/27/18.
 */

public class HelpRecyclerAdapter extends RecyclerView.Adapter<HelpRecyclerAdapter.ViewHolder> {

    private ArrayList<Faq> mFaqList;

    // Provide a reference to the views for each data item
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView questionTextView;
        private TextView answerTextView;

        public ViewHolder(View v) {
            super(v);
            questionTextView = v.findViewById(R.id.faq_question);
            answerTextView = v.findViewById(R.id.faq_answer);
        }

        public TextView getQuestionTextView() {
            return questionTextView;
        }

        public TextView getAnswerTextView() {
            return answerTextView;
        }
    }


    // Provide the constructor (depending on the kind of dataset)
    public HelpRecyclerAdapter(ArrayList<Faq> faqList) {
        mFaqList = faqList;
    }


    // Create new views (invoked by the layout manager)
    @Override
    public HelpRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v;

        // Inflate a specific view depeding on the type of item (Faq or image)
        // This test wouldn't be necessary if it was a "regular" list (all items
        // of the same type, in this case Faq class)
        if(viewType == R.layout.faq_item){
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.faq_item, parent, false);
        }

        else {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.faq_image, parent, false);
        }

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }


    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(HelpRecyclerAdapter.ViewHolder holder, int position) {

        // It will check if the current position is not past the last item in the list. If so
        // it will get the question and answer texts at this position and replace the contents
        // of the view with those elements. Otherwise, does nothing since the layout with the
        // image is not dynamic.
        if(position < mFaqList.size()) {
            holder.getQuestionTextView().setText(mFaqList.get(position).getQuestion());
            holder.getAnswerTextView().setText(mFaqList.get(position).getAnswer());
        }
    }


    // Return the size of your dataset (invoked by the layout manager)
    // It must return +1 because the Recycler View will hold an image as the last item.
    @Override
    public int getItemCount() {
        return mFaqList.size() + 1;
    }

    // If it was a "regular" list (all items of the same type, in this case a Faq class) overriding
    // getItemView wouldn't be necessary. Because the last item is different (an image), the method
    // must return a different type of view
    @Override
    public int getItemViewType(int position) {
        // It will check if the current position is past the last item in the list. If so
        // it will return the image layout
        return (position == mFaqList.size()) ? R.layout.faq_image : R.layout.faq_item;
    }
}
