package com.github.tadoya.calculator;

import android.graphics.Color;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.SoundPool;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {

    private String cnt="0", ac="AC";
    private boolean landscapeMode;
    String cntTemp="0";

    Button  buttonAC, buttonEqual, buttonPlusMinus, buttonPlus, buttonMinus, buttonDivision,
            buttonMultiply, buttonPercent, buttonDot;
    Button[] buttonNum = new Button[11];

    Button buttonTenX, buttonEx, buttonXy, buttonX2, buttonX3, buttonFraction, buttonRoot,
            buttonRoot3, buttonRooty, buttonLn, buttonLog, buttonEE, buttonE, buttonSin,
            buttonCos, buttonTan, buttonFactorial, buttonRadian, buttonSinh, buttonCosh,
            buttonTanh, buttonRand, buttonPi;
    Button buttonMC, buttonMP, buttonMM, buttonMR;
    Button buttonPrtStart, buttonPrtEnd;
    TextView radText;
    AutoResizeTextView textView;

    String answer="0";
    double memory=0;

    String number = "0";
    String temp = "0";

    BigDecimal dcmalA,dcmalB, dcmalC; // 소수의 곱하기 나누기
    //DecimalFormat df = new DecimalFormat("#,###.###############");

    char operator='0';                // 연산자

    /** caled : '='버튼 눌렀는지에 대한 플레그, errorFlag : x/0 에 대한 플레그
     *  tempFalg : 계산 후('='누른 후) 숫자버튼을 누르고 연산자 버튼을 눌렀을 때 기존의 temp값을 초기화
     *  secondFlag : 공학용 계산기 모드에서 두번째 페이지(추가함수)버튼을 눌렀을 때의 플레그
     *  radFalg : 라디안 플레그
     *  prtFlag : 괄호시작플레그, prtEndFlag : 괄호 마침 플레그
     */
    boolean caled = false, errorFlag = false, tempFlag = false, secondFlag = false, radFlag = false,
            prtFlag = false, prtEndFlag = false;

    int prtFlagNum = 0;     // 괄호 수(이중괄호 삼중괄호...)
    char prtOperator = '0'; // 괄호 내에서의 연산자
    String prtTemp = "";    // 괄호 누르기 전의 값을 저장하기위한 임시변수

    /**
     * 버튼 사운드 효과
     */
    private SoundPool sound_pool;
    private int sound_click;

    private void initSound() {
        //SoundPool(int maxStreams, int streamType, int srcQuality)
        sound_pool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
        //public int load (Context context, int resId, int priority)
        sound_click = sound_pool.load(this, R.raw.click2, 1);
    }

    //public final int play (int soundID, float leftVolume, float rightVolume, int priority, int loop, float rate)
    public void playSound(){
        sound_pool.play( sound_click, 1f, 1f, 0, 0, 1f );
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //처음 시작시 portrait인지 landscape모드인지 확인
        if(getResources().getConfiguration().orientation == 1) landscapeMode = false;
        else landscapeMode = true;
        /**
         * 가로 세로모드 변환에 따른 주요변수 초기화 방지
         */
        if(savedInstanceState != null){
            cnt = savedInstanceState.getString("num");
            number = cnt;
            answer = cnt;
            ac = savedInstanceState.getString("ac");
        }

        initSound();

        textView = (AutoResizeTextView) findViewById(R.id.textView);
        radText = (TextView) findViewById(R.id.radText);

        buttonNum[0] = (Button) findViewById(R.id.button0);
        buttonNum[1] = (Button) findViewById(R.id.button1);
        buttonNum[2] = (Button) findViewById(R.id.button2);
        buttonNum[3] = (Button) findViewById(R.id.button3);
        buttonNum[4] = (Button) findViewById(R.id.button4);
        buttonNum[5] = (Button) findViewById(R.id.button5);
        buttonNum[6] = (Button) findViewById(R.id.button6);
        buttonNum[7] = (Button) findViewById(R.id.button7);
        buttonNum[8] = (Button) findViewById(R.id.button8);
        buttonNum[9] = (Button) findViewById(R.id.button9);
        buttonNum[10] = (Button) findViewById(R.id.button10);


        buttonAC = (Button) findViewById(R.id.buttonAC);
        buttonEqual = (Button) findViewById(R.id.buttonEqual);
        buttonPlusMinus = (Button) findViewById(R.id.buttonPlusMinus);
        buttonPlus = (Button) findViewById(R.id.buttonPlus);
        buttonMinus = (Button) findViewById(R.id.buttonMinus);
        buttonDivision = (Button) findViewById(R.id.buttonDivision);
        buttonMultiply = (Button) findViewById(R.id.buttonMultiply);
        buttonPercent = (Button) findViewById(R.id.buttonPercent);
        buttonDot = (Button) findViewById(R.id.buttonDot);

        buttonTenX = (Button) findViewById(R.id.buttonTenX);    //10^x
        buttonEx = (Button) findViewById(R.id.buttonEx);        //e^x
        buttonXy = (Button) findViewById(R.id.buttonXy);        //x^y
        buttonX2 = (Button) findViewById(R.id.buttonX2);        //x^2
        buttonX3 = (Button) findViewById(R.id.buttonX3);        //x^3
        buttonFraction = (Button) findViewById(R.id.buttonFraction);    // 1/x
        buttonRoot = (Button) findViewById(R.id.buttonRoot);
        buttonRoot3 = (Button) findViewById(R.id.buttonRoot3);
        buttonRooty = (Button) findViewById(R.id.buttonRooty);
        buttonLn = (Button) findViewById(R.id.buttonLn);
        buttonLog = (Button) findViewById(R.id.buttonLog);
        buttonEE = (Button) findViewById(R.id.buttonEE);
        buttonE = (Button) findViewById(R.id.buttonE);
        buttonSin = (Button) findViewById(R.id.buttonSin);
        buttonCos = (Button) findViewById(R.id.buttonCos);
        buttonTan = (Button) findViewById(R.id.buttonTan);
        buttonFactorial = (Button) findViewById(R.id.buttonFactorial);
        buttonRadian = (Button) findViewById(R.id.buttonRadian);
        buttonSinh = (Button) findViewById(R.id.buttonSinh);
        buttonCosh = (Button) findViewById(R.id.buttonCosh);
        buttonTanh = (Button) findViewById(R.id.buttonTanh);
        buttonRand = (Button) findViewById(R.id.buttonRand);
        buttonPi = (Button) findViewById(R.id.buttonPi);

        buttonMC = (Button) findViewById(R.id.buttonMemoryClear);
        buttonMP = (Button) findViewById(R.id.buttonMemoryPlus);
        buttonMM = (Button) findViewById(R.id.buttonMemoryMinus);
        buttonMR = (Button) findViewById(R.id.buttonMemoryRead);

        buttonPrtStart = (Button) findViewById(R.id.buttonParenthesesStart);    // (
        buttonPrtEnd = (Button) findViewById(R.id.buttonParenthesesEnd);        // )

        // API 21은 xml serif가 안먹힘
        buttonNum[0].setTypeface(Typeface.SERIF);
        buttonNum[1].setTypeface(Typeface.SERIF);
        buttonNum[2].setTypeface(Typeface.SERIF);
        buttonNum[3].setTypeface(Typeface.SERIF);
        buttonNum[4].setTypeface(Typeface.SERIF);
        buttonNum[5].setTypeface(Typeface.SERIF);
        buttonNum[6].setTypeface(Typeface.SERIF);
        buttonNum[7].setTypeface(Typeface.SERIF);
        buttonNum[8].setTypeface(Typeface.SERIF);
        buttonNum[9].setTypeface(Typeface.SERIF);

        buttonAC.setTypeface(Typeface.SERIF);
        buttonEqual.setTypeface(Typeface.SERIF);
        buttonPlusMinus.setTypeface(Typeface.SERIF);
        buttonPlus.setTypeface(Typeface.SERIF);
        buttonMinus.setTypeface(Typeface.SERIF);
        buttonDivision.setTypeface(Typeface.SERIF);
        buttonMultiply.setTypeface(Typeface.SERIF);
        buttonPercent.setTypeface(Typeface.SERIF);
        buttonDot.setTypeface(Typeface.SERIF);

        if(landscapeMode) {
            buttonTenX.setTypeface(Typeface.SERIF);
            buttonEx.setTypeface(Typeface.SERIF);
            buttonXy.setTypeface(Typeface.SERIF);
            buttonX2.setTypeface(Typeface.SERIF);
            buttonX3.setTypeface(Typeface.SERIF);
            buttonFraction.setTypeface(Typeface.SERIF);
            buttonRoot.setTypeface(Typeface.SERIF);
            buttonRoot3.setTypeface(Typeface.SERIF);
            buttonRooty.setTypeface(Typeface.SERIF);
            buttonLn.setTypeface(Typeface.SERIF);
            buttonLog.setTypeface(Typeface.SERIF);
            buttonEE.setTypeface(Typeface.SERIF);
            buttonE.setTypeface(Typeface.SERIF);
            buttonSin.setTypeface(Typeface.SERIF);
            buttonCos.setTypeface(Typeface.SERIF);
            buttonTan.setTypeface(Typeface.SERIF);
            buttonFactorial.setTypeface(Typeface.SERIF);
            buttonRadian.setTypeface(Typeface.SERIF);
            buttonSinh.setTypeface(Typeface.SERIF);
            buttonCosh.setTypeface(Typeface.SERIF);
            buttonTanh.setTypeface(Typeface.SERIF);
            buttonRand.setTypeface(Typeface.SERIF);
            buttonPi.setTypeface(Typeface.SERIF);

            buttonMC.setTypeface(Typeface.SERIF);
            buttonMP.setTypeface(Typeface.SERIF);
            buttonMM.setTypeface(Typeface.SERIF);
            buttonMR.setTypeface(Typeface.SERIF);

            buttonPrtStart.setTypeface(Typeface.SERIF);
            buttonPrtEnd.setTypeface(Typeface.SERIF);
        }


        // 세로 가로모드를 전활할 때 허용 글자수를 초과하면 지수표기법으로 표시
        if(((!landscapeMode && (cnt.length() > 10))) || (landscapeMode && (cnt.length() > 16))){
            if(!landscapeMode && (Double.parseDouble(cnt) < 0)) {  // 세로모드에서 11글자 이상에 음수면 지수표기
                cntTemp = String.format("%.4E",Double.parseDouble(cnt));
            }
            else {
                cntTemp = String.format("%.4E", Double.parseDouble(cnt));
                // 지수표기 법에서 E-4미만일 경우 지수표기법이 아닌 일반표기로 출력
                if((Integer.parseInt(cntTemp.substring(cntTemp.length()-2, cntTemp.length())))<4){
                    if(!landscapeMode) {
                        cntTemp = cnt.substring(0, 10);
                    }
                    else {
                        cntTemp = cnt.substring(0, 16);
                    }
                }
            }
            textView.setText(cntTemp);
        }
        else{
            textView.setText(cnt);
        }
        buttonAC.setText(ac);

    }

    /**
     *  숫자버튼(number button)
     */
    public void onClick_Num(View v){
        if(number.equals("0")) {
            number="";
        }else if(number.equals("-0")){
            number = "-";
        }
        if(caled){  // 계산 후(=) 숫버자튼을 눌렀을 시 number초기화
            number = "";
            tempFlag= true; // 계산 후(=) 연산자를 눌렀을 시에 대한 플레그
            prtFlag = false;
            prtEndFlag = false;
            prtTemp = "";
            caled = false;
        }
        if(((landscapeMode && number.length()<16) || (!landscapeMode && number.length()<10))&& !number.contains("E")) {
            switch (v.getId()) {
                case R.id.button0:
                case R.id.button10:
                    number += "0";
                    break;
                case R.id.button1:
                    number += "1";
                    break;
                case R.id.button2:
                    number += "2";
                    break;
                case R.id.button3:
                    number += "3";
                    break;
                case R.id.button4:
                    number += "4";
                    break;
                case R.id.button5:
                    number += "5";
                    break;
                case R.id.button6:
                    number += "6";
                    break;
                case R.id.button7:
                    number += "7";
                    break;
                case R.id.button8:
                    number += "8";
                    break;
                case R.id.button9:
                    number += "9";
                    break;
                case R.id.buttonDot:
                    if (number.isEmpty()) number = "0.";
                    else if (number.contains(".")) break;
                    else number += ".";
                    break;
            }
            textView.setText(number);//f.format(Double.parseDouble(number)));
            playSound();
            buttonAC.setText("C");
            cnt = number;
        }
    }
    /* 옵션 */
    public void onClick_Option(View v){
        switch (v.getId()) {
            case R.id.buttonAC:
                number = "0";
                operator = '0';
                answer = "0";
                temp = "0";
                buttonAC.setText("AC");
                caled = false;
                errorFlag = false;
                prtFlag = false;
                prtEndFlag =false;
                prtOperator = '0';
                prtFlagNum = 0;
                prtTemp = "";
                if(landscapeMode) {
                    buttonPrtStart.setTextColor(Color.BLACK);
                    buttonPrtEnd.setTextColor(Color.BLACK);
                }
                break;
            case R.id.buttonPlusMinus:
                answer = textView.getText().toString();
                if (answer.charAt(0) == '-') {
                    answer = answer.replaceFirst("-", "");
                    number = number.replaceFirst("-", "");
                } else {
                    answer = "-" + answer;
                    number = "-" + number;
                }
                break;
            case R.id.buttonPercent:
                dcmalA = new BigDecimal(textView.getText().toString());
                dcmalB = new BigDecimal("100");
                answer = ""+Double.parseDouble(String.valueOf(dcmalA.divide(dcmalB,99, RoundingMode.HALF_UP)));
                break;
            case R.id.buttonE:
                answer = String.valueOf(Math.exp(1));
                break;
            case R.id.buttonPi:
                answer = String.valueOf(Math.PI);
                break;
            case R.id.buttonRand:
                answer = String.valueOf(Math.random());
                break;
            default:
                break;
        }textView.setText(answer);
        cnt = answer;
        playSound();
    }
    /* 두번째 페이지 */
    public void onClick_Second(View v){
        if (!secondFlag) {
            buttonEx.setText("xᵉ");
            buttonTenX.setText("2ˣ");
            buttonLn.setText("log1p");
            buttonLog.setText("log₂");
            buttonSin.setText("sin⁻¹");
            buttonCos.setText("cos⁻¹");
            buttonTan.setText("tan⁻¹");
            buttonSinh.setText("sinh⁻¹");
            buttonCosh.setText("cosh⁻¹");
            buttonTanh.setText("tanh⁻¹");
            secondFlag = true;
        } else {
            buttonEx.setText("eˣ");
            buttonTenX.setText("10ˣ");
            buttonLn.setText("ln");
            buttonLog.setText("log");
            buttonSin.setText("sin");
            buttonCos.setText("cos");
            buttonTan.setText("tan");
            buttonSinh.setText("sinh");
            buttonCosh.setText("cosh");
            buttonTanh.setText("tanh");
            secondFlag = false;
        }
        playSound();
    }
    /* 라디안 */
    public void onClick_RAD(View v){
        if(!radFlag){
            radText.setText("Rad ");
            buttonRadian.setText("Deg");
            radFlag = true;
        }
        else{
            radText.setText("");
            buttonRadian.setText("Rad");
            radFlag = false;
        }
        playSound();
    }
    /* 연산자에 따른 계산 부분 */
    public void onClick_Calc(View v){
        playSound();

        if(tempFlag){   // 계산 후(=) 숫자버를을 눌렀을 시 temp값 초기화
            temp = "0";
            tempFlag = false;
        }

        if(operator != '0' && !caled && !number.equals("")) {     //연속된 연산자 버튼터치에 대한 수식(ex 1+1+ -> 2)
            dcmalA = new BigDecimal(temp);
            dcmalB = new BigDecimal(number);
            switch (operator) {
                case '+':
                    if(prtFlag && !prtTemp.equals("") && prtEndFlag){
                        temp = textView.getText().toString();
                        answer = String.valueOf(Double.parseDouble(temp)+Double.parseDouble(prtTemp));
                    }
                    else answer = String.valueOf(Double.parseDouble(temp) + Double.parseDouble(number));
                    break;
                case '-':
                    if(prtFlag && !prtTemp.equals("") && prtEndFlag){
                        temp = textView.getText().toString();
                        answer = String.valueOf(Double.parseDouble(prtTemp)-Double.parseDouble(temp));
                    }
                    else answer = String.valueOf(Double.parseDouble(temp) - Double.parseDouble(number));
                    break;
                case '*':
                    if(prtFlag && !prtTemp.equals("") && prtEndFlag){
                        temp = textView.getText().toString();
                        dcmalB = new BigDecimal(prtTemp);
                        dcmalA = new BigDecimal(temp);
                        answer = ""+Double.parseDouble(String.valueOf(dcmalA.multiply(dcmalB)));
                    }
                    else answer = ""+Double.parseDouble(String.valueOf(dcmalA.multiply(dcmalB)));
                    break;
                case '/':
                    if(prtFlag && !prtTemp.equals("") && prtEndFlag) {
                        temp = textView.getText().toString();
                        dcmalB = new BigDecimal(prtTemp);
                        dcmalA = new BigDecimal(temp);
                        try {
                            answer = ""+Double.parseDouble(String.valueOf(dcmalB.divide(dcmalA, 99, BigDecimal.ROUND_HALF_UP)));
                        }catch (Exception e){
                            number = "";
                            operator = '0';
                            answer = "0";
                            temp = "0";
                            errorFlag = true;
                            prtFlag = false;
                            prtOperator = '0';
                            prtFlagNum = 0;
                            prtTemp = "";
                        }
                    }
                    else {
                        try {
                            answer = ""+Double.parseDouble(String.valueOf(dcmalA.divide(dcmalB, 99, BigDecimal.ROUND_HALF_UP)));
                        } catch (Exception e) {
                            number = "";
                            operator = '0';
                            answer = "0";
                            temp = "0";
                            errorFlag = true;
                        }
                    }
                    break;
                case 'x':   // x^y
                    if(prtFlag && !prtTemp.equals("") && prtEndFlag){
                        temp = textView.getText().toString();
                        answer = String.valueOf(Math.pow(Double.parseDouble(prtTemp), Double.parseDouble(temp)));
                    }
                    else answer = String.valueOf(Math.pow(Double.parseDouble(temp), Double.parseDouble(number)));
                    break;
                case 'r':
                    if(prtFlag && !prtTemp.equals("") && prtEndFlag) {
                        try {
                            answer = String.valueOf(Math.pow(Double.parseDouble(prtTemp), 1 / Double.parseDouble(temp)));
                        } catch (Exception e) {
                            number = "";
                            operator = '0';
                            answer = "0";
                            temp = "0";
                            errorFlag = true;
                            prtFlag = false;
                            prtOperator = '0';
                            prtFlagNum = 0;
                            prtTemp = "";
                        }
                    }
                    else {
                        try {
                            answer = String.valueOf(Math.pow(Double.parseDouble(temp), 1 / Double.parseDouble(number)));
                        } catch (Exception e) {
                            number = "";
                            operator = '0';
                            answer = "0";
                            temp = "0";
                            errorFlag = true;
                        }
                    }
                    break;
                case 'e':
                    if(prtFlag && !prtTemp.equals("") && prtEndFlag){
                        temp = textView.getText().toString();
                        dcmalA = new BigDecimal(prtTemp);
                        if(Integer.parseInt(temp)<300) {
                            dcmalC = new BigDecimal(Math.pow(10, Double.parseDouble(temp)));
                            answer = ""+Double.parseDouble(String.valueOf(dcmalA.multiply(dcmalC)));
                        }
                        else errorFlag = true;
                    }
                    else{
                        if(Integer.parseInt(number)<300) {
                            dcmalC = new BigDecimal(Math.pow(10, Double.parseDouble(number)));
                            answer = ""+Double.parseDouble(String.valueOf(dcmalA.multiply(dcmalC)));
                        }
                        else errorFlag = true;
                    }
                    break;
                default:
                    break;
            }
            if(prtEndFlag){     //괄호 닫기 플레그
                prtFlag = false;
                prtEndFlag = false;
            }
            answerText();
        }

        temp = textView.getText().toString();

        switch (v.getId()){
            case R.id.buttonPlus:
                operator = '+';
                break;
            case R.id.buttonMinus:
                operator = '-';
                break;
            case R.id.buttonMultiply:
                operator = '*';
                break;
            case R.id.buttonDivision:
                operator = '/';
                break;
            case R.id.buttonXy:
                operator = 'x';
                break;
            case R.id.buttonRooty:
                operator = 'r';
                break;
            case R.id.buttonEE:
                operator = 'e';
                break;
            default:
                break;
        }
        number = "";
    }
    /* 계산식에 따른 "="에 대한 출력 */
    public void onClick_Answer(View v){
        playSound();
        dcmalA = new BigDecimal(temp);
        if(!number.isEmpty()) dcmalB = new BigDecimal(number);
        switch(operator){
            case '+':
                if(prtFlag && !prtTemp.equals("") && prtEndFlag){
                    temp = textView.getText().toString();
                    temp = String.valueOf(Double.parseDouble(temp)+Double.parseDouble(prtTemp));
                }
                else temp = String.valueOf(Double.parseDouble(temp)+Double.parseDouble(number));
                answer= temp;
                answerText();
                break;
            case '-':
                if(prtFlag && !prtTemp.equals("") && prtEndFlag){
                    temp = textView.getText().toString();
                    temp = String.valueOf(Double.parseDouble(prtTemp)-Double.parseDouble(temp));
                }
                else temp = String.valueOf(Double.parseDouble(temp)-Double.parseDouble(number));
                answer= temp;
                answerText();
                break;
            case '*':
                if(prtFlag && !prtTemp.equals("") && prtEndFlag){
                    temp = textView.getText().toString();
                    dcmalB = new BigDecimal(prtTemp);
                    dcmalA = new BigDecimal(temp);
                    temp = String.valueOf(dcmalA.multiply(dcmalB));
                }
                else temp = String.valueOf(dcmalA.multiply(dcmalB));
                answer= ""+Double.parseDouble(temp);
                answerText();
                break;
            case '/':
                if(prtFlag && !prtTemp.equals("") && prtEndFlag) {
                    temp = textView.getText().toString();
                    dcmalB = new BigDecimal(prtTemp);
                    dcmalA = new BigDecimal(temp);
                    try {
                        temp = String.valueOf(dcmalB.divide(dcmalA, 99, BigDecimal.ROUND_HALF_UP));
                    }catch (Exception e){
                        number = "";
                        operator = '0';
                        answer = "0";
                        temp = "0";
                        errorFlag = true;
                        prtFlag = false;
                        prtOperator = '0';
                        prtFlagNum = 0;
                        prtTemp = "";
                    }
                }
                else {
                    try {
                        temp = String.valueOf(dcmalA.divide(dcmalB, 99, BigDecimal.ROUND_HALF_UP));
                    } catch (Exception e) {
                        number = "";
                        operator = '0';
                        answer = "0";
                        temp = "0";
                        errorFlag = true;
                    }
                }
                answer = ""+Double.parseDouble(temp);
                answerText();
                break;
            case 'x':
                if(prtFlag && !prtTemp.equals("") && prtEndFlag){
                    temp = textView.getText().toString();
                    temp = String.valueOf(Math.pow(Double.parseDouble(prtTemp), Double.parseDouble(temp)));
                }
                else temp = String.valueOf(Math.pow(Double.parseDouble(temp), Double.parseDouble(number)));
                answer = temp;
                answerText();
                break;
            case 'r':
                if(prtFlag && !prtTemp.equals("") && prtEndFlag) {
                    try {
                        temp = String.valueOf(Math.pow(Double.parseDouble(prtTemp), 1 / Double.parseDouble(temp)));
                    } catch (Exception e) {
                        number = "";
                        operator = '0';
                        answer = "0";
                        temp = "0";
                        errorFlag = true;
                        prtFlag = false;
                        prtOperator = '0';
                        prtFlagNum = 0;
                        prtTemp = "";
                    }
                }
                else {
                    try {
                        temp = String.valueOf(Math.pow(Double.parseDouble(temp), 1 / Double.parseDouble(number)));
                    } catch (Exception e) {
                        number = "";
                        operator = '0';
                        answer = "0";
                        temp = "0";
                        errorFlag = true;
                    }
                }
                answer = temp;
                answerText();
                break;
            case 'e':
                if(prtFlag && !prtTemp.equals("") && prtEndFlag){
                    temp = textView.getText().toString();
                    if(Integer.parseInt(temp)<300) {
                        dcmalA = new BigDecimal(prtTemp);
                        dcmalC = new BigDecimal(Math.pow(10, Double.parseDouble(temp)));
                        temp = String.valueOf(dcmalA.multiply(dcmalC));
                    }
                    else errorFlag = true;
                }
                else {
                    if(Integer.parseInt(number)<300) {
                        dcmalC = new BigDecimal(Math.pow(10, Double.parseDouble(number)));
                        temp = String.valueOf(dcmalA.multiply(dcmalC));
                    }
                    else errorFlag = true;
                }
                answer =""+Double.parseDouble(temp);
                answerText();
                break;
            default:
                break;
        }
        caled = true;
    }
    /*한 번의 터치로만 계산되는 함수 (just one touch function)*/
    public void onClick_Calc2(View v){
        playSound();
        switch (v.getId()){
            case R.id.buttonTenX:
                answer = textView.getText().toString();
                if(!secondFlag)
                    answer = String.valueOf(Math.pow(10,Double.parseDouble(answer)));
                else
                    answer = String.valueOf(Math.pow(2,Double.parseDouble(answer)));
                break;
            case R.id.buttonEx:
                answer = textView.getText().toString();
                if(!secondFlag)
                    answer = String.valueOf(Math.exp(Double.parseDouble(answer)));
                else
                    answer = String.valueOf(Math.pow(Double.parseDouble(answer),Math.exp(1)));
                break;
            case R.id.buttonX2:
                answer = textView.getText().toString();
                answer = String.valueOf(Math.pow(Double.parseDouble(answer),2));
                break;
            case R.id.buttonX3:
                answer = textView.getText().toString();
                answer = String.valueOf(Math.pow(Double.parseDouble(answer),3));
                break;
            case R.id.buttonFraction:
                answer = textView.getText().toString();
                answer = String.valueOf(1/Double.parseDouble(answer));
                break;
            case R.id.buttonRoot:
                answer = textView.getText().toString();
                answer = String.valueOf(Math.sqrt(Double.parseDouble(answer)));
                break;
            case R.id.buttonRoot3:
                answer = textView.getText().toString();
                answer = String.valueOf(Math.cbrt(Double.parseDouble(answer)));
                break;
            case R.id.buttonLn:
                answer = textView.getText().toString();
                if(!secondFlag)
                    answer = String.valueOf(Math.log(Double.parseDouble(answer)));
                else
                    answer = String.valueOf(Math.log1p(Double.parseDouble(answer)));
                break;
            case R.id.buttonLog:
                answer = textView.getText().toString();
                if(!secondFlag)
                    answer = String.valueOf(Math.log10(Double.parseDouble(answer)));
                else
                    answer = String.valueOf(Math.log(Double.parseDouble(answer))/Math.log(2));
                break;
            case R.id.buttonFactorial:
                answer = textView.getText().toString();
                if(Double.parseDouble(answer) % 1 == 0)
                    answer = String.valueOf(factorial(Double.parseDouble(answer)));
                else errorFlag = true;
                break;
            case R.id.buttonSin:
                answer = textView.getText().toString();
                if(radFlag) {   //라디안입력
                    if (!secondFlag)
                        answer = String.valueOf(Math.sin(Double.parseDouble(answer)));
                    else
                        answer = String.valueOf(Math.asin(Double.parseDouble(answer)));
                }
                else{   //디그리 입력
                    if (!secondFlag)
                        answer = String.valueOf(Math.sin(Double.parseDouble(answer)* Math.PI/180.0));
                    else
                        answer = String.valueOf(Math.asin(Double.parseDouble(answer))* 180.0 / Math.PI);
                }
                break;
            case R.id.buttonCos:
                answer = textView.getText().toString();
                if(radFlag) {
                    if (!secondFlag)
                        answer = String.valueOf(Math.cos(Double.parseDouble(answer)));
                    else
                        answer = String.valueOf(Math.acos(Double.parseDouble(answer)));
                }
                else{   //디그리 입력
                    if (!secondFlag)
                        answer = String.valueOf(Math.cos(Double.parseDouble(answer)* Math.PI/180.0));
                    else
                        answer = String.valueOf(Math.acos(Double.parseDouble(answer)* 180.0 / Math.PI));
                }
                break;
            case R.id.buttonTan:
                answer = textView.getText().toString();
                if(radFlag) {
                    if (!secondFlag)
                        answer = String.valueOf(Math.tan(Double.parseDouble(answer)));
                    else
                        answer = String.valueOf(Math.atan(Double.parseDouble(answer)));
                }
                else{   //디그리 입력
                    if (!secondFlag)
                        answer = String.valueOf(Math.tan(Double.parseDouble(answer)* Math.PI/180.0));
                    else
                        answer = String.valueOf(Math.atan(Double.parseDouble(answer)* 180.0 / Math.PI));
                }
                break;
            case R.id.buttonSinh:
                answer = textView.getText().toString();
                if(!secondFlag)
                    answer = String.valueOf(Math.sinh(Double.parseDouble(answer)));
                else
                    answer = String.valueOf(Math.log(Double.parseDouble(answer) +
                            Math.sqrt(Double.parseDouble(answer) * Double.parseDouble(answer) + 1.0)));
                break;
            case R.id.buttonCosh:
                answer = textView.getText().toString();
                if(!secondFlag)
                    answer = String.valueOf(Math.sinh(Double.parseDouble(answer)));
                else
                    answer = String.valueOf(Math.log(Double.parseDouble(answer) +
                            Math.sqrt(Double.parseDouble(answer) * Double.parseDouble(answer) - 1.0)));
                break;
            case R.id.buttonTanh:
                answer = textView.getText().toString();
                if(!secondFlag)
                    answer = String.valueOf(Math.tanh(Double.parseDouble(answer)));
                else
                    answer = String.valueOf(0.5 *
                            Math.log((Double.parseDouble(answer) + 1.0) / ((Double.parseDouble(answer)) - 1.0)));
                break;
            default:
                break;

        }
        answerText();
        number = "";
    }
    /* 메모리 부분 (Memory) */
    public void onClick_Memory(View v){
        playSound();
        switch(v.getId()){
            case R.id.buttonMemoryClear:
                memory = 0;
                buttonMR.setText("mr");
                break;
            case R.id.buttonMemoryPlus:
                memory += Double.parseDouble(textView.getText().toString());
                number="0";
                buttonMR.setText("MR");
                break;
            case R.id.buttonMemoryMinus:
                memory -= Double.parseDouble(textView.getText().toString());
                number="0";
                buttonMR.setText("MR");
                break;
            case R.id.buttonMemoryRead:
                if(memory % 1 == 0){
                    textView.setText(""+(long)memory);//String.format("%.0f",Double.parseDouble(answer)));
                    number = ""+(long)memory;
                }
                else{
                    textView.setText(""+memory);
                    number = ""+memory;
                }
                caled =true;
                break;
            default:
                break;
        }
    }
    /* 괄호 처리 (Paraenthsese)*/
    public void onClick_Parenthses(View v) {
        playSound();
        switch (v.getId()) {
            case R.id.buttonParenthesesStart:
                if (prtFlagNum == 0) prtOperator = operator;
                if (prtOperator != '0') prtTemp = textView.getText().toString();
                temp = "0";
                number = "0";
                answer = "0";
                operator = '0';
                prtFlagNum++;
                prtFlag = true;
                buttonPrtStart.setTextColor(Color.BLUE);
                buttonPrtEnd.setTextColor(Color.BLUE);
                break;
            case R.id.buttonParenthesesEnd:
                if (prtFlagNum > 0) {
                    dcmalA = new BigDecimal(temp);
                    dcmalB = new BigDecimal(number);
                    switch (operator) {
                        case '+':
                            answer = String.valueOf(Double.parseDouble(temp) + Double.parseDouble(number));
                            break;
                        case '-':
                            answer = String.valueOf(Double.parseDouble(temp) - Double.parseDouble(number));
                            break;
                        case '*':
                            answer = ""+Double.parseDouble(String.valueOf(dcmalA.multiply(dcmalB)));
                            break;
                        case '/':
                            try {
                                answer = ""+Double.parseDouble(String.valueOf(dcmalA.divide(dcmalB, 99, BigDecimal.ROUND_HALF_UP)));
                            } catch (Exception e) {
                                number = "";
                                operator = '0';
                                answer = "0";
                                temp = "0";
                                errorFlag = true;
                            }
                            break;
                        case 'x':   // x^y
                            answer = String.valueOf(Math.pow(Double.parseDouble(temp), Double.parseDouble(number)));
                            break;
                        case 'r':
                            try {
                                answer = String.valueOf(Math.pow(Double.parseDouble(temp), 1 / Double.parseDouble(number)));
                            } catch (Exception e) {
                                number = "";
                                operator = '0';
                                answer = "0";
                                temp = "0";
                                errorFlag = true;
                            }
                            break;
                        case 'e':
                            if(Integer.parseInt(number)<300) {
                                dcmalC = new BigDecimal(Math.pow(10, Double.parseDouble(number)));
                                answer = ""+Double.parseDouble(String.valueOf(dcmalA.multiply(dcmalC)));
                            }
                            else errorFlag = true;
                            break;
                        default:
                            break;
                    }
                    answerText();
                    prtFlagNum--;
                }
                if (prtFlagNum == 0 && prtOperator != '0') {
                    operator = prtOperator;
                    prtOperator = '0';
                    prtEndFlag = true;
                }
                if (prtFlagNum == 0) {
                    buttonPrtStart.setTextColor(Color.BLACK);
                    buttonPrtEnd.setTextColor(Color.BLACK);
                }
                break;
            default:
                break;
        }
    }


    public double factorial(double i){
        if(i==0 || i==1) return 1;
        else{
            return (i*factorial(i-1));
        }
    }

    /**
     * 에러 유무 및 정수 소수 판별 최종 출력 값
     */
    public void answerText(){
        if(errorFlag){
            textView.setText("Error");
            errorFlag = false;
        }
        else{ //세로모드 10자리 이상 더하기시 지수표기법, 가로세로 전환 시 지수표기법일때 숫자 타이핑 안 되게하기("E"포함 안 될때 쳐지게하기)
            if(Double.parseDouble(answer) % 1 == 0 && (Double.parseDouble(answer)<=Long.MAX_VALUE) && (Double.parseDouble(answer)>=Long.MIN_VALUE)){    // 출력 값이 정수 일 때(long의 최대최소값범위내)
                cnt = "" + (long) Double.parseDouble(answer);
                answer = cnt;
                // 가로모드(16자 초과)이거나 세로모드(10자 초과)일 때 지수 표기법으로 출력(음수일땐 11자)
                if((((answer).length() > 16) && landscapeMode) || ((((answer.length() > 10) && (answer.charAt(0) != '-')) || ((answer.length() > 11) && (answer.charAt(0) == '-'))) && !landscapeMode)) {
                    answer = String.format("%.4E",Double.parseDouble(answer));
                }
                textView.setText(answer);
            }
            else{
                cnt = answer;
                // 가로모드(16자 초과)이거나 세로모드(10자 초과)일 때 지수 표기법으로 츨력
                if((((answer).length() > 16) && landscapeMode) || ((((answer.length() > 10) && (answer.charAt(0) != '-')) || ((answer.length() > 11) && (answer.charAt(0) == '-'))) && !landscapeMode)) {
                    answer = String.format("%.4E",Double.parseDouble(answer));
                    // 지수표기법이 E-4까지는 그냥 지수 표기법이 아닌 일반 표기법으로 출력
                    if(Integer.parseInt(answer.substring(answer.length()-2,answer.length())) < 4){
                        if(!landscapeMode)
                            answer = cnt.substring(0, 10);
                        else
                            answer = cnt.substring(0, 16);
                    }
                }
                textView.setText(answer);
            }
        }

    }
    @Override
    protected void onSaveInstanceState(Bundle outState){    //화면 가로세로 전환 시 값 유지
        super.onSaveInstanceState(outState);
        ac = buttonAC.getText().toString();
        outState.putString("num",cnt);
        outState.putString("ac", ac);
    }
}