package a13341.egco428.com.a13341

import a13341.egco428.com.a13341.DataSource.CookieDataSource
import a13341.egco428.com.a13341.Model.Cookie
import android.content.Intent
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_add_delete_message.*

class AddDeleteMessage : AppCompatActivity() {

    private var cookieType :String?=null
    private var dataSource: CookieDataSource?=null
    private var selector : Spinner? = null
    var list_of_items = arrayOf("Positive", "Negative")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_delete_message)

        setTitle("Add/Delete Fortune Cookies")
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        val myspin = findViewById(R.id.spinneR) as Spinner
        val spinAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, list_of_items)
        spinAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
        myspin.setAdapter(spinAdapter)
        //item selected listener for spinner
        myspin.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                cookieType = p0!!.getItemAtPosition(p2).toString()
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {

            }
        }

        dataSource = CookieDataSource(this)
        dataSource!!.open()

        val values = dataSource!!.allComments
        val adapter = object : ArrayAdapter<Cookie>(this, android.R.layout.simple_list_item_2, android.R.id.text1, values) {
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = super.getView(position, convertView, parent)
                val text1 = view.findViewById(android.R.id.text1) as TextView
                val text2 = view.findViewById(android.R.id.text2) as TextView

                if(values.get(position).type == "Positive"){
                    text1.setText(values.get(position).message)
                    text1.setTextColor(Color.BLUE)
                    text2.setText(values.get(position).type)
                    text2.setTextColor(Color.BLUE)
                }else if(values.get(position).type == "Negative"){
                    text1.setText(values.get(position).message)
                    text1.setTextColor(Color.RED)
                    text2.setText(values.get(position).type)
                    text2.setTextColor(Color.RED)
                }
                return view
            }
        }
        listMessage.setAdapter(adapter)
//
//
        addBtn.setOnClickListener {
            var cookie :Cookie?
            cookie = dataSource!!.createComment(addResult.text.toString(),cookieType.toString())
            adapter.add(cookie)
            adapter.notifyDataSetChanged()

        }
        listMessage.setOnItemClickListener { parent, view, position, id ->
            var cookie = values.get(position)
            dataSource!!.deleteComment(cookie!!)
            adapter.remove(cookie)
            adapter.notifyDataSetChanged()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.adddelete_menu,menu)
        return true
    }

    //คล้ายๆonClick
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val id = item!!.getItemId()

        if (id == R.id.home){
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        super.onResume()
        dataSource!!.open()
    }

    override fun onPause() {
        super.onPause()
        dataSource!!.close()
    }
}
