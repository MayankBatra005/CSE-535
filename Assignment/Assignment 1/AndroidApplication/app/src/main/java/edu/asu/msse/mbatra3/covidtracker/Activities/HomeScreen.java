package edu.asu.msse.mbatra3.covidtracker.Activities;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import edu.asu.msse.mbatra3.covidtracker.R;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class HomeScreen extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_screen);
    }
    public void moveToChatScreen(View v){
    Intent chatScreen= new Intent(this,ChatScreen.class);
    startActivity(chatScreen);
    }

}
