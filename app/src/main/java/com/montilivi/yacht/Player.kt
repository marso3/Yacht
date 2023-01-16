package com.montilivi.yacht

import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color

class Player {

    //each array position represents a value from 1 to 6 and the left column of the view
    var minorScores : IntArray by mutableStateOf(IntArray(6) { 0 })

    //region Temp Scores

    //this one's same as the other but used only for internal logic, doesn't take into
    //account when the user locks a score so the logic of the calculations still make sense
    private var logicMinorScores : IntArray by mutableStateOf(IntArray(6) { 0 })

    var smallStraightScore by mutableStateOf(0)
    var largeStraightScore by mutableStateOf(0)
    var fullHouseScore by mutableStateOf(0)
    var pokerScore by mutableStateOf(0)
    var yachtScore by mutableStateOf(0)
    var playerChoiceScore by mutableStateOf(0)
    //endregion

    var globalScore by mutableStateOf(0)
    var definitiveIndividualScores by mutableStateOf(IntArray(12) { 0 })
    private var _isDefinitiveScoreSet = mutableStateOf(Array(12) { false })
    val isDefinitiveScoreSet = _isDefinitiveScoreSet.value
    var _scoreTextColors = mutableStateOf(Array(12) { Color.Black })

    fun updateScores(diceList : List<Dice>) {

        //if the left column isn't locked, we set all of them to 0
        for (i in 0 .. 5) {
            if (!isDefinitiveScoreSet[i])
                minorScores[i] = 0
            logicMinorScores[i] = 0
        }

        //we populate the left column scores in function of the dicelist rolls
        if (diceList.first().number != 0) { //we check if the dice have been thrown
            for (dice in diceList) {
                if (!isDefinitiveScoreSet[dice.number - 1]) {
                    minorScores[dice.number - 1] += dice.number
                }
                logicMinorScores[dice.number - 1] += dice.number
            }
        }

        if (!isDefinitiveScoreSet[6]) smallStraightScore = checkSmallStraight()
        if (!isDefinitiveScoreSet[7]) largeStraightScore = checkLargeStraight()
        if (!isDefinitiveScoreSet[8]) fullHouseScore = checkFullHouse()
        if (!isDefinitiveScoreSet[9]) pokerScore = checkPoker()
        if (!isDefinitiveScoreSet[10]) yachtScore = checkYacht()
        if (!isDefinitiveScoreSet[11]) playerChoiceScore = checkPlayerChoice()

        for (i in 0 .. 11) {
            if (definitiveIndividualScores[i] != 0 && !isDefinitiveScoreSet[i])
                assignIndividualScore(i)
        }

    }

    //region Scores' logic
    private fun checkSmallStraight(): Int {
        var flag = 0
        for (i in 0 .. 5) {
            if (logicMinorScores[i] >= 1) flag++
            else if (logicMinorScores[i] == 0) flag = 0
            if (flag == 4) return 30
        }
        return 0
    }

    private fun checkLargeStraight(): Int {
        if (logicMinorScores[0] == 0 || logicMinorScores[5] == 0)
        {
            if (logicMinorScores.withIndex().all { it.value / (it.index+1) > 0 } &&
                !logicMinorScores.withIndex().any { it.value / (it.index+1) > 1 })
                return 40
        }
        return 0
    }

    private fun checkFullHouse(): Int {
        val existsThree = logicMinorScores.withIndex().any { it.value / (it.index+1) == 3 }
        val existsPair =  logicMinorScores.withIndex().any { it.value / (it.index+1) == 2 }

        if (existsThree && existsPair)
        {
            return logicMinorScores.sum()
        }
        return 0
    }

    private fun checkPoker(): Int {
        if (logicMinorScores.withIndex().any { it.value / (it.index+1) >= 4 })
        {
            return logicMinorScores.maxOrNull() ?: 0
        }
        return 0
    }

    private fun checkYacht(): Int {
        if (logicMinorScores.withIndex().any { it.value / (it.index+1) == 5 })
        {
            return 50
        }
        return 0
    }

    private fun checkPlayerChoice(): Int {
        return logicMinorScores.sum()
    }
    //endregion

    fun assignIndividualScore(id : Int) {
        when (id) {
            0 -> if (definitiveIndividualScores[id] == 0) definitiveIndividualScores[id] =
                minorScores[id]
            1 -> if (definitiveIndividualScores[id] == 0) definitiveIndividualScores[id] =
                minorScores[id]
            2 -> if (definitiveIndividualScores[id] == 0) definitiveIndividualScores[id] =
                minorScores[id]
            3 -> if (definitiveIndividualScores[id] == 0) definitiveIndividualScores[id] =
                minorScores[id]
            4 -> if (definitiveIndividualScores[id] == 0) definitiveIndividualScores[id] =
                minorScores[id]
            5 -> if (definitiveIndividualScores[id] == 0) definitiveIndividualScores[id] =
                minorScores[id]
            6 -> if (definitiveIndividualScores[id] == 0) definitiveIndividualScores[id] =
                smallStraightScore
            7 -> if (definitiveIndividualScores[id] == 0) definitiveIndividualScores[id] =
                largeStraightScore
            8 -> if (definitiveIndividualScores[id] == 0) definitiveIndividualScores[id] =
                fullHouseScore
            9 -> if (definitiveIndividualScores[id] == 0) definitiveIndividualScores[id] =
                pokerScore
            10 -> if (definitiveIndividualScores[id] == 0) definitiveIndividualScores[id] =
                yachtScore
            11 -> if (definitiveIndividualScores[id] == 0) definitiveIndividualScores[id] =
                playerChoiceScore
        }
    }
}
