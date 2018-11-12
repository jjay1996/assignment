package a13341.egco428.com.a13341

import a13341.egco428.com.a13341.DataSource.CookieDataSource
import a13341.egco428.com.a13341.Model.Cookie
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import android.widget.ArrayAdapter
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_add_delete_message.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_new_cookie.view.*
import kotlinx.android.synthetic.main.list_main.*
import kotlinx.android.synthetic.main.list_main.view.*

class MainActivity : AppCompatActivity() {

    private var dataSource:CookieDataSource? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setTitle("                        Fortune Cookies")

        val data = CookiePovider.getData()

        val courseArrayAdapter = CourseArrayAdapter(this,0,data!!)
        mesageList.setAdapter(courseArrayAdapter)
        courseArrayAdapter.notifyDataSetChanged()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu,menu)
        return true
    }

    //คล้ายๆonClick
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val id = item!!.getItemId()

        if(id == R.id.addBtn){
            val intent = Intent(this,NewCookie::class.java)
            startActivity(intent)
        }
        return super.onOptionsItemSelected(item)
    }

    private class CourseArrayAdapter(var context: Context, var resource:Int, var objects :ArrayList<CookieMessage>): BaseAdapter(){
        override fun getCount(): Int {
            return objects.size
        }

        override fun getItem(position: Int): Any {
            return objects[position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val cookies = objects[position]
            val view :View

            if(convertView==null){

                val layoutInflater = LayoutInflater.from(parent!!.context)
                view = layoutInflater.inflate(R.layout.list_main,null)

                val ViewHolder = ViewHolder(view.wordMain,view.dateMain,view.imageMain,view.textcenterMain)
                view.tag = ViewHolder
            }else{
                view = convertView
            }

            val viewHolder = view.tag as ViewHolder

            if (cookies.type == "Positive"){
                viewHolder.wordM.setTextColor(Color.BLUE)
            }else if (cookies.type == "Negative"){
                viewHolder.wordM.setTextColor(Color.RED)
            }
            viewHolder.wordM.text = cookies.message
            viewHolder.dateM.text = cookies.date
            viewHolder.textcenterM.text = cookies.message
            view.imageMain.setImageResource(R.drawable.opened_cookie)



            return view
        }
    }

    private class ViewHolder(val wordM:TextView,val dateM: TextView, imageM: ImageView,val textcenterM:TextView)
}
