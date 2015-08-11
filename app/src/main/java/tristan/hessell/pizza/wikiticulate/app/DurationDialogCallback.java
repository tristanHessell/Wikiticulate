package tristan.hessell.pizza.wikiticulate.app;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Tristan on 11/08/2015.
 */
public abstract class DurationDialogCallback implements Parcelable
{
    public abstract void onCallback( int minutes, int seconds );

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel( Parcel dest, int flags )
    {

    }
}
