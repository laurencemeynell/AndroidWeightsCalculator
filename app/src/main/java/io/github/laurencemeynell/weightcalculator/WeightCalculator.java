package io.github.laurencemeynell.weightcalculator;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Scanner;
import java.util.TreeMap;

public class WeightCalculator extends AppCompatActivity
{
    public static final String TEXT_TO_DISPLAY = "display";
    public static final String HELP = "help";
    public static final String ABOUT = "about";

    public int numOfWeights = 1;
    private String packageName;

    private TreeMap<Double, Integer> availableWeights;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weight_calculator);
        packageName  = getPackageName();
        if(savedInstanceState != null)
        {
            int numberOfWeights = savedInstanceState.getInt("numberOfWeights");
            for (int i = 1; i <= numberOfWeights; i++)
            {
                changeWeightSetVisibility(i, View.VISIBLE);
                numOfWeights = numberOfWeights;
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState)
    {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putInt("numberOfWeights", numOfWeights);
    }

    /**
     * Displays a toast
     * @param message the message to display
     * @param length the length of the message: Toast.LENGTH_LONG or Toast.LENGTH_SHORT
     */
    private void displayToast(String message, int length)
    {
        Context context = getApplicationContext();

        Toast toast = Toast.makeText(context, message, length);
        toast.show();
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
        Scanner weightScanner = new Scanner(currentString);
        if(weightScanner.hasNextDouble())
        {
            currentWeight = weightScanner.nextDouble();
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
     * Returns a reference to the EditText representing the weight of a weight set
     * at the specific line index
     * @param index the weight set's line index on the UI. 1 for 1st line, 2 for 2nd etc
     * @return the reference to the EditText UI element
     */
    private EditText getWeighRef(int index)
    {
        String packageName = getPackageName();

        String weightEditTextId = "weight" + (index);
        int weightResId = getResources().getIdentifier(weightEditTextId, "id", packageName);
        EditText weight = (EditText) findViewById(weightResId);

        return weight;
    }



    /**
     * Returns a reference to the Spinner representing the number of a weight set
     * at the specified line index
     * @param index the multiplier's line index on the UI.  1 for 1st, 2 for 2nd etc
     * @return the reference to the Spinner UI element
     */
    private Spinner getMultiplierRef(int index)
    {
        String spinnerId = "multiplier" + (index);
        int spinnerResId = getResources().getIdentifier(spinnerId, "id", packageName);
        Spinner multiplier = (Spinner) findViewById(spinnerResId);

        return multiplier;
    }

    /**
     * Returns a reference to the TextView representing the "*" of a weight set
     * at the specified line index
     * @param index the *'s line index on the UI.  1 for 1st, 2 for 2nd etc
     * @return the reference to the TextView UI element
     */
    private TextView getXRef(int index)
    {
        String xId = "x" + (index);
        int xResId = getResources().getIdentifier(xId, "id", packageName);
        TextView x = (TextView) findViewById(xResId);

        return x;
    }

    /**
     * Makes an available weight set visible or invisible depending on parameters
     * @param index the index of the weight set to change, starting from 1
     * @param visibility valid numbers are View.VISIBLE or View.INVISIBLE
     */
    private void changeWeightSetVisibility(int index, int visibility)
    {
        EditText weight = getWeighRef(index);
        TextView theX = getXRef(index);
        Spinner multiplier = getMultiplierRef(index);

        weight.setVisibility(visibility);
        theX.setVisibility(visibility);
        multiplier.setVisibility(visibility);
    }


    /**
     * If there are currently < 15 visible available weights addWeight will make another visible
     * If there are 15 visible weights it will display a toast stating 15 is the maximum weights
     * @param view the current view
     */
    public void addWeight(View view)
    {
        if(numOfWeights < 15)
        {
            changeWeightSetVisibility(numOfWeights + 1, View.VISIBLE);
            numOfWeights++;
        }
        else
        {
            displayToast("Cannot add weight set.  15 is the maximum", Toast.LENGTH_LONG);
        }
    }

    /**
     * If there is currently > 1 visible available weights removeWeight will make the last one
     * invisible and clear any user inputted data in it
     * If there are 0 visible weights it will display a toast stating none are available and
     * advise the user to add one
     * @param view the current view
     */
    public void removeWeight(View view)
    {
        if(numOfWeights > 0)
        {
            changeWeightSetVisibility(numOfWeights, View.INVISIBLE);

            EditText weight = getWeighRef(numOfWeights);
            Spinner multiplier = getMultiplierRef(numOfWeights);

            weight.setText(null);
            multiplier.setSelection(0);

            numOfWeights--;
        }
        else
        {
            displayToast("No weights visible.  Try adding weight sets to begin", Toast.LENGTH_LONG);
        }
    }

    /**
     * Saves all of the user inputted data from the UI into SharedPreferences
     * Then displays a toast that says "Saved"
     * @param view the current view
     */
    public void saveWeights(View view)
    {
        //make a shared preferences and editor
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        //get the bar weight
        EditText barWeight = (EditText) findViewById(R.id.bar);
        editor.putString("barWeight", barWeight.getText().toString());

        //get the weights and multipliers
        for(int i = 1; i <= numOfWeights; i++)
        {
            EditText weight = getWeighRef(i);
            Spinner multiplier = getMultiplierRef(i);
            editor.putString("weight" + i, weight.getText().toString());
            editor.putInt("multiplier" + i, multiplier.getSelectedItemPosition());
        }

        //get the target
        EditText targetWeight = (EditText) findViewById(R.id.target);
        editor.putString("targetWeight", targetWeight.getText().toString());

        //save the number of visible weight sets
        editor.putInt("numberOfWeights", numOfWeights);

        editor.apply();
        //show save toast
        displayToast("Saved", Toast.LENGTH_SHORT);
    }

    /**
     * Loads all the data from SharedPreferences and puts it into the UI
     * Then displays a toast that says "Loaded"
     * @param view the current view
     */
    public void loadWeights(View view)
    {
        //make a shared preferences
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);

        //set the bar weight
        EditText barWeight = (EditText) findViewById(R.id.bar);
        String barString = sharedPref.getString("barWeight", "");
        barWeight.setText(barString);

        //show the saved number of weight sets
        int savedWeightSets = sharedPref.getInt("numberOfWeights", 1);
        for (int i = 1; i <= savedWeightSets; i++)
        {
            changeWeightSetVisibility(i, View.VISIBLE);
        }

        //set the available weights and multipliers
        for(int i = 1; i <= savedWeightSets; i++)
        {
            EditText weight = getWeighRef(i);
            Spinner multiplier = getMultiplierRef(i);

            String weightValue = sharedPref.getString("weight" + i, "");
            int multiplierPos = sharedPref.getInt("multiplier" + i, 0);

            weight.setText(weightValue);
            multiplier.setSelection(multiplierPos);
        }

        numOfWeights = savedWeightSets;

        //set the target
        EditText targetWeight = (EditText) findViewById(R.id.target);
        String targetString = sharedPref.getString("targetWeight", "");
        targetWeight.setText(targetString);



        //show load toast
        displayToast("Loaded", Toast.LENGTH_SHORT);
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
        availableWeights = new TreeMap<>();

        //for each weight/multiplier UI pair, get a reference and pass to inputWeights
        for(int i = 1; i <= numOfWeights; i++)
        {
            EditText weight = getWeighRef(i);
            Spinner multiplier = getMultiplierRef(i);
            inputWeights(weight, multiplier);
        }

        //Get the bar weight
        EditText barWeight = (EditText) findViewById(R.id.bar);
        String barString = barWeight.getText().toString();

        //Gets the target weight
        EditText targetWeight = (EditText) findViewById(R.id.target);
        String targetString = targetWeight.getText().toString();

        //If the target and bar weights are valid, make an intent for DisplayResults
        Scanner targetScanner = new Scanner(targetString);
        Scanner barScanner = new Scanner(barString);
        if(targetScanner.hasNextDouble() && barScanner.hasNextDouble())
        {
            Double targetDouble = targetScanner.nextDouble();
            Double barDouble = barScanner.nextDouble();

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
            displayToast("Numbers not recognised try again", Toast.LENGTH_SHORT);
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

        switch (id)
        {
            case R.id.help:
                Intent helpIntent = new Intent(this, DisplayText.class);
                helpIntent.putExtra(TEXT_TO_DISPLAY, HELP);
                startActivity(helpIntent);
                return true;
            case R.id.about:
                Intent aboutIntent = new Intent(this, DisplayText.class);
                aboutIntent.putExtra(TEXT_TO_DISPLAY, ABOUT);
                startActivity(aboutIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
