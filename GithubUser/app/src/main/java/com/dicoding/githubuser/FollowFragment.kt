
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.ItemsItem
import com.dicoding.githubuser.databinding.FragmentFollowBinding
import com.dicoding.githubuser.model.UserListViewModel
import com.dicoding.githubuser.ui.UserAdapter

class FollowFragment : Fragment() {

    private lateinit var binding: FragmentFollowBinding
    private val adapter = UserAdapter()
    private lateinit var viewModel: UserListViewModel
    private var position: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFollowBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Retrieve username and position from arguments
        arguments?.let {
            position = it.getInt(ARG_POSITION) ?: 0
        }

        // Initialize ViewModel
        viewModel = ViewModelProvider(this).get(UserListViewModel::class.java)

        // Set up RecyclerView
        binding.rvFollow.layoutManager = LinearLayoutManager(requireContext())
        binding.rvFollow.adapter = adapter

        // Set up observers
        viewModel.following.observe(viewLifecycleOwner) { following ->
            recyclerViewSetUp(following)
        }

        viewModel.followers.observe(viewLifecycleOwner) { followers ->
            recyclerViewSetUp(followers)
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) {
                // Show progress bar
                binding.progressBar.visibility = View.VISIBLE
            } else {
                // Hide progress bar
                binding.progressBar.visibility = View.GONE
            }
        }

        // Retrieve data based on position
        if (position == 0) {
            viewModel.getFollowers(username)
        } else if (position == 1){
            viewModel.getFollowing(username)
        } else {
            throw IllegalArgumentException("Invalid tab position: $position")
        }
    }

    private fun recyclerViewSetUp(users: List<ItemsItem>?) {
        // Submit list of users to the adapter
        adapter.submitList(users)
    }

    companion object {
        const val ARG_POSITION = "arg_position"
        const val ARG_USERNAME = "username"
        var username: String = ""

        fun newInstance(position: Int): FollowFragment {
            val fragment = FollowFragment()
            val args = Bundle().apply {
                putInt(ARG_POSITION, position)
            }
            fragment.arguments = args
            return fragment
        }
    }
}
