package com.example.nycschools.view

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavArgs
import androidx.navigation.fragment.navArgs
import com.example.nycschools.R
import com.example.nycschools.databinding.FragmentSchoolDetailsBinding

class SchoolDetailsFragment : Fragment(R.layout.fragment_school_details) {

    private lateinit var binding: FragmentSchoolDetailsBinding
    private val args: SchoolDetailsFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentSchoolDetailsBinding.bind(view)
        val schoolDetails = args.schooldata

        // The texts can be formatted in better way.

        binding.apply {
            tvSchoolName.text = schoolDetails.name
            tvSchoolAddress.text = schoolDetails.address +", " + schoolDetails.city + ", " +
                    schoolDetails.state + " - " + schoolDetails.zip
            tvSchoolWebsite.text = schoolDetails.website
            tvSchoolEmail.text = schoolDetails.email
            tvSchoolPhone.text = schoolDetails.phone
            tvSchoolFax.text = schoolDetails.fax
            tvTotalTestTakers.text = schoolDetails.testTakers.toString()
            tvReadingScore.text = schoolDetails.criticalReading.toString()
            tvWritingScore.text = schoolDetails.writing.toString()
            tvMathScore.text = schoolDetails.math.toString()
        }
    }
}