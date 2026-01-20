package com.lannstark.lec10

fun main() {
    val derived1 = Derived1(300)
    println()
    val derived2 = Derived2(300)
    /***
     *
     * Bass Class
     * 0 <- 이때 0이 나오는게 ? 인건데.
     *      해당 메서드가 Derived1 에서 호출되어 실행되어
     *      Derived1 의 getNumber 가 호출 되는데,
     *      이 시점에 Derived1 의 프로퍼티가 초기화가 안돼 0이 나오는거임.
     *      생성 된 후에는 300이 제대로 나옴.
     * Derived1 Class
     *
     * Bass Class
     * 300
     * Derived2 Class
     *
     */
}

open class Base(
    open val number: Int = 100
) {
    init {
        println("Bass Class")
        println(number)
    }
}

// 상위클래스의 생성자 또는 초기화 블록에서
// open 프로퍼티를 사용하면 예기치 못한 버그가 생길 수 있다.
// => 애초에 자바에서도 상속을 지양하는 방향이 BP 라 이렇게까지 깊게 고민할 필요가 있을까 싶음.
// 기억할만한 키워드는 상속 사용 시 생성자, 초기화 블록에서 open 프로퍼티는 지양하자.
class Derived1(
    override val number: Int
) : Base(number) {

    init {
        println("Derived1 Class")
    }
}

class Derived2(
    number: Int
) : Base(number) {

    init {
        println("Derived2 Class")
    }
}