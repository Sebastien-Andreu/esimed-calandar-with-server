package sebastien.andreu.esimed.ui.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.DialogFragment
import sebastien.andreu.esimed.R
import sebastien.andreu.esimed.extension.dateToString
import sebastien.andreu.esimed.extension.selected
import sebastien.andreu.esimed.listener.ListenerDialog
import sebastien.andreu.esimed.listener.ListenerDialogDelete
import sebastien.andreu.esimed.model.Task
import sebastien.andreu.esimed.model.enum.TaskEnum
import sebastien.andreu.esimed.utils.FINISH
import sebastien.andreu.esimed.utils.IN_PROGRESS
import sebastien.andreu.esimed.utils.NOT_START
import sebastien.andreu.esimed.utils.ToastUtils

class DialogShowTask: DialogFragment() {

    private var listener: ListenerDialog? = null

    private var tasks: ArrayList<Task>? = null

    private var task: Task? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (null == this.context || null == this.activity || null == tasks) {
            this.dismiss()
            return
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.show_task, null, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (tasks?.size == 1) {
            view.findViewById<LinearLayout>(R.id.chooseTasks)?.visibility = View.GONE
        }

        task = tasks!![0]
        view.findViewById<TextView>(R.id.title)?.text = requireContext().getString(R.string.title, task!!.date.dateToString())

        view.findViewById<EditText>(R.id.taskName)?.let { editText ->
            editText.setText(task!!.name)

            view.findViewById<Spinner>(R.id.spinnerAllTask)?.let { spinner ->
                spinner.adapter = ArrayAdapter(requireContext(), R.layout.support_simple_spinner_dropdown_item, tasks!!)

                spinner.selected { position ->
                    task = tasks!![position]
                    view.findViewById<Spinner>(R.id.spinnerProgress)?.setSelection(TaskEnum.getEnumByValue(task!!.progress).value)
                    view.findViewById<TextView>(R.id.title)?.text = requireContext().getString(R.string.title, task!!.date.dateToString())
                    editText.setText(task!!.name)
                }
            }

            view.findViewById<Spinner>(R.id.spinnerProgress)?.let { spinner ->
                spinner.adapter = ArrayAdapter(requireContext(), R.layout.support_simple_spinner_dropdown_item, arrayOf(NOT_START, IN_PROGRESS, FINISH))

                spinner.selected { position ->
                    task!!.progress = position
                }

                spinner.setSelection(TaskEnum.getEnumByValue(task!!.progress).value)
            }

            view.findViewById<Button>(R.id.validateButton)?.setOnClickListener {
                if (editText.text.toString().isNotEmpty()) {
                    listener?.onValidate(
                        Task(
                            name = editText.text.toString(),
                            date = task!!.date,
                            time = task!!.time,
                            progress = task!!.progress
                        )
                    )
                    dismiss()
                } else {
                    ToastUtils.error(requireContext(), requireContext().getString(R.string.errorNewTaskTitle))
                }
            }
        }

        view.findViewById<Button>(R.id.cancelButton)?.setOnClickListener {
            dismiss()
        }

        view.findViewById<Button>(R.id.removeButton)?.setOnClickListener {
            val dialogDelete = DialogDelete.newInstance()
            dialogDelete.let { dialog ->
                dialog.setListener(object : ListenerDialogDelete {
                    override fun onDelete() {
                        this@DialogShowTask.dismiss()
                    }

                    override fun onCancel() { /* WE DO NOTHING */}
                })
                dialog.isCancelable = false
                dialog.show(parentFragmentManager, "TAG_DELETE_DIALOG")
            }
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
        const val TAG = "DialogShowTask"

        fun newInstance(tasks: ArrayList<Task>, listenerDialog: ListenerDialog): DialogShowTask {
            val fragment = DialogShowTask()

            fragment.listener = listenerDialog
            fragment.tasks = tasks

            return fragment
        }
    }

}