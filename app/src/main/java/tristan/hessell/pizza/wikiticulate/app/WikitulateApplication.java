package tristan.hessell.pizza.wikiticulate.app;

import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;

import org.apache.http.client.methods.HttpGet;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

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

    /** The background task responsible for getting new article titles. */
    private RefillArticlesTask mRefiller;

    @Override
    public void onCreate()
    {
        super.onCreate();

        mArticleTitles = new LinkedList<String>();

        mRefiller = new RefillArticlesTask();

        /* Get the smallest number of articles to begin with as we don't want setup to take long. */
        mRefiller.execute(LOWWATER);
    }

    private class RefillArticlesTask extends AsyncTask<Integer, Void, Void> {
        protected Void doInBackground(Integer... counts) {
            List<String> articles = ArticleSource.getArticles(counts[0]);
            mArticleTitles.addAll(articles);
            return null;
        }
    }

    public int getArticleCount() {
        return mArticleTitles.size();
    }

    public String getNextArticle() {
        String result = mArticleTitles.remove();

        /* Try and refill the articles if we have reached the low water mark, but check that we
        * are not already doing it first. */
        if(mArticleTitles.size() <= LOWWATER) {
            if(mRefiller.getStatus() != AsyncTask.Status.RUNNING)
            {
                mRefiller = new RefillArticlesTask();
                mRefiller.execute(HIGHWATER - LOWWATER);
            }
        }
        return result;
    }

}
