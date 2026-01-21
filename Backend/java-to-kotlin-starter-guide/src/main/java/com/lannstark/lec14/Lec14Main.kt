package com.lannstark.lec14

fun main() {
    // 1. Data Class
    //  DTO
    //  equals, hashCode, toString 자동 생성
    val person1 = PersonDto("박현준", 29)
    val person2 = PersonDto("박현준", 30)

    println(person1.equals(person2))

    // 2. Enum Class
//    val country = Country.KOREA
    val country = Country.AMERICA
    when (country) {
        Country.KOREA -> println("Korea")
        Country.AMERICA -> println("America")
        // enum 을 기준으로 when 을 사용하면 else 에 대한 처리가 필요 없음.
    }

    // 3. Sealed Class, Sealed Interface (봉인한)
    // 상속이 가능하게끔 하고 싶은데 외부에서는 이 클래스를 상속받지 않으면 좋겠어
    // java17 에서 추가.
//    val car: HyundaiCar = Avante()
    val car: HyundaiCar = Sonata()
    when (car) {
        is Avante -> println("Avante")
        is Sonata -> println("Sonata")
    }

}

// equals, hashCode, toString 자동으로 만들어줌
// java 의 record class
data class PersonDto(
    val name: String,
    val age: Int,
)

enum class Country (private val code: String) {
    KOREA("KO"),
    AMERICA("US"),
    ;
}

sealed class HyundaiCar (
    val name: String,
    val price: Int
)

class Avante : HyundaiCar("Avante", 10000000)
class Sonata : HyundaiCar("Sonata", 12000000)