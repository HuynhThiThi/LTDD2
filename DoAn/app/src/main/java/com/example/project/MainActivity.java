package com.example.project;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.provider.Settings;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import com.example.project.Adapter.GripViewAnswerAdapter;
import com.example.project.Adapter.GripViewSuggestAdapter;
import com.example.project.Common.Common;

public class MainActivity extends AppCompatActivity {

    static  Intent intent;
    public List<String> suggestSource = new ArrayList<>();

    public GripViewAnswerAdapter answerAdapter;
    public GripViewSuggestAdapter suggestAdapter;

    public Button btnSubmit, btnTranfer;

    public GridView gripViewAnswer, gripViewSuggest;

    public ImageView imgViewQuestion;

    int[] image_list = {
            R.drawable.android,
            R.drawable.facebook,
            R.drawable.instagram,
            R.drawable.snapchat,
            R.drawable.twitter,
            R.drawable.youtube,
    };

    public char[] answer;

    String correct_answer;

    MediaPlayer mp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btnTranfer = (Button) findViewById(R.id.btnTranfer);

        //set action bar
        ActionBar actionBar = getSupportActionBar();
        Drawable drawable = getResources().getDrawable(R.drawable.ic_music_note_black_24dp);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(drawable);

        //Init View
        initView();

        btnTranfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(MainActivity.this , PointActivity.class);
                intent.setFlags(intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
            }
        });
    }

    //create menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    //menu item selected
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        switch (item.getItemId()) {
            case android.R.id.home:
                if(mp == null){
                    mp = MediaPlayer.create(getApplicationContext(), R.raw.fantacy);
                    mp.start();
                    Toast.makeText(MainActivity.this, "Music is playing now", Toast.LENGTH_SHORT).show();
                }else {
                    mp.stop();
                    mp = null;
                    Toast.makeText(MainActivity.this, "Music is stop", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.action_close:
                finish();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    private void initView() {
        gripViewAnswer = (GridView) findViewById(R.id.gripViewAnswer);
        gripViewSuggest = (GridView) findViewById(R.id.gripViewSuggest);

        imgViewQuestion = (ImageView) findViewById(R.id.imgLogo);

        //Add setup here
        setupList();

        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String result = "";
                for (int i = 0; i < Common.user_submit_answer.length; i++) {
                    result += String.valueOf(Common.user_submit_answer[i]);

                    if (result.equals(correct_answer)) {
                        Toast.makeText(getApplicationContext(), "Finish ! This is " + result, Toast.LENGTH_SHORT).show();

                        //Reset
                        Common.count = 0;
                        Common.user_submit_answer = new char[correct_answer.length()];

                        //set Adapter
                        GripViewAnswerAdapter answerAdapter = new GripViewAnswerAdapter(setupNullList(), getApplicationContext());
                        gripViewAnswer.setAdapter(answerAdapter);
                        answerAdapter.notifyDataSetChanged();

                        GripViewSuggestAdapter suggestAdapter = new GripViewSuggestAdapter(suggestSource, getApplicationContext(), MainActivity.this);
                        gripViewSuggest.setAdapter(suggestAdapter);
                        suggestAdapter.notifyDataSetChanged();

                        setupList();
                    } else {
                        Toast.makeText(MainActivity.this, "Incorrect!!!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }

    private void setupList() {
        //random Logo
        Random random = new Random();
        int imageSelected = image_list[random.nextInt(image_list.length)];
        imgViewQuestion.setImageResource(imageSelected);

        correct_answer = getResources().getResourceName(imageSelected);
        correct_answer = correct_answer.substring(correct_answer.lastIndexOf("/") + 1);

        answer = correct_answer.toCharArray();

        Common.user_submit_answer = new char[answer.length];

        //Add answer character to list
        suggestSource.clear();
        for (char item : answer) {
            //Add logo name to list
            suggestSource.add(String.valueOf(item));
        }

        //Random add some character to list
        for (int i = answer.length; i < answer.length * 2; i++) {
            suggestSource.add(Common.alphabet_character[random.nextInt(Common.alphabet_character.length)]);
        }

        //sort random
        Collections.shuffle(suggestSource);

        //set for GripView
        answerAdapter = new GripViewAnswerAdapter(setupNullList(), this);
        suggestAdapter = new GripViewSuggestAdapter(suggestSource, this, this);

        answerAdapter.notifyDataSetChanged();
        suggestAdapter.notifyDataSetChanged();

        gripViewSuggest.setAdapter(suggestAdapter);
        gripViewAnswer.setAdapter(answerAdapter);

    }

    private char[] setupNullList() {
        char result[] = new char[answer.length];
        for (int i = 0; i < answer.length; i++) {
            result[i] = ' ';
        }
        return result;
    }
}
