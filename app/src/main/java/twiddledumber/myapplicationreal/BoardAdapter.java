package twiddledumber.myapplicationreal;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.DragEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


/**
 * Created by Thomas on 11/30/2015.
 *
 * An adapter that maintains the data for the board. Controls what blocks are displayed and
 * dynamically modifies the board in response to user input.
 *
 * mContext: the instance of game that this adpater belongs to
 * cells: a representation of the board where each cell contains the id of the image resource
 *        of the corresponding cell on the board
 * score: the current score of this instance of the game
 * sharedPref: local storage accessor for writing and accessing high scores
 * cellWidth: the width of one cell on the board. Based on screen width
 * scoreView: view containing current score and high score text views
 *
 */
public class BoardAdapter extends BaseAdapter {

    public final static String HIGH_SCORE = "HighScore";
    public final static int ROW_LENGTH = 10;
    public final static int COLUMN_LENGTH = 10;
    public final static int NUM_PIECES = 16;

    private Context mContext;
    private int[][] cells;
    private int score;
    private SharedPreferences sharedPref;
    private int cellWidth;
    private LinearLayout scoreView;

    /**
     *  Constructor for a BoardApapter. Initializes fields needed to generate the board.
     */
    public BoardAdapter(Context c, SharedPreferences s, int width, LinearLayout v) {
        mContext = c;
        cells = new int[ROW_LENGTH][COLUMN_LENGTH];
        score = 0;
        sharedPref = s;
        cellWidth = width;
        scoreView = v;
    }

    /**
     *  Returns number of ImageViews in the board
     */
    public int getCount() {
        return ROW_LENGTH * COLUMN_LENGTH;
    }

    /**
     *  Returns the id of the image resource used by the ImageView at a given position
     */
    public Object getItem(int position) {
        return (Integer)cells[position / ROW_LENGTH][position % ROW_LENGTH];
    }

    /**
     *  Returns the id of an ImageView based on its position
     */
    public long getItemId(int position) {
        return position;
    }

