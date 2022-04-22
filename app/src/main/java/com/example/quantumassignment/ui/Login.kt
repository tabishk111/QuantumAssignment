package com.example.quantumassignment.ui

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.quantumassignment.MainActivity
import com.example.quantumassignment.ProfileActivity
import com.example.quantumassignment.R
import com.example.quantumassignment.databinding.FragmentLoginBinding
import com.google.firebase.auth.FirebaseAuth

class Login : Fragment() {

    private var _binding: FragmentLoginBinding?=null
    val binding get() = _binding!!

    private lateinit var progressDialog: ProgressDialog

    private lateinit var firebaseAuth: FirebaseAuth

    private var email = ""
    private var password = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)

        progressDialog = ProgressDialog(this@Login.requireContext())
        progressDialog.setTitle("Please Wait")
        progressDialog.setMessage("Logging In...")
        progressDialog.setCanceledOnTouchOutside(false)

        firebaseAuth = FirebaseAuth.getInstance()
        checkUser()


        binding.loginbtn.setOnClickListener {
            validateData()
        }

        return binding.root
    }

    private fun validateData() {
        email = binding.email.text.toString().trim()
        password = binding.pass.text.toString().trim()

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            binding.email.error = "Invalid Email Address"
        }
        else if(TextUtils.isEmpty(password)){
            binding.pass.error = "Please Enter Password"
        }
        else{
            firebaseLogin()
        }
    }

    private fun firebaseLogin() {
        progressDialog.show()
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                progressDialog.dismiss()
                val user = firebaseAuth.currentUser
                val email = user!!.email
                Toast.makeText(this@Login.requireContext(),"Logging as $email", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this@Login.requireContext(),ProfileActivity::class.java))
                activity?.finish()

            }
            .addOnFailureListener { e->
                progressDialog.dismiss()
                Toast.makeText(this@Login.requireContext(),"Login failed due to ${e.message}", Toast.LENGTH_LONG).show()
            }
    }

    private fun checkUser() {
        val user = firebaseAuth.currentUser
        if(user != null){
            Log.e("Login","Intent called1")
            startActivity(Intent(this@Login.requireContext(),ProfileActivity::class.java))
            Log.e("Login","Intent called2")
            activity?.finish()
            Log.e("Login","Intent called3")
        }

    }
}