package com.lannstark.lec12

fun main() {
    // object 키워드
    // 1. Java 의 static 처럼 사용하고 싶은 경우: companion object
    // 2. 싱글톤 클래스 만들 경우
    // 3. 익명 클래스 만들 경우
    println(Singleton.a)
    Singleton.a++
    println(Singleton.a)

    moveSomething(object : Movable {
        override fun move() {
            println("parent move")
        }

        override fun fly() {
            println("parent fly")
        }
    })

}

class Person(
    val name: String,
    var age: Int,
) {
    // static
    // class 와 동행하는 오브젝트
    // 이름을 지정안하면 Companion 으로 접근 가능
    companion object Factory : Log {
        // 컴파일시 변수 할당: 진짜 상수에 대해서만 사용 가능
        const val MIN_AGE = 1

        override fun log() {
            println("Person Factory")
        }

        fun newBaby(name: String): Person {
            return Person(name, MIN_AGE)
        }

        @JvmStatic
        fun newBabyJavaStatic(name: String): Person {
            return Person(name, MIN_AGE)
        }
    }
}

// 싱글톤으로 만드는 방법 object 만 붙이면 끝.
object Singleton {
    var a: Int = 0
}

fun moveSomething(movable: Movable) {
    movable.move()
    movable.fly()
}