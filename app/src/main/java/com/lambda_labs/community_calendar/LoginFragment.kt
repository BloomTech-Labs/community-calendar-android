package com.lambda_labs.community_calendar

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.auth0.android.Auth0
import com.auth0.android.Auth0Exception
import com.auth0.android.authentication.AuthenticationException
import com.auth0.android.provider.AuthCallback
import com.auth0.android.provider.VoidCallback
import com.auth0.android.provider.WebAuthProvider
import com.auth0.android.result.Credentials
import kotlinx.android.synthetic.main.fragment_login.*


class LoginFragment : Fragment() {
    // TODO: Rename and change types of parameters
    lateinit var auth0: Auth0
    lateinit var mainActivity: MainActivity


    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }

    /**
     * Called immediately after [.onCreateView]
     * has returned, but before any saved state has been restored in to the view.
     * This gives subclasses a chance to initialize themselves once
     * they know their view hierarchy has been completely created.  The fragment's
     * view hierarchy is not however attached to its parent at this point.
     * @param view The View returned by [.onCreateView].
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        blank_logout.setOnClickListener {

            WebAuthProvider.logout(auth0)
                .withScheme("demo")
                .start(mainActivity, object : VoidCallback {
                    override fun onSuccess(payload: Void?) {
                        println("logged out")

                        mainActivity.listener(false)
                    }

                    override fun onFailure(error: Auth0Exception?) {
                        println("logout error")
                    }
                })
        }
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

                    mainActivity.listener(true)
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
