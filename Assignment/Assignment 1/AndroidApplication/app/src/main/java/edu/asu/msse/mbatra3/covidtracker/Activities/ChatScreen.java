package edu.asu.msse.mbatra3.covidtracker.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.concurrent.ExecutionException;

import androidx.appcompat.app.AppCompatActivity;
import edu.asu.msse.mbatra3.covidtracker.R;
import edu.asu.msse.mbatra3.covidtracker.utilities.ChatHelper;
import edu.asu.msse.mbatra3.covidtracker.utilities.UploadTask;

public class ChatScreen extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_screen);

    }

    public void updateChatWindow(View v) {
        EditText chatMessage=findViewById(R.id.chatMessage);
        TextView chatWindow=findViewById(R.id.chatWindow);
        chatWindow.setText("Chat updated "+chatMessage.getText());
        // chatMessage.setText(" ");
        // Check if text file is created or not
        // if not created then create one
        // and add the text to the file
        // else append text to the file

        // in the end upload the text file

        ChatHelper task= ChatHelper.getInstance();
        task.initFile(this);
        // Extension changed
        task.generateChatFile("ChatFile.txt",chatMessage.getText().toString());
        UploadTask upload=new UploadTask(this);
        upload.execute();
        chatMessage.setText(" ");
//        String result=null;
//        try {
//            result =
////            task.execute("https://www.google.com/").get();
//                    task.execute("http://10.218.107.121/").get();
//        }catch (Exception E){
//
//        }    Log.i("Result from ASU server",result);
//
//        Intent location=new Intent(this,LocationScreen.class);
//        startActivity(location);

    }


}
