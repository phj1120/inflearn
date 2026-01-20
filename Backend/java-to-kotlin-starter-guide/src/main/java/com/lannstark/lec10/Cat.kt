package com.lannstark.lec10

class Cat(
    species: String,
) : Animal(species, 4) {

    override fun move() {
        print("고양이 이동")
    }
}