package com.luke.numberbaseball;

public class ArrayBasic {
    public static void main(String[] args) {

        // 배열 연습 클래스, 어플 실행과 상관없음 없어도 되는 클래스

        // 배열 없이 선언
        int score1 = 100;
        int score2 = 90;
        int score3 = 80;
        int score4 = 70;
        int score5 = 60;

        // 총 점수
        int tot = score1 + score2 + score3 + score4 + score5;
        // 평균
        int ave = tot / 5;

        int [] scoreArray1 = {100,90,70,95,85};
        // 셋이 같은 뜻, 배열 이용하여 각기 다른 방법으로 선언한 것.
        int [] scoreArray2 = new int[5];
        scoreArray2[0] = 100;
        scoreArray2[1] = 90;
        scoreArray2[2] = 70;
        scoreArray2[3] = 95;
        scoreArray2[4] = 85;
        int [] scoreArray3;
        scoreArray3 = new int[] {100,90,70,95,85};

        // scoreArray2 배열과 반복문 이용하여 총점,평균 구하기
        int total = 0;
        for (int i = 0; i < scoreArray2.length; i++) { // scoreArray2.length 를 이용하면 그 배열의 가지고 있는 개수만큼 반복을 시키기때문에 좋다.
            total = total + scoreArray2[i]; // total += scoreArray2[i]; 와 같다.
        }

        int ave1 = total / scoreArray2.length; // 총점에서 배열의 개수만큼 나눠준다, 평균.

    }

}
