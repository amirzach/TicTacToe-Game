package com.example.myapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ResultFragment extends Fragment {

    private RecyclerView recyclerView;
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

        DBHelper = new DBHelper(getContext());
        List<Player> playerList = DBHelper.getAllPlayers();

        playerAdapter = new PlayerAdapter(playerList);
        recyclerView.setAdapter(playerAdapter);

        return view;
    }
}
