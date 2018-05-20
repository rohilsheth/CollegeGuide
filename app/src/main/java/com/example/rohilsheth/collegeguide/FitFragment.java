package com.example.rohilsheth.collegeguide;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by rohilsheth on 4/27/18.
 */

public class FitFragment extends Fragment {
    HorizontalBarChart barChart;
    ArrayList<Schools> colleges;
    RadioGroup radioGroup;
    String dataType;
    Spinner collegeList;
    ArrayList<String> names;
    String file = "info.json";
    String allInfo="";
    int chosen;
    int score;
    TextView chance;
    public void updateArray(ArrayList<Schools> arrayList){
        colleges = arrayList;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fitFragmentView = inflater.inflate(R.layout.fragment_fit, container, false);
        barChart = fitFragmentView.findViewById(R.id.chart);
        radioGroup = fitFragmentView.findViewById(R.id.radioGroup1);
        collegeList = fitFragmentView.findViewById(R.id.spinner);
        chance = fitFragmentView.findViewById(R.id.textView12);
        names = new ArrayList<>();
        if(colleges!=null){
            for(int j=0;j<colleges.size();j++){
                names.add(colleges.get(j).getName());
            }
        }
        dataType="SAT";
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_dropdown_item,names);
        collegeList.setAdapter(adapter);
        collegeList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                chosen=i;
                BarData data = new BarData(getDataSet());
                barChart.setData(data);
                barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(getXAxisValues()));
                data.setBarWidth(0.7f);
                barChart.setFitBars(true);
                barChart.animateXY(1000,1000);
                barChart.invalidate();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if(i==R.id.radioButton3){
                    dataType="SAT";
                    BarData data = new BarData(getDataSet());
                    barChart.setData(data);
                    barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(getXAxisValues()));
                    data.setBarWidth(0.7f);
                    barChart.setFitBars(true);
                    barChart.animateXY(1000,1000);
                    barChart.invalidate();
                }
                if(i==R.id.radioButton4){
                    dataType="ACT";
                    BarData data = new BarData(getDataSet());
                    barChart.setData(data);
                    barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(getXAxisValues()));
                    data.setBarWidth(0.7f);
                    barChart.setFitBars(true);
                    barChart.animateXY(1000,1000);
                    barChart.invalidate();
                }
            }
        });
        BarData data = new BarData(getDataSet());
        barChart.setData(data);
        barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(getXAxisValues()));
        data.setBarWidth(0.7f);
        barChart.setFitBars(true);
        barChart.animateXY(1000,1000);
        barChart.invalidate();
        return fitFragmentView;
    }
    private BarDataSet getDataSet() {
        ArrayList<BarDataSet> dataSets = null;
        ArrayList<BarEntry> valueSet1 = new ArrayList<>();
        if (dataType=="SAT"){

            BarEntry low = new BarEntry(0, (int)colleges.get(chosen).getSATScorelow());
            valueSet1.add(low);
            BarEntry high = new BarEntry(1, (int)colleges.get(chosen).getSATScorehigh());
            valueSet1.add(high);
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
                    score = (int) root.get("sat");
                    if(score>(int)colleges.get(chosen).getSATScorehigh()){
                        chance.setText("Great");
                        chance.setTextColor(Color.GREEN);
                    }
                    if ((score>(int)colleges.get(chosen).getSATScorelow())&&(score<(int)colleges.get(chosen).getSATScorehigh())){
                        chance.setText("Medium");
                        chance.setTextColor(Color.YELLOW);
                    }
                    if(score<(int)colleges.get(chosen).getSATScorelow()){
                        chance.setText("Low");
                        chance.setTextColor(Color.RED);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            BarEntry user = new BarEntry(2, score);
            valueSet1.add(user);
            barChart.invalidate();

        }
        if (dataType=="ACT"){
            BarEntry low = new BarEntry(0, (int)colleges.get(chosen).getACTScorelow());
            valueSet1.add(low);
            BarEntry high = new BarEntry(1, (int)colleges.get(chosen).getACTScorehigh());
            valueSet1.add(high);
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
                if(root.get("act")!="null") {
                    score = (int) root.get("act");
                    if(score>(int)colleges.get(chosen).getACTScorehigh()){
                        chance.setText("Great");
                        chance.setTextColor(Color.GREEN);
                    }
                    if ((score>(int)colleges.get(chosen).getACTScorelow())&&(score<(int)colleges.get(chosen).getACTScorehigh())){
                        chance.setText("Medium");
                        chance.setTextColor(Color.YELLOW);
                    }
                    if(score<(int)colleges.get(chosen).getACTScorelow()){
                        chance.setText("Low");
                        chance.setTextColor(Color.RED);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            BarEntry user = new BarEntry(2, score);
            valueSet1.add(user);
            barChart.invalidate();
        }
        BarDataSet barDataSet = new BarDataSet(valueSet1,"Scores");
        barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        dataSets = new ArrayList<>();
        dataSets.add(barDataSet);
        return barDataSet;
    }
    private ArrayList<String> getXAxisValues(){
        ArrayList<String> xAxis = new ArrayList<>();
        xAxis.add("25 Percentile");
        xAxis.add("75 Percentile");
        xAxis.add("Your Score");
        return xAxis;
    }
}
