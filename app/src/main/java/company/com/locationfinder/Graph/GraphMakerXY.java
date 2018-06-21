package company.com.locationfinder.Graph;

import android.graphics.Color;
import android.view.View;

import com.github.mikephil.charting.charts.ScatterChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.ScatterData;
import com.github.mikephil.charting.data.ScatterDataSet;

import java.util.ArrayList;
import java.util.List;

import company.com.locationfinder.LocationFindingAlgorithm.Coordinate2D;


public class GraphMakerXY {

    static ScatterChart chart;
    static ScatterData scatterData;

    static ScatterDataSet beacon_1_;
    static ScatterDataSet beacon_2_;
    static ScatterDataSet beacon_3_;
    static ScatterDataSet point_;

    public static void drawGraph(View viewById, Coordinate2D beacon_1, Coordinate2D beacon_2, Coordinate2D beacon_3){

        chart = (ScatterChart) viewById;
        setGraphProperties();



//        chart.setDescription(descriptionX);
//        chart.setDescription(descriptionY);

        final List<Entry> beacon_1Entry = new ArrayList<Entry>();
        beacon_1Entry.add(new Entry(beacon_1.getX(), beacon_1.getY()));

        beacon_1_ = new ScatterDataSet(beacon_1Entry, "Beacon_1");
        beacon_1_.setColor(Color.BLUE);
        beacon_1_.setValueTextColor(Color.BLACK);
        beacon_1_.setScatterShape(ScatterChart.ScatterShape.CIRCLE);
        beacon_1_.setScatterShapeSize(50);
        beacon_1_.setScatterShapeHoleRadius(5);
        beacon_1_.setDrawValues(false);

        final List<Entry> beacon_2Entry = new ArrayList<Entry>();
        beacon_2Entry.add(new Entry(beacon_2.getX(), beacon_2.getY()));

        beacon_2_ = new ScatterDataSet(beacon_2Entry, "Beacon_2");
        beacon_2_.setColor(Color.GREEN);
        beacon_2_.setValueTextColor(Color.BLACK);
        beacon_2_.setScatterShape(ScatterChart.ScatterShape.CIRCLE);
        beacon_2_.setScatterShapeSize(50);
        beacon_2_.setScatterShapeHoleRadius(5);
        beacon_2_.setDrawValues(false);

        final List<Entry> beacon_3Entry = new ArrayList<Entry>();
        beacon_3Entry.add(new Entry(beacon_3.getX(), beacon_3.getY()));

        beacon_3_ = new ScatterDataSet(beacon_3Entry, "Beacon_3");
        beacon_3_.setColor(Color.BLACK);
        beacon_3_.setValueTextColor(Color.BLACK);
        beacon_3_.setScatterShape(ScatterChart.ScatterShape.CIRCLE);
        beacon_3_.setScatterShapeSize(50);
        beacon_3_.setScatterShapeHoleRadius(5);
        beacon_3_.setDrawValues(false);
        beacon_3_.setDrawIcons(true);


        ScatterDataSet[] dataSets={beacon_1_,beacon_2_,beacon_3_};
        addToGraph(dataSets);
    }

    private static void setGraphProperties(){

        Description description = new Description();
        description.setText("");

        chart.setDescription(description);

        chart.getAxisRight().setEnabled(false);
        chart.setDrawGridBackground(false);

        chart.getAxisLeft().setDrawGridLines(false);

        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        chart.setDrawBorders(true);

        chart.getLegend().setForm(Legend.LegendForm.CIRCLE);
        chart.getLegend().setXEntrySpace(30);

//        chart.setAutoScaleMinMaxEnabled(false);

    }

    public static void addPoint(Coordinate2D point) {
        final List<Entry> point_Entry = new ArrayList<Entry>();
        point_Entry.add(new Entry(point.getX(), point.getY()));
        point_ = new ScatterDataSet(point_Entry, "Point");
        point_.setColor(Color.RED);
        point_.setValueTextColor(Color.BLACK);
        point_.setScatterShape(ScatterChart.ScatterShape.CIRCLE);
        point_.setScatterShapeSize(70);
        point_.setDrawValues(false);

        ScatterDataSet[] dataSets={beacon_1_,beacon_2_,beacon_3_,point_};
        addToGraph(dataSets);

    }

    private static void addToGraph(ScatterDataSet[] dataSets){
        scatterData= new ScatterData(dataSets);
        chart.setData(scatterData);
        chart.invalidate();
    }

    public static void updateGraph() {
        chart.invalidate();
    }

    public static ScatterChart getChart() {
        return chart;
    }

    public static ScatterData getScatterData(){
        return scatterData;
    }
}
