package tristan.hessell.pizza.wikiticulate.app;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity
{
    private int numPlayers;

    @Override
    protected void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );


        numPlayers = getIntent().getIntExtra( "numPlayers", -1 );
        //if(numPlayers == -1)
        //{
            //TODO cry as the number of players wasnt set.
        //}

        WikitulateApplication application = (WikitulateApplication) getApplication();

        Log.d("MainActivity",
                application.getArticleCount() + " articles available after onCreate()");

        if(application.getArticleCount() > 0)
        {
            TextView text = (TextView) findViewById(R.id.articleTitleText);
            text.setText(application.getNextArticle());
        }
    }

    @Override
    public boolean onCreateOptionsMenu( Menu menu )
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate( R.menu.menu_main, menu );
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
}
