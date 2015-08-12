package tristan.hessell.pizza.wikiticulate.app;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by Tristan on 11/08/2015.
 */
public abstract class StringListDialogCallback implements Parcelable
{
    public abstract void onCallback( ArrayList<CheckItem> exclusions );

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
