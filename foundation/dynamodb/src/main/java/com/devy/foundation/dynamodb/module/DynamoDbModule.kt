package com.devy.foundation.dynamodb.module

import android.content.Context
import com.amazonaws.auth.CognitoCachingCredentialsProvider
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper
import com.amazonaws.regions.Region
import com.amazonaws.regions.Regions
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient
import com.devy.foundation.dynamodb.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Created by JeYHoon on 2025. 10. 13..
 */

@Module
@InstallIn(SingletonComponent::class)
object DynamoDbModule {

    @Provides
    @Singleton
    fun provideCognitoCredentialsProvider(
        @ApplicationContext context: Context
    ): CognitoCachingCredentialsProvider {
        return CognitoCachingCredentialsProvider(
            context,
            BuildConfig.AWS_IDENTITY_POOL_ID,
            Regions.AP_NORTHEAST_2
        )
    }

    @Provides
    @Singleton
    fun provideDynamoDBClient(
        credentialsProvider: CognitoCachingCredentialsProvider
    ): AmazonDynamoDBClient {
        return AmazonDynamoDBClient(credentialsProvider).apply { setRegion(Region.getRegion(Regions.AP_NORTHEAST_2)) }
    }

    @Provides
    @Singleton
    fun provideDynamoDbMapper(client: AmazonDynamoDBClient): DynamoDBMapper {
        return DynamoDBMapper.builder().dynamoDBClient(client).build()
    }
}