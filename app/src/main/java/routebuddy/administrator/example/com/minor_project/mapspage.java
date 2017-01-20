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
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class mapspage extends AppCompatActivity implements View.OnClickListener {

    Button button1;
    Button button2;
    Button myroute;
    static String source_d;
    static String destination_d;
    static String go_time_d;
    static String come_time_d;
    static int pincode_d;
    static String days_d;
    static String source_i;
    static String destination_i;
    static String go_time_i;
    static String date_i;
    static int pincode_i;

    static int h;
    static String username;
    String JSON_STRING;
    static String def="n/a";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapspage);
        button1=(Button)findViewById(R.id.btninstantroute);
        button1.setOnClickListener(this);
        button2=(Button)findViewById(R.id.btndailyroute);
        button2.setOnClickListener(this);
        SharedPreferences pre=getSharedPreferences("bool", Context.MODE_PRIVATE);
        username=pre.getString("id", def);
        ConnectivityManager connectivityManager=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo=connectivityManager.getActiveNetworkInfo();
        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btninstantroute:
                h=0;
                new BackgroundTask1().execute();
                break;
            case R.id.btndailyroute:
                h=1;
                new BackgroundTask().execute();
                break;

        }
    }
    class BackgroundTask extends AsyncTask<Void,Void,String> {

        String json_url;


        @Override
        protected  void onPreExecute()
        {
            //    add_info_url="http://routebuddy.comli.com/add_info.php";
            json_url= "http://routebuddy.comli.com/json_get_data_daily_check.php";

        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                URL url = new URL(json_url);
                Log.d(username,"a");
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter=new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                String data_string= URLEncoder.encode("username","UTF-8")+"="+URLEncoder.encode(username,"UTF-8");
                bufferedWriter.write(data_string);
                bufferedWriter.flush();
                bufferedWriter.close();
                InputStream inputStream=httpURLConnection.getInputStream();
                BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder= new StringBuilder();
                //JSON_STRING=new HashMap();
                while((JSON_STRING = bufferedReader.readLine())!=null)
                {
                    stringBuilder.append(JSON_STRING+"\n");
                }

                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return stringBuilder.toString().trim();

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
        protected void onPostExecute(String result) {

            boolean b1=false;
            String json_string;
            JSONObject jsonObject;
            JSONArray jsonArray;
            String loginid;
            String pass ;
            json_string=result;
            //get_json gj=new get_json();
            //gj.check_login(username,password);
            try
            {
                jsonObject=new JSONObject(json_string);
                jsonArray=jsonObject.getJSONArray("server_response");
                int count=0;
                Log.d("c","c");
                if(jsonArray.length()==0)
                {
                    h=1;
                    Intent i=new Intent(mapspage.this,MapsActivity2.class);
                    startActivity(i);
                }
                else
                {
                    Log.d(json_string,"json");
                    while (count < jsonArray.length()) {
                        Log.d("inside","inside");
                        JSONObject JO = jsonArray.getJSONObject(count);
                        source_d = JO.getString("source");
                        destination_d = JO.getString("destination");
                        go_time_d = JO.getString("go_time");
                        come_time_d = JO.getString("come_time");
                        days_d = JO.getString("days");
                        pincode_d = Integer.parseInt(JO.getString("pincode"));
                        count++;
                    }
                    Intent i = new Intent(mapspage.this, daily_route.class);
                    startActivity(i);
                }
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
        }
    }
    class BackgroundTask1 extends AsyncTask<Void,Void,String> {

        String json_url;


        @Override
        protected  void onPreExecute()
        {
            //    add_info_url="http://routebuddy.comli.com/add_info.php";
            json_url= "http://routebuddy.comli.com/json_get_data_instant_check.php";

        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                URL url = new URL(json_url);
                Log.d(username,"a");
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter=new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                String data_string= URLEncoder.encode("username","UTF-8")+"="+URLEncoder.encode(username,"UTF-8");
                bufferedWriter.write(data_string);
                bufferedWriter.flush();
                bufferedWriter.close();
                InputStream inputStream=httpURLConnection.getInputStream();
                BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder= new StringBuilder();
                //JSON_STRING=new HashMap();
                while((JSON_STRING = bufferedReader.readLine())!=null)
                {
                    stringBuilder.append(JSON_STRING+"\n");
                }

                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return stringBuilder.toString().trim();

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
        protected void onPostExecute(String result) {

            boolean b1=false;
            String json_string;
            JSONObject jsonObject;
            JSONArray jsonArray;
            String loginid;
            String pass ;
            json_string=result;
            //get_json gj=new get_json();
            //gj.check_login(username,password);
            try
            {
                jsonObject=new JSONObject(json_string);
                jsonArray=jsonObject.getJSONArray("server_response");
                int count=0;
                Log.d("c","c");
                if(jsonArray.length()==0)
                {
                    h=0;
                    Intent i=new Intent(mapspage.this,MapsActivity2.class);
                    startActivity(i);
                }
                else
                {
                    Log.d(json_string,"json");
                    while (count < jsonArray.length()) {
                        Log.d("inside","inside");
                        JSONObject JO = jsonArray.getJSONObject(count);
                        source_i = JO.getString("source");
                        destination_i = JO.getString("destination");
                        go_time_i = JO.getString("go_time");
                        date_i = JO.getString("date");
                        pincode_i = Integer.parseInt(JO.getString("pincode"));
                        count++;
                    }
                    Intent i = new Intent(mapspage.this, instant_route.class);
                    startActivity(i);
                }
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
        }
    }
    public int get_h()
    {
        return h;
    }

    public String get_source_d()
    {
        return source_d;
    }
    public String get_destination_d()
    {
        return destination_d;
    }
    public String get_days_d()
    {
        return days_d;
    }
    public int get_pincode_d()
    {
        return pincode_d;
    }
    public String get_go_time_d()
    {
        return go_time_d;
    }
    public String get_come_time_d()
    {
        return come_time_d;
    }
    public String get_username()
    {
        return username;
    }
    public String get_source_i()
    {
        return source_i;
    }
    public String get_destination_i()
    {
        return destination_i;
    }
    public String get_date_i()
    {
        return date_i;
    }
    public int get_pincode_i()
    {
        return pincode_i;
    }
    public String get_go_time_i()
    {
        return go_time_i;
    }
    public void onBackPressed()
    {
        Intent i=new Intent(mapspage.this,main.class);
        startActivity(i);
    }
}
