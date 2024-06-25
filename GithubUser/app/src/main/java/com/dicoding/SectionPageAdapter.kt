import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class SectionPageAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {

    companion object {
        const val NUM_TABS = 2
    }

    override fun getItemCount(): Int = NUM_TABS

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> FollowFragment.newInstance(0)
            1 -> FollowFragment.newInstance(1)
            else -> throw IllegalArgumentException("Invalid tab position")
        }
    }
}