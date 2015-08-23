package com.thirteen.dialogs;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import com.thirteen.wikiticulate.app.ConfigurationObject;
import com.thirteen.wikiticulate.app.MainActivity;
import com.thirteen.wikiticulate.app.R;
import com.thirteen.wikiticulate.app.WikitulateApplication;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;


public class SetupActivity extends AppCompatActivity
{
    private final int MIN_PLAYERS = 2;
    private final int DEFAULT_PLAYERS = 2;
    private final int MAX_PLAYERS = 5;

    private final int MAX_ROUND_DURATION_MINUTES = 10;
    private final int DEFAULT_ROUND_DURATION_MINUTES = 5;
    private final int DEFAULT_ROUND_DURATION_SECONDS = 0;
    private final int MAX_ROUND_DURATION_SECONDS = 59;

    private final int MIN_SCORE = 1;
    private final int DEFAULT_SCORE = 10;
    private final int MAX_SCORE = 20;

    private Button btnStart;

    private TextView tvDuration;
    private TextView tvMaxScore;
    private TextView tvNumPlayers;
    private CheckBox cbMaxScore;
    private CheckBox cbDuration;

    private int maxScore = DEFAULT_SCORE;
    private int numPlayers = DEFAULT_PLAYERS;
    private int selectedMinutes = DEFAULT_ROUND_DURATION_MINUTES;
    private int selectedSeconds = DEFAULT_ROUND_DURATION_SECONDS;
    private CheckBox cbExclusions;
    private TextView tvExclusions;

    private ArrayList<CheckItem> exclusionCheckList; //used in the checklist
    private Map<String, String> exclusionMap; //used to map the exclusion text to the regex
    private Pattern exclusionRegex; //the final compiled regex

    @Override
    protected void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_setup );

        tvDuration = (TextView)findViewById( R.id.tvRoundDuration );
        tvDuration.setText( String.format( "%02d:%02d", selectedMinutes, selectedSeconds ) );
        tvDuration.setOnClickListener( new View.OnClickListener()
        {
            @Override
            public void onClick( final View v )
            {
            DurationPickerDialogFragment.newInstance( "Round Duration", new DialogCallback()
            {
                @Override
                public void onCallback( int minutes, int seconds )
                {
                    selectedMinutes = minutes;
                    selectedSeconds = seconds;
                    tvDuration.setText( String.format( "%02d:%02d", minutes, seconds ) );
                }
            } )
                .setMinimumDuration( 0, 0 )
                .setDefaultDuration( selectedMinutes, selectedSeconds )
                .setMaxmimumDuration( MAX_ROUND_DURATION_MINUTES, MAX_ROUND_DURATION_SECONDS )
                .show( getSupportFragmentManager(), "dlg" );
            }
        } );

        cbDuration = (CheckBox)findViewById( R.id.cbDuration );
        cbDuration.setOnCheckedChangeListener( new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged( CompoundButton buttonView, boolean isChecked )
            {
                tvDuration.setEnabled( isChecked );
            }
        } );

        tvMaxScore = (TextView)findViewById( R.id.tvMaxScore );
        tvMaxScore.setText( String.valueOf( maxScore ) );
        tvMaxScore.setOnClickListener( new View.OnClickListener()
        {
            @Override
            public void onClick( final View v )
            {
                NumberPickerDialogFragment.newInstance( "Maximum Score", new DialogCallback()
                {
                    @Override
                    public void onCallback( int number )
                    {
                        tvMaxScore.setText( String.valueOf( number ) );
                        maxScore = number;
                    }
                } )
                .setDefaultValue( maxScore )
                .setMinimumValue( MIN_SCORE )
                .setMaximumValue( MAX_SCORE )
                .show( getSupportFragmentManager(), "dlg" );
            }
        } );

        cbMaxScore = (CheckBox)findViewById( R.id.cbMaxScore );
        cbMaxScore.setOnCheckedChangeListener( new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged( CompoundButton buttonView, boolean isChecked )
            {
                tvMaxScore.setEnabled( isChecked );
            }
        } );

        tvNumPlayers = (TextView)findViewById( R.id.tvNumPlayers );
        tvNumPlayers.setText( String.valueOf( numPlayers ) );
        tvNumPlayers.setOnClickListener( new View.OnClickListener()
        {
            @Override
            public void onClick( final View v )
            {
                NumberPickerDialogFragment.newInstance( "Number of Players", new DialogCallback()
                {
                    @Override
                    public void onCallback( int number )
                    {
                        tvNumPlayers.setText( String.valueOf( number ) );
                        numPlayers = number;
                    }
                } )
                    .setMinimumValue( MIN_PLAYERS )
                    .setMaximumValue( MAX_PLAYERS )
                    .setDefaultValue( numPlayers )
                    .show( getSupportFragmentManager(), "dlg" );
            }
        } );

        cbExclusions = (CheckBox)findViewById( R.id.cbExclusions);
        cbExclusions.setOnCheckedChangeListener( new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged( CompoundButton buttonView, boolean isChecked )
            {
                tvExclusions.setEnabled( isChecked );
            }
        } );

        exclusionCheckList = new ArrayList<>();
        exclusionCheckList.add( new CheckItem( "X (disambiguation)" ) );
        exclusionCheckList.add( new CheckItem( "List of X" ) );
        exclusionMap = new HashMap<>();
        exclusionMap.put( "X (disambiguation)", ".* \\(disambiguation\\)$" );
        exclusionMap.put( "List of X", "^List of .*" );

        tvExclusions = (TextView)findViewById( R.id.tvExclusions);
        tvExclusions.setText( "See" );
        tvExclusions.setOnClickListener( new View.OnClickListener()
        {
            @Override
            public void onClick( final View v )
            {
                CheckListDialogFragment.newInstance( "Exclusions", new DialogCallback()
                {
                    @Override
                    public void onCallback( ArrayList<CheckItem> inExclusions )
                    {
                        /*save the users selections - so if they change their mind, their current selection still stays */
                        exclusionCheckList = inExclusions;
                        //TODO make this cleaner
                        //from the selected inclusions, make the regex string
                        StringBuilder strBuild = new StringBuilder();
                        for( CheckItem ch : exclusionCheckList )
                        {
                            if( ch.isChecked() )
                            {
                                if( strBuild.length() != 0 )
                                {
                                    strBuild.append( "|" );
                                }
                                strBuild.append( exclusionMap.get( ch.toString() ) );
                            }
                        }
                        exclusionRegex = Pattern.compile( strBuild.toString() );
                    }
                } )
                    .setListItems( exclusionCheckList )
                    .show( getSupportFragmentManager(), "dlg" );
            }
        } );
    }

    @Override
    public boolean onCreateOptionsMenu( Menu menu )
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate( R.menu.menu_setup, menu );
        return true;
    }

    @Override
    public boolean onOptionsItemSelected( MenuItem item )
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if( id == R.id.action_settings )
        {
            return true;
        }

        return super.onOptionsItemSelected( item );
    }

    /**
     * Function called when the start game button is clicked.
     * Takes the user to the game screen.
     *
     * @param v Required via assigning the onClick via XML
     */
    public void btnStartGameClick(View v)
    {
        //TODO have no exclusion regex if the cb is unchecked.
        //Put everything into a single object to cart the configuration data around in
        ConfigurationObject conf = new ConfigurationObject( numPlayers, (selectedMinutes* 60 + selectedSeconds) * 1000 , maxScore, exclusionRegex );
        //set the configuration of the application
        ((WikitulateApplication)getApplication()).setConfiguration(conf);
        //move onto the next activity
        startActivity( new Intent(this, MainActivity.class) );
    }
}
