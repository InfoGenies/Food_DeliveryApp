package com.example.fooddelivery.utils

// let, apply, run, with, and also : With Differences
// here we use this funtcion the avoid redundancy
class Kotlin_Scoope_Function {
    fun main() {
        // let : to verify if the objet is not null
        var person = Personne().let {
            it.name = "Amir"
            it.age = 28
            " this the result of the let"
        }
        // the OutPut is : this the result of the let
        println("person")
        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////

        // With : accept abject and return the laste line
        var person2 = Personne()
        var result: String = with(person2) {
            this.name = "Amir"
            this.age = 25
            " this the result of With "
        }
        // the OutPut is : this the result of With
        print(result)
        println("the name of this object is ${person2.name} and the ege is ${person2.age}")

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        // Run  : Let + With it verify the nullable and return the last line
        var person3: Personne? = Personne()
        var res = person3?.run {
            this.name = ""
            this.age = 25
            " this the result of Run "
        }
        // the OutPut of this is : this the result of Run  :
        print(res)

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        // Apply  : it excute all the line but the return is the object
        val person4: Personne = Personne().apply {
            this.name = "zaki"
            this.age = 25
        }
        // the OutPut is the object
        println(person4.toString())

    }


}

class Personne {
    var name: String = "ZAki"
    var age: Int = 25

}