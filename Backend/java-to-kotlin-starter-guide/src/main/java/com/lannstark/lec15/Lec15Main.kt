package com.lannstark.lec15

fun main() {
    // 1. 배열 잘 안쓰긴 하는데. 문법이니까~
    val array = arrayOf(100, 200)
    array.plus(300)

    // 0 ~ 마지막 인덱스
    for (i in array.indices) {
        println("${i}: ${array[i]}")
    }

    for ((idx, value) in array.withIndex()) {
        println("${idx}: ${value}")
    }

    // 2. Collection
    // 불변/가변을 생성시부터 지정해줘야함.
    // Tip 기본은 불변으로 만들고, 필요시 가변으로 변경
    // 2.1. 불변 리스트
    val emptyList = emptyList<Int>()
    val numbers = listOf(100, 200) // 타입 추론 가능하면 생략 가능

    println(numbers[0])

    for (number in numbers) {
        println(number)
    }

    for ((idx, value) in numbers.withIndex()) {
        println("${idx}: ${value}")
    }

    // 2.2. 가변 리스트
    val numbers2 = mutableListOf(100, 200)
    numbers2.add(300)

    // 2.3. 불변 셋
    setOf(100, 200)

    // 2.4. 가변 셋
    mutableSetOf(100, 200)

    // 2.5. 불변 맵
    val map1 = mutableMapOf(1 to "MON", 2 to "TUE")
    for (key in map1.keys) {
        println("${key} : ${map1[key]}")
    }

    for ((key, value) in map1.entries) {
        println("${key} : ${value}")
    }

    // 2.6. 가변 맵
    val map2 = mutableMapOf<Int, String>()
    map2[1] = "MON"
    map2[2] = "TUE"


    // 3. 컬랙션과 null 가능성
    // List<Int?>  : List에 null 가능, List null 불가능
    // List<Int>?  : List에 null 불가능, List null 가능
    // List<Int?>? : List에 null 가능, List null 가능
    // Java <-> Kotlin 을 못 가름.
    // 코틀린에서 불변으로 만들어도 자바에서 쓸 때 가변으로 사용 가능
    // 코틀린에서 null 이 못들어가는 리스트인데, 자바에선 null 넣을 수 있음.
    // 자바의 리스트를 코틀린에서 쓸 경우 자바에서의 맥락을 확인하고, 가져오는 지점에서 wrapping.
    // 이거는 언어적 특성이기때무에 어쩔 수 없음.
    // 코틀린의 컬렉션이 자바에서 호출 되면 컬렉션 내용이 변할 수 있음을 감안해야함.(방어로직을짜거나, Collections.unmodifableXXX() 사용


}