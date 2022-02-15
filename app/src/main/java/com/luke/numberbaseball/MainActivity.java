package com.luke.numberbaseball;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    int[] comNumbers = new int[3]; // 3개의 배열을 집어넣을 수 있는 comNumbers 변수 생성
    int inputTextCount = 0; // 카운트 해줄 수 있는 변수
    int hitCount = 1; // 맞추는게 ~번째 시도인지, 1부터 시작

    TextView[] inputTextView = new TextView[3]; // TextView 야구공이 3개
    Button[] numButton = new Button[10]; // 숫자 버튼이 10개

    ImageButton backSpaceButton; // 이미지 버튼, 백스페이스 버튼 변수 선언
    ImageButton hitButton; // 이미지 버튼, 히트 버튼 변수 선언

    TextView resultTextView; // 텍스트 뷰 변수 선언
    ScrollView scrollView; // 스크롤 뷰 변수 선언

    SoundPool soundPool; // 사운드풀 변수 선언
    int[] buttonSound = new int[5];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        comNumbers = getComNumbers(); // 랜덤으로 생성된 컴퓨터의 숫자 저장

        // 반복문 이용하여 TextView 야구공 설정
        for (int i = 0; i < inputTextView.length; i++) { // 3개라 0,1,2
            inputTextView[i] = findViewById(R.id.input_text_view_0 + i); // 반복문으로 실행할때마다 i를 더해줘서, 실행 후 다음으로 넘어가게 된다.
        }

        // 반복문 이용하여 숫자버튼 설정
        for (int i = 0; i < numButton.length; i++) {
            numButton[i] = findViewById(R.id.num_button_0 + i);
        }

        backSpaceButton = findViewById(R.id.back_space_button); // 백스페이스 버튼 받아오기
        hitButton = findViewById(R.id.hit_button); // 히트 버튼 받아오기
        resultTextView = findViewById(R.id.result_text_view); // 결과 textview 받아오기
        scrollView = findViewById(R.id.scroll_view); // 스크롤뷰 받아오기

        for (Button getNumButton : numButton) { // numButton 0번부터 ~ 9번까지 getNumButton 에 던져줌
            // getNumButton 을 눌렀을 때 (OnclickListener)
            getNumButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    numButtonClick(view);
                }
            });
        }

        // backSpaceButton 을 눌렀을 때 (OnclickListener)
        backSpaceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backSpaceClick();
            }
        });

        // hitButton 을 눌렀을 때 (OnclickListener)
        hitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hitButtonClick();
            }
        });

    }

    // 히트 버튼 코드 및 예외처리
    private void hitButtonClick() {
        if (inputTextCount < 3) {
            Toast.makeText(getApplicationContext(), "숫자를 선택해주세요 ! ", Toast.LENGTH_SHORT).show();
        } else {
            int[] userNumbers = new int[3]; // 유저가 선택한 숫자 저장하는 배열 선언
            for (int i = 0; i < userNumbers.length; i++) { // i가 0이면
                userNumbers[i] = Integer.parseInt(inputTextView[i].getText().toString()); // 0번째 있는 방에 있는 글자를 가져와서, 정수형으로 바꿔서 userNumbers에 입력해준다.
            }

            int[] countCheck = getCountCheck(comNumbers, userNumbers);

            String resultCount = getCountString(userNumbers, countCheck);

            if (hitCount == 1) {
                resultTextView.setText(resultCount);
                resultTextView.append("\n");
            } else {
                resultTextView.append(resultCount + "\n");
            }

            if (countCheck[0] == 3) {
                hitCount = 1;
            } else {
                hitCount++;
            }

            scrollView.fullScroll(View.FOCUS_DOWN);

            for (int i = 0; i < inputTextView.length; i++) {
                backSpaceClick();
            }

            inputTextCount = 0; // 다시 숫자 입력할수있게 0으로 만든것

        }
    }

    @NonNull
    private String getCountString(int[] userNumbers, int[] countCheck) {
        String resultCount;
        if (countCheck[0] == 3) {
            resultCount = hitCount + "  [" + userNumbers[0] + " " + userNumbers[1] + " " + userNumbers[2] + "] 아웃 - 축하합니다";
        } else {
            resultCount = hitCount + "  [" + userNumbers[0] + " " + userNumbers[1] + " " + userNumbers[2] + "] S : "
                    + countCheck[0] + " B : " + countCheck[1];
        }
        return resultCount;
    }

    // 백스페이스 버튼 코드 및 예외처리
    private void backSpaceClick() {
        if (inputTextCount > 0) {
            int buttonEnableCount = Integer.parseInt(inputTextView[inputTextCount - 1].getText().toString()); // 지우면 지운 숫자를 받아오는 것, 문자형이라 Integer 이용해서 정수형으로 바꿔줌
            numButton[buttonEnableCount].setEnabled(true); // 지운 숫자는 버튼의 색이 활성화 된 색으로 바뀌도록 true 로 바꿔주는 것
            inputTextView[inputTextCount - 1].setText(""); // 마지막에 3이 되어서 -1을 빼주고, 해당 라인때마다 입력되었던 숫자를 공백으로 전환
            inputTextCount--; // 공백으로 바꿔주고 Count 도 -1 가감 해주는것
        } else {
            Toast.makeText(getApplicationContext(), "숫자를 선택해주세요 ! ", Toast.LENGTH_SHORT).show();
        }
    }

    // 숫자 버튼 코드 및 예외처리
    private void numButtonClick(View view) {
        if (inputTextCount < 3) {
            Button button = findViewById(view.getId()); // 위의 view 에서 받아옴
            inputTextView[inputTextCount].setText(button.getText().toString()); // inputTextCount 가 0이라, 0부터 받아온다.
            button.setEnabled(false);
            inputTextCount++; // 마지막에 3이 된다.
            soundPool.play(buttonSound[0], 1, 1, 1, 0, 1);
        } else {
            Toast.makeText(getApplicationContext(), "Hit 버튼을 눌러주세요 ! ", Toast.LENGTH_SHORT).show();
        }
    }

    private int[] getCountCheck(int[] comNumbers, int[] userNumbers) {
        int[] setCount = new int[2];
        for (int i = 0; i < comNumbers.length; i++) {
            for (int j = 0; j < userNumbers.length; j++) {
                if(comNumbers[i] == userNumbers[j]){
                    if(i == j){
                        setCount[0]++; // // 컴퓨터의 숫자와 유저의 숫자가 같고, i와 j가 같으면 = strike, strike 면 setCount 0번째 배열방에 +1 누적
                    } else {
                        setCount[1]++; // 컴퓨터의 숫자와 유저의 숫자가 같고, i와 j가 다르면 = ball, ball 이면 setCount 1번째 배열방에 +1 누적
                    }
                }

            }
        }
        return setCount;
    }

    // 컴퓨터 랜덤으로 숫자 생성하는 코딩
    public int[] getComNumbers() {
        int[] setComNumbers = new int[3]; // 배열 생성

        for (int i = 0; i < setComNumbers.length; i++) {
            // i -> 0, 1, 2 일때 Random 숫자 생성
            setComNumbers[i] = new Random().nextInt(10); // 0부터 9까지 랜덤 숫자를 받아옴.
            // But, 같은 숫자를 받아오면 안되기때문에 for 문 한번 더 사용
            for (int j = 0; j < i; j++) {
                if (setComNumbers[i] == setComNumbers[j]) { // 숫자가 같으면
                    i--; // 랜덤한 숫자를 다시 받아오기 위해, i값을 감소시켜서 for 문을 다시 돌게 한다.
                    break;
                }
            }
        }
        return setComNumbers;
    }

    // 사운드 재생
    @Override
    protected void onStart() {
        super.onStart();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) { // 버젼 비교해서 21버젼 이상만 사용가능.
            AudioAttributes audioAttributes = new AudioAttributes.Builder() // AudioAttributes 타입 생성
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build();

            soundPool = new SoundPool.Builder() // soundPool 타입 생성
                    .setAudioAttributes(audioAttributes)
                    .setMaxStreams(6)
                    .build();

        } else { // 이하면
            soundPool = new SoundPool(6, AudioManager.STREAM_MUSIC, 0); // soundPool 인스턴스화
        }

        for (int i = 0; i < buttonSound.length; i++) {
            buttonSound[i] = soundPool.load(getApplicationContext(), R.raw.button_1 + i, 1);
        }

    }

    // 쓸일 없어지면 재생 해제 시켜줘야함
    @Override
    protected void onStop() {
        super.onStop();
        soundPool.release(); // soundPool 리소스 해제
    }

}

