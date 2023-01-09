package com.montilivi.yacht

import androidx.compose.runtime.*

class Player {
    var globalScore by mutableStateOf(0)

    //each array position represents a value from 1 to 6
    var minorScores : MutableState<IntArray> = mutableStateOf(IntArray(6) { 0 })

    var smallStraightScore by mutableStateOf(0)
    var largeStraightScore by mutableStateOf(0)
    var fullHouseScore by mutableStateOf(0)
    var pokerScore by mutableStateOf(0)
    var yachtScore by mutableStateOf(0)
    var playerChoiceScore by mutableStateOf(0)

    fun updateScores(diceList : List<Dice>) {

        resetPoints()

        for (dice in diceList) {
            minorScores.value         [ dice.number - 1] += dice.number
        }

        smallStraightScore = checkSmallStraight()
        largeStraightScore = checkLargeStraight()
        fullHouseScore = checkFullHouse()
        pokerScore = checkPoker()
        yachtScore = checkYacht()
        playerChoiceScore = checkPlayerChoice()
    }

    private fun checkSmallStraight(): Int {
        if (minorScores[0] == 0 || minorScores[5] == 0)
        {
            if (!minorScores.withIndex().any { it.value % (it.index+1) > 1 })
                return 30
        }
        return 0
    }

    private fun checkLargeStraight(): Int {
        if (minorScores.withIndex().all { it.value % (it.index+1) == 1 })
        {
            return 40
        }
        return 0
    }

    private fun checkFullHouse(): Int {
        val existsThree = minorScores.withIndex().any { it.value % (it.index+1) == 3 }
        val existsPair =  minorScores.withIndex().any { it.value % (it.index+1) == 2 }

        if (existsThree && existsPair)
        {
            return minorScores[minorScores.indexOfFirst { existsThree } + 1] +
                   minorScores[minorScores.indexOfFirst { existsPair } + 1]
        }
        return 0
    }

    private fun checkPoker(): Int {
        if (minorScores.withIndex().any { it.value % (it.index+1) == 4 })
        {
            return minorScores.sum()
        }
        return 0
    }

    private fun checkYacht(): Int {
        if (minorScores.withIndex().any { it.value % (it.index+1) == 5 })
        {
            return 50
        }
        return 0
    }

    private fun checkPlayerChoice(): Int {
        return minorScores.sum()
    }

    private fun resetPoints() {
        minorScores = IntArray(6) { 0 }
    }


}