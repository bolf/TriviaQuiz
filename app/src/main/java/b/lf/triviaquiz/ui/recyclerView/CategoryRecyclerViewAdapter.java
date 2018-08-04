package b.lf.triviaquiz.ui.recyclerView;

import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import b.lf.triviaquiz.R;
import b.lf.triviaquiz.model.QuestionCategory;

public class CategoryRecyclerViewAdapter extends RecyclerView.Adapter<CategoryRecyclerViewAdapter.CategoryItemViewHolder> {

    private static final int ITEM_VIEW_TYPE_HEADER = 0;
    private static final int ITEM_VIEW_TYPE_ITEM = 1;

    private final View mHeader;
    private List<QuestionCategory> mCategoriesList;


    public void setCategoriesList(List<QuestionCategory> mCategoriesList) {
        this.mCategoriesList = mCategoriesList;
    }

    public CategoryRecyclerViewAdapter(List<QuestionCategory> mCategoriesList, View header) {
        this.mCategoriesList = mCategoriesList;
        this.mHeader = header;
    }

    public boolean isHeader(int position) {
        return position == 0;
    }

    @NonNull
    @Override
    public CategoryItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == ITEM_VIEW_TYPE_HEADER) {
            return new CategoryItemViewHolder(mHeader);
        }
        View layoutView = null;
        if (mHeader != null) {
            layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_rv_item, parent, false);
            layoutView.setTag(mCategoriesList);
        } else {
            layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.chosen_category_rv_item, parent, false);
        }
        return new CategoryItemViewHolder(layoutView);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryItemViewHolder holder, int position) {
        if (mHeader != null && isHeader(position)) {
            return;
        }
        int minus = mHeader == null ? 0 : 1;
        holder.mCategoryName.setText(mCategoriesList.get(position-minus).getName());
        holder.mCategoryImage.setImageResource(mCategoriesList.get(position-minus).getIconId());
        setChosenAppearance(holder,mCategoriesList.get(position-minus));
    }

    private void setChosenAppearance(CategoryItemViewHolder holder, QuestionCategory questionCategory) {
//        if(questionCategory.getCategoryIsChosen()){
//            holder.mCategoryName.setTextColor(holder.mCategoryName.getResources().getColor(R.color.colorPrimaryDark));
//            holder.mCategoryName.setTypeface(null, Typeface.BOLD);
//        }else{
//            holder.mCategoryName.setTextColor(holder.mCategoryName.getResources().getColor(android.R.color.black));
//            holder.mCategoryName.setTypeface(null, Typeface.NORMAL);
//        }
    }

    @Override
    public int getItemViewType(int position) {
        return (isHeader(position)&& mHeader != null) ? ITEM_VIEW_TYPE_HEADER : ITEM_VIEW_TYPE_ITEM;
    }

    @Override
    public int getItemCount() {
        return mCategoriesList.size() + (mHeader == null ? 0 : 1);
    }

    class CategoryItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView mCategoryImage;
        TextView mCategoryName;

        public CategoryItemViewHolder(View itemView) {
            super(itemView);
            if (mHeader != null){
                itemView.setOnClickListener(this);
            }
            mCategoryImage = itemView.findViewById(R.id.imageView_category);
            mCategoryName = itemView.findViewById(R.id.textView_categoryName);
        }

        @Override
        public void onClick(View view) {
//            QuestionCategory tappedCategory = ((ArrayList<QuestionCategory>) itemView.getTag()).get(getAdapterPosition()-1);
//            tappedCategory.setCategoryIsChosen(!tappedCategory.getCategoryIsChosen());
//            if(tappedCategory.getCategoryIsChosen()){
//                ((TextView) view.findViewById(R.id.textView_categoryName)).setTextColor(view.getResources().getColor(R.color.colorPrimaryDark));
//                ((TextView) view.findViewById(R.id.textView_categoryName)).setTypeface(null, Typeface.BOLD);
//            }else{
//                ((TextView) view.findViewById(R.id.textView_categoryName)).setTextColor(view.getResources().getColor(android.R.color.black));
//                ((TextView) view.findViewById(R.id.textView_categoryName)).setTypeface(null, Typeface.NORMAL);
//            }
        }
    }
}
