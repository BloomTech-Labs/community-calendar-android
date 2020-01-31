package com.lambda_labs.community_calendar.apollo

import com.apollographql.apollo.ApolloClient
import okhttp3.OkHttpClient

object ApolloClient {
    // Base url of are GraphQL server
    private val BASE_URL = "https://ccapollo-staging.herokuapp.com/"
    // Building a OkHttpClient to use with Apollo
    private val okHttp = OkHttpClient.Builder()
    private val apolloClient = ApolloClient.builder().serverUrl(BASE_URL)


    // Returns an ApolloClient to be used to make the API call without Authorization
    fun client(): ApolloClient {

        return apolloClient
            .okHttpClient(okHttp.build())
            .build()
    }

    // // Returns an ApolloClient to be used to make the API call with Authorization
    fun authClient(token: String): ApolloClient{

        val authOkHttp = okHttp.addInterceptor {
            val request = it.request()
                .newBuilder()
                .addHeader("Authorization", "Bearer $token")
                .build()
            it.proceed(request)
        }
            .build()

        return apolloClient
            .okHttpClient(authOkHttp)
            .build()

    }



}