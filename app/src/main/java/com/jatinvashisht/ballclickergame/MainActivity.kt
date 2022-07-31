package com.jatinvashisht.ballclickergame

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jatinvashisht.ballclickergame.ui.theme.BallClickerGameTheme
import com.jatinvashisht.ballclickergame.ui.theme.colorPrimary
import kotlinx.coroutines.delay
import kotlin.math.pow
import kotlin.math.roundToInt
import kotlin.math.sqrt
import kotlin.random.Random
import kotlin.time.Duration
import kotlin.time.ExperimentalTime

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BallClickerGameTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    MyUi()
                }
            }
        }
    }
}

@OptIn(ExperimentalTime::class)
@Composable
fun MyUi() {
    // to keep track of points
    val score = remember { mutableStateOf(0) }
    // to keep track if game/timer is running or not
    val inResetMode = remember { mutableStateOf(true) }
    // to keep record of seconds
    val timer = remember { mutableStateOf(30) }

    // this launched effect will run whenever reset mode and timer value changes
    LaunchedEffect(key1 = inResetMode.value, key2 = timer.value) {
        if (inResetMode.value) {
            timer.value = 30
            return@LaunchedEffect
        }
        if (timer.value > 0) {
            delay(duration = Duration.Companion.seconds(1))
            timer.value = --timer.value
        }else{
            inResetMode.value = true
        }

    }

    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Score(value = score.value.toString())
            StartAndResetButton(
                text = if (inResetMode.value) "Start" else "Reset",
                modifier = Modifier,
            ) {
                inResetMode.value = !inResetMode.value
                score.value = 0
            }
            CustomTimer(text = timer.value.toString())
        }
        BallClickerCanvas(enabled = !inResetMode.value){
            score.value++
        }
    }
}

@Composable
fun Score(value: String, modifier: Modifier = Modifier) {
    Text(
        text = value,
        modifier = modifier,
        style = TextStyle(
            color = MaterialTheme.colors.secondary,
            fontSize = 32.sp,
            fontWeight = FontWeight.Medium,
            shadow = Shadow(color = Color.Gray, blurRadius = 0.7f),
        )
    )
}

@Composable
fun StartAndResetButton(text: String, modifier: Modifier = Modifier, onButtonClick: () -> Unit) {
    Button(onClick = onButtonClick, modifier = modifier) {
        Text(text = text, modifier = Modifier.animateContentSize(tween(200)))
    }
}

@Composable
fun CustomTimer(text: String) {
    Text(
        text = text,
        style = TextStyle(
            color = MaterialTheme.colors.secondary,
            fontSize = 32.sp,
            fontWeight = FontWeight.Medium,
            shadow = Shadow(color = Color.Gray, blurRadius = 0.7f),
        )
    )
}

@Composable
fun BallClickerCanvas(
    radius: Float = 100f,
    enabled: Boolean = false,
    ballColor: Color = colorPrimary,
    onBallClick: () -> Unit = {},
) {

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        var ballPosition by remember{
            mutableStateOf(
                randomOffset(
                    radius = radius,
                    width = constraints.maxWidth,
                    height = constraints.maxHeight
                )
            )
        }
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(enabled) {
                    if (!enabled) {
                        return@pointerInput
                    }
                    detectTapGestures {
                        // finding if the tapped area falls within the range of ball
                        // we are using pythagoras theorem here
                        val distance = sqrt(
                            (it.x - ballPosition.x).pow(2) +
                                    (it.y - ballPosition.y).pow(2)
                        )
                        if (distance <= radius) {
                            ballPosition = randomOffset(
                                radius = radius,
                                width = constraints.maxWidth,
                                height = constraints.maxHeight
                            )
                            onBallClick()
                        }
                    }
                }
        ) {
            drawCircle(
                color = ballColor,
                radius = radius,
                center = ballPosition
            )
        }
    }
}

// helper function to find random position
// width and height values are needed because we don't want the ball to go beyond screen dimensions
private fun randomOffset(radius: Float, width: Int,height: Int): Offset{
    return Offset(
        x = Random.nextInt(radius.roundToInt(), width - radius.roundToInt()).toFloat(),
        y = Random.nextInt(radius.roundToInt(), height - radius.roundToInt()).toFloat()
    )
}