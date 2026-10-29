package com.devy.core_ui.utils

import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import com.devy.core_ui.model.image.ImageFolderData

object ImageFolderUtil {

    fun getImageFolders(context: Context): List<ImageFolderData> {
        val imageFolders = mutableListOf<ImageFolderData>()
        val folderMap = mutableMapOf<Long, ImageFolderData>()

        // 쿼리할 컬럼 정의
        val projection = arrayOf(
            MediaStore.Images.Media.BUCKET_ID,
            MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
            MediaStore.Images.Media._ID // 이미지 자체의 ID
        )

        // API 29 (Android 10) 이상에서는 `contentUri` 사용 권장
        val contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI

        // ContentResolver를 사용하여 이미지 쿼리
        context.contentResolver.query(
            contentUri,
            projection,
            null,
            null,
            "${MediaStore.Images.Media.DATE_MODIFIED} DESC" // 최신순으로 정렬
        )?.use { cursor ->
            val bucketIdColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_ID)
            val bucketNameColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME)
            val imageIdColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)

            while (cursor.moveToNext()) {
                val bucketId = cursor.getLong(bucketIdColumn)
                val bucketName = cursor.getString(bucketNameColumn) ?: "알 수 없는 폴더"
                val imageId = cursor.getLong(imageIdColumn)
                val imageUri = Uri.withAppendedPath(contentUri, imageId.toString())

                // 이미 맵에 폴더가 있으면 이미지 개수만 증가
                if (folderMap.containsKey(bucketId)) {
                    val existingFolder = folderMap[bucketId]!!
                    folderMap[bucketId] = existingFolder.copy(count = existingFolder.count + 1)
                } else {
                    // 새로운 폴더면 맵에 추가 (첫 이미지를 썸네일로 사용)
                    folderMap[bucketId] = ImageFolderData(
                        bucketId = bucketId,
                        title = bucketName,
                        count = 1,
                        thumbnailUri = imageUri,
                    )
                }
            }
        }

        imageFolders.addAll(folderMap.values.sortedBy { it.title }) // 폴더 이름순으로 정렬
        return imageFolders
    }

    /**
     * 특정 폴더에 속한 모든 이미지의 목록을 가져옵니다.
     * @param context 컨텍스트
     * @param bucketId 이미지를 가져올 폴더의 고유 ID
     * @return ImageItem 리스트
     */
    fun getImagesInFolder(context: Context, bucketId: Long): List<String> {
        val imageList = mutableListOf<String>()
        val contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI

        // 쿼리할 컬럼: 이미지의 고유 ID
        val projection = arrayOf(MediaStore.Images.Media._ID)

        // 선택 조건: 특정 bucketId에 해당하는 이미지만 선택
        val selection = "${MediaStore.Images.Media.BUCKET_ID} = ?"
        val selectionArgs = arrayOf(bucketId.toString())

        // 정렬 순서: 최신 이미지가 위로 오도록
        val sortOrder = "${MediaStore.Images.Media.DATE_MODIFIED} DESC"

        context.contentResolver.query(
            contentUri,
            projection,
            selection,
            selectionArgs,
            sortOrder
        )?.use { cursor ->
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
            while (cursor.moveToNext()) {
                val id = cursor.getLong(idColumn)
                val uri = Uri.withAppendedPath(contentUri, id.toString())
                imageList.add(uri.toString())
            }
        }
        return imageList
    }

    /**
     * 기기에 있는 모든 이미지 목록을 가져옵니다.
     * @param context 컨텍스트
     * @return ImageItem 리스트
     */
    fun getAllImages(context: Context): List<String> {
        val imageList = mutableListOf<String>()
        val contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI

        val projection = arrayOf(MediaStore.Images.Media._ID)
        val sortOrder = "${MediaStore.Images.Media.DATE_MODIFIED} DESC"

        context.contentResolver.query(
            contentUri,
            projection,
            null, // selection과 selectionArgs를 null로 설정하여 모든 이미지를 가져옴
            null,
            sortOrder
        )?.use { cursor ->
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
            while (cursor.moveToNext()) {
                val id = cursor.getLong(idColumn)
                val uri = Uri.withAppendedPath(contentUri, id.toString())
                imageList.add(uri.toString())
            }
        }
        return imageList
    }

    /**
     * 기기에 저장된 모든 이미지의 개수를 안정적으로 가져옵니다.
     * @param context 컨텍스트
     * @return 전체 이미지의 개수 (Int)
     */
    fun getAllImageCount(context: Context): Int {
        var count = 0
        val contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI

        // 가장 가벼운 데이터인 _ID 컬럼 하나만 요청합니다.
        val projection = arrayOf(MediaStore.Images.Media._ID)

        try {
            context.contentResolver.query(
                contentUri,
                projection, // _ID 컬럼만 요청
                null,
                null,
                null
            )?.use { cursor ->
                // cursor.count는 쿼리 결과로 나온 행(row)의 총 개수를 반환합니다.
                count = cursor.count
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return count
    }
}