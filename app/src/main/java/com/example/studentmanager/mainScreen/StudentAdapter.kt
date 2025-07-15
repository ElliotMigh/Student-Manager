package com.example.studentmanager.mainScreen

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.studentmanager.model.Student
import com.example.studentmanager.databinding.RecyclerItemBinding

class StudentAdapter(private val data: ArrayList<Student>, val studentEvent: StudentEvent) :
    RecyclerView.Adapter<StudentAdapter.StudentViewHolder>() {

    private lateinit var binding: RecyclerItemBinding

    inner class StudentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindViews(student: Student) {
            binding.txtName.text = student.name
            binding.txtCourse.text = student.course
            binding.txtScore.text = student.score.toString()
            binding.txtHarfAval.text = student.name[0].uppercaseChar().toString()

            itemView.setOnClickListener {
                studentEvent.onItemClicked(student, adapterPosition)
            }
            itemView.setOnLongClickListener {
                studentEvent.onItemLongClicked(student, adapterPosition)
                true
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder {
        binding = RecyclerItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StudentViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
        holder.bindViews(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun removeItem(student: Student, position: Int) {
        data.remove(student)
        notifyItemRemoved(position)
    }

    fun addItem(student: Student) {
        data.add(0, student)
        notifyItemInserted(0)
    }

    fun updateItem(student: Student, position: Int) {
        data.set(position, student)
        notifyItemChanged(position)
    }

    interface StudentEvent {
        fun onItemClicked(student: Student, position: Int)
        fun onItemLongClicked(student: Student, position: Int)
    }
}