package com.montilivi.yacht

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
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
					val vm =  YachtViewModel()
					DicePanel(vm)
				}
			}
		}
	}
	@RequiresApi(Build.VERSION_CODES.N)
	@Preview(showBackground = true)
	@Composable
	fun DefaultPreview() {
		YachtTheme() {
			DicePanel(vm = YachtViewModel())
		}
	}

	@RequiresApi(Build.VERSION_CODES.N)
	@Composable
	fun DicePanel(vm : YachtViewModel) {
		YachtTheme {
			Column(modifier = Modifier.padding(10.dp)) {

				DiceBox(vm.diceList.collectAsState(), vm.clickDice)

				DiceButton(vm.clickRollButton, vm.canClickRollButton)
			}
		}
	}
	@Composable
	fun DiceBox(diceList: State<List<Dice>>, clickDice: (Dice) -> Unit) {
		Row(Modifier.background(MaterialTheme.colorScheme.secondary))
		{
			for (selectedDice in diceList.value)
			{
				Box(modifier = Modifier.weight(1f)) {
					Image(painter = painterResource(id = diceIcons[selectedDice.number]),
						contentDescription = null,
						modifier = Modifier
							.padding(5.dp)
							.clickable { clickDice(selectedDice) })
					if (selectedDice.isLocked)
					{
						Image(painter = painterResource(id = lockIcon),
							contentDescription = null,
							Modifier
								.align(Alignment.BottomEnd)
								.padding(bottom = 10.dp, end = 5.dp))
					}
				}
			}
		}
	}
	@RequiresApi(Build.VERSION_CODES.N)
	@Composable
	fun DiceButton(clickRollButton: () -> Unit, canClickRollButton: Boolean) {
		Row(Modifier.background(MaterialTheme.colorScheme.secondary)) {
			Button(
				onClick = clickRollButton,
				Modifier.weight(1f),
				enabled = canClickRollButton
			) {
				Text(text = "Tira!")
			}
		}
	}
}