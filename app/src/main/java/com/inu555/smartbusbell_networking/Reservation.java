package com.inu555.smartbusbell_networking;

// 예약 정보 클래스
public class Reservation {

    // 안드로이드 클라이언트 식별자 (예:"abc111")
    private String androidClientIdentifier;
    // 버스 차량 번호 (예:"인천11가2222")
    private String busNumPlate;
    // 예약 하차 정류장 고유번호 (예:"38-396")
    private String reservationToStop;

    public Reservation(){}

    // 예약 클래스 생성자
    public Reservation(String androidClientIdentifier, String busNumPlate, String reservationToStop){
        this.androidClientIdentifier = androidClientIdentifier;
        this.busNumPlate = busNumPlate;
        this.reservationToStop = reservationToStop;
    }

    // 안드로이드 클라이언트 식별자 셋터 함수
    public void setAndroidClientIdentifier(String androidClientIdentifier) {
        this.androidClientIdentifier = androidClientIdentifier;
    }

    // 안드로이드 클라이언트 식별자 반환 함수
    public String getAndroidClientIdentifier(){ return androidClientIdentifier; }

    // 탑승버스 식별자 셋터 함수
    public void setBusNumPlate(String busNumPlate) {
        this.busNumPlate = busNumPlate;
    }

    // 탑승 버스 식별자 반환 함수
    public String getBusNumPlate(){ return busNumPlate; }

    // 예약 하차 정류장 번호 셋터 함수
    public void setReservationToStop(String reservationToStop) {
        this.reservationToStop = reservationToStop;
    }

    // 예약 하차 정류장 번호 반환 함수
    public String getReservationToStop(){ return reservationToStop; }

}
