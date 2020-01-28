package com.lambda_labs.community_calendar.apollo

import com.apollographql.apollo.ApolloClient
import okhttp3.OkHttpClient

object ApolloClient {
    fun client(): ApolloClient {

        // Base url of are GraphQL server
        val BASE_URL = "https://ccapollo-production.herokuapp.com/"

        // Building a OkHttpClient to use with Apollo
        val okHttp = OkHttpClient
            .Builder()
            .build()

        // Returns an ApolloClient to be used to make the API call
        return ApolloClient
            .builder()
            .serverUrl(BASE_URL)
            .okHttpClient(okHttp)
            .build()
    }
}