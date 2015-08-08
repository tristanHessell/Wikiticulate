package tristan.hessell.pizza.wikiticulate.app;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Formatter;
import java.util.Locale;

public class MainActivity extends AppCompatActivity
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
//                Log.v("MainActivity", "PollTimer Tick");
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

            Log.d("MainActivity", "Polltimer finished");
            endRound();
//            mTimerText.setTextColor(Color.parseColor("#FF0000"));
        }
    }

    @Override
    protected void onDestroy() {
        Log.e("MainActivity", "OnDestroy");
        super.onDestroy();
        if(mPollTimer != null) {
            Log.e("MainActivity", "Cancelling polltimer");
            mPollTimer.cancel(true);
        }
    }

    @Override
    protected void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );


        mNumPlayers = getIntent().getIntExtra( "numPlayers", -1 );
        //if(mNumPlayers == -1)
        //{
        //TODO cry as the number of players wasnt set.
        //}

        mApplication = (WikitulateApplication) getApplication();

        mTimerText   = (TextView) findViewById(R.id.timerText);
        mArticleText = (TextView) findViewById(R.id.articleTitleText);

        Log.d("MainActivity",
                mApplication.getArticleCount() + " articles available after onCreate()");

        if(mApplication.isRoundInProgress()) {
            Log.d("MainActivity", "Creating MainActivity in Round Mode.");
            setInRoundMode();
        } else {
            Log.d("MainActivity", "Creating MainActivity in Out of Round Mode.");
            setOutOfRoundMode();
        }
    }

    private void setInRoundMode() {
        Log.d("MainActivity", "Creating PollTimer");
        mPollTimer = new PollTimerTask();
        mPollTimer.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        mArticleText.setText(mApplication.getCurrentArticle());
        mArticleText.setVisibility(View.VISIBLE);
        Button passButton = (Button) findViewById(R.id.passButton);
        Button nextButton = (Button) findViewById(R.id.nextButton);
        Button startButton = (Button) findViewById(R.id.startButton);

        /* TODO: only if the game rules are "most you can get in a time limit" rather than "Fastest you can get one"*/
        TextView scoreText = (TextView) findViewById(R.id.scoreText);
        scoreText.setVisibility(View.VISIBLE);
        scoreText.setText("" + mApplication.getRoundScore());

        passButton.setVisibility(View.VISIBLE);
        nextButton.setVisibility(View.VISIBLE);
        startButton.setVisibility(View.GONE);
        mTimerText.setTextColor(Color.parseColor("#000000"));
    }

    private void setOutOfRoundMode() {
        if(mPollTimer != null) {
            Log.e("MainActivity", "Cancelling polltimer");
            mPollTimer.cancel(true);
        }
        mArticleText.setVisibility(View.GONE);
        Button passButton = (Button) findViewById(R.id.passButton);
        Button nextButton = (Button) findViewById(R.id.nextButton);
        Button startButton = (Button) findViewById(R.id.startButton);

        passButton.setVisibility(View.GONE);
        nextButton.setVisibility(View.GONE);
        startButton.setVisibility(View.VISIBLE);

        TextView scoreText = (TextView) findViewById(R.id.scoreText);
        scoreText.setVisibility(View.GONE);
        scoreText.setText("" + mApplication.getRoundScore());
    }

    private void endRound() {
        if(mPollTimer != null) {
            Log.e("MainActivity", "Cancelling polltimer");
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
        startButton.setVisibility(View.GONE);
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
            mArticleText.setText(mApplication.getNextArticle());
        }
    }

    private void next() {
        if(mApplication.getArticleCount() > 0)
        {
            mArticleText.setText(mApplication.getNextArticle());
        }

        TextView scoreText = (TextView) findViewById(R.id.scoreText);
        mApplication.scoreOne();
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
