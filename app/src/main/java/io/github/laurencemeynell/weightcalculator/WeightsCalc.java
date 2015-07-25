package io.github.laurencemeynell.weightcalculator;

import java.util.*;

/**
 * WeightsCalc is a prototype to test some concepts for a simple program
 * It works out how many weights you should put on one side of a barbell
 * given a target weight, the weight of your bar and your available weights.
 * <p/>
 * When run WeightsCalc will ask the user from the command line to input
 * the weight of their bar.  The it asks for the number and weights of the
 * plates owned.  Then it asks for a target weight and performs the calculation.
 * <p/>
 * If the user doesn't have the required plates to meet their target it shall warn them
 * and suggest a configuration that will get them as near as possible to their target.
 *
 * @author Laurence Meynell
 */
public class WeightsCalc
{
    private double barWeight;
    private SortedMap<Double, Integer> availableWeights;
    private SortedMap<Double, Integer> targetWeights;

    /**
     * Zero argument constructor for objects of class WeightsCalc
     * Initalises bar weight to 0.0 and creates an empty SortedMap of available weights
     */
    public WeightsCalc()
    {
        this(0.0, new TreeMap<Double, Integer>());
    }

    /**
     * Two argument contructor for objects of class WeightsCalc
     *
     * @param aBar               the weight of your bar
     * @param anAvailableWeights a SortedMap containing all your available weights
     *                           The key of your SortedMap should be the weight of your available plates
     *                           The value should be the number of those plates that you have
     */
    public WeightsCalc(double aBar, SortedMap<Double, Integer> anAvailableWeights)
    {
        this.barWeight = aBar;
        this.availableWeights = anAvailableWeights;
        this.targetWeights = new TreeMap<>();
    }

    /**
     * getBarWeight returns the weight of the bar
     *
     * @return the weight of the bar
     */
    public double getBarWeight()
    {
        return this.barWeight;
    }

    /**
     * getAvailableWeights returns a SortedMap of all the available weights
     *
     * @return all the available weights as a SortedMap
     * The key is the weight of your available plates
     * The value is the number of those plates that you have
     */
    public SortedMap<Double, Integer> getAvailableWeights()
    {
        return this.availableWeights;
    }

    /**
     * getTargetWeights returns a SortedMap of weights that match a weight target
     *
     * @return the weights that match your target weight as a SortedMap
     * The key is the weight of your targetted plates
     * The value is the number of those plates
     */
    public SortedMap<Double, Integer> getTargetWeights()
    {
        return this.targetWeights;
    }

    /**
     * Sets the weight of the bar to the parameter
     *
     * @param the weight of the bar to be set
     */
    public void setBarWeight(double aBarWeight)
    {
        this.barWeight = aBarWeight;
    }

    /**
     * Sets the available weights to the parameter
     *
     * @param a SortedMap of the available weights
     *          The key of your SortedMap should be the weight of your available plates
     *          The value should be the number of those plates that you have
     */
    public void setAvailableWeights(SortedMap<Double, Integer> anAvailableWeights)
    {
        this.availableWeights = anAvailableWeights;
    }

    /**
     * userInput gets input from the user from the command line
     * It returns this input as a String
     *
     * @return String whatever the user just inputted
     */
    private String userInput()
    {
        Scanner aScanner = new Scanner(System.in);
        String theInput = aScanner.nextLine();
        return theInput;
    }

    /**
     * Asks the user to input the weight of their bar and the weight
     * and number of any weight plates they have avilable.  Will keep
     * asking for new weights until the user presses enter.  Does
     * not do any error checking so input that can't be parsed will
     * cause the program to crash
     */
    public void inputWeights()
    {
        //create a String for all user input
        String input;
        //create Strings requesting user input
        String requestPlatesString = "Please enter the weight of a plate set or press enter to quit >";
        String requestNumberString = "Please enter the number of these plates >";

        //Ask user for initial input
        System.out.println("Please enter the weight of your bar >");

        //get the input and set the bar weight
        input = this.userInput();
        this.setBarWeight(Double.parseDouble(input));

        //Ask the user to enter the weight of the first set of plates
        System.out.println(requestPlatesString);
        input = this.userInput();
        while (!(input.equals("")))
        {
            //Initalise variables for new Map entry
            Double aWeight;
            Integer aNumberOfPlates;

            //Set the weight of the plates to user input
            aWeight = (Double.parseDouble(input));

            //Ask the user to enter the number of plates in this set
            System.out.println(requestNumberString);
            input = this.userInput();

            //Set the number of plates to user input
            aNumberOfPlates = (Integer.parseInt(input));

            //Add the inputted weights values to WeightCalc's Map of Weights
            this.getAvailableWeights().put(aWeight, aNumberOfPlates);

            //Ask the user for a new weight set
            System.out.println(requestPlatesString);
            input = this.userInput();
        }
    }

