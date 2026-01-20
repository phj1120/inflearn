package com.lannstark.lec08

fun main(){
    repeat("Hello World")
    repeat("Hello World", useNewLine = false)

    // Named Argument
    // Builder 를 만들지 않아도 builder 의 장점을 가지게 됨!
    // Kotlin 에서 Java 의 함수를 사용 할 경우엔 불가능
    printNameAndGender(name="박현준", gender="남자")
    printNameAndGender(gender = "남자", name = "박현준")

    // 가변인자
    printAll("A", "B", "C")

    // 배열을 넘길 경우 스프레드 연산자(*)를 붙여줘야함.
    val strings = arrayOf("A", "B", "C")
    printAll(*strings)
}

fun max(a: Int, b: Int): Int {
    if (a > b) {
        return a
    } else {
        return b
    }
}

// if-else 는 Expression
fun max2(a: Int, b: Int): Int {
    return if (a > b) {
        a
    } else {
        b
    }
}

// 함수의 결과값이 하나라면 block 대신 = 사용 가능
fun max3(a: Int, b: Int): Int =
    if (a > b) {
        a
    } else {
        b
    }

// 함수의 반환형 생략 가능한 경우
// 1. 식 본문 함수 -> = 사용시
// 2. Unit 반환 함수
fun max4(a: Int, b: Int) = if (a > b) a else b

// Default Parameter
// Java - 메서드 오버로딩을 활용해서 여러 메소드를 만들어야함.
// Kotlin - Default Parameter 세팅 + Named Argument 사용
fun repeat(str: String, num: Int = 3, useNewLine: Boolean = true) {
    for (i in 1..num) {
        if(useNewLine)
            println(str)
        else
            print(str)
    }
}

fun printNameAndGender(name: String, gender: String){
    println(name)
    println(gender)
}

fun printAll(vararg strings: String) {
    for(str in strings){
        println(str)
    }
}