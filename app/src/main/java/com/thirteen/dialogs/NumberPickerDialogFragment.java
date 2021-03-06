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

public class NumberPickerDialogFragment extends DialogFragment
{
    private static final String TITLE_IDENTIFIER     = "title";
    private static final String CALLBACK_IDENTIFIER  = "callback";
    private static final String MIN_VALUE_IDENTIFIER = "minVal";
    private static final String DEF_VALUE_IDENTIFIER = "defVal";
    private static final String MAX_VALUE_IDENTIFIER = "maxVal";

    private DialogCallback cb;
    private String title;
    private int minNumber;
    private int maxNumber;
    private int defaultNumber;

    public static NumberPickerDialogFragment newInstance(String inTitle, DialogCallback inCb)
    {
        NumberPickerDialogFragment frag = new NumberPickerDialogFragment();
        Bundle args = new Bundle();
        args.putString( TITLE_IDENTIFIER, inTitle );
        args.putParcelable( CALLBACK_IDENTIFIER, inCb );
        frag.setArguments( args );

        return frag;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        getValuesFromArguments(getArguments());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate( R.layout.dialog_number_picker, null );

        final NumberPicker npNumber = (NumberPicker)v.findViewById( R.id.npNumber );
        npNumber.setMinValue( minNumber );
        npNumber.setMaxValue( maxNumber );
        npNumber.setValue( defaultNumber );
        npNumber.setWrapSelectorWheel( true );

        bugFix( npNumber );

        return new AlertDialog.Builder(getActivity()).setTitle( title )
            .setView(v)
            .setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton)
                    {
                        cb.onCallback(npNumber.getValue());
                    }
                })
            .setNegativeButton("Cancel",
                new DialogInterface.OnClickListener()
                {
                    public void onClick( DialogInterface dialog, int whichButton )
                    {}
                })
            .create();
    }

    /**
     *
     * @param args The bundle that should be supplied to the Dialog at creation.
     */
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

        minNumber = args.getInt( MIN_VALUE_IDENTIFIER, 0 );
        defaultNumber = args.getInt( DEF_VALUE_IDENTIFIER, minNumber );
        maxNumber = args.getInt( MAX_VALUE_IDENTIFIER, defaultNumber );
    }

    //Ill explain this later - cbf atm
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
            System.out.println( "Exception in bug fix for numberPicker" );
        }
    }

    public NumberPickerDialogFragment setDefaultValue(int inDefault)
    {
        getArguments().putInt(DEF_VALUE_IDENTIFIER, inDefault);
        return this;
    }

    public NumberPickerDialogFragment setMinimumValue(int inMin)
    {
        getArguments().putInt( MIN_VALUE_IDENTIFIER, inMin );
        return this;
    }

    public NumberPickerDialogFragment setMaximumValue(int inMax)
    {
        getArguments().putInt( MAX_VALUE_IDENTIFIER, inMax );
        return this;
    }
}
