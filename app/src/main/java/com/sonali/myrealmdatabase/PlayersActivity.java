package com.sonali.myrealmdatabase;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.sonali.myrealmdatabase.adapter.PlayerAdapter;
import com.sonali.myrealmdatabase.model.Player;
import com.sonali.myrealmdatabase.ui.GridRecyclerView;
import com.yarolegovich.lovelydialog.LovelyStandardDialog;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

public class PlayersActivity extends AppCompatActivity {

    private PlayerAdapter adapter;
    // private List<Player> alPlayers;
    private Realm realm;
    private GridRecyclerView rvPlayers;
    private List<Player> alWomen, alMen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_players);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(PlayersActivity.this, AddPlayerActivity.class);
                startActivity(i);
            }
        });

        getControls();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //on selecting on item in menu
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void getControls() {
        // alPlayers=new ArrayList<>();
        Realm.init(this);
        realm = Realm.getDefaultInstance();
        rvPlayers = findViewById(R.id.rvPlayers);
        GridLayoutManager linearLayoutManager = new GridLayoutManager(PlayersActivity.this, 1);
        rvPlayers.setLayoutManager(linearLayoutManager);
        alWomen = new ArrayList<>();
        alMen = new ArrayList<>();
        getPlayers();

    }

    private void getPlayers() {
        try {
            RealmResults<Player> alPlayers = realm.where(Player.class).findAll();

            if (alPlayers.size() > 0 && alPlayers != null) {
                if (getIntent().getExtras().getString("type") != null) {

                    for (int i = 0; i < alPlayers.size(); i++) {
                        if (alPlayers.get(i).getppGender().equalsIgnoreCase("Female")) {
                            Player player = new Player();
                            player.setId(alPlayers.get(i).getId());
                            player.setpName(alPlayers.get(i).getpName());
                            player.setppGender(alPlayers.get(i).getppGender());
                            player.setpYear(alPlayers.get(i).getpYear());
                            player.setpImag(alPlayers.get(i).getpImag());
                            alWomen.add(player);
                        } else if (alPlayers.get(i).getppGender().equalsIgnoreCase("Male")) {
                            Player player = new Player();
                            player.setId(alPlayers.get(i).getId());
                            player.setpName(alPlayers.get(i).getpName());
                            player.setppGender(alPlayers.get(i).getppGender());
                            player.setpYear(alPlayers.get(i).getpYear());
                            player.setpImag(alPlayers.get(i).getpImag());
                            alMen.add(player);
                        }
                    }

                }

                if (getIntent().getExtras().getString("type").equalsIgnoreCase("women")) {
                    if (alWomen.size() > 0) {
                        adapter = new PlayerAdapter(PlayersActivity.this, alWomen, realm);
                        rvPlayers.setAdapter(adapter);
                    } else {
                        showMyDialog("NOT FOUND", "No Player Found. Add new Player");
                    }
                } else {
                    if (alMen.size() > 0) {
                        adapter = new PlayerAdapter(PlayersActivity.this, alMen, realm);
                        rvPlayers.setAdapter(adapter);
                    } else {
                        showMyDialog("NOT FOUND", "No Player Found. Add new Player");
                    }
                }
            } else {
                //Ask user to add player

                showMyDialog("NOT FOUND", "No Player Found. Add new Player");
            }
        } catch (Exception e) {

        }
        /*for(Player player : results){
            text.append(player.getpName() + " " + player.getpScore() + "\n");
        }*/
    }

    private void showMyDialog(String title, String msg) {
        new LovelyStandardDialog(this, LovelyStandardDialog.ButtonLayout.VERTICAL)
                .setTopColorRes(R.color.dialog)
                .setButtonsColorRes(R.color.black)
                .setIcon(R.drawable.info)
                .setTitle(title)
                .setMessage(msg)
                .setPositiveButton(android.R.string.ok, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent i = new Intent(PlayersActivity.this, AddPlayerActivity.class);
                        startActivityForResult(i, 100);
                        // Toast.makeText(PlayersActivity.this, "positive clicked", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton(android.R.string.no, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        finish();
                    }
                })
                .show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == Activity.RESULT_OK) {
            Snackbar.make(rvPlayers, "Added", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();

        }
    }
}
