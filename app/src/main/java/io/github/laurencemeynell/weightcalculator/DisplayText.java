package io.github.laurencemeynell.weightcalculator;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.widget.TextView;

public class DisplayText extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_text);

        TextView content = (TextView) findViewById(R.id.content);

        Intent intent = getIntent();
        String textToDisplay = intent.getStringExtra(WeightCalculator.TEXT_TO_DISPLAY);

        switch (textToDisplay)
        {
            case WeightCalculator.HELP:
                setTitle(getResources().getString(R.string.help));
                content.setText(getResources().getString(R.string.help_content));
                break;
            case WeightCalculator.ABOUT:
                setTitle(getResources().getString(R.string.about));
                content.setText(getResources().getString(R.string.about_content));

                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_display_text, menu);
        return true;
    }
}
