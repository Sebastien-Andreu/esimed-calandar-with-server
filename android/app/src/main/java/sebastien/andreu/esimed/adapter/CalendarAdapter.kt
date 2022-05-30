package sebastien.andreu.esimed.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import sebastien.andreu.esimed.R
import sebastien.andreu.esimed.holder.CalendarHolder
import sebastien.andreu.esimed.listener.OnItemListener
import sebastien.andreu.esimed.model.Task
import java.util.ArrayList

class CalendarAdapter(
    private val context: Context,
    private val display: ArrayList<Task>
) : RecyclerView.Adapter<CalendarHolder>() {

    private var listener: OnItemListener? = null

    fun setListener (listener: OnItemListener) {
        this.listener = listener
        notifyDataSetChanged()
    }

    fun setList(list: List<Task>) {
        display.clear()
        display.addAll(list)
        notifyDataSetChanged()
    }

    fun removeAt(position: Int) {
        display.removeAt(position)
        notifyItemRemoved(position)
        if (position == display.size) {
            notifyItemChanged(display.size-1)
        }
    }

    fun updateAt(task: Task, position: Int) {
        if (position == display.size-1) {
            display[position] = task
            notifyItemChanged(position)
            notifyItemChanged(display.size-1)
        } else {
            removeAt(position)
            insert(task)
        }
    }

    fun insert(task: Task) {
        display.add(task)
        notifyItemInserted(display.size)
    }

    fun updateView() {
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view: View = inflater.inflate(R.layout.calendar_cell, parent, false)
        val layoutParams = view.layoutParams

        if (display.size > 15) {
            layoutParams.height = (parent.height * 0.166666666).toInt()
        } else {
            layoutParams.height = parent.height
        }

        return CalendarHolder(view)
    }

    override fun onBindViewHolder(holder: CalendarHolder, position: Int) {
        if ((display.size) > position) {
            holder.bind(context, display[position], position, listener)
        }
    }

    override fun getItemCount(): Int {
        return display.size
    }
}