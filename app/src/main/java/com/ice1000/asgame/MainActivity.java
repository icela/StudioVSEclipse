package com.ice1000.asgame;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.lfk.justweengine.Utils.tools.SpUtils;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView bestText = (TextView) findViewById(R.id.bestScore);
        if (bestText != null) {
            bestText.append(String.valueOf((int) SpUtils.get(this, Game.BEST, 0)));
        }
    }

    public void startGame(View view) {
        startActivity(new Intent(this, Game.class));
    }
}
