package routebuddy.administrator.example.com.minor_project;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
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
import java.sql.Date;
import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class mapspage3 extends AppCompatActivity implements View.OnClickListener{

    TextView e1;
    TextView e2;
    EditText e3;
    EditText e4;
    EditText e5;
    EditText e6;
    EditText e7;
    CheckBox ch1;
    CheckBox ch2;
    CheckBox ch3;
    CheckBox ch4;
    CheckBox ch5;
    CheckBox ch6;
    CheckBox ch7;
    static String def="n/a";
    static String days="";
    static String days1;
    static Time t1;
    static Time t2;
    static String org;
    static String dest;
    static String unme;
    static int pcode;
    static String tg1;
    static String tg2;
    static String tc1;
    static String tc2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapspage3);
        Log.d("mapspage","m");
        Button b1= (Button)findViewById(R.id.btnadd1);
        e1= (TextView)findViewById(R.id.source_add);
        e2= (TextView)findViewById(R.id.dest_add);
        e3= (EditText)findViewById(R.id.pin_code);
        e4= (EditText)findViewById(R.id.time_g1);
        e5= (EditText)findViewById(R.id.time_g2);
        e6= (EditText)findViewById(R.id.time_c1);
        e7= (EditText)findViewById(R.id.time_c2);
        ch1=(CheckBox)findViewById(R.id.checkBox_mon);
        ch2=(CheckBox)findViewById(R.id.checkBox_tue);
        ch3=(CheckBox)findViewById(R.id.checkBox_wed);
        ch4=(CheckBox)findViewById(R.id.checkBox_thr);
        ch5=(CheckBox)findViewById(R.id.checkBox_fri);
        ch6=(CheckBox)findViewById(R.id.checkBox_sat);
        ch7=(CheckBox)findViewById(R.id.checkBox_sun);
        mapspage lm= new mapspage();
        unme= lm.get_username();
        Log.d("obtained username",unme);
        MapsActivity2 mm=new MapsActivity2();
        org=mm.get_source();
        dest=mm.get_dest();
        Log.d(org,dest);
        //Log.d("days",days);
        //int x=
        //SharedPreferences pre=getSharedPreferences("bool", Context.MODE_PRIVATE);
        //unme=pre.getString("id", def);

        e1.setText(org);
        e2.setText(dest);
        b1.setOnClickListener(this);
        Log.d("abcd","abcd");
        ConnectivityManager connectivityManager=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo=connectivityManager.getActiveNetworkInfo();
        if(networkInfo==null && !networkInfo.isConnected())
        {
            b1.setEnabled(false);

        }

    }




    @Override
    public void onClick(View v) {
        tg1 = e4.getText().toString();
        tg2 = e5.getText().toString();


        tc1 = e6.getText().toString();
        tc2 = e7.getText().toString();

        pcode = Integer.parseInt(e3.getText().toString());
        //DateFormat df=new SimpleDateFormat("hhmm");
        //SimpleDateFormat df2 = new SimpleDateFormat("hh:mm");

       /* try {
            Date time1 = (Date)df.parse(tg1);
            time_go = df2.format(time1);
            Date time2=(Date)df.parse(tc1);
            time_come=df.format(time2);

            Log.d(time_go,time_come);
        } catch (ParseException e) {
            e.printStackTrace();
        }*/


        //days="";
        if (ch1.isChecked()) {
            days = days + "0";
        }
        if (ch2.isChecked()) {
            days = days + "1";
        }
        if (ch3.isChecked()) {
            days = days + "2";
        }
        if (ch4.isChecked()) {
            days = days + "3";
        }
        if (ch5.isChecked()) {
            days = days + "4";

        }
        if (ch6.isChecked()) {
            days = days + "5";
        }
        if (ch7.isChecked()) {
            days = days + "6";
        }
        Log.d(days, days);
        days1 = days;
        if (v.getId() == R.id.btnadd1) {
            int l1 = tg1.length();
            int l2 = tg2.length();
            int h1 = Integer.parseInt(tg1);
            int m1 = Integer.parseInt(tg2);
            int v1 = tc1.length();
            int v2 = tc2.length();
            int hh1 = Integer.parseInt(tc1);
            int mm1 = Integer.parseInt(tc2);
            tg1 = tg1 + ":" + tg2 + ":" + "00";
            tc1 = tc1 + ":" + tc2 + ":" + "00";
            if (l1 == 2 && l2 == 2 && h1 >= 00 && h1 <= 24 && m1 >= 00 && m1 <= 60)
            {
                if (v1 == 2 && v2 == 2 && hh1 >= 00 && hh1 <= 24 && mm1 >= 00 && mm1 <= 60)
                {
                    if(!e3.getText().toString().isEmpty() && e3.getText().toString().length()==6)
                    {
                        if(ch1.isChecked() || ch2.isChecked() || ch3.isChecked() || ch4.isChecked() || ch5.isChecked() || ch6.isChecked() || ch7.isChecked())
                        {

                            BackgroundTask backgroundTask = new BackgroundTask();
                            backgroundTask.execute(unme, org, dest, tg1, tc1, days, Integer.toString(pcode));
                            //days="";
                        }
                        else
                        {
                            Toast.makeText(this,"Enter week days",Toast.LENGTH_LONG).show();
                        }
                    }
                    else
                    {
                        Toast.makeText(this,"Incorrect Pincode !!",Toast.LENGTH_LONG).show();
                    }
                }
                else
                {
                    Toast.makeText(this,"Incorrect Time_Format !!",Toast.LENGTH_LONG).show();
                }
            }
            else
            {
                Toast.makeText(this,"incorrect Time_Format",Toast.LENGTH_LONG).show();
            }
        }
    }
    class BackgroundTask extends AsyncTask<String,Void,Void> {
        String add_info_url;
        @Override
        protected  void onPreExecute()
        {
            add_info_url="http://routebuddy.comli.com/add_info_daily.php";
        }

        @Override
        protected Void doInBackground(String... args) {
            try {
                URL url = new URL(add_info_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                String username,source,destination,go_time,come_time,days;
                int pincode;
                username=args[0];
                source=args[1];
                destination=args[2];
                go_time=args[3];
                come_time=args[4];
                pincode=Integer.parseInt(args[6]);
                days=args[5];
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter=new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                String data_string= URLEncoder.encode("username","UTF-8")+"="+URLEncoder.encode(username,"UTF-8")+"&"+
                        URLEncoder.encode("source","UTF-8")+"="+URLEncoder.encode(source,"UTF-8")+"&"+
                        URLEncoder.encode("destination","UTF-8")+"="+URLEncoder.encode(destination,"UTF-8")+"&"+
                        URLEncoder.encode("go_time","UTF-8")+"="+URLEncoder.encode(go_time,"UTF-8")+"&"+
                        URLEncoder.encode("come_time","UTF-8")+"="+ URLEncoder.encode(come_time,"UTF-8")+"&"+
                        URLEncoder.encode("days","UTF-8")+"="+ URLEncoder.encode(days,"UTF-8")+"&"+
                        URLEncoder.encode("pincode","UTF-8")+"="+ URLEncoder.encode(Integer.toString(pincode),"UTF-8");
                //Log.d("bc","bc");
                days="";
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
            days="";
            Log.d("done","done");
            Intent i=new Intent(mapspage3.this,myroutes.class);
            startActivity(i);
        }
    }

    int get_pincode()
    {
        return pcode;
    }
    String get_source()
    {
        return org;
    }
    String get_destination() {
        return dest;
    }
    String get_go_time(){return tg1;}
    String get_come_time(){return tc1;}
    String get_days(){return days1;}
    public void onBackPressed()
    {
        Intent i=new Intent(mapspage3.this,main.class);
        startActivity(i);
    }
}
