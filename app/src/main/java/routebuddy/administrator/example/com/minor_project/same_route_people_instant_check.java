package routebuddy.administrator.example.com.minor_project;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class same_route_people_instant_check extends AppCompatActivity
{
    List all_route;
    List all_username;
    List same_route=new ArrayList();
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_same_route_people_instant_check);
        ListView lv = (ListView) findViewById(R.id.lv3);
        Log.d("same_route","same_route");
        all_username=new daily_route().give_all_username();
        Log.d(Integer.toString(all_username.size()),"size");
        all_route=new daily_route().give_all_route();
        same_route.clear();
        same_route=give_same_route_people();
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_list_item_1, same_route);
        lv.setAdapter(arrayAdapter);
        lv.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d("pro5","pro5");
                String pro = ((TextView) view).getText().toString();
                Log.d("pro4","pro4");
                Toast.makeText(view.getContext(), pro, Toast.LENGTH_LONG).show();
                SharedPreferences sharedPreferences = same_route_people_instant_check.this.getSharedPreferences("bool", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();

                editor.putString("recname",pro);
                editor.commit();
                // Launching new Activity on selecting single List Item
                Intent itt = new Intent(view.getContext(), rec_profile.class);
                // sending data to new activity
                //  i.putExtra("product", product);
                startActivity(itt);
            }
        });

    }

    List give_same_route_people()
    {
        Log.d("yyyyyyyy","yy");
        for(int i=0,j=0;i<all_username.size();i++)
        {
            if((Float)all_route.get(j)<=1.0 )
            {
                if ((Float) all_route.get(j+1)<=1.0) {
                    same_route.add(all_username.get(i));
                }
                Log.d("y", "y");
            }
            j=j+2;
        }
        Log.d("yuyuyu","yuyuyuyuy");
        if(same_route.size()==0)
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(same_route_people_instant_check.this);
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
                    Intent i=new Intent(same_route_people_instant_check.this,main.class);
                    startActivity(i);//this will cancel the timer of the system
                }
            }, 3000);
        }
        for(int i=0;i<same_route.size();i++)
        {
            Log.d((String)same_route.get(i),"yaaayyy");
        }
        return same_route;
    }
    public void onBackPressed()
    {
        same_route.clear();
        all_username.clear();
        all_route.clear();
        Intent i=new Intent(same_route_people_instant_check.this,main.class);
        startActivity(i);
    }
}
