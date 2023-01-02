package com.montilivi.yacht

data class Dice(
    val itemId : Int,
    val number : Int,
    val momentLocked : Int,
    val isLocked : Boolean) {

    fun copy(
        number : Int = this.number,
        momentLocked : Int = this.momentLocked,
        isLocked : Boolean = this.isLocked) = Dice(this.itemId, number, momentLocked, isLocked)
}