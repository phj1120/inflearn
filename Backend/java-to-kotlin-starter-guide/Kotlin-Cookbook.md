# 코틀린 쿡북 (강의 코드 기반 정리)

강의에서 작성한 코드(`src/main/java/com/lannstark`)를 기준으로 자주 쓰는 문법과 패턴을 빠르게 찾아볼 수 있도록 정리했다.

## 1. 변수와 타입 추론 (lec01)

`val`은 불변, `var`는 가변이다. 타입은 기본적으로 추론된다.

```kotlin
var number1 = 10L
number1 = 20L

val number2 = 20L
// number2 = 30L // 불가능

var number3: Int = 10
var number5: Int
```

널 가능성은 타입에 `?`로 표시한다.

```kotlin
var number9: Long? = 100L
number9 = null
```

## 2. 널 안정성: Safe Call, Elvis, 단언 (lec02)

```kotlin
val str: String? = null
println(str?.startsWith("A"))
println(str?.startsWith("A") ?: false)

val str4: String? = "ABC"
println(str4!!.startsWith("A"))
```

널이 가능한 입력을 여러 스타일로 처리하는 함수 예시:

```kotlin
fun startsWithA1Kt(str: String?): Boolean {
    return str?.startsWith("A") ?: throw IllegalArgumentException("null 이 들어왔습니다")
}

fun startsWithA2Kt(str: String?): Boolean? = str?.startsWith("A")
fun startsWithA3Kt(str: String?): Boolean = str?.startsWith("A") ?: false
```

## 3. 기본 타입 변환, Any/Unit/Nothing, 문자열 템플릿 (lec03)

코틀린은 기본 타입 간 변환을 명시적으로 해야 한다.

```kotlin
val number3: Int = 4
val number5: Long = number3.toLong()
```

문자열 템플릿과 인덱싱:

```kotlin
val person = Person("Park", 29)
val message1 = "이름: ${person.name} 나이: ${person.age}"
println(message1)
println(person.name[0])
```

## 4. 비교, 동등성/동일성, 연산자 오버로딩 (lec04)

`>`, `<` 등은 `compareTo` 호출로 매핑된다. `==`는 동등성, `===`는 동일성이다.

```kotlin
println(money1 > money2)
println(money2 == money3)
println(money2 === money3)
```

## 5. 조건문과 when 표현식 (lec05)

코틀린의 `if`는 표현식이라 값을 반환한다.

```kotlin
fun getPassOrFail(score: Int): String =
    if (score >= 50) "P" else "F"
```

`when`은 switch 대체로 쓰이며 범위/타입 매칭도 가능하다.

```kotlin
fun getGradeWithSwitch2(score: Int): String =
    when (score) {
        in 90..99 -> "A"
        in 80..89 -> "B"
        else -> "C"
    }
```

## 6. 반복문과 범위 (lec06)

범위와 중위 호출을 활용한 반복:

```kotlin
for (i in 1..3) println(i)
for (i in 3 downTo 1) println(i)
for (i in 1..5 step 2) println(i)
```

## 7. 예외 처리와 자원 정리 (lec07)

코틀린은 체크 예외가 없다. `use`로 자원을 안전하게 정리한다.

```kotlin
fun parseIntOrNull(str: String): Int? =
    try { str.toInt() } catch (e: NumberFormatException) { null }

BufferedReader(FileReader(str)).use { reader ->
    println(reader.readLine())
}
```

## 8. 함수: 기본값, Named Argument, Vararg (lec08)

```kotlin
fun repeat(str: String, num: Int = 3, useNewLine: Boolean = true) { ... }

printNameAndGender(name = "박현준", gender = "남자")

fun printAll(vararg strings: String) { ... }
```

## 9. 클래스, 생성자, 프로퍼티, backing field (lec09)

주 생성자와 프로퍼티를 함께 선언할 수 있다.

```kotlin
class Person3(val name: String, var age: Int)
```

`init`에서 유효성 검사, custom getter, backing field:

```kotlin
class Person6(name: String, var age: Int) {
    val name: String = name
        get() = field.uppercase()
}
```

## 10. 상속/추상/인터페이스 (lec10)

`open`이 없으면 상속 불가. 인터페이스 기본 구현은 `super<타입>`으로 호출 가능.

```kotlin
abstract class Animal(protected val species: String, protected open val legCount: Int) {
    abstract fun move()
}

class Penguin(species: String, val wingCount: Int = 2)
    : Animal(species, 2), Swimable, Flyable {
    override fun act() {
        super<Swimable>.act()
        super<Flyable>.act()
    }
}
```

## 11. 가시성 제어와 프로퍼티 제한 (lec11)

`internal`은 모듈 내 공개, `private set`으로 setter 제한 가능.

```kotlin
class Car(
    internal val name: String,
    private var owner: String,
    _price: Int
) {
    var price = _price
        private set
}
```

## 12. object 키워드: companion, singleton, 익명 객체 (lec12)

```kotlin
class Person(val name: String, var age: Int) {
    companion object Factory : Log {
        const val MIN_AGE = 1
        fun newBaby(name: String) = Person(name, MIN_AGE)
    }
}

object Singleton { var a: Int = 0 }

moveSomething(object : Movable { ... })
```

## 13. 중첩/inner 클래스 (lec13)

바깥 클래스를 참조하지 않는 중첩 클래스가 기본 권장.

```kotlin
class House(val address: String, val livingLoom: LivingRoom) {
    class LivingRoom(area: String)
    inner class LivingRoomInner(area: String) {
        val address: String
            get() = this@House.address
    }
}
```

## 14. Data/Enum/Sealed (lec14)

```kotlin
data class PersonDto(val name: String, val age: Int)

enum class Country(private val code: String) { KOREA("KO"), AMERICA("US") }

sealed class HyundaiCar(val name: String, val price: Int)
class Avante : HyundaiCar("Avante", 10000000)
```

## 15. 배열과 컬렉션 (lec15)

불변/가변 컬렉션을 생성 시점에 분리한다.

```kotlin
val numbers = listOf(100, 200)
val numbers2 = mutableListOf(100, 200)
numbers2.add(300)

val map1 = mutableMapOf(1 to "MON", 2 to "TUE")
```

## 16. 확장/중위/inline/지역 함수 (lec16)

```kotlin
fun String.lastChar(): Char = this[this.length - 1]

infix fun Int.add2(other: Int): Int = this + other

inline fun Int.add3(other: Int): Int = this + other
```

## 17. 람다, 고차함수, 클로저 (lec17)

```kotlin
val isApple: (Fruit) -> Boolean = { fruit -> fruit.name == "사과" }
filterFruits(fruits) { it.name == "사과" }

var targetFruitName = "수박"
filterFruits(fruits) { it.name == targetFruitName }
```

## 18. typealias, 구조분해, label, takeIf (lec19)

```kotlin
typealias FruitFilter = (Fruit) -> Boolean

val (name, price) = Fruit("사과", 1000)

val number2: Int? = number.takeIf { it >= 0 }
val number4: Int? = number.takeUnless { it >= 0 }
```

## 19. 스코프 함수 정리 (lec20)

`let`, `run`, `also`, `apply`, `with`는 일시적 스코프를 만들어 체이닝과 null 처리에 유용하다.

```kotlin
strings.map { it.length }
    .filter { it > 3 }
    .let { lengths -> println(lengths) }

person?.let {
    println(it.name)
    println(it.age)
}
```

---

추가로, 자바 코드와 혼용 시 `플랫폼 타입`과 `가변성/널 가능성` 이슈가 있으니 경계 지점에서 래핑하거나 변환하는 것이 안전하다.
