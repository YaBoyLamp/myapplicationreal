package twiddledumber.myapplicationreal;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.TextView;

/**
 *  Activity for Screen at the end of Game. Shows user their score and buttons to play again
 *  and return to main menu.
 */

public class LossActivity extends AppCompatActivity {

    /**
     *  Retrieves user's score from intent and sets score into TextView
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loss);

        Intent intent = getIntent();
        int score = intent.getIntExtra(MainActivity.FINAL_SCORE, 0);

        TextView finalScore = (TextView)findViewById(R.id.finalScore);
        finalScore.setText("Score: " + score);
    }

    /**
     *  Redirects user to a new game upon button click
     *
     */
    public void retry(View v) {
        Intent intent = new Intent(this, PlayGameActivity.class);

        // Gets screen width for cell width
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        intent.putExtra(MainActivity.SCREEN_WIDTH, metrics.widthPixels);

        startActivity(intent);
        finish();
    }

    /**
     *  Redirects user to main menu upon button press
     */
    public void menu(View v) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

}