    /**
     *  Create a new ImageView for each item referenced by the Adapter
     */
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);

            // size cells to be 1/10 of the screen wide
            imageView.setLayoutParams(new GridView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, cellWidth));

            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(0, 0, 0, 0);

            // identify an ImageView by its position
            imageView.setId(position);
        } else {
            imageView = (ImageView) convertView;
        }

        // set the display image of an ImageView based on intended id
        if (cells[position / ROW_LENGTH][position % ROW_LENGTH] == 0)
            imageView.setImageResource(R.drawable.blank_cell);
        else
            imageView.setImageResource(cells[position / ROW_LENGTH][position % ROW_LENGTH]);

        // set piece placement listener for each ImageView on the board
        imageView.setOnDragListener(dropListener);

        return imageView;
    }

    /**
     * Listener for ImageViews to register uesr moves
     */
    View.OnDragListener dropListener = new View.OnDragListener() {


        @Override
        public boolean onDrag(View v, DragEvent event)
        {
            int dragEvent = event.getAction();
            switch(dragEvent)
            {
                // condition when piece is dropped
                case DragEvent.ACTION_DROP:

                    // get ImageView that piece was dropped on and the dropped piece
                    ImageView target = (ImageView) v;
                    ImageView dragged = (ImageView) event.getLocalState();
                    GridView draggedGrid = (GridView)dragged.getTag();

                    // get adapter for dragged piece and the position of the dragged ImageView in
                    // the piece
                    PieceAdapter adapter = (PieceAdapter) draggedGrid.getAdapter();
                    int draggedId = dragged.getId();

                    // calculate the offset between piece coordinates and board coordinates
                    int draggedRow = draggedId / PieceAdapter.MAX_HEIGHT;
                    int draggedCol = draggedId % PieceAdapter.MAX_WIDTH;

                    int targetRow = target.getId() / BoardAdapter.ROW_LENGTH;
                    int targetCol = target.getId() % BoardAdapter.COLUMN_LENGTH;

                    int rowOffset = targetRow - draggedRow;
                    int colOffset = targetCol - draggedCol;

                    // check if move is valid. If so make move and update board and piece
                    if (isValidMove(rowOffset, colOffset, adapter.getCells())) {
                        move(rowOffset, colOffset, adapter.getCells());
                        score += clear();
                        adapter.setTemplate((int) (Math.random() * NUM_PIECES));
                    }

                    // fade in new piece
                    draggedGrid.startAnimation(draggedGrid.getAnimation());
                    draggedGrid.setAlpha(1);

                    TextView currentScore = (TextView)scoreView.getChildAt(0);
                    TextView highScoreView = (TextView)scoreView.getChildAt(1);

                    // update score
                    currentScore.setText("Score: " + score);

                    // update high score if beaten
                    if (sharedPref.getInt(HIGH_SCORE, 0) < score) {
                        highScoreView.setText("High Score: " + score);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putInt(HIGH_SCORE, score);
                        editor.commit();
                    }

                    // move to loss screen if game is over
                    if (((PlayGameActivity)mContext).hasLost()) {
                        Intent intent = new Intent(mContext, LossActivity.class);
                        intent.putExtra(MainActivity.FINAL_SCORE, score);
                        mContext.startActivity(intent);
                        ((PlayGameActivity) mContext).finish();
                    }
            }
            return true;
        }
    };

    /**
     * Check if a move is valid. Offset is the amount a coordinate has to be shifted
     * to properly overlay piece on the board
     */
    public boolean isValidMove(int rowOffset, int colOffset, int[][] move){
        // for every ImageView in the piece check if there is an open slot
        for (int i = 0; i < PieceAdapter.MAX_HEIGHT; i++)
            for (int j = 0; j < PieceAdapter.MAX_WIDTH; j++)
                // if slot is filled or piece has to be placed off board return false
                if (move[i][j] != 0 && (rowOffset + i < 0 || i + rowOffset > 9 || colOffset + j < 0 || j + colOffset > 9 || cells[i + rowOffset][j + colOffset] != 0))
                    return false;
        return true;
    }

    /**
     *  Execute a validated move
     */
    public void move(int rowOffset, int colOffset, int[][]move) {
        // iterate through every block of a piece and reassign image ids on board
        for (int i = 0; i < PieceAdapter.MAX_HEIGHT; i++)
            for (int j = 0; j < PieceAdapter.MAX_WIDTH; j++)
                if (move[i][j] != 0) {
                    cells[i + rowOffset][j + colOffset] = move[i][j];
                }
        // update board
        notifyDataSetChanged();
    }

    /**
     *  Clear the board of 3 in a row color streaks. Returns number of blocks cleared
     *  for scoring
     */
    public int clear(){
        int count = 0;
        int currentCell;
        int horzPreviousCell = 0;
        int vertPreviousCell = 0;
        int horzStreak = 1;
        int vertStreak = 1;
        int newBoard[][] = new int[10][10];

        // iterate through every cell on the board
        for (int i = 0; i < ROW_LENGTH; i++) {
            for (int j = 0; j < COLUMN_LENGTH; j++) {

                // clear horizontal
                currentCell = cells[i][j];

                // reset streak for a new row
                if (j == 0)
                    horzStreak = 1;
                // increment streak if repeated cell and record cells to be cleared
                else if (horzPreviousCell != 0 && currentCell == horzPreviousCell) {
                    horzStreak++;
                    // record cells to be cleared for a long enough streak
                    if (horzStreak > 2)
                        for (int k = 0; k < horzStreak; k++)
                            newBoard[i][j - k] = 1;

                    // reset streak if at end of streak
                } else
                    horzStreak = 1;

                // update previousCell
                horzPreviousCell = currentCell;

                // clear vertical by traversing different axes
                currentCell = cells[j][i];

                // reset streak for a new column
                if (j == 0)
                    vertStreak = 1;
                // increment streak if repeated cell and record cells to be cleared
                else if (vertPreviousCell != 0 && currentCell == vertPreviousCell) {
                    vertStreak++;
                    // record cells to be cleared for long enough streak
                    if (vertStreak > 2)
                        for (int k = 0; k < vertStreak; k++)
                            newBoard[j - k][i] = 1;

                    // reset streak at end of streak
                } else
                    vertStreak = 1;

                // update previous cell
                vertPreviousCell = currentCell;
            }
        }

        // iterate through newBoard for cells to be cleared
        for (int row = 0; row < ROW_LENGTH; row++)
            for (int col = 0; col < COLUMN_LENGTH; col++)
                // clear cells that were flagged
                if (newBoard[row][col] == 1){
                    count++;
                    cells[row][col] = 0;
                }
        return count;
    }
}