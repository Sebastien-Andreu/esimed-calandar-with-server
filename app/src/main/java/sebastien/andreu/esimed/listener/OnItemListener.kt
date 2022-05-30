package sebastien.andreu.esimed.listener

import sebastien.andreu.esimed.model.Task

interface OnItemListener {
    fun onItemClick(position: Int, task: Task)

    fun onItemDoubleClick(position: Int, task: Task)
}