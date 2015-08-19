package com.thirteen.words;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;

/**
 * Created by manko on 19/08/15.
 */
public class InputStreamSource extends WordSource {

    public InputStreamSource(InputStream strm) {
        try {
            BufferedReader br = new BufferedReader (
                    new InputStreamReader(strm));
            for(String line; (line = br.readLine()) != null; ) {
                mWords.add(new Word(line));
            }
        } catch (java.io.IOException ex) {
            log.log(Level.SEVERE, ex.getMessage());
        }
    }

}
