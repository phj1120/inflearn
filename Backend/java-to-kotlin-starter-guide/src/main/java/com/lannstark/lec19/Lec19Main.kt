package com.lannstark.lec19

import com.lannstark.lec19.a.printHelloWorld as printHelloWorldA
import com.lannstark.lec19.b.printHelloWorld as printHelloWorldB

typealias FruitFilter = (Fruit) -> Boolean

// 해당 부분은 외우지 말고 나중에 코드를 줄이고 싶을 경우 사용!
fun main() {
    // 1. typealias 와 as import
    val fruits: List<Fruit> = listOf(
        Fruit("사과", 1000),
        Fruit("사과", 2000),
        Fruit("사과", 3000),
        Fruit("포드", 1000),
        Fruit("포드", 2000),
        Fruit("포드", 3000),
        Fruit("수박", 1000),
        Fruit("수박", 2000),
        Fruit("수박", 3000),
    )

    val filter1: (Fruit) -> Boolean = { fruit -> "사과" == fruit.name }
    val filter2: (Fruit) -> Boolean = { fruit -> fruit.price > 1000 }
    val filteringFruits = fruits
        .filterFruit(filter1)
        .filterFruit(filter2)
        .filterFruit { fruit -> fruit.price < 3000 }

    println(filteringFruits)

    printHelloWorldA()
    printHelloWorldB()

    // 2. 구조분해와 componentN 함수
    val fruit: Fruit = Fruit("사과", 1000)

    val (name1, price1) = fruit
    println("$name1 $price1")

    // data class 의 경우 componentN 함수가 자동으로 생성 됨
    val name2 = fruit.component1()
    val price2 = fruit.component2()
    println("$name2 $price2")

    // 변수명으로 가져오는게 아니라, 순서로 할당 됨.
    val (name3) = fruit
    val (price3) = fruit
    println("$name3 $price3")

    // data class 가 아닐 경우 componentN 함수 직접 구현(연산자 취급)
    val justFruit: JustFruit = JustFruit("사과", 1000)
    val (name4, price4) = justFruit
    println("$name4 $price4")

    // 3. Jump 와 Label
    // steam forEach 에서 break, continue 를 사용하고 싶을때는 전통적인 for 문을 사용하자.
    // 굳이굳이 어떻게 쓸수는 있는데 가독성 떨어짐.
    // @ Label
    // 특정 expression 에 라벨이름@ 을 붙여 하나의 라벨로 간주하고, break, continue, return 등을 사용하는 기능
    // 근데 코드 복잡도 올라가니까 쓰지마.
    for (i in 1..10) {
        for (j in 1..10) {
            if (j == 2 ) {
                break
            }
            println("i: ${i} / j: ${j}")
        }
    }

    myLoop@for (i in 1..10) {
        for (j in 1..10) {
            if (j == 2 ) {
                break@myLoop
            }
            println("i: ${i} / j: ${j}")
        }
    }

    // 4. TakeIf 와 TakeUnless
    val number = 10

    // takeIf: 주어진 조건을 만족하면 그 값이. 그렇지 않으면 null 이 반환
    val number1: Int? = if(number >= 0){
        number
    } else {
        null
    }
    println(number1)

    val number2: Int? = number.takeIf { it >= 0 }
    println(number2)

    // takeUnless: 주어진 조건을 만족하면 null 이 그렇지 않으면 그 값이 반환
    val number3: Int? = if(number >= 0){
        null
    } else {
        number
    }
    println(number3)

    val number4: Int? = number.takeUnless { it >= 0 }
    println(number4)
}

fun List<Fruit>.filterFruit(filter: FruitFilter): List<Fruit> {
    val results = mutableListOf<Fruit>()
    for (fruit in this) {
        if (filter(fruit)) {
            results.add(fruit)
        }
    }
    return results
}

data class Fruit(val name: String, val price: Int)

class JustFruit(val name: String, val price: Int) {
    operator fun component1() = name
    operator fun component2(): Int {
        return this.price
    }
}
