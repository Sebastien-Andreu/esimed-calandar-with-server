package sebastien.andreu.esimed.ui.calendar.daily

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import sebastien.andreu.esimed.R
import sebastien.andreu.esimed.adapter.HourAdapter
import sebastien.andreu.esimed.listener.ListenerDialog
import sebastien.andreu.esimed.listener.OnTaskListener
import sebastien.andreu.esimed.model.Task
import sebastien.andreu.esimed.ui.dialog.DialogShowTask
import sebastien.andreu.esimed.utils.CalendarUtils
import sebastien.andreu.esimed.utils.CalendarUtils.selectedDate
import sebastien.andreu.esimed.utils.ToastUtils
import java.time.format.TextStyle
import java.util.*
import kotlin.collections.ArrayList

@AndroidEntryPoint
class DailyFragment : Fragment() {

    private val TAG = "DailyFragment"

    private val viewModel: DailyFragmentViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return View.inflate(context, R.layout.fragment_daily, null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<Button>(R.id.arrowLeft)?.setOnClickListener {
            selectedDate?.let { date ->
                selectedDate = date.minusDays(1)
                viewModel.getListOfTask()
            }
        }

        view.findViewById<Button>(R.id.arrowRight)?.setOnClickListener {
            selectedDate?.let { date ->
                selectedDate = date.plusDays(1)
                viewModel.getListOfTask()
            }
        }

        val listener = object : OnTaskListener {
            override fun onTaskClick(task: ArrayList<Task>) {
                if (task.size > 0) {
                    val dialog = DialogShowTask.newInstance(task, object : ListenerDialog {
                        override fun onValidate(task: Task) {
                            ToastUtils.success(requireContext(), task.toString())
                        }

                        override fun onDelete(task: Task) {
                            /* delete */
                        }
                    })
                    dialog.isCancelable = false
                    dialog.show(parentFragmentManager, TAG)
                }
            }
        }

        viewModel.listOfTask.observe(this, { list ->
            selectedDate?.let { date ->
                view.findViewById<TextView>(R.id.dayOfMonth)?.text = CalendarUtils.monthDayFromDate(date)
                val dayOfWeek: String = date.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.getDefault())
                view.findViewById<TextView>(R.id.dayOfWeek)?.text = dayOfWeek
            }
            view.findViewById<ListView>(R.id.taskListView)?.adapter = HourAdapter(requireContext(), list, listener)
        })

        viewModel.getListOfTask()
    }

    override fun onResume() {
        super.onResume()
        viewModel.getListOfTask()
    }
}