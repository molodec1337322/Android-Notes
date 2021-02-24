package com.example.androidnotes.activities

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Canvas
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.Toast
import androidx.recyclerview.widget.*

import com.example.androidnotes.R
import com.example.androidnotes.TinyDB.TinyDB
import com.example.androidnotes.extensions.setActive
import com.example.androidnotes.extensions.setInactive
import com.example.androidnotes.notes.Note
import com.example.androidnotes.notes.NoteAdapter
import com.example.androidnotes.notes.NotesData
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var recycler: RecyclerView
    private lateinit var button_add: ImageButton
    private lateinit var button_reply: ImageButton
    private lateinit var button_popup_settins_column: ImageButton

    private var notes: MutableList<Note> = mutableListOf<Note>()
    private var deletedNotes: Stack<Pair<Note, Int>> = Stack()
    private var notesData: NotesData? = null

    private var numberOfColumns = 0

    private var adapter: RecyclerView.Adapter<NoteAdapter.NoteHolder>? = null

    private val context: Context = this

    private val PERMISSION_CODE: Int = 1000

    private val ItemTouchHelperCallback = object: ItemTouchHelper.SimpleCallback(
        ItemTouchHelper.UP or
                ItemTouchHelper.DOWN or
                ItemTouchHelper.START or
                ItemTouchHelper.END,
        ItemTouchHelper.LEFT
    ) {
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            val fromPosition = viewHolder.adapterPosition
            val toPosition = target.adapterPosition

            val tempNote = notes[fromPosition]
            notes[fromPosition] = notes[toPosition]
            notes[toPosition] = tempNote

            putDataInDB()

            adapter!!.notifyItemMoved(fromPosition, toPosition)

            return true
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            val position = viewHolder.adapterPosition

            deletedNotes.push(notes.removeAt(position) to position)

            putDataInDB()
            adapter!!.notifyItemRemoved(position)
            checkForDeletedNotes()
        }

        override fun onChildDraw(
            c: Canvas,
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            dX: Float,
            dY: Float,
            actionState: Int,
            isCurrentlyActive: Boolean
        ) {
            val recyclerViewSwipeDecorator = RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                .addSwipeLeftLabel("Удалить")
                .setSwipeLeftLabelColor(R.color.colorAccent)
                .addSwipeLeftActionIcon(R.drawable.ic_baseline_delete_40)
                .create()
                .decorate()

            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
        }
    }

    companion object{
        const val CREATE_NEW_NOTE = 1
        const val EDIT_NOTE = 2

        const val RESULT_OK = 1
        const val RESULT_CANCELED = -1

        const val NOTES = "notes"
        const val NOTE_TEXT = "note_text"
        const val NOTE_TITLE = "note_title"
        const val NOTE_TIMESTAMP = "note_timestamp"
        const val NOTE_IMAGE = "note_image"
        const val NOTE_POSITION = "note_position"
        const val NUMBER_OF_COLUMNS = "number_of_columns"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        checkPermissions()

        getDataFromDB()
        getNumberOfColumnsFromDB()

        adapter = NoteAdapter(notes, context)

        recycler = recycle_list
        //recycler.setHasFixedSize(false)
        recycler.setHasFixedSize(true)
        //recycler.layoutManager = LinearLayoutManager(this)
        recycler.layoutManager = StaggeredGridLayoutManager(numberOfColumns, StaggeredGridLayoutManager.VERTICAL)
        recycler.adapter = adapter

        button_add = btn_create_new
        button_reply = btn_reply
        button_popup_settins_column = btn_popup_settings_menu

        checkForDeletedNotes()

        button_add.setOnClickListener(View.OnClickListener {
            val intent = Intent(context, CreateActivity::class.java)
            startActivityForResult(intent, CREATE_NEW_NOTE)
        })

        button_reply.setOnClickListener(View.OnClickListener {
            if(!deletedNotes.empty()){
                val (note, pos) = deletedNotes.pop()
                notes.add(pos, note)
                adapter!!.notifyDataSetChanged()
                putDataInDB()
                checkForDeletedNotes()
            }
        })

        button_popup_settins_column.setOnClickListener(View.OnClickListener {
            val popupSettings = PopupMenu(this, button_popup_settins_column)
            popupSettings.inflate(R.menu.popup_menu_columns)

            popupSettings.setOnMenuItemClickListener(object: PopupMenu.OnMenuItemClickListener {
                override fun onMenuItemClick(p0: MenuItem?): Boolean {
                    val itemId = p0?.itemId
                    when(itemId){
                        R.id.one_column -> {
                            numberOfColumns = 1
                            recycler.layoutManager = StaggeredGridLayoutManager(numberOfColumns, StaggeredGridLayoutManager.VERTICAL)
                            putNumberOfColumnsInDB()
                            return true
                        }
                        R.id.two_column -> {
                            numberOfColumns = 2
                            recycler.layoutManager = StaggeredGridLayoutManager(numberOfColumns, StaggeredGridLayoutManager.VERTICAL)
                            putNumberOfColumnsInDB()
                            return true
                        }
                        R.id.three_column -> {
                            numberOfColumns = 3
                            recycler.layoutManager = StaggeredGridLayoutManager(numberOfColumns, StaggeredGridLayoutManager.VERTICAL)
                            putNumberOfColumnsInDB()
                            return true
                        }
                        else -> return false
                    }
                }
            })

            popupSettings.show()
        })

        val itemTouchHelper = ItemTouchHelper(ItemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(recycler)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == CREATE_NEW_NOTE && resultCode == RESULT_OK){
            val noteTitle = data?.extras?.getString(NOTE_TITLE)
            val noteText = data?.extras?.getString(NOTE_TEXT)
            val noteTimeStamp = data?.extras?.getString(NOTE_TIMESTAMP)

            notes.add(Note(noteTitle!!, noteText!!, noteTimeStamp!!, null))
            adapter!!.notifyItemChanged(notes.size - 1)

            putDataInDB()

            Toast.makeText(context, "Заметка создана", Toast.LENGTH_SHORT).show()
        }
        else if(requestCode == EDIT_NOTE && resultCode == RESULT_OK){
            val noteTitle = data?.extras?.getString(NOTE_TITLE)
            val noteText = data?.extras?.getString(NOTE_TEXT)
            val noteTimestamp = data?.extras?.getString(NOTE_TIMESTAMP)
            val notePos = data?.extras?.getInt(NOTE_POSITION)

            val note = notes[notePos!!]
            note.setTitle(noteTitle!!)
            note.setText(noteText!!)
            note.setTimestamp("Изменено $noteTimestamp")

            adapter!!.notifyItemChanged(notePos)

            putDataInDB()

            Toast.makeText(context, "Заметка изменена", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            PERMISSION_CODE -> {
                if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){

                }
                else{
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    fun checkPermissions(){
        val permissions = arrayOf(
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )

        for (permission in permissions){
            if(checkSelfPermission(permission) == PackageManager.PERMISSION_DENIED){
                requestPermissions(permissions, PERMISSION_CODE)
            }
        }
    }

    fun putDataInDB(){
        val tinyDB = TinyDB(context)
        tinyDB.putObject(NOTES, NotesData(notes))
    }

    fun getDataFromDB(){
        val tinyDB = TinyDB(context)
        notesData = tinyDB.getObject(NOTES, NotesData::class.java)

        if(notesData != null){
            notes = notesData!!.notes
        }
    }

    fun putNumberOfColumnsInDB(){
        val tinyDB = TinyDB(context)
        tinyDB.putInt(NUMBER_OF_COLUMNS, numberOfColumns)
    }

    fun getNumberOfColumnsFromDB(){
        val tinyDB = TinyDB(context)
        numberOfColumns = tinyDB.getInt(NUMBER_OF_COLUMNS)
        if(numberOfColumns != 1 && numberOfColumns != 2 && numberOfColumns != 3){
            numberOfColumns = 2
        }
    }

    fun checkForDeletedNotes(){
        if(deletedNotes.empty()){
            button_reply.setInactive()
        }
        else{
            button_reply.setActive()
        }
    }
}