    /**
     * Returns an arrangement of available weights that will match the
     * target weight.  If the target weight is impossible to achieve with
     * the available weights it will return false, otherwise it will return
     * true
     *
     * @param aTarget the target weight you want to achieve
     * @return false if the target is unachieveable with your current weights
     * otherwise true
     */
    public boolean calculateWeights(Double aTarget)
    {
        SortedMap<Double, Integer> ourWeights = new TreeMap<>(Collections.reverseOrder());
        ourWeights.putAll(this.getAvailableWeights());

        this.getTargetWeights().clear();

        aTarget = aTarget - this.getBarWeight();

        for (Double aWeight : ourWeights.keySet())
        {
            int weightsToAdd = 0;
            for (int i = 0; i < (ourWeights.get(aWeight) / 2); i++)
            {
                if ((aWeight * 2) <= aTarget)
                {
                    weightsToAdd = weightsToAdd + 1;
                    aTarget = aTarget - (aWeight * 2);
                }
            }
            targetWeights.put(aWeight, weightsToAdd);
        }

        if (aTarget == 0.0)
        {
            return true;
        }
        else
        {
            return false;
        }

    }

    /**
     * runCalc asks the user to input their bar weight and available weights
     * Then it will asks the user to input a target weight and display how to
     * load the side of a barbell to achieve this target weight repeatedly
     * until the user enters an empty String for a target weight.
     * Performs no error checking so any input which can't be parsed will
     * cause the program to crash
     */
    public void runCalc()
    {
        //uncomment after calculate weights is developed and tested
        //this.inputWeights();

        //quickly populate a map for testing purposes
        this.setBarWeight(20);
        this.getAvailableWeights().put(20.0, 2);
        this.getAvailableWeights().put(15.0, 4);
        this.getAvailableWeights().put(10.0, 2);
        this.getAvailableWeights().put(5.0, 4);
        this.getAvailableWeights().put(2.5, 4);
        this.getAvailableWeights().put(1.25, 4);

        System.out.println(this.toString());

        String requestString = "Please enter a target weight or press enter to finish >";

        System.out.println(requestString);
        String input = this.userInput();
        while (!input.equals(""))
        {
            boolean targetMet = this.calculateWeights(Double.parseDouble(input));
            if (!targetMet)
            {
                System.out.println("Target weight not achievable, displaying nearest weight under target you can make");
            }
            System.out.println(this.targetWeightsString());
            System.out.println(requestString);
            input = this.userInput();
        }
    }

    /**
     * Returns the weight and number of each set of plates needed to make
     * the current target weight in descending order
     *
     * @return the weight and number of plates needed to make the target weight
     */
    public String targetWeightsString()
    {
        String returnString = "";

        returnString += "Weight : Numbers of plates on each side\n";

        //Create a new Treeset with it's ordering reversed to sort in descending order
        SortedSet<Double> theKeySet = new TreeSet<>(Collections.reverseOrder());
        //Add keys from availableWeights to new set
        theKeySet.addAll(this.getTargetWeights().keySet());

        //Iterate over this reservse ordered keyset
        for (Double aWeight : theKeySet)
        {
            returnString += aWeight + " : " +
                    this.getTargetWeights().get(aWeight) + "\n";
        }

        return returnString;
    }

    /**
     * toString returns the weight of the bar followed by the weight and number
     * of each set of plates in descending order
     *
     * @return the weight of the bar and number and weight of the plates
     */
    @Override
    public String toString()
    {
        String returnString = "";

        returnString += "Bar: " + this.getBarWeight() + "\n";
        returnString += "Weight : Numbers of plates\n";

        //Create a new Treeset with it's ordering reversed to sort in descending order
        SortedSet<Double> theKeySet = new TreeSet<>(Collections.reverseOrder());
        //Add keys from availableWeights to new set
        theKeySet.addAll(this.getAvailableWeights().keySet());

        //Iterate over this reservse ordered keyset
        for (Double aWeight : theKeySet)
        {
            returnString += aWeight + " : " +
                    this.getAvailableWeights().get(aWeight) + "\n";
        }

        return returnString;
    }

    /**
     * main method runs the WeightsCalc program in the command line
     */
    public static void main(String[] args)
    {
        WeightsCalc weightsCalc = new WeightsCalc();
        weightsCalc.runCalc();
    }
}
