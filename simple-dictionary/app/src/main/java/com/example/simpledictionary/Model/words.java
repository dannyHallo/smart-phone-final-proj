package com.example.simpledictionary.Model;

public class words {
    public String num, obj, basic_trans, spec_trans, english_trans;

    public words() {

    }
//    public words(String num, String obj, String basic_trans, String spec_trans, String english_trans){
//        this.num = num;
//        this.obj = obj;
//        this.basic_trans = basic_trans;
//        this.spec_trans = spec_trans;
//        this.english_trans = english_trans;
//
//
//    }
    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getObj() {
        return obj;
    }

    public int getObjLength(){
        return obj.length();
    }

    public void setObj(String obj) {
        this.obj = obj;
    }

    public String getBasic_trans() {
        return basic_trans;
    }

    public void setBasic_trans(String basic_trans) {
        this.basic_trans = basic_trans;
    }

    public String getSpec_trans() {
        return spec_trans;
    }

    public void setSpec_trans(String spec_trans) {
        this.spec_trans = spec_trans;
    }

    public String getEnglish_trans() {
        return english_trans;
    }

    public void setEnglish_trans(String english_trans) {
        this.english_trans = english_trans;
    }


}
