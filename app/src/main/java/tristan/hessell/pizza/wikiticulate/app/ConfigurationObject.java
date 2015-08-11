package tristan.hessell.pizza.wikiticulate.app;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Tristan on 9/08/2015.
 */
public class ConfigurationObject implements Parcelable
{
    //Required for Parcelable (what allows the object to be passed between activities)
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator()
    {
        @Override
        public ConfigurationObject createFromParcel( Parcel source )
        {
            return new ConfigurationObject( source );
        }

        @Override
        public ConfigurationObject[] newArray( int size )
        {
            return new ConfigurationObject[size];
        }
    };

    private final int numberOfPlayers;
    private final int duration; //in milliseconds
    private final int maxScore;

    public ConfigurationObject(final int numPlayers, final int dur, final int inMaxScore)
    {
        numberOfPlayers = numPlayers;
        duration =        dur;
        maxScore =        inMaxScore;
    }

    //constructor that is used when the object is passed between activities
    public ConfigurationObject(Parcel in)
    {
        numberOfPlayers = in.readInt();
        duration =        in.readInt();
        maxScore =        in.readInt();
    }

    public int getNumberOfPlayers()
    {
        return numberOfPlayers;
    }

    public int getDuration()
    {
        return duration;
    }

    public int getMaxScore()
    {
        return maxScore;
    }

    //not actually sure what this does - its needed for the Parcelable interface
    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel( Parcel dest, int flags )
    {
        dest.writeInt( numberOfPlayers );
        dest.writeInt( duration );
        dest.writeInt( maxScore );
    }
}
