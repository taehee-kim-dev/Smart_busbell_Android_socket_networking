package com.inu555.smartbusbell_networking;

// 버스 탑승 확정 객체
public class ConfirmBus {
    // 버스 차량 번호 (예:"인천74사1071")
    private String busNumPlate;
    // 버스 노선번호(예:"780-1번")
    private String routeNum;

    public ConfirmBus(){}

    public ConfirmBus(String busNumPlate, String routeNum) {
        this.busNumPlate = busNumPlate;
        this.routeNum = routeNum;
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
}
