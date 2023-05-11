// 이곳에 문제 생성 알고리즘을 넣어두고, ProblemSerice에서 필요한 method를 호출하면 될 듯 싶습니다..

package com.onejo.seosuri.service.algorithm;

import java.util.Random;

/*
템플릿 생성 완료

남은 일
0. 난이도 기준 마련
    - 템플릿 난이도
        상황 문장 갯수

    - 숫자 난이도
1. 랜덤 숫자 뽑기
2. 숫자에 맞는 단어 뽑기
3. 템플릿 -> 실제 문장으로 변경

 */

public class Elementary5th {
    Random random;
    static final int PLUS_SIGN = 0;
    static final int MINUS_SIGN = 1;

    // 상황 문장 id
    static final int AGE_CATEGORY_ID_YX = 0;
    static final int AGE_CATEGORY_ID_SUM_DIFFERENCE = 1;

    // 변수명 string 규칙
    static final String AGE_STR = "age";    // age 변수 : {age0}, {age1}, {age2}, ...
    static final String NAME_STR = "name";  // name 변수 : {name0}, {name1}, {name2}, ...
    static final String VAR_STR = "var";    // 상수 변수 : {var0}, {var1}, {var2}, ...
    static final String YEAR_STR = "year";  // year 변수
    static final String VAR_START = "{";
    static final String VAR_END = "}";
    static final String EXPRESSION_START = "[";
    static final String EXPRESSION_END = "]";
    static final String PLUS_SYM = "+";
    static final String MINUS_SYM = "-";
    static final String MULT_SYM = "*";
    static final String DIVIDE_SYM = "/";
    static final String EQUAL_BLANK_SYM = " = ";
    static final String PLUS_BLANK_STR = " + ";
    static final String MINUS_BLANK_STR = " - ";
    static final String MULT_BLANK_STR = " * ";
    static final String DIVIDE_BLANK_STR = " / ";

