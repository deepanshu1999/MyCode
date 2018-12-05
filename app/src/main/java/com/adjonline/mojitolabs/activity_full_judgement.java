package com.adjonline.mojitolabs;
import android.Manifest;
import android.annotation.TargetApi;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.audiofx.EnvironmentalReverb;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.CancellationSignal;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.print.PageRange;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintJob;
import android.print.PrintManager;
//import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import okhttp3.internal.http2.Settings;

public class activity_full_judgement extends AppCompatActivity {
    private Toolbar toolbar;
    private String link;
    private WebView webView;
    ProgressDialog progDailog=null;
    String mera_link=null;
    ImageButton printButton;
    String printlink=null;
    String FILEPATH;
    String Username="NO NAME";
    private int mCurrentSearchIndex = -1;

    private static final String TAG = "activity_full_judgement";
    private static final int PERMISSION_REQUEST_CODE = 1;
    public ImageButton imageButton;
    String searchtext;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public  void createWebPagePrint(WebView webView) {
        PrintManager printManager = (PrintManager) getSystemService(Context.PRINT_SERVICE);
        PrintDocumentAdapter printAdapter = webView.createPrintDocumentAdapter();
        String jobName = "ADJ" + " Document";
        PrintAttributes.Builder builder = new PrintAttributes.Builder();
        builder.setMediaSize(PrintAttributes.MediaSize.ISO_A4);
        progDailog.dismiss();
        PrintJob printJob = printManager.print(jobName, printAdapter, builder.build());

        if(printJob.isCompleted()){
            Toast.makeText(getApplicationContext(),"Printed", Toast.LENGTH_LONG).show();

        }
        else if(printJob.isFailed()) {
            Toast.makeText(getApplicationContext(), "Printing Cancelled", Toast.LENGTH_LONG).show();
            }
        // Save the job object for later status checking
    }



    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        progDailog= new ProgressDialog(activity_full_judgement.this,
                R.style.AppTheme_Dark_Dialog);
        progDailog.setMessage("Loading...");
        progDailog.setIndeterminate(true);
        if(getIntent().hasExtra("CALLER") && getIntent().getStringExtra("CALLER").equals(AdvanceSearch.ADVANCESEARCH)){
        searchtext=getIntent().getStringExtra("INPUTTEXT");
        }
        SharedPreferences sharedPreferences=getSharedPreferences("USER",MODE_PRIVATE);
        Username=sharedPreferences.getString("NAME","Deepanshu");
        setContentView(R.layout.activity_full_judgement);
        imageButton=findViewById(R.id.shareButton);
        printButton=findViewById(R.id.printButton);
        Intent intentExtra=getIntent();
        link=intentExtra.getStringExtra("link");

        printlink=intentExtra.getStringExtra("print")+Username.replaceAll("\\s+","%20");
        final String subject=intentExtra.getStringExtra("subject");
        toolbar =findViewById(R.id.Resulttoolbar);
        toolbar.setTitle("Document");
        toolbar.setTitleTextAppearance(this,R.style.amaticboldColor);
        final TextView mTitle = toolbar.findViewById(R.id.toolbar_title);
        mTitle.setText("Judgment");
        mTitle.setTextColor(Color.BLACK);
        mTitle.setGravity(Gravity.NO_GRAVITY);
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
        /*printButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                WebView web2=new WebView(activity_full_judgement.this);
                web2.loadUrl(printlink);

                SharedPreferences sharedPreferences=getSharedPreferences("USER",MODE_PRIVATE);



                //web2.getSettings().setTextSize(WebSettings.TextSize.NORMAL);
                web2.setWebViewClient(new WebViewClient(){

                    @Override
                    public void onPageStarted(WebView view, String url, Bitmap favicon) {
                        super.onPageStarted(view, url, favicon);
                        progDailog.setMessage("Loading...");
                        progDailog.setIndeterminate(true);
                        progDailog.show();
                    }

                    @Override
                    public void onPageFinished(WebView view, String url) {
                        super.onPageFinished(view, url);
                        progDailog.dismiss();
                    }
                });
                createWebPagePrint(web2);

            }
        });*/

        printButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DownloadFile().execute(printlink);
            }
        });
        webView =findViewById(R.id.fullJudgment);
        //webView.setInitialScale(200);
        WebSettings webSettings = webView.getSettings();
        webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setTextSize(WebSettings.TextSize.LARGEST );
        webView.getSettings().setBuiltInZoomControls(true);
        webView.setWebChromeClient(new WebChromeClient());
        webView.setWebViewClient(new WebViewClient(){
            final ProgressDialog progDailog = new ProgressDialog(activity_full_judgement.this,
                    R.style.AppTheme_Dark_Dialog);

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                progDailog.setMessage("Loading...");
                progDailog.setIndeterminate(true);
                progDailog.show();
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                progDailog.show();
                return super.shouldOverrideUrlLoading(view, url);
            }
            @Override
            public void onPageFinished(WebView view, final String url) {
                progDailog.dismiss();


            }
        });

        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setJavaScriptEnabled(true);
        //webView.getSettings().setTextSize(WebSettings.TextSize.NORMAL);

        if (Build.VERSION.SDK_INT >= 23)
        {
            if (checkPermission())
            {
                // Code for above or equal 23 API Oriented Device
                // Your Permission granted already .Do next code
                webView.setDownloadListener(new DownloadListener() {
                    public void onDownloadStart(String url, String userAgent,
                                                String contentDisposition, String mimetype,
                                                long contentLength) {
                        DownloadManager.Request request = new DownloadManager.Request(
                                Uri.parse(url));
                        request.allowScanningByMediaScanner();
                        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "Scilab");
                        DownloadManager dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                        dm.enqueue(request);
                    }
                });
            }
            else {
                // Code for permission

                webView.setDownloadListener(new DownloadListener() {

                    public void onDownloadStart(String url, String userAgent,
                                                String contentDisposition, String mimetype,
                                                long contentLength) {
                        requestPermission();
                        if (checkPermission())
                        {
                            // Code for above or equal 23 API Oriented Device
                            // Your Permission granted already .Do next code

                            DownloadManager.Request request = new DownloadManager.Request(
                                    Uri.parse(url));
                            request.allowScanningByMediaScanner();
                            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "ADJ");
                            DownloadManager dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                            dm.enqueue(request);

                        }
                    }
                });
            }
        }
        else
        {

            webView.setDownloadListener(new DownloadListener() {

                public void onDownloadStart(String url, String userAgent,
                                            String contentDisposition, String mimetype,
                                            long contentLength) {
                    DownloadManager.Request request = new DownloadManager.Request(
                            Uri.parse(url));
                    request.allowScanningByMediaScanner();
                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                    request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "ADJ");
                    DownloadManager dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                    dm.enqueue(request);
                }

            });
        }

        String [] arrOfStr = link.split("com", 2);

        mera_link = arrOfStr[0]+"com/user/hviewer";
        String[] fir_mera_link = arrOfStr[1].split("/myadj");
        mera_link = mera_link+fir_mera_link[1];
        webSettings.setJavaScriptEnabled(true);
        Log.e("MYJSON1",mera_link);
        webView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        webView.getSettings().setDomStorageEnabled(true);
        webView.loadUrl(mera_link);

        if(getIntent().hasExtra("CALLER")) {
            if(searchtext.charAt(0)=='\"'){
                searchtext=searchtext.substring(1,searchtext.length()-1);
            }
            webView.findAllAsync(searchtext);
        }


        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody = link;
                String partyName=" ";
                String shareSubject ="Looking for Allahabad High Court Judgements. Subscribe ADJ Online-Easily Accessible Legal Database and Download ADJ Online App.";
               if(getIntent().hasExtra("partyname"))
               {
                   partyName=getIntent().getStringExtra("partyname");
               }
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, partyName);
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT,shareSubject+"\n\n\n"+ shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Share via"));

            }
        });




    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(activity_full_judgement.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    private void requestPermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(activity_full_judgement.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

            ActivityCompat.requestPermissions(activity_full_judgement.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        } else {
            ActivityCompat.requestPermissions(activity_full_judgement.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        }
    }

    private class DownloadFile extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            progDailog.show();
        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected void onPostExecute(String aVoid) {
            super.onPostExecute(aVoid);
            if(aVoid.equals("DONE")){
               // PrintManager printManager = (PrintManager) getSystemService(Context.PRINT_SERVICE);
                //PdfDocumentAdapter printAdapter = webView.createPrintDocumentAdapter();
                PrintManager printManager=(PrintManager) getSystemService(Context.PRINT_SERVICE);
                try
                { String fileName = "ADJTEMPPDF.pdf";
                    String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
                    File folder = new File(extStorageDirectory);
                    folder.mkdir();


                    PrintDocumentAdapter printAdapter = new PdfDocumentAdapter(getApplicationContext(),FILEPATH);
                    PrintAttributes.Builder newbuilder=new PrintAttributes.Builder();
                    newbuilder.setMediaSize(PrintAttributes.MediaSize.ISO_A4);
                    printManager.print("Document"+Username, printAdapter,newbuilder.build());

                }
                catch (Exception e)
            {
                e.printStackTrace();
            }

            }
            progDailog.dismiss();
        }

        @Override
        protected String doInBackground(String... strings) {
            String fileUrl = strings[0];
            if (ContextCompat.checkSelfPermission(activity_full_judgement.this, Manifest.permission.WRITE_CALENDAR)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity_full_judgement.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
                }
                else{
                return null;
            }
            String fileName = "ADJTEMPPDF.pdf";
            String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
            File folder = new File(extStorageDirectory);
            folder.mkdir();

            File pdfFile = new File(folder, fileName);
            FILEPATH=pdfFile.getAbsolutePath();

            try{
                pdfFile.createNewFile();
            }catch (IOException e){
                e.printStackTrace();
            }
            downloadFile(fileUrl, pdfFile);
            return "DONE";
        }
        public void downloadFile(String fileUrl, File directory){
            try {

                URL url = new URL(fileUrl);
                HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                FileOutputStream fileOutputStream = new FileOutputStream(directory);
                int totalSize = urlConnection.getContentLength();

                byte[] buffer = new byte[1024*1024];
                int bufferLength = 0;
                while((bufferLength = inputStream.read(buffer))>0 ){
                    fileOutputStream.write(buffer, 0, bufferLength);
                }
                fileOutputStream.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}