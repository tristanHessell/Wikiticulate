package tristan.hessell.pizza.wikiticulate.app;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.ArrayList;


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

    private ArrayList<CheckItem> exclusions;

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
                DurationPickerDialogFragment.newInstance( "Round Duration", new DurationDialogCallback()
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
                NumberPickerDialogFragment.newInstance( "Maximum Score", new NumberDialogCallback()
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
                //when the text view is clicked, show a dialog that will have number spinner
                NumberPickerDialogFragment.newInstance( "Number of Players", new NumberDialogCallback()
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

        exclusions = new ArrayList<>();
        exclusions.add( new CheckItem( "X (disambiguation") );
        exclusions.add( new CheckItem( "List of X" ) );

        tvExclusions = (TextView)findViewById( R.id.tvExclusions);
        tvExclusions.setText( String.valueOf( numPlayers ) ); //TODO what to put here
        tvExclusions.setOnClickListener( new View.OnClickListener()
        {
            @Override
            public void onClick( final View v )
            {
                StringListDialogFragment.newInstance( "Exclusions", new StringListDialogCallback()
                {
                    @Override
                    public void onCallback( ArrayList<CheckItem> inExclusions )
                    {
                        exclusions = inExclusions;
                        tvExclusions.setText( String.valueOf( exclusions.get( 0 ).isChecked() ) ); //TODO what to put here
                    }
                } )
                    .setListItems( exclusions )
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
        Intent intent = new Intent(this, MainActivity.class);
        //Put everything into a single object to cart the configuration data around in
        ConfigurationObject conf = new ConfigurationObject( numPlayers, (selectedMinutes* 60 + selectedSeconds) * 1000 , maxScore );
        //add the configuration data to the intent (which is used to move to the next activity
        intent.putExtra( "configuration", conf );
        //move to the next activity
        startActivity( intent );
    }
}
