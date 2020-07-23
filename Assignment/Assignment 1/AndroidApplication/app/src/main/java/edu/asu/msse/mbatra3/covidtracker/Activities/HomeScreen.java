package edu.asu.msse.mbatra3.covidtracker.Activities;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import edu.asu.msse.mbatra3.covidtracker.R;
import edu.asu.msse.mbatra3.covidtracker.utilities.UnboundDataPostingService;

import android.view.View;

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

    public void activateService(View view)throws  Exception{
        // Starting the service
            Intent intent = new Intent(this, UnboundDataPostingService.class);
            startService(intent);
         //   Thread.sleep(5000);
        // Stopping the service after 5 seconds
        // stopService(intent);

    }

}