    // 나이 구하기 문제 알고리즘
    public void ageProblem() {
        ////////////////////////////////////////////////////////////////////////////////////////////////
        // 값 설정
        random = new Random(); //랜덤 객체 생성(디폴트 시드값 : 현재시간)
        random.setSeed(System.currentTimeMillis()); //시드값 설정을 따로 할수도 있음
        int prob_sentence_num = 5;  // 상황 문장 갯수 - 문제 난이도에 따라 값 달라짐
        int sentence_category_num = 2;  // 상황 문장 유형 갯수

        int var_num_per_sentence = 2;
        int[] age_ls = new int[prob_sentence_num + 1];  // 상황문장 1개일 때 age 변수는 2개, 상황 문장 하나 추가될 때마다 age 변수 1개씩 추가됨
        int[] var_ls = new int[prob_sentence_num * var_num_per_sentence];  // yx유형문장은 상수 변수 2개, 합차유형문장은 상수 변수 1개
        int[] sentence_category_id_ls = new int[prob_sentence_num];    // 각 상황문장이 어떤 유형의 문장인지를 저장한 배열
        for(int i = 0; i < sentence_category_id_ls.length; i++){
            sentence_category_id_ls[i] = random.nextInt(sentence_category_num);
        }

        int answer_inx = random.nextInt(age_ls.length);  // 구하는 나이의 인덱스
        int condition_inx = (random.nextInt(age_ls.length-1) + answer_inx) % age_ls.length;   // 조건으로 값이 주어진 나이의 인덱스, answer_inx와 다른 인덱스가 되도록 설정

        ////////////////////////////////////////////////////////////////////////////////////////////////
        // 문장 생성

        // condition 문장 생성
        String condition = "이때, {"+NAME_STR+condition_inx+"}의 나이는 {"+AGE_STR+condition_inx+"}살 입니다.";

        // question 문장 생성
        String question = "그렇다면 {"+NAME_STR+answer_inx+"}의 나이는 몇 살입니까?";

        // answer 문장 생성
        String answer = "{"+AGE_STR+answer_inx+"}살";

        // 상황 문장 생성 : {content, explanation}
        String[][] sentence_ls = new String[prob_sentence_num][2];
        int cond_inx_for_sentence = 1;  // age1, age2 중 age2가 given
        if(prob_sentence_num==1)    cond_inx_for_sentence = condition_inx;
        for(int i = 0; i < prob_sentence_num; i++){
            // create_age_sentence(유형id, 값 참조 시작하는 인덱스, answer_index, condition_index) ex) index = 2 -> name2, name3, age2, age3, var2, var3 사용
            sentence_ls[i] = create_age_sentence(sentence_category_id_ls[i], i , var_num_per_sentence, cond_inx_for_sentence);  // sentence_ls[i] = {content, explanation}
        }


        /////////////////////////////////////////////////////////////////////////////////////////////////////
        // 상황 문장 연결

        // content : sentences + condition + question
        String content = "";
        for(int i = 0; i < sentence_ls.length; i++){ // 상황 문장 content - 순서 섞으면 난이도 올라감
            content += sentence_ls[i][0] + "\n";
        }
        content = content + condition + question;

        // explanation : conditon_inx -> condition_inx + 1 -> ... -> 끝 index -> 1 -> 2 -> ... -> condition_inx - 1 순서로 연결
        String explanation = "";
        int start_index = (condition_inx + prob_sentence_num - 1) % prob_sentence_num;
        for(int i = start_index; i >= 0; i--){    // condition_inx - 1   ~   0
            explanation += sentence_ls[i][1] + "\n";   // 상황 문장 explanation
            System.out.println("i = " + i);
        }
        for(int i = sentence_ls.length - 1; i >= condition_inx; i--){                     // 끝   ~   condition_inx
            explanation += sentence_ls[i][1] + "\n";   // 상황 문장 exlanation
        }

        ////////////////////////////////////////////////////////////////////////////////////////////////////
        // 결과 : {문제, explanation, answer, prob_sentence_category_list}
        String[] result_ls = new String[] {content, explanation, answer, sentence_category_id_ls.toString()};

        System.out.println(content);
        System.out.println("------------------------------------");
        System.out.println(explanation);
        System.out.println("------------------------------------");
        System.out.println(answer);
        System.out.println("------------------------------");
        System.out.println(sentence_category_id_ls.toString());
    }

    // 이은 색테이프 문제 알고리즘
    public void colorTapeProblem() {}

    // 어떤 수 문제 알고리즘
    public void anyNumberProblem() {}

    // 도형에서의 혼합 계산 응용 알고리즘
    public void geometryCalculation() {}



    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // 나이 문제 - 상황문장 생성
    // create_age_sentence(유형id, 값 참조 시작하는 인덱스) ex) index = 2 -> name2, name3, age2, age3, var2, var3 사용
    private String[] create_age_sentence(int category_id, int index, int var_num_per_sentence, int cond_inx){
        boolean useYear=true;
        boolean useMult=true;
        boolean useAddMinus=true;
        int sign= PLUS_SIGN;
        if(category_id == AGE_CATEGORY_ID_YX){
            return create_age_sentence_yx(index, var_num_per_sentence, cond_inx, useYear, useMult, useAddMinus, sign);               // {content, explanation}
        } else if (category_id == AGE_CATEGORY_ID_SUM_DIFFERENCE){
            return create_age_sentence_sum_difference(index, var_num_per_sentence, cond_inx, sign);   // {content, explanation}
        } else{
            return null;
        }
    }

