package com.example.rohilsheth.collegeguide;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;

/**
 * Created by rohilsheth on 4/27/18.
 */

public class SearchFragment extends Fragment {
    String apiReq;
    View searchFragmentView;
    Button searchButton;
    Button addButton;
    TextView schoolName;
    TextView admRateText;
    TextView SATtext;
    TextView costText;
    TextView earnText;
    TextView ACTtext;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View searchFragmentView = inflater.inflate(R.layout.fragment_search, container, false);
        searchButton = searchFragmentView.findViewById(R.id.ID_Search);
        addButton = searchFragmentView.findViewById(R.id.ID_Add);
        schoolName = searchFragmentView.findViewById(R.id.ID_SchoolName);
        admRateText = searchFragmentView.findViewById(R.id.ID_AdmissionRate);
        SATtext = searchFragmentView.findViewById(R.id.ID_SAT);
        costText = searchFragmentView.findViewById(R.id.ID_Cost);
        earnText = searchFragmentView.findViewById(R.id.ID_Earnings);
        ACTtext = searchFragmentView.findViewById(R.id.ID_ACT);
        AsyncThread thread = new AsyncThread();
        thread.execute();
        return searchFragmentView;
    }


    //https://api.data.gov/ed/collegescorecard/v1/schools?school.name=princeton&fields=school.name,id,2015.admissions.admission_rate.overall&api_key=9DCEOKfwyuInWXJ8GTLRrEiudCqu3uZjMEMzM4Vd


    public class AsyncThread extends AsyncTask<String, Void, Void> {
        String json;
        String apiReq;
        JSONObject root;

        @Override
        protected Void doInBackground(String... zipnum) {
            try {
                apiReq = "https://api.data.gov/ed/collegescorecard/v1/schools?school.name=princeton&fields=school.name,id,2015.admissions.admission_rate.overall,2015.admissions.act_scores.25th_percentile.cumulative,2015.admissions.act_scores.75th_percentile.cumulative,2015.admissions.sat_scores.25th_percentile.math,2015.admissions.sat_scores.75th_percentile.math,2015.admissions.sat_scores.25th_percentile.critical_reading,2015.admissions.sat_scores.75th_percentile.critical_reading&api_key=9DCEOKfwyuInWXJ8GTLRrEiudCqu3uZjMEMzM4Vd";
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
                String findName = (String) findschool.get("school.name");
                Double findadm = (Double) findschool.get("2015.admissions.admission_rate.overall");
                double lowSATCR = (double)findschool.get("2015.admissions.sat_scores.25th_percentile.critical_reading");
                double highSATCR = (double) findschool.get("2015.admissions.sat_scores.75th_percentile.critical_reading");
                double lowSATM = (double) findschool.get("2015.admissions.sat_scores.25th_percentile.math");
                double highSATM = (double) findschool.get("2015.admissions.sat_scores.75th_percentile.math");
                double lowACT = (double) findschool.get("2015.admissions.act_scores.25th_percentile.cumulative");
                double highACT = (double) findschool.get("2015.admissions.act_scores.75th_percentile.cumulative");
                SATtext.setText("SAT Score Range: "+(lowSATCR+lowSATM)+" to "+(highSATCR+highSATM));
                ACTtext.setText("ACT Score Range: "+lowACT+" to "+highACT);
                schoolName.setText(findName);
                admRateText.setText("Admissions Rate: "+(twoDForm.format(findadm*100))+"%");
                //Long epoch = Long.parseLong(findTime.get("dt")+"");

            } catch (JSONException e) {
                e.printStackTrace();
            }
            super.onPostExecute(aVoid);
        }
    }
}
