package tristan.hessell.pizza.wikiticulate.app;

import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;

import org.apache.http.client.methods.HttpGet;

import java.sql.Time;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Created by jsj on 8/08/15.
 */
public class WikitulateApplication extends Application {

    /** The point at which the article downloader will stop. */
    final static private int           HIGHWATER = 30;

    /** The point at which the article downloader will start */
    final static private int           LOWWATER  = 10;

    /** A queue of article titles. */
    private Queue<String>      mArticleTitles;

    private GameTimer          mGameTimer;

    private int mRound;
    private int mRoundScore;
    private boolean mRoundInProgress;

    //The length of the round in milliseconds. Default of 5 seconds
    private int mRoundDuration = 5 * 1000;

    String mCurrentArticle;

    /** The background task responsible for getting new article titles. */
    private RefillArticlesTask mRefiller;

    @Override
    public void onCreate()
    {
        super.onCreate();

        mRound = 0;
        mRoundScore = 0;
        mRoundInProgress = false;

        mArticleTitles = new LinkedList<String>();
        mCurrentArticle = "";

        mRefiller = new RefillArticlesTask();

        /* Get the smallest number of articles to begin with as we don't want setup to take long. */
        mRefiller.execute(LOWWATER);

        mGameTimer = new GameTimer(this);
    }

    private class RefillArticlesTask extends AsyncTask<Integer, Void, Void> {
        protected Void doInBackground(Integer... counts) {
            List<String> articles = ArticleSource.getArticles(counts[0]);
            mArticleTitles.addAll(articles);
            return null;
        }
    }

    public void onRoundTimeUp() {
        mRoundInProgress = false;
    }

    public boolean isRoundInProgress() {
        return mRoundInProgress;
    }

    public void startNewRound(){
        if(mRoundInProgress) {
            // bad
        }
        mGameTimer.startRound();
        mRound++;
        mRoundScore = 0;
        mRoundInProgress = true;
    }

    public int getRoundScore() {
        return mRoundScore;
    }

    public int getRoundDuration() {return mRoundDuration;}

    public void setRoundDuration(int inDuration)
    {
        mRoundDuration = inDuration;
    }

    public void scoreOne() {
        mRoundScore++;
    }

    public void stopRound(){
        if(!mRoundInProgress) {
            // bad
        }
        mRoundInProgress = false;
    }

    public GameTimer getTimer() {
        return mGameTimer;
    }

    public int getArticleCount() {
        return mArticleTitles.size();
    }

    public String getCurrentArticle() {
        return mCurrentArticle;
    }

    public String getNextArticle() {
        mCurrentArticle = mArticleTitles.remove();

        /* Try and refill the articles if we have reached the low water mark, but check that we
        * are not already doing it first. */
        if(mArticleTitles.size() <= LOWWATER) {
            if(mRefiller.getStatus() != AsyncTask.Status.RUNNING)
            {
                mRefiller = new RefillArticlesTask();
                mRefiller.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, HIGHWATER - LOWWATER);
            }
        }
        return mCurrentArticle;
    }

}
