package sebastien.andreu.esimed.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import sebastien.andreu.esimed.R
import sebastien.andreu.esimed.extension.toTime
import sebastien.andreu.esimed.listener.OnTaskListener
import sebastien.andreu.esimed.model.Task
import sebastien.andreu.esimed.model.enum.TaskEnum
import sebastien.andreu.esimed.utils.CalendarUtils

class TaskAdapter(
    context: Context,
    display: List<Task>,
    private val listener: OnTaskListener
) : ArrayAdapter<Task>(context, 0, display) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view = convertView
        if (view == null) view = LayoutInflater.from(context).inflate(R.layout.event_cell, parent, false)

        getItem(position)?.let { task ->
            val eventCellTV = view!!.findViewById<TextView>(R.id.eventCellTV)
            val eventTitle: String = task.name + " " + CalendarUtils.formattedTime(task.time?.toTime())
            eventCellTV.text = eventTitle

            view.findViewById<View>(R.id.colorPreview)?.let { view ->
                when (task.progress) {
                    TaskEnum.NOT_START.value -> {
                        view.setBackgroundColor(context.getColor(R.color.notStart))
                    }
                    TaskEnum.IN_PROGRESS.value -> {
                        view.setBackgroundColor(context.getColor(R.color.inProgress))
                    }
                    TaskEnum.FINISH.value -> {
                        view.setBackgroundColor(context.getColor(R.color.finish))
                    }
                }
            }

            view.setOnClickListener { listener.onTaskClick(arrayListOf(task)) }
        }

        return view!!
    }
}