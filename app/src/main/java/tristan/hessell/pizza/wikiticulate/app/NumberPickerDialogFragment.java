package tristan.hessell.pizza.wikiticulate.app;

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

import java.lang.reflect.Field;

public class NumberPickerDialogFragment extends DialogFragment
{
    private NumberDialogCallback cb;
    private String title;
    private int minNumber;
    private int maxNumber;
    private int defaultNumber;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        getValuesFromArguments(getArguments());

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate( R.layout.dialog_number_picker, null );

        /*NumberPicker.Formatter timeFormatter = new NumberPicker.Formatter()
        {
            @Override
            public String format( int value )
            {
                return String.format( "%02d", value );
            }
        };*/

        final NumberPicker npNumber = (NumberPicker)v.findViewById( R.id.npNumber );
        npNumber.setMinValue( minNumber );
        npNumber.setMaxValue( maxNumber );
        npNumber.setValue( defaultNumber );
        npNumber.setWrapSelectorWheel( true );
        //npNumber.setFormatter( timeFormatter );

        bugFix( npNumber );

        builder.setTitle( title )
                .setView(v)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton)
                            {
                                cb.onCallback(npNumber.getValue());
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton)
                            {}
                        });
        return builder.create();
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

        title = args.getString( "title" );
        if(title == null)
        {
            throw new IllegalArgumentException( "Cannot create Dialog: No Title passed via arguments" );
        }

        cb = args.getParcelable( "callback" );
        if(cb == null)
        {
            throw new IllegalArgumentException( "Cannot create Dialog: No callback passed via arguments" );
        }

        minNumber = args.getInt( "minValue", 0 );
        defaultNumber = args.getInt( "defaultValue", minNumber );
        maxNumber = args.getInt( "maxValue", defaultNumber );
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
            System.out.println( "bug ddint wfddffgdddf" );
        }
    }
}