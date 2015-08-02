package io.github.laurencemeynell.weightcalculator;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.TreeMap;


public class DisplayResults extends ActionBarActivity
{
    public static final String THE_WEIGHTS = "availableWeightsExtra";
    public static final String BAR_WEIGHT = "theBarWeight";
    public static final String TARGET = "theTargetWeight";

    private TreeMap<Double, Integer> availableWeights;
    private TreeMap<Double, Integer> weightsNeededForTarget;
    private double targetWeight;
    private double barWeight;
    private boolean targetMet;
    private WeightsCalc weightsCalc;

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
        barWeight = theIntent.getDoubleExtra(BAR_WEIGHT, 0.0);

        //calculate weights needed to achieve target and store whether target was met
        weightsCalc = new WeightsCalc();
        weightsCalc.setAvailableWeights(availableWeights);
        weightsCalc.setBarWeight(barWeight);
        targetMet = weightsCalc.calculateWeights(targetWeight);

        //get weights needed to achieve target
        weightsNeededForTarget = new TreeMap<>(weightsCalc.getTargetWeights());

        //weightNumber used to count which UI element I am on
        int weightNumber = 1;
        //anyWeightsUsed will tell if any weights were used to achieve target
        boolean anyWeightsUsed = false;

        String packageName = getPackageName();
        //display each weight needed in a new UI element
        for(double eachWeight : weightsNeededForTarget.keySet())
        {
            //If the weight is needed to meet the target
            if(weightsNeededForTarget.get(eachWeight) != 0)
            {
                //get the weight TextView and set it to the map key, make visible
                String weightTextId = "weight" + weightNumber;
                int weightResId = getResources().getIdentifier(weightTextId, "id", packageName);
                TextView weight = (TextView) findViewById(weightResId);
                weight.setText(eachWeight + " ");
                weight.setVisibility(View.VISIBLE);

                //get the x TextView make visible
                String xTextId = "x" + weightNumber;
                int xResId = getResources().getIdentifier(xTextId, "id", packageName);
                TextView x = (TextView) findViewById(xResId);
                x.setVisibility(View.VISIBLE);

                //get the number of weights TextView, set it to the map value, make visible
                String multiplierTextId = "multiplier" + (weightNumber);
                int multiplierResId = getResources().getIdentifier(multiplierTextId, "id", packageName);
                TextView multiplier = (TextView) findViewById(multiplierResId);
                multiplier.setText(" " + weightsNeededForTarget.get(eachWeight));
                multiplier.setVisibility(View.VISIBLE);

                anyWeightsUsed = true;

                //increment weightNumber for the next weight
                weightNumber++;
            }
        }

        //If no weights were used, display "Just the bar"
        if(!anyWeightsUsed)
        {
            TextView justBarTextId = (TextView) findViewById(R.id.weight1);
            justBarTextId.setText(getResources().getString(R.string.just_bar));
            justBarTextId.setVisibility(View.VISIBLE);
        }

        //If that target was not achieveable display a toast and set the background margin red.
        // Later I will add something better to warn the user
        if(!targetMet)
        {
            RelativeLayout margin = (RelativeLayout) findViewById(R.id.displayResultsMargin);
            margin.setBackgroundColor(Color.RED);

            Context context = getApplicationContext();
            CharSequence text = getResources().getString(R.string.can_not_make);
            int duration = Toast.LENGTH_LONG;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }
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
