package com.example.myapplication;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class GameFragment extends Fragment implements View.OnClickListener {

    private SQLiteDatabase db;
    private TextView playerOneScore, playerTwoScore, playerStatus, playerOneNameTextView, playerTwoNameTextView;
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

    private String playerOneName = ""; // Store player one's name
    private String playerTwoName = ""; // Store player two's name

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_game, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        db = new DBHelper(getContext()).getWritableDatabase();

        playerOneScore = view.findViewById(R.id.score_Player1);
        playerTwoScore = view.findViewById(R.id.score_Player2);
        playerStatus = view.findViewById(R.id.textStatus);
        reset = view.findViewById(R.id.btn_reset);
        playAgain = view.findViewById(R.id.btn_play_again);
        playerOneNameTextView = view.findViewById(R.id.text_player1);
        playerTwoNameTextView = view.findViewById(R.id.text_player2);

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
                playerStatus.setText(playerOneChoosesX ? playerOneName + " has won this round" : playerTwoName + " has won this round");
            } else {
                playerTwoScoreCount++;
                playerStatus.setText(playerTwoName + " has won this round");
            }

            if (playerOneScoreCount == 3) {
                Toast.makeText(getActivity(), playerOneName + " wins the game!", Toast.LENGTH_SHORT).show();
                playerStatus.setText(playerOneName + " has won the game!");
                updatePlayerScore();
                resetGame();
            } else if (playerTwoScoreCount == 3) {
                Toast.makeText(getActivity(), playerTwoName + " wins the game!", Toast.LENGTH_SHORT).show();
                playerStatus.setText(playerTwoName + " has won the game!");
                updatePlayerScore();
                resetGame();
            } else {
                updatePlayerScore();
            }
        } else if (rounds == 16) {
            playerStatus.setText("It's a draw!");
            playAgain();
        } else {
            isPlayerOneTurn = !isPlayerOneTurn;
            playerStatus.setText(isPlayerOneTurn ? playerOneName + "'s Turn" : playerTwoName + "'s Turn");
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
        playerStatus.setText(playerOneName + "'s Turn");

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
                .setMessage(playerOneName + ", choose X or O")
                .setPositiveButton("X", (dialog, which) -> {
                    playerOneChoosesX = true;
                    playerStatus.setText(playerOneName + "'s Turn");
                    showPlayerTwoNameDialog();
                })
                .setNegativeButton("O", (dialog, which) -> {
                    playerOneChoosesX = false;
                    playerStatus.setText(playerOneName + "'s Turn");
                    showPlayerTwoNameDialog();
                })
                .setCancelable(false)
                .show();
    }

    private void showPlayerTwoNameDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_player_name, null);
        builder.setView(dialogView);

        EditText editTextNickname = dialogView.findViewById(R.id.editTextNickname);

        builder.setTitle("Player-2 Name")
                .setPositiveButton("OK", (dialog, which) -> {
                    String nickname = editTextNickname.getText().toString().trim();
                    if (!nickname.isEmpty()) {
                        insertPlayerTwoName(nickname);
                        playerTwoName = nickname;
                        playerTwoNameTextView.setText(playerTwoName);
                        playerStatus.setText(playerOneName + "'s Turn");
                    } else {
                        Toast.makeText(getActivity(), "Please enter a nickname for Player-2", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", (dialog, which) -> {
                    // Handle cancel if needed
                })
                .setCancelable(false)
                .show();
    }

    private void insertPlayerTwoName(String nickname) {
        ContentValues values = new ContentValues();
        values.put("nickname", nickname);
        db.insert("PLAYER", null, values);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        db.close(); // Close the database connection when fragment is destroyed
    }

    // Fetch player names from database
// Fetch player names from database
    private void fetchPlayerNames() {
        // Query for Player-1 name from Host table
        Cursor cursorHost = db.query("Host", new String[]{"nickname"}, null, null, null, null, null);
        if (cursorHost.moveToFirst()) {
            int nicknameIndex = cursorHost.getColumnIndex("nickname");
            if (nicknameIndex >= 0) {
                playerOneName = cursorHost.getString(nicknameIndex);
            } else {
                // Log an error or handle the situation where "nickname" column is not found
                // Example: Log.e(TAG, "Column 'nickname' not found in Host table");
            }
        }
        cursorHost.close();

        // Query for Player-2 name from Player table
        Cursor cursorPlayer = db.query("Player", new String[]{"nickname"}, null, null, null, null, null);
        if (cursorPlayer.moveToFirst()) {
            int nicknameIndex = cursorPlayer.getColumnIndex("nickname");
            if (nicknameIndex >= 0) {
                playerTwoName = cursorPlayer.getString(nicknameIndex);
            } else {
                // Log an error or handle the situation where "nickname" column is not found
                // Example: Log.e(TAG, "Column 'nickname' not found in Player table");
            }
        }
        cursorPlayer.close();

        // Update TextViews with player names
        playerOneNameTextView.setText(playerOneName);
        playerTwoNameTextView.setText(playerTwoName);
    }


    @Override
    public void onResume() {
        super.onResume();
        fetchPlayerNames();
    }
}
