package dev.chrisstone.sprintplanningtimer

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_first.*
import java.util.*

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val deadlineView = view.findViewById<TimePicker>(R.id.timePicker_deadline)
        val hour = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            deadlineView.hour
        } else {
            deadlineView.currentHour
        }
        if (hour == 23) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                deadlineView.minute = 59
            } else {
                deadlineView.currentMinute = 59
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                deadlineView.hour++
            } else {
                deadlineView.currentHour++
            }
        }


        view.findViewById<Button>(R.id.button_start).setOnClickListener {
            val showCountTextView = view.findViewById<EditText>(R.id.editText_item_count)
            val currentCount = showCountTextView.text.toString().toIntOrNull() ?: 10

            val deadlineView = view.findViewById<TimePicker>(R.id.timePicker_deadline)

            val hour = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                deadlineView.hour
            } else {
                deadlineView.currentHour
            }

            val minute = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                deadlineView.minute
            } else {
                deadlineView.currentMinute
            }

            val inFuture = millisecondsRemaining(hour, minute) > 0

            if (inFuture) {

                val action = FirstFragmentDirections.actionFirstFragmentToSecondFragment(
                    currentCount,
                    hour,
                    minute
                )
                findNavController().navigate(action)
            } else {
                Toast.makeText(activity, "I am a toast message", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun millisecondsRemaining(hour: Int, minute: Int): Long {
        val rightNow = Calendar.getInstance()
        val currentHour = rightNow[Calendar.HOUR_OF_DAY]
        val currentMinute = rightNow[Calendar.MINUTE]
        val currentSecond = rightNow[Calendar.SECOND]
        val currentTime = currentHour * 3600 + currentMinute * 60 + currentSecond
        val deadlineTime = hour * 3600 + minute * 60
        return (deadlineTime - currentTime).toLong() * 1000
    }
}