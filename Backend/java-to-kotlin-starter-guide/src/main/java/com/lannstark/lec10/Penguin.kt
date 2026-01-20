package com.lannstark.lec10

class Penguin(
    species: String,
    val wingCount: Int = 2
) : Animal(species, 2), Swimable, Flyable {

    override fun act() {
        super<Swimable>.act()
        super<Flyable>.act()
    }

    override fun move() {
        println("펭귄 이동")
    }

    override val legCount: Int
        get() = this.wingCount + super.legCount
}