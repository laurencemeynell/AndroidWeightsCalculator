package io.github.laurencemeynell.weightcalculator;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.HashMap;
import java.util.TreeMap;


public class DisplayResults extends ActionBarActivity
{
    public static final String THE_WEIGHTS = "availableWeightsExtra";
    public static final String TARGET = "theTargetWeight";

    private TreeMap<Double, Integer> availableWeights;
    double targetWeight;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_results);

        Intent theIntent = getIntent();

        //retrieve HashMap referenced by THE_WEIGHTS and cast it into availableWeights
        availableWeights = new TreeMap<>((HashMap) theIntent.getSerializableExtra(THE_WEIGHTS));
        //retrieve double referenced by TARGET
        targetWeight = theIntent.getDoubleExtra(TARGET, 0.0);

        TextView results = (TextView) findViewById(R.id.textView1);
        results.setText(availableWeights.toString());
        results.append("\n" + targetWeight);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_display_results, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)
        {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
