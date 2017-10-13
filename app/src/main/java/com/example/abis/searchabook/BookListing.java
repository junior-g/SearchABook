package com.example.abis.searchabook;

/**
 * Created by abis on 27/8/17.
 */
import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.app.LoaderManager.LoaderCallbacks;

import java.util.ArrayList;
import java.util.List;

public class BookListing extends AppCompatActivity implements LoaderCallbacks<List<Books>>{
    private  static  final  int LOADER_ID=1;
    private AdapterList mAdapter;
    private TextView mEmptyStateTextView;
    private ProgressBar progressBar;
    ConnectivityManager cm ;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_list);
        //netwok connected?
        cm =
                (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);
        progressBar=(ProgressBar)findViewById(R.id.loading_spinner);
        if(isConnected==false)
        {
            mEmptyStateTextView.setText("NO INTERNET CONNECTION!!!");
            progressBar.setVisibility(View.GONE);
        }
        ListView booksListView=(ListView)findViewById(R.id.list_item);


        mAdapter=new AdapterList(this,new  ArrayList<Books>());
        booksListView.setAdapter(mAdapter);

        booksListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Books currentBook=mAdapter.getItem(position);
                Uri bookUri=Uri.parse(currentBook.getUrl());
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, bookUri); // Create a new intent to view the earthquake URI
                try {
                    startActivity(websiteIntent);// Send the intent to launch a new activity
                }catch (Exception e)
                {
                    Log.e("EarthAcitvity", "  activity not started", e);
                }
            }
        });

        LoaderManager loaderManager = getLoaderManager();
        // Initialize the loader. Pass in the int ID constant defined above and pass in null for
        try {
            loaderManager.initLoader(LOADER_ID, null, this);
        }catch (Exception e)
        {
            Log.e("Earthqueake", "  Error caused due to:-", e);
        }
    }
    @Override
    public Loader<List<Books>> onCreateLoader(int i, Bundle bundle) {

        return new BookLoader(this, MainActivity.url);
    }

    @Override
    public void onLoadFinished(Loader<List<Books>> loader, List<Books> bookses) {
        if(mAdapter!=null)
            mAdapter.clear();
        mEmptyStateTextView.setText("NO DATA FOUND");
        if(bookses!=null&&!bookses.isEmpty()  )
        {
            try
            {
                progressBar.setVisibility(View.GONE);
                mEmptyStateTextView.setVisibility(View.GONE);
                mAdapter.addAll(bookses);
            }
            catch (Exception e)
            {
                Log.e("Earthactivity", "  addAll()", e);
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Books>> loader) {
        mAdapter.clear();
    }
}
