package com.inu555.smartbusbell_networking;

public class BusGPSInform{

    // 버스 차량번호(예:"인천11가2222")
    private String busNumPlate;
    // 버스 노선번호(예:"780-1번")
    private String routeNum;

    // 버스 현재 위도
    private double lat;
    // 버스 현재 경도
    private double lon;

    public BusGPSInform(){}

    public BusGPSInform(String busNumPlate, String routeNum, double lat, double lon) {
        this.busNumPlate = busNumPlate;
        this.routeNum = routeNum;
        this.lat = lat;
        this.lon = lon;
    }

    public String getBusNumPlate() {
        return busNumPlate;
    }

    public void setBusNumPlate(String busNumPlate) {
        this.busNumPlate = busNumPlate;
    }

    public String getRouteNum() {
        return routeNum;
    }

    public void setRouteNum(String routeNum) {
        this.routeNum = routeNum;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }
}
