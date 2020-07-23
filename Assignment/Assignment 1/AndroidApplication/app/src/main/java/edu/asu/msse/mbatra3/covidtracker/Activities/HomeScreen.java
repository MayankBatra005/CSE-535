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


}
