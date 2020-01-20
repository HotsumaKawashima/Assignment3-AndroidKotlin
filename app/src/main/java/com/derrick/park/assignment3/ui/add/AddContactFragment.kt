package com.derrick.park.assignment3.ui.add


import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.derrick.park.assignment3.database.getDatabase
import com.derrick.park.assignment3.databinding.FragmentAddContactBinding
import com.derrick.park.assignment3.repository.ContactsRepository
import com.derrick.park.assignment3.util.isSaveButtonEnabled

/**
 *
 * AddContactFragment for adding a new contact
 */
class AddContactFragment : Fragment() {

    private val viewModel: AddContactViewModel by lazy {
        val activity = requireNotNull(this.activity) {
            "You can only access the viewModel after onActivityCreated()"
        }
        ViewModelProviders.of(this, AddContactViewModel.Factory(
            ContactsRepository(getDatabase(activity.application))
        )).get(AddContactViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentAddContactBinding.inflate(inflater)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        viewModel.navigateToContacts.observe(this, Observer {
            if (it == true) {
                this.findNavController().navigate(
                    AddContactFragmentDirections.actionAddContactFragmentToContactsFragment())
                viewModel.onContactsNavigated()
                hideKeyboard()
            }
        })

        binding.nameEt.doAfterTextChanged {
            binding.saveBtn.isEnabled =
                isSaveButtonEnabled(binding.cellEt.text.toString(), binding.nameEt.text.toString())
        }

        binding.cellEt.doAfterTextChanged {
            binding.saveBtn.isEnabled =
                isSaveButtonEnabled(binding.cellEt.text.toString(), binding.nameEt.text.toString())
        }

        return binding.root
    }

    private fun hideKeyboard() {
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view!!.windowToken, 0)
    }
}
