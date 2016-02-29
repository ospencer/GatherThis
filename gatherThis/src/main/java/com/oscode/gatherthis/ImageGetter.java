package com.oscode.gatherthis;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Html;

public class ImageGetter implements Html.ImageGetter {

        int dia = 100;
        Context c;
        
        public ImageGetter(int diameter, Context context) {
            dia = diameter;
            c = context;
        }
        
        public Drawable getDrawable(String source) {
            int id;

            if (source.equals("{U}")) {
                id = R.drawable.u;
            }
            else if (source.equals("{B}")) {
                id = R.drawable.b;
            }
            else if (source.equals("{W}")) {
                id = R.drawable.w;
            }
            else if (source.equals("{R}")) {
                id = R.drawable.r;
            }
            else if (source.equals("{G}")) {
                id = R.drawable.g;
            }
            else if (source.equals("{0}")) {
                id = R.drawable.c0;
            }
            else if (source.equals("{1}")) {
                id = R.drawable.c1;
            }
            else if (source.equals("{2}")) {
                id = R.drawable.c2;
            }
            else if (source.equals("{3}")) {
                id = R.drawable.c3;
            }
            else if (source.equals("{4}")) {
                id = R.drawable.c4;
            }
            else if (source.equals("{5}")) {
                id = R.drawable.c5;
            }
            else if (source.equals("{6}")) {
                id = R.drawable.c6;
            }
            else if (source.equals("{7}")) {
                id = R.drawable.c7;
            }
            else if (source.equals("{8}")) {
                id = R.drawable.c8;
            }
            else if (source.equals("{9}")) {
                id = R.drawable.c9;
            }
            else if (source.equals("{10}")) {
                id = R.drawable.c10;
            }
            else if (source.equals("{11}")) {
                id = R.drawable.c11;
            }
            else if (source.equals("{12}")) {
                id = R.drawable.c12;
            }
            else if (source.equals("{13}")) {
                id = R.drawable.c13;
            }
            else if (source.equals("{14}")) {
                id = R.drawable.c14;
            }
            else if (source.equals("{15}")) {
                id = R.drawable.c15;
            }
            else if (source.equals("{16}")) {
                id = R.drawable.c16;
            }
            else if (source.equals("{17}")) {
                id = R.drawable.c17;
            }
            else if (source.equals("{18}")) {
                id = R.drawable.c18;
            }
            else if (source.equals("{19}")) {
                id = R.drawable.c19;
            }
            else if (source.equals("{20}")) {
                id = R.drawable.c18;
            }
            else if (source.equals("{S}")) {
                id = R.drawable.s;
            }
            else if (source.equals("{X}")) {
                id = R.drawable.x;
            }
            else if (source.equals("{Y}")) {
                id = R.drawable.y;
            }
            else if (source.equals("{Z}")) {
                id = R.drawable.z;
            }
            else if (source.equals("{W/U}")) {
                id = R.drawable.wu;
            }
            else if (source.equals("{W/B}")) {
                id = R.drawable.wb;
            }
            else if (source.equals("{U/B}")) {
                id = R.drawable.ub;
            }
            else if (source.equals("{U/R}")) {
                id = R.drawable.ur;
            }
            else if (source.equals("{B/R}")) {
                id = R.drawable.br;
            }
            else if (source.equals("{B/G}")) {
                id = R.drawable.bg;
            }
            else if (source.equals("{R/G}")) {
                id = R.drawable.rg;
            }
            else if (source.equals("{R/W}")) {
                id = R.drawable.rw;
            }
            else if (source.equals("{G/W}")) {
                id = R.drawable.gw;
            }
            else if (source.equals("{G/U}")) {
                id = R.drawable.gu;
            }
            else if (source.equals("{2/U}")) {
                id = R.drawable.c2u;
            }
            else if (source.equals("{2/W}")) {
                id = R.drawable.c2w;
            }
            else if (source.equals("{2/B}")) {
                id = R.drawable.c2b;
            }
            else if (source.equals("{2/R}")) {
                id = R.drawable.c2r;
            }
            else if (source.equals("{2/G}")) {
                id = R.drawable.c2g;
            }
            else if (source.equals("{P}")) {
                id = R.drawable.p;
            }
            else if (source.equals("{P/W}")) {
                id = R.drawable.pw;
            }
            else if (source.equals("{P/B}")) {
                id = R.drawable.pb;
            }
            else if (source.equals("{P/U}")) {
                id = R.drawable.pu;
            }
            else if (source.equals("{P/R}")) {
                id = R.drawable.pr;
            }
            else if (source.equals("{P/G}")) {
                id = R.drawable.pg;
            }
            else if (source.equals("{T}")) {
                id = R.drawable.t;
            }
            else if (source.equals("{Q}")) {
                id = R.drawable.q;
            }
            else {
                return null;
            }

            Drawable d = c.getResources().getDrawable(id);
            d.setBounds(0,0,dia,dia);
            return d;
        }
    };