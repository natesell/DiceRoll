package com.example.android.diceroll;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClickButtonForThrees(View view) {
        startActivity(new Intent(this, ThreesActivity.class));
    }

    public void onClickButtonForShipCaptainCrew(View view) {
        final Intent intent = new Intent(this, ShipCaptainCrewActivity.class);

        CharSequence colors[] = new CharSequence[] {"Myself", "Computer", "Friends"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Play against whom?");
        builder.setItems(colors, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // the user clicked on colors[which]
                switch (which) {
                    case 0:
                        intent.putExtra("playChoice", "myself");
                        startActivity(intent);
                        break;
                    case 1:
                        intent.putExtra("playChoice", "computer");
                        startActivity(intent);
                        break;
                    case 2:
                        intent.putExtra("playChoice", "friends");
                        startActivity(intent);
                        break;
                }

            }
        });
        builder.show();
    }

    public void onClickButtonForOneFourEighteen(View view) {
        startActivity(new Intent(this, OneFourEighteenActivity.class));
    }

    public void onClickButtonForFiveRoll(View view) {
        startActivity(new Intent(this, FiveRollActivity.class));
    }
}

