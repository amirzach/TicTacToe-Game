package com.example.myapplication;

import android.app.AlertDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class GameFragment extends Fragment implements View.OnClickListener {

    private TextView playerOneScore, playerTwoScore, playerStatus;
    private Button[] buttons = new Button[16];
    private Button reset, playAgain;

    private int[] gameState = new int[16];
    private int[][] winningPositions = {
            {0, 1, 2, 3}, {4, 5, 6, 7}, {8, 9, 10, 11}, {12, 13, 14, 15}, // Rows
            {0, 4, 8, 12}, {1, 5, 9, 13}, {2, 6, 10, 14}, {3, 7, 11, 15}, // Columns
            {0, 5, 10, 15}, {3, 6, 9, 12} // Diagonals
    };

    private int rounds = 0;
    private boolean isPlayerOneTurn = true;
    private boolean playerOneChoosesX = true; // Track player one's choice
    private int playerOneScoreCount = 0;
    private int playerTwoScoreCount = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_game, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        playerOneScore = view.findViewById(R.id.score_Player1);
        playerTwoScore = view.findViewById(R.id.score_Player2);
        playerStatus = view.findViewById(R.id.textStatus);
        reset = view.findViewById(R.id.btn_reset);
        playAgain = view.findViewById(R.id.btn_play_again);

        // Initialize gameState for 16 buttons
        for (int i = 0; i < 16; i++) {
            gameState[i] = 2; // 0 for X, 1 for O, 2 for empty
        }

        buttons[0] = view.findViewById(R.id.btn0);
        buttons[1] = view.findViewById(R.id.btn1);
        buttons[2] = view.findViewById(R.id.btn2);
        buttons[3] = view.findViewById(R.id.btn3);
        buttons[4] = view.findViewById(R.id.btn4);
        buttons[5] = view.findViewById(R.id.btn5);
        buttons[6] = view.findViewById(R.id.btn6);
        buttons[7] = view.findViewById(R.id.btn7);
        buttons[8] = view.findViewById(R.id.btn8);
        buttons[9] = view.findViewById(R.id.btn9);
        buttons[10] = view.findViewById(R.id.btn10);
        buttons[11] = view.findViewById(R.id.btn11);
        buttons[12] = view.findViewById(R.id.btn12);
        buttons[13] = view.findViewById(R.id.btn13);
        buttons[14] = view.findViewById(R.id.btn14);
        buttons[15] = view.findViewById(R.id.btn15);

        for (Button button : buttons) {
            button.setOnClickListener(this);
        }

        reset.setOnClickListener(v -> {
            playAgain();
            playerOneScoreCount = 0;
            playerTwoScoreCount = 0;
            updatePlayerScore();
        });

        playAgain.setOnClickListener(v -> playAgain());

        // Prompt user to choose X or O at the start
        showChooseSymbolDialog();
    }

    @Override
    public void onClick(View view) {
        Button clickedButton = (Button) view;
        int clickedIndex = Integer.parseInt(clickedButton.getTag().toString());

        if (gameState[clickedIndex] != 2) {
            return; // If the button is already clicked, do nothing
        }

        if (isPlayerOneTurn) {
            clickedButton.setText(playerOneChoosesX ? "X" : "O");
            clickedButton.setTextColor(Color.parseColor("#000000")); // Set color to black
            gameState[clickedIndex] = playerOneChoosesX ? 0 : 1;
        } else {
            clickedButton.setText(playerOneChoosesX ? "O" : "X"); // Opponent gets opposite symbol
            clickedButton.setTextColor(Color.parseColor("#000000")); // Set color to black
            gameState[clickedIndex] = playerOneChoosesX ? 1 : 0;
        }

        rounds++;

        if (checkWinner()) {
            if (isPlayerOneTurn) {
                playerOneScoreCount++;
                playerStatus.setText("Player-1 has won this round");
            } else {
                playerTwoScoreCount++;
                playerStatus.setText("Player-2 has won this round");
            }

            if (playerOneScoreCount == 3) {
                Toast.makeText(getActivity(), "Player-1 wins the game!", Toast.LENGTH_SHORT).show();
                playerStatus.setText("Player-1 has won the game!");
                resetGame();
            } else if (playerTwoScoreCount == 3) {
                Toast.makeText(getActivity(), "Player-2 wins the game!", Toast.LENGTH_SHORT).show();
                playerStatus.setText("Player-2 has won the game!");
                resetGame();
            }

            updatePlayerScore();
        } else if (rounds == 16) {
            playerStatus.setText("It's a draw!");
            playAgain();
        } else {
            isPlayerOneTurn = !isPlayerOneTurn;
            playerStatus.setText(isPlayerOneTurn ? "Player-1's Turn" : "Player-2's Turn");
        }
    }

    private boolean checkWinner() {
        for (int[] winningPosition : winningPositions) {
            if (gameState[winningPosition[0]] == gameState[winningPosition[1]]
                    && gameState[winningPosition[1]] == gameState[winningPosition[2]]
                    && gameState[winningPosition[2]] == gameState[winningPosition[3]]
                    && gameState[winningPosition[0]] != 2) {
                return true;
            }
        }
        return false;
    }

    private void updatePlayerScore() {
        playerOneScore.setText(String.valueOf(playerOneScoreCount));
        playerTwoScore.setText(String.valueOf(playerTwoScoreCount));
    }

    private void playAgain() {
        rounds = 0;
        isPlayerOneTurn = true;
        playerOneChoosesX = true;
        playerStatus.setText("Player-1's Turn");

        for (int i = 0; i < 16; i++) {
            gameState[i] = 2;
            buttons[i].setText("");
            buttons[i].setTextColor(Color.parseColor("#000000"));
        }
    }

    private void resetGame() {
        playerOneScoreCount = 0;
        playerTwoScoreCount = 0;
        updatePlayerScore();
        playAgain();
    }

    private void showChooseSymbolDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Choose Symbol")
                .setMessage("Player-1, choose X or O")
                .setPositiveButton("X", (dialog, which) -> {
                    playerOneChoosesX = true;
                    playerStatus.setText("Player-1's Turn");
                })
                .setNegativeButton("O", (dialog, which) -> {
                    playerOneChoosesX = false;
                    playerStatus.setText("Player-1's Turn");
                })
                .setCancelable(false)
                .show();
    }
}
