package com.montilivi.yacht

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

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
	private var _diceList : MutableStateFlow<MutableList<Dice>> = MutableStateFlow(emptyList<Dice>().toMutableList())
	public val diceList  = _diceList
	private var diceRollCounter by mutableStateOf(0)
	private var _canClickRollButton by mutableStateOf(true)
	public val canClickRollButton = _canClickRollButton

	init {
		Start()
	}

	//Called on dice click
	val clickDice = { dice : Dice ->
		//enters when dice is reset
		if (diceRollCounter == 0) {
			dice.isLocked = false
		}

		//allows you to change lock state if on the same throw
		// or if on posterior throw but dice wasn't locked yet
		else if (dice.momentLocked == -1 ||
			(dice.momentLocked == diceRollCounter) ||
			(dice.momentLocked != diceRollCounter && !dice.isLocked))
		{
			dice.isLocked = !dice.isLocked
			dice.momentLocked = diceRollCounter
		}
	}

	val clickRollButton: () -> Unit = {
		var tempList = mutableListOf<Dice>()
		if (_canClickRollButton) {
			for (dice in _diceList.value) {
				if (!dice.isLocked) {
					tempList.add(dice.roll())
				}
			}
			diceRollCounter++
		}
		//reset roll state
		else {
			for (dice in _diceList.value) {
				tempList.add(dice.reset())
			}
			diceRollCounter = 0
		}
		_diceList.value = tempList
		if (diceRollCounter > 3)
			_canClickRollButton = false
	}

	private fun Start() {
		_diceList.value = emptyList<Dice>().toMutableList()

		var tempList = mutableListOf<Dice>()
		for (pos in 0 until diceNumber) {
			tempList.add(Dice())
		}
		_diceList.value = tempList
		diceRollCounter = 0
	}
}