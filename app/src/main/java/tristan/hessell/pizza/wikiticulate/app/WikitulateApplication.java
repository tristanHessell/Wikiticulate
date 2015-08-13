package tristan.hessell.pizza.wikiticulate.app;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import java.util.*;
import java.util.regex.Pattern;

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
    private ConfigurationObject configuration;

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

    public void setConfiguration( ConfigurationObject configuration )
    {
        this.configuration = configuration;
    }

    private class RefillArticlesTask extends AsyncTask<Integer, Void, Void> {
        protected Void doInBackground(Integer... counts) {
            List<String> articles = ArticleSource.getArticles(counts[0]);
            mArticleTitles.addAll(articles);
            return null;
        }
    }

    /**
     * Not the most efficient way.
     *
     * @param articles
     * @return
     */
    private List<String> filterArticles( List<String> articles )
    {
        Pattern exclusionRegex = configuration.getExclusionRegex();
        List<String> tempList = new ArrayList<>( articles );

        for(String article : articles)
        {
            if( exclusionRegex.matcher( article ).matches() )
            {
                Log.d("WikiApp", "Article " + article + "filtered out");
                tempList.remove( article );
            }
        }

        return tempList;
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

    public int getRoundDuration() {return configuration.getDuration();}

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
