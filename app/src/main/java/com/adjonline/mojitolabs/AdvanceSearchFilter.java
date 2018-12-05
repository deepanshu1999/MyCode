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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
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

public class AdvanceSearchFilter extends AppCompatActivity {
    Spinner typespinner,yearspinner,monthspinner;
    Button button;
    Toolbar toolbar;
    String st="0";
    public static int TypeDef=0;
    public static int YearDef=0;
    public static int MonthDef=0;

    public static List<Post> posts = new ArrayList<Post>();

    public static String Result=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advance_search_filter);
        st="0";
        toolbar = (Toolbar)findViewById(R.id.filtertoolbar);
        toolbar.setTitle("Document");
        toolbar.setTitleTextAppearance(this,R.style.amaticboldColor);
        setSupportActionBar(toolbar);
        final TextView mTitle =toolbar.findViewById(R.id.toolbar_title);
        mTitle.setText("Apply Filter");
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

        final String INPUTTEXT=getIntent().getStringExtra("INPUTTEXT");

        ArrayList<String> types=new ArrayList<>();
        ArrayList<String> years=new ArrayList<>();
        ArrayList<String> months=new ArrayList<>();
        types.add("All");
        types.add("SB");types.add("DB");types.add("FB");types.add("SC");
        String str[]={"All","January","February","March","April","May","June","July","August","September","October","November","December"};
        months.addAll(Arrays.asList(str));
        final String[] values3 =
                {"All","2005", "2006", "2007","2008","2009","2010","2011","2012","2013","2014","2015","2016","2017","2018"};
        years.addAll(Arrays.asList(values3));

        typespinner=findViewById(R.id.typespinner);
        yearspinner=findViewById(R.id.yearspinner);
        monthspinner=findViewById(R.id.monthspinner);

        ArrayAdapter typeadapter=new ArrayAdapter(this,android.R.layout.simple_spinner_item,types);
        typeadapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        ArrayAdapter monthadapter=new ArrayAdapter(this,android.R.layout.simple_spinner_item,months);
        monthadapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        ArrayAdapter yearadapter=new ArrayAdapter(this,android.R.layout.simple_spinner_item,years);
        yearadapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        typespinner.setAdapter(typeadapter);
        yearspinner.setAdapter(yearadapter);
        monthspinner.setAdapter(monthadapter);
        if(Viewer.isfilter){
        typespinner.setSelection(TypeDef);
        monthspinner.setSelection(MonthDef);
        yearspinner.setSelection(YearDef);}

        button=findViewById(R.id.applyfilterbutton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String TYPE=typespinner.getSelectedItem().toString();
                String MONTH=monthspinner.getSelectedItem().toString();
                String YEAR=yearspinner.getSelectedItem().toString();
                if(TYPE.equals("All")){
                    TYPE="not";
                }
                if(MONTH.equals("All")){
                    MONTH="not";
                }
                if(YEAR.equals("All")){
                    YEAR="not";
                }

                TypeDef=typespinner.getSelectedItemPosition();
                MonthDef=monthspinner.getSelectedItemPosition();
                YearDef=yearspinner.getSelectedItemPosition();
                String URL="http://adjonline.com/mojito/advfilter.php?inputtext="+INPUTTEXT
                        +"&year="+YEAR
                        +"&month="+MONTH
                        +"&type="+TYPE;
                Log.d("FILTERCALL",URL);
                SendAdvanceFilterRequest sendAdvanceFilterRequest=new SendAdvanceFilterRequest();
                sendAdvanceFilterRequest.execute(URL);

            }
        });




    }

    class SendAdvanceFilterRequest extends AsyncTask<String,Void,String>{
        final ProgressDialog progressDialog = new ProgressDialog(AdvanceSearchFilter.this,
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
            //AdvanceSearch.clearpost();
            //AdvanceSearch.addall(posts);
            Intent i=new Intent();
            st="1";

            i.putExtra("TAG",st);
            i.putExtra("INPUTTEXT",AdvanceSearchFilter.this.getIntent().getStringExtra("INPUTTEXT"));
            i.putExtra("CALLER",AdvanceSearch.ADVANCESEARCH);

            progressDialog.dismiss();
            if(s==null){
                Toast.makeText(AdvanceSearchFilter.this,"No Result!",Toast.LENGTH_SHORT).show();
                st="0";
            }
            setResult(99,i);
            finish();

        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                //Create an HTTP client
                URL url = new URL(strings[0]);
                Log.e("IIP","Ok till url");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(50000 /* milliseconds */);
                conn.setConnectTimeout(50000 /* milliseconds */);
                conn.setRequestMethod("GET");
                InputStream content = conn.getInputStream();

                //Read the server response and attempt to parse it as JSON
                Reader reader = new InputStreamReader(content);
                Log.e("IIP","Ok till InputStream");

                GsonBuilder gsonBuilder = new GsonBuilder();
                Log.e("IIP","OK1");
                gsonBuilder.setDateFormat("M/d/yy hh:mm a");
                Log.e("IIP","OK2");
                Gson gson = gsonBuilder.create();
                Log.e("IIP","OK3");
                Log.d("IIP",Integer.toString(posts.size()));
                posts = Arrays.asList(gson.fromJson(reader, Post[].class));
                content.close();
                return "Wow";

            } catch (Exception ex) {
                Log.e("TAG", "Failed to parse JSON due to: " + ex);
                return null;
            }


        }
    }
}
