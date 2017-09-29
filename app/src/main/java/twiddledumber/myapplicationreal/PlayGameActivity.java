package twiddledumber.myapplicationreal;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.DragEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 *  An Activity that controls the flow and operation an instance of the game.
 */

public class PlayGameActivity extends AppCompatActivity {

    /**
     *  Instantiates an initial board state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.board);

        // retrieves screen size and calculates cellWidth
        Intent intent = getIntent();
        int cellWidth = intent.getIntExtra(MainActivity.SCREEN_WIDTH, 0)/ BoardAdapter.ROW_LENGTH;

        // gets user preferences for high score
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);

        // gets LinearLayout parent for draggable pieces
        LinearLayout activePieces = (LinearLayout)findViewById(R.id.activePieces);

        // instantiates each draggable piece
        for (int i = 0; i < 3; i++) {
            // gets draggable piece as a gridview
            GridView drag = (GridView)activePieces.getChildAt(i);

            // display piece with initial pieces
            drag.setVisibility(View.VISIBLE);
            drag.setAdapter(new PieceAdapter(this, cellWidth,(int)(Math.random() * BoardAdapter.NUM_PIECES), drag));

            // assign animation to fade images in when making a new piece
            Animation animationfadein = AnimationUtils.loadAnimation(this, R.anim.fade_in);
            drag.setAnimation(animationfadein);
            drag.startAnimation(animationfadein);
        }

        // add listener for invalid moves dropped anywhere on the screen
        LinearLayout screen = (LinearLayout)findViewById(R.id.screen);
        screen.setOnDragListener(dropListener);

        // initialize score view
        LinearLayout score = (LinearLayout)findViewById(R.id.score);
        TextView currentScore = (TextView)score.getChildAt(0);
        currentScore.setText("Score: 0");

        // initialize board with a new adapter
        GridView board = (GridView) findViewById(R.id.board);
        board.setAdapter(new BoardAdapter(this, sharedPref, cellWidth, score));

        // initialize highScore field with saved high score
        TextView highScore = (TextView)score.getChildAt(1);
        highScore.setText("High Score: " + sharedPref.getInt(BoardAdapter.HIGH_SCORE, 0));
    }

    /**
     *  check to see if the user has any remaining moves
     */
    public boolean hasLost() {
        // get board adapter for current board state
        GridView board = (GridView) findViewById(R.id.board);
        LinearLayout activePieces = (LinearLayout) findViewById(R.id.activePieces);
        BoardAdapter boardAdapter = (BoardAdapter) board.getAdapter();

        // iterate through each piece
        for (int i = 0; i < 3; i++) {
            PieceAdapter pieceAdapter = (PieceAdapter) ((GridView) activePieces.getChildAt(i)).getAdapter();
            // check each cell on board for a valid placement of piece
            for (int j = 0; j < BoardAdapter.ROW_LENGTH; j++) {
                for (int k = 0; k < BoardAdapter.COLUMN_LENGTH; k++) {
                    // offset assuming center block is placed on target
                    int rowOffset = j - 1;
                    int colOffset = k - 1;

                    // check for valid move
                    if (boardAdapter.isValidMove(rowOffset, colOffset, pieceAdapter.getCells()))
                        return false;
                }
            }
        }
        return true;
    }

    /**
     *  Gets a randomly selected image resource id of one of 4 colors
     */
    public static int getRandomColor() {
        int random = (int) (Math.random() * 4);
        if (random < 1)
            return R.drawable.blue_block;
        else if (random < 2)
            return R.drawable.red_block;
        else if (random < 3)
            return R.drawable.green_block;
        else
            return R.drawable.purple_block;
    }

    /**
     *  Listener for entire screen for an invalid move
     */
    View.OnDragListener dropListener = new View.OnDragListener() {
        @Override
        public boolean onDrag(View v, DragEvent event) {

            // unhides dragged piece on an invalid drop or drag
            if (event.getAction() == DragEvent.ACTION_DROP || event.getAction() == DragEvent.ACTION_DRAG_EXITED)
                ((GridView) ((ImageView) event.getLocalState()).getTag()).setAlpha(1);

            return true;
        }
    };

}