    /*
    // age1 + year = (age2 + year) * var1 +- var2
    // {year}년 후 {name1}의 나이는 {year}년 후 {name2}의 나이의 {var1}배한 것과 같습니다.    year_token +
    // {year}년 후 {name1}의 나이는 {year}년 후 {name2}의 나이의 {var1}배한 것보다 {var2}살 많습니다(적습니다).
    // {year}년 후 {name1}의 나이는 {year}년 후 {name2}의 나이보다 {var2}살 많습니다(적습니다).
    // year_token + name1_token + year_token + name2_token
        // mult_token + mult_end_token          : 곱셈만
        // mult_token + pm_token + add_token    : 곱셈 + 덧셈
        // mult_token + pm_token + minus_token  : 곱셈 + 뺄셈
        // pm_token + add_token                 : 덧셈만
        // pm_token + minus_token               : 뺄셈만
     */
    public String[] create_age_sentence_yx(int ls_index, int var_num_per_sentence, int cond_inx, boolean useYear, boolean useMult, boolean useAddMinus, int sign){
        // content token
        int index1 = ls_index;
        int index2 = ls_index + 1;
        int var_index1 = ls_index * var_num_per_sentence;
        int var_index2 = ls_index * var_num_per_sentence + 1;

        String name1_token = VAR_START + NAME_STR + index1 + VAR_END;
        String name2_token = VAR_START + NAME_STR + index2 + VAR_END;
        String name_to_age_token = "의 나이";
        String name1_age_token = name1_token + name_to_age_token;
        String name2_age_token = name2_token + name_to_age_token;
        String age1_token = VAR_START + AGE_STR + index1 + VAR_END;
        String age2_token = VAR_START + AGE_STR + index2 + VAR_END;
        String var1_token = VAR_START + VAR_STR + var_index1 + VAR_END;
        String var2_token = VAR_START + VAR_STR + var_index2 + VAR_END;
        String year_token = VAR_START + YEAR_STR + VAR_END;

        String sign_token = "", sign_blank_token = "";
        String inv_sign_token = "", inv_sign_blank_token = "";
        if(sign == PLUS_SIGN){
            sign_token = PLUS_SYM;  sign_blank_token = PLUS_BLANK_STR;
            inv_sign_token = MINUS_SYM; inv_sign_blank_token = MINUS_BLANK_STR;
        } else if(sign == MINUS_SIGN){
            sign_token = MINUS_SYM; sign_blank_token = MINUS_BLANK_STR;
            inv_sign_token = PLUS_SYM;  inv_sign_blank_token = PLUS_BLANK_STR;
        } else{
            System.out.println("ERROR:: SIGN VALUE ERROR");
            return null;
        }

        String year_past_token = year_token + "년 후 ";
        if(useYear == false){
            year_past_token = "";
        }

        String c_mult_token = "의 "+ var1_token +"배 한 것";
        String c_mult_end_token = "과 같습니다.";
        String c_mult_end_wa_token = "와 같습니다.";
        String c_pm_token = "보다 " + var2_token + "살 ";
        String c_add_token = "많습니다.";
        String c_minus_token = "적습니다.";

        // content
        String content = year_past_token + name1_age_token + "는 " + year_past_token + name2_age_token;   // {name1}의 나이는 {name2}의 나이
        if(useMult){
            if(useAddMinus == false){   // 의 {var1}배 한 것과 같습니다.
                content += c_mult_token + c_mult_end_token;
            } else if(sign == PLUS_SIGN){    // 의 {var1}배한 것보다 {var2}살 많습니다.
                content += c_mult_token + c_pm_token + c_add_token;
            } else if(sign == MINUS_SIGN){   // 의 {var1}배한 것보다 {var2}살 적습니다.
                content += c_mult_token + c_pm_token + c_minus_token;
            } else{
                content = "sign value error\n";
            }
        } else{
            if(useAddMinus == false){   // 와 같습니다.
                content += c_mult_end_wa_token;
            } else if(sign == PLUS_SIGN){   // 보다 {var2}살 많습니다.
                content += c_pm_token + c_add_token;
            } else if(sign == MINUS_SIGN){   // 보다 {var2}살 적습니다.
                content += c_pm_token + c_minus_token;
            } else{
                content = "sign value error\n";
            }
        }

        // explanation token
        /*
        String ex_age_after_year_token = year_past_token +"{"+NAME_STR+"%d}의 나이 = {"+AGE_STR+"%d} + {"+YEAR_STR+"} = [{"
                +AGE_STR+"%d}+{"+YEAR_STR+"}]";
        */
        String ex_expression_token = year_past_token + name1_age_token
                + EQUAL_BLANK_SYM + year_past_token + name2_age_token;
        if(useMult)
            ex_expression_token += MULT_BLANK_STR + var1_token;
        if(useAddMinus)
            ex_expression_token += sign_blank_token + var2_token;

        String ex_age1_after_year_token = year_past_token + name1_age_token
                + EQUAL_BLANK_SYM + age1_token + PLUS_BLANK_STR + year_token
                + EQUAL_BLANK_SYM + EXPRESSION_START + age1_token + PLUS_SYM + year_token + EXPRESSION_END;
        String ex_age2_after_year_token = year_past_token + name2_age_token
                + EQUAL_BLANK_SYM + age2_token + PLUS_BLANK_STR + year_token
                + EQUAL_BLANK_SYM + EXPRESSION_START + age2_token + PLUS_SYM + year_token + EXPRESSION_END;
        String ex_age1_after_year_same_age_token = EQUAL_BLANK_SYM + year_past_token + name2_age_token;
        String ex_age2_after_year_same_age_token = EQUAL_BLANK_SYM + year_past_token + name1_age_token;

        String ex_after_year_to_age1_token = name1_age_token
                + EQUAL_BLANK_SYM + year_past_token + name1_age_token + MINUS_BLANK_STR + year_token
                + EQUAL_BLANK_SYM + EXPRESSION_START + "(" + age2_token + PLUS_SYM + year_token + ")" + MULT_SYM + var1_token + sign_token + var2_token + EXPRESSION_END + MINUS_BLANK_STR + year_token
                + EQUAL_BLANK_SYM + EXPRESSION_START + "(" + age2_token + PLUS_SYM + year_token + ")" + MULT_SYM + var1_token + sign_token + var2_token + MINUS_SYM + year_token + EXPRESSION_END;
        String ex_after_year_to_age2_with_plus_sign_token = name2_age_token
                + EQUAL_BLANK_SYM + year_past_token + name2_age_token + MINUS_BLANK_STR + year_token
                + EQUAL_BLANK_SYM + EXPRESSION_START + "(" + age1_token + PLUS_SYM + year_token + MINUS_SYM + var2_token + ")" + DIVIDE_SYM + var1_token + EXPRESSION_END + MINUS_BLANK_STR + year_token
                + EQUAL_BLANK_SYM + EXPRESSION_START + "(" + age1_token + PLUS_SYM + year_token + MINUS_SYM + var2_token + ")" + DIVIDE_SYM + var1_token + MINUS_SYM + year_token + EXPRESSION_END;
        String ex_after_year_to_age2_with_minus_sign_token = name2_age_token
                + EQUAL_BLANK_SYM + year_past_token + name2_age_token + MINUS_BLANK_STR + year_token
                + EQUAL_BLANK_SYM + EXPRESSION_START + "(" + age1_token + PLUS_SYM + year_token + PLUS_SYM + var2_token + ")" + DIVIDE_SYM + var1_token + EXPRESSION_END + MINUS_BLANK_STR + year_token
                + EQUAL_BLANK_SYM + EXPRESSION_START + "(" + age1_token + PLUS_SYM + year_token + PLUS_SYM + var2_token + ")" + DIVIDE_SYM + var1_token + MINUS_SYM + year_token + EXPRESSION_END;

        // (%s%s의 나이) = (%s%s의 나이) * %d %s %d = %d * %d %s %d = %d
        // y년후 name1의 나이 = y년후 name2의나이 * var1 +- var2 = [age2+year] * var1 +- var2 = age1
        String ex_cond2_compute_with_plus_sign_token = year_past_token + name1_age_token
                + EQUAL_BLANK_SYM + year_past_token + name2_age_token + MULT_BLANK_STR + var1_token + sign_blank_token + var2_token
                + EQUAL_BLANK_SYM + EXPRESSION_START + age2_token + PLUS_SYM + year_token + EXPRESSION_END + MULT_BLANK_STR + var1_token + sign_blank_token + var2_token
                + EQUAL_BLANK_SYM + EXPRESSION_START + "(" + age2_token + PLUS_SYM + year_token + ")" + MULT_SYM + var1_token + sign_token + var2_token + EXPRESSION_END;
        String ex_cond2_compute_with_minus_sign_token = year_past_token + name1_age_token
                + EQUAL_BLANK_SYM + year_past_token + name2_age_token + MULT_BLANK_STR + var1_token + inv_sign_blank_token + var2_token
                + EQUAL_BLANK_SYM + EXPRESSION_START + age2_token + PLUS_SYM + year_token + EXPRESSION_END + MULT_BLANK_STR + var1_token + inv_sign_blank_token + var2_token
                + EQUAL_BLANK_SYM + EXPRESSION_START + "(" + age2_token + PLUS_SYM + year_token + ")" + MULT_SYM + var1_token + sign_token + var2_token + EXPRESSION_END;

        // if var2 != 0
        // (year년 후 name1의 나이) -+ var2 = age1 + year -+ var2
        String ex_cond1_compute_b4_divide_with_zero_var2_token = year_past_token + name2_age_token + EQUAL_BLANK_SYM;
        String ex_cond1_compute_b4_divide_with_plus_sign_token = year_past_token + name1_age_token + MINUS_BLANK_STR + var2_token
                + EQUAL_BLANK_SYM + EXPRESSION_START + age1_token + PLUS_SYM + year_token + MINUS_SYM + var2_token + EXPRESSION_END;
        String ex_cond1_compute_b4_divide_with_minus_sign_token = year_past_token + name1_age_token + PLUS_BLANK_STR + var2_token
                + EQUAL_BLANK_SYM + EXPRESSION_START + age1_token + PLUS_SYM + year_token + PLUS_SYM + var2_token + EXPRESSION_END;
        if(useAddMinus==false){
            ex_cond1_compute_b4_divide_with_plus_sign_token="";
            ex_cond1_compute_b4_divide_with_minus_sign_token="";
        }

        // if var1 != 1
        //"(%s%s의 나이) = {age1 + year -+ var2} / var1 = %d\n"
        String ex_cond1_compute_divide_with_plus_sign_token = year_past_token + name2_age_token
                + EQUAL_BLANK_SYM + EXPRESSION_START + age1_token + PLUS_SYM + year_token + MINUS_SYM + var2_token + EXPRESSION_END + DIVIDE_BLANK_STR + var1_token
                + EQUAL_BLANK_SYM + EXPRESSION_START + "(" + age1_token + PLUS_SYM + year_token + MINUS_SYM + var2_token + ")" + DIVIDE_SYM + var1_token + EXPRESSION_END;
        String ex_cond1_compute_divide_with_minus_sign_token = year_past_token + name2_age_token
                + EQUAL_BLANK_SYM + EXPRESSION_START + age1_token + PLUS_SYM + year_token + PLUS_SYM + var2_token + EXPRESSION_END + DIVIDE_BLANK_STR + var1_token
                + EQUAL_BLANK_SYM + EXPRESSION_START + "(" + age1_token + PLUS_SYM + year_token + PLUS_SYM + var2_token + ")" + DIVIDE_SYM + var1_token + EXPRESSION_END;
        if(useMult==false) {
            ex_cond1_compute_divide_with_plus_sign_token="";
            ex_cond1_compute_divide_with_minus_sign_token="";
        }

        // explanation
        String explanation = ex_expression_token + "\n";
        if(cond_inx == 0){  // age1 given, find age2
            explanation += ex_age1_after_year_token;
            if(useMult == false && useAddMinus == false)
                explanation += ex_age1_after_year_same_age_token;
            explanation += "\n";

            if(useMult == false && useAddMinus == true)
                explanation += ex_cond1_compute_b4_divide_with_zero_var2_token;
            if(sign == PLUS_SIGN){
                explanation += ex_cond1_compute_b4_divide_with_plus_sign_token + "\n"
                        + ex_cond1_compute_divide_with_plus_sign_token + "\n"
                        + ex_after_year_to_age2_with_plus_sign_token + "\n";
            } else if(sign == MINUS_SIGN){
                explanation += ex_cond1_compute_b4_divide_with_minus_sign_token + "\n"
                        + ex_cond1_compute_divide_with_minus_sign_token + "\n"
                        + ex_after_year_to_age2_with_minus_sign_token + "\n";
            }
        } else{ // age2 given, find age1
            explanation += ex_age2_after_year_token;
            if(useMult == false && useAddMinus == false)
                explanation += ex_age2_after_year_same_age_token;
            explanation += "\n";

            if(sign == PLUS_SIGN){
                explanation += ex_cond2_compute_with_plus_sign_token + "\n";
            } else if(sign == MINUS_SIGN){
                explanation += ex_cond2_compute_with_minus_sign_token + "\n";
            } else{
                explanation = "sign value error";
            }

            explanation += ex_after_year_to_age1_token + "\n";
        }

        return new String[] {content, explanation};     // {content, explanation}
    }

