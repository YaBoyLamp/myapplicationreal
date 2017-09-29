package twiddledumber.myapplicationreal;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.EditText;

/**
 *  Activity for the Main Menu
 */

public class MainActivity extends AppCompatActivity {

    // unique identifiers for passed extras
    public final static String SCREEN_WIDTH = "twiddledumber.myapplicationreal_width";
    public final static String FINAL_SCORE = "twiddledumber.finalScore";

    /**
     *  Sets the view to be the main menu
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     *  Starts game on click of New Game text
     */
    public void playGame(View view) {
        Intent intent = new Intent(this, PlayGameActivity.class);

        // Gets screen width for width of board cells
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        intent.putExtra(SCREEN_WIDTH, metrics.widthPixels);

        startActivity(intent);
    }

    /**
     *  Open help screen upon click of help text
     */
    public void display_help(View v) {
        Intent intent = new Intent (this, Help.class);
        startActivity(intent);
    }
}
