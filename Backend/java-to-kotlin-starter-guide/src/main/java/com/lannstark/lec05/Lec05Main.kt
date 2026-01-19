package com.lannstark.lec05

fun validateScore(score: Int) {
    if (score < 0) {
        throw IllegalArgumentException("${score}는 0보다 작을 수 없습니다")
    }

    // !(0 <= score <= 100)
    if (score !in 0..100) {
        throw IllegalArgumentException("${score} 범위는 0부터 100 입니다.")
    }
}

// Statement: 프로그램의 문장(하나의 값으로 도출 되지 않아도 됨.)
// Expression: 하나의 값으로 도출 되는 문장.

// if-else
// Java: Statement
// Kotlin: Expression
// 때문에 3항 연산자가 사라지고, if-else 사용.
fun getPassOrFail(score: Int): String {
//    return score >= 50 ? "P" : "F"

    return if (score >= 50) {
        "P"
    } else {
        "F"
    }
}

fun getGrade(score: Int): String {
    return if (score >= 90) {
        "A"
    } else if (score >= 80) {
        "B"
    } else {
        "C"
    }
}

// Switch Case -> When
// Enum Class 혹은 Sealed Class 와 함께 사용할 경우 더욱 진가 발휘.
fun getGradeWithSwitch(score: Int): String {
    return when (score / 10) {
        9 -> "A"
        8 -> "B"
        else -> "C"
    }
}

fun getGradeWithSwitch2(score: Int): String {
    return when (score) {
        in 90..99 -> "A"
        in 80..89 -> "B"
        else -> "C"
    }
}

fun startWithA(obj: Any): Boolean {
    return when (obj) {
        is String -> obj.startsWith("A") // Smart Cast
        else -> false
    }
}

fun judgeNumber(number: Int) {
    return when (number) {
        1, 0, -1 -> println("1, 0, -1 맞음")
        else -> println("1, 0, -1 아님")
    }
}

fun judgeNumber2(number: Int) {
    return when {
        number == 0 -> println("0")
        number % 2 == 0 -> println("짝수")
        else -> println("홀수")
    }
}