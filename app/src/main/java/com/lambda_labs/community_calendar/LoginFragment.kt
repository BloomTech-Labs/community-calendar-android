package com.lambda_labs.community_calendar

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.auth0.android.Auth0
import com.auth0.android.authentication.AuthenticationException
import com.auth0.android.provider.AuthCallback
import com.auth0.android.provider.WebAuthProvider
import com.auth0.android.result.Credentials


class LoginFragment : Fragment() {
    // TODO: Rename and change types of parameters
    lateinit var auth0: Auth0
    lateinit var mainActivity: Activity


    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as Activity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth0 = Auth0(mainActivity)
        auth0.isOIDCConformant = true


        WebAuthProvider.login(auth0)
            .withScheme("demo")
            .withAudience(getString(R.string.audience))
            .start(mainActivity, object : AuthCallback {
                override fun onSuccess(credentials: Credentials) {
                    println(credentials.accessToken)
                }

                override fun onFailure(dialog: Dialog) {
                    println("Fail Dialog")
                }

                override fun onFailure(exception: AuthenticationException?) {
                    println(exception?.message)
                }

            })


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false)
    }


}
