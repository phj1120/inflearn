package com.lannstark.lec01

fun main() {
    /**
     * 실전 팁
     * 모든 변수는 우선 val (불변) 으로 만들고, 꼭 필요한 경우 var(가변) 로 변경한다.
     */


    var number1 = 10L // Variable: 발 -> 변경 가능
    number1 = 20L

    val number2 = 20L // Value: 밸 -> 변경 불가능
//    number2 = 30L


    // 코틀린에서는 타입을 컴파일 시점에 추론하지만, 지정해줄 수 있음.
    var number3: Int = 10

    // 값이 할당 되지 않았기 때문에 추론 불가능해 에러 발생
//    var number4

    // 값이 할당 되지 않아도, 타입을 정해줬기때문에 가능.
    var number5: Int

    // 할당 되지 않은 값을 사용하려고 하면 에러 발생
//    print(number5)

    // 2.
    // Primitive Type 에 대한 고민 할 필요 없음. boxing, unboxing 을 코틀린이 알아서 처리.
    val number6 = 10L
    val number7 = 10000L

    // 3. nullable 처리
    var number8 = 100L
//    number8 = null

    var number9: Long? = 100L
    number9 = null

    // 4. 객체의 인스턴스화 -> new 안붙힘.
    var person = Person("박현준")



}