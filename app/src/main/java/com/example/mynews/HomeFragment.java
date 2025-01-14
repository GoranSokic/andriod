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

public class HomeFragment extends Fragment {

    private static final String API_KEY = "81a9c7bd76044a3189826206468eb56c";
    private static final String COUNTRY = "us";

    private ArrayList<ModelClass> modelClassArrayList;
    private Adapter adapter;

    private RecyclerView recyclerViewofhome;
    private SearchView searchView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.homefragment, container, false);

        recyclerViewofhome = v.findViewById(R.id.recyclerviewofhome);
        searchView = v.findViewById(R.id.search_view);

        modelClassArrayList = new ArrayList<>();

        recyclerViewofhome.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new Adapter(getContext(), modelClassArrayList);
        recyclerViewofhome.setAdapter(adapter);

        findNews();

        return v;
    }


    private void findNews() {
        Log.d("HomeFragment", "Fetching news...");

        ApiUtilities.getApiInterface().getNews(COUNTRY, 100, API_KEY).enqueue(new Callback<mainNews>() {
            @Override
            public void onResponse(Call<mainNews> call, Response<mainNews> response) {
                if (response.isSuccessful() && response.body() != null) {
                    modelClassArrayList.clear();
                    modelClassArrayList.addAll(response.body().getArticles());
                    adapter.notifyDataSetChanged();


                    adapter.setFullList(modelClassArrayList);


                    implementSearch();
                } else {
                    Log.e("HomeFragment", "Response unsuccessful or empty.");
                }
            }

            @Override
            public void onFailure(Call<mainNews> call, Throwable t) {
                Log.e("HomeFragment", "Failed to fetch news: " + t.getMessage());
            }
        });
    }


    private void implementSearch() {
        searchView.setIconifiedByDefault(false);
        searchView.clearFocus();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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

        searchView.setOnCloseListener(() -> {
            searchView.setQuery("", false);
            searchView.clearFocus();
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
