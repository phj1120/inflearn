package com.group.libraryapp.calculator

fun main() {
    val calculator = Calculator(10)
    calculator.add(5)
        .minus(2)
        .multiply(3)
        .divide(2)
    println(calculator)
}

// getter 만 열고 싶어
// kotlin 의 backingField 공식 컨벤션 _변수명
// 사실 추천은 그냥 열어두고 setter 안쓰는 방향으로!
class Calculator(private var _number: Int) {

    val number: Int
        get() = _number

    fun add(operand: Int): Calculator {
        this._number += operand
        return this
    }

    fun minus(operand: Int): Calculator {
        this._number -= operand
        return this
    }

    fun multiply(operand: Int): Calculator {
        this._number *= operand
        return this
    }

    fun divide(operand: Int): Calculator {
        if (operand == 0) throw IllegalArgumentException("Division by zero")
        this._number /= operand
        return this
    }
}