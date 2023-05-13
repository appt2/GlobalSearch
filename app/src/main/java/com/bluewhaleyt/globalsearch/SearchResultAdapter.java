package com.bluewhaleyt.globalsearch;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bluewhaleyt.filemanagement.FileIconUtil;

import java.util.List;

public class SearchResultAdapter extends RecyclerView.Adapter<SearchResultAdapter.ViewHolder> {
    private List<SearchResult> searchResults;

    public void setSearchResults(List<SearchResult> searchResults) {
        this.searchResults = searchResults;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_search_result_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        var context = holder.itemView.getContext();
        SearchResult result = searchResults.get(position);
        holder.tvFilePath.setText(result.getFilePath());
        holder.tvResult.setText(result.getHighlightedContent());
        holder.tvLineNumber.setText(context.getString(R.string.line, result.getLineNumber()));

        var fileIconHelper = new FileIconUtil(result.getFilePath(), "");
        fileIconHelper.bindFileIcon(holder.imgFileIcon);
    }

    @Override
    public int getItemCount() {
        return searchResults == null ? 0 : searchResults.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvFilePath, tvResult, tvLineNumber;
        private ImageView imgFileIcon;

        public ViewHolder(View view) {
            super(view);
            tvFilePath = view.findViewById(R.id.tv_file_path);
            tvResult = view.findViewById(R.id.tv_result);
            tvLineNumber = view.findViewById(R.id.tv_line_number);
            imgFileIcon = view.findViewById(R.id.img_file_icon);
        }
    }
}