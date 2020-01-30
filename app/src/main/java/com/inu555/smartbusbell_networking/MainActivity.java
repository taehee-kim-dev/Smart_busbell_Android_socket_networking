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
    private String dataJsonStrToSend1;
    private String dataJsonStrToSend2;
    private String dataJsonStrToSend3;


    // 데이터를 웹 서버에 보낼 버튼 참조변수
    private Button bt_send1;
    private Button bt_send2;
    private Button bt_send3;

    // 전송 버튼 클릭 여부를 체크할 boolean 변수
    private boolean isSendButtonClicked1 = false;
    private boolean isSendButtonClicked2 = false;
    private boolean isSendButtonClicked3 = false;
    // 웹 서버로부터 수신한 객체들을 JSON형식의 String으로 바꿔서 출력할 TextView
    private TextView tv_to_print_received_obejcts_in_json_string;

    // 서버로부터 데이터를 받는 스레드 객체 참조변수 선언
    Thread receivingDataThread;
    // 서버로 데이터를 전송하는 스레드 객체 참조변수 선언
    Thread sendDataThread1;
    Thread sendDataThread2;
    Thread sendDataThread3;


    // Handler 참조변수 선언
    Handler mHandler;

    // String을 key값으로,
    // Object를 value값으로 담을 HashMap 생성
    // 이 데이터는 어플에서 서버로 전송할 총 데이터 묶음임.
    HashMap<String, Object> model1 = new HashMap<String, Object>();
    HashMap<String, Object> model2 = new HashMap<String, Object>();
    HashMap<String, Object> model3 = new HashMap<String, Object>();



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
        sendDataThread1 = new Thread(new sendData1());
        System.out.println("새로운 sendData1 스레드 생성");
        sendDataThread1.start();

        sendDataThread2 = new Thread(new sendData2());
        System.out.println("새로운 sendData1 스레드 생성");
        sendDataThread2.start();

        sendDataThread3 = new Thread(new sendData3());
        System.out.println("새로운 sendData1 스레드 생성");
        sendDataThread3.start();

        // Gson형 객체 생성
        Gson gson = new Gson();

        // === 1번 데이터 === //


        Reservation reservation = new Reservation(
                "abc111",
                "인천11가2222",
                "38-026"
        );

        ConfirmBus confirmBus = new ConfirmBus("인천11가2222", "780-1번");

        Android android1 = new Android("abc111", "인천11가2222", "780-1", null);

        // HashMap에
        // "DataToSendFromAndroidToSpring"를 key값으로
        // reservationsArrayList를 model값으로 하여 저장
        // 위에서 말했듯이 이 model변수에 있는 데이터가
        // 어플에서 서버로 전송할 총 데이터 묶음이다.
        model1.put("test1", android1);

        // 전체 model 데이터를 Gson의 라이브러리 함수로
        // JSON형식의 문자열로 변환하여,j
        // dataJsonStrToSend 참조 변수에 할당하였다.
        dataJsonStrToSend1 = gson.toJson(model1);

        // 확인차 로그출력을 한다.
        System.out.println(dataJsonStrToSend1);

        // === 1번 데이터 끝 === //




        // === 2번 데이터 === //

        Android android2 = new Android("abc222", "인천11가2222", "780-1", null);

        // HashMap에
        // "DataToSendFromAndroidToSpring"를 key값으로
        // reservationsArrayList를 model값으로 하여 저장
        // 위에서 말했듯이 이 model변수에 있는 데이터가
        // 어플에서 서버로 전송할 총 데이터 묶음이다.
        model2.put("test2", android2);

        // 전체 model 데이터를 Gson의 라이브러리 함수로
        // JSON형식의 문자열로 변환하여,
        // dataJsonStrToSend 참조 변수에 할당하였다.
        dataJsonStrToSend2 = gson.toJson(model2);

        // 확인차 로그출력을 한다.
        System.out.println(dataJsonStrToSend2);

        // === 2번 데이터 끝 === //


        Android android3 = new Android("abc333", "인천11가2222", "780-1", null);

        // HashMap에
        // "DataToSendFromAndroidToSpring"를 key값으로
        // reservationsArrayList를 model값으로 하여 저장
        // 위에서 말했듯이 이 model변수에 있는 데이터가
        // 어플에서 서버로 전송할 총 데이터 묶음이다.
        model3.put("test3", android3);

        // 전체 model 데이터를 Gson의 라이브러리 함수로
        // JSON형식의 문자열로 변환하여,
        // dataJsonStrToSend 참조 변수에 할당하였다.
        dataJsonStrToSend3 = gson.toJson(model3);

        // 확인차 로그출력을 한다.
        System.out.println(dataJsonStrToSend3);

        // === 3번 데이터 === //



        // === 3번 데이터 끝 === //

        // 전송 버튼에 클릭리스터 설정
        bt_send1.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 서버에 데이터 전송 버튼 클릭 시,
                // 버튼 클릭 상태 true로 변경
                isSendButtonClicked1 = true;
            }
        });

        bt_send2.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 서버에 데이터 전송 버튼 클릭 시,
                // 버튼 클릭 상태 true로 변경
                isSendButtonClicked2 = true;
            }
        });

        bt_send3.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 서버에 데이터 전송 버튼 클릭 시,
                // 버튼 클릭 상태 true로 변경
                isSendButtonClicked3 = true;
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
    private class sendData1 implements Runnable{
        public void run() {
            System.out.println("새로운 sendData 스레드 실행");
            try {
                while(true){
                    // 만약 버튼이 클릭되지 않았으면
                    // continue
                    if(isSendButtonClicked1 == false){
                        continue;
                    }else{
                        // 버튼이 클릭되었으면
                        // JSON형태의 문자열 데이터를
                        // 버퍼스트림을 통해 서버로 보낸다
                        bw.write(dataJsonStrToSend1);
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
                        System.out.println("전송한 데이터 : " + dataJsonStrToSend1);
                        System.out.println("데이터 전송 완료");
                        // 데이터 전송 완료 후
                        // 버튼 클릭 상태 false로 변경
                        isSendButtonClicked1 = false;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private class sendData2 implements Runnable{
        public void run() {
            System.out.println("새로운 sendData 스레드 실행");
            try {
                while(true){
                    // 만약 버튼이 클릭되지 않았으면
                    // continue
                    if(isSendButtonClicked2 == false){
                        continue;
                    }else{
                        // 버튼이 클릭되었으면
                        // JSON형태의 문자열 데이터를
                        // 버퍼스트림을 통해 서버로 보낸다
                        bw.write(dataJsonStrToSend2);
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
                        System.out.println("전송한 데이터 : " + dataJsonStrToSend2);
                        System.out.println("데이터 전송 완료");
                        // 데이터 전송 완료 후
                        // 버튼 클릭 상태 false로 변경
                        isSendButtonClicked2 = false;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private class sendData3 implements Runnable{
        public void run() {
            System.out.println("새로운 sendData 스레드 실행");
            try {
                while(true){
                    // 만약 버튼이 클릭되지 않았으면
                    // continue
                    if(isSendButtonClicked3 == false){
                        continue;
                    }else{
                        // 버튼이 클릭되었으면
                        // JSON형태의 문자열 데이터를
                        // 버퍼스트림을 통해 서버로 보낸다
                        bw.write(dataJsonStrToSend3);
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
                        System.out.println("전송한 데이터 : " + dataJsonStrToSend3);
                        System.out.println("데이터 전송 완료");
                        // 데이터 전송 완료 후
                        // 버튼 클릭 상태 false로 변경
                        isSendButtonClicked3 = false;
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
