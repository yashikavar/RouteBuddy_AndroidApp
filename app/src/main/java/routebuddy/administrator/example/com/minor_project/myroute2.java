package routebuddy.administrator.example.com.minor_project;

import android.app.AlertDialog;
import android.app.ProgressDialog;
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
import android.widget.TextView;


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
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class myroute2 extends AppCompatActivity implements View.OnClickListener {
    TextView t1;
    TextView t2;
    ProgressDialog progressDialog;
    Button search, change;
    String JSON_STRING;
    static int c=0;
    static int v=0;
    int x,y;
    static int end=0;
    static int end1=0;
    static String def="n/a";
    List same_route=new ArrayList();
    static List<Float> all_route=new ArrayList<Float>();
    static List all_username=new ArrayList();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myroute2);
        search= (Button)findViewById(R.id.button_s);
        change=(Button)findViewById(R.id.button_c);
        t1=(TextView)findViewById(R.id.textView32);
        t2=(TextView)findViewById(R.id.textView34);
        t1.setText(new mapspage2().get_source());
        t2.setText(new mapspage2().get_destination());
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo == null && !networkInfo.isConnected()) {
            search.setEnabled(false);
        }
        search.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.button_s) {
            progressDialog = ProgressDialog.show(this, "Please wait.",
                    "Finding Friends..!", true);
            ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if (networkInfo == null && !networkInfo.isConnected()) {
                search.setEnabled(false);
            }
            new BackgroundTask().execute();

        } else if (v.getId() == R.id.button_c) {
            ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            new BackgroundTask1().execute();

        }
    }

    class BackgroundTask extends AsyncTask<Void, Void, String> implements DirectionFinderListener {


        String json_url;


        @Override
        protected void onPreExecute() {
            //    add_info_url="http://routebuddy.comli.com/add_info.php";
            json_url = "http://routebuddy.comli.com/json_get_data_instant.php";

        }

        @Override
        protected String doInBackground(Void... voids)
        {
            try {
                URL url = new URL(json_url);
                Log.d("a", "a");
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder = new StringBuilder();
                //JSON_STRING=new HashMap();
                while ((JSON_STRING = bufferedReader.readLine()) != null) {
                    stringBuilder.append(JSON_STRING + "\n");
                }

                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return stringBuilder.toString().trim();

            }
            catch (MalformedURLException e)
            {
                e.printStackTrace();
            }
            catch (IOException e)
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

            boolean b1 = false;
            String json_string;
            JSONObject jsonObject;
            JSONArray jsonArray;
            int pincode;
            String login;
            String source;
            String destination;
            String go_time;
            String date;
            //String come_time;
            String username;
            json_string = result;
            Log.d("0", "0");
            SharedPreferences pre = getSharedPreferences("bool", Context.MODE_PRIVATE);
            login = pre.getString("id", def);
            mapspage2 m = new mapspage2();
            int pcode = m.get_pincode();
            String org = m.get_source();
            String dest = m.get_destination();
            String go = m.get_go_time();
            String current_date=m.get_date();
            //String come = m.get_come_time();
            //String my_days = m.get_days();
            //String days;
            //get_json gj=new get_json();
            //gj.check_login(username,password);
            Log.d("1", "1");
            try {
                jsonObject = new JSONObject(json_string);
                jsonArray = jsonObject.getJSONArray("server_response");
                int count = 0;
                while (count < jsonArray.length()) {
                    JSONObject JO = jsonArray.getJSONObject(count);
                    username = JO.getString("username");
                    source = JO.getString("source");
                    destination = JO.getString("destination");
                    go_time = JO.getString("time");
                    //come_time = JO.getString("come_time");
                    //days = JO.getString("days");
                    pincode = Integer.parseInt(JO.getString("pincode"));
                    date=JO.getString("date");
                    /*DateFormat df = new SimpleDateFormat("hhmmss");
                    try {
                        Date time1 = (Date) df.parse(go_time);
                        Date time2 = (Date) df.parse(come_time);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    // call the dcryption function ans save in dcrypt pass n pass it

                    //name=JO.getString("name");
                    //email=JO.getString("email");

                    //phone=Integer.parseInt(JO.getString("phone"));*/
                    //Log.d("enter0","enter0");
                    if (!login.equals(username) && pincode == pcode && date.equals(current_date))//&& days.equals(my_days))
                    {
                        //Log.d("enter","enter");
                        if (check_time(go, go_time) == 1) {
                            // all_username.add(username);
                            end++;
                        }
                    }
                    count++;
                }
                if(end==0)
                {
                    progressDialog.dismiss();
                    AlertDialog.Builder builder = new AlertDialog.Builder(myroute2.this);
                    builder.setTitle("   Sorry !!  ");
                    builder.setMessage("No Friends Found :-(");
                    builder.setCancelable(true);

                    final AlertDialog closedialog= builder.create();

                    closedialog.show();

                    final Timer timer2 = new Timer();
                    timer2.schedule(new TimerTask() {
                        public void run() {
                            closedialog.dismiss();
                            timer2.cancel();
                            Intent i=new Intent(myroute2.this,main.class);
                            startActivity(i);//this will cancel the timer of the system
                        }
                    }, 3000);
                }
                count = 0;
                while (count < jsonArray.length()) {
                    JSONObject JO = jsonArray.getJSONObject(count);
                    username = JO.getString("username");
                    source = JO.getString("source");
                    destination = JO.getString("destination");
                    go_time = JO.getString("time");
                    //come_time = JO.getString("come_time");
                    //days = JO.getString("days");

                    pincode = Integer.parseInt(JO.getString("pincode"));
                    date=JO.getString("date");
                    /*DateFormat df = new SimpleDateFormat("hhmmss");
                    try {
                        Date time1 = (Date) df.parse(go_time);
                        Date time2 = (Date) df.parse(come_time);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    // call the dcryption function ans save in dcrypt pass n pass it

                    //name=JO.getString("name");
                    //email=JO.getString("email");

                    //phone=Integer.parseInt(JO.getString("phone"));*/
                    //Log.d("enter0","enter0");
                    if (!login.equals(username) && (pincode == pcode )  && date.equals(current_date)) {
                        //if (days.equals(my_days))

                        //Log.d("enter","enter");
                        if (check_time(go, go_time) == 1) {
                            all_username.add(username);
                            Log.d((String) all_username.get(all_username.size() - 1), "first");
                            //end++;
                            try {
                                new DirectionFinder(this, org, source).execute();
                                c++;
                            } catch (UnsupportedEncodingException e) {
                                //e.printStackTrace();jk
                            }
                            try {
                                new DirectionFinder(this, dest, destination).execute();
                                c++;
                            } catch (UnsupportedEncodingException e) {
                                //e.printStackTrace();
                            }


                        }

                }
                //Log.d(username,username);
                                    /*if (x == 1 && y == 1) {
                                        same_route.add(username);
                                        x = 0;
                                        y = 0;
                                    }
                                }
                        }
                        //Log.d("search","search");
                        count++;
                    }
                    for (int i = 0; i < same_route.size(); i++) {
                        Log.d((String) same_route.get(i), "people");
                    }
                    Log.d("done1", "done11");
                    Intent i = new Intent(myroutes.this, same_route_people.class);
                    startActivity(i);*/
                /*if (b1 == false) {
                    attempt_counter--;
                    Toast.makeText(loginpage.this, "attempts left " + attempt_counter, Toast.LENGTH_SHORT).show();
                    if (attempt_counter == 0) {
                        Toast.makeText(loginpage.this, "no attempts left!!", Toast.LENGTH_SHORT).show();
                        button.setEnabled(false);
                        finish();
                    }
                    Toast.makeText(loginpage.this, "incorrect username or password", Toast.LENGTH_LONG).show();
                    editText1.setText("");
                    editText2.setText("");
                }*/
                count++;
            }

            }
            catch(Exception e)
            {

            }


        }

        @Override
        public void onDirectionFinderStart() {
            Log.d("start", "start");
        }

        @Override
        public void onDirectionFinderSuccess(List<Route> routes) {

            Log.d("howmay","how");
            for (Route route : routes) {

                String s1 = route.distance.text;
                //Log.d(s1,s1)
                Log.d(s1, s1);
                String k = new String("");
                int i = 0;
                while (s1.charAt(i) != 32) {
                    k = k + s1.charAt(i);
                    i++;
                }
                all_route.add(Float.parseFloat(k));
                Log.d(Float.toString((Float)all_route.get(all_route.size()-1)),"size_route");
                end1++;
                if(end1==end*2)
                {
                    progressDialog.dismiss();
                    Intent i1 = new Intent(myroute2.this, same_route_people_instant.class);
                    startActivity(i1);
                }
                //s1.substring(0,3);
                //give_route(s1);
                    /*Log.d(k, "s");
                    if (c % 2 == 0) {
                        if (Float.parseFloat(k) <= 2)
                            x = 1;
                        else x = 0;

                    } else {
                        if (Float.parseFloat(k) <= 2) {
                            y = 1;
                        } else
                            y = 0;
                    }*/

            }


        }
    }


    int check_time(String go, String go_time) {
        int i1, i2, i3, i4;
        Log.d("hh","hh");
        String s1 = go.substring(3,5);
        i1 = Integer.parseInt(s1);
        Log.d(Integer.toString(i1),"i1");
        String s2 = go.substring(0,2);
        i2 = Integer.parseInt(s2);
        String s3 = go_time.substring(3,5);
        i3 = Integer.parseInt(s3);
        String s4 = go_time.substring(0,2);
        i4 = Integer.parseInt(s4);
        Log.d(Integer.toString(i1),Integer.toString(i3));
        if(go.equals(go_time))
        {
            return 1;
        }
        else if (i1 > 10 && i1 < 50)
        {
            if ((i3 <= (i1 + 10) && i3 >= i1) || (i3 >= (i1 - 10) && i3 <= i1))
            {
                return 1;
            } else return 0;
        }
        else if(i1==0)
        {
            if(i4==i2)
            {
                if(i3<=10)
                {
                    return 1;
                }
                else return 0;
            }
            if(i4==i2-1)
            {
                if(i3>=50)
                {
                    return 1;
                }
                else return 0;
            }
        }
        else if(i1==10)
        {
            if(i4==i2 && i3<=20 && i3>=10)
            {
                return 1;
            }
            else if(i4==i2-1 && i3>=50)
            {
                return 1;
            }
            else return 0;
        }
        else if (i1 < 10)
        {
            if (i4 == i2 - 1)
            {
                if ((i3<=59) && i3>=60-10+i1)
                {
                    return 1;
                }

               else return 0;
            }
            else if(i4 == i2 && i3>=0 && i3<=i1+10)
            {
                return 1;
            }
            else return 0;
        }
        else if (i1==50)
        {
            if (i4 == i2 && i3>=40 && i3<=59)
            {
                    return 1;
            }
            else if(i4==i2+1 && i3==00)
            {
                return 1;
            }
            else return 0;
        }
        else if(i1>50)
        {
            if(i4==i2)
            {
                if(i3<=59 && i3>=i1-10)
                {
                    return 1;
                }
                else return 0;
            }
            else if(i4==i2+1)
            {
                if(i3>=0 && i3<=i1-50)
                {
                    return 1;
                }
                else return 0;
            }
        }
        return 0;

    }
    List give_all_route()
    {
        for(int i=0;i<all_route.size();i++)
        {
            Log.d("c","c");
            Log.d(Float.toString(all_route.get(i)),"u");
        }
        return all_route;
    }
    List give_all_username()
    {
        Log.d(Integer.toString(all_username.size()),"size1");
        for(int i=0;i<all_username.size();i++)
        {
            Log.d((String)all_username.get(i),"y");
        }
        return all_username;
    }

    class BackgroundTask1 extends AsyncTask<String,Void, Void> {


        String json_url;
        SharedPreferences pre = getSharedPreferences("bool", Context.MODE_PRIVATE);
        String username = pre.getString("id", def);
        @Override
        protected void onPreExecute() {
            Log.d("cc","cc");
            //    add_info_url="http://routebuddy.comli.com/add_info.php";
            json_url = "http://routebuddy.comli.com/json_remove_data_instant.php";

        }

        @Override
        protected Void doInBackground(String... args) {
            try {


                URL url = new URL(json_url);
                Log.d("a", "a");
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                Log.d("given_username",username);
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String data_string = URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(username, "UTF-8");
                bufferedWriter.write(data_string);
                bufferedWriter.flush();
                bufferedWriter.close();
                InputStream inputStream=httpURLConnection.getInputStream();
                inputStream.close();
                httpURLConnection.disconnect();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }


        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);

        }

        @Override
        protected void onPostExecute(Void a) {
            Intent i = new Intent(myroute2.this, MapsActivity2.class);
            startActivity(i);
        }
    }

    public void onBackPressed()
    {
        Intent i=new Intent(myroute2.this,main.class);
        startActivity(i);
    }

}


