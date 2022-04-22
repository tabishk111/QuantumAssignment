package com.example.quantumassignment.ui

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.quantumassignment.ProfileActivity
import com.example.quantumassignment.databinding.FragmentSignupBinding
import com.google.firebase.auth.FirebaseAuth

class SignUp : Fragment() {

    private var _binding: FragmentSignupBinding?=null
    val binding get() = _binding!!

    private lateinit var progressDialog: ProgressDialog

    private lateinit var firebaseAuth: FirebaseAuth

    private var email = ""
    private var password = ""
    private var number = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSignupBinding.inflate(inflater, container, false)

        progressDialog = ProgressDialog(this@SignUp.requireContext())
        progressDialog.setTitle("Please Wait")
        progressDialog.setMessage("Creating Account...")
        progressDialog.setCanceledOnTouchOutside(false)

        firebaseAuth = FirebaseAuth.getInstance()

        binding.signupbtn.setOnClickListener {
            validateData()
        }

        return binding.root
    }

    private fun validateData() {
        email = binding.email.text.toString().trim()
        password = binding.pass.text.toString().trim()
        number = binding.contact.text.toString().trim()
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            binding.email.error = "Invalid email"
        }
        else if(TextUtils.isEmpty(password)){
            binding.pass.error = "Please enter valid password"
        }
        else if(password.length<6){
            binding.pass.error = "Password must be atleast 6 character long"
        }
        else if(number.length!=10){
            binding.pass.error = "Enter correct Contact number"
        }
        else if(binding.check.equals(false)){
            Toast.makeText(this@SignUp.requireContext(),"Check terms and condition",Toast.LENGTH_SHORT).show()
        }
        else{
            firebaseSignUp()
        }
    }

    private fun firebaseSignUp() {
        progressDialog.show()
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                progressDialog.dismiss()
                val user = firebaseAuth.currentUser
                val email = user!!.email
                Toast.makeText(this@SignUp.requireContext(),"Account created successfully", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this@SignUp.requireContext(),ProfileActivity::class.java))
                activity?.finish()
            }
            .addOnFailureListener { e->
                progressDialog.dismiss()
                Toast.makeText(this@SignUp.requireContext(),"Sign Up failed due to ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

}