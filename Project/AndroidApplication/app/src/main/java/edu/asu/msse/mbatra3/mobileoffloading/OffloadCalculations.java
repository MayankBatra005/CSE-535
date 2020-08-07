package edu.asu.msse.mbatra3.mobileoffloading;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class OffloadCalculations extends AppCompatActivity {
    public String estimate1,estimate2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.offload_calculations_screen);

        Intent intent = getIntent();
        ((TextView) findViewById(R.id.row1)).setText(intent.getStringExtra("row1"));
        ((TextView) findViewById(R.id.row2)).setText(intent.getStringExtra("row2"));
        ((TextView) findViewById(R.id.row3)).setText(intent.getStringExtra("row3"));
        ((TextView) findViewById(R.id.row4)).setText(intent.getStringExtra("row4"));
        estimate1=intent.getStringExtra("estimation1");
        estimate2=intent.getStringExtra("estimation2");
    }

    public void navigateToEstimates(View view){
        Intent intent=new Intent(this,EstimatedCalulationsView.class);
        intent.putExtra("estimation1",estimate1);
        intent.putExtra("estimation2",estimate2);
        startActivity(intent);

    }
}
