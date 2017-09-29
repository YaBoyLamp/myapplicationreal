package twiddledumber.myapplicationreal;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * Created by Eddie on 12/6/2015.
 *
 * An activity to load the help screen
 */
public class Help extends AppCompatActivity {

    /**
     *  Shows user help xml document
     */
    @Override
    protected void onCreate (Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        setContentView(R.layout.help_display);
    }

    /**
     *  Ends help process and exits to menu when back button is clicked
     */
    public void back (View view) {
        this.finish();
    }

}


