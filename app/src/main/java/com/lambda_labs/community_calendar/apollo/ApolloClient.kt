package com.lambda_labs.community_calendar.apollo

import com.apollographql.apollo.ApolloClient
import okhttp3.OkHttpClient

object ApolloClient {
    fun client(): ApolloClient {

        val BASE_URL = "https://ccstaging.herokuapp.com/graphql"

        val okHttp = OkHttpClient
            .Builder()
            .build()

        return ApolloClient
            .builder()
            .serverUrl(BASE_URL)
            .okHttpClient(okHttp)
            .build()
    }
}