package io.github.laurencemeynell.weightcalculator;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.SortedMap;
import java.util.TreeMap;
import org.apache.commons.lang3.math.NumberUtils;

public class WeightCalculator extends ActionBarActivity
{
    private WeightsCalc weightsCalc;
    private SortedMap<Double, Integer> availableWeights = new TreeMap<>();

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
    private void addWeights(EditText aWeight, Spinner aMultipler)
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
            }
            else
            {
                availableWeights.put(currentWeight, currentMultiplier);
            }
        }
    }

    /**
     * On press of Calculate button it will store the weight of the bar
     * and all available weights.  Then it will calculate how to achieve
     * the inputted target weight if that's possible using the available
     * weights.  The calculation will be displayed in another window.
     * @param view the current view
     */
    public void calculateWeights(View view)
    {
        //initialze variables
        weightsCalc = new WeightsCalc();

        //Get and input the bar weight
        EditText barWeight = (EditText) findViewById(R.id.bar);
        String barString = barWeight.getText().toString();
        if(NumberUtils.isNumber(barString))
        {
            Double barDouble = Double.parseDouble(barString);
            weightsCalc.setBarWeight(barDouble);
        }

        //If user has inputted a weight1 get and add it to availableWeights
        EditText weight1 = (EditText) findViewById(R.id.weight1);
        Spinner multiplier1 = (Spinner) findViewById(R.id.multiplier1);
        addWeights(weight1, multiplier1);

        //If user has inputted a weight2 get and add it to availableWeights
        EditText weight2 = (EditText) findViewById(R.id.weight2);
        Spinner multiplier2 = (Spinner) findViewById(R.id.multiplier2);
        addWeights(weight2, multiplier2);

        //If user has inputted a weight3 get and add it to availableWeights
        EditText weight3 = (EditText) findViewById(R.id.weight3);
        Spinner multiplier3 = (Spinner) findViewById(R.id.multiplier3);
        addWeights(weight3, multiplier3);

        //If user has inputted a weight4 get and add it to availableWeights
        EditText weight4 = (EditText) findViewById(R.id.weight4);
        Spinner multiplier4 = (Spinner) findViewById(R.id.multiplier4);
        addWeights(weight4, multiplier4);

        //If user has inputted a weight5 get and add it to availableWeights
        EditText weight5 = (EditText) findViewById(R.id.weight5);
        Spinner multiplier5 = (Spinner) findViewById(R.id.multiplier5);
        addWeights(weight5, multiplier5);

        //If user has inputted a weight6 get and add it to availableWeights
        EditText weight6 = (EditText) findViewById(R.id.weight6);
        Spinner multiplier6 = (Spinner) findViewById(R.id.multiplier6);
        addWeights(weight6, multiplier6);

        //Set weightsCalc's availableWeights to our newly generated map of available weights
        weightsCalc.setAvailableWeights(availableWeights);

        //Gets the target weight
        EditText targetWeight = (EditText) findViewById(R.id.target);
        //if the target weight has been set, calculate how to achieve it and display results
        String targetString = targetWeight.getText().toString();
        if(NumberUtils.isNumber(targetString))
        {
            Double targetDouble = Double.parseDouble(targetString);
            boolean targetMet = weightsCalc.calculateWeights(targetDouble);
            //display results in output TextView
            TextView results = (TextView) findViewById(R.id.output);
            String resultsString = "";
            if(!targetMet)
            {
                resultsString += ("Target is not achievable with your weights displaying " +
                        "nearest achievable weight under your target\n\n");
            }
            resultsString += weightsCalc.targetWeightsString();
            results.setText(resultsString);
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
