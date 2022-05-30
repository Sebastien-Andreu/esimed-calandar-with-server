package sebastien.andreu.esimed.ui.calendar.month

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import sebastien.andreu.esimed.R
import sebastien.andreu.esimed.adapter.CalendarAdapter
import sebastien.andreu.esimed.listener.OnItemListener
import sebastien.andreu.esimed.model.Task
import sebastien.andreu.esimed.utils.CalendarUtils
import sebastien.andreu.esimed.utils.CalendarUtils.monthYearFromDate
import java.time.LocalDate

@AndroidEntryPoint
class MonthFragment : Fragment(){

    private val viewModel: MonthFragmentViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return View.inflate(context, R.layout.fragment_month, null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (CalendarUtils.selectedDate == null) {
            CalendarUtils.selectedDate = LocalDate.now()
        }

        view.findViewById<Button>(R.id.arrowLeft)?.setOnClickListener {
            CalendarUtils.selectedDate?.let { date ->
                CalendarUtils.selectedDate = date.minusMonths(1)
                viewModel.getListOfTask()
            }
        }

        view.findViewById<Button>(R.id.arrowRight)?.setOnClickListener {
            CalendarUtils.selectedDate?.let { date ->
                CalendarUtils.selectedDate = date.plusMonths(1)
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
                        findNavController().navigate(R.id.nav_week)
                    }
                })

                viewModel.listOfTask.observe(this, { list ->
                    view.findViewById<TextView>(R.id.monthText)?.text = monthYearFromDate(CalendarUtils.selectedDate)
                    adapter.setList(list)
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