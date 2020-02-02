package com.inu555.smartbusbell_networking;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.inu555.smartbusbell_networking.R;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Field;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.HashMap;

/*
    안드로이드의 인터넷 접근 권한 허용을 위해
    manifests/AndroidManifest.xml에
    <uses-permission android:name="android.permission.INTERNET" /> 를 추가하였다.
 */

public class MainActivity extends AppCompatActivity {

    // 포트번호
    private final int port = 5555;
    // IP주소
    //private final String ip = "13.125.8.121";
    private final String ip = "10.0.2.2";

    // Socket 객체 참조 변수
    private Socket socket;

    // 데이터를 송수신할 스트림버퍼 Reader, Writer 참조변수 선언
    private BufferedReader br;
    private BufferedWriter bw;

    // 수신한 데이터를 저장할 스트링 참조변수
    private String receivedDataString;
    // 송신할 객체 데이터에 대한 JSON형 문자열
    private String dataJsonStrToSend;

    private String dataJsonStr1;
    private String dataJsonStr2;
    private String dataJsonStr3;
    private String dataJsonStr4;
    private String dataJsonStr5;
    private String dataJsonStr6;
    private String dataJsonStr7;
    private String dataJsonStr8;
    private String dataJsonStr9;
    private String dataJsonStr10;
    private String dataJsonStr11;
    private String dataJsonStr12;
    private String dataJsonStr13;
    private String dataJsonStr14;
    private String dataJsonStr15;


    // 데이터를 웹 서버에 보낼 버튼 참조변수
    private Button bt_send1;
    private Button bt_send2;
    private Button bt_send3;
    private Button bt_send4;
    private Button bt_send5;
    private Button bt_send6;
    private Button bt_send7;
    private Button bt_send8;
    private Button bt_send9;
    private Button bt_send10;
    private Button bt_send11;
    private Button bt_send12;
    private Button bt_send13;
    private Button bt_send14;
    private Button bt_send15;

    // 전송 버튼 클릭 여부를 체크할 boolean 변수
    private boolean isSendButtonClicked = false;

    // 웹 서버로부터 수신한 객체들을 JSON형식의 String으로 바꿔서 출력할 TextView
    private TextView tv_to_print_received_obejcts_in_json_string;

    // 서버로부터 데이터를 받는 스레드 객체 참조변수 선언
    Thread receivingDataThread;
    // 서버로 데이터를 전송하는 스레드 객체 참조변수 선언
    Thread sendDataThread;

    // String을 key값으로,
    // Object를 value값으로 담을 HashMap 생성
    // 이 데이터는 어플에서 서버로 전송할 총 데이터 묶음임.
    HashMap<String, Object> model1 = new HashMap<String, Object>();
    HashMap<String, Object> model2 = new HashMap<String, Object>();
    HashMap<String, Object> model3 = new HashMap<String, Object>();
    HashMap<String, Object> model4 = new HashMap<String, Object>();
    HashMap<String, Object> model5 = new HashMap<String, Object>();
    HashMap<String, Object> model6 = new HashMap<String, Object>();
    HashMap<String, Object> model7 = new HashMap<String, Object>();
    HashMap<String, Object> model8 = new HashMap<String, Object>();
    HashMap<String, Object> model9 = new HashMap<String, Object>();
    HashMap<String, Object> model10 = new HashMap<String, Object>();
    HashMap<String, Object> model11 = new HashMap<String, Object>();
    HashMap<String, Object> model12 = new HashMap<String, Object>();
    HashMap<String, Object> model13 = new HashMap<String, Object>();
    HashMap<String, Object> model14 = new HashMap<String, Object>();
    HashMap<String, Object> model15 = new HashMap<String, Object>();



    // 어플이 처음 켜질 때
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        System.setProperty("file.encoding","UTF-8");

        Field charset = null;
        try {
            charset = Charset.class.getDeclaredField("defaultCharset");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        charset.setAccessible(true);

        try {
            charset.set(null,null);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        // 위젯 참조 변수에 대한 할당
        bt_send1 = (Button) findViewById(R.id.button1);
        bt_send2 = (Button) findViewById(R.id.button2);
        bt_send3 = (Button) findViewById(R.id.button3);
        bt_send4 = (Button) findViewById(R.id.button4);
        bt_send5 = (Button) findViewById(R.id.button5);
        bt_send6 = (Button) findViewById(R.id.button6);
        bt_send7 = (Button) findViewById(R.id.button7);
        bt_send8 = (Button) findViewById(R.id.button8);
        bt_send9 = (Button) findViewById(R.id.button9);
        bt_send10 = (Button) findViewById(R.id.button10);
        bt_send11 = (Button) findViewById(R.id.button11);
        bt_send12 = (Button) findViewById(R.id.button12);
        bt_send13 = (Button) findViewById(R.id.button13);
        bt_send14 = (Button) findViewById(R.id.button14);
        bt_send15 = (Button) findViewById(R.id.button15);

        // receivingData Runnable 객체를 사용하여
        // 위에서 설정한 IP주소 및 port번호로 socket에 연결하여 Runnable 객체 생성
        // 해당 Runnable객체를 스레드 참조변수에 할당
        receivingDataThread = new Thread(new receivingData());
        System.out.println("새로운 receivingData 스레드 생성");
        // 웹소켓 데이터 수신 스레드 시작
        receivingDataThread.start();

        // sendData Runnable 객체를 사용하여
        // 스레드 객체 생성
        // 해당 객체를 참조변수에 할당
        // 데이터 전송 스레드 실행
        sendDataThread = new Thread(new sendData());
        System.out.println("새로운 sendData 스레드 생성");
        sendDataThread.start();

        // Gson형 객체 생성
        Gson gson = new Gson();




        // === 1번 데이터 === //



        // HashMap에
        // "DataToSendFromAndroidToSpring"를 key값으로
        // reservationsArrayList를 model값으로 하여 저장
        // 위에서 말했듯이 이 model변수에 있는 데이터가
        // 어플에서 서버로 전송할 총 데이터 묶음이다.
//        model1.put();

        // 전체 model 데이터를 Gson의 라이브러리 함수로
        // JSON형식의 문자열로 변환하여,j
        // dataJsonStrToSend 참조 변수에 할당하였다.
        dataJsonStr1 = gson.toJson(model1);

        // 확인차 로그출력을 한다.
        System.out.println(dataJsonStr1);

        // === 1번 데이터 끝 === //




        // === 2번 데이터 === //



        // HashMap에
        // "DataToSendFromAndroidToSpring"를 key값으로
        // reservationsArrayList를 model값으로 하여 저장
        // 위에서 말했듯이 이 model변수에 있는 데이터가
        // 어플에서 서버로 전송할 총 데이터 묶음이다.
//        model2.put();

        // 전체 model 데이터를 Gson의 라이브러리 함수로
        // JSON형식의 문자열로 변환하여,
        // dataJsonStrToSend 참조 변수에 할당하였다.
        dataJsonStr2 = gson.toJson(model2);

        // 확인차 로그출력을 한다.
        System.out.println(dataJsonStr2);

        // === 2번 데이터 끝 === //




        // === 3번 데이터 === //



        // HashMap에
        // "DataToSendFromAndroidToSpring"를 key값으로
        // reservationsArrayList를 model값으로 하여 저장
        // 위에서 말했듯이 이 model변수에 있는 데이터가
        // 어플에서 서버로 전송할 총 데이터 묶음이다.
//        model3.put();

        // 전체 model 데이터를 Gson의 라이브러리 함수로
        // JSON형식의 문자열로 변환하여,
        // dataJsonStrToSend 참조 변수에 할당하였다.
        dataJsonStr3 = gson.toJson(model3);

        // 확인차 로그출력을 한다.
        System.out.println(dataJsonStr3);

        // === 3번 데이터 끝 === //




        // === 4번 데이터 === //



        // HashMap에
        // "DataToSendFromAndroidToSpring"를 key값으로
        // reservationsArrayList를 model값으로 하여 저장
        // 위에서 말했듯이 이 model변수에 있는 데이터가
        // 어플에서 서버로 전송할 총 데이터 묶음이다.
//        model4.put();

        // 전체 model 데이터를 Gson의 라이브러리 함수로
        // JSON형식의 문자열로 변환하여,j
        // dataJsonStrToSend 참조 변수에 할당하였다.
        dataJsonStr4 = gson.toJson(model4);

        // 확인차 로그출력을 한다.
        System.out.println(dataJsonStr4);

        // === 4번 데이터 끝 === //




        // === 5번 데이터 === //



        // HashMap에
        // "DataToSendFromAndroidToSpring"를 key값으로
        // reservationsArrayList를 model값으로 하여 저장
        // 위에서 말했듯이 이 model변수에 있는 데이터가
        // 어플에서 서버로 전송할 총 데이터 묶음이다.
//        model5.put();

        // 전체 model 데이터를 Gson의 라이브러리 함수로
        // JSON형식의 문자열로 변환하여,
        // dataJsonStrToSend 참조 변수에 할당하였다.
        dataJsonStr5 = gson.toJson(model5);

        // 확인차 로그출력을 한다.
        System.out.println(dataJsonStr5);

        // === 5번 데이터 끝 === //




        // === 6번 데이터 === //



        // HashMap에
        // "DataToSendFromAndroidToSpring"를 key값으로
        // reservationsArrayList를 model값으로 하여 저장
        // 위에서 말했듯이 이 model변수에 있는 데이터가
        // 어플에서 서버로 전송할 총 데이터 묶음이다.
//        model6.put();

        // 전체 model 데이터를 Gson의 라이브러리 함수로
        // JSON형식의 문자열로 변환하여,
        // dataJsonStrToSend 참조 변수에 할당하였다.
        dataJsonStr6 = gson.toJson(model6);

        // 확인차 로그출력을 한다.
        System.out.println(dataJsonStr6);

        // === 6번 데이터 끝 === //




        // === 7번 데이터 === //



        // HashMap에
        // "DataToSendFromAndroidToSpring"를 key값으로
        // reservationsArrayList를 model값으로 하여 저장
        // 위에서 말했듯이 이 model변수에 있는 데이터가
        // 어플에서 서버로 전송할 총 데이터 묶음이다.
//        model7.put();

        // 전체 model 데이터를 Gson의 라이브러리 함수로
        // JSON형식의 문자열로 변환하여,j
        // dataJsonStrToSend 참조 변수에 할당하였다.
        dataJsonStr7 = gson.toJson(model7);

        // 확인차 로그출력을 한다.
        System.out.println(dataJsonStr7);

        // === 7번 데이터 끝 === //




        // === 8번 데이터 === //



        // HashMap에
        // "DataToSendFromAndroidToSpring"를 key값으로
        // reservationsArrayList를 model값으로 하여 저장
        // 위에서 말했듯이 이 model변수에 있는 데이터가
        // 어플에서 서버로 전송할 총 데이터 묶음이다.
//        model8.put();

        // 전체 model 데이터를 Gson의 라이브러리 함수로
        // JSON형식의 문자열로 변환하여,
        // dataJsonStrToSend 참조 변수에 할당하였다.
        dataJsonStr8 = gson.toJson(model8);

        // 확인차 로그출력을 한다.
        System.out.println(dataJsonStr8);

        // === 8번 데이터 끝 === //




        // === 9번 데이터 === //



        // HashMap에
        // "DataToSendFromAndroidToSpring"를 key값으로
        // reservationsArrayList를 model값으로 하여 저장
        // 위에서 말했듯이 이 model변수에 있는 데이터가
        // 어플에서 서버로 전송할 총 데이터 묶음이다.
//        model9.put();

        // 전체 model 데이터를 Gson의 라이브러리 함수로
        // JSON형식의 문자열로 변환하여,
        // dataJsonStrToSend 참조 변수에 할당하였다.
        dataJsonStr9 = gson.toJson(model9);

        // 확인차 로그출력을 한다.
        System.out.println(dataJsonStr9);

        // === 9번 데이터 끝 === //




        // === 10번 데이터 === //



        // HashMap에
        // "DataToSendFromAndroidToSpring"를 key값으로
        // reservationsArrayList를 model값으로 하여 저장
        // 위에서 말했듯이 이 model변수에 있는 데이터가
        // 어플에서 서버로 전송할 총 데이터 묶음이다.
//        model10.put();

        // 전체 model 데이터를 Gson의 라이브러리 함수로
        // JSON형식의 문자열로 변환하여,j
        // dataJsonStrToSend 참조 변수에 할당하였다.
        dataJsonStr10 = gson.toJson(model10);

        // 확인차 로그출력을 한다.
        System.out.println(dataJsonStr10);

        // === 10번 데이터 끝 === //




        // === 11번 데이터 === //



        // HashMap에
        // "DataToSendFromAndroidToSpring"를 key값으로
        // reservationsArrayList를 model값으로 하여 저장
        // 위에서 말했듯이 이 model변수에 있는 데이터가
        // 어플에서 서버로 전송할 총 데이터 묶음이다.
//        model11.put();

        // 전체 model 데이터를 Gson의 라이브러리 함수로
        // JSON형식의 문자열로 변환하여,
        // dataJsonStrToSend 참조 변수에 할당하였다.
        dataJsonStr11 = gson.toJson(model11);

        // 확인차 로그출력을 한다.
        System.out.println(dataJsonStr11);

        // === 11번 데이터 끝 === //




        // === 12번 데이터 === //



        // HashMap에
        // "DataToSendFromAndroidToSpring"를 key값으로
        // reservationsArrayList를 model값으로 하여 저장
        // 위에서 말했듯이 이 model변수에 있는 데이터가
        // 어플에서 서버로 전송할 총 데이터 묶음이다.
//        model12.put();

        // 전체 model 데이터를 Gson의 라이브러리 함수로
        // JSON형식의 문자열로 변환하여,
        // dataJsonStrToSend 참조 변수에 할당하였다.
        dataJsonStr12 = gson.toJson(model12);

        // 확인차 로그출력을 한다.
        System.out.println(dataJsonStr12);

        // === 12번 데이터 끝 === //




        // === 13번 데이터 === //



        // HashMap에
        // "DataToSendFromAndroidToSpring"를 key값으로
        // reservationsArrayList를 model값으로 하여 저장
        // 위에서 말했듯이 이 model변수에 있는 데이터가
        // 어플에서 서버로 전송할 총 데이터 묶음이다.
//        model13.put();

        // 전체 model 데이터를 Gson의 라이브러리 함수로
        // JSON형식의 문자열로 변환하여,j
        // dataJsonStrToSend 참조 변수에 할당하였다.
        dataJsonStr13 = gson.toJson(model13);

        // 확인차 로그출력을 한다.
        System.out.println(dataJsonStr13);

        // === 13번 데이터 끝 === //




        // === 14번 데이터 === //



        // HashMap에
        // "DataToSendFromAndroidToSpring"를 key값으로
        // reservationsArrayList를 model값으로 하여 저장
        // 위에서 말했듯이 이 model변수에 있는 데이터가
        // 어플에서 서버로 전송할 총 데이터 묶음이다.
//        model14.put();

        // 전체 model 데이터를 Gson의 라이브러리 함수로
        // JSON형식의 문자열로 변환하여,
        // dataJsonStrToSend 참조 변수에 할당하였다.
        dataJsonStr14 = gson.toJson(model14);

        // 확인차 로그출력을 한다.
        System.out.println(dataJsonStr14);

        // === 14번 데이터 끝 === //




        // === 15번 데이터 === //



        // HashMap에
        // "DataToSendFromAndroidToSpring"를 key값으로
        // reservationsArrayList를 model값으로 하여 저장
        // 위에서 말했듯이 이 model변수에 있는 데이터가
        // 어플에서 서버로 전송할 총 데이터 묶음이다.
//        model15.put();

        // 전체 model 데이터를 Gson의 라이브러리 함수로
        // JSON형식의 문자열로 변환하여,
        // dataJsonStrToSend 참조 변수에 할당하였다.
        dataJsonStr15 = gson.toJson(model15);

        // 확인차 로그출력을 한다.
        System.out.println(dataJsonStr15);

        // === 3번 데이터 끝 === //








        // 전송 버튼에 클릭리스터 설정
        bt_send1.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 서버에 데이터 전송 버튼 클릭 시,
                // 버튼 클릭 상태 true로 변경
                dataJsonStrToSend = dataJsonStr1;
                isSendButtonClicked = true;
            }
        });

        bt_send2.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 서버에 데이터 전송 버튼 클릭 시,
                // 버튼 클릭 상태 true로 변경
                dataJsonStrToSend = dataJsonStr2;
                isSendButtonClicked = true;
            }
        });

        bt_send3.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 서버에 데이터 전송 버튼 클릭 시,
                // 버튼 클릭 상태 true로 변경
                dataJsonStrToSend = dataJsonStr3;
                isSendButtonClicked = true;
            }
        });
        bt_send4.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 서버에 데이터 전송 버튼 클릭 시,
                // 버튼 클릭 상태 true로 변경
                dataJsonStrToSend = dataJsonStr4;
                isSendButtonClicked = true;
            }
        });

        bt_send5.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 서버에 데이터 전송 버튼 클릭 시,
                // 버튼 클릭 상태 true로 변경
                dataJsonStrToSend = dataJsonStr5;
                isSendButtonClicked = true;
            }
        });

        bt_send6.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 서버에 데이터 전송 버튼 클릭 시,
                // 버튼 클릭 상태 true로 변경
                dataJsonStrToSend = dataJsonStr6;
                isSendButtonClicked = true;
            }
        });
        bt_send7.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 서버에 데이터 전송 버튼 클릭 시,
                // 버튼 클릭 상태 true로 변경
                dataJsonStrToSend = dataJsonStr7;
                isSendButtonClicked = true;
            }
        });

        bt_send8.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 서버에 데이터 전송 버튼 클릭 시,
                // 버튼 클릭 상태 true로 변경
                dataJsonStrToSend = dataJsonStr8;
                isSendButtonClicked = true;
            }
        });

        bt_send9.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 서버에 데이터 전송 버튼 클릭 시,
                // 버튼 클릭 상태 true로 변경
                dataJsonStrToSend = dataJsonStr9;
                isSendButtonClicked = true;
            }
        });
        bt_send10.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 서버에 데이터 전송 버튼 클릭 시,
                // 버튼 클릭 상태 true로 변경
                dataJsonStrToSend = dataJsonStr10;
                isSendButtonClicked = true;
            }
        });

        bt_send11.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 서버에 데이터 전송 버튼 클릭 시,
                // 버튼 클릭 상태 true로 변경
                dataJsonStrToSend = dataJsonStr11;
                isSendButtonClicked = true;
            }
        });

        bt_send12.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 서버에 데이터 전송 버튼 클릭 시,
                // 버튼 클릭 상태 true로 변경
                dataJsonStrToSend = dataJsonStr12;
                isSendButtonClicked = true;
            }
        });
        bt_send13.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 서버에 데이터 전송 버튼 클릭 시,
                // 버튼 클릭 상태 true로 변경
                dataJsonStrToSend = dataJsonStr13;
                isSendButtonClicked = true;
            }
        });

        bt_send14.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 서버에 데이터 전송 버튼 클릭 시,
                // 버튼 클릭 상태 true로 변경
                dataJsonStrToSend = dataJsonStr14;
                isSendButtonClicked = true;
            }
        });

        bt_send15.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 서버에 데이터 전송 버튼 클릭 시,
                // 버튼 클릭 상태 true로 변경
                dataJsonStrToSend = dataJsonStr15;
                isSendButtonClicked = true;
            }
        });
    }

    // 어플이 종료될 때
    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            // 버퍼 Reader, Writer 닫기
            bw.close();
            br.close();
            // 소켓 닫기
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    // 안드로이드에서 네트워킹은 무조건 별도 스레드를 사용하여야 함
    // 메인스레드는 UI작업만 하여야 함
    // 메인스레드에서 네트워킹 작업시 에러발생함
    // 소켓 데이터 송신을 위한 별도 스레드를 위한
    // Runnable sendDate클래스 구현
    // 액티비티의 송신 버튼 클릭 여부를
    // 무한루프로 체크하면서
    // 사용자가 버튼을 클릭할 시,
    // 서버에 위에서 준비한 총 데이터 model을
    // 버퍼스트림으로 전송함
    private class sendData implements Runnable{
        public void run() {
            System.out.println("새로운 sendData 스레드 실행");
            try {
                while(true){
                    // 만약 버튼이 클릭되지 않았으면
                    // continue
                    if(isSendButtonClicked == false){
                        continue;
                    }else{
                        // 버튼이 클릭되었으면
                        // JSON형태의 문자열 데이터를
                        // 버퍼스트림을 통해 서버로 보낸다
                        bw.write(dataJsonStrToSend);
                        // 수신측 BufferdReader의 readLine()함수를 통해 문자열 데이터를 수신하려면
                        // 송신측의 inputStream에는  반드시 개행문자가 포함되어야 한다.
                        // 자바에서의 개행문자는 "\n" 이지만,
                        // 스트림에서의 개행문자는 "\r\n"이 개행문자이다.
                        // 따라서, 보내는쪽 스트림의 의 데이터 뒤에 "\r\n"을 반드시 붙여야한다.
                        // BufferedWriter에서 이 역할을
                        // newLine()함수가 수행한다.
                        bw.newLine();
                        //남아있는 데이터를 모두 출력시킴
                        bw.flush();
                        System.out.println("전송한 데이터 : " + dataJsonStrToSend);
                        System.out.println("데이터 전송 완료");
                        // 데이터 전송 완료 후
                        // 버튼 클릭 상태 false로 변경
                        isSendButtonClicked = false;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // 소켓 데이터 수신을 위한 별도 스레드를 위한
    // Runnable receivingData 클래스 구현
    // 수신되는 데이터 문자열을 무한루프로 체크
    private class receivingData implements Runnable{
        public void run() {
            System.out.println("새로운 receivingData 스레드 실행");
            try {
                // IP주소와 port번호로 소켓 객체를 생성하여
                // 소켓 참조변수에 할당
                socket = new Socket(ip, port);
                System.out.println("소켓 객체 생성 및 할당 성공");
                // utf-8형으로 문자열을 읽거나 출력하도록 버퍼스트림 설정 및 할당
                br = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
                bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"));
                System.out.println("버퍼스트림 생성 및 할당 성공");

                System.out.println("통신 시작.");
                while (true) {
                    System.out.println("데이터 수신 대기중");
                    receivedDataString = br.readLine();
                    System.out.println("데이터 수신됨");
                    // 로그 출력을 통한 수신 데이터 확인
                    System.out.println(receivedDataString);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println("receivingData 스레드 종료");
        }
    }

}
