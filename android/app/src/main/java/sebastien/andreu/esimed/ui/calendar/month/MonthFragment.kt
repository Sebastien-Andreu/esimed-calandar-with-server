package sebastien.andreu.esimed.ui.calendar.month

import android.os.Bundle
import android.util.Log
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
import sebastien.andreu.esimed.api.Status
import sebastien.andreu.esimed.extension.toDate
import sebastien.andreu.esimed.listener.OnItemListener
import sebastien.andreu.esimed.local.Storage
import sebastien.andreu.esimed.local.StorageEnum
import sebastien.andreu.esimed.model.Task
import sebastien.andreu.esimed.ui.home.HomeActivity
import sebastien.andreu.esimed.utils.*
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

        Storage(requireContext()).setString(StorageEnum.VIEW, MONTH)

        if (CalendarUtils.selectedDate == null) {
            CalendarUtils.selectedDate = LocalDate.now()
        }

        view.findViewById<Button>(R.id.arrowLeft)?.setOnClickListener {
            CalendarUtils.selectedDate?.let { date ->
                CalendarUtils.selectedDate = date.minusMonths(1)
                viewModel.getTask(requireContext())
            }
        }

        view.findViewById<Button>(R.id.arrowRight)?.setOnClickListener {
            CalendarUtils.selectedDate?.let { date ->
                CalendarUtils.selectedDate = date.plusMonths(1)
                viewModel.getTask(requireContext())
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
                        CalendarUtils.selectedDate = task.date.toDate()
                        adapter.updateView()
                    }

                    override fun onItemDoubleClick(position: Int, task: Task) {
                        CalendarUtils.selectedDate = task.date.toDate()
                        adapter.updateView()
                        findNavController().navigate(R.id.nav_week)
                    }
                })

                viewModel.apiResponse.observe(this, {
                    when (it.first) {
                        Status.SUCCESS -> {
                            view.findViewById<TextView>(R.id.monthText)?.text = monthYearFromDate(CalendarUtils.selectedDate)
                            adapter.setList(it.third!!)
                        }
                        Status.ERROR -> {
                            ToastUtils.error(requireContext(), it.second)
                        }
                    }
                })
            }
        }
        viewModel.getTask(requireContext())
    }

    override fun onResume() {
        super.onResume()
        viewModel.getTask(requireContext())
    }
}