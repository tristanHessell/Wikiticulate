package com.thirteen.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatCheckedTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.thirteen.wikiticulate.app.R;

import java.util.ArrayList;

public class CheckListDialogFragment extends DialogFragment implements ListView.OnItemClickListener
{
    private static final String TITLE_IDENTIFIER    = "title";
    private static final String CALLBACK_IDENTIFIER = "callback";
    private static final String ITEMS_IDENTIFIER    = "listItems";

    private DialogCallback cb;
    private String title;
    private ArrayList<CheckItem> checkListItems;

    public static CheckListDialogFragment newInstance(String inTitle, DialogCallback inCb)
    {
        CheckListDialogFragment frag = new CheckListDialogFragment();
        Bundle args = new Bundle();
        args.putString( TITLE_IDENTIFIER, inTitle );
        args.putParcelable( CALLBACK_IDENTIFIER, inCb );
        frag.setArguments( args );

        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        getValuesFromArguments( getArguments() );

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate( R.layout.dialog_string_list, null );

        //get the list view, load it with the items.
        ArrayAdapter<CheckItem> adapter =
                new ArrayAdapter<>(getActivity(),android.R.layout.simple_list_item_multiple_choice, checkListItems);

        ListView listView = (ListView)v.findViewById( R.id.listview );
        listView.setAdapter( adapter );
        listView.setChoiceMode( ListView.CHOICE_MODE_MULTIPLE );
        listView.setItemsCanFocus( false );
        listView.setOnItemClickListener( this );

        //check the items that have already been checked previously
        for (int i = 0; i < checkListItems.size(); i++)
        {
            listView.setItemChecked(i, checkListItems.get( i ).isChecked() );
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle( title )
            .setView( v )
            .setPositiveButton( "OK",
                new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick( DialogInterface dialog, int whichButton )
                    {
                        cb.onCallback( checkListItems );
                    }
                } )
            .setNegativeButton( "Cancel",
                new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick( DialogInterface dialog, int whichButton ) {}
                } );
        return builder.create();
    }

    //if a value isnt present, a default is used
    //Title, callback and the list of strings are required - exception will be thrown if they are not present
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

        checkListItems = args.getParcelableArrayList( ITEMS_IDENTIFIER );
        if(checkListItems == null)
        {
            throw new IllegalArgumentException( "Cannot create Dialog: No list entries passed via arguments" );
        }
    }

    public CheckListDialogFragment setListItems(ArrayList<CheckItem> list)
    {
        getArguments().putParcelableArrayList( ITEMS_IDENTIFIER, list );
        return this;
    }

    @Override
    public void onItemClick( AdapterView<?> parent, View view, int position, long id )
    {
        AppCompatCheckedTextView checkbox = (AppCompatCheckedTextView)view;
        checkListItems.get( position ).setChecked( checkbox.isChecked() );
    }
}
