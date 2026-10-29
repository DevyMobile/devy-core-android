package com.devy.core_ui.utils

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.media.ExifInterface
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.core.net.toUri
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

object ImageUtils {
    fun uriToBitmap(context: Context, uri: Uri): Bitmap {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val source = ImageDecoder.createSource(context.contentResolver, uri)
            ImageDecoder.decodeBitmap(source) { decoder, _, _ ->
                decoder.allocator = ImageDecoder.ALLOCATOR_SOFTWARE
            }
        } else {
            MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
        }
    }

    fun createFolder(
        context: Context,
        folderName: String = System.currentTimeMillis().toString()
    ): CreatedImageFolder {
        val internalDir = context.filesDir
        val folder = File(internalDir, folderName)
        if (folder.exists().not()) {
            folder.mkdirs()
        }

        return CreatedImageFolder(folderName, folder.absolutePath)
    }

    /**
     * 캐시에 저장된 파일을 앱 내부 저장소의 특정 폴더로 복사하는 함수
     * @param context 컨텍스트
     * @param folderName 저장할 폴더 이름 (예: "gemini_images")
     * @param sourceFile 이미 저장된 캐시 파일 (GeminiImageResponse에서 받은 파일)
     * @return 저장된 새로운 File 객체
     */
    fun saveFileToPermanentStorage(context: Context, folderName: String, sourceFile: File): File? {
        val internalDir = context.filesDir
        val folder = File(internalDir, folderName)

        // 1. 폴더 생성
        if (!folder.exists()) {
            folder.mkdirs()
        }

        // 2. 새 파일 생성 (이름 유지 또는 새로 생성)
        val targetFile = File(folder, "image_${System.currentTimeMillis()}.png")

        return try {
            // 3. Kotlin의 확장 함수 copyTo 사용 (내부적으로 스트림 처리되어 안전함)
            // overwrite = true는 이미 파일이 있으면 덮어쓴다는 뜻입니다.
            sourceFile.copyTo(targetFile, overwrite = true)
            targetFile
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    /**
     * 전달받은 Bitmap 앱 내부 저장소에 파일로 저장하는 함수
     * @param context 컨텍스트
     * @param uri 갤러리에서 선택된 콘텐츠의 Uri
     * @return 저장된 File 객체. 실패 시 null.
     */
    fun saveBitmapToFile(context: Context, folderName: String, bitmap: Bitmap): File? {
        val contentResolver = context.contentResolver
        // 파일 이름은 중복되지 않게 현재 시간 등으로 설정
        val fileName = "image_${System.currentTimeMillis()}.png"

        // 앱의 내부 파일 디렉토리
        val internalDir = context.filesDir
        val folder = File(internalDir, folderName)
        if (folder.exists().not()) {
            folder.mkdirs()
        }
        val file = File(folder, fileName)

        try {
            FileOutputStream(file).use { out ->
                // PNG 형식으로 압축 (품질 100)
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
            }
            return file
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }

    /**
     * 전달받은 Uri의 데이터를 앱 내부 저장소에 파일로 저장하는 함수
     * @param context 컨텍스트
     * @param uri 갤러리에서 선택된 콘텐츠의 Uri
     * @return 저장된 File 객체. 실패 시 null.
     */
    fun saveUriToFile(context: Context, folderName: String, uri: Uri): File? {
        val contentResolver = context.contentResolver
        // 파일 이름은 중복되지 않게 현재 시간 등으로 설정
        val fileName = "image_${System.currentTimeMillis()}.jpg"

        // 앱의 내부 파일 디렉토리
        val internalDir = context.filesDir
        val folder = File(internalDir, folderName)
        if (folder.exists().not()) {
            folder.mkdirs()
        }
        val file = File(folder, fileName)

        try {
            val inputStream = contentResolver.openInputStream(uri)
            val outputStream = FileOutputStream(file)
            inputStream?.use { input ->
                outputStream.use { output ->
                    input.copyTo(output)
                }
            }
            return file
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }

    fun getInternalDirPath(context: Context): String {
        return context.filesDir.absolutePath
    }

    fun saveImageFileToGallery(context: Context, sourcePath: String, folderName: String): String? {
        val sourceFile = File(sourcePath)
        if (!sourceFile.exists()) return null // 원본 파일이 없으면 종료
        val picturesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
        val folderDir = File(picturesDir, folderName)
        if (!folderDir.exists()) {
            folderDir.mkdirs()
        }

        val contentResolver = context.contentResolver
        val fileName = sourceFile.name // 원본 파일 이름 유지
        val savedFile = File(folderDir, fileName)
        if (savedFile.exists()) return null

        // 1. MediaStore 정보 설정
        val contentValues = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, fileName)
            put(MediaStore.Images.Media.MIME_TYPE, "image/png") // 파일 확장자에 맞춰 변경 가능

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                put(MediaStore.Images.Media.RELATIVE_PATH, "${Environment.DIRECTORY_PICTURES}/$folderName")
                put(MediaStore.Images.Media.IS_PENDING, 1)
            } else {
                // Android 10 미만: 직접 경로 지정 (_data)
                val picturesDir =
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                val saveDir = File(picturesDir, folderName)
                if (!saveDir.exists()) saveDir.mkdirs()

                val saveFile = File(saveDir, fileName)
                put(MediaStore.Images.Media.DATA, saveFile.absolutePath)
            }
        }

        // 2. 갤러리에 공간 확보 후 Uri 가져오기
        val uri =
            contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
        var isSuccess = false

        uri?.let { targetUri ->
            try {
                // 3. 파일 스트림 복사 (원본 -> 갤러리)
                contentResolver.openOutputStream(targetUri).use { outputStream ->
                    FileInputStream(sourceFile).use { inputStream ->
                        inputStream.copyTo(outputStream!!) // Kotlin 확장 함수 사용
                    }
                }

                // 4. 쓰기 완료 알림 (Android 10 이상)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    contentValues.clear()
                    contentValues.put(MediaStore.Images.Media.IS_PENDING, 0)
                    contentResolver.update(targetUri, contentValues, null, null)
                }

                isSuccess = true
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        // 5. Android 9 이하 버전을 위한 미디어 스캔 호출 (★핵심 추가)
        if (isSuccess && Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            MediaScannerConnection.scanFile(
                context,
                arrayOf(savedFile.absolutePath),
                arrayOf("image/png") // MIME 타입 지정
            ) { path, uri ->
                // 스캔 완료 시
            }
        }

        return savedFile.absolutePath
    }

    fun getAspectRatioFromUri(context: Context, uriString: String): Float {
        val uri = uriString.toUri()
        val contentResolver = context.contentResolver

        // 1. 이미지 크기(너비, 높이) 가져오기
        val options = BitmapFactory.Options().apply { inJustDecodeBounds = true }
        contentResolver.openInputStream(uri)?.use { inputStream ->
            BitmapFactory.decodeStream(inputStream, null, options)
        }

        // 2. EXIF 회전 정보 확인하기
        var orientation = ExifInterface.ORIENTATION_NORMAL
        contentResolver.openInputStream(uri)?.use { inputStream ->
            val exif = ExifInterface(inputStream)
            orientation = exif.getAttributeInt(
                ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_NORMAL
            )
        }

        // 3. 회전 값에 따른 가로/세로 반전 처리
        val isRotated = orientation == ExifInterface.ORIENTATION_ROTATE_90 ||
                orientation == ExifInterface.ORIENTATION_ROTATE_270

        val width = if (isRotated) options.outHeight else options.outWidth
        val height = if (isRotated) options.outWidth else options.outHeight

        return if (height > 0) width.toFloat() / height.toFloat() else 0f
    }
}

data class CreatedImageFolder(
    val folderName: String,
    val folderPath: String
)
