package com.thirteen.wikiticulate.app;

import android.app.Application;
import android.util.Log;
import com.thirteen.words.InputStreamSource;
import com.thirteen.words.WordSource;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by jsj on 8/08/15.
 */
public class WikitulateApplication extends Application {

    private GameTimer          mGameTimer;
    private int mRound;
    private int mRoundScore;
    private boolean mRoundInProgress;
    private WordSource mWordSource;

    //The length of the round in milliseconds. Default of 5 seconds
    private int mRoundDuration = 5 * 1000;

    String mCurrentArticle;

    /** The background task responsible for getting new article titles. */
    private ConfigurationObject configuration;

    @Override
    public void onCreate()
    {
        super.onCreate();

        mRound = 0;
        mRoundScore = 0;
        mRoundInProgress = false;
        mCurrentArticle = "";

        /*
        This uses a local file as the word source.
        */
        try {
            mWordSource = new InputStreamSource(getAssets().open("words.txt"));
        } catch (java.io.IOException ex) {
            Log.e("WORDS", ex.getMessage());
        }

        /*
        This uses Wikipedia as the word source.
         */
        // mWordSource = new WikipediaWordSource(false);

        mWordSource.shuffle();


        mGameTimer = new GameTimer(this);
    }

    public void setConfiguration( ConfigurationObject configuration )
    {
        this.configuration = configuration;
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
        return mWordSource.getCount();
    }

    public String getCurrentArticle() {
        return mCurrentArticle;
    }

    public String getNextArticle() {
        mCurrentArticle = mWordSource.getNext().toString();
        Log.d("WORDS:", mCurrentArticle);
        return mCurrentArticle;
    }

}
