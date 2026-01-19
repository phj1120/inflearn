package com.lannstark.lec02

fun main() {
    // 1. 코틀린에서 null 체크
    // 코틀린에서는 Null 이 가능한 타입을 완전히 다르게 취급한다.
    var str: String? = null
//    str.startsWith("A")

    var str2: String = ""
    str2.startsWith("A")

    // 2. Safe Call 과 Elvis 연산자
//    str.startsWith("A")
    // Safe Call ?.
    println(str?.startsWith("A"))
    // Elvis 연산자 ?:
    println(str?.startsWith("A") ?: false)

    // early return 에도 사용 가능
    var str3: String? = "123"
    if(str3 == null) {
        return
    }
    str3 ?: return

    // 3. null 아님 단언. !!
    // db 상으로 nullable 이라 null 을 열어두긴 해야되는데,
    // 현재 상황에선 무조건 데이터가 있을거라고 판단 될 경우
    // 이거에대한 null 처리르 할 필요까진 없다고 판단 될 경우
    // 이건 널 아니야 단언해서 사용.
    // 그럼에도 null 이 들어오면 NPE 발생
    var str4: String? = "ABC"
    println(str4!!.startsWith("A"))

    var str5: String? = "BCD"
    println(str5!!.startsWith("A"))

//    var str6: String? = null
//    println(str6!!.startsWith("A"))

    // 4. 플랫폼 타입
    // 한 프로젝트에서 자바와 코틀린의 병행이 가능한데, 이거에 대한 null 처리
    // java 의 어노테이션 정보를 읽어서 사용함.
    // java 에 없다면? 판단할 수 없는데 이를 플랫폼 타입이라 함.
    // java 코드를 읽으며 null 가능성을 확인하거나,
    // kotlin 으로 wrapping 해서 이를 단일 지점으로.
    val person = Person(null)
    person.name.startsWith("A")


}

fun startsWithA1(str: String?): Boolean {
    if (str == null) {
        throw IllegalArgumentException("null 이 들어왔습니다.")
    }
    return str.startsWith("A")
}

fun startsWithA1Kt(str: String?): Boolean {
    return str?.startsWith("A") ?: throw IllegalArgumentException("null 이 들어왔습니다")
}

fun startsWithA2(str: String?): Boolean? {
    if (str == null) {
        return null
    }
    return str.startsWith("A")
}

fun startsWithA2Kt(str: String?): Boolean? {
    return str?.startsWith("A")
}

fun startsWithA3(str: String?): Boolean {
    if (str == null) {
        return false
    }
    return str.startsWith("A")
}

fun startsWithA3Kt(str: String?): Boolean {
    return str?.startsWith("A") ?: false
}

fun startWith(str: String?): Boolean {
    return str!!.startsWith("A")
}