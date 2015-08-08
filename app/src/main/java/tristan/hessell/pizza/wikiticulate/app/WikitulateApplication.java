package tristan.hessell.pizza.wikiticulate.app;

import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.os.Handler;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by jsj on 8/08/15.
 */
public class WikitulateApplication extends Application {

    /** The point at which the article downloader will stop. */
    private int           mHighWater = 50;

    /** The point at which the article downloader will start */
    private int           mLowWater  = 10;

    /** A queue of article titles. */
    private Queue<String> mArticleTitles;

    @Override
    public void onCreate()
    {
        super.onCreate();

        mArticleTitles = new LinkedList<String>();
        refillArticles();
    }

    public String getNextArticle() {
        return mArticleTitles.remove();
    }

    private void refillArticles() {
        final Handler threadHandler;
        final Context cbt;

        cbt = this;
        threadHandler = new Handler();

        new Thread(new Runnable() {
            @Override
            public void run() {
                threadHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mArticleTitles.add("foo");
                    }
                });
            }
        }).start();
    }
}
