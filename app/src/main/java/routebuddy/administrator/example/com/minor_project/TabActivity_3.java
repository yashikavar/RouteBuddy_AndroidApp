package routebuddy.administrator.example.com.minor_project;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import se.simbio.encryption.Encryption;


public class TabActivity_3 extends Activity implements View.OnClickListener {

    Button send_msg;
    EditText newMessageView;
   static String newmsg;
   static String sender_uname;
   static String r_name;
    static String def="n/a";
     static String complete_mssg;
    TextView show;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_activity_3);
       show=(TextView)findViewById(R.id.show_msg);
        Log.d("chat1","chat1");


        send_msg=(Button)findViewById(R.id.send_message);
        send_msg.setOnClickListener(this);
        newMessageView=(EditText)findViewById(R.id.new_message);
        SharedPreferences si= getSharedPreferences("bool", Context.MODE_PRIVATE);
        r_name=si.getString("recname", def);
        Log.d("recname",r_name);
        r_name=r_name.trim();
        sender_uname=si.getString("id",def);

       // ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>
          //      (this, android.R.layout.simple_list_item_1, );
        //ll.setAdapter(arrayAdapter);

        ConnectivityManager connectivityManager=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo=connectivityManager.getActiveNetworkInfo();
        if(networkInfo==null && !networkInfo.isConnected())
        {
            send_msg.setEnabled(false);

        }

    }
    public void onBackPressed()
    {
        Intent i=new Intent(this,main.class);
        startActivity(i);
        show.setText("");
    }

    @Override
    public void onClick(View view) {
        Log.d("chat2","chat2");

        newmsg=newMessageView.getText().toString();
        newMessageView.setText("");
        complete_mssg=sender_uname+":"+newmsg;
            show.setText(complete_mssg);
        Log.d("chat3","chat3");
        BackgroundTask backgroundTask = new BackgroundTask();
        backgroundTask.execute(complete_mssg,r_name);
        Log.d("chat4","chat4");
    }

    class BackgroundTask extends AsyncTask<String,Void,Void> {
        String add_info_url;
        @Override
        protected  void onPreExecute()
        {
            add_info_url="http://routebuddy.comli.com/file_push.php";
            Log.d("chat5","chat5");

        }

        @Override
        protected Void doInBackground(String... args) {
            String msg, recname;
            msg=args[0];
            recname=args[1];

            Log.d("reg1chat","reg1chat");
            String c=null;
            try {


                URL url = new URL(add_info_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter=new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                String data_string= URLEncoder.encode("message","UTF-8")+"="+URLEncoder.encode(msg,"UTF-8")+"&"+
                        URLEncoder.encode("rec_name","UTF-8")+"="+URLEncoder.encode(recname,"UTF-8");

                Log.d("reg2chat","reg2chat");

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

        }
    }

}
