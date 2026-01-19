package com.lannstark.lec06

fun main() {
    // : 대신.
    val numbers = listOf(1L, 2L, 3L)
    for (number in numbers) {
        println(number)
    }

    // 1씩 증가
    for (i in 1..3) {
        println(i)
    }

    // 1씩 감소
    for (i in 3 downTo 1) {
        println(i)
    }

    // 2칸씩 증가
    for (i in 1..5 step 2) {
        println(i)
    }

    // 3 downTo 1: 시작값 3, 끝값 1, 공차가 -1 인 등차수열
    // 1..5 step 2: 시작값1, 끝값 5, 곡차가 2 인 등차수열
    // downTo, step 도 함수!( 중위 호출 함수)
    // 변수.함수이름(인자) -> 변수 함수 인자

    // while문 동일
    var i = 1
    while (i <= 3) {
        println(i)
        i++
    }
}