package com.thirteen.wikiticulate.app;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class GameActivity extends AppCompatActivity
{
    private int      mNumPlayers;
    private TextView mArticleText;
    private TextView mTimerText;
    private WikitulateApplication mApplication;
    private PollTimerTask mPollTimer;

    private class PollTimerTask extends AsyncTask<Void, Void, Void> {

        boolean started;

        protected Void doInBackground(Void... v) {
            started = false;
            while(!isCancelled() && (!started || (mApplication.getTimer().getRoundTime() > 30))) {
                if(mApplication.getTimer().getRoundTime() > 0) {
                    started = true;
                }
//                Log.v("GameActivity", "PollTimer Tick");
                publishProgress();
                SystemClock.sleep(30);
            }
            return null;
        }

        protected void onProgressUpdate(Void... v) {
            mTimerText.setText(mApplication.getTimer().formatRoundTime());
        }

        protected void onPostExecute(Void v) {
            // TODO: move this to GameTimer to ensure always the same formatting
            String time = String.format("%02d:%02d:%03d", 0, 0, 0);
            mTimerText.setText(time);

            Log.d("GameActivity", "Polltimer finished");
            endRound();
//            mTimerText.setTextColor(Color.parseColor("#FF0000"));
        }
    }

    @Override
    protected void onDestroy() {
        Log.e("GameActivity", "OnDestroy");
        super.onDestroy();
        if(mPollTimer != null) {
            Log.e("GameActivity", "Cancelling polltimer");
            mPollTimer.cancel(true);
        }
    }

    @Override
    protected void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );

        mApplication = (WikitulateApplication) getApplication();

        mTimerText   = (TextView) findViewById(R.id.timerText);
        mArticleText = (TextView) findViewById(R.id.articleTitleText);

        Log.d( "GameActivity",
                mApplication.getArticleCount() + " articles available after onCreate()" );

        if(mApplication.isRoundInProgress()) {
            Log.d("GameActivity", "Creating GameActivity in Round Mode.");
            setInRoundMode();
        } else {
            Log.d( "GameActivity", "Creating GameActivity in Out of Round Mode." );
            setOutOfRoundMode();
        }
    }

    private void setInRoundMode() {
        Log.d("GameActivity", "Creating PollTimer");
        mPollTimer = new PollTimerTask();
        mPollTimer.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        mArticleText.setText(mApplication.getCurrentArticle());
        mArticleText.setVisibility(View.VISIBLE);
        Button passButton = (Button) findViewById(R.id.passButton);
        Button nextButton = (Button) findViewById(R.id.nextButton);
        Button startButton = (Button) findViewById(R.id.startButton);
        Button endButton = (Button) findViewById( R.id.endButton );

        /* TODO: only if the game rules are "most you can get in a time limit" rather than "Fastest you can get one"*/
        TextView scoreText = (TextView) findViewById(R.id.scoreText);
        scoreText.setVisibility( View.VISIBLE );
        scoreText.setText( "" + mApplication.getRoundScore() );

        passButton.setVisibility( View.VISIBLE );
        nextButton.setVisibility(View.VISIBLE);
        endButton.setVisibility( View.VISIBLE );
        startButton.setVisibility(View.GONE);
        mTimerText.setTextColor( Color.parseColor( "#000000" ) );
    }

    private void setOutOfRoundMode() {
        if(mPollTimer != null) {
            Log.e("GameActivity", "Cancelling polltimer");
            mPollTimer.cancel(true);
        }
        mArticleText.setVisibility(View.GONE);
        Button passButton = (Button) findViewById(R.id.passButton);
        Button nextButton = (Button) findViewById(R.id.nextButton);
        Button startButton = (Button) findViewById(R.id.startButton);
        Button endButton = (Button) findViewById( R.id.endButton );

        passButton.setVisibility( View.GONE );
        nextButton.setVisibility( View.GONE );
        endButton.setVisibility( View.GONE );
        startButton.setVisibility(View.VISIBLE);

        TextView scoreText = (TextView) findViewById(R.id.scoreText);
        scoreText.setVisibility(View.GONE);
        scoreText.setText( "" + mApplication.getRoundScore() );
    }

    private void endRound() {
        if(mPollTimer != null) {
            Log.e("GameActivity", "Cancelling polltimer");
            mPollTimer.cancel(true);
        }
        mArticleText.setVisibility(View.VISIBLE);
        Button passButton = (Button) findViewById(R.id.passButton);
        Button nextButton = (Button) findViewById(R.id.nextButton);
        Button startButton = (Button) findViewById(R.id.startButton);

        TextView scoreText = (TextView) findViewById(R.id.scoreText);
        scoreText.setVisibility(View.VISIBLE);

        passButton.setVisibility(View.GONE);
        nextButton.setVisibility(View.GONE);
        startButton.setVisibility( View.GONE );


    }

    private void startRound() {

        if(mApplication.getArticleCount() > 0)
        {
            mApplication.getNextArticle();
        }

        mApplication.startNewRound();
        setInRoundMode();
    }

    private void pass() {
        if(mApplication.getArticleCount() > 0)
        {
            //add the word to the passed words
            mApplication.passWord();
            mArticleText.setText( mApplication.getNextArticle() );
        }
    }

    private void next() {
        //why is only this bit in the conditional
        if(mApplication.getArticleCount() > 0)
        {
            //add the word to the scored words
            mApplication.scoreWord();
            mArticleText.setText(mApplication.getNextArticle());
        }

        TextView scoreText = (TextView) findViewById(R.id.scoreText);
        scoreText.setText("" + mApplication.getRoundScore());
    }

    public void onNextBtnClicked(View v){
        if(v.getId() == R.id.nextButton){
            next();
        }
    }

    public void onPassBtnClicked(View v){
        if(v.getId() == R.id.passButton){
            pass();
        }
    }

    public void onStartBtnClicked(View v){
        if(v.getId() == R.id.startButton){
            startRound();
        }
    }

    public void onEndBtnClicked(View v){
        if(v.getId() == R.id.endButton){
            if( mApplication.isRoundInProgress() )
            {
                new AlertDialog.Builder( this )
                        .setTitle( "End the round?" )
                        .setMessage( "Are you sure you want to end the round?" )
                        .setPositiveButton( "Yes", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick( DialogInterface dialog, int which )
                            {
                                mApplication.stopRound();
                                setResult( RESULT_OK );
                                finish();
                            }
                        } )
                        .setNegativeButton( "No", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick( DialogInterface dialog, int which )
                            {
                                //
                            }
                        } )
                        .show();
            }
            else
            {
                //just go back to lobby
                setResult( RESULT_OK );
                finish();

            }
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
