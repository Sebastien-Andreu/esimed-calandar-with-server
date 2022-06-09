package sebastien.andreu.esimed.holder

import android.content.Context
import android.graphics.Color
import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.ddd.androidutils.DoubleClick
import com.ddd.androidutils.DoubleClickListener
import sebastien.andreu.esimed.R
import sebastien.andreu.esimed.extension.toDate
import sebastien.andreu.esimed.listener.OnItemListener
import sebastien.andreu.esimed.model.Task
import sebastien.andreu.esimed.utils.CalendarUtils

class CalendarHolder(view: View) : RecyclerView.ViewHolder(view) {

    fun bind(context: Context, task: Task, position: Int, listener: OnItemListener?) {

        if (task.date.toDate() == CalendarUtils.selectedDate) {
            itemView.findViewById<View>(R.id.parentView).setBackgroundColor(Color.LTGRAY)
        } else {
            itemView.findViewById<View>(R.id.parentView).setBackgroundColor(Color.WHITE)
        }

        if (task.name != null) {
            itemView.findViewById<View>(R.id.haveTask).visibility = View.VISIBLE
        } else {
            itemView.findViewById<View>(R.id.haveTask).visibility = View.GONE
        }

        itemView.findViewById<TextView>(R.id.cellDayText)?.let { dayOfMonth ->
            dayOfMonth.text = task.date.toDate().dayOfMonth.toString()
            if (task.date.toDate().month == CalendarUtils.selectedDate?.month) {
                dayOfMonth.setTextColor(Color.BLACK)
            } else {
                dayOfMonth.setTextColor(Color.LTGRAY)
            }
        }

        val click = DoubleClick(object : DoubleClickListener {
            override fun onDoubleClickEvent(view: View?) {
                listener?.onItemDoubleClick(position, task)
            }

            override fun onSingleClickEvent(view: View?) {
                listener?.onItemClick(position, task)
            }

        })

        itemView.findViewById<ConstraintLayout>(R.id.parentView)?.setOnClickListener(click)
    }
}