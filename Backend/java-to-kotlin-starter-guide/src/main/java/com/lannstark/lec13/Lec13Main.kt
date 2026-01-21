package com.lannstark.lec13

fun main() {
    // 중첩 클래스 자체를 실무에서 잘 안쓰긴 했는데, 문법 강의니까...
}

class House (
    val address: String,
    val livingLoom: LivingRoom,
    val livingLoomInner: LivingRoomInner,
){
    // (권장) 바깥 클래스를 참조하지 않는 클래스: static
    class LivingRoom(area: String) {
    }

    // (권장X) 바깥 클래스를 참조하는 중첩 클래스: 디버깅 하기 어렵게 하는 안티 패턴(Effective Java)
    inner class LivingRoomInner(area: String) {
        val address: String
            get() = this@House.address
    }
}