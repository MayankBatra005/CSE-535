package edu.asu.msse.mbatra3.covidtracker.Activities;

import androidx.appcompat.app.AppCompatActivity;
import edu.asu.msse.mbatra3.covidtracker.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_screen);
    }

   public void submitSuccess(View v)
    {
        Toast.makeText(this, "Submit success", Toast.LENGTH_SHORT).show();
        Intent home=new Intent(this,HomeScreen.class);
        startActivity(home);

    }

}
