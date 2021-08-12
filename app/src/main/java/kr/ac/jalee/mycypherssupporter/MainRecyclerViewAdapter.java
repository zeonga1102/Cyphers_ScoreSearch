package kr.ac.jalee.mycypherssupporter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import kr.ac.jalee.mycypherssupporter.databinding.MainRecyclerviewItemBinding;

public class MainRecyclerViewAdapter extends RecyclerView.Adapter {

    private  ArrayList<SearchHistory> searchHistories;

    MainRecyclerViewAdapter(ArrayList<SearchHistory> searchHistories) {
        this.searchHistories = searchHistories;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_recyclerview_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MainRecyclerviewItemBinding binding = ((ViewHolder) holder).getBinding();

        Context context = null;

        String nickname = searchHistories.get(position).getNickname();

        binding.mainRecyclerItemTextName.setText(nickname);
        binding.mainRecyclerItemBtnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String deleteSql = "delete from " + SearchHistoryDatabase.TABLE_SEARCHHISTORY + " where " + "  NICKNAME = '" + nickname +"'";
                SearchHistoryDatabase database = SearchHistoryDatabase.getInstance(context);
                database.execSQL(deleteSql);

                searchHistories.remove(position);

                notifyDataSetChanged();
            }
        });

    }

    @Override
    public int getItemCount() {
        return searchHistories.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private MainRecyclerviewItemBinding binding;

        public ViewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }

        MainRecyclerviewItemBinding getBinding() {
            return binding;
        }
    }
}
