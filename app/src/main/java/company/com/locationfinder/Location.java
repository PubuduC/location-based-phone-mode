package company.com.locationfinder;

public class Location {

    private int id;
    private String place;

    private String beacon1;
    private double beacon1x;
    private double beacon1y;

    private String beacon2;
    private double beacon2x;
    private double beacon2y;

    private String beacon3;
    private double beacon3x;
    private double beacon3y;

    private String mode;

    public Location(){

    }
    public Location(int id, String place, String beacon1, double beacon1x, double beacon1y, String beacon2, double beacon2x, double beacon2y, String beacon3, double beacon3x, double beacon3y, String mode) {
        this.id = id;
        this.place = place;
        this.beacon1 = beacon1;
        this.beacon1x = beacon1x;
        this.beacon1y = beacon1y;
        this.beacon2 = beacon2;
        this.beacon2x = beacon2x;
        this.beacon2y = beacon2y;
        this.beacon3 = beacon3;
        this.beacon3x = beacon3x;
        this.beacon3y = beacon3y;
        this.mode = mode;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getBeacon1() {
        return beacon1;
    }

    public void setBeacon1(String beacon1) {
        this.beacon1 = beacon1;
    }

    public double getBeacon1x() {
        return beacon1x;
    }

    public void setBeacon1x(double beacon1x) {
        this.beacon1x = beacon1x;
    }

    public double getBeacon1y() {
        return beacon1y;
    }

    public void setBeacon1y(double beacon1y) {
        this.beacon1y = beacon1y;
    }

    public String getBeacon2() {
        return beacon2;
    }

    public void setBeacon2(String beacon2) {
        this.beacon2 = beacon2;
    }

    public double getBeacon2x() {
        return beacon2x;
    }

    public void setBeacon2x(double beacon2x) {
        this.beacon2x = beacon2x;
    }

    public double getBeacon2y() {
        return beacon2y;
    }

    public void setBeacon2y(double beacon2y) {
        this.beacon2y = beacon2y;
    }

    public String getBeacon3() {
        return beacon3;
    }

    public void setBeacon3(String beacon3) {
        this.beacon3 = beacon3;
    }

    public double getBeacon3x() {
        return beacon3x;
    }

    public void setBeacon3x(double beacon3x) {
        this.beacon3x = beacon3x;
    }

    public double getBeacon3y() {
        return beacon3y;
    }

    public void setBeacon3y(double beacon3y) {
        this.beacon3y = beacon3y;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    @Override
    public String toString() {
        return "Location{" +
                "id=" + id +
                ", place='" + place + '\'' +
                ", beacon1='" + beacon1 + '\'' +
                ", beacon1x=" + beacon1x +
                ", beacon1y=" + beacon1y +
                ", beacon2='" + beacon2 + '\'' +
                ", beacon2x=" + beacon2x +
                ", beacon2y=" + beacon2y +
                ", beacon3='" + beacon3 + '\'' +
                ", beacon3x=" + beacon3x +
                ", beacon3y=" + beacon3y +
                ", mode='" + mode + '\'' +
                '}';
    }
}
