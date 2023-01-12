package com.montilivi.yacht

import androidx.compose.runtime.*

class Player {

    //region Temp Scores
    //each array position represents a value from 1 to 6
    var minorScores : IntArray by mutableStateOf(IntArray(6) { 0 })

    var smallStraightScore by mutableStateOf(0)
    var largeStraightScore by mutableStateOf(0)
    var fullHouseScore by mutableStateOf(0)
    var pokerScore by mutableStateOf(0)
    var yachtScore by mutableStateOf(0)
    var playerChoiceScore by mutableStateOf(0)
    //endregion

    var globalScore by mutableStateOf(0)
    var definitiveIndividualScores by mutableStateOf(IntArray(12) { 0 })
    var isDefinitiveScoreSet by mutableStateOf(Array(12) { false })

    fun updateScores(diceList : List<Dice>) {

        minorScores = IntArray(6) { 0 }

        for (dice in diceList) {
            minorScores[ dice.number - 1] += dice.number
        }

        smallStraightScore = checkSmallStraight()
        largeStraightScore = checkLargeStraight()
        fullHouseScore = checkFullHouse()
        pokerScore = checkPoker()
        yachtScore = checkYacht()
        playerChoiceScore = checkPlayerChoice()
    }

    //region Scores' logic
    private fun checkSmallStraight(): Int {
        var flag = 0
        for (i in 0 .. 5) {
            if (minorScores[i] >= 1) flag++
            else if (minorScores[i] == 0) flag = 0
            if (flag == 4) return 30
        }
        return 0
    }

    private fun checkLargeStraight(): Int {
        if (minorScores[0] == 0 || minorScores[5] == 0)
        {
            if (!minorScores.withIndex().any { it.value / (it.index+1) > 1 })
                return 40
        }
        return 0
    }

    private fun checkFullHouse(): Int {
        val existsThree = minorScores.withIndex().any { it.value / (it.index+1) == 3 }
        val existsPair =  minorScores.withIndex().any { it.value / (it.index+1) == 2 }

        if (existsThree && existsPair)
        {
            return minorScores.sum()
        }
        return 0
    }

    private fun checkPoker(): Int {
        if (minorScores.withIndex().any { it.value / (it.index+1) >= 4 })
        {
            return minorScores.maxOrNull() ?: 0
        }
        return 0
    }

    private fun checkYacht(): Int {
        if (minorScores.withIndex().any { it.value / (it.index+1) == 5 })
        {
            return 50
        }
        return 0
    }

    private fun checkPlayerChoice(): Int {
        return minorScores.sum()
    }
    //endregion

    private fun resetPoints() {
        minorScores = IntArray(6) { 0 }
    }

    fun lockDefinitiveScore() {

    }
}
