package com.devy.core_ui.component.image

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.ext.SdkExtensions
import android.provider.MediaStore
import android.provider.Settings
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.PermissionChecker
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.devy.core_ui.R
import com.devy.core_ui.component.BOTTOM_GRADIENT_WITH_BUTTON_HEIGHT
import com.devy.core_ui.component.DAlertDialog
import com.devy.core_ui.component.DBottomButtonContainer
import com.devy.core_ui.component.DDropDownMenu
import com.devy.core_ui.component.DGradientBox
import com.devy.core_ui.component.DLazyVerticalGrid
import com.devy.core_ui.component.DTextField
import com.devy.core_ui.component.DToolBarLayout
import com.devy.core_ui.component.DToolBarTrailingIcon
import com.devy.core_ui.component.Image
import com.devy.core_ui.component.MIN_BOTTOM_BUTTON_HEIGHT
import com.devy.core_ui.component.NavigationIcon
import com.devy.core_ui.component.local.LocalDCommonColor
import com.devy.core_ui.component.local.LocalShowSnackbar
import com.devy.core_ui.component.strokeButton
import com.devy.core_ui.model.DBottomButtonInfo
import com.devy.core_ui.model.DBottomButtonType
import com.devy.core_ui.model.DDropDownMenuItem
import com.devy.core_ui.model.image.ImageFolderType
import com.devy.core_ui.model.image.ImagePickerUiModel
import com.devy.core_ui.model.snackbar.DSnackbarData
import com.devy.core_ui.theme.CoreTheme
import com.devy.core_ui.utils.noRippleClickable
import com.devy.core_ui.utils.toFormatString
import com.devy.core_ui.viewmodel.ImageFolderViewModel
import kotlinx.coroutines.launch

