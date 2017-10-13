package com.example.abis.searchabook;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {


    private  static  final  int LOADER_ID=1;
    private AdapterList mAdapter;
    //to store search keyword
    //JSON
    public static String  tempURL="https://www.googleapis.com/books/v1/volumes?q=http://books.google.com/books/content?id=e0b5wUwNGt4C&printsec=frontcover&img=1&zoom=5&edge=curl&source=gbs_api";
   public EditText searchView;
  public  static    String searchKeyword;
    private String main_sting;

    private ImageButton btnSpeak;
    private final int REQ_CODE_SPEECH_INPUT = 100;
   //Search URL @TODO:  https://www.googleapis.com/books/v1/volumes?q=
    ///+keyword or key word=key%20word
   private StringBuilder urlResult;
    public static String url="https://www.googleapis.com/books/v1/volumes?q="+"gitanjali";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchView=(EditText)findViewById(R.id.search_keyword);
        ///generating URL for typed keyword
        Button searchButton=(Button)findViewById(R.id.search_button);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                urlResult=new StringBuilder();
                urlResult.append("https://www.googleapis.com/books/v1/volumes?q=");
                searchKeyword=searchView.getText().toString();

                for(int i=0; i<searchKeyword.length(); ++i)
        {
            if(searchKeyword.charAt(i)==' ')
            {
                urlResult.append("%20");
            }
            else
            {
                urlResult.append(searchKeyword.charAt(i));
            }
        }

                searchView.setVisibility(View.GONE);
        TextView textView=(TextView)findViewById(R.id.searchResult);
         url=urlResult.toString();
        textView.setText("search result for "+searchKeyword+"\n   URL="+url);
        Log.e("URL =", url);
        Button searchButton=(Button)findViewById(R.id.search_button);
        searchButton.setVisibility(View.GONE);

                Intent i=new Intent(MainActivity.this, BookListing.class);
                startActivity(i);
            }
        });


        //generate url from speech

        btnSpeak = (ImageButton) findViewById(R.id.btnSpeak);
        btnSpeak.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                promptSpeechInput();
            }
        });


    }

    /**
     * Showing google speech input dialog
     * */
    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                getString(R.string.speech_prompt));
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(),
                    getString(R.string.speech_not_supported),
                    Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Receiving speech input
     * */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

                    urlResult=new StringBuilder();
                    urlResult.append("https://www.googleapis.com/books/v1/volumes?q=");
                    searchKeyword=result.get(0);

                    for(int i=0; i<searchKeyword.length(); ++i)
                    {
                        if(searchKeyword.charAt(i)==' ')
                        {
                            urlResult.append("%20");
                        }
                        else
                        {
                            urlResult.append(searchKeyword.charAt(i));
                        }
                    }

                    searchView.setVisibility(View.GONE);
                    TextView textView=(TextView)findViewById(R.id.searchResult);
                    url=urlResult.toString();
                    textView.setText("search result for "+searchKeyword+"\n   URL="+url);
                    Log.e("URL =", url);
                    Button searchButton=(Button)findViewById(R.id.search_button);
                    searchButton.setVisibility(View.GONE);

                    Intent i=new Intent(MainActivity.this, BookListing.class);
                    startActivity(i);

                }
                break;
            }

        }
    }
}
