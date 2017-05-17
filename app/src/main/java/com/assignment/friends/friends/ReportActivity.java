package com.assignment.friends.friends;

import android.content.Context;
import android.graphics.Color;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.assignment.friends.friends.util.HttpGetAsyncTask;
import com.assignment.friends.friends.util.JsonHandler;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReportActivity extends AppCompatActivity {

    final int BAR_CHART = 1;
    final int PIE_CHART = 2;
    public String[] x1;
    public int[] y1;
    public String[] x2;
    public  int[] y2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        String path1 = "http://192.168.191.1:8080/FindFriends/webresources/location/locationfrenquency";
        GetFrequencyTask locationTask = new GetFrequencyTask(this,"init bar chart failed",path1,BAR_CHART);
        locationTask.execute();
        String path2 = "http://192.168.191.1:8080/FindFriends/webresources/profile/coursefrenquency";
        GetFrequencyTask courseTask = new GetFrequencyTask(this,"init pie chart failed",path2,PIE_CHART);
        courseTask.execute();
        initPie();
    }

    void initBar(){
        BarChart barchart = (BarChart)findViewById(R.id.mBarChart);

        XAxis xAxis = barchart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f); // only intervals of 1 day
        YAxis leftAxis = barchart.getAxisLeft();
        leftAxis.setLabelCount(8, false);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setSpaceTop(15f);
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)
        YAxis rightAxis = barchart.getAxisRight();
        rightAxis.setDrawGridLines(false);
        rightAxis.setLabelCount(8, false);
        rightAxis.setSpaceTop(15f);
        rightAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)
        Legend l = barchart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setForm(Legend.LegendForm.SQUARE);
        l.setFormSize(9f);
        l.setTextSize(11f);
        l.setXEntrySpace(4f);

        ArrayList<BarEntry> yVal = new ArrayList<BarEntry>();
        ArrayList<String> xVal = new ArrayList<>();
        ArrayList<IBarDataSet> thebardata = new ArrayList<>();
        for (int i = 0; i < y1.length; i++) {
            xVal.add(x1[i]);
            yVal.add(new BarEntry(i, y1[i]));
        }
        l.setExtra(ColorTemplate.VORDIPLOM_COLORS, x1);
        BarDataSet barDataSet = new BarDataSet(yVal,"");
        barDataSet.setColors(ColorTemplate.VORDIPLOM_COLORS);//colors
        thebardata.add(barDataSet);

        BarData bardata = new BarData(thebardata);
        barchart.setData(bardata);
        barchart.getLegend().setPosition(Legend.LegendPosition.ABOVE_CHART_LEFT);
        barchart.getLegend().setForm(Legend.LegendForm.CIRCLE);

        barchart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        barchart.getXAxis().setDrawGridLines(true);

        barchart.getAxisRight().setEnabled(false);
        //barchart.getAxisLeft().setAxisMinValue(0.0f);
        barchart.getAxisLeft().setDrawGridLines(true);

        barchart.animateXY(1000, 2000);
    }

    void initPie(){
        PieChart chart =(PieChart) findViewById(R.id.chart);
        List<PieEntry> entries = new ArrayList<PieEntry>();

        ArrayList<String> xVals = new ArrayList<String>();
        //for (int i=0; i<xAxis.length; i++){
        //    xVals.add(i, x[i]);
        //    entries.add(new PieEntry((float)(y[i]), xVals));
        //}
        entries.add(new PieEntry(18.5f, "FIT5183"));
        entries.add(new PieEntry(26.7f, "FIB5663"));
        entries.add(new PieEntry(24.0f, "FDS4562"));
        entries.add(new PieEntry(3.8f, "FIT5186"));

        ArrayList<Integer> colors = new ArrayList<Integer>();
        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);
        colors.add(ColorTemplate.getHoloBlue());


        PieDataSet dataSet = new PieDataSet(entries, "Pie chart");
        dataSet.setColors(colors);
        dataSet.setValueTextSize(11f);
        PieData lineData = new PieData(dataSet);
        chart.setData(lineData);
        chart.setHoleRadius(0f);
        chart.setEntryLabelColor(Color.WHITE);
        chart.setEntryLabelTextSize(12f);

        Legend l = chart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);
    }

    class GetFrequencyTask extends HttpGetAsyncTask<Void,Map>{

        private int type;

        public GetFrequencyTask(Context c, String msg, String path, int type) {
            super(c, msg, path);
            this.type = type;
        }

        @Override
        protected void httpPostExecute(Map frequency) throws Exception {
            if(type == BAR_CHART){
                initBar();
            }else if (type == PIE_CHART){
                initPie();
            }else{
                throw new Exception("Wrong Arguments");
            }
        }

        @Override
        protected Map<String, Integer> ownTask(InputStream result) throws Exception {
            String json = JsonHandler.readInputStream(result);
            JSONArray array = JSON.parseArray(json);
            Map<String, Integer> frequency = new HashMap<>();
            x1 = new String[array.size()];
            y1 = new int[array.size()];
            for(int i = 0; i < array.size(); i++){
                JSONObject temp = array.getJSONObject(i);
                frequency.put(temp.getString("name"),temp.getIntValue("count"));
                x1[i] = temp.getString("name");
                y1[i] = temp.getIntValue("count");
            }
            return frequency;
        }
    }

}