    /* 유형 2 : 두 나이의 합 차만 주어진 경우
            x + y = var1
            x - y = var2
         */
    public String[] create_age_sentence_sum_difference(int ls_index, int var_num_per_sentence, int cond_inx, int sign){
        int index1 = ls_index;
        int index2 = ls_index + 1;
        int var_index = ls_index * var_num_per_sentence;

        String name1_token = VAR_START + NAME_STR + index1 + VAR_END;
        String name2_token = VAR_START + NAME_STR + index2 + VAR_END;
        String age1_token = VAR_START + AGE_STR + index1 + VAR_END;
        String age2_token = VAR_START + AGE_STR + index2 + VAR_END;
        String var1_token = VAR_START + VAR_STR + var_index + VAR_END;

        String name1_age_token = name1_token + "의 나이";
        String name2_age_token = name2_token + "의 나이";

        String content="";
        String age_sum_start_token = name1_age_token + "와 " + name2_age_token + "를 ";
        String age_diff_start_token = name1_age_token + "에서 " + name2_age_token + "를 ";
        String sum_token = "합한 ";
        String difference_token = "뺀 ";
        String age_end_token = "값은 " + var1_token + "와 같습니다.";
        if(sign == PLUS_SIGN){
            content =  age_sum_start_token + sum_token + age_end_token;
        } else{
            content = age_diff_start_token + difference_token + age_end_token;
        }

        String sign_token = "", sign_blank_token="", inv_sign_token = "", inv_sign_blank_token="";
        if(sign== PLUS_SIGN) {
            sign_token = PLUS_SYM;  sign_blank_token = PLUS_BLANK_STR;
            inv_sign_token = MINUS_SYM; inv_sign_blank_token = MINUS_BLANK_STR;
        }
        else if(sign== MINUS_SIGN) {
            sign_token = MINUS_SYM; sign_blank_token = MINUS_BLANK_STR;
            inv_sign_token = PLUS_SYM;  inv_sign_blank_token = PLUS_BLANK_STR;
        }
        else sign_token = "!!!!!!!SIGN_ERROR!!!!";


        //(name1의 나이) +- (name2의 나이) = %d
        // (name1의 나이) = %d -+ (name2의 나이) = %d
        // (name2의 나이) = %d - name1의 나이 = %d
        String ex_expr_token = name1_age_token + sign_blank_token + name2_age_token
                + EQUAL_BLANK_SYM + var1_token;
        String ex_age1_token = name1_age_token
                + EQUAL_BLANK_SYM + var1_token + inv_sign_blank_token + name2_age_token
                + EQUAL_BLANK_SYM + var1_token + inv_sign_blank_token + age2_token
                + EQUAL_BLANK_SYM + EXPRESSION_START + var1_token + inv_sign_token + age2_token + EXPRESSION_END;
        String ex_age2_with_sum_token = name2_age_token
                + EQUAL_BLANK_SYM + var1_token + MINUS_BLANK_STR + name1_age_token
                + EQUAL_BLANK_SYM + var1_token + MINUS_BLANK_STR + age1_token
                + EQUAL_BLANK_SYM + EXPRESSION_START + var1_token + MINUS_SYM + age1_token + EXPRESSION_END;

        String explanation = "";
        if(cond_inx == 0) {  // age1 given, find age2
            if(sign == PLUS_SIGN) {
                explanation = ex_expr_token + "\n" + ex_age2_with_sum_token + "\n";
            } else if(sign == MINUS_SIGN) {
                explanation = "VALUE ERROR:: should do minus from bigger to smaller";
            } else{
                explanation = "SIGN VALUE ERROR";
            }
        } else if(cond_inx == 1){   // age2 given, find age1
            explanation = ex_expr_token + "\n" + ex_age1_token + "\n";
        }

        return new String[] {content, explanation};     // {content, explanation}
    }


