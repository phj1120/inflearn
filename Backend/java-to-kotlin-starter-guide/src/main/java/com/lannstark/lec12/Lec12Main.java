package com.lannstark.lec12;

public class Lec12Main {

    public static void main(String[] args) {
        Person.Factory.newBaby("ABC");
//        Person.Companion.newBaby("ABC");
        Person.newBabyJavaStatic("ABC");
    }

}
