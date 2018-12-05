package com.adjonline.mojitolabs;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static com.android.volley.VolleyLog.TAG;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link fragment1.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link fragment1#newInstance} factory method to
 * create an instance of this fragment.
 */


public class fragment1 extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static int firstVisibleInListview;

    // TODO: Rename and change types of parameters
    public TextView partnumber;
    String res;
    public TextView date;
    private String mParam1;
    private String mParam2;
    public RecyclerView recyclerView;
    List<ListData> data;
    JSONArray myjsonarray;
    JSONObject myjsonObject;
    public int A = 0;
    JSONObject mjsonobject;
    JSONArray mjsonarr;
    LinearLayoutManager linearLayoutManager;
    Adapter  mAdapter;
    SwipeRefreshLayout mSwipeRefresh;
    //private OnFragmentInteractionListener mListener;

    public fragment1() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment fragment1.
     */
    // TODO: Rename and change types and number of parameters
    public static fragment1 newInstance(String param1, String param2) {
        fragment1 fragment = new fragment1();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_fragment1, container, false);
        data=new ArrayList<>();
        mSwipeRefresh=rootView.findViewById(R.id.swipeRefresh);
        recyclerView=rootView.findViewById(R.id.recycler);
        linearLayoutManager=new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        //  recyclerView.setHasFixedSize(true);
        new Setuprecyclerview().execute();
//        mAdapter= new Adapter(getContext(), data);
//        recyclerView.setAdapter(mAdapter);
        mSwipeRefresh.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        //Log.i(LOG_TAG, "onRefresh called from SwipeRefreshLayout");

                        // This method performs the actual data-refresh operation.
                        // The method calls setRefreshing(false) when it's finished.
                        new Setuprecyclerview().execute();
                    }
                }
        );
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getContext(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Intent intentBundle = new Intent(getContext(), judgement_activity_1.class);
                        try {
                            mjsonobject= mjsonarr.getJSONObject(position);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        intentBundle.putExtra("jsonobj",mjsonobject.toString());
                        try {
                            intentBundle.putExtra("partyname",mjsonobject.getString("18"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        //intentBundle.putStringArrayListExtra("dimension2", ((ArrayList) dimension2.get(position))); // Very Very Important To Understand //
                        startActivity(intentBundle);
                        //Toast.makeText(getContext(),"hi",Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                        // do whatever
                        //  Toast.makeText(getContext(), dimension2.get(position).toString(), Toast.LENGTH_SHORT).show();
                    }
                })
        );

        partnumber=rootView.findViewById(R.id.partnumber);
        date=rootView.findViewById(R.id.date);

        //linearLayoutManager=recyclerView.getLayoutManager();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @RequiresApi(api = Build.VERSION_CODES.M)
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                //linearLayoutManager=recyclerView.getLayoutManager();
                int currentFirstVisible = linearLayoutManager.findFirstVisibleItemPosition();
                Log.d(TAG, "onScrolled: currentfistvisibleelemenr"+currentFirstVisible);
                Log.d(TAG, "onScrolled: lastknownuwbjbwibwibwbjwbi"+linearLayoutManager.findLastCompletelyVisibleItemPosition());
                try {
                    myjsonObject = myjsonarray.getJSONObject(linearLayoutManager.findLastCompletelyVisibleItemPosition());

                    String partnO = new String();
                    String partdate = new String();

                    if (myjsonObject.has("partNo")) {
                        partnO = myjsonObject.getString("partNo");

                    }

                    if (myjsonObject.has("partdate")) {
                        partdate = myjsonObject.getString("partdate");
                    }

                    partnumber.setText("Part Number: " + partnO + ",");
                    date.setText(" Date: " + partdate);

//                    partnumber.setTextAppearance(R.style.nimbusromno9lreg);
//                    date.setTextAppearance(R.style.nimbusromno9lreg);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (currentFirstVisible > firstVisibleInListview)
                    Log.i("RecyclerView scrolled: ", "scroll up!");
                else
                    Log.i("RecyclerView scrolled: ", "scroll down!");

                firstVisibleInListview = currentFirstVisible;

            }

        });
        return rootView;
        // return inflater.inflate(R.layout.fragment_fragment1, container, false);
    }

    public class Setuprecyclerview extends AsyncTask<String,String,String>
    {
        URL url;
        HttpURLConnection conn;
//        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            progressDialog.setIndeterminate(true);
//            progressDialog.setMessage("Loading data...");
//            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {

            //check for correct url
            try {
                // Enter URL address where your json file resides
                // Even you can make call to php file which returns json data
                url = new URL("http://adjonline.com/mojito/newdetails.php");

            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                mSwipeRefresh.setRefreshing(false);
                e.printStackTrace();
                return e.toString();
            }

            //set up connection
            try {
                // Setup HttpURLConnection class to send and receive data from php and mysql
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("GET");
                conn.setDoOutput(true);

            } catch (IOException e1) {
                mSwipeRefresh.setRefreshing(false);
                // TODO Auto-generated catch block
                e1.printStackTrace();
                return e1.toString();
            }

            try {

                int response_code = conn.getResponseCode();

                // Check if successful connection made
                if (response_code == HttpURLConnection.HTTP_OK) {

                    // Read data sent from server
                    InputStream input = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                    StringBuilder result = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }

                    // Pass data to onPostExecute method
                    return (result.toString());

                } else {
                    mSwipeRefresh.setRefreshing(false);
                    return ("unsuccessful");
                }

            } catch (IOException e) {
                mSwipeRefresh.setRefreshing(false);
                e.printStackTrace();
                return e.toString();
            } finally {
                conn.disconnect();
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try {
                res=result;
                JSONArray jsonArray= new JSONArray(result);
                mjsonarr=new JSONArray(result);
                myjsonarray=jsonArray;
                //result.isEmpty();

            } catch (JSONException e) {
                mSwipeRefresh.setRefreshing(false);
                Toast.makeText( getContext(),"Please Check your Internet Connection and try again", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }

//            data=new ArrayList<>();
//            progressDialog.dismiss();

            try {
                JSONArray jArray = new JSONArray(result);
                String t1,t2,t3,t4;
                // Extract data from json and store into ArrayList as class objects
                for(int i=0;i<jArray.length();i++){
                    JSONObject json_data = jArray.getJSONObject(i);
                    t4=json_data.getString("print");
                    t1=json_data.getString("18");
                    t2=json_data.getString("jud");
                    t3=json_data.getString("dt")+"/"+json_data.getString("mn")+"/"+json_data.getString("yer");
                    ListData listdata=new ListData(t1,t2,t3,t4);
                    Log.d(TAG, "onPostExecute: pata ni qa problem hai");
                    data.add(listdata);
                }
                // Setup and Handover data to recyclerview
                mAdapter= new Adapter(getContext(), data);
                recyclerView.setAdapter(mAdapter);
                mSwipeRefresh.setRefreshing(false);
//                RecyclerViewPositionHelper recyclerViewPositionHelper=new RecyclerViewPositionHelper(recyclerView);
//                recyclerViewPositionHelper.

            } catch (JSONException e) {
                mSwipeRefresh.setRefreshing(false);
                Toast.makeText( getContext(),"Please Check your Internet Connection and try again", Toast.LENGTH_LONG).show();
            }

        }
    }

    // TODO: Rename method, update argument and hook method into UI event
//    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
//    }

//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }
//
//    @Override
//    public void onDetach() {
//        super.onDetach();
//        mListener = null;
//    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
