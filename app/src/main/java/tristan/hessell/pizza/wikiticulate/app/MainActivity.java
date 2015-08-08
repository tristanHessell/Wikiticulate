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
    private WikitulateApplication mApplication;

    private class PollTimerTask extends AsyncTask<Void, Void, Void> {
        protected Void doInBackground(Void... v) {

            while(mApplication.getTimer().getRoundTime() > 30) {
                publishProgress();
                SystemClock.sleep(30);
            }
            return null;
        }

        protected void onProgressUpdate(Void... v) {
            TextView timerText = (TextView) findViewById(R.id.timerText);
            timerText.setText(mApplication.getTimer().formatRoundTime());
        }

        protected void onPostExecute(Void v) {
            TextView timerText = (TextView) findViewById(R.id.timerText);
            String time = String.format("%02d:%02d:%03d", 0, 0, 0);
            timerText.setTextColor(Color.parseColor("#FF0000"));
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

        mArticleText = (TextView) findViewById(R.id.articleTitleText);

        Log.d("MainActivity",
                mApplication.getArticleCount() + " articles available after onCreate()");

        if(mApplication.isRoundInProgress()) {
            setInRoundMode();
            new PollTimerTask().execute();
        }
        else {
            setOutOfRoundMode();
        }
    }

    private void setInRoundMode() {
        mArticleText.setText(mApplication.getCurrentArticle());
        mArticleText.setVisibility(View.VISIBLE);
        Button passButton = (Button) findViewById(R.id.passButton);
        Button nextButton = (Button) findViewById(R.id.nextButton);
        Button startButton = (Button) findViewById(R.id.startButton);

        passButton.setVisibility(View.VISIBLE);
        nextButton.setVisibility(View.VISIBLE);
        startButton.setVisibility(View.GONE);
    }

    private void setOutOfRoundMode() {
        mArticleText.setVisibility(View.GONE);
        Button passButton = (Button) findViewById(R.id.passButton);
        Button nextButton = (Button) findViewById(R.id.nextButton);
        Button startButton = (Button) findViewById(R.id.startButton);

        passButton.setVisibility(View.GONE);
        nextButton.setVisibility(View.GONE);
        startButton.setVisibility(View.VISIBLE);
    }

    private void startRound() {

        if(mApplication.getArticleCount() > 0)
        {
            mApplication.getNextArticle();
        }

        mApplication.startNewRound();
        setInRoundMode();
        new PollTimerTask().execute();
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
