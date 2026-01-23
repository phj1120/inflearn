package com.group.libraryapp.calculator

fun main() {
    val calculatorTest = CalculatorTest()
    calculatorTest.addTest()
    calculatorTest.minusTest()
    calculatorTest.multiplyTest()
    calculatorTest.divideTest()
    calculatorTest.divideExceptionTest()
}


class CalculatorTest {

    fun addTest() {
        // given: 테스트 대상을 만들어 준비하는 과정
        val calculator = Calculator(5)

        // when: 실제 우리가 테스트 하고 싶은 기능을 호출하는 과정
        calculator.add(3)

        // then: 호출 이후 의도한대로 결과가 나왔는지 확인하는 과정
        if( calculator.number != 8 ) {
            throw IllegalStateException()
        }
    }

    fun minusTest() {
        // given
        val calculator = Calculator(5)

        // when
        calculator.minus(3)

        // then
        if( calculator.number != 2 ) {
            throw IllegalStateException()
        }
    }

    fun multiplyTest() {
        // given
        val calculator = Calculator(5)

        // when
        calculator.multiply(3)

        // then
        if( calculator.number != 15 ) {
            throw IllegalStateException()
        }
    }

    fun divideTest() {
        // given
        val calculator = Calculator(5)

        // when
        calculator.divide(3)

        // then
        if( calculator.number != 1 ) {
            throw IllegalStateException()
        }
    }

    fun divideExceptionTest() {
        // given
        val calculator = Calculator(5)

        // when
        try {
            calculator.divide(0)
        } catch (e: IllegalArgumentException) {
            // then
            return
        } catch (e: Exception) {
            throw IllegalStateException()
        }
        throw IllegalStateException()
    }
}