    /////////////////////////////////////////////////////////////////////////////////////////
    // 나이문제 - 랜덤 숫자 뽑기

    // 숫자 뽑기 연속
    private int[] getRandomAgeValue(int cond_inx, int[] sentence_category_ls){
        int[] age_ls;
        int[] var_ls;
        for(int i = 0; i < sentence_category_ls.length; i++){
            // age, var 생성
        }
        return new int[] {};
    }

    // age1 + year = (age2 + year) * var1 +- var2
    // return new int[] {sign, age1, age2, var1, var2};
    private int[] getRandomAgeYXValue(int given_age, int year){ // age2, year given
        int var1=0, var2=0, age1=0, age2=given_age;

        int sign = random.nextInt(1);

        // age1 = age2 * var1 +- var2
        while(age1 <= 0) {
            var1 = random.nextInt(4) + 2;
            var2 = random.nextInt(20) + 1;
            if(sign == PLUS_SIGN) {
                age1 = (age2 + year) * var1 + var2 - year;
            } else{
                age1 = (age2 + year) * var1 - var2 - year;
            }
        }

        return new int[] {sign, age1, age2, var1, var2};
    }

    // age1 +- age2 = var1
    // 단, age1 - age2의 경우, age1 > age2
    // return new int[] {sign, age1, age2, var1};
    private int[] getRandomAgeX1X2Value(int given_age, int given_inx){
        int age1, age2;
        /* 랜덤 값 범위 수정 필요
            세자리수 -> 높은 난이도
            두자리수 -> 낮은 난이도
         */
        if(given_inx == 0) {
            age1 = given_age;
            age2 = random.nextInt(50) + given_age; // age1 > age2 유지
        } else{
            age1 = random.nextInt(50) + given_age; // age1 > age2 유지
            age2 = given_age;
        }

        int sign = random.nextInt(2);
        int var1 = 0;
        if(sign == PLUS_SIGN){  // age1 + age2 = var1
            var1 = age1 + age2;
        } else { // age1 - age2 = var1
            var1 = age1 - age2;
        }
        return new int[] {sign, age1, age2, var1};
    }


    /* 유형3 : 대입형 문제 -> 1부터 숫자 넣어가며 푸는 유형 -> 별개 유형으로 취급하기로 함
    ex) 남준이 형의 나이는 남준이 아버지의 나이에서 남준이의 나이를 나눈 것의 4배보다 2살 적습니다. 아버지의 연세가 46세 일 때, 남준이 형의 나이는 몇 살일까요?

    */

}