package com.thirteen.wikiticulate.app;

import java.util.ArrayList;
import java.util.List;

/**
 * Simple container for round information
 * Created by Tristan on 25/08/2015.
 */
public class Round
{
    private final String playerName;
    private final List<String> passed;
    private final List<String> scored;

    public Round(String inName)
    {
        playerName = inName;
        passed = new ArrayList<>();
        scored = new ArrayList<>();
    }

    public Round(String inName, List<String> inPassed, List<String> inScored )
    {
        playerName = inName;
        passed = inPassed;
        scored = inScored;
    }

    public void addPassedWords(List<String> passedWords)
    {
        passed.addAll( passedWords );
    }

    public void addPassedWord(String word)
    {
        passed.add( word );
    }

    public void addScoredWords(List<String> scoredWords)
    {
        scored.addAll( scoredWords );
    }

    public void addScoredWord(String word)
    {
        scored.add( word );
    }

    public List<String> getScoredWords()
    {
        return scored;
    }

    public List<String> getPassedWords()
    {
        return passed;
    }

    public int getScore()
    {
        return scored.size();
    }
}
