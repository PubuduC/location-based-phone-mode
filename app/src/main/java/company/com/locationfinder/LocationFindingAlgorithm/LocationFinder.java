package company.com.locationfinder.LocationFindingAlgorithm;

import java.text.DecimalFormat;
import java.util.HashMap;

public class LocationFinder {


    private static double angle;


    /**
     *
     * @param beacon_1_position
     * @param beacon_2_position
     * @param beacon_3_position
     * @param r1_fromB1
     * @param r2_fromB2
     * @param r3_fromB3
     * @return estimated location of the point
     */
    public static Coordinate2D getLocation(Coordinate2D beacon_1_position, Coordinate2D beacon_2_position, Coordinate2D beacon_3_position,
                                           double r1_fromB1,double r2_fromB2,double r3_fromB3){

        HashMap<String,Coordinate2D> shiftedAndRotatedBeacons=shiftAndRotateForCondition1(beacon_1_position,beacon_2_position,beacon_3_position);

        Coordinate2D relativeLocation = getRelativeLocation(shiftedAndRotatedBeacons, r1_fromB1, r2_fromB2, r3_fromB3);

        Coordinate2D conditionOneResolved = resolveCondition1(relativeLocation, beacon_1_position.getX(), beacon_1_position.getY(), angle);

//        Coordinate2D location  = Util2D.rotateAntiClockwiseXY(conditionOneResolved,angle);

        Coordinate2D roundedLocation=new Coordinate2D(Util2D.round3deci(conditionOneResolved.getX()),Util2D.round3deci(conditionOneResolved.getY()));

        return roundedLocation;

    }


    /**
     * getting transformed cartesian values for beacon positions to satisfy
     * condition1
     *
     * Condition 1
     * : get beacon_1 to (0,0) and beacon_2 to (k,0)
     */
    private static HashMap<String,Coordinate2D> shiftAndRotateForCondition1(Coordinate2D beacon_1_real,
                                                                            Coordinate2D beacon_2_real,
                                                                            Coordinate2D beacon_3_real){

        //nothing to calculate for shifting or rotating Beacon_1 goes to (0,0)
        Coordinate2D shiftedAndRotatedB_1=new Coordinate2D(0,0);

        //shifting//
        Coordinate2D shiftedB_2=Util2D.shiftXY_intoOrigin(beacon_2_real,beacon_1_real.getX(),beacon_1_real.getY());
        Coordinate2D shiftedB_3=Util2D.shiftXY_intoOrigin(beacon_3_real,beacon_1_real.getX(),beacon_1_real.getY());

        //angle of the rotation
        angle=Math.atan(shiftedB_2.getY()/shiftedB_2.getX());

        //rotating//
        //in Beacon_2 calculate only rotated x, y goes to 0
        Coordinate2D shiftedAndRotatedB_2=new Coordinate2D((float)Util2D.rotateClockwiseX(shiftedB_2,angle),0);
        Coordinate2D shiftedAndRotatedB_3=Util2D.rotateClockwiseXY(shiftedB_3,angle);

        HashMap<String, Coordinate2D> shiftedAndRotatedBeaconCoordinates = new HashMap<>();
        shiftedAndRotatedBeaconCoordinates.put("beacon_1",shiftedAndRotatedB_1);
        shiftedAndRotatedBeaconCoordinates.put("beacon_2",shiftedAndRotatedB_2);
        shiftedAndRotatedBeaconCoordinates.put("beacon_3",shiftedAndRotatedB_3);

        return shiftedAndRotatedBeaconCoordinates;

    }


    /**
     * getting location according to transformed cartesian plane
     */
    private static Coordinate2D getRelativeLocation(HashMap<String, Coordinate2D> beaconCoordinatesAfterCondition1,
                                                    double distanceToLocationFromBeacon1,
                                                    double distanceToLocationFromBeacon2,
                                                    double distanceToLocationFromBeacon3){

        Coordinate2D b1 = beaconCoordinatesAfterCondition1.get("beacon_1");
        Coordinate2D b2 = beaconCoordinatesAfterCondition1.get("beacon_2");
        Coordinate2D b3 = beaconCoordinatesAfterCondition1.get("beacon_3");

        double r1 = distanceToLocationFromBeacon1;
        double r2 = distanceToLocationFromBeacon2;
        double r3 = distanceToLocationFromBeacon3;

        double b1b2 = Util2D.getDistance(b1,b2);
        double b2b3 = Util2D.getDistance(b2,b3);

        // P is the point we are looking P = (x_,y_)

        //  x_= (r1**2 - r2**2 + distanceBetweenB1B2**2)/2*distanceBetweenB1B2
        //  y_= +-[sqrt(r1**2 -x_**2)]

        double x_ = ( Math.pow(r1,2) - Math.pow(r2,2) + Math.pow(b1b2,2) ) / (2 * b1b2);

        double y_plus = Math.sqrt ( Math.pow(r1,2) - Math.pow(x_,2));
        double y_minus = -Math.sqrt ( Math.pow(r1,2) - Math.pow(x_,2));


        //////////////////////////////////////////////////
        // * need to select y_ from y_plus and y_plus * //
        ///////////////////////////company/com/locationfinder/LocationFindingAlgorithm/LocationFinder.java:13///////////////////////

        // P is the point we are looking P = (x_,y_)
        // point N is the crossing point on b2b3 by the perpendicular line from P
        // g = b2N = distance to Point N from b2
        // f = PN

        double g = ( Math.pow(r2,2) - Math.pow(r3,2) + Math.pow(b2b3,2) )/ (2 * b2b3);
        double f =  Math.sqrt(Math.abs(Math.pow(r2,2) - Math.pow(g,2) ));

        //ratio on b2b3 by N
        double ratio=g/(b2b3-g);
        Coordinate2D N = Util2D.pointOnLineByRatio(b2,b3,ratio);

        Coordinate2D P_1=new Coordinate2D(x_,y_plus);
        Coordinate2D P_2= new Coordinate2D(x_,y_minus);

        /// as f=PN and now N has been found
        // lets get P_1=(x_,y_plus) and P_2=(x_,y_minus)
        // then P_1N == f or P_2N == f, because the values may not be exactly the same,
        // consider lowest difference

        double P_1N = Util2D.getDistance(P_1,N);
        double P_2N = Util2D.getDistance(P_2,N);

        double difference1=Math.abs(P_1N - f);
        double difference2=Math.abs(P_2N - f);

        Coordinate2D P=P_1;
        if (difference1>difference2){
            P=P_2;
        }
        return P;
    }


    /**
     * getting real location from transformed cartesian plane
     */
    private static Coordinate2D resolveCondition1(Coordinate2D point,double shiftingX,double shiftingY, double angle){
        Coordinate2D shiftedResolvedPoint = Util2D.shiftXY_outOfOrigin(point,shiftingX, shiftingY);
        Coordinate2D shiftedAndRotatedResolvedPoint=Util2D.rotateAntiClockwiseXY(shiftedResolvedPoint,angle);
        return shiftedAndRotatedResolvedPoint;
    }
}
