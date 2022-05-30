package sebastien.andreu.esimed.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import sebastien.andreu.esimed.R
import sebastien.andreu.esimed.event.HourTask
import sebastien.andreu.esimed.listener.OnTaskListener
import sebastien.andreu.esimed.model.Task
import sebastien.andreu.esimed.model.enum.TaskEnum
import sebastien.andreu.esimed.utils.CalendarUtils
import java.time.LocalTime
import java.util.ArrayList

class HourAdapter(
    context: Context,
    hourTasks: List<HourTask>,
    private val listener: OnTaskListener
) : ArrayAdapter<HourTask?>(context, 0, hourTasks) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        if (convertView == null) convertView = LayoutInflater.from(context).inflate(R.layout.hour_cell, parent, false)

        getItem(position)?.let { task ->
            setHour(convertView, task.time)
            setEvents(convertView, task.getTasks())
            convertView?.setOnClickListener {
                listener.onTaskClick(task.getTasks())
            }
        }

        return convertView!!
    }

    private fun setHour(convertView: View?, time: LocalTime?) {
        val timeTV = convertView!!.findViewById<TextView>(R.id.timeTV)
        timeTV.text = CalendarUtils.formattedShortTime(time)
    }

    private fun setEvents(convertView: View?, tasks: ArrayList<Task>) {
        val event1 = convertView!!.findViewById<TextView>(R.id.event1)
        val event2 = convertView.findViewById<TextView>(R.id.event2)
        val event3 = convertView.findViewById<TextView>(R.id.event3)
        when (tasks.size) {
            0 -> {
                hideEvent(event1)
                hideEvent(event2)
                hideEvent(event3)
            }
            1 -> {
                setEvent(event1, tasks[0])
                hideEvent(event2)
                hideEvent(event3)
            }
            2 -> {
                setEvent(event1, tasks[0])
                setEvent(event2, tasks[1])
                hideEvent(event3)
            }
            3 -> {
                setEvent(event1, tasks[0])
                setEvent(event2, tasks[1])
                setEvent(event3, tasks[2])
            }
            else -> {
                setEvent(event1, tasks[0])
                setEvent(event2, tasks[1])
                event3.visibility = View.VISIBLE
                event3.setBackgroundColor(context.getColor(R.color.orange))
                var eventsNotShown = (tasks.size - 2).toString()
                eventsNotShown += " More Events"
                event3.text = eventsNotShown
            }
        }
    }

    private fun setEvent(textView: TextView, task: Task) {
        textView.text = task.name
        textView.visibility = View.VISIBLE

        when (task.progress) {
            TaskEnum.NOT_START.value -> {
                textView.setBackgroundColor(context.getColor(R.color.notStart))
            }
            TaskEnum.IN_PROGRESS.value -> {
                textView.setBackgroundColor(context.getColor(R.color.inProgress))
            }
            TaskEnum.FINISH.value -> {
                textView.setBackgroundColor(context.getColor(R.color.finish))
            }
        }
    }

    private fun hideEvent(tv: TextView) {
        tv.visibility = View.INVISIBLE
    }
}