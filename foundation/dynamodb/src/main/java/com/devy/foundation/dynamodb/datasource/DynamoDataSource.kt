package com.devy.foundation.dynamodb.datasource

import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBScanExpression
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.reflect.KClass

/**
 * Created by JeYHoon on 2025. 10. 16..
 */
class DynamoDataSource @Inject constructor(
    private val dynamoDBMapper: DynamoDBMapper,
) {

    /**
     * 특정 테이블의 모든 아이템을 스캔하여 리스트로 가져오는 제네릭 함수
     * @param T 가져올 데이터 모델의 타입 (예: UserProfile)
     * @param clazz 가져올 데이터 모델의 KClass (예: UserProfile::class)
     * @return 성공 시 List<T>, 실패 시 Exception을 담은 Result
     */
    suspend fun <T : Any> getList(clazz: KClass<T>): Result<List<T>> = withContext(Dispatchers.IO) {
        runCatching {
            // 💡 DynamoDBMapper.scan() 메서드를 사용합니다.
            // scan은 테이블의 모든 데이터를 읽어오는 작업입니다. (데이터가 많을 경우 비용과 성능에 주의)
            val scanExpression = DynamoDBScanExpression()
            val scanResult: List<T> = dynamoDBMapper.scan(clazz.java, scanExpression)
            scanResult
        }
    }

    /**
     * 기본 키(Primary Key)를 사용해 단일 아이템을 가져오는 제네릭 함수
     * @param T 가져올 데이터 모델의 타입 (예: UserProfile)
     * @param clazz 가져올 데이터 모델의 KClass (예: UserProfile::class)
     * @param id 조회할 아이템의 기본 키 (Hash Key)
     */
    suspend fun <T : Any> getItem(clazz: KClass<T>, id: Any): Result<T> = withContext(Dispatchers.IO) {
        runCatching {
            dynamoDBMapper.load(clazz.java, id)
                ?: throw NoSuchElementException("Item not found with id: $id")
        }
    }

    /**
     * 단일 아이템을 저장하거나 업데이트하는 제네릭 함수
     * @param T 저장할 데이터 모델의 타입 (예: UserProfile)
     * @param item 저장할 객체 인스턴스
     */
    suspend fun <T : Any> saveItem(item: T): Result<Unit> = withContext(Dispatchers.IO) {
        try{
            dynamoDBMapper.save(item)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun <T : Any> deleteItem(item: T): Result<Unit> = withContext(Dispatchers.IO) {
        try{
            dynamoDBMapper.delete(item)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}