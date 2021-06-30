package dev.chrisstone.agendatimer

import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import java.util.*

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class SecondFragment : Fragment() {

    private var headerText: TextView? = null
    private var totalTimeTextView: TextView? = null
    private var itemTimeTextView: TextView? = null
    private val args: SecondFragmentArgs by navArgs()
    private var totalCountDownTimer: CountDownTimer? = null
    private var itemCountDownTimer: CountDownTimer? = null
    private var itemsRemaining: Int = 0
    private var deadlineText: String = ""

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_second, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<Button>(R.id.button_1_down).setOnClickListener {
            itemsRemaining--
            updateHeaderText()
            resetItemTime()
        }

        view.findViewById<Button>(R.id.button_reset_item).setOnClickListener {
            resetItemTime()
        }

        view.findViewById<Button>(R.id.button_start_over).setOnClickListener {
            activity?.onBackPressed()
        }

        itemsRemaining = args.itemCount
        var deadlineHour = args.deadlineHour
        val deadlineMinute = args.deadlineMinute
        if (DateFormat.is24HourFormat(context)) {
            deadlineText = getString(R.string.time_of_day_format_24_hour, deadlineHour, deadlineMinute)
        } else {
            val amPm = if (deadlineHour < 12) {
                getString(R.string.time_of_day_format_am)
            } else {
                getString(R.string.time_of_day_format_pm)
            }
            if (deadlineHour == 0) {
                deadlineHour = 12
            } else if (deadlineHour > 12) {
                deadlineHour -= 12
            }
            deadlineText = getString(R.string.time_of_day_format_12_hour, deadlineHour, deadlineMinute, amPm)
        }

        headerText = view.findViewById(R.id.textView_countdown_header)

        updateHeaderText()

        totalTimeTextView = view.findViewById(R.id.textView_total_time_remaining)
        itemTimeTextView = view.findViewById(R.id.textView_item_time_remaining)

        startTotalTimer(millisecondsRemaining())
        resetItemTime()
    }

    private fun millisecondsRemaining(): Long {
        val rightNow = Calendar.getInstance()
        val hour = rightNow[Calendar.HOUR_OF_DAY]
        val minute = rightNow[Calendar.MINUTE]
        val second = rightNow[Calendar.SECOND]
        val currentTime = hour * 3600 + minute * 60 + second
        val deadlineTime = args.deadlineHour * 3600 + args.deadlineMinute * 60
        return (deadlineTime - currentTime).toLong() * 1000
    }

    private fun resetItemTime() {
        cancelItemTimer()
        if (itemsRemaining == 0) {
            cancelTotalTimer()
            Toast.makeText(context, getString(R.string.tasks_complete_toast), Toast.LENGTH_LONG).show()
            activity?.onBackPressed()
            return
        }

        startItemTimer(millisecondsRemaining() / itemsRemaining)
    }

    private fun updateHeaderText() {
        val countText = resources.getQuantityString(R.plurals.countdown_header_text, itemsRemaining, itemsRemaining, deadlineText)
        headerText!!.text = countText
    }

    private fun startTotalTimer(millisInFuture: Long) {
        totalTimeTextView!!.setTextColor(Color.WHITE)
        totalCountDownTimer = object : CountDownTimer(millisInFuture, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                totalTimeTextView!!.text = getTimerText(millisUntilFinished)
            }
            override fun onFinish() {
                totalTimeTextView!!.text = getString(R.string.time_format, 0, 0, 0)
                totalTimeTextView!!.setTextColor(Color.RED)
            }
        }
        totalCountDownTimer!!.start()
    }

    private fun startItemTimer(millisInFuture: Long) {
        itemTimeTextView!!.setTextColor(Color.WHITE)
        itemCountDownTimer = object : CountDownTimer(millisInFuture, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                itemTimeTextView!!.text = getTimerText(millisUntilFinished)
            }
            override fun onFinish() {
                try {
                    itemTimeTextView!!.text = getString(R.string.time_format, 0, 0, 0)
                    itemTimeTextView!!.setTextColor(Color.RED)
                } catch (e: IllegalStateException) { }
            }
        }
        itemCountDownTimer!!.start()
    }

    private fun getTimerText(millisUntilFinished: Long) : String {
        val totalSeconds = millisUntilFinished / 1000
        val hours = totalSeconds / 60 / 60
        val minutes = totalSeconds / 60 - hours * 60
        val seconds = totalSeconds - hours * 3600 - minutes * 60
        return try {
            getString(R.string.time_format, hours, minutes, seconds)
        } catch (e: IllegalStateException) {
            ""
        }
    }

    private fun cancelTotalTimer() {
        if (totalCountDownTimer != null) totalCountDownTimer!!.cancel()
    }

    private fun cancelItemTimer() {
        if (itemCountDownTimer != null) itemCountDownTimer!!.cancel()
    }
}