package com.thirteen.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.NumberPicker;
import com.thirteen.wikiticulate.app.R;

import java.lang.reflect.Field;

public class DurationPickerDialogFragment extends DialogFragment
{
    private static final String TITLE_IDENTIFIER       = "title";
    private static final String CALLBACK_IDENTIFIER    = "callback";
    private static final String MIN_MINUTES_IDENTIFIER = "minMin";
    private static final String DEF_MINUTES_IDENTIFIER = "defMin";
    private static final String MAX_MINUTES_IDENTIFIER = "maxMin";
    private static final String MIN_SECONDS_IDENTIFIER = "minSec";
    private static final String DEF_SECONDS_IDENTIFIER = "defSec";
    private static final String MAX_SECONDS_IDENTIFIER = "maxSec";

    private DialogCallback cb;
    private String title;
    private int minMinutes;
    private int defaultMinutes;
    private int maxMinutes;
    private int minSeconds;
    private int defaultSeconds;
    private int maxSeconds;

    public static DurationPickerDialogFragment newInstance(String inTitle, DialogCallback inCb)
    {
        DurationPickerDialogFragment frag = new DurationPickerDialogFragment();
        Bundle args = new Bundle();
        args.putString( TITLE_IDENTIFIER, inTitle );
        args.putParcelable( CALLBACK_IDENTIFIER, inCb );
        frag.setArguments( args );

        return frag;
    }

    private static NumberPicker.Formatter timeFormatter = new NumberPicker.Formatter()
    {
        @Override
        public String format( int value )
        {
            return String.format( "%02d", value );
        }
    };

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        getValuesFromArguments( getArguments() );
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate( R.layout.dialog_duration_picker, null );

        final NumberPicker npDurationMinutes = (NumberPicker)v.findViewById( R.id.npRoundMinutes );
        npDurationMinutes.setMinValue( minMinutes );
        npDurationMinutes.setMaxValue( maxMinutes );
        npDurationMinutes.setValue( defaultMinutes );
        npDurationMinutes.setWrapSelectorWheel( true );
        npDurationMinutes.setFormatter( timeFormatter );

        final NumberPicker npDurationSeconds = (NumberPicker)v.findViewById( R.id.npRoundSeconds );
        npDurationSeconds.setMinValue( minSeconds );
        npDurationSeconds.setMaxValue( maxSeconds );
        npDurationSeconds.setValue( defaultSeconds);
        npDurationSeconds.setWrapSelectorWheel( true );
        npDurationSeconds.setFormatter( timeFormatter );

        bugFix( npDurationMinutes );
        bugFix( npDurationSeconds );

        builder.setTitle( title )
            .setView( v )
            .setPositiveButton( "OK",
                new DialogInterface.OnClickListener()
                {
                    public void onClick( DialogInterface dialog, int whichButton )
                    {
                        cb.onCallback( npDurationMinutes.getValue(), npDurationSeconds.getValue() );
                    }
                } )
            .setNegativeButton( "Cancel",
                new DialogInterface.OnClickListener()
                {
                    public void onClick( DialogInterface dialog, int whichButton )
                    {
                    }
                } );
        return builder.create();
    }

    //if a value isnt present, a default is used
    //Title and callback are required - exception will be thrown if they are not present
    private void getValuesFromArguments(Bundle args)
    {
        if(args == null)
        {
            throw new IllegalArgumentException( "Cannot create Dialog: No arguments passed to dialog" );
        }

        title = args.getString( TITLE_IDENTIFIER );
        if(title == null)
        {
            throw new IllegalArgumentException( "Cannot create Dialog: No Title passed via arguments" );
        }

        cb = args.getParcelable( CALLBACK_IDENTIFIER );
        if(cb == null)
        {
            throw new IllegalArgumentException( "Cannot create Dialog: No callback passed via arguments" );
        }

        minMinutes = args.getInt( MIN_MINUTES_IDENTIFIER, 0 );
        defaultMinutes = args.getInt( DEF_MINUTES_IDENTIFIER, minMinutes );
        maxMinutes = args.getInt( MAX_MINUTES_IDENTIFIER, defaultMinutes );

        minSeconds = args.getInt( MIN_SECONDS_IDENTIFIER, 0 );
        defaultSeconds = args.getInt( DEF_SECONDS_IDENTIFIER, minSeconds );
        maxSeconds = args.getInt( MAX_SECONDS_IDENTIFIER, defaultSeconds );
    }

    private void bugFix(NumberPicker np)
    {
        try
        {
            Field f = NumberPicker.class.getDeclaredField( "mInputText" );
            f.setAccessible( true );
            EditText inputText = (EditText)f.get( np );
            inputText.setFilters( new InputFilter[0] );
        }
        catch( Exception ex )
        {
            System.out.println("bug ddint work");
        }
    }


    public DurationPickerDialogFragment setMinimumDuration(int minMinutes, int minSeconds)
    {
        getArguments().putInt( MIN_MINUTES_IDENTIFIER, minMinutes);
        getArguments().putInt( MIN_SECONDS_IDENTIFIER, minSeconds);

        return this;
    }

    public DurationPickerDialogFragment setMaxmimumDuration(int maxMinutes, int maxSeconds)
    {
        getArguments().putInt(MAX_MINUTES_IDENTIFIER, maxMinutes);
        getArguments().putInt(MAX_SECONDS_IDENTIFIER, maxSeconds);

        return this;
    }

    public DurationPickerDialogFragment setDefaultDuration(int defaultMinutes, int defaultSeconds)
    {
        getArguments().putInt( DEF_MINUTES_IDENTIFIER, defaultMinutes);
        getArguments().putInt( DEF_SECONDS_IDENTIFIER, defaultSeconds);

        return this;
    }
}
