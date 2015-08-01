package io.github.laurencemeynell.weightcalculator;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.SortedMap;
import java.util.TreeMap;
import org.apache.commons.lang3.math.NumberUtils;

public class WeightCalculator extends ActionBarActivity
{
    public static final int MAX_NUM_OF_WEIGHTS = 6;

    private WeightsCalc weightsCalc;
    private TreeMap<Double, Integer> availableWeights;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weight_calculator);
    }

    /**
     * If aWeight contains a valid weight then first the Map is checked
     * to see if it already contains this weight as a key.  If the key
     * already exists then the multiplier value for this weight is added
     * to the value of the existing key.  If the weight doesn't already
     * exist as a key is the Map then the weight and multipler are used
     * to create a new key/value pair.
     * @param aWeight the current weight
     * @param aMultipler the current muliplier
     */
    private void inputWeights(EditText aWeight, Spinner aMultipler)
    {
        String currentString;
        Double currentWeight;
        Integer currentMultiplier;

        currentString = aWeight.getText().toString();
        if(NumberUtils.isNumber(currentString))
        {
            currentWeight = Double.parseDouble(currentString);
            currentMultiplier = Integer.parseInt(String.valueOf(aMultipler.getSelectedItem()));
            //if the weight already exists in the map add the multiplier to the stored weight
            if(availableWeights.containsKey(currentWeight))
            {
                currentMultiplier += availableWeights.get(currentWeight);
                availableWeights.put(currentWeight, currentMultiplier);
            }
            else
            {
                availableWeights.put(currentWeight, currentMultiplier);
            }
        }
    }

    /**
     * On press of Calculate button it will store the weight of the bar
     * and all available weights.  Then it will then pass these values
     * to DisplayResults.
     *
     * @param view the current view
     */
    public void calculateWeights(View view)
    {
        //initialze variables
        weightsCalc = new WeightsCalc();
        availableWeights = new TreeMap<>();
        Double barDouble;

        //make arrays for the weights and weight multiplier UI elements
        EditText[] weights = new EditText[MAX_NUM_OF_WEIGHTS];
        Spinner[] spinners = new Spinner[MAX_NUM_OF_WEIGHTS];

        //for each element, read the values pass to inputWeights
        for(int i = 0; i < MAX_NUM_OF_WEIGHTS; i++)
        {
            String packageName = getPackageName();

            String weightEditTextId = "weight" + (i + 1);
            int weightResId = getResources().getIdentifier(weightEditTextId, "id", packageName);
            weights[i] = (EditText) findViewById(weightResId);

            String spinnerId = "multiplier" + (i + 1);
            int spinnerResId = getResources().getIdentifier(spinnerId, "id", packageName);
            spinners[i] = (Spinner) findViewById(spinnerResId);

            inputWeights(weights[i], spinners[i]);
        }

        //Get the bar weight
        EditText barWeight = (EditText) findViewById(R.id.bar);
        String barString = barWeight.getText().toString();

        //Gets the target weight
        EditText targetWeight = (EditText) findViewById(R.id.target);
        String targetString = targetWeight.getText().toString();

        //If the target and bar weights are valid, make an intent for DisplayResults
        if(NumberUtils.isNumber(targetString) || NumberUtils.isNumber(barString))
        {
            Double targetDouble = Double.parseDouble(targetString);
            barDouble = Double.parseDouble(barString);
            boolean targetMet = weightsCalc.calculateWeights(targetDouble);

            //Start DisplayResults and pass available weights, bar and target weight
            Intent displayResultsIntent = new Intent(this, DisplayResults.class);
            displayResultsIntent.putExtra(DisplayResults.THE_WEIGHTS, availableWeights);
            displayResultsIntent.putExtra(DisplayResults.TARGET, targetDouble);
            displayResultsIntent.putExtra(DisplayResults.BAR_WEIGHT, barDouble);
            startActivity(displayResultsIntent);
        }
        else
        {
            //If the user enters invalid numbers for target or bar display a warning toast
            Context context = getApplicationContext();
            CharSequence text = "Numbers not recognised try again";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_weight_calculator, menu);
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
