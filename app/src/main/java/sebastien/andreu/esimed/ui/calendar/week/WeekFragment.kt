package sebastien.andreu.esimed.ui.calendar.week

import android.os.Bundle
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
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dagger.hilt.android.AndroidEntryPoint
import sebastien.andreu.esimed.R
import sebastien.andreu.esimed.adapter.CalendarAdapter
import sebastien.andreu.esimed.adapter.TaskAdapter
import sebastien.andreu.esimed.listener.ListenerDialog
import sebastien.andreu.esimed.listener.OnItemListener
import sebastien.andreu.esimed.listener.OnTaskListener
import sebastien.andreu.esimed.model.Task
import sebastien.andreu.esimed.ui.dialog.DialogCreateTask
import sebastien.andreu.esimed.ui.dialog.DialogShowTask
import sebastien.andreu.esimed.ui.home.HomeActivity
import sebastien.andreu.esimed.utils.CalendarUtils
import sebastien.andreu.esimed.utils.CalendarUtils.monthYearFromDate
import sebastien.andreu.esimed.utils.ToastUtils

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

        view.findViewById<Button>(R.id.arrowLeft)?.setOnClickListener {
            CalendarUtils.selectedDate?.let { date ->
                CalendarUtils.selectedDate = date.minusWeeks(1)
                viewModel.getListOfTask()
            }
        }

        view.findViewById<Button>(R.id.arrowRight)?.setOnClickListener {
            CalendarUtils.selectedDate?.let { date ->
                CalendarUtils.selectedDate = date.plusWeeks(1)
                viewModel.getListOfTask()
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
                        CalendarUtils.selectedDate = task.date
                        adapter.updateView()
                    }

                    override fun onItemDoubleClick(position: Int, task: Task) {
                        CalendarUtils.selectedDate = task.date
                        adapter.updateView()
                        findNavController().navigate(R.id.nav_day)
                    }
                })

                val listener = object : OnTaskListener {
                    override fun onTaskClick(task: ArrayList<Task>) {
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

                viewModel.listOfTask.observe(this, { list ->
                    view.findViewById<TextView>(R.id.yearText)?.text = monthYearFromDate(CalendarUtils.selectedDate)
                    adapter.setList(list.first)
                    Task.taskList = list.second

                    view.findViewById<ListView>(R.id.eventListView)?.apply {
                        CalendarUtils.selectedDate?.let { date -> this.adapter = TaskAdapter(requireContext(), Task.eventsForDate(date), listener) }
                    }
                })
            }
        }
        viewModel.getListOfTask()
    }

    override fun onResume() {
        super.onResume()
        viewModel.getListOfTask()
    }

}