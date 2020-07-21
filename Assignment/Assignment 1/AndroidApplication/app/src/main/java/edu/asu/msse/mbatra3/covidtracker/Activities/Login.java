package edu.asu.msse.mbatra3.covidtracker.Activities;

import androidx.appcompat.app.AppCompatActivity;
import edu.asu.msse.mbatra3.covidtracker.Model.Data;
import edu.asu.msse.mbatra3.covidtracker.R;
import edu.asu.msse.mbatra3.covidtracker.utilities.BoundService;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.SearchRecentSuggestions;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Login extends AppCompatActivity {

    BoundService myService;
    boolean isBound = false;

    public void showText(View v) {
        String currentText = myService.getCurrentText();
        TextView myTextView = (TextView) findViewById(R.id.myTextView);
        myTextView.setText(currentText);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_screen);
        Intent intent = new Intent(this, BoundService.class);
        bindService(intent, myConnection, Context.BIND_AUTO_CREATE);
    }

    private ServiceConnection myConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            BoundService.MyLocalBinder binder = (BoundService.MyLocalBinder) iBinder;
            myService = binder.getService();
            isBound = true;

        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            isBound = false;

        }
    };

   public void submitSuccess(View v){
        EditText Password,userName;
        userName=findViewById(R.id.userName);
        Password=findViewById(R.id.Password);
        String usr,pswd;
        usr=userName.getText().toString();
        pswd=Password.getText().toString();
        if((usr.equals(getString(R.string.user)))&&(pswd.equals(getString(R.string.passwd))))
        {
            Log.i("User login","Success");
            // DB not created intialise it
            if(Data.getInstance().getDb()==null){
                Data.getInstance().setDbName(this,getString(R.string.user));
               if( Data.getInstance().initDB(this))
                   Log.i("Database initialisation","Success");
            /*
                Code to verify database insertion and fetching
               if(Data.getInstance().insertData(""+1,""+2,""+3)){
                   Log.i("Insertion","Success");}
               String result=Data.getInstance().fetchData(""+1,""+2);
               Log.i("Result",result);
            */
            }
            Intent home=new Intent(this,HomeScreen.class);
            startActivity(home);
        }else{
            Toast.makeText(this, "Please enter Valid credentials",
                    Toast.LENGTH_SHORT).show();
        }
    }

}
