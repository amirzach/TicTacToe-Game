package com.example.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PlayerAdapter extends RecyclerView.Adapter<PlayerAdapter.PlayerViewHolder> {

    private List<Player> playerList;

    public static class PlayerViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewNickName, textViewWins, textViewLose, textViewDraw, textViewPoints;

        public PlayerViewHolder(View view) {
            super(view);
            textViewNickName = view.findViewById(R.id.textViewNickName);
            textViewWins = view.findViewById(R.id.textViewWins);
            textViewLose = view.findViewById(R.id.textViewLose);
            textViewDraw = view.findViewById(R.id.textViewDraw);
            textViewPoints = view.findViewById(R.id.textViewPoints);
        }
    }

    public PlayerAdapter(List<Player> playerList) {
        this.playerList = playerList;
    }

    @Override
    public PlayerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.player_list_row, parent, false);

        return new PlayerViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(PlayerViewHolder holder, int position) {
        Player player = playerList.get(position);
        holder.textViewNickName.setText(player.getNickname());
        holder.textViewWins.setText(String.valueOf(player.getWins()));
        holder.textViewLose.setText(String.valueOf(player.getLose()));
        holder.textViewDraw.setText(String.valueOf(player.getDraw()));
        holder.textViewPoints.setText(String.valueOf(player.getPoints()));
    }

    @Override
    public int getItemCount() {
        return playerList.size();
    }
}
