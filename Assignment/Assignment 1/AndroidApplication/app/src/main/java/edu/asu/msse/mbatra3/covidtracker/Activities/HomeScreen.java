package edu.asu.msse.mbatra3.covidtracker.Activities;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import edu.asu.msse.mbatra3.covidtracker.R;
import edu.asu.msse.mbatra3.covidtracker.utilities.UnboundDataPostingService;

import android.view.View;
import android.widget.Button;

public class HomeScreen extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_screen);

        Button bt1 = (Button) findViewById(R.id.locationConsent);

        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeScreen.this, UnboundDataPostingService.class);
                startService(intent);
            }
        });

    }
    public void moveToChatScreen(View v){
    Intent chatScreen= new Intent(this,ChatScreen.class);
    startActivity(chatScreen);
    }

    public void activateService(View view)throws  Exception{
        // Starting the service
            Intent intent = new Intent(HomeScreen.this, UnboundDataPostingService.class);
            startService(intent);

//        Intent int1 = new Intent(MainActivity.this, MyServiceExTuesday.class);
//        startService(int1);
         //   Thread.sleep(5000);
        // Stopping the service after 5 seconds
        // stopService(intent);

    }

}
