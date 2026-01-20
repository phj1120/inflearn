package com.lannstark.lec07

import org.jetbrains.annotations.NotNull
import java.io.BufferedReader
import java.io.File
import java.io.FileReader

fun main() {
    println(parseIntOrThrow("123"))
//    println(parseIntOrThrow("abc"))

    println(parseIntOrNull("123"))
    println(parseIntOrNull("abc"))

    readFile()
    readFileTryWithResources(File(".").absolutePath + "/a.txt")
}

fun parseIntOrThrow(@NotNull str: String): Int {
    try {
        return str.toInt()
    } catch (e: NumberFormatException) {
        throw IllegalArgumentException("$str 은 숫자가 아닙니다.")
    }
}

fun parseIntOrNull(@NotNull str: String): Int? {
    return try {
        str.toInt()
    } catch (e: NumberFormatException) {
        println(e.message)
        null
    }
}

// 코틀린은 모든 예외를 UncheckedException 으로 취급한다.
fun readFile() {
    val currentFile = File(".")
    val file = File(currentFile.absolutePath + "/a.txt")
    val reader = BufferedReader(FileReader(file))
    println(reader.readLine()) // Java 의 경우 UncheckedException 으로 메서드에 throws 를 사용하거나, CheckedException 으로 바꿔주는 처리를 해야함.
    reader.close()
}

fun readFileTryWithResources(str: String) {
    // 코틀린은 람다가 중괄호라 reader -> {} 이런식으로 하면 람다를 선언한거지 사용하지 않아서. 원하는대로 동작 X
//    BufferedReader(FileReader(str)).use { reader -> {
//            println(reader.readLine())
//    }}
    BufferedReader(FileReader(str)).use { reader ->
        println(reader.readLine())
    }
}


