package com.lannstark.lec16

fun main() {
    // 기존 Java 코드 위에 자연스럽게 코틀린 코드를 추가할 수는 없을까?
    // 어떤 클래스안에 있는 메소드처럼 호출할 수 있지만, 함수는 밖에 만들 수 있게 하자. => 확장함수
    val str = "ABC"
    println(str.lastChar())


    println(1.add(2))
    println(1.add2(2))
    println(1 add2 2)
}

// 1. 확장 함수
// 확장하려는클래스.함수이름(파라미터): 리턴타입{
//  this 를 이용해 실제 클래스 안의 값에 접근
// -> 어 그러면 캡슐화가 깨지는거 아닌가?
//  private, protected 가져올 수 없음.
// -> 멤버 함수와 확장 함수의 시그니처가 같다면?
//  멤버함수가 우선적으로 호출
// -> 확장함수가 오버라이딩 될 경우
//  해당 변수의 정적인 현재 타입의 확장함수 호출
fun String.lastChar(): Char {
    return this[this.length - 1]
}

// 2. 중위 함수(infix)
// 함수를 호출하는 새로운 방법
// 매개변수가하나 있을떄
// 변수.함수이름(매개변수) 대신
// 변수 함수이름 매개변수
fun Int.add(other: Int): Int {
    return this + other
}

infix fun Int.add2(other: Int): Int {
    return this + other
}

// 3. inline 함수
// 함수가 호출 되는 대신, 함수를 호출한 지점에 함수 본문을 그대로 복붙하고 싶은 경우
// ... 최적화의 영역으로 성능 측정과 함께 신중하게 사용
// 함수가 또다른 함수를 부르고하면 함수 call chain 에 overhead 가 생기므로.
inline fun Int.add3(other: Int): Int {
    return this + other
}

// 4. 지역함수
// depth 가 깊어지기도하고, 코드가 깔끔하진 않음...
fun add4(a: Int, b: Int): Int {
    fun overZero(num: Int){
        if(num <= 0) {
            throw IllegalArgumentException("${num} 이 0보다 작습니다.")
        }
    }
    overZero(a)
    overZero(b)

    return a + b
}