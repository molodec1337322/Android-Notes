package com.example.androidnotes.activities

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import com.example.androidnotes.R
import com.example.androidnotes.custom.CanvasView
import kotlinx.android.synthetic.main.activity_drawing.*
import java.io.ByteArrayOutputStream
import java.io.FileOutputStream

class DrawingActivity : AppCompatActivity() {

    lateinit var canvasView: CanvasView
    lateinit var btn_close: ImageButton
    lateinit var btn_save: ImageButton
    lateinit var btn_brush_settings: ImageButton

    companion object{
        const val BITMAP_TO_IMPORT = "bitmap_to_import"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_drawing)
        canvasView = canvas

        btn_close = btn_draw_new_cancel
        btn_close.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, CreateActivity::class.java)
            setResult(CreateActivity.RESULT_CANCELED, intent)
            finish()
        })

        btn_save = btn_draw_new_accept
        btn_save.setOnClickListener(View.OnClickListener {
            if(!canvasView.isBitmapChanged){
                val intent = Intent(this, CreateActivity::class.java)
                setResult(CreateActivity.RESULT_CANCELED, intent)
                finish()
            }
            else{
                val intent = Intent(this, CreateActivity::class.java)
                /*
                intent.putExtra(BITMAP_TO_IMPORT, canvasView.bitmap)
                 */
                createImageFromBitmap(canvas.bitmap)
                setResult(CreateActivity.RESULT_OK, intent)
                finish()
            }
        })

        btn_brush_settings = btn_draw_new_brush_settings
        btn_brush_settings.setOnClickListener(View.OnClickListener {

        })
    }

    fun createImageFromBitmap(bitmap: Bitmap): String? {
        var fileName: String? = BITMAP_TO_IMPORT //no .png or .jpg needed
        try {
            val bytes = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
            val fo: FileOutputStream = openFileOutput(fileName, Context.MODE_PRIVATE)
            fo.write(bytes.toByteArray())
            // remember close file output
            fo.close()
        } catch (e: Exception) {
            e.printStackTrace()
            fileName = null
        }
        return fileName
    }
}