package com.montilivi.yacht

import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.montilivi.yacht.YachtViewModel.Companion.diceIcons
import com.montilivi.yacht.YachtViewModel.Companion.lockIcon
import com.montilivi.yacht.ui.theme.YachtTheme

class MainActivity : ComponentActivity()
{
	@RequiresApi(Build.VERSION_CODES.N)
	override fun onCreate(savedInstanceState: Bundle?)
	{
		super.onCreate(savedInstanceState)
		setContent {
			YachtTheme { // A surface container using the 'background' color from the theme
				Surface(modifier = Modifier.fillMaxSize(),
					color = MaterialTheme.colorScheme.background
				) {
					Screen(YachtViewModel())
				}
			}
		}
	}
	@RequiresApi(Build.VERSION_CODES.N)
	@Preview(showBackground = true)
	@Composable
	fun DefaultPreview() {
		YachtTheme {
			Screen(vm = YachtViewModel())
		}
	}

	@OptIn(ExperimentalMaterial3Api::class)
	@RequiresApi(Build.VERSION_CODES.N)
	@Composable
	fun Screen(vm : YachtViewModel) {
		Scaffold(
			topBar =
			{ TopPanel(vm.player.globalScore, vm.player.globalScore) },

			content =
			{ innerPadding -> AssignmentsPanel(vm.player, vm.player, innerPadding) },

			bottomBar =
			{ DicePanel(vm = vm) },

			modifier = Modifier.padding(5.dp)
		)
	}
	//region Top Panel Block
	@OptIn(ExperimentalMaterial3Api::class)
	@Composable
	fun TopPanel(p1Score : Int, p2Score : Int) {
		CenterAlignedTopAppBar(
			title = {
				Row(horizontalArrangement = Arrangement.SpaceEvenly) {
					ScoreBox(p1Score, "Player 1")
					ScoreBox(p2Score, "Player 2")
				}
			}
		)
	}
	@Composable
	fun ScoreBox(score : Int, name : String) {
		Column() {
			Text(text = score.toString())
			Text(text = name)
		}
	}
	//endregion

	//region Content Block
	@Composable
	fun AssignmentsPanel(player1 : Player, player2: Player, padding : PaddingValues) {
		Row(Modifier.padding(padding)) {

			AssignmentColumn(player1.minorScores)

			//AssignmentColumn()
		}
	}
	@Composable
	fun AssignmentColumn(minorScores : List<Int>) {
		Column() {

			AssignmentRow(1, minorScores[0])

			AssignmentRow(2, minorScores[1])

			AssignmentRow(3, minorScores[2])

			AssignmentRow(4, minorScores[3])

			AssignmentRow(5, minorScores[4])

			AssignmentRow(6, minorScores[5])
		}
	}
	@Composable
	fun AssignmentRow(descriptionIcon : Int, score : Int) {
		Row() {
			Image(
				painter = painterResource(id = diceIcons[0]),
				contentDescription = "temp",
				modifier = Modifier.weight(1f))
			Text(text = score.toString())
		}
	}
	//endregion

	//region Dice Block
	@RequiresApi(Build.VERSION_CODES.N)
	@Composable
	fun DicePanel(vm : YachtViewModel) {
		Column(modifier = Modifier.padding(8.dp)) {

			DiceBox(vm.diceList, vm.clickDice)

			RollButton(vm.clickRollButton, vm.canClickRollButton)
		}
	}
	@Composable
	fun DiceBox(diceList: List<Dice>, clickDice : (Int) -> Unit) {
		Row(Modifier.background(MaterialTheme.colorScheme.secondary))
		{
			for (selectedDice in diceList) {
				Box(modifier = Modifier.weight(1f)) {
					Image(painter = painterResource(id = diceIcons[selectedDice.number]),
						contentDescription = null,
						modifier = Modifier
							.padding(5.dp)
							.clickable { clickDice(selectedDice.itemId) })
					if (selectedDice.isLocked) {
						Image(
							painter = painterResource(id = lockIcon),
							contentDescription = null,
							Modifier
								.align(Alignment.BottomEnd)
								.padding(bottom = 10.dp, end = 5.dp)
						)
					}
				}
			}
		}
	}
	@Composable
	fun RollButton(clickRollButton: () -> Unit, canClickRollButton: Boolean) {
		Row(Modifier.background(MaterialTheme.colorScheme.secondary)) {
			Button(
				onClick = clickRollButton,
				Modifier.weight(1f),
				enabled = canClickRollButton
			) {
				Text(text = "Roll!")
			}
		}
	}
	//endregion
}