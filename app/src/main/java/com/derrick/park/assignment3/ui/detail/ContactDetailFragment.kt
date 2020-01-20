package com.derrick.park.assignment3.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.derrick.park.assignment3.databinding.FragmentContactDetailBinding


/**
 * DetailFragment for contact details
 */
class ContactDetailFragment : Fragment() {

    private val viewModel: ContactDetailViewModel by lazy {
        ViewModelProviders
            .of(this,
                ContactDetailViewModel
                    .Factory(ContactDetailFragmentArgs.fromBundle(arguments!!).selectedContact))
            .get(ContactDetailViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentContactDetailBinding.inflate(inflater).also {
            it.lifecycleOwner = this
            it.viewModel = viewModel
        }

        return binding.root
    }
}
