package com.oscode.gatherthis;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends Fragment {

    private static int p1lifetotal = 20;
    private static int p2lifetotal = 20;
    private static int poison1 = 0;
    private static int poison2 = 0;
    private static int storm1 = 0;
    private static int storm2 = 0;
    private static TextView player1life;
    private static TextView player2life;
    private static TextView poisoncounter1;
    private static TextView poisoncounter2;
    private static TextView stormcounter1;
    private static TextView stormcounter2;
    private ImageView stormicon1;
    private ImageView stormicon2;
    
//    private int p1poisoncounters = 0;
//    private int p2poisoncounters = 0;
    
    
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        player1life = (TextView) getView().findViewById(R.id.cardname);
        player2life = (TextView) getView().findViewById(R.id.manacost);
        poisoncounter1 = (TextView) getView().findViewById(R.id.poisoncounter1);
        poisoncounter2 = (TextView) getView().findViewById(R.id.poisoncounter2);
        stormcounter1 = (TextView) getView().findViewById(R.id.stormcounter1);
        stormcounter2 = (TextView) getView().findViewById(R.id.stormcounter2);
        stormicon1 = (ImageView) getView().findViewById(R.id.ImageView01);
        stormicon2 = (ImageView) getView().findViewById(R.id.ImageView02);
        player1life.requestFocus();
        
        player1life.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                player1life.setText(Integer.toString(--p1lifetotal));
            }
        });
        
        player2life.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                player2life.setText(Integer.toString(--p2lifetotal));
            }
        });
        
        player1life.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return false;
            }
        });
        
        player1life.setText(Integer.toString(p1lifetotal));
        player2life.setText(Integer.toString(p2lifetotal));
        poisoncounter1.setText(Integer.toString(poison1));
        poisoncounter2.setText(Integer.toString(poison2));
        stormcounter1.setText(Integer.toString(storm1));
        stormcounter2.setText(Integer.toString(storm2));
        
        Button p1m5 = (Button) getView().findViewById(R.id.advancedbutton);
        p1m5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                p1lifetotal = p1lifetotal - 5;
                player1life.setText(Integer.toString(p1lifetotal));
            }
        });
        
        Button p1m1 = (Button) getView().findViewById(R.id.advancedclear);
        p1m1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                p1lifetotal = p1lifetotal - 1;
                player1life.setText(Integer.toString(p1lifetotal));
            }
        });
        
        Button p1p1 = (Button) getView().findViewById(R.id.button3);
        p1p1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                p1lifetotal = p1lifetotal + 1;
                player1life.setText(Integer.toString(p1lifetotal));
            }
        });
        
        Button p1p5 = (Button) getView().findViewById(R.id.button4);
        p1p5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                p1lifetotal = p1lifetotal + 5;
                player1life.setText(Integer.toString(p1lifetotal));
            }
        });
        
        Button p2m5 = (Button) getView().findViewById(R.id.Button01);
        p2m5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                p2lifetotal = p2lifetotal - 5;
                player2life.setText(Integer.toString(p2lifetotal));
            }
        });
        
        Button p2m1 = (Button) getView().findViewById(R.id.Button02);
        p2m1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                p2lifetotal = p2lifetotal - 1;
                player2life.setText(Integer.toString(p2lifetotal));
            }
        });
        
        Button p2p1 = (Button) getView().findViewById(R.id.Button03);
        p2p1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                p2lifetotal = p2lifetotal + 1;
                player2life.setText(Integer.toString(p2lifetotal));
            }
        });
        
        Button p2p5 = (Button) getView().findViewById(R.id.Button04);
        p2p5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                p2lifetotal = p2lifetotal + 5;
                player2life.setText(Integer.toString(p2lifetotal));
            }
        });
        
        Button poison1minus = (Button) getView().findViewById(R.id.button5);
        Button poison1plus = (Button) getView().findViewById(R.id.Button05);
        Button poison2minus = (Button) getView().findViewById(R.id.Button11);
        Button poison2plus = (Button) getView().findViewById(R.id.Button08);
        Button storm1minus = (Button) getView().findViewById(R.id.Button07);
        Button storm1plus = (Button) getView().findViewById(R.id.Button06);
        Button storm2minus = (Button) getView().findViewById(R.id.Button10);
        Button storm2plus = (Button) getView().findViewById(R.id.Button09);
        
        poison1minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                poisoncounter1.setText(Integer.toString(--poison1));
            }
        });
        poison1plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                poisoncounter1.setText(Integer.toString(++poison1));
            }
        });
        poison2minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                poisoncounter2.setText(Integer.toString(--poison2));
            }
        });
        poison2plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                poisoncounter2.setText(Integer.toString(++poison2));
            }
        });
        storm1minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stormcounter1.setText(Integer.toString(--storm1));
            }
        });
        storm1plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stormcounter1.setText(Integer.toString(++storm1));
            }
        });
        storm2minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stormcounter2.setText(Integer.toString(--storm2));
            }
        });
        storm2plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stormcounter2.setText(Integer.toString(++storm2));
            }
        });
        
        stormicon1.setOnClickListener(new View.OnClickListener() {
            
            @Override
            public void onClick(View v) {
                storm1 = 0;
                stormcounter1.setText(Integer.toString(storm1));
            }
        });
        stormicon2.setOnClickListener(new View.OnClickListener() {
            
            @Override
            public void onClick(View v) {
                storm2 = 0;
                stormcounter2.setText(Integer.toString(storm2));
            }
        });
    }

    public static void reset() {
        p1lifetotal = 20;
        p2lifetotal = 20;
        poison1 = 0;
        poison2 = 0;
        storm1 = 0;
        storm2 = 0;
        player1life.setText(Integer.toString(p1lifetotal));
        player2life.setText(Integer.toString(p2lifetotal));
        poisoncounter1.setText(Integer.toString(poison1));
        poisoncounter2.setText(Integer.toString(poison2));
        stormcounter1.setText(Integer.toString(storm1));
        stormcounter2.setText(Integer.toString(storm2));
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_main, container, false);
    }

    

    

    

    
    
    

}