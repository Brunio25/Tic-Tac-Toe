package com.example.tictactoe;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button[][] buttons = new Button[3][3];
    private boolean player1 = true;
    private int numRounds;
    private TextView textP1;
    private int p1Points = 0;
    private TextView textP2;
    private int p2Points = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        numRounds = 0;

        textP1 = findViewById(R.id.text_view_p1);
        textP2 = findViewById(R.id.text_view_p2);
        textP1.setTypeface(null, Typeface.BOLD);

        for (int i = 0; i < buttons.length; i++) {
            for (int j  = 0; j < buttons.length; j++) {
                String buttonID = "button_" + i + j;
                int id = getResources().getIdentifier(buttonID, "id", getPackageName());

                buttons[i][j] = findViewById(id);
                buttons[i][j].setOnClickListener(this);
            }
        }

        Button reset = findViewById(R.id.button_reset);
        reset.setBackgroundColor(Color.rgb(187, 134, 252));
        
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                player1 = true;
                p1Points = 0;
                p2Points = 0;
                numRounds = 0;

                restartTextViews();

                clearBoard();
            }
        });
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        if (!((Button) v).getText().toString().equals("")) {
            return;
        }

        if (player1) {
            ((Button) v).setText("X");
        }
        else {
            ((Button) v).setText("O");
        }

        numRounds++;

        if (numRounds == 9) {
            Toast.makeText(this, "Draw!", Toast.LENGTH_SHORT).show();
            clearBoard();
            player1 = true;
        }
        else {
            boolean[] winner = winner();

            if (winner[0]) {
                if (winner[1]) {
                    p1Points++;
                    Toast.makeText(this, "Player 1 won!", Toast.LENGTH_SHORT).show();
                    this.textP1.setText("Player 1 = " + p1Points);
                }
                else {
                    p2Points++;
                    Toast.makeText(this, "Player 2 won!", Toast.LENGTH_SHORT).show();
                    this.textP2.setText("Player 2 = " + p2Points);
                }

                clearBoard();
                player1 = true;
                numRounds = 0;
            }
            else {
                player1 = !player1;
            }
        }
        changeHighlight();
    }

    /**
     * Method that specifies if the game is over, and which player wins.
     * @return an array of booleans specifying, respectively, whether there is a winner, and if the
     * winner is or not player 1
     *
     * @requires game not to be a draw
     * @ensures {@code \result.length == 2}
     */
    private boolean[] winner() {
        boolean[] res = new boolean[2];

        for (int i = 0; i < buttons.length && !res[0]; i++) {
            boolean notIt = false;
            int xCounter = 0;
            int oCounter = 0;

            for (int j = 0; j < buttons.length && !notIt; j++) {
                if (buttons[i][j].getText().toString().equals("")) {
                    notIt = true;
                }
                else {
                    if (buttons[i][j].getText().toString().equals("X")) {
                        xCounter++;

                        notIt = oCounter != 0;
                    }
                    else {
                        oCounter++;

                        notIt = xCounter != 0;
                    }
                }
            }

            if (!notIt) {
                res[0] = true;
                res[1] = xCounter == 3;
            }
        }

        for (int i = 0; i < buttons.length && !res[0]; i++) {
            boolean notIt = false;
            int xCounter = 0;
            int oCounter = 0;

            for (int j = 0; j < buttons.length && !notIt; j++) {
                if (buttons[j][i].getText().toString().equals("")) {
                    notIt = true;
                }
                else {
                    if (buttons[j][i].getText().toString().equals("X")) {
                        xCounter++;

                        notIt = oCounter != 0;
                    }
                    else {
                        oCounter++;

                        notIt = xCounter != 0;
                    }
                }
            }

            if (!notIt) {
                res[0] = true;
                res[1] = xCounter == 3;
            }
        }

        if (!res[0]) {
            boolean notIt = false;
            String temp = buttons[0][0].getText().toString();

            for (int i = 1; i < buttons.length && !notIt; i++) {
                if (buttons[i][i].getText().toString().equals("") ||
                        !buttons[i][i].getText().toString().equals(temp) ) {
                    notIt = true;
                }
            }

            if (!notIt) {
                res[0] = true;
                res[1] = temp.equals("X");
            }
        }

        if (!res[0]) {
            boolean notIt = false;
            String temp = buttons[0][buttons.length - 1].getText().toString();

            for (int i = 0; i < buttons.length && !notIt; i++) {
                if (buttons[i][buttons.length - i - 1].getText().toString().equals("") ||
                        !buttons[i][buttons.length - i - 1].getText().toString().equals(temp) ) {
                    notIt = true;
                }
            }

            if (!notIt) {
                res[0] = true;
                res[1] = temp.equals("X");
            }
        }

        return res;
    }

    private void clearBoard() {
        for (Button[] buttons : buttons) {
            for (Button button : buttons) {
                button.setText("");
            }
        }
    }

    private void changeHighlight() {
        if (player1) {
            textP1.setTypeface(null, Typeface.BOLD);
            textP2.setTypeface(null, Typeface.NORMAL);
        }
        else {
            textP1.setTypeface(null, Typeface.NORMAL);
            textP2.setTypeface(null, Typeface.BOLD);
        }
    }

    public void restartTextViews() {
        this.textP1.setText("Player 1 = " + p1Points);
        this.textP2.setText("Player 2 = " + p2Points);
        textP1.setTypeface(null, Typeface.BOLD);
        textP2.setTypeface(null, Typeface.NORMAL);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt("numRounds", numRounds);
        outState.putInt("p1Points", p1Points);
        outState.putInt("p2Points", p2Points);
        outState.putBoolean("player1", player1);
    }

    /**
     * This method is called after {@link #onStart} when the activity is
     * being re-initialized from a previously saved state, given here in
     * <var>savedInstanceState</var>.  Most implementations will simply use {@link #onCreate}
     * to restore their state, but it is sometimes convenient to do it here
     * after all of the initialization has been done or to allow subclasses to
     * decide whether to use your default implementation.  The default
     * implementation of this method performs a restore of any view state that
     * had previously been frozen by {@link #onSaveInstanceState}.
     *
     * <p>This method is called between {@link #onStart} and
     * {@link #onPostCreate}. This method is called only when recreating
     * an activity; the method isn't invoked if {@link #onStart} is called for
     * any other reason.</p>
     *
     * @param savedInstanceState the data most recently supplied in {@link #onSaveInstanceState}.
     * @see #onCreate
     * @see #onPostCreate
     * @see #onResume
     * @see #onSaveInstanceState
     */
    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        numRounds = savedInstanceState.getInt("numRounds");
        p1Points = savedInstanceState.getInt("p1Points");
        p2Points = savedInstanceState.getInt("p2Points");
        player1 = savedInstanceState.getBoolean("player1");
    }
}