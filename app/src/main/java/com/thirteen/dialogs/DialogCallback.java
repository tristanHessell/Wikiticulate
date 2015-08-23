package com.thirteen.dialogs;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 *
 * Won't need a CREATOR for this class as there is no state to capture in the objects.
 * Class should only be instantiated anonymously.
 * Class is not abstract as only one onCallback method would be overridden at a time.
 * Created by Tristan on 11/08/2015.
 */
public class DialogCallback implements Parcelable
{
    public void onCallback( ArrayList<CheckItem> exclusions )
    {
    }

    public void onCallback( int minutes, int seconds )
    {
    }

    public void onCallback( int number )
    {
    }

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
