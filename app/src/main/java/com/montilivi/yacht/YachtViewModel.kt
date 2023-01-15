package com.montilivi.yacht

import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import kotlin.random.Random

class YachtViewModel : ViewModel()
{
    companion object {
        val diceIcons = listOf(
            R.drawable.dice_0,
            R.drawable.dice_1,
            R.drawable.dice_2,
            R.drawable.dice_3,
            R.drawable.dice_4,
            R.drawable.dice_5,
            R.drawable.dice_6,
        )
        val assignmentDiceIcons = listOf(
            R.drawable.assignment_dice_1,
            R.drawable.assignment_dice_2,
            R.drawable.assignment_dice_3,
            R.drawable.assignment_dice_4,
            R.drawable.assignment_dice_5,
            R.drawable.assignment_dice_6,
        )
        val assignmentSpecialIcons = listOf(
            R.drawable.assignment_smallstraight,
            R.drawable.assignment_largestraight,
            R.drawable.assignment_fullhouse,
            R.drawable.assignment_poker,
            R.drawable.assignment_yacht,
            R.drawable.assignment_playerchoice,
        )
        const val lockIcon = R.drawable.lock_icon
        const val diceNumber = 5
    }

    //region Player attributes
    private var _player = mutableStateOf(Player())
    val player = _player.value
    private var _selectedScoreId = mutableStateOf(-1)
    val selectedScoreId = _selectedScoreId.value
    private var scorePreviousId by mutableStateOf(-1)
    //endregion

    //region Dice attributes
    private val _diceList = mutableStateListOf<Dice>()
    val diceList : List<Dice> = _diceList
    private var diceRollCounter by mutableStateOf(0)
    private var _canClickRollButton = mutableStateOf(true)
    val canClickRollButton = _canClickRollButton
    private var _canClickPlayButton = mutableStateOf(false)
    val canClickPlayButton = _canClickPlayButton
    //endregion

    //region Start

    init {
        start()
    }

    private fun start() {
        diceStart()
    }

    //endregion

    //region Dice events and functions

    val clickDice = { id : Int ->
        val index = _diceList.indexOfFirst { it.itemId == id }
        val number : Int
        val momentLocked : Int
        val isLocked : Boolean

        //enters when dice is reset
        if (diceRollCounter == 0) {
            number = 0
            momentLocked = -1
            isLocked = false
        }

        //allows you to change lock state if on the same throw
        // or if on posterior throw but dice wasn't locked yet
        else if (
            _diceList[index].momentLocked == -1 ||
            (_diceList[index].momentLocked == diceRollCounter) ||
            (_diceList[index].momentLocked != diceRollCounter && !_diceList[index].isLocked))
        {
            number = _diceList[index].number
            isLocked = !_diceList[index].isLocked
            momentLocked = diceRollCounter
        }

        else {
            number = _diceList[index].number
            momentLocked = _diceList[index].momentLocked
            isLocked = _diceList[index].isLocked
        }
        _diceList[index] = _diceList[index].copy(number, momentLocked, isLocked)
    }

    val clickRollButton: () -> Unit = {
        if (_canClickRollButton.value) {
            for (i in 0 until diceNumber) {
                if (!_diceList[i].isLocked)
                {
                    diceRoll(i)
                }
            }
            diceRollCounter++
        }
        //reset roll state
        else {
            diceStart()
        }

        _player.value.updateScores(_diceList)

        if (diceRollCounter > 2) {
            _canClickRollButton.value = false
        }
    }


    //region Dice functions
    private fun diceStart() {
        _diceList.clear()
        for (pos in 0 until diceNumber) {
            _diceList.add(Dice(pos, 0, -1, false))
        }
        for (i in 0 .. 11) {
            if (!_player.value.isDefinitiveScoreSet[i])
                _player.value._scoreTextColors.value[i] = Color.Black
        }
        diceRollCounter = 0
        _canClickRollButton.value = true
        _canClickPlayButton.value = false
    }

    private fun diceRoll(id : Int) {
        val index = _diceList.indexOfFirst { it.itemId == id }
        val number = Random.nextInt(1,7)
        _diceList[index] = _diceList[index].copy(number)
    }
    private fun diceReset(id: Int) {
        val index = _diceList.indexOfFirst { it.itemId == id }

        val number = 0
        val momentLocked = -1
        val isLocked = false

        _diceList[index] = _diceList[index].copy(number, momentLocked, isLocked)
    }
    //endregion

    //endregion

    //region Score events

    val clickPlayButton: () -> Unit = {

        _player.value.isDefinitiveScoreSet[_selectedScoreId.value] = true
        _player.value._scoreTextColors.value[_selectedScoreId.value] = Color.Yellow
        scorePreviousId = -1
        _selectedScoreId.value = -1
        _player.value.globalScore = _player.value.definitiveIndividualScores.sum()

        _player.value.updateScores(_diceList)
        diceStart()
    }

    val clickScore = { id : Int ->
        if (!_player.value.isDefinitiveScoreSet[id]) {
            _canClickPlayButton.value = true
            _selectedScoreId.value = id
            _player.value._scoreTextColors.value[_selectedScoreId.value] = Color.White

            //assign the score to the selected box
            _player.value.assignIndividualScore(id)

            if (scorePreviousId != -1 && _selectedScoreId.value != scorePreviousId)
                _player.value.definitiveIndividualScores[scorePreviousId] = 0

            scorePreviousId = id
        }
    }

    //endregion
}