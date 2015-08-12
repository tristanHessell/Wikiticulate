package tristan.hessell.pizza.wikiticulate.app;

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

import java.util.ArrayList;

public class StringListDialogFragment extends DialogFragment implements ListView.OnItemClickListener
{
    private StringListDialogCallback cb;
    private String title;
    private ArrayList<CheckItem> exclusions;


    public static StringListDialogFragment newInstance(String inTitle, StringListDialogCallback inCb)
    {
        StringListDialogFragment frag = new StringListDialogFragment();
        Bundle args = new Bundle();
        args.putString( "title", inTitle );
        args.putParcelable( "callback", inCb );
        frag.setArguments( args );

        return frag;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        getValuesFromArguments( getArguments() );
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate( R.layout.dialog_string_list, null );

        //get the list view, load it with the exclusions.
        ArrayAdapter<CheckItem> adapter =
                new ArrayAdapter<>(getActivity(),android.R.layout.simple_list_item_multiple_choice, exclusions);

        ListView listView = (ListView)v.findViewById( R.id.listview );
        listView.setAdapter( adapter );
        listView.setChoiceMode( ListView.CHOICE_MODE_MULTIPLE );
        listView.setItemsCanFocus( false );
        listView.setOnItemClickListener( this );

        for (int i = 0; i < exclusions.size(); i++) {
            listView.setItemChecked(i, exclusions.get( i ).isChecked() );
        }

        builder.setTitle( title )
                .setView(v)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                cb.onCallback(exclusions);
                            }
                        })
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
    //Title, callback and the list of strings are required - exception will be thrown if they are not present
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

        exclusions = args.getParcelableArrayList( "exclusions" );
        if(exclusions == null)
        {
            throw new IllegalArgumentException( "" );
        }
    }

    public StringListDialogFragment setListItems(ArrayList<CheckItem> list)
    {
        getArguments().putParcelableArrayList( "exclusions", list );
        return this;
    }

    @Override
    public void onItemClick( AdapterView<?> parent, View view, int position, long id )
    {
        AppCompatCheckedTextView check = (AppCompatCheckedTextView)view;
        exclusions.get( position ).setChecked( check.isChecked() );
    }
}
