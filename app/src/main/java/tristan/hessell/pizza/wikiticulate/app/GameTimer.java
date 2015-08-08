package tristan.hessell.pizza.wikiticulate.app;

import android.graphics.Color;
import android.os.CountDownTimer;
import android.util.Log;
import android.widget.TextView;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by manko on 8/08/15.
 */
public class GameTimer {
    private GregorianCalendar mGameTime;
    private GregorianCalendar mRoundTime;
    private WikitulateApplication mApplication;

    GameTimer(WikitulateApplication application) {
        mApplication = application;
        mGameTime  = new GregorianCalendar();
        mRoundTime = new GregorianCalendar();
        mGameTime.setTimeInMillis(0);
        mRoundTime.setTimeInMillis(0);
    }

    private static String format(GregorianCalendar timer) {
        return String.format("%02d:%02d:%03d",
                timer.get(Calendar.MINUTE),
                timer.get(Calendar.SECOND),
                timer.get(Calendar.MILLISECOND));
    }

    public String formatGameTime() {
        return format(mGameTime);
    }

    public long getRoundTime() {
        return mRoundTime.getTimeInMillis();
    }

    public String formatRoundTime() {
        return format(mRoundTime);
    }

    public void startGame() {

    }

    public void startRound() {
        Log.d("GameTimer", "Starting Timer");
        new CountDownTimer(5 * 1000, 30) {

            public void onTick(long ms) {
//                Log.v("ROUNDTIMER", "Tick: " + ms);
                mRoundTime.setTimeInMillis(ms);
            }

            public void onFinish() {
                Log.d("GameTimer", "Stopping Timer");
                mRoundTime.setTimeInMillis(0);
                mApplication.onRoundTimeUp();
            }
        }.start();
    }
}
