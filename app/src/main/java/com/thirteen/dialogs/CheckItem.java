package com.thirteen.dialogs;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Cant make this generic unfortunately, due to the parcelable interface.
 *
 * Created by Tristan on 13/08/2015.
 */
public class CheckItem implements Parcelable
{
    private final String mString;
    private boolean mChecked;

    public CheckItem(final String inStr)
    {
        mString = inStr;
        mChecked = false;
    }

    public CheckItem(final String inStr, final boolean checked)
    {
        mString = inStr;
        mChecked = checked;
    }

    public final void setChecked( final boolean checked)
    {
        mChecked = checked;
    }

    public final boolean isChecked( )
    {
        return mChecked;
    }

    public CheckItem(Parcel in)
    {
        mString = in.readString();
        mChecked = in.readByte() == 1;
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel( Parcel dest, int flags )
    {
        dest.writeString( mString );
        dest.writeByte( (byte)(mChecked ? 1 : 0 )); //parcels don't take booleans
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public CheckItem createFromParcel(Parcel in) {
            return new CheckItem(in);
        }
        public CheckItem[] newArray(int size) {
            return new CheckItem[size];
        }
    };

    //hacky but it works for now
    @Override
    public String toString()
    {
        return mString;
    }
}
