package com.example.textrecognization

import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.tasks.Task
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage

class MainActivity : AppCompatActivity() {
    private lateinit var img:ImageView
    private lateinit var text:TextView
    private lateinit var btn:Button
    private val REQUEST_CODE=403
    private val REQUEST_CODE1=503
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        img=findViewById(R.id.img_view)
        text=findViewById(R.id.textView)
    }

    fun Capture(view: View) {
          val intent= Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if(intent.resolveActivity(packageManager)!=null)
        startActivityForResult(intent,REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==REQUEST_CODE && resultCode== RESULT_OK && data!=null) {
            val bundle:Bundle= data.extras!!
            val bitmap: Any? =bundle.get("data")
            img.setImageBitmap(bitmap as Bitmap?)
            val firebaseVisionImage=FirebaseVisionImage.fromBitmap(bitmap!!)
            val firebaseVision=FirebaseVision.getInstance()
            val firebaseVisionTextRecognizer=firebaseVision.onDeviceTextRecognizer
            firebaseVisionTextRecognizer.processImage(firebaseVisionImage).addOnSuccessListener {
                text.text = it.text
            }.addOnFailureListener {
                Toast.makeText(this,"Failed",Toast.LENGTH_SHORT).show()
            }
        }
        if(requestCode==REQUEST_CODE1 && resultCode== RESULT_OK && data!=null)
        {
            img.setImageURI(data.data)
            val firebaseVisionImage=FirebaseVisionImage.fromFilePath(applicationContext, data.data!!)
            val firebaseVisionTextRecognizer=FirebaseVision.getInstance().onDeviceTextRecognizer
            firebaseVisionTextRecognizer.processImage(firebaseVisionImage).addOnSuccessListener {
                text.text = it.text
            }.addOnFailureListener {
                Toast.makeText(applicationContext,"Failed To Process",Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun selectImage(view: View) {
        val intent=Intent()
        intent.type = "image/*"
        intent.action=Intent.ACTION_GET_CONTENT
        if(intent.resolveActivity(packageManager)!=null)
            startActivityForResult(intent,REQUEST_CODE1)

    }

}