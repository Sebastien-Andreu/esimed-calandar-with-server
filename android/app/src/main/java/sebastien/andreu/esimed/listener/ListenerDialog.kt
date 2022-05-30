package sebastien.andreu.esimed.listener

import sebastien.andreu.esimed.model.Task

interface ListenerDialog {
    fun onValidate(task: Task)
    fun onDelete(task: Task)
}