package com.thirteen.words;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * Created by manko on 19/08/15.
 */
public class WordSource {

    protected static final Logger log = Logger.getLogger(WordSource.class.getName());

    protected List<Word> mWords = new ArrayList<Word>();

    public WordSource() {
        mWords = new ArrayList<Word>();
    }

    public int getCount() {
        return mWords.size();
    }

    public void shuffle() {
        long seed = System.nanoTime();
        Collections.shuffle(mWords, new Random(seed));
    }

    public Word getNext() {
        Word next = mWords.remove(0);
        log.log(Level.CONFIG, "Next Word: " + next.toString());
        return next;
    }
}
