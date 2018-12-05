package com.adjonline.mojitolabs;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.android.volley.VolleyLog.TAG;

public class Viewer extends AppCompatActivity {
    RecyclerView recyclerView;
    TextView numView;
    List<ListData> data;
    Adapter  mAdapter;
    String res;
    public static boolean isfilter=false;
    JSONArray jArray;
    String inputtext;
    Toolbar toolbar;
    public static final int RESULT_OK=1;
    ImageButton filterButton;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent datar) {
        super.onActivityResult(requestCode, resultCode, datar);
        if(resultCode==99){
        String t1, t2, t3,t4;
        numView.setVisibility(View.VISIBLE);
        numView.setText(inputtext + "  ("+ AdvanceSearchFilter.posts.size()+")");
        filterButton.setVisibility(View.VISIBLE);
        List<ListData> dataholder=new ArrayList<>();
        data=dataholder;
        Log.e("Entries After",Integer.toString(AdvanceSearchFilter.posts.size()));
        //Log.e("MAGGU", "WHy i am herer");
        if(AdvanceSearchFilter.posts.size()==0){
            Toast.makeText(Viewer.this,"No Result",Toast.LENGTH_SHORT).show();
        }
        for (int i = 0; i < AdvanceSearchFilter.posts.size(); i++) {
            t1 = AdvanceSearchFilter.posts.get(i).middle;
            t2 = AdvanceSearchFilter.posts.get(i).top;
            t3 = AdvanceSearchFilter.posts.get(i).cita1;
            t4=AdvanceSearchFilter.posts.get(i).cita1;
            ListData listdata = new ListData(t1, t2, t3,t4);

            data.add(listdata);
            if (i % 10 == 0) {
                mAdapter = new Adapter(Viewer.this, data);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(Viewer.this);
                recyclerView.setLayoutManager(linearLayoutManager);
                recyclerView.setAdapter(mAdapter);
            }
            //Log.e("TAGGU", listdata.toString());


        }
        mAdapter = new Adapter(Viewer.this,data);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(Viewer.this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(mAdapter);
        isfilter=true;}
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewer);
        numView=findViewById(R.id.textView2);
        recyclerView = findViewById(R.id.viewerid);
        toolbar = findViewById(R.id.result);
        filterButton=findViewById(R.id.filterButton);
        toolbar.setTitle("Document");
        toolbar.setTitleTextAppearance(this, R.style.amaticboldColor);
        setSupportActionBar(toolbar);
        final TextView mTitle = toolbar.findViewById(R.id.toolbar_title);
        mTitle.setText("Search Results");
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
        data = new ArrayList<>();

        final String Tag=getIntent().getStringExtra("CALLER");
        if (Tag.equals(CitationSearch.CITATION)) {
            res = CitationSearch.Res;
            numView.setVisibility(View.GONE);
            filterButton.setVisibility(View.GONE);
            try {

                jArray = new JSONArray(res);
                Log.d(TAG, "onCreate: ddsdsdsdsdsdsds " + res);
                String t1, t2, t3,t4;
                Log.e("MYJSON",jArray.get(0).toString());
                // Extract data from json and store into ArrayList as class objects
                for (int i = 0; i < jArray.length(); i++) {
                    JSONObject json_data = jArray.getJSONObject(i);
                    t1 = json_data.getString("2");
                    t2 = json_data.getString("1");
                    t3 = json_data.getString("Citation No");
                    t4=json_data.getString("print");
                    ListData listdata = new ListData(t1, t2, t3,t4);

                    //Log.d(TAG, "onPostExecute: pata ni qa problem hai");
                    //Log.d(TAG, json_data.toString());
                    data.add(listdata);
                    Log.e("TAGGU", listdata.toString());
                }
                // Setup and Handover data to recyclerview
                mAdapter = new Adapter(Viewer.this, data);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(Viewer.this);
                recyclerView.setLayoutManager(linearLayoutManager);
                recyclerView.setAdapter(mAdapter);
                //   mSwipeRefresh.setRefreshing(false);
//                RecyclerViewPositionHelper recyclerViewPositionHelper=new RecyclerViewPositionHelper(recyclerView);
//                recyclerViewPositionHelper.

            } catch (JSONException e) {
                // mSwipeRefresh.setRefreshing(false);
                Toast.makeText(getApplicationContext(), "No Result", Toast.LENGTH_LONG).show();
            }
        } else if (Tag.equals(DateSearch.DATESEARCH)) {
            res = DateSearch.Res;
            numView.setVisibility(View.GONE);
            filterButton.setVisibility(View.GONE);
            try {

                jArray = new JSONArray(res);
                Log.d(TAG, "onCreate: ddsdsdsdsdsdsds " + res);
                String t1, t2, t3,t4;
                // Extract data from json and store into ArrayList as class objects
                for (int i = 0; i < jArray.length(); i++) {
                    JSONObject json_data = jArray.getJSONObject(i);
                    t1 = json_data.getString("2");
                    t2 = json_data.getString("1");
                    t3 = json_data.getString("Citation No");
                    t4=json_data.getString("print");
                    ListData listdata = new ListData(t1, t2, t3,t4);
                    //Log.d(TAG, "onPostExecute: pata ni qa problem hai");
                    //Log.d(TAG, json_data.toString());
                    data.add(listdata);
                    Log.e("TAGGU", listdata.toString());
                }
                // Setup and Handover data to recyclerview
                mAdapter = new Adapter(Viewer.this, data);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(Viewer.this);
                recyclerView.setLayoutManager(linearLayoutManager);
                recyclerView.setAdapter(mAdapter);
                //   mSwipeRefresh.setRefreshing(false);
//                RecyclerViewPositionHelper recyclerViewPositionHelper=new RecyclerViewPositionHelper(recyclerView);
//                recyclerViewPositionHelper.

            } catch (JSONException e) {
                // mSwipeRefresh.setRefreshing(false);
                Toast.makeText(getApplicationContext(), "No Result", Toast.LENGTH_LONG).show();
            }
        } else if (Tag.equals(PartySearch.PARTYSEARCH)) {
            String t1, t2, t3,t4;
            numView.setVisibility(View.GONE);
            filterButton.setVisibility(View.GONE);
            //Log.d("MAGGU",Integer.toString(PartySearch.posts.size()));
            for (int i = 0; i <PartySearch.posts.size(); i++) {
                t1 = PartySearch.posts.get(i).peti;
                t2 = PartySearch.posts.get(i).jud;
                t3 = PartySearch.posts.get(i).cita1;
                t4 = PartySearch.posts.get(i).print;
                ListData listdata = new ListData(t1,t2,t3,t4);
                //Log.d(TAG, "onPostExecute: pata ni qa problem hai");
                //Log.d(TAG, json_data.toString());
                data.add(listdata);
                if(i%10==0){
                    mAdapter = new Adapter(Viewer.this, data);
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(Viewer.this);
                    recyclerView.setLayoutManager(linearLayoutManager);
                    recyclerView.setAdapter(mAdapter);
                }
                //Log.e("TAGGU", listdata.toString());


            }
            mAdapter = new Adapter(Viewer.this, data);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(Viewer.this);
            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.setAdapter(mAdapter);
            // Setup and Handover data to recyclerview

            //   mSwipeRefresh.setRefreshing(false);
//                RecyclerViewPositionHelper recyclerViewPositionHelper=new RecyclerViewPositionHelper(recyclerView);
//                recyclerViewPositionHelper.
        } else if (Tag.equals(AdvanceSearch.ADVANCESEARCH)) {
            String t1, t2, t3,t4;
            //String tag=getIntent().getStringExtra("TAG");
            inputtext=getIntent().getStringExtra("INPUTTEXT");
            Log.e("SEARCHTEXTVIEWER",inputtext);
            numView.setText(inputtext + "  ("+ AdvanceSearch.posts.size()+")");
            filterButton.setVisibility(View.VISIBLE);
            //Log.e("MAGGU", "WHy i am herer");
            Log.e("Entries Before",Integer.toString(AdvanceSearch.posts.size()));
            for (int i = 0; i < AdvanceSearch.posts.size(); i++) {
                t1 = AdvanceSearch.posts.get(i).middle;
                t2 = AdvanceSearch.posts.get(i).top;
                t3 = AdvanceSearch.posts.get(i).cita1;
                t4 = AdvanceSearch.posts.get(i).print;
                ListData listdata = new ListData(t1, t2, t3,t4);

                data.add(listdata);
                if (i % 10 == 0) {
                    mAdapter = new Adapter(Viewer.this, data);
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(Viewer.this);
                    recyclerView.setLayoutManager(linearLayoutManager);
                    recyclerView.setAdapter(mAdapter);
                }
                //Log.e("TAGGU", listdata.toString());


            }

            mAdapter = new Adapter(Viewer.this, data);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(Viewer.this);
            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.setAdapter(mAdapter);

        }
            recyclerView.addOnItemTouchListener(
                    new RecyclerItemClickListener(Viewer.this, recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {
                            Log.e("ABS","Yaha a gya");
                            JSONObject msjsonobject = null;
                            String link = null;
                            String print=null;
                            Intent intentBundle = new Intent(Viewer.this, activity_full_judgement.class);
                            if(isfilter==true){
                                link=AdvanceSearchFilter.posts.get(position).link;
                                print=AdvanceSearchFilter.posts.get(position).print;
                                intentBundle.putExtra("CALLER",AdvanceSearch.ADVANCESEARCH);
                                intentBundle.putExtra("INPUTTEXT",inputtext);

                            }
                            else if (Tag.equals(AdvanceSearch.ADVANCESEARCH)) {
                                Log.d("ABS","BC yaha kaise a gya");
                                link = AdvanceSearch.posts.get(position).link;
                                print=AdvanceSearch.posts.get(position).print;
                                intentBundle.putExtra("CALLER",AdvanceSearch.ADVANCESEARCH);
                                intentBundle.putExtra("INPUTTEXT",inputtext);
                            }
                            else if (Tag.equals(PartySearch.PARTYSEARCH)) {
                                Log.d("POSSTAUS","I am here");
                                link = PartySearch.posts.get(position).link;
                                print=PartySearch.posts.get(position).print;
                            }
                            else {
                                try {
                                    msjsonobject = jArray.getJSONObject(position);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                try {
                                    print=msjsonobject.getString("print");
                                    link = msjsonobject.getString("link");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            if (!Tag.equals(AdvanceSearch.ADVANCESEARCH)&&!Tag.equals(PartySearch.PARTYSEARCH)) {
                                String partyName = " ";
                                try {
                                    msjsonobject = jArray.getJSONObject(position);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                try {
                                    partyName = msjsonobject.getString("2");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                if (partyName != " ")
                                    intentBundle.putExtra("partyname", partyName);
                            }
                            else
                            {
                                //insert the code to get party name in advance search and party search here
                            }
                            intentBundle.putExtra("link", link);
                            intentBundle.putExtra("print",print);

                            //intentBundle.putStringArrayListExtra("dimension2", ((ArrayList) dimension2.get(position))); // Very Very Important To Understand //
                            startActivity(intentBundle);
                            //Toast.makeText(getContext(),"hi",Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onLongItemClick(View view, int position) {
                            // do whatever
                            //  Toast.makeText(getContext(), dimension2.get(position).toString(), Toast.LENGTH_SHORT).show();
                        }
                    }));


        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(Viewer.this,AdvanceSearchFilter.class);
                i.putExtra("INPUTTEXT",getIntent().getStringExtra("INPUTTEXT"));
                Viewer.this.startActivityForResult(i,RESULT_OK);
                //numView.setVisibility(View.GONE);
            }
        });
    }
}
