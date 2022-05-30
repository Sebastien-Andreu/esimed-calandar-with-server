package sebastien.andreu.esimed.listener

import sebastien.andreu.esimed.model.Task

interface OnTaskListener {
    fun onTaskClick(task: ArrayList<Task>)
}