package spacekotlin.vaniukova.clock

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import java.util.*
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin

class MyClock(
    context: Context, attrs: AttributeSet
) : View(context, attrs) {

    private var radius = 0.0f
    private val SCALE_NUM = 12

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.BLACK
        style = Paint.Style.FILL
    }

    override fun onSizeChanged(width: Int, height: Int, oldWidth: Int, oldHeight: Int) {
        radius = (min(width, height) / 2.0 * 0.8).toFloat()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawDial(canvas)
        drawHands(canvas)
        drawCenter(canvas)
        postInvalidateDelayed(1000)
        invalidate()
    }

    private fun drawDial(canvas: Canvas) {
        paint.color = Color.BLACK
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 15f
        val dial = Path()
        dial.addCircle((width / 2).toFloat(), (height / 2).toFloat(), radius, Path.Direction.CCW)
        canvas.drawPath(dial, paint)
        val dialMeasure = PathMeasure(dial, false)
        for (i in 0 until SCALE_NUM) {
            val matrix = Matrix()
            dialMeasure.getMatrix(
                dialMeasure.length / 12 * i,
                matrix,
                PathMeasure.POSITION_MATRIX_FLAG + PathMeasure.TANGENT_MATRIX_FLAG
            )
            canvas.save()
            canvas.setMatrix(matrix)
            canvas.drawLine(0f, -30f, 0f, 0f, paint)
            canvas.restore()
        }
    }

    private fun drawCenter(canvas: Canvas) {
        paint.style = Paint.Style.FILL
        paint.color = Color.BLACK
        canvas.drawCircle((width / 2).toFloat(), (height / 2).toFloat(), 12f, paint)
    }

    private fun drawHourHand(canvas: Canvas, date: Float) {
        drawHand(canvas, date, "black", 15f, 80f)
    }

    private fun drawMinuteHand(canvas: Canvas, date: Float) {
        drawHand(canvas, date, "red", 15f, 150f)
    }

    private fun drawSecondHand(canvas: Canvas, date: Float) {
        drawHand(canvas, date, "blue", 5f, 200f)
    }

    private fun drawHands(canvas: Canvas) {
        val date: Calendar = Calendar.getInstance()
        drawHourHand(
            canvas,
            ((date.get(Calendar.MINUTE) / 60 + date.get(Calendar.HOUR).toFloat()) * 5)
        )
        drawMinuteHand(canvas, date.get(Calendar.MINUTE).toFloat())
        drawSecondHand(canvas, date.get(Calendar.SECOND).toFloat())
    }

    private fun drawHand(canvas: Canvas, date: Float, color: String, str: Float, size: Float) {
        val corner = Math.PI * date / 30 - Math.PI / 2
        when (color) {
            "blue" -> paint.color = Color.BLUE
            "red" -> paint.color = Color.RED
            "black" -> paint.color = Color.BLACK
            else -> paint.color = Color.YELLOW
        }
        paint.style = Paint.Style.FILL
        paint.strokeWidth = str
        canvas.drawLine(
            width.toFloat() / 2,
            height.toFloat() / 2,
            (width / 2 + cos(corner) * size).toFloat(),
            (height / 2 + sin(corner) * size).toFloat(),
            paint
        )
    }
}