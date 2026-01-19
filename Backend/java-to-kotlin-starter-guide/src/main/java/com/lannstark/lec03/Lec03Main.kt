package com.lannstark.lec03

fun main() {
    // 1. 기본타입
    // 선언된 기본값을 보고 타입을 추론.
    val number1 = 3.0f // Float
    val number2 = 3.0 // Double

    // kotlin 에서 기본 타입 간의 변환이 명시적으로 이루어져야 함(to변환타입())
    val number3: Int = 4
//    val number4: Long = number3
    val number5: Long = number3.toLong()

    // 2. 일반타입
    // printAgeIfPerson
    // printAgeIfNullablePerson

    // 3. 특이타입
    // 3.1. Any
    // Java 의 Object 느낌.
    // null 을 포함하고 싶으면 Any?
    // 3.2. Unit
    // Java 의 void 느낌.
    // void 와 다르게, Unit 은 그 자체로 타입인자로 사용가능.(void 는 Void.class 로 사용해야함)
    // 3.3. Nothing
    // 함수가 정상적으로 끝나지 않았다는 사실을 표현하는 역할
    // 무조건 예외를 반환하는 함수, 무한 루프 함수의 반환형으로.
    // 3.4. String interpolation / String indexing
    val person = Person("Park", 29)
    val message1 = "이름: ${person.name} 나이: ${person.age}"
    println(message1)

    // 중괄호를 생략할 수 있으나,
    // 가독성 및 추후 변경을 위해 위의 방식대로 쓰는 것 추천
    val name = person.name
    val age = person.age
    val message2 = "이름: $name 나이: $age"
    println(message2)

    println(name[0])
    println(name[1])
    println(name[2])
    println(name[3])

}

fun printAgeIfPerson(obj: Any) {
    if (obj is Person){
        // SmartCast 이미 이 if 문에는 Person 만 있을거니까.
        val person = obj as Person
        println(person.age)
    }

    // 반대의 경우.
//    !(obj is Person)
//    obj !is Person
}

fun printAgeIfNullablePerson(obj: Any?) {
    if (obj is Person){
        // SmartCast 이미 이 if 문에는 Person 만 있을거니까.
        val person = obj as? Person
        // obj 가 null 일 수 있으니. 사용 하는 곳에서도 ?로 사용.
        println(person?.age)
    }
}