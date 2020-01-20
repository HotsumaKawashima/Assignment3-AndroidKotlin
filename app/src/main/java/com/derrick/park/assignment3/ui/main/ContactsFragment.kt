package com.derrick.park.assignment3.ui.main


import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.derrick.park.assignment3.R
import com.derrick.park.assignment3.databinding.FragmentContactsBinding
import com.derrick.park.assignment3.domain.Contact

/**
 * A simple [Fragment] subclass.
 */
class ContactsFragment : Fragment() {

    private val viewModel: ContactsViewModel by lazy {
        val activity = requireNotNull(this.activity) {
            "You can only access the viewModel after onActivityCreated()"
        }
        ViewModelProviders.of(this, ContactsViewModel.Factory(activity.application))
            .get(ContactsViewModel::class.java)
    }

    /**
     * RecyclerView Adapter for converting a list of contacts to ViewHolder
     */
    private var viewModelAdapter: ContactsAdapter? = null

    /**
     * Called when the fragment's activity has been created and this
     * fragment's view hierarchy instantiated.  It can be used to do final
     * initialization once these pieces are in place, such as retrieving
     * views or restoring state. (gets called right after onCreateView)
     */
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.contacts.observe(viewLifecycleOwner, Observer<List<Contact>> { contacts ->
            contacts.apply {
                viewModelAdapter?.contacts = contacts
                viewModelAdapter?.addHeaderAndSubmitList(contacts)
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentContactsBinding.inflate(inflater)
        // Set the lifecycleOwner so DataBinding can observe LiveData
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        viewModelAdapter = ContactsAdapter(OnContactClickListener {
            viewModel.onContactClicked(it)
        })

        binding.contactList.adapter = viewModelAdapter
        binding.addFab.setOnClickListener {
            this.findNavController().navigate(
                ContactsFragmentDirections
                    .actionContactsFragmentToAddContactFragment())
        }

        viewModel.networkErrorStatus.observe(this, Observer<ContactsApiStatus> {
            if (it == ContactsApiStatus.ERROR) {
                Toast.makeText(activity, "Network Error", Toast.LENGTH_SHORT).show()
                // just fetch from the database (offline cache)
            }
        })

        viewModel.navigateToContactDetail.observe(this, Observer {
            if (it != null) {
                this.findNavController()
                    .navigate(ContactsFragmentDirections.actionContactsFragmentToDetailFragment(it))
                viewModel.onContactDetailNavigated()
            }
        })
        setHasOptionsMenu(true)

        return binding.root
    }

    /**
     * Inflates the overflow menu that contains filtering options.
     */
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.contacts_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.refresh_contacts-> viewModel.refreshDataFromRepository()
            else -> viewModel.clearDataFromRepository()
        }
        return true
    }
}

