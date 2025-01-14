package com.example.mynews;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HealthFragment extends Fragment {

    String api = "81a9c7bd76044a3189826206468eb56c";
    ArrayList<ModelClass> modelClassArrayList;
    Adapter adapter;
    String country = "us";
    private String category = "health";

    private RecyclerView recyclerViewofhealth;
    private SearchView searchViewHealth;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.healthfragment, container, false);


        recyclerViewofhealth = v.findViewById(R.id.recyclerviewofhealth);
        searchViewHealth = v.findViewById(R.id.search_view);

        modelClassArrayList = new ArrayList<>();
        recyclerViewofhealth.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new Adapter(getContext(), modelClassArrayList);
        recyclerViewofhealth.setAdapter(adapter);


        findNews();

        return v;
    }


    private void findNews() {
        ApiUtilities.getApiInterface().getCategoryNews(country, category, 100, api).enqueue(new Callback<mainNews>() {
            @Override
            public void onResponse(Call<mainNews> call, Response<mainNews> response) {
                if (response.isSuccessful() && response.body() != null) {
                    modelClassArrayList.clear();
                    modelClassArrayList.addAll(response.body().getArticles());
                    adapter.notifyDataSetChanged();


                    adapter.setFullList(modelClassArrayList);


                    implementSearch();
                } else {
                    Log.e("HealthFragment", "Error: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<mainNews> call, Throwable t) {
                Log.e("HealthFragment", "Failure: " + t.getMessage());
            }
        });
    }


    private void implementSearch() {
        searchViewHealth.setIconifiedByDefault(false);
        searchViewHealth.clearFocus();

        searchViewHealth.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.isEmpty()) {
                    adapter.resetData();
                } else {
                    filter(newText);
                }
                return false;
            }
        });

        searchViewHealth.setOnCloseListener(() -> {
            searchViewHealth.setQuery("", false);
            searchViewHealth.clearFocus();
            adapter.resetData();
            return true;
        });
    }


    private void filter(String text) {
        ArrayList<ModelClass> filteredList = new ArrayList<>();

        for (ModelClass item : modelClassArrayList) {
            String title = item.getTitle() != null ? item.getTitle().toLowerCase() : "";
            String description = item.getDescription() != null ? item.getDescription().toLowerCase() : "";

            if (title.contains(text.toLowerCase()) || description.contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }

        adapter.updateData(filteredList);
    }
}
