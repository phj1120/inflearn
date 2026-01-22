package com.lannstark.lec17

import java.io.BufferedReader
import java.io.FileReader

fun main() {
    // Java 에서 함수는 2급 시민이지만, -> 함수를 파라미터로 넘기는 척은 하지만 못 넘김.
    // Kotlin 에서 함수는 1급 시민이다. -> 함수를 파라미터로 넘길 수 있음.
    val fruits: List<Fruit> = listOf(
        Fruit("사과", 1000),
        Fruit("사과", 2000),
        Fruit("사과", 3000),
        Fruit("오렌지", 1000),
        Fruit("오렌지", 2000),
        Fruit("오렌지", 3000),
        Fruit("수박", 1000),
        Fruit("수박", 2000),
        Fruit("수박", 3000),
    )

    // 람다를 만드는 방법1.
    val isApple1: (Fruit) -> Boolean = fun(fruit: Fruit): Boolean {
        return fruit.name == "사과"
    }

    // 람다를 만드는 방법2.
    val isApple2: (Fruit) -> Boolean = { fruit: Fruit -> fruit.name == "사과" }

    filterFruits(fruits, isApple1)
    filterFruits(fruits, isApple2)
    filterFruits(fruits, { fruit -> fruit.name == "사과" })
    filterFruits(fruits, { fruit ->
        println("fruit : $fruit")
        fruit.name == "사과" // 마지막 줄이 람다의 반환값
    })
    filterFruits(fruits) { fruit -> fruit.name == "사과" }
    filterFruits(fruits) { it.name == "사과" }

    // 3. Closure
    // Java 에서 람다를 쓸 때 밖에 있는 변수는 final 만 사용 가능.
    // 코틀린에서는 람다가 시작하는 시점에 존재하는 모든 변수를 포획히여 가지고 있기 떄문에 모든 변수가 사용 가능함.
    var targetFruitName = "바나나"
    targetFruitName = "수박"
    filterFruits(fruits) { it.name == targetFruitName }

    // 4. try with resources
    // use 에 람다를 넘기는거고, 파라미터가 하나라 괄호 밖에 작성.
    BufferedReader(FileReader("test")).use { reader ->
        println(reader.readLine())
    }
}

private fun filterFruits(
    fruits: List<Fruit>,
    filter: (Fruit) -> Boolean,
): List<Fruit> {
    val results = mutableListOf<Fruit>()
    for (fruit in fruits) {
        if (filter(fruit)) {
            results.add(fruit)
        }
    }
    return results
}