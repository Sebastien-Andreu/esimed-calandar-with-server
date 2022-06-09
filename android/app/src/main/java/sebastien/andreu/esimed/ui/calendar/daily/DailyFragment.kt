package sebastien.andreu.esimed.ui.calendar.daily

import android.os.Bundle
import android.util.Log
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
import sebastien.andreu.esimed.adapter.TaskAdapter
import sebastien.andreu.esimed.api.Status
import sebastien.andreu.esimed.listener.ListenerDialog
import sebastien.andreu.esimed.listener.OnTaskListener
import sebastien.andreu.esimed.local.Storage
import sebastien.andreu.esimed.local.StorageEnum
import sebastien.andreu.esimed.model.Task
import sebastien.andreu.esimed.ui.dialog.DialogShowTask
import sebastien.andreu.esimed.utils.CalendarUtils
import sebastien.andreu.esimed.utils.CalendarUtils.selectedDate
import sebastien.andreu.esimed.utils.DAY
import sebastien.andreu.esimed.utils.ToastUtils
import sebastien.andreu.esimed.utils.WEEK
import java.time.LocalDate
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

        if (selectedDate == null) {
            selectedDate = LocalDate.now()
        }

        Storage(requireContext()).setString(StorageEnum.VIEW, DAY)

        view.findViewById<Button>(R.id.arrowLeft)?.setOnClickListener {
            selectedDate?.let { date ->
                selectedDate = date.minusDays(1)
                viewModel.getTask(requireContext(), date)
            }
        }

        view.findViewById<Button>(R.id.arrowRight)?.setOnClickListener {
            selectedDate?.let { date ->
                selectedDate = date.plusDays(1)
                viewModel.getTask(requireContext(), date)
            }
        }

        val listener = object : OnTaskListener {
            override fun onTaskClick(task: ArrayList<Task>) {
                if (task.size > 0) {
                    val dialog = DialogShowTask.newInstance(task, object : ListenerDialog {
                        override fun onValidate(task: Task) {
                            viewModel.updateTask(requireContext(), task)
                        }

                        override fun onDelete(task: Task) {
                            viewModel.deleteTask(requireContext(), task)
                        }
                    })
                    dialog.isCancelable = false
                    dialog.show(parentFragmentManager, TAG)
                }
            }
        }

        viewModel.apiResponse.observe(this, {
            when (it.first) {
                Status.SUCCESS -> {
                    selectedDate?.let { date ->
                        view.findViewById<TextView>(R.id.dayOfMonth)?.text = CalendarUtils.monthDayFromDate(date)
                        val dayOfWeek: String = date.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.getDefault())
                        view.findViewById<TextView>(R.id.dayOfWeek)?.text = dayOfWeek
                    }

                    view.findViewById<ListView>(R.id.taskListView)?.adapter = HourAdapter(requireContext(), it.third!!, listener)
                }
                Status.ERROR -> {
                    ToastUtils.error(requireContext(), it.second)
                }
            }
        })

        viewModel.apiResponseDelete.observe(this, {
            when (it.status) {
                Status.SUCCESS -> {
                    viewModel.getTask(requireContext(), selectedDate)
                }
                Status.ERROR -> {
                    ToastUtils.error(requireContext(), it.message)
                }
            }
        })

        viewModel.apiResponseUpdate.observe(this, {
            when (it.status) {
                Status.SUCCESS -> {
                    viewModel.getTask(requireContext(), selectedDate)
                }
                Status.ERROR -> {
                    ToastUtils.error(requireContext(), it.message)
                }
            }
        })

        viewModel.getTask(requireContext(), selectedDate)
    }

    override fun onResume() {
        super.onResume()
        viewModel.getTask(requireContext(), selectedDate)
    }
}