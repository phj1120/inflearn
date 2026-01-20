package com.lannstark.lec10

abstract class Animal(
    protected val species: String,
    // 프로퍼티를 오버라이드 할 때
    // 추상 프로퍼티가 아니라면 open 키워드를 붙여줘야함.
    protected open val legCount: Int,
) {
    abstract fun move()
}