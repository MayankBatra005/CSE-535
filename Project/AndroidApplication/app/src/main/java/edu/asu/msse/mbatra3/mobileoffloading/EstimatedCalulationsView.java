package edu.asu.msse.mbatra3.mobileoffloading;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class EstimatedCalulationsView extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.estimated_calculations_screen);
        Intent intent = getIntent();
        ((TextView) findViewById(R.id.estimation1)).setText(intent.
                getStringExtra("estimation1"));
        ((TextView) findViewById(R.id.estimation2)).setText(intent.
                getStringExtra("estimation2"));
    }
}
