package com.socratesdiaz.bracketssample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.draggable2D
import androidx.compose.foundation.gestures.rememberDraggable2DState
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.DrawStyle
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.BrushPainter
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.onPlaced
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.offset
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
            .absoluteOffset(canvasSize/2 - 50.dp, canvasSize/2 - 50.dp))
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
    Box(modifier = modifier.drawBehind {
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