package com.thirteen.words;

/**
 * Created by manko on 19/08/15.
 */
public class Word {

    private String mWord;

    public Word() {
        mWord = "";
    }

    public Word(String word) {
        mWord = word;
    }

    @Override
    public String toString() {
        return mWord.toString();
    }
}
