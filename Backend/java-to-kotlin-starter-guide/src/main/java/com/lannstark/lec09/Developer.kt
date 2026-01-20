package com.lannstark.lec09

fun main() {
    /**
     * backing field 를 사용해서 getter 자체를 변경해서 반환하게끔 가능은 하지만,
     * 보통은 그렇게까진 안 하고 custom getter 사용.
     * */
    val developer1 = Developer1("phj", listOf("Java", "TS", "Kotlin"))
    println(developer1.name)
    println(developer1.languages)

    val developer2 = Developer2("phj", listOf("Java", "TS", "Kotlin"))
    println(developer2.nameUpperCase)
    println(developer2.languagesUpperCase)
}

class Developer1(
    name: String,
    languages: List<String>,
) {
    val name: String = name
        get() {
            return field.uppercase()
        }
    val languages: List<String> = languages
        get() {
            val languagesUpperCase: MutableList<String> = ArrayList()

            field.forEach { language -> languagesUpperCase.add(language.uppercase()) }

            return languagesUpperCase
        }
}

class Developer2(
    val name: String,
    val languages: List<String>,
) {
    val nameUpperCase: String
        get() = name.uppercase()
    val languagesUpperCase: List<String>
        get() {
            val languagesUpperCase: MutableList<String> = ArrayList()
            for (language in languages) {
                languagesUpperCase.add(language.uppercase())
            }
            return languagesUpperCase
        }
}