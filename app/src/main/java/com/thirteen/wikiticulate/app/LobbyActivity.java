package com.thirteen.wikiticulate.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;


public class LobbyActivity extends AppCompatActivity
{
    //result code used in getting stuff from activity
    private static final int PLAY_ROUND = 1;
    //private final List<Round> scores = new ArrayList<>();

    //private Round currentRound;
    //private String currentPlayer;

    private WikitulateApplication mApplication;

    @Override
    protected void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_lobby );
        mApplication = (WikitulateApplication)getApplication();
    }

    public void btnToGameClick(View v)
    {
        Intent intent = new Intent(this, GameActivity.class);

        startActivityForResult( intent, PLAY_ROUND );
    }



    @Override
    protected void onActivityResult(int reqCode, int resCode, Intent data)
    {
        //Get the round data back from the activity

        if(reqCode == PLAY_ROUND )
        {
            //update this page to the persons current score


            //if everything went well
            if(resCode == RESULT_OK)
            {
                Round rnd = mApplication.getCurrentRound();
                TextView tv = (TextView)findViewById( R.id.tvLob );

                tv.setText( tv.getText().toString() + rnd.getScore() );


            }
        }


    }



    @Override
    public boolean onCreateOptionsMenu( Menu menu )
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate( R.menu.menu_lobby, menu );
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
