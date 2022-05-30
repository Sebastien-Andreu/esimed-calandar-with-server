package sebastien.andreu.esimed.ui.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.DialogFragment
import sebastien.andreu.esimed.R
import sebastien.andreu.esimed.extension.dateToString
import sebastien.andreu.esimed.listener.ListenerDialog
import sebastien.andreu.esimed.model.Task
import sebastien.andreu.esimed.utils.ToastUtils
import java.time.LocalDate
import java.time.LocalTime

class DialogCreateTask: DialogFragment() {

    private var listener: ListenerDialog? = null

    private var date: LocalDate? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (null == this.context || null == this.activity || null == date) {
            this.dismiss()
            return
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.create_task, null, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<TextView>(R.id.title)?.text = requireContext().getString(R.string.title, date!!.dateToString())

        view.findViewById<TimePicker>(R.id.timePicker)?.let { time ->
            time.setIs24HourView(true)
            time.hour = LocalTime.now().hour
            time.minute = LocalTime.now().minute


            view.findViewById<EditText>(R.id.editText)?.let { editText ->

                view.findViewById<Button>(R.id.validateButton)?.setOnClickListener {
                    if (editText.text.toString().isNotEmpty()) {
                        listener?.onValidate(
                            Task(
                                name = editText.text.toString(),
                                date = date!!,
                                time = LocalTime.of(time.hour, time.minute)
                            )
                        )
                        dismiss()
                    } else {
                        ToastUtils.error(requireContext(), requireContext().getString(R.string.errorNewTaskTitle))
                    }
                }
            }
        }


        view.findViewById<Button>(R.id.cancelButton)?.setOnClickListener {
            dismiss()
        }
    }

    override fun onResume() {
        super.onResume()

        if (dialog != null) {
            val window = dialog?.window ?: return
            window.setBackgroundDrawableResource(R.drawable.background_transparent)
        }
    }

    companion object {
        const val TAG = "DialogCreateTask"

        fun newInstance(date: LocalDate, listenerDialog: ListenerDialog): DialogCreateTask {
            val fragment = DialogCreateTask()

            fragment.listener = listenerDialog
            fragment.date = date

            return fragment
        }
    }

}