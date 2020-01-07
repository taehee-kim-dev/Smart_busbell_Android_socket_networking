package com.inu555.smartbusbell_networking;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.google.gson.Gson;

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

    // 데이터를 웹 서버에 보낼 버튼 참조변수
    private Button bt_to_send_objects_to_web_server;
    // 전송 버튼 클릭 여부를 체크할 boolean 변수
    private boolean isSendButtonClicked = false;
    // 웹 서버로부터 수신한 객체들을 JSON형식의 String으로 바꿔서 출력할 TextView
    private TextView tv_to_print_received_obejcts_in_json_string;

    // 서버로부터 데이터를 받는 스레드 객체 참조변수 선언
    Thread receivingDataThread;
    // 서버로 데이터를 전송하는 스레드 객체 참조변수 선언
    Thread sendDataThread;

    // Handler 참조변수 선언
    Handler mHandler;

    // String을 key값으로,
    // Object를 value값으로 담을 HashMap 생성
    // 이 데이터는 어플에서 서버로 전송할 총 데이터 묶음임.
    HashMap<String, Object> model = new HashMap<String, Object>();


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
        bt_to_send_objects_to_web_server = (Button) findViewById(R.id.button_toSendToObejctsToWebServer);
        tv_to_print_received_obejcts_in_json_string = (TextView) findViewById(R.id.textView_receivedMessagesFromWebServer);

        // Handler 객체 할당
        mHandler = new Handler();

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
        sendDataThread = new Thread(new sendDate());
        System.out.println("새로운 sendData 스레드 생성");
        sendDataThread.start();

        /*

        // ==== 데이터 생성 및 준비 ====
        // 어플에서 Reservation 객체 2개를 ArrayList에 담아서 서버에 보낸다.
        // Reservation형 ArrayList 생성
        ArrayList<Reservation> reservationsArrayList = new ArrayList<Reservation>();

        // 첫 번째 Reservation 객체를 생성하여
        // Reservation 클래스에 있는 set함수를 통해
        // 객체의 각 멤버변수 필드를 설정하고
        // ArrayList에 추가
        reservationsArrayList.add(new Reservation()
                .setSampleNumber(1111)
                .setSampleCode("안드로이드 코드1")
                .setSampleName("안드로이드 이름1")
                .setSampleDate(new Date()));

        // 두 번째 Reservation 객체를 생성하여
        // Reservation 클래스에 있는 set함수를 통해
        // 객체의 각 멤버변수 필드를 설정하고
        // ArrayList에 추가
        reservationsArrayList.add(new Reservation()
                .setSampleNumber(2222)
                .setSampleCode("안드로이드 코드2")
                .setSampleName("안드로이드 이름2")
                .setSampleDate(new Date()));

         */

        Reservation reservation = new Reservation(
                "abc111",
                "인천11가2222",
                "38-026"
        );

        ConfirmBus confirmBus = new ConfirmBus("인천11가2222", "780-1번");

        // HashMap에
        // "DataToSendFromAndroidToSpring"를 key값으로
        // reservationsArrayList를 model값으로 하여 저장
        // 위에서 말했듯이 이 model변수에 있는 데이터가
        // 어플에서 서버로 전송할 총 데이터 묶음이다.
        model.put("requestBusRoute", "780-1번");

        /*
            위에서 설정한 총 데이터 HaspMap 참조변수는 model 이다.
            클라이언트와 서버간에 데이터 전송 및 수신을 할 때,
            보통 데이터 표현 방식으로 JSON을 사용한다.
            데이터를 JSON 표현 방식으로 String화 하여
            송신 및 수신한다.

            클라이언트 : 객체 데이터 -> JSON 표현방식의 문자열 -> 전송 -> 서버
            서버 :  클라이언트 -> 수신 -> JSON 표현방식의 문자열 -> 객체 데이터

            따라서
            이 HashMap 객체 model을 JSON형식 String으로 변환한다.
            이를 위해 Gson을 사용한다.
            Gson은 Google에서 만든 JSON 활용 라이브러리이다. 매우 편리하다.
            이 라이브러리 사용을 위해 Gradle Scripts/build.gradle(Module: app)에
            implementation 'com.google.code.gson:gson:2.8.5' 로 의존성 추가를 해 주었다.
            HashMap형 객체 데이터를 JSON형식의 문자열로 간편하게 변환해준다.
            물론 반대로 변환도 가능하다.
        */

        // Gson형 객체 생성
        Gson gson = new Gson();
        // 전체 model 데이터를 Gson의 라이브러리 함수로
        // JSON형식의 문자열로 변환하여,
        // dataJsonStrToSend 참조 변수에 할당하였다.
        dataJsonStrToSend = gson.toJson(model);

        // 확인차 로그출력을 한다.
        System.out.println(dataJsonStrToSend);

        // 전송 버튼에 클릭리스터 설정
        bt_to_send_objects_to_web_server.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 서버에 데이터 전송 버튼 클릭 시,
                // 버튼 클릭 상태 true로 변경
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
    private class sendDate implements Runnable{
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
    // 수신되는 데이터 문자열을 무한루프로 체크하면서
    // 수신된 데이터의 내용이 존재할 시,
    // Handler를 통해 UI 업데이트를 한다.
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
                    // 핸들러를 통한 TextView 업데이트
                    mHandler.post(updateTextView);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println("receivingData 스레드 종료");
        }
    }

    // 익명클래스를 사용한 Runnable 클래스 구현
    // Runnable 객체 생성 및 할당
    // receivingData의 Handler post에서 사용된다.
    private Runnable updateTextView = new Runnable() {
        @Override
        public void run() {
            tv_to_print_received_obejcts_in_json_string.append("\n"+receivedDataString);
            System.out.println("텍스트뷰 업데이트 완료");
        }
    };

}
