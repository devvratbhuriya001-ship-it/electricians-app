package com.example.core.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke

/**
 * Beautiful, custom Warm Sand hero illustration for the Electricians App.
 * Depicts city skyline silhouettes, electrical utility poles, curving power cables,
 * festoon lights, and an electrician on an extension ladder working on the grid.
 */
@Composable
fun ElectricianHeroArtwork(
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height

        // 1. Warm Sand Sky Gradient
        drawRect(
            brush = Brush.verticalGradient(
                colorStops = arrayOf(
                    0.0f to Color(0xFFFFF4E6),
                    0.55f to Color(0xFFF7E8D3),
                    1.0f to Color(0xFFFFF8EF)
                ),
                startY = 0f,
                endY = h
            ),
            size = size
        )

        // 2. Warm Sun / Energy Radial Glow
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(Color(0x33FFB45C), Color(0x00FFF8EF)),
                center = Offset(w * 0.45f, h * 0.25f),
                radius = w * 0.25f
            ),
            center = Offset(w * 0.45f, h * 0.25f),
            radius = w * 0.25f
        )

        // 3. Distant City Skyline Silhouettes (Faded sand #E8DBCF)
        val farSkyline = Path().apply {
            moveTo(w * 0.22f, h * 0.70f)
            lineTo(w * 0.28f, h * 0.70f)
            lineTo(w * 0.28f, h * 0.48f)
            lineTo(w * 0.35f, h * 0.48f)
            lineTo(w * 0.35f, h * 0.38f)
            lineTo(w * 0.38f, h * 0.38f)
            lineTo(w * 0.38f, h * 0.34f)
            lineTo(w * 0.39f, h * 0.34f)
            lineTo(w * 0.39f, h * 0.70f)
            lineTo(w * 0.45f, h * 0.70f)
            lineTo(w * 0.45f, h * 0.42f)
            lineTo(w * 0.52f, h * 0.42f)
            lineTo(w * 0.52f, h * 0.70f)
            lineTo(w * 0.58f, h * 0.70f)
            lineTo(w * 0.58f, h * 0.52f)
            lineTo(w * 0.65f, h * 0.52f)
            lineTo(w * 0.65f, h * 0.70f)
            lineTo(w * 0.72f, h * 0.70f)
            lineTo(w * 0.72f, h * 0.55f)
            lineTo(w * 0.76f, h * 0.55f)
            lineTo(w * 0.76f, h * 0.70f)
            close()
        }
        drawPath(farSkyline, Color(0xFFE8DBCF))

        // 4. Mid-distance City Skyline (Muted sand/brown #DECEC0)
        val midSkyline = Path().apply {
            moveTo(w * 0.15f, h * 0.75f)
            lineTo(w * 0.23f, h * 0.75f)
            lineTo(w * 0.23f, h * 0.54f)
            lineTo(w * 0.30f, h * 0.54f)
            lineTo(w * 0.30f, h * 0.75f)
            lineTo(w * 0.37f, h * 0.75f)
            lineTo(w * 0.37f, h * 0.48f)
            lineTo(w * 0.44f, h * 0.48f)
            lineTo(w * 0.44f, h * 0.30f)
            lineTo(w * 0.455f, h * 0.30f)
            lineTo(w * 0.455f, h * 0.25f)
            lineTo(w * 0.46f, h * 0.25f)
            lineTo(w * 0.46f, h * 0.48f)
            lineTo(w * 0.50f, h * 0.48f)
            lineTo(w * 0.50f, h * 0.75f)
            lineTo(w * 0.56f, h * 0.75f)
            lineTo(w * 0.56f, h * 0.58f)
            lineTo(w * 0.63f, h * 0.58f)
            lineTo(w * 0.63f, h * 0.75f)
            lineTo(w * 0.68f, h * 0.75f)
            lineTo(w * 0.68f, h * 0.46f)
            lineTo(w * 0.73f, h * 0.46f)
            lineTo(w * 0.73f, h * 0.40f)
            lineTo(w * 0.75f, h * 0.40f)
            lineTo(w * 0.75f, h * 0.75f)
            close()
        }
        drawPath(midSkyline, Color(0xFFDECEC0))

        // 5. Foreground Building Blocks (#D3C0B1)
        val fgBuildings = Path().apply {
            moveTo(w * 0.26f, h * 0.80f)
            lineTo(w * 0.36f, h * 0.80f)
            lineTo(w * 0.36f, h * 0.56f)
            lineTo(w * 0.43f, h * 0.56f)
            lineTo(w * 0.43f, h * 0.80f)
            lineTo(w * 0.49f, h * 0.80f)
            lineTo(w * 0.49f, h * 0.60f)
            lineTo(w * 0.56f, h * 0.60f)
            lineTo(w * 0.56f, h * 0.80f)
            lineTo(w * 0.61f, h * 0.80f)
            lineTo(w * 0.61f, h * 0.52f)
            lineTo(w * 0.70f, h * 0.52f)
            lineTo(w * 0.70f, h * 0.80f)
            close()
        }
        drawPath(fgBuildings, Color(0xFFD3C0B1))

        // 6. Glowing Building Window Dots
        val windowCols = listOf(Color(0xFFFFF4E6), Color(0xFFFFB45C))
        for (row in 0..3) {
            for (col in 0..2) {
                drawRect(
                    color = windowCols[(row + col) % 2],
                    topLeft = Offset(w * 0.28f + col * 12f, h * 0.58f + row * 14f),
                    size = Size(6f, 6f)
                )
            }
        }
        for (row in 0..2) {
            for (col in 0..1) {
                drawRect(
                    color = windowCols[(row + col) % 2],
                    topLeft = Offset(w * 0.51f + col * 12f, h * 0.62f + row * 14f),
                    size = Size(6f, 6f)
                )
            }
        }
        for (row in 0..3) {
            for (col in 0..1) {
                drawRect(
                    color = windowCols[(row + col) % 2],
                    topLeft = Offset(w * 0.63f + col * 14f, h * 0.55f + row * 14f),
                    size = Size(6f, 6f)
                )
            }
        }

        // 7. Ground / Baseline Foundation
        drawRect(
            color = Color(0xFFC6B2A1),
            topLeft = Offset(0f, h * 0.80f),
            size = Size(w, h * 0.20f)
        )

        // 8. Soft bottom fade to seamlessly merge with card background (#FFFFF8EF)
        drawRect(
            brush = Brush.verticalGradient(
                colorStops = arrayOf(
                    0.0f to Color(0x00FFF8EF),
                    0.7f to Color(0xEEFFF8EF),
                    1.0f to Color(0xFFFFF8EF)
                ),
                startY = h * 0.75f,
                endY = h
            ),
            topLeft = Offset(0f, h * 0.75f),
            size = Size(w, h * 0.25f)
        )

        // 9. LEFT: Utility Pole & Electrical Infrastructure
        val poleColor = Color(0xFF675B52)
        val poleX = w * 0.12f

        // Main Pole
        drawRect(
            color = poleColor,
            topLeft = Offset(poleX - 5f, h * 0.20f),
            size = Size(10f, h * 0.68f)
        )
        // Top Crossbar
        drawRect(
            color = poleColor,
            topLeft = Offset(poleX - 44f, h * 0.26f),
            size = Size(88f, 7f)
        )
        // Diagonal Braces
        drawLine(
            color = poleColor,
            start = Offset(poleX - 25f, h * 0.27f),
            end = Offset(poleX - 3f, h * 0.32f),
            strokeWidth = 3f
        )
        drawLine(
            color = poleColor,
            start = Offset(poleX + 25f, h * 0.27f),
            end = Offset(poleX + 3f, h * 0.32f),
            strokeWidth = 3f
        )

        // Insulators (Orange accents)
        val insulatorColor = Color(0xFFE86400)
        drawRect(insulatorColor, topLeft = Offset(poleX - 38f, h * 0.24f), size = Size(6f, 8f))
        drawRect(insulatorColor, topLeft = Offset(poleX - 3f, h * 0.24f), size = Size(6f, 8f))
        drawRect(insulatorColor, topLeft = Offset(poleX + 32f, h * 0.24f), size = Size(6f, 8f))

        // Lower Distribution Crossbar
        drawRect(
            color = poleColor,
            topLeft = Offset(poleX - 30f, h * 0.38f),
            size = Size(60f, 6f)
        )
        drawRect(insulatorColor, topLeft = Offset(poleX - 24f, h * 0.36f), size = Size(5f, 6f))
        drawRect(insulatorColor, topLeft = Offset(poleX + 19f, h * 0.36f), size = Size(5f, 6f))

        // Transformer Cylinder
        drawRect(
            color = Color(0xFF93877D),
            topLeft = Offset(poleX + 6f, h * 0.34f),
            size = Size(16f, 32f)
        )
        drawRect(
            color = poleColor,
            topLeft = Offset(poleX + 5f, h * 0.33f),
            size = Size(18f, 3f)
        )
        drawRect(
            color = poleColor,
            topLeft = Offset(poleX + 5f, h * 0.46f),
            size = Size(18f, 3f)
        )

        // Street Lamp Arm with Amber Glow
        val lampPath = Path().apply {
            moveTo(poleX, h * 0.42f)
            quadraticTo(poleX - 28f, h * 0.42f, poleX - 44f, h * 0.48f)
        }
        drawPath(lampPath, poleColor, style = Stroke(width = 3.5f))
        drawRect(poleColor, topLeft = Offset(poleX - 52f, h * 0.48f), size = Size(16f, 6f))
        // Street lamp glowing aura
        drawCircle(Color(0x4DFFB45C), radius = 18f, center = Offset(poleX - 44f, h * 0.50f))
        drawCircle(Color(0xFFF47A00), radius = 5f, center = Offset(poleX - 44f, h * 0.50f))

        // 10. Curving Overhead Power Cables
        val rightPoleX = w * 0.86f
        val wireColor1 = Color(0xFF675B52)
        val wireColor2 = Color(0xFF93877D)

        // Wire 1
        val wire1 = Path().apply {
            moveTo(poleX - 35f, h * 0.25f)
            quadraticTo(w * 0.48f, h * 0.36f, rightPoleX, h * 0.23f)
        }
        drawPath(wire1, wireColor1, style = Stroke(width = 2.2f))

        // Wire 2
        val wire2 = Path().apply {
            moveTo(poleX, h * 0.25f)
            quadraticTo(w * 0.50f, h * 0.40f, rightPoleX, h * 0.29f)
        }
        drawPath(wire2, wireColor2, style = Stroke(width = 1.8f))

        // Wire 3
        val wire3 = Path().apply {
            moveTo(poleX + 35f, h * 0.25f)
            quadraticTo(w * 0.52f, h * 0.44f, rightPoleX, h * 0.35f)
        }
        drawPath(wire3, wireColor1, style = Stroke(width = 1.8f))

        // Lower wire connecting to the work ladder area
        val wire4 = Path().apply {
            moveTo(poleX - 20f, h * 0.37f)
            quadraticTo(w * 0.48f, h * 0.52f, w * 0.80f, h * 0.42f)
        }
        drawPath(wire4, wireColor2, style = Stroke(width = 1.8f))

        // 11. Hanging Festive / Decorative Festoon Lights
        val festoonWire = Path().apply {
            moveTo(poleX + 40f, h * 0.32f)
            quadraticTo(w * 0.32f, h * 0.42f, w * 0.45f, h * 0.34f)
            quadraticTo(w * 0.58f, h * 0.42f, w * 0.70f, h * 0.35f)
            quadraticTo(w * 0.78f, h * 0.42f, rightPoleX, h * 0.36f)
        }
        drawPath(festoonWire, Color(0xFF93877D), style = Stroke(width = 1.2f))

        // Hanging glowing warm bulbs
        val bulbPositions = listOf(
            Offset(w * 0.25f, h * 0.39f),
            Offset(w * 0.35f, h * 0.395f),
            Offset(w * 0.50f, h * 0.40f),
            Offset(w * 0.63f, h * 0.40f),
            Offset(w * 0.74f, h * 0.395f)
        )
        for (pt in bulbPositions) {
            drawCircle(Color(0x66FFB45C), radius = 9f, center = pt)
            drawCircle(Color(0xFFF47A00), radius = 3.5f, center = pt)
        }

        // 12. RIGHT: Secondary Pole / Mast
        drawRect(
            color = poleColor,
            topLeft = Offset(rightPoleX - 4f, h * 0.18f),
            size = Size(8f, h * 0.66f)
        )
        drawRect(
            color = poleColor,
            topLeft = Offset(rightPoleX - 25f, h * 0.22f),
            size = Size(50f, 6f)
        )
        drawRect(insulatorColor, topLeft = Offset(rightPoleX - 20f, h * 0.20f), size = Size(5f, 6f))
        drawRect(insulatorColor, topLeft = Offset(rightPoleX + 15f, h * 0.20f), size = Size(5f, 6f))

        // 13. Electrician Extension Ladder
        val ladderLeftBottom = Offset(w * 0.72f, h * 0.82f)
        val ladderLeftTop = Offset(w * 0.78f, h * 0.33f)
        val ladderRightBottom = Offset(w * 0.77f, h * 0.82f)
        val ladderRightTop = Offset(w * 0.82f, h * 0.33f)

        // Rails
        drawLine(poleColor, ladderLeftBottom, ladderLeftTop, strokeWidth = 4.5f, cap = StrokeCap.Round)
        drawLine(poleColor, ladderRightBottom, ladderRightTop, strokeWidth = 4.5f, cap = StrokeCap.Round)

        // Rungs
        for (step in 1..8) {
            val frac = step / 9f
            val startX = ladderLeftBottom.x + (ladderLeftTop.x - ladderLeftBottom.x) * frac
            val startY = ladderLeftBottom.y + (ladderLeftTop.y - ladderLeftBottom.y) * frac
            val endX = ladderRightBottom.x + (ladderRightTop.x - ladderRightBottom.x) * frac
            val endY = ladderRightBottom.y + (ladderRightTop.y - ladderRightBottom.y) * frac
            drawLine(poleColor, Offset(startX, startY), Offset(endX, endY), strokeWidth = 3f)
        }

        // 14. Electrician Figure Silhouette
        val figureColor = Color(0xFF241A14)
        val hiVisOrange = Color(0xFFF47A00)
        val helmetOrange = Color(0xFFE86400)

        // Legs on ladder rungs
        val legLeft = Path().apply {
            moveTo(w * 0.77f, h * 0.48f)
            lineTo(w * 0.785f, h * 0.48f)
            lineTo(w * 0.795f, h * 0.58f)
            lineTo(w * 0.78f, h * 0.58f)
            close()
        }
        val legRight = Path().apply {
            moveTo(w * 0.79f, h * 0.48f)
            lineTo(w * 0.805f, h * 0.48f)
            lineTo(w * 0.815f, h * 0.56f)
            lineTo(w * 0.80f, h * 0.56f)
            close()
        }
        drawPath(legLeft, figureColor)
        drawPath(legRight, figureColor)

        // Work boots
        drawRect(poleColor, topLeft = Offset(w * 0.77f, h * 0.58f), size = Size(14f, 7f))
        drawRect(poleColor, topLeft = Offset(w * 0.80f, h * 0.56f), size = Size(14f, 7f))

        // Torso / Jacket
        val torso = Path().apply {
            moveTo(w * 0.77f, h * 0.38f)
            lineTo(w * 0.81f, h * 0.37f)
            lineTo(w * 0.815f, h * 0.48f)
            lineTo(w * 0.77f, h * 0.48f)
            close()
        }
        drawPath(torso, figureColor)

        // High-Vis Safety Vest (Primary Orange accents)
        drawRect(hiVisOrange, topLeft = Offset(w * 0.775f, h * 0.39f), size = Size(5f, 22f))
        drawRect(hiVisOrange, topLeft = Offset(w * 0.795f, h * 0.385f), size = Size(5f, 22f))
        drawRect(hiVisOrange, topLeft = Offset(w * 0.77f, h * 0.44f), size = Size(18f, 5f))

        // Arms reaching up toward service wire
        drawLine(
            figureColor,
            start = Offset(w * 0.80f, h * 0.40f),
            end = Offset(w * 0.84f, h * 0.35f),
            strokeWidth = 6f,
            cap = StrokeCap.Round
        )

        // Safety Hard Hat / Helmet
        val helmet = Path().apply {
            moveTo(w * 0.775f, h * 0.345f)
            cubicTo(w * 0.78f, h * 0.315f, w * 0.81f, h * 0.315f, w * 0.815f, h * 0.345f)
            lineTo(w * 0.82f, h * 0.355f)
            lineTo(w * 0.77f, h * 0.355f)
            close()
        }
        drawPath(helmet, helmetOrange)
        drawCircle(figureColor, radius = 5f, center = Offset(w * 0.795f, h * 0.355f))

        // 15. Spark / Glow of Electrician at Work
        val sparkCenter = Offset(w * 0.845f, h * 0.345f)
        drawCircle(Color(0x4DFFB45C), radius = 22f, center = sparkCenter)
        drawCircle(Color(0xFFFFFCF8), radius = 5f, center = sparkCenter)

        // Spark star rays
        val sparkRayColor = Color(0xFFFFB45C)
        drawLine(sparkRayColor, Offset(sparkCenter.x, sparkCenter.y - 14f), Offset(sparkCenter.x, sparkCenter.y + 14f), strokeWidth = 2.5f)
        drawLine(sparkRayColor, Offset(sparkCenter.x - 14f, sparkCenter.y), Offset(sparkCenter.x + 14f, sparkCenter.y), strokeWidth = 2.5f)
        drawLine(sparkRayColor, Offset(sparkCenter.x - 10f, sparkCenter.y - 10f), Offset(sparkCenter.x + 10f, sparkCenter.y + 10f), strokeWidth = 1.8f)
        drawLine(sparkRayColor, Offset(sparkCenter.x - 10f, sparkCenter.y + 10f), Offset(sparkCenter.x + 10f, sparkCenter.y - 10f), strokeWidth = 1.8f)
    }
}
