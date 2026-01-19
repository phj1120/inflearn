package com.lannstark.lec04

fun main() {
    val money1 = JavaMoney(1000L)
    val money2 = JavaMoney(2000L)
    val money3 = JavaMoney(2000L)

    // 코틀린에서는 비교 연산자를 사용하게 되면 compareTo 를 자동으로 호출
    println(money1 > money2)
    println("money1 > money2: ${money1 > money2}")

    // 동등성, 동일성
    // 동등성: 두 객체의 값이 같은가? (Java 의 equals)
    println("money2 == money3: ${money2 == money3}")
    // 동일성: 완전히 동일한 객체인가?(참조값 주소가 동일한지) (Java 의 ==)
    println("money2 === money3: ${money2 === money3}")
    println("money2 === money2: ${money2 === money2}")

    // 4. 코틀린에 있는 특이한 연산자
    // in / !in: 컬렉션이나 범위에 포함 되어있다, 포함되어있지 않다.
    // a..b: a부터 b까지 범위 객체 생성 - 추후 다룸
    // a[i]: a 에서 특정 index 값 가져옴

    // 객체 연산자 오버로딩
    println(money1 + money2)
}
