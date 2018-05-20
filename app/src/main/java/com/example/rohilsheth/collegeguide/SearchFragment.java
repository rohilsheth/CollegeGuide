package com.example.rohilsheth.collegeguide;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by rohilsheth on 4/27/18.
 */

public class SearchFragment extends Fragment {
    Button searchButton;
    Button addButton;
    TextView schoolName;
    TextView admRateText;
    TextView SATtext;
    TextView costText;
    TextView debtText;
    TextView ACTtext;
    EditText searchEdit;
    String input;
    String json;
    String apiReq;
    JSONObject root;
    Boolean valid;
    ArrayList<Schools> colleges;
    Schools newSchool;
    String file="schools.json";
    String dataToWrite;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View searchFragmentView = inflater.inflate(R.layout.fragment_search, container, false);
        colleges = new ArrayList<>();
        searchButton = searchFragmentView.findViewById(R.id.ID_Search);
        addButton = searchFragmentView.findViewById(R.id.ID_Add);
        schoolName = searchFragmentView.findViewById(R.id.ID_SchoolName);
        admRateText = searchFragmentView.findViewById(R.id.ID_AdmissionRate);
        SATtext = searchFragmentView.findViewById(R.id.ID_SAT);
        costText = searchFragmentView.findViewById(R.id.ID_Cost);
        debtText = searchFragmentView.findViewById(R.id.ID_Debt);
        ACTtext = searchFragmentView.findViewById(R.id.ID_ACT);
        searchEdit = searchFragmentView.findViewById(R.id.editText);
        searchEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchEdit.setText("");
            }
        });
        searchEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                input = charSequence.toString();

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AsyncThread thread = new AsyncThread();
                thread.execute(input);
            }
        });
        return searchFragmentView;

    }


    //https://api.data.gov/ed/collegescorecard/v1/schools?school.name=princeton&fields=school.name,id,2015.admissions.admission_rate.overall&api_key=9DCEOKfwyuInWXJ8GTLRrEiudCqu3uZjMEMzM4Vd


    public class AsyncThread extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... input) {
            String tempInput = input[0];
            try {
                apiReq = "https://api.data.gov/ed/collegescorecard/v1/schools?school.name="+tempInput+"&fields=school.name,id,2015.admissions.admission_rate.overall,2015.admissions.act_scores.25th_percentile.cumulative,2015.admissions.act_scores.75th_percentile.cumulative,2015.admissions.sat_scores.25th_percentile.math,2015.admissions.sat_scores.75th_percentile.math,2015.admissions.sat_scores.25th_percentile.critical_reading,2015.admissions.sat_scores.75th_percentile.critical_reading,2015.cost.attendance.academic_year,2015.aid.median_debt.completers.overall&api_key=9DCEOKfwyuInWXJ8GTLRrEiudCqu3uZjMEMzM4Vd";
                //apiReq = "https://api.data.gov/ed/collegescorecard/v1/schools?school.name="+tempInput+"&fields=school.name,id,2015.admissions.admission_rate.overall,2015.admissions.act_scores.25th_percentile.cumulative,2015.admissions.act_scores.75th_percentile.cumulative,2015.admissions.sat_scores.25th_percentile.math,2015.admissions.sat_scores.75th_percentile.math,2015.admissions.sat_scores.25th_percentile.critical_reading,2015.admissions.sat_scores.75th_percentile.critical_reading,2015.cost.attendance.academic_year,2015.aid.median_debt.completers.overall&api_key=9DCEOKfwyuInWXJ8GTLRrEiudCqu3uZjMEMzM4Vd";
                URL url = new URL(apiReq);
                URLConnection urlConnection = url.openConnection();
                InputStream stream = urlConnection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
                String line;
                json = "";
                while ((line = reader.readLine()) != null) {
                    json += line;
                }
                Log.d("TAG", json);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            try {
                DecimalFormat twoDForm = new DecimalFormat(".##");

                root = new JSONObject(json);
                Log.d("TAG", root.toString());
                JSONArray findResults = root.getJSONArray("results");
                JSONObject findschool = findResults.getJSONObject(0);
                final String findName = (String) findschool.get("school.name");
                Double findadm = (Double) findschool.get("2015.admissions.admission_rate.overall");
                double lowSATCR = (double)findschool.get("2015.admissions.sat_scores.25th_percentile.critical_reading");
                double highSATCR = (double) findschool.get("2015.admissions.sat_scores.75th_percentile.critical_reading");
                double lowSATM = (double) findschool.get("2015.admissions.sat_scores.25th_percentile.math");
                double highSATM = (double) findschool.get("2015.admissions.sat_scores.75th_percentile.math");
                int cost = (int) findschool.get("2015.cost.attendance.academic_year");
                double debt = (double) findschool.get("2015.aid.median_debt.completers.overall");
                newSchool = new Schools(findName,lowSATCR+lowSATM,highSATCR+highSATM,0.0,0.0);
                try {
                    valid=true;
                    double lowACT = (double) findschool.get("2015.admissions.act_scores.25th_percentile.cumulative");
                    double highACT = (double) findschool.get("2015.admissions.act_scores.75th_percentile.cumulative");
                }catch (ClassCastException e){
                    valid=false;
                    ACTtext.setText("ACT Range: N/A");
                }
                if(valid){
                    double lowACT = (double) findschool.get("2015.admissions.act_scores.25th_percentile.cumulative");
                    double highACT = (double) findschool.get("2015.admissions.act_scores.75th_percentile.cumulative");
                    newSchool.setACTScorehigh(highACT);
                    newSchool.setACTScorelow(lowACT);
                    ACTtext.setText("ACT Range: "+lowACT+" to "+highACT);
                }
                SATtext.setText("SAT Range: "+(lowSATCR+lowSATM)+" to "+(highSATCR+highSATM));
                schoolName.setText(findName);
                costText.setText("Cost of Attendance: $"+cost);
                debtText.setText("Average Debt: $"+debt);
                admRateText.setText("Admissions Rate: "+(twoDForm.format(findadm*100))+"%");
                addButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        colleges.add(newSchool);
                        Log.d("TAG",newSchool+"");
                        Log.d("Tag",colleges+"");
                        Log.d("TAg",colleges.get(0).getName());
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("key",colleges);
                        arraySend();
                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            } //catch (ClassCastException e){
              //  Toast.makeText(getActivity(), "Please clarify your choice", Toast.LENGTH_SHORT).show();
            //}
            super.onPostExecute(aVoid);
        }
    }
    addClicked mCallback;

    public interface addClicked{
        public void sendArray(ArrayList<Schools> schools);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (addClicked) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement TextClicked");
        }
    }

    public void arraySend(){
        Log.d("TAG",colleges+"");
        mCallback.sendArray(colleges);
    }

    @Override
    public void onDetach() {
        mCallback = null; // => avoid leaking, thanks @Deepscorn
        super.onDetach();
    }
}
