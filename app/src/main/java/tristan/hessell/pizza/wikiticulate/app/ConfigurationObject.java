package tristan.hessell.pizza.wikiticulate.app;

import java.util.regex.Pattern;

/**
 * Container class for the configuration settings for the game
 * Created by Tristan on 9/08/2015.
 */
public class ConfigurationObject
{
    //the number of teams in the game
    private final int mNumberOfPlayers;
    //the round duration in milliseconds
    private final int mDuration;
    //the score the teams are aiming for
    private final int mMaxScore;
    //the regex that will be used to filter out certain articles
    private final Pattern mExclusionRegex;

    public ConfigurationObject(final int numPlayers, final int dur, final int inMaxScore, final Pattern inRegex)
    {
        mNumberOfPlayers = numPlayers;
        mDuration =        dur;
        mMaxScore =        inMaxScore;
        mExclusionRegex = inRegex;
    }

    public int getNumberOfPlayers()
    {
        return mNumberOfPlayers;
    }

    public int getDuration()
    {
        return mDuration;
    }

    public int getMaxScore()
    {
        return mMaxScore;
    }

    public Pattern getExclusionRegex()
    {
        return mExclusionRegex;
    }
}
