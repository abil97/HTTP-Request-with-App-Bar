package com.example.app_bar;

import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

public class MainActivity extends AppCompatActivity {

    // Define all necessary variables

    private String url = "https://api.github.com/users/abil97"; // Url fo get data from
    private TextView myText;                                    // Text view that appears in the middle of the screen
    private RestTemplate restTemplate;
    private GetAWebResourceTask dataFetcher;
    private Button btnReset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Set up the toolbar
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        // Set up the textView
        myText = findViewById(R.id.textView);

        // Set up RestTemplate() for making Web request
        restTemplate = new RestTemplate();

        // Set up a new object for asynchronous task
        dataFetcher = new GetAWebResourceTask(restTemplate, myText);


        btnReset = findViewById(R.id.button);
        btnReset.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                myText.setText(getResources().getString(R.string.about));
                myText.setTypeface(null, Typeface.BOLD);
            }
        });

    }

    // Just start an asynchronous task of fetching data from the web
    public void getData(GetAWebResourceTask dataFetcher, String url){
        dataFetcher.execute(url);
    }


    // This will create an overflow menu
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.example_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            // In case of selecting a first option from overflow menu,
            // just execute the function that fetches data
            case R.id.make_request:
                getData(dataFetcher, url);
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }


    private class GetAWebResourceTask extends AsyncTask<String, Void, String> {

        final public static String TAG = "GetAWebResourceTask";
        private RestTemplate restTemplate ;
        private TextView textView ;


        // Constructor
        public GetAWebResourceTask(RestTemplate restTemplate, TextView textView) {
            this.restTemplate = restTemplate ;
            this.textView = textView ;
        }


        // This is run in a background thread
        @Override
        protected String doInBackground(String... params) {


            // get the string from params, which is an array
            restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
            String result = restTemplate.getForObject(params[0], String.class , "Android");
            Log.d(TAG, result);
            System.out.println("Result is: " + result);

            // Result is passed into onPostExecute
            return result;
        }

        // This runs in UI when background thread finishes
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            // Changing TextView to the fetched result
            textView.setText(result);
            textView.setTypeface(null, Typeface.NORMAL);
        }
    }
}
