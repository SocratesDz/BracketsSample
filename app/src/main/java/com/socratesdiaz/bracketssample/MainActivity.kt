package com.socratesdiaz.bracketssample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.socratesdiaz.bracketssample.ui.theme.BracketsSampleTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BracketsSampleTheme {
                Scaffold { padding ->
                    BracketsScreen(modifier = Modifier.padding(padding))
                }
            }
        }
    }
}

@Composable
fun BracketsScreen(modifier: Modifier = Modifier) {
    val canvasSize = remember { 2000.dp }
    var scale by remember { mutableFloatStateOf(1f) }
    var offsetX by remember { mutableFloatStateOf(0f) }
    var offsetY by remember { mutableFloatStateOf(0f) }
    val state = rememberTransformableState { zoomChange, panChange, _ ->
        scale *= zoomChange
        offsetX += panChange.x * scale
        offsetY += panChange.y * scale
    }
    GridBox(modifier = modifier
        .requiredSize(canvasSize)
        .graphicsLayer(
            scaleX = scale,
            scaleY = scale,
            translationX = offsetX,
            translationY = offsetY,
        )
        .transformable(state)
    ) {
        BracketNode(modifier = Modifier
            .width(100.dp)
            .absoluteOffset(canvasSize/2 - 50.dp * 3, canvasSize/2 - 50.dp))

        BracketNode(modifier = Modifier
            .width(100.dp)
            .absoluteOffset(canvasSize/2 + 50.dp, canvasSize/2 - 50.dp * 3))
    }
}

@Composable
fun BracketNode(modifier: Modifier = Modifier) {
    Card(border = BorderStroke(1.dp, Color.Gray), modifier = modifier) {
        Text("Player 1", modifier = Modifier.padding(4.dp))
        HorizontalDivider()
        Text("Player 2", modifier = Modifier.padding(4.dp))
    }
}

@Composable
fun GridBox(modifier: Modifier, content: @Composable () -> Unit) {
    Box(modifier = modifier.drawWithContent {
        val defaultColor = Color.LightGray
        val rectSize = 50
        drawRect(
            defaultColor,
            Offset.Zero,
            size = size,
            style = Stroke(1.0f, pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 25f)))
        )
        for (x in 0..size.width.toInt() step rectSize) {
            val posX = x.toFloat()
            val posY = 0f
            drawLine(defaultColor, start = Offset(posX, posY), end = Offset(posX, size.height))
        }
        for (y in 0..size.height.toInt() step rectSize) {
            val posX = 0f
            val posY = y.toFloat()
            drawLine(defaultColor, start = Offset(posX, posY), end = Offset(size.width, posY))
        }

        drawPath(
            path = Path().apply {
                // Using arbitrary positions for drawing lines
                val x1 = size.width/2 - (20.dp.toPx() * 3)
                val y1 = size.height/2 - 40.dp.toPx()
                moveTo(x1, y1)
                relativeLineTo(30.dp.toPx(), 0f)
                relativeMoveTo(0.dp.toPx(), 0f)
                relativeQuadraticBezierTo(20.dp.toPx(), 0f, 20.dp.toPx(), -20.dp.toPx())
                relativeLineTo(0.dp.toPx(), -50.dp.toPx())
                relativeQuadraticBezierTo(0.dp.toPx(), -20.dp.toPx(), 20.dp.toPx(), -20.dp.toPx())
                relativeLineTo(50.dp.toPx(), 0.dp.toPx())
            },
            color = defaultColor,
            style = Stroke(width = 5f)
        )

        drawContent()
    }) {
        content()
    }
}

@Preview
@Composable
fun BracketNodePreview() {
    BracketNode()
}

@Preview
@Composable
fun BracketsScreenPreview() {
    BracketsScreen(modifier = Modifier.fillMaxSize())
}