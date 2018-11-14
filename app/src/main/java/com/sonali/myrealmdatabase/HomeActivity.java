package com.sonali.myrealmdatabase;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {


    private LinearLayout llWomen, llMen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(HomeActivity.this,AddPlayerActivity.class);
                startActivity(i);
            }
        });
        getControls();
    }

    private void getControls() {
        llWomen = findViewById(R.id.llWomen);
        llMen = findViewById(R.id.llMen);
        llWomen.setOnClickListener(this);
        llMen.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.llWomen:
                Intent intent = new Intent(HomeActivity.this, PlayersActivity.class)
                        .putExtra("type","women");
                startActivity(intent);
                break;
            case R.id.llMen:
                Intent i = new Intent(HomeActivity.this, PlayersActivity.class)
                        .putExtra("type","men");
                startActivity(i);
                break;
        }
    }
}
