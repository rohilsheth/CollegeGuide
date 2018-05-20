package com.example.rohilsheth.collegeguide;

import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

/**
 * Created by rohilsheth on 4/27/18.
 */

public class HomeFragment extends Fragment {
    EditText satScore;
    EditText actScore;
    TextView SATtext, actText, RD,ED;
    String SAT, ACT;
    Boolean saved=false;
    Button save;
    String file = "info.json";
    String dataToWrite;
    String allInfo="";
    TextView satDisp;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View homeFragmentView = inflater.inflate(R.layout.fragment_home, container, false);
        satScore = homeFragmentView.findViewById(R.id.SATedit);
        actScore = homeFragmentView.findViewById(R.id.actEdit);
        SATtext = homeFragmentView.findViewById(R.id.textView7);
        actText = homeFragmentView.findViewById(R.id.textView8);
        RD = homeFragmentView.findViewById(R.id.textView9);
        ED = homeFragmentView.findViewById(R.id.textView10);
        save = homeFragmentView.findViewById(R.id.saveButton);
        Calendar EDstart_calendar = Calendar.getInstance();
        Calendar EDend_calendar = Calendar.getInstance();
        EDend_calendar.set(2018, 10, 1); // 10 = November, month start at 0 = January
        long EDstart_millis = EDstart_calendar.getTimeInMillis(); //get the start time in milliseconds
        long EDend_millis = EDend_calendar.getTimeInMillis(); //get the end time in milliseconds
        long EDtotal_millis = (EDend_millis - EDstart_millis); //total time in milliseconds

        //1000 = 1 second interval
        CountDownTimer EDcdt = new CountDownTimer(EDtotal_millis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long days = TimeUnit.MILLISECONDS.toDays(millisUntilFinished);
                ED.setText("ED Deadline is in "+days+" days"); //You can compute the millisUntilFinished on hours/minutes/seconds
            }
            @Override
            public void onFinish() {
                ED.setText("Finish!");
            }
        };
        EDcdt.start();

        Calendar RDstart_calendar = Calendar.getInstance();
        Calendar RDend_calendar = Calendar.getInstance();
        RDend_calendar.set(2019, 0, 1); // 10 = November, month start at 0 = January

        long RDstart_millis = RDstart_calendar.getTimeInMillis(); //get the start time in milliseconds
        long RDend_millis = RDend_calendar.getTimeInMillis(); //get the end time in milliseconds
        long RDtotal_millis = (RDend_millis - RDstart_millis); //total time in milliseconds

        //1000 = 1 second interval
        CountDownTimer RDcdt = new CountDownTimer(RDtotal_millis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long days = TimeUnit.MILLISECONDS.toDays(millisUntilFinished);
                RD.setText("RD Deadline is in "+days+" days"); //You can compute the millisUntilFinished on hours/minutes/seconds
            }
            @Override
            public void onFinish() {
                RD.setText("Finish!");
            }
        };
        RDcdt.start();
        satScore.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                SAT = charSequence.toString();

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        satScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                satScore.setText("");
            }
        });
        actScore.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                ACT = charSequence.toString();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        actScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                actScore.setText("");
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    OutputStreamWriter writer = new OutputStreamWriter(getContext().openFileOutput(file, Context.MODE_WORLD_WRITEABLE));
                    if(SAT!=null){
                        SATtext.setText(SAT);
                    }
                    if(ACT!=null) {
                        actText.setText(ACT);
                    }
                    dataToWrite = "{sat:"+SAT+",act:"+ACT+"}";
                    writer.write(dataToWrite);
                    writer.close();
                    saved=true;
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

            try {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(getContext().openFileInput(file)));
                String currentLine;
                currentLine = bufferedReader.readLine();
                while (currentLine!=null){
                    allInfo+=currentLine;
                    currentLine = bufferedReader.readLine();
                }
                Log.d("TAG",allInfo);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                JSONObject root = new JSONObject(allInfo);
                Log.d("TAG",root+"e");
                if(root.get("sat")!="null") {
                    SATtext.setText(root.get("sat") + "");
                }
                if(root.get("act")!="null") {
                    actText.setText(root.get("act") + "");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        return homeFragmentView;

    }
}
