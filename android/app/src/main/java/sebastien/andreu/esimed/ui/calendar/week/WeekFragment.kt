package sebastien.andreu.esimed.ui.calendar.week

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
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import sebastien.andreu.esimed.R
import sebastien.andreu.esimed.adapter.CalendarAdapter
import sebastien.andreu.esimed.adapter.TaskAdapter
import sebastien.andreu.esimed.api.Status
import sebastien.andreu.esimed.extension.toDate
import sebastien.andreu.esimed.listener.ListenerDialog
import sebastien.andreu.esimed.listener.OnItemListener
import sebastien.andreu.esimed.listener.OnTaskListener
import sebastien.andreu.esimed.local.Storage
import sebastien.andreu.esimed.local.StorageEnum
import sebastien.andreu.esimed.model.Task
import sebastien.andreu.esimed.ui.dialog.DialogShowTask
import sebastien.andreu.esimed.utils.CalendarUtils
import sebastien.andreu.esimed.utils.CalendarUtils.monthYearFromDate
import sebastien.andreu.esimed.utils.CalendarUtils.selectedDate
import sebastien.andreu.esimed.utils.ToastUtils
import sebastien.andreu.esimed.utils.WEEK
import java.time.LocalDate

@AndroidEntryPoint
class WeekFragment : Fragment() {

    private val TAG = "WeekFragment"
    private val viewModel: WeekFragmentViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return View.inflate(context, R.layout.fragment_week, null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (selectedDate == null) {
            selectedDate = LocalDate.now()
        }

        view.findViewById<Button>(R.id.arrowLeft)?.setOnClickListener {
            selectedDate?.let { date ->
                selectedDate = date.minusWeeks(1)
                viewModel.getTask(requireContext(), date)
            }
        }

        view.findViewById<Button>(R.id.arrowRight)?.setOnClickListener {
            selectedDate?.let { date ->
                selectedDate = date.plusWeeks(1)
                viewModel.getTask(requireContext(), date)
            }
        }

        view.findViewById<RecyclerView>(R.id.calendarRecyclerView)?.let { recyclerView ->
            recyclerView.isNestedScrollingEnabled = false
            recyclerView.layoutManager = GridLayoutManager(requireContext(), 7)
            recyclerView.adapter = CalendarAdapter(
                context = requireContext(),
                display = arrayListOf(),
            ).also { adapter ->
                adapter.setListener(object : OnItemListener {
                    override fun onItemClick(position: Int, task: Task) {
                        selectedDate = task.date.toDate()
                        adapter.updateView()
                    }

                    override fun onItemDoubleClick(position: Int, task: Task) {
                        selectedDate = task.date.toDate()
                        adapter.updateView()
                        findNavController().navigate(R.id.nav_day)
                    }
                })

                val listener = object : OnTaskListener {
                    override fun onTaskClick(task: ArrayList<Task>) {
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

                viewModel.apiResponse.observe(this, {
                    when (it.first) {
                        Status.SUCCESS -> {
                            view.findViewById<TextView>(R.id.yearText)?.text = monthYearFromDate(selectedDate)
                            if (it.third != null) {
                                adapter.setList(it.third!!.first!!)
                                Task.taskList = it.third!!.second
                            }
                            view.findViewById<ListView>(R.id.eventListView)?.apply {
                                selectedDate?.let { date -> this.adapter = TaskAdapter(requireContext(), Task.eventsForDateInWeek(date), listener) }
                            }
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
            }
        }
        viewModel.getTask(requireContext(), selectedDate)
    }

    override fun onResume() {
        super.onResume()
        viewModel.getTask(requireContext(), selectedDate)
    }

}