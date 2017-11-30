package com.egci428.firebasedatabase1

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    lateinit var dataReference: DatabaseReference
    lateinit var messageList: MutableList<Message>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        dataReference = FirebaseDatabase.getInstance()
                .getReference("data")
        messageList = mutableListOf()
        saveBtn.setOnClickListener {
            SaveData()
        }

        dataReference.addValueEventListener(object: ValueEventListener{
            override fun onCancelled(p0: DatabaseError?) {

            }

            override fun onDataChange(dataSnapshot: DataSnapshot?) {
                if(dataSnapshot!!.exists()){
                    messageList.clear()
                    for(i in dataSnapshot.children){
                        val message = i.getValue(Message::class.java)
                        messageList.add(message!!)
                    }
                    val adapter = MessageAdapter(applicationContext,R.layout.messages,messageList)
                    listView.adapter = adapter
                }
            }
        })
    }
    private fun SaveData(){
        val msg = editText.text.toString()
        if(msg.isEmpty()){
            editText.error = "Please Enter a message"
            return
        }

        val messageId = dataReference.push().key
        val messageData = Message(messageId,msg,ratingBar.rating.toInt())
        dataReference.child(messageId).setValue(messageData)
                .addOnCompleteListener {
                    Toast.makeText(applicationContext,"Message saved succesfully",Toast.LENGTH_SHORT).show()
                }
    }
}
