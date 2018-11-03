package com.adjonline.mojitolabs;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PartySearch extends AppCompatActivity {
    EditText editText;
    Button button;
    Toolbar toolbar;
    public static List<Post> posts = new ArrayList<Post>();
    public static final String PARTYSEARCH="PARTYSEARCH";
    public static String Res;
    /*private void writeToFile(String data,Context context) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput("config.txt", Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.a
            outputStreamWriter.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_party_search);

        toolbar = (Toolbar)findViewById(R.id.partytoolbar);
        toolbar.setTitle("Document");
        toolbar.setTitleTextAppearance(this,R.style.amaticboldColor);
        setSupportActionBar(toolbar);
        final TextView mTitle =toolbar.findViewById(R.id.toolbar_title);
        mTitle.setText("Party Search");
        mTitle.setGravity(Gravity.NO_GRAVITY);
        mTitle.setTextColor(Color.BLACK);
        toolbar.setBackgroundColor(Color.WHITE);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        editText=findViewById(R.id.partygetter);
        button=findViewById(R.id.searchpartybutton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String partyname=editText.getText().toString();
                String URL="http://adjonline.com/mojito/party.php?inputtext="+partyname;
                SendPartyRequest sendPartyRequest=new SendPartyRequest();
                sendPartyRequest.execute(URL);

            }
        });
    }
    class SendPartyRequest extends AsyncTask<String,Void,String>{
        final ProgressDialog progressDialog = new ProgressDialog(PartySearch.this,
                R.style.AppTheme_Dark_Dialog);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Searching....");
            progressDialog.show();

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            Intent i=new Intent(PartySearch.this,Viewer.class);
            i.putExtra("INPUTTEXT",editText.getText().toString());
            i.putExtra("CALLER",PartySearch.PARTYSEARCH);
            progressDialog.dismiss();
            if(s==null || s.length()==2){
                Toast.makeText(PartySearch.this,"No Result",Toast.LENGTH_SHORT).show();
            }
            else{
                startActivity(i);
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                //Create an HTTP client
                URL url = new URL(strings[0]);

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(50000 /* milliseconds */);
                conn.setConnectTimeout(50000 /* milliseconds */);
                conn.setRequestMethod("GET");
                InputStream content = conn.getInputStream();


                //Read the server response and attempt to parse it as JSON
                Reader reader = new InputStreamReader(content);

                GsonBuilder gsonBuilder = new GsonBuilder();
                gsonBuilder.setDateFormat("M/d/yy hh:mm a");
                Gson gson = gsonBuilder.create();

                posts = Arrays.asList(gson.fromJson(reader, Post[].class));
                content.close();
                return "Wow";
            } catch (Exception ex) {
                return null;

            }

        }
    }
}
