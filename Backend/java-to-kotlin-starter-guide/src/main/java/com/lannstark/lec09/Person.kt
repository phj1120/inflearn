package com.lannstark.lec09

fun main() {
    val person = Person2("박현준", 29)
    println(person.name)
    // val 변경 불가능
//    person.name = "현준"
    // var 변경 가능
    person.age = 30
    println(person.age)

    // 자바의 Class 를 사용할 때도 가능!
    val javaPerson = JavaPerson("박현준", 29)
    println(javaPerson.name)
    javaPerson.age = 30
    println(javaPerson.age)

    /**
     * init
     * 부생성자1
     * 부생성자2
     * */
    val person4 = Person4()
    person4.isAdult2

    val person6 = Person6("박현준", 29)
    person6.getUpperCaseNameMethod()
    person6.getUpperCaseNameProperty
}

// 프로퍼티: 필드 + getter + setter
// 코틀린에서 getter, setter 는 자동으로 만들어줌
class Person1 constructor(name: String, age: Int) {
    val name: String = name
    var age: Int = age
}

// constructor 지시어 생략 가능
class Person2(name: String, age: Int) {
    val name: String = name
    var age: Int = age
}

// 클래스의 필드 선언과 생성자를 동시에 선언 할 수 있다.
class Person3(val name: String, var age: Int)

// 주생성자: 반드시 존재해야함.
class Person4(val name: String, var age: Int) {
    // 생성자가 호출 되는 시점에 호출
    init {
        if (age <= 0) {
            throw IllegalArgumentException("나이는 $age 일 수 없습니다.")
        }
        println("init")
    }

    // 부생성자는 결국엔 주생성자를 this 로 호출 해야함.
    // => 부생성자보다 Default Parameter 를 권장!
    // convert 같은 경우는 정적 팩토리 메서드를 추천!
    // 강사님 기준 부생성자 써 본 기억 거의 없음.
    constructor(name: String) : this(name, 1) {
        println("부생성자1")
    }

    constructor() : this("홍길동") {
        println("부생성자2")
    }

    fun isAdult1(): Boolean {
        return this.age > 19
    }

    // custom getter
    val isAdult2: Boolean
        get() {
            return this.age > 19
        }

    // 하나의 값을 반환하는 함수 블럭의 경우 = 으로 변경 가능하다고 했음.
    val isAdult3: Boolean
        get() = this.age > 19
}

class Person6(
    name: String, var age: Int
) {
    val name: String = name
        // 이렇게 되면 name 이 getter 를 부르고 계속 무한 루프가 돌기 때문에.
        // 이를 막기 위해 backing field 를 사용.
//        get() = name.uppercase()
        get() = field.uppercase()

    fun getUpperCaseNameMethod(): String = this.name.uppercase()

    // 근데 보통. backing field 를 사용할 일은 없었던게,
    // 만약 대문자로 반환하고 싶은 경우 다른 메서드를 만들어 프로퍼티 처럼 사용.
    val getUpperCaseNameProperty: String
        get() = this.name.uppercase()
}

// setter 자체를 지양하기 때문에 custom setter 도 잘 안 씀.
class Person7(
    name: String, var age: Int
) {
    var name = name
        set(value) {
            field = value.uppercase()
        }
}