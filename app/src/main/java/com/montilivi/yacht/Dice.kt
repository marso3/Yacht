package com.montilivi.yacht

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlin.random.Random

//data class Dice(val number : Int, val momentLocked : Int, val isLocked : Boolean)
class Dice
{
	private var _number by mutableStateOf(0)
	val number = _number
	var momentLocked by mutableStateOf(-1)
	var isLocked by mutableStateOf(false)

	fun roll(): Dice
	{
		_number = Random.nextInt(1,7)
		return this
	}
	fun reset(): Dice {
		_number = 0
		momentLocked = -1
		isLocked = false
		return this
	}
}