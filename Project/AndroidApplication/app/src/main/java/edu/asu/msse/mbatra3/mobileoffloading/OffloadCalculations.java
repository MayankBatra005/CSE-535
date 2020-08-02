package edu.asu.msse.mbatra3.mobileoffloading;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class OffloadCalculations extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.offload_calculations_screen);

        Intent intent = getIntent();
        ((TextView) findViewById(R.id.row1)).setText(intent.getStringExtra("row1"));
        ((TextView) findViewById(R.id.row2)).setText(intent.getStringExtra("row2"));
        ((TextView) findViewById(R.id.row3)).setText(intent.getStringExtra("row3"));
        ((TextView) findViewById(R.id.row4)).setText(intent.getStringExtra("row4"));
        ((TextView) findViewById(R.id.estimation1)).setText(intent.
                getStringExtra("estimation1"));
        ((TextView) findViewById(R.id.estimation2)).setText(intent.
                getStringExtra("estimation2"));
    }
}
