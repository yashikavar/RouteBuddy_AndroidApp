package routebuddy.administrator.example.com.minor_project;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.zip.DataFormatException;

public class mapspage2 extends AppCompatActivity implements View.OnClickListener{
    TextView ei1;
    TextView ei2;
    EditText ei3;
    EditText ei4;
    EditText ei5;
    static String currentdate;
    static String tgi1;
    static String tgi2;
    static String orgi;
    static String desti;
    static String unmei;
    static int pcodei;
    static int li1, li2;
    static  int hi1, mi1;
    static String def="n/a";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapspage2);
        Button b2= (Button)findViewById(R.id.btnadd2);
        ei1= (TextView)findViewById(R.id.source_add1);
        ei2= (TextView)findViewById(R.id.dest_add1);
        ei3= (EditText)findViewById(R.id.pin_code1);
        ei4= (EditText)findViewById(R.id.time_g11);
        ei5= (EditText)findViewById(R.id.time_g21);
        loginpage lm= new loginpage();
        MapsActivity2 mm=new MapsActivity2();
        orgi=mm.get_source();
        desti=mm.get_dest();
        //Log.d(org,dest);
        //Log.d("days",days);
        Calendar c=Calendar.getInstance();
        Date c1=c.getTime();
        SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd");
        currentdate=df.format(c1.getTime());
        //String currentDate= Date.get
        SharedPreferences pre=getSharedPreferences("bool", Context.MODE_PRIVATE);
        unmei=pre.getString("id", def);
        Log.d(currentdate,unmei);
        //unmei= lm.getid();
        ei1.setText(orgi);
        ei2.setText(desti);
        b2.setOnClickListener(this);

        ConnectivityManager connectivityManager=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo=connectivityManager.getActiveNetworkInfo();
        if(networkInfo==null && !networkInfo.isConnected())
        {
            b2.setEnabled(false);

        }

    }

    @Override
    public void onClick(View v) {
        tgi1=ei4.getText().toString();
        tgi2=ei5.getText().toString();
        //Log.d(tg1,tg2);
        li1= tgi1.length();
        li2=tgi2.length();
        hi1=Integer.parseInt(tgi1);
        mi1=Integer.parseInt(tgi2);


        pcodei= Integer.parseInt(ei3.getText().toString());

        if(v.getId()==R.id.btnadd2)
        {
            if(li1==2 && li2==2 && hi1>=00 && hi1<=24 && mi1>=00 && mi1<=60 ) {
                if(!ei3.getText().toString().isEmpty() && ei3.getText().toString().length()==6) {
                    tgi1 = tgi1 + ":" + tgi2 + ":" + "00";
                    BackgroundTask backgroundTask = new BackgroundTask();
                    backgroundTask.execute(unmei, orgi, desti, tgi1, Integer.toString(pcodei), currentdate);
                }
                else
                {
                    Toast.makeText(mapspage2.this, "Incorrect Pincode !!", Toast.LENGTH_LONG).show();
                }
            }
            else
            {
                Toast.makeText(mapspage2.this, "Incorrect format of time!!", Toast.LENGTH_LONG).show();
                ei4.setText(" ");
                ei5.setText(" ");
            }

            //BackgroundTask backgroundTask = new BackgroundTask();
            //backgroundTask.execute(unmei,orgi, desti,  tgi1 , Integer.toString(pcodei),currentdate);
            //days="";
        }

    }

    class BackgroundTask extends AsyncTask<String,Void,Void> {
        String add_info_url;
        @Override
        protected  void onPreExecute()
        {
            add_info_url="http://routebuddy.comli.com/add_info_instant.php";
        }

        @Override
        protected Void doInBackground(String... args) {
            try {
                URL url = new URL(add_info_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                String username,source,destination,go_time,date;
                int pincode;
                username=args[0];
                source=args[1];
                destination=args[2];
                go_time=args[3];
                //come_time=args[4];
                pincode=Integer.parseInt(args[4]);
                currentdate=args[5];
                //days=args[5];
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter=new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                String data_string= URLEncoder.encode("username","UTF-8")+"="+URLEncoder.encode(username,"UTF-8")+"&"+
                        URLEncoder.encode("source","UTF-8")+"="+URLEncoder.encode(source,"UTF-8")+"&"+
                        URLEncoder.encode("destination","UTF-8")+"="+URLEncoder.encode(destination,"UTF-8")+"&"+
                        URLEncoder.encode("time","UTF-8")+"="+URLEncoder.encode(go_time,"UTF-8")+"&"+
                        URLEncoder.encode("pincode","UTF-8")+"="+ URLEncoder.encode(Integer.toString(pincode),"UTF-8")+"&"+
                        URLEncoder.encode("date","UTF-8")+"="+URLEncoder.encode(currentdate,"UTF-8");
                //Log.d("bc","bc");
                bufferedWriter.write(data_string);
                bufferedWriter.flush();
                bufferedWriter.close();
                InputStream inputStream=httpURLConnection.getInputStream();
                inputStream.close();
                httpURLConnection.disconnect();
            }
            catch(MalformedURLException e)
            {
                e.printStackTrace();
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }
            return null;
        }


        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            // Log.d(org,pcode);//Log.d(dest,days);
            Intent i=new Intent(mapspage2.this,myroute2.class);
            startActivity(i);

        }
    }
    public void onBackPressed()
    {
        Intent i=new Intent(mapspage2.this,main.class);
        startActivity(i);
    }
    int get_pincode()
    {
        return pcodei;
    }
    String get_source()
    {
        return orgi;
    }
    String get_destination() {
        return desti;
    }
    String get_go_time(){return tgi1;}
    String get_date(){return currentdate;}

}
