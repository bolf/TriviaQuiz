package b.lf.triviaquiz.ui.recyclerView;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import b.lf.triviaquiz.R;
import b.lf.triviaquiz.model.QuestionCategory;

public class CategoryRecyclerViewAdapter extends RecyclerView.Adapter<CategoryRecyclerViewAdapter.CategoryItemViewHolder> {

    private List<QuestionCategory> mCategoriesList;

    public CategoryRecyclerViewAdapter(List<QuestionCategory> mCategoriesList) {
        this.mCategoriesList = mCategoriesList;
    }

    @NonNull
    @Override
    public CategoryItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_rv_item, parent, false);
        return new CategoryItemViewHolder(layoutView);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryItemViewHolder holder, int position) {
        holder.mCategoryName.setText(mCategoriesList.get(position).getmName());
        holder.mCategoryImage.setImageResource(mCategoriesList.get(position).getmIcon());
        //TODO: set text/background color (if Category is chosen)
    }

    @Override
    public int getItemCount() {
        return mCategoriesList.size();
    }

    class CategoryItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView mCategoryImage;
        TextView mCategoryName;

        public CategoryItemViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            mCategoryImage = itemView.findViewById(R.id.imageView_category);
            mCategoryName  = itemView.findViewById(R.id.textView_categoryName);
        }

        @Override
        public void onClick(View view) {
            Toast.makeText(view.getContext(), "eeee", Toast.LENGTH_SHORT).show();
        }
    }
}
