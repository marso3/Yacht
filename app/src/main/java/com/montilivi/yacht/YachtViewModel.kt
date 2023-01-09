package com.montilivi.yacht

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
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
        const val lockIcon = R.drawable.lock_icon
        const val diceNumber = 5
    }
    //region Attributes
    private var _player by mutableStateOf(Player())
    val player = _player

    //region Dice attributes
    private val _diceList = mutableStateListOf<Dice>()
    val diceList : List<Dice> = _diceList
    private var diceRollCounter by mutableStateOf(0)
    private var _canClickRollButton by mutableStateOf(true)
    val canClickRollButton = _canClickRollButton
    //endregion

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
        if (_canClickRollButton) {
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
            for (dice in _diceList) {
                diceReset(dice.itemId)
            }
            diceRollCounter = 0
        }

        if (diceRollCounter > 3)
            _canClickRollButton = false

        _player.updateScores(_diceList)
    }

    //region Dice functions
    private fun diceStart() {
        for (pos in 0 until diceNumber) {
            _diceList.add(Dice(pos, 0, -1, false))
        }
        diceRollCounter = 0
        _canClickRollButton = true
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
}