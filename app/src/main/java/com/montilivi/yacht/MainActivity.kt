@file:OptIn(ExperimentalTextApi::class)

package com.montilivi.yacht

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.montilivi.yacht.YachtViewModel.Companion.assignmentDiceIcons
import com.montilivi.yacht.YachtViewModel.Companion.assignmentSpecialIcons
import com.montilivi.yacht.YachtViewModel.Companion.diceIcons
import com.montilivi.yacht.YachtViewModel.Companion.lockIcon

class MainActivity : ComponentActivity()
{
	var vm = YachtViewModel()
	@RequiresApi(Build.VERSION_CODES.N)
	override fun onCreate(savedInstanceState: Bundle?)
	{
		super.onCreate(savedInstanceState)
		setContent {
			MaterialTheme { // A surface container using the 'background' color from the theme
				Surface(modifier = Modifier.fillMaxSize(),
					color = MaterialTheme.colorScheme.background
				) {
					Screen(vm)
				}
			}
		}
	}
	@RequiresApi(Build.VERSION_CODES.N)
	@Preview(showBackground = true)
	@Composable
	fun DefaultPreview() {
		MaterialTheme {
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
			{ innerPadding -> AssignmentsPanel(vm.player, vm.player,
				vm.clickScore, vm.selectedScoreId, innerPadding) },

			bottomBar =
			{ DicePanel(vm = vm) },

			modifier = Modifier
				.padding(5.dp)
				.background(MaterialTheme.colorScheme.background)
		)
	}
	//region Top Panel Block
	@OptIn(ExperimentalMaterial3Api::class)
	@Composable
	fun TopPanel(p1Score : Int, p2Score : Int) {
		CenterAlignedTopAppBar(
			title = {
				Row() {
					ScoreBox(p1Score, "Player 1", Modifier.weight(1f))
				}
			}
		)
	}
	@Composable
	fun ScoreBox(score : Int, name : String, modifier: Modifier) {
		Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
			Text(text = score.toString())
			Text(text = name)
		}
	}
	//endregion

	//region Content Block
	@Composable
	fun AssignmentsPanel(player1 : Player, player2: Player, clickScore : (Int) -> Unit,
						 selectedScoreId : Int, padding : PaddingValues) {
		Row(Modifier.padding(padding)) {

			Box(Modifier.weight(1f)) {
				AssignmentLeftColumn(player1.minorScores, player1.isDefinitiveScoreSet,
					clickScore, selectedScoreId, player1.scoreTextColors)
			}

			Box(Modifier.weight(1f)) {
				AssignmentRightColumn(
					player1.smallStraightScore,
					player1.largeStraightScore,
					player1.fullHouseScore,
					player1.pokerScore,
					player1.yachtScore,
					player1.playerChoiceScore,
					player1.isDefinitiveScoreSet,
					clickScore, selectedScoreId, player1.scoreTextColors
				)
			}

		}
	}
	@Composable
	fun AssignmentLeftColumn(minorScores : IntArray, isDefinitiveScoreSet : Array<Boolean>,
							 clickScore: (Int) -> Unit, selectedScoreId: Int, scoreTextColors : Array<Color>) {
		Column(Modifier.fillMaxWidth()) {
			for (i in 0 .. 5) {
				AssignmentRow(i, clickScore, assignmentDiceIcons[i], minorScores[i],
					selectedScoreId, isDefinitiveScoreSet, scoreTextColors[i],
					Modifier.weight(1f))
			}
		}
	}
	@Composable
	fun AssignmentRightColumn(smallStraightScore: Int, largeStraightScore: Int,
							  fullHouseScore: Int, pokerScore: Int,
							  yachtScore: Int, playerChoiceScore: Int,
							  isDefinitiveScoreSet : Array<Boolean>,
							  clickScore: (Int) -> Unit, selectedScoreId: Int,
							  scoreTextColors : Array<Color>
							  ) {
		Column(Modifier.fillMaxWidth()) {

			AssignmentRow(6, clickScore, assignmentSpecialIcons[0],
				smallStraightScore, selectedScoreId, isDefinitiveScoreSet,
				scoreTextColors[6], Modifier.weight(1f))

			AssignmentRow(7, clickScore, assignmentSpecialIcons[1],
				largeStraightScore, selectedScoreId, isDefinitiveScoreSet,
				scoreTextColors[7], Modifier.weight(1f))

			AssignmentRow(8, clickScore, assignmentSpecialIcons[2],
				fullHouseScore, selectedScoreId, isDefinitiveScoreSet,
				scoreTextColors[8], Modifier.weight(1f))

			AssignmentRow(9, clickScore, assignmentSpecialIcons[3],
				pokerScore, selectedScoreId, isDefinitiveScoreSet,
				scoreTextColors[9], Modifier.weight(1f))

			AssignmentRow(10, clickScore, assignmentSpecialIcons[4],
				yachtScore, selectedScoreId, isDefinitiveScoreSet,
				scoreTextColors[10], Modifier.weight(1f))

			AssignmentRow(11, clickScore, assignmentSpecialIcons[5],
				playerChoiceScore, selectedScoreId, isDefinitiveScoreSet,
				scoreTextColors[11], Modifier.weight(1f))
		}
	}
	@Composable
	fun AssignmentRow(scoreId : Int, clickScore: (Int) -> Unit,
					  assignmentIcon : Int, score : Int,
					  selectedScoreId : Int, isDefinitiveScoreSet : Array<Boolean>,
					  scoreTextColor: Color,
					  modifier: Modifier = Modifier) {

		Row(modifier = modifier.then(Modifier.padding(5.dp))) {
			Image(
				painter = painterResource(id = assignmentIcon),
				contentDescription = null,
				Modifier.weight(1f)
				)
			Text(text = score.toString(),
				Modifier
					.fillMaxHeight()
					.weight(1f)
					.background(MaterialTheme.colorScheme.secondary)
					.clickable {
						clickScore(scoreId) },
				color = scoreTextColor,
				textAlign = TextAlign.Center,
				fontSize = 40.sp
			)
		}
	}
	//endregion

	//region Dice Block
	@RequiresApi(Build.VERSION_CODES.N)
	@Composable
	fun DicePanel(vm : YachtViewModel) {
		Column(modifier = Modifier.padding(8.dp)) {

			DiceBox(vm.diceList, vm.clickDice)

			Row() {

				RollButton(vm.clickRollButton, vm.canClickRollButton.value, Modifier.weight(1f))

				PlayButton(vm.clickPlayButton, vm.canClickPlayButton.value, Modifier.weight(1f))
			}
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
	fun RollButton(clickRollButton: () -> Unit, canClickRollButton: Boolean, modifier: Modifier) {
		Row(modifier = modifier.then(Modifier.background(MaterialTheme.colorScheme.secondary))) {
			Button(
				onClick = clickRollButton,
				Modifier.weight(1f),
				enabled = canClickRollButton
			) {
				Text(text = "Roll")
			}
		}
	}
	@Composable
	fun PlayButton(clickPlayButton: () -> Unit, canClickPlayButton: Boolean, modifier: Modifier) {
		Row(modifier = modifier.then(Modifier.background(MaterialTheme.colorScheme.secondary))) {
			Button(
				onClick = clickPlayButton,
				Modifier.weight(1f),
				enabled = canClickPlayButton
			) {
				Text(text = "Play")
			}
		}
	}
	//endregion
}