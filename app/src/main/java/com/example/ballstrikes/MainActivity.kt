package com.example.ballstrikes

import androidx.compose.runtime.MutableState
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ballstrikes.ui.theme.BallStrikesTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BallStrikesTheme {
                BallStrikesApp()
            }
        }
    }
}

@Composable
fun BallStrikesApp() {
    // States to hold ball, strike, out, run, inning, and team counts
    val balls = remember { mutableStateOf(0) }
    val strikes = remember { mutableStateOf(0) }
    val outs = remember { mutableStateOf(0) }
    val team1Runs = remember { mutableStateOf(0) }
    val team2Runs = remember { mutableStateOf(0) }
    val team1TotalRuns = remember { mutableStateOf(0) }
    val team2TotalRuns = remember { mutableStateOf(0) }
    val inning = remember { mutableStateOf(1) }
    val currentTeam = remember { mutableStateOf(1) } // 1 for Team 1, 2 for Team 2
    val maxInnings = 9

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Inning: ${inning.value}",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(8.dp)
        )
        Text(
            text = "Current Team: Team ${currentTeam.value}",
            fontSize = 16.sp,
            modifier = Modifier.padding(8.dp)
        )
        Text(text = "Balls: ${balls.value}", modifier = Modifier.padding(8.dp))
        Text(text = "Strikes: ${strikes.value}", modifier = Modifier.padding(8.dp))
        Text(text = "Outs: ${outs.value}", modifier = Modifier.padding(8.dp))
        Text(text = "Yankees Score (Current Inning): ${team1Runs.value}", modifier = Modifier.padding(8.dp))
        Text(text = "Blue Jays Score (Current Inning): ${team2Runs.value}", modifier = Modifier.padding(8.dp))
        Text(text = "Yankees Total Runs: ${team1TotalRuns.value}", modifier = Modifier.padding(8.dp))
        Text(text = "Blue Jays Total Runs: ${team2TotalRuns.value}", modifier = Modifier.padding(8.dp))

        // Buttons for balls, strikes, and outs (horizontally aligned)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = { balls.value++ },
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp)
            ) {
                Text(text = "Ball", fontSize = 14.sp)
            }

            Button(
                onClick = {
                    strikes.value++
                    if (strikes.value == 3) {
                        outs.value++
                        strikes.value = 0
                        balls.value = 0
                    }

                    if (outs.value == 3) {
                        handleOuts(
                            outs,
                            balls,
                            strikes,
                            currentTeam,
                            team1Runs,
                            team2Runs,
                            team1TotalRuns,
                            team2TotalRuns,
                            inning,
                            maxInnings
                        )
                    }
                },
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp)
            ) {
                Text(text = "Strike", fontSize = 14.sp)
            }

            Button(
                onClick = {
                    outs.value++
                    if (outs.value == 3) {
                        handleOuts(
                            outs,
                            balls,
                            strikes,
                            currentTeam,
                            team1Runs,
                            team2Runs,
                            team1TotalRuns,
                            team2TotalRuns,
                            inning,
                            maxInnings
                        )
                    }
                },
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp)
            ) {
                Text(text = "Out", fontSize = 14.sp)
            }
        }

        // Buttons for runs
        Button(onClick = { team1Runs.value++ }, modifier = Modifier.fillMaxWidth()) {
            Text(text = "Yankees Score Run")
        }

        Button(onClick = { team2Runs.value++ }, modifier = Modifier.fillMaxWidth()) {
            Text(text = "Blue Jays Score Run")
        }

        // Reset button to clear all counts
        Button(
            onClick = {
                balls.value = 0
                strikes.value = 0
                outs.value = 0
                team1Runs.value = 0
                team2Runs.value = 0
                team1TotalRuns.value = 0
                team2TotalRuns.value = 0
                inning.value = 1
                currentTeam.value = 1
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Reset")
        }
    }
}

// Function to handle outs and team switching/inning advancement
fun handleOuts(
    outs: MutableState<Int>,
    balls: MutableState<Int>,
    strikes: MutableState<Int>,
    currentTeam: MutableState<Int>,
    team1Runs: MutableState<Int>,
    team2Runs: MutableState<Int>,
    team1TotalRuns: MutableState<Int>,
    team2TotalRuns: MutableState<Int>,
    inning: MutableState<Int>,
    maxInnings: Int
) {
    balls.value = 0
    strikes.value = 0
    outs.value = 0

    if (currentTeam.value == 1) {
        currentTeam.value = 2 // Switch to Team 2
    } else {
        // Add runs for both teams to total scores
        team1TotalRuns.value += team1Runs.value
        team2TotalRuns.value += team2Runs.value

        // Reset current inning runs
        team1Runs.value = 0
        team2Runs.value = 0

        if (inning.value < maxInnings) {
            inning.value++
        } else {
            println("Game Over!")
        }

        currentTeam.value = 1 // Reset to Team 1 for the next inning
    }
}

@Preview(showBackground = true)
@Composable
fun BallStrikesPreview() {
    BallStrikesTheme {
        BallStrikesApp()
    }
}
