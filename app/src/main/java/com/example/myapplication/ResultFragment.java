package com.example.myapplication;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ResultFragment extends Fragment {

    private RecyclerView recyclerView;
    private SearchView searchView;
    private PlayerAdapter playerAdapter;
    private DBHelper DBHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_result, container, false);

        //View table of Player with W/D/L and total points
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        searchView = view.findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchPlayers(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (TextUtils.isEmpty(newText)) {
                    loadAllPlayers();
                } else {
                    searchPlayers(newText);
                }
                return false;
            }
        });

        DBHelper = new DBHelper(getContext());
        loadAllPlayers();

        return view;
    }

    private void loadAllPlayers() {
        List<Player> playerList = DBHelper.getAllPlayersDesc();
        playerAdapter = new PlayerAdapter(playerList);
        recyclerView.setAdapter(playerAdapter);
    }

    private void searchPlayers(String nickname) {
        List<Player> playerList = DBHelper.searchPlayersByName(nickname);
        if (playerList.isEmpty()) {
            Toast.makeText(getContext(), "No players found", Toast.LENGTH_SHORT).show();
        }
        playerAdapter = new PlayerAdapter(playerList);
        recyclerView.setAdapter(playerAdapter);
    }
}
