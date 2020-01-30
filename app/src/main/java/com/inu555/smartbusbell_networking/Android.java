package com.inu555.smartbusbell_networking;

// 안드로이드 객체
public class Android {

    // 안드로이드 식별자 (예:"android123")
    private String identifier;
    // 버스 차량 번호 (예:"인천74사1071")
    private String busNumPlate;
    // 버스 노선번호(예:"780-1번")
    private String routeNum;
    // 예약할 정류장 고유번호(예:"38-034")
    private String stopToBook;

    public Android() {}

    public Android(String identifier, String busNumPlate, String routeNum, String stopToBook) {
        this.identifier = identifier;
        this.busNumPlate = busNumPlate;
        this.routeNum = routeNum;
        this.stopToBook = stopToBook;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
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

    public String getStopToBook() {
        return stopToBook;
    }

    public void setStopToBook(String stopToBook) {
        this.stopToBook = stopToBook;
    }
}