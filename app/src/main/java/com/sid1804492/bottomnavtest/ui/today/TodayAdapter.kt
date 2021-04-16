package com.sid1804492.bottomnavtest.ui.today

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.sid1804492.bottomnavtest.ui.today_tabs.TodayHomeworkFragment
import com.sid1804492.bottomnavtest.ui.today_tabs.TodayEventFragment
import com.sid1804492.bottomnavtest.ui.today_tabs.TodayTodoFragment

class TodayAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> {
                TodayTodoFragment()
            }
            1 -> {
                TodayHomeworkFragment()
            }
            else -> {
                TodayEventFragment()
            }
        }
    }

}