package com.example.term_project

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import com.example.term_project.Model.User
import com.example.term_project.Model.UserItem
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.activity_chat_list.*

class ChatListActivity : AppCompatActivity() {

    val db = Firebase.firestore
    lateinit var chatListBtn : Button
    private  val TAG = ChatListActivity::class.java.simpleName
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_list)

        chatListBtn = findViewById<Button>(R.id.myChatList)
        val adapter = GroupAdapter<GroupieViewHolder>()

        chatListBtn.setOnClickListener {
            val intent = Intent(this, MyroomActivity::class.java)
            startActivity(intent)
        }

        db.collection("users")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    adapter.add(UserItem(document.get("username").toString(), document.get("uid").toString()))
                    Log.d(TAG, document.get("username").toString())
                    Log.d(TAG, "${document.id} => ${document.data}")
                }
                recyclerView_list.adapter = adapter
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents.", exception)
            }
        adapter.setOnItemClickListener { item, view ->

            val uid = (item as UserItem).uid
            val name = (item as UserItem).name
            val intent = Intent(this, ChatRoomActivity::class.java)
            intent.putExtra("setUid", uid)
            intent.putExtra("setName", name)
            startActivity(intent)
        }


    }
}