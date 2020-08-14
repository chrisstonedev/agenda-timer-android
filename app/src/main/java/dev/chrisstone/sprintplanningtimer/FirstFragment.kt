package dev.chrisstone.sprintplanningtimer

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.navigation.fragment.findNavController

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

            val action = FirstFragmentDirections.actionFirstFragmentToSecondFragment(currentCount, hour, minute)
            findNavController().navigate(action)
        }
    }
}