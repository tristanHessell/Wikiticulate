package com.thirteen.wikiticulate.app;

import java.util.List;
import java.util.regex.Pattern;

/**
 * Container class for the configuration settings for the game
 *
 * There is probably a better way to do this, but it works for now :)
 *
 * Created by Tristan on 9/08/2015.
 */
public class ConfigurationObject
{
    //only the required fields here are final - the others may not be needed in a given game

    //the number of teams in the game
    private final int mNumberOfPlayers;
    //the round duration in milliseconds
    private final int mDuration;
    //the score the teams are aiming for
    private final int mMaxScore;
    //the regex that will be used to filter out certain articles
    private Pattern mExclusionRegex;
    //the list of topics that the words come from
    private List<String> mTopics;

    public ConfigurationObject(final int numPlayers, final int dur, final int inMaxScore)
    {
        mNumberOfPlayers = numPlayers;
        mDuration =        dur;
        mMaxScore =        inMaxScore;
    }

    /**
     * Constructor with the regex
     * @param numPlayers
     * @param dur
     * @param inMaxScore
     * @param inRegex
     */
    public ConfigurationObject(final int numPlayers, final int dur, final int inMaxScore, final Pattern inRegex)
    {
        this(numPlayers,dur,inMaxScore);
        mExclusionRegex = inRegex;
    }

    public ConfigurationObject(final int numPlayers, final int dur, final int inMaxScore, final List<String> inTopics)
    {
        this(numPlayers,dur,inMaxScore);
        mTopics = inTopics;
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

    public List<String> getTopics()
    {
        return mTopics;
    }
}
