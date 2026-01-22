package com.lannstark.lec20

fun main() {
    // 1. scope function
    // 일시적인 영역을 형성하는 함수
    // 람다를 사용해 일시적인 영역을 만들어 코드를 더 간결하게 만들거나, method chaining 에 활용하는 함수

    // it  | this
    // let, run  | 람다의 결과가 반환
    // also, apply | 객체 그 자체가 반환
    // with

    // this 생략이 가능한대신, 다른 이름을 붙일 수 없다. : 일반함수 호출
    // it 생략이 불가능한 대신, 다른 이름을 붙일 수 있다. : 확장함수 호출

    // let
    // 하나 이상의 함수를 call chain 결과로 호출 할 때
    val strings = listOf("APPLE", "ORANGE", "CAR")
    strings.map { it.length }
        .filter { it > 3 }
        .let { lengths -> println(lengths) }

    // non-null 값에 대해서만 code block 을 실행시킬때
    var string: String?
//    string = null
    string = "apple"

    var string2 = string?.let {
        println(it.uppercase())
        it.length
    }
    println(string2)

    // 일회성으로 제한된 영역에 지역변수를 만들때

    // let
    // run
    // also
    // apply
    // with

    // scope chain 과 가독성
    // scope chain 을 사용하는 것이 가독성에 좋을까?
    // 사용하지 않는 편이 디버깅도 쉽고 수정도 쉬움.
    // 숙련된 코틀린 개발자는 쉬울 수 있음...
    // -> 팀내 적절한 컨벤션을 가지고 적용하면 좋음.
}

fun printPerson(person: Person?) {
//    if(person != null) {
//        println(person.name)
//        println(person.age)
//    }

    person?.let {
        println(it.name)
        println(it.age)
    }
}


data class Person(val name: String, val age: Int)