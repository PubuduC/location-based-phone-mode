package company.com.locationfinder;

import org.junit.Test;

import java.text.DecimalFormat;
import java.util.ArrayList;

import company.com.locationfinder.LocationFindingAlgorithm.Coordinate2D;
import company.com.locationfinder.LocationFindingAlgorithm.LocationFinder;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

    @Test
    public void trilateration_algo_check(){

        ArrayList<double[]> dataSetList=new ArrayList<>();

        double[] dataset1=new double[]{0,0,7,4,2,10,7.21,3.6,4.47,4,6};
        dataSetList.add(dataset1);

        double[] dataset2=new double[]{8,9,20,11,15,20,9.21,7.81,4.12,14,16};
        dataSetList.add(dataset2);

        for (double[] dataset : dataSetList){

            Coordinate2D cord1=new Coordinate2D(dataset[0],dataset[1]);
            Coordinate2D cord2=new Coordinate2D(dataset[2],dataset[3]);
            Coordinate2D cord3=new Coordinate2D(dataset[4],dataset[5]);

            double dist1=dataset[6];
            double dist2=dataset[7];
            double dist3=dataset[8];

            Coordinate2D methodReturned=LocationFinder.getLocation(cord1,cord2,cord3,dist1,dist2,dist3);

            Coordinate2D expected=new Coordinate2D(dataset[9],dataset[10]);

            System.out.println("point1: "+cord1.toString()+"\tpoint2: "+cord2.toString()+"\tpoint3: "+cord3.toString());
            System.out.println("distance1: "+dist1+"\tdistance2: "+dist2+"\tdistance3: "+dist3);

            System.out.println("=> location: "+methodReturned.toString()+"[ expected: "+expected.toString()+"]\n");
        }
    }

}