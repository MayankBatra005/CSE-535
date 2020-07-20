package edu.asu.msse.mbatra3.covidtracker.Activities;

import androidx.appcompat.app.AppCompatActivity;
import edu.asu.msse.mbatra3.covidtracker.Model.Data;
import edu.asu.msse.mbatra3.covidtracker.R;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_screen);
    }

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

               if(Data.getInstance().insertData(""+1,""+2,""+3)){
                   Log.i("Insertion","Success");}
               String result=Data.getInstance().fetchData(""+1,""+2);
               Log.i("Result",result);
            }
            Intent home=new Intent(this,HomeScreen.class);
            startActivity(home);
        }else{
            Toast.makeText(this, "Please enter Valid credentials",
                    Toast.LENGTH_SHORT).show();
        }
    }

}
