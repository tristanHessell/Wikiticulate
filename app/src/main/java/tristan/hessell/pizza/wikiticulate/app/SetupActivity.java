package tristan.hessell.pizza.wikiticulate.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.NumberPicker;


public class SetupActivity extends AppCompatActivity
{
    private final int MIN_PLAYERS = 2;
    private final int MAX_PLAYERS = 4;
    private NumberPicker npNumPlayers;

    @Override
    protected void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_setup );

        //The number picker widjet that the user selects the number of players with
        npNumPlayers = (NumberPicker)findViewById( R.id.npNumPlayers );
        npNumPlayers.setMinValue( MIN_PLAYERS );
        npNumPlayers.setMaxValue( MAX_PLAYERS );


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
        intent.putExtra( "numPlayers", npNumPlayers.getValue() );

        startActivity( intent );
    }
}
