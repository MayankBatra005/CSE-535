package edu.asu.msse.mbatra3.covidtracker.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import edu.asu.msse.mbatra3.covidtracker.R;

public class ChatScreen extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_screen);
    }

    public void updateChatWindow(View v) throws InterruptedException {
        EditText chatMessage=findViewById(R.id.chatMessage);
        TextView chatWindow=findViewById(R.id.chatWindow);
        chatWindow.setText("Chat updated "+chatMessage.getText());
        chatMessage.setText(" ");

        Intent location=new Intent(this,LocationScreen.class);
        startActivity(location);

    }


}