@Composable
fun DImagePicker(
    selectCount: Int = Int.MAX_VALUE,
    color: Color? = null,
    onSelectImages: (List<String>) -> Unit,
    favoriteBucketIds: List<Long>? = null,
    onChangedFavoriteBucketIds: ((List<Long>) -> Unit)? = null,
    onClose: () -> Unit,
    viewModel: ImageFolderViewModel = hiltViewModel()
) {

    val providedPrimaryColor = LocalDCommonColor.current.primary
    val primaryColor = when {
        color != null -> color
        providedPrimaryColor != Color.Unspecified -> providedPrimaryColor
        else -> CoreTheme.colors.primary
    }

    val showDropDown = remember { mutableStateOf(false) }
    val selectedImages = remember { mutableStateListOf<String>() }
    var selectedFullImageIndex by remember { mutableStateOf<Int?>(null) }
    var selectedImageFolder by remember { mutableStateOf<ImageFolderType>(ImageFolderType.All) }
    val mutableFavoriteBucketIds = remember { mutableStateListOf<Long>() }
    LaunchedEffect(favoriteBucketIds) {
        mutableFavoriteBucketIds.clear()
        favoriteBucketIds?.let { bucketIds ->
            mutableFavoriteBucketIds.addAll(bucketIds.toMutableList())
        }
    }

    val context = LocalContext.current

    var showPermissionDialog by remember { mutableStateOf(false) }
    var grantedPermission by remember { mutableStateOf(false) }

    val permissionImage = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        Manifest.permission.READ_MEDIA_IMAGES
    } else {
        Manifest.permission.READ_EXTERNAL_STORAGE
    }

    val permissionWriteExternal =
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) Manifest.permission.WRITE_EXTERNAL_STORAGE else null

    // 1. 요청할 권한 리스트 준비
    val permissionsToRequest = mutableListOf<String>().apply {
        add(permissionImage)
        permissionWriteExternal?.let { permission -> add(permission) }
    }.toTypedArray()

    val isAllGranted = permissionsToRequest.all { permission ->
        PermissionChecker.checkSelfPermission(context, permission) == PermissionChecker.PERMISSION_GRANTED
    }

    // 💡 [핵심] 만약 SDK 28(Android 9) 이하인데, WRITE 권한 버그 때문에 실패한 경우 구제 로직
    grantedPermission = if (!isAllGranted && Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
        // Android 9 이하이고 전체 통과는 못 했지만, 'READ' 권한만큼은 확실히 켜져 있는지 재확인
        PermissionChecker.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) == PermissionChecker.PERMISSION_GRANTED
    } else {
        isAllGranted
    }

    val activity = (context as? Activity)
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        // 결과는 Map<String, Boolean> 형태로 반환됨
        val isImageGranted = permissions[permissionImage] ?: false
        val isExternalWrite = permissionWriteExternal?.let { permissions[it] }
            ?: (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)

        if (isImageGranted && isExternalWrite) {
            // 모든 권한 획득 성공 시 로직
            grantedPermission = true
        } else {
            grantedPermission = false
            // 하나라도 거부되었을 때의 로직
            activity?.let {
                // 거부된 권한들 중 하나라도 Rationale이 필요한지 확인
                val showRationale = permissionsToRequest.any { permission ->
                    ActivityCompat.shouldShowRequestPermissionRationale(it, permission)
                }

                if (!showRationale) {
                    // 사용자가 '다시 묻지 않음'을 눌렀을 경우
                    showPermissionDialog = true
                }
            }
        }
    }

    LaunchedEffect(grantedPermission) {
        if (grantedPermission.not()) {
            launcher.launch(permissionsToRequest)
        } else {
            viewModel.initImageFolders()
        }
    }

    val images by viewModel.images.collectAsStateWithLifecycle()

    var searchKeyword by remember { mutableStateOf("") }
    var showImageFolderListSide by remember { mutableStateOf(false) }

    val focusRequester = remember { FocusRequester() }


    val onConfirm = {
        if (selectedImages.isNotEmpty()) {
            onSelectImages(selectedImages.toList())
        }
    }

    val isSelected = selectedImages.isNotEmpty()

    val listState = rememberLazyListState()
    val gridState = rememberLazyGridState()

    val coroutineScope = rememberCoroutineScope()

    val showSnackBar = LocalShowSnackbar.current
    val maxSelectMessage = stringResource(id = R.string.image_picker_max_select, selectCount)

    val maxLimit =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && SdkExtensions.getExtensionVersion(
                Build.VERSION_CODES.R
            ) >= 2
        ) {
            MediaStore.getPickImagesMaxLimit()
        } else {
            100
        }
    val externalItemCount = selectCount.coerceIn(minimumValue = 2, maximumValue = maxLimit)
    val multiplePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia(maxItems = externalItemCount)
    ) { uris ->
        // 사용자가 사진 선택을 완료하거나 취소했을 때 결과가 이곳으로 들어옵니다.
        if (uris.isNotEmpty()) {
            onSelectImages(uris.map { it.toString() })
        }
    }

    val singlePhotoLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            onSelectImages(listOf(uri.toString()))
        }
    }

    val externalRequest = PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
    val externalLauncher = {
        if(selectCount == 1) {
            singlePhotoLauncher.launch(externalRequest)
        } else {
            multiplePhotoPickerLauncher.launch(externalRequest)
        }
    }

    DToolBarLayout(
        title = stringResource(id = R.string.title_select_image),
        navigationIcon = NavigationIcon.Close(onClose),
        visibleBottomGradient = false,
        trailingContent = {
            DDropDownMenu(
                showDropDown = showDropDown,
                items = listOf(
                    DDropDownMenuItem(
                        text = stringResource(id = R.string.external_gallery),
                        onClick = {
                            externalLauncher()
                        }
                    )
                )
            ) {
                DToolBarTrailingIcon(
                    iconResId = R.drawable.ic_more_circle_24,
                    onClick = { showDropDown.value = true }
                )
            }
        },
        bottomFloatingContent = {
            if (showImageFolderListSide) {
                DGradientBox(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp, horizontal = 24.dp)
                            .noRippleClickable { focusRequester.requestFocus() }
                            .heightIn(min = MIN_BOTTOM_BUTTON_HEIGHT.dp)
                            .strokeButton(
                                color = primaryColor,
                                isText = true,
                            )
                            .padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_search_24)
                        )
                        DTextField(
                            modifier = Modifier
                                .weight(1f)
                                .focusRequester(focusRequester),
                            content = searchKeyword,
                            singleLine = true,
                            placeholder = stringResource(id = R.string.search_folder),
                            onValueChange = {
                                searchKeyword = it
                            }
                        )
                        Image(
                            modifier = Modifier.noRippleClickable { searchKeyword = "" },
                            painter = painterResource(id = R.drawable.ic_clear_24),
                        )
                    }
                }
            } else {
                DBottomButtonContainer(
                    bottomButtonInfos = mutableListOf(
                        DBottomButtonInfo(
                            type = DBottomButtonType.IconStroke,
                            color = primaryColor,
                            icon = painterResource(
                                id = if (isSelected) R.drawable.ic_all_unchecked_24 else R.drawable.ic_all_checked_24
                            ),
                            onClick = {
                                if (isSelected) selectedImages.clear() else selectedImages.addAll(
                                    images.subList(0, selectCount.coerceAtMost(images.size))
                                )
                            }
                        ),
                        DBottomButtonInfo(
                            type = if (isSelected) DBottomButtonType.TextSolid else DBottomButtonType.TextStroke,
                            color = primaryColor,
                            text = if (isSelected) {
                                stringResource(
                                    id = R.string.count_select_format,
                                    selectedImages.size.toFormatString()
                                )
                            } else {
                                stringResource(id = R.string.close)
                            },
                            onClick = {
                                if (isSelected) {
                                    onConfirm()
                                } else {
                                    onClose()
                                }
                            }
                        ),
                    ).apply {
                        if (favoriteBucketIds != null && selectedImageFolder is ImageFolderType.Folder) {
                            val data = (selectedImageFolder as ImageFolderType.Folder).data
                            val pinned = mutableFavoriteBucketIds.contains(data.bucketId)
                            add(
                                DBottomButtonInfo(
                                    type = if (pinned) DBottomButtonType.IconSolid else DBottomButtonType.IconStroke,
                                    color = primaryColor,
                                    icon = painterResource(
                                        id = if (pinned) {
                                            R.drawable.ic_pin_solid_24
                                        } else {
                                            R.drawable.ic_pin_stroke_24
                                        }
                                    ),
                                    onClick = {
                                        if (pinned) {
                                            mutableFavoriteBucketIds.remove(data.bucketId)
                                        } else {
                                            mutableFavoriteBucketIds.add(data.bucketId)
                                        }
                                        onChangedFavoriteBucketIds?.invoke(mutableFavoriteBucketIds)
                                    }
                                )
                            )
                        }
                        add(
                            DBottomButtonInfo(
                                type = DBottomButtonType.IconStroke,
                                color = primaryColor,
                                icon = painterResource(R.drawable.ic_search_24),
                                onClick = { showImageFolderListSide = true }
                            )
                        )
                    }
                )
            }
        }
    ) { gradientHeight ->
        if (grantedPermission) {
            Column(modifier = Modifier.fillMaxSize()) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                ) {
                    DLazyVerticalGrid(
                        columns = GridCells.Fixed(3),
                        state = gridState,
                        modifier = Modifier.padding(horizontal = 0.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        contentPadding = PaddingValues(bottom = BOTTOM_GRADIENT_WITH_BUTTON_HEIGHT.dp)
                    ) {
                        itemsIndexed(images) { index, filePath ->
                            val isGif = filePath.endsWith(".gif")
                            val checked = selectedImages.contains(filePath)
                            DImagePickerItem(
                                data = ImagePickerUiModel(
                                    imgSrc = filePath,
                                    checked = checked,
                                    isGif = isGif,
                                ),
                                color = primaryColor,
                                onClickFull = {
                                    selectedFullImageIndex = index
                                },
                                onChecked = {
                                    when {
                                        checked -> {
                                            selectedImages.remove(it)
                                        }

                                        selectCount == 1 -> {
                                            selectedImages.clear()
                                            selectedImages.add(it)
                                        }

                                        selectedImages.size < selectCount -> {
                                            selectedImages.add(it)
                                        }

                                        else -> {
                                            showSnackBar(DSnackbarData(message = maxSelectMessage))
                                        }
                                    }
                                }
                            )
                        }
                    }

                    if (showImageFolderListSide) {
                        DImageFolderListSide(
                            searchKeyword = searchKeyword,
                            listState = listState,
                            favoriteBucketIds = mutableFavoriteBucketIds,
                            onClick = { item ->
                                selectedImageFolder = item
                                viewModel.selectImageFolder(item)
                                coroutineScope.launch { gridState.animateScrollToItem(0) }
                            },
                            onDismissRequest = { showImageFolderListSide = false }
                        )
                    }
                }
            }
        } else {
            RequiresPermission(
                primaryColor = primaryColor,
                onRequestPermission = { launcher.launch(permissionsToRequest) }
            )
        }
    }

    selectedFullImageIndex?.let { index ->
        DImageViewer(
            imgSrcs = images,
            initIndex = index,
            onDismiss = {
                selectedFullImageIndex = null
            }
        )
    }

    if (showPermissionDialog) {
        DAlertDialog(
            title = stringResource(id = R.string.image_permission),
            content = stringResource(id = R.string.need_image_permission_set_profile_image),
            positiveText = stringResource(id = R.string.request_permission),
            color = primaryColor,
            onPositive = {
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                    data = Uri.fromParts("package", context.packageName, null)
                }
                context.startActivity(intent)
            },
            onNegative = {},
            onDismiss = {
                showPermissionDialog = false
            }
        )
    }

    BackHandler {
        when {
            showImageFolderListSide -> {
                showImageFolderListSide = false
            }

            selectedFullImageIndex != null -> {
                selectedFullImageIndex = null
            }

            else -> {
                onClose()
            }
        }
    }
}

@Composable
private fun RequiresPermission(
    primaryColor: Color,
    onRequestPermission: () -> Unit,
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Text(
                text = stringResource(id = R.string.need_image_permission),
                style = CoreTheme.typo.bodyB,
                color = CoreTheme.colors.onSurface0,
            )

            Text(
                modifier = Modifier
                    .background(
                        color = primaryColor,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .noRippleClickable { onRequestPermission() }
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                text = stringResource(id = R.string.request_permission),
                style = CoreTheme.typo.body,
                color = CoreTheme.colors.onSurface0,
            )
        }
    }
}
