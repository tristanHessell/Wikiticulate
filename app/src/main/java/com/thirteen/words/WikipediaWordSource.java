package com.thirteen.words;

import com.thirteen.wikipedia.Api;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executors;

/**
 * Created by manko on 19/08/15.
 */
public class WikipediaWordSource extends WordSource {

    protected static final int LOWWATER = 10;

    protected boolean mSynchronous;

    public WikipediaWordSource(boolean synchronous) {
        mSynchronous = synchronous;
        if(!mSynchronous) {
            mWords = new CopyOnWriteArrayList<Word>();
        }
        refill();
    }

    public void refill() {
        if(mSynchronous) {
            refillSync();
        } else {
            refillAsync();
        }
    }

    public void refillSync() {
        List<String> randomArticles = Api.getRandomTitles(LOWWATER);
        for(Iterator<String> i = randomArticles.iterator(); i.hasNext();) {
            mWords.add(new Word(i.next()));
        }
    }

    public void refillAsync() {
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                refillSync();
            }
        });
    }

    @Override
    public Word getNext() {
        if(getCount() <= LOWWATER) {
            refill();
        }
        return super.getNext();
    }
}
