# devy-core-android

개인 Android 앱들이 공유하는 공통 라이브러리입니다. 네트워크 호출, 화면 상태, 디자인 시스템,
로컬 저장소처럼 앱마다 반복되던 기반 코드를 한 곳에 모아 두고, 여러 앱이 같은 코드를 쓰도록
Gradle Composite Build 로 연결합니다.

| 항목 | 내용 |
|---|---|
| 언어 / UI | Kotlin 2.0.21, Jetpack Compose (Material3) |
| 모듈 | 6개 (`global`, `network`, `core:core-ui`, `core:app`, `foundation:datastore`, `foundation:dynamodb`) |
| 최소 / 타깃 SDK | minSdk 27 / compileSdk·targetSdk 36 |
| DI | Hilt 2.51.1 |

## 모듈 구조

```
devy-core-android
├── global/               — 로거, 앱 버전·알림·기기 정보 유틸
├── network/              — Retrofit2 / OkHttp / Moshi 설정, ApiResponse, DataLoader
├── core/
│   ├── app/              — 앱 수준 공통 상태 (DevyAppState)
│   └── core-ui/          — 디자인 시스템과 공통 Compose 컴포넌트
└── foundation/
    ├── datastore/        — 암호화 DataStore 래퍼
    └── dynamodb/         — AWS DynamoDB 클라이언트 (Cognito 인증)
```

모듈 간 의존은 `network`·`dynamodb` → `global`, `core:app` → `core:core-ui` 두 갈래뿐입니다.
나머지 모듈은 서로 독립이라 필요한 것만 골라 쓸 수 있습니다.

### global
- `DLog` — Timber 기반 로거. `"메시지".dLog()` 처럼 확장함수로 쓰며 파일명:라인:메서드명 태그가 자동으로 붙습니다
- `AppVersionManager`, `NotificationHelper`, `DeviceUtils`, `DateExtension`

### network
- `ApiResponse<T>` — `Success` / `Failure.HttpError` / `NetworkError` / `Exception` / `EmptyResponse` 로 응답을 분류하는 sealed class
- `ResponseCallAdapter` — Retrofit 이 예외를 던지지 않고 `ApiResponse` 를 반환하게 하는 커스텀 CallAdapter.
  JSON 이 아닌 오류 본문도 `HttpError` 로 분류합니다 (`ResponseCallErrorBodyTest` 로 검증)
- `DataLoader` — 호출 / 성공 / 오류 / 네트워크 오류 콜백과 진행 상태(`showProgress`)를 ViewModel 에 위임
- `Retrofit2Module` — OkHttp·Moshi·Retrofit 을 제공하는 Hilt 모듈 (디버그 빌드에서만 HTTP 로깅)

### core:core-ui
- `UiState<T>` — `Idle | Loading | Empty | Progress | Success | Error`
- 디자인 시스템 — `CoreTheme` (색상, 타이포그래피)
- 레이아웃 / 입력 / 목록 / 바텀시트 / 이미지 / 다이얼로그 등 `D` 접두사 공통 컴포넌트
  (`DScaffold`, `DTextField`, `DLazyColumn`, `DBottomSheet`, `DAlertDialog`, `DAsyncImage` 등)
- 화면 전환 애니메이션 (`animatedComposable`, `AnimatedTransition`) 과 Navigation 인터페이스

### foundation
- `datastore` — DataStore Preferences 암호화 래퍼와 Hilt 모듈
- `dynamodb` — Cognito 자격 증명으로 DynamoDB 에 접근하는 `DynamoDataSource` 와 Hilt 모듈

## 사용 예

### 앱에 포함하기 (Composite Build)

이 저장소를 앱 저장소와 같은 부모 디렉터리에 `DevyCore` 라는 이름으로 클론하고
(`git clone <url> DevyCore`), 앱의 `settings.gradle` 에서 maven 좌표를
로컬 모듈로 치환합니다.

```gradle
includeBuild("../DevyCore") {
    dependencySubstitution {
        substitute(module("com.devy.core:global")).using(project(":global"))
        substitute(module("com.devy.core:network")).using(project(":network"))
        substitute(module("com.devy.core:core:app")).using(project(":core:app"))
        substitute(module("com.devy.core:core:core-ui")).using(project(":core:core-ui"))
        substitute(module("com.devy.core:foundation:datastore")).using(project(":foundation:datastore"))
        substitute(module("com.devy.core:foundation:dynamodb")).using(project(":foundation:dynamodb"))
    }
}
```

앱 모듈에서는 일반 의존성처럼 선언합니다.

```gradle
dependencies {
    implementation "com.devy.core:network"
    implementation "com.devy.core:core:core-ui"
}
```

### API 호출과 화면 상태

`DataLoader` 로 호출하고, 결과를 `UiState` 로 노출하는 ViewModel 예시입니다.

```kotlin
interface UserApi {
    @GET("users/{id}")
    suspend fun getUser(@Path("id") id: Long): ApiResponse<UserDto>
}

@HiltViewModel
class UserViewModel @Inject constructor(
    private val api: UserApi,
) : ViewModel(), DataLoader by DataLoaderImpl() {

    private val _state = MutableStateFlow<UiState<UserDto>>(UiState.Idle)
    val state = _state.asStateFlow()

    fun load(id: Long) = viewModelScope.loadData(
        call = { api.getUser(id) },
        onSuccess = { _state.value = UiState.Success(it) },
        onError = { _state.value = UiState.Error(it.message) },
        onNetworkError = { _state.value = UiState.Error("네트워크 연결을 확인해 주세요", isNetworkError = true) },
    )
}
```

### 로깅

```kotlin
import com.devy.global.DLog.dLog

"로드 시작".dLog()
```

## 요구 환경

| 항목 | 버전 |
|---|---|
| JDK | 17 |
| Android SDK | Platform 36 |
| Gradle | 8.13 (wrapper 포함) |
| Android Gradle Plugin | 8.3.1 |

## 빌드

```bash
git clone https://github.com/DevyMobile/devy-core-android.git
cd devy-core-android
echo "sdk.dir=/path/to/Android/sdk" > local.properties   # 또는 ANDROID_HOME 환경변수

./gradlew assembleDebug
./gradlew :network:testDebugUnitTest
```

### AWS 설정 (`:foundation:dynamodb` 를 쓸 때만)

`keys.gradle.example` 을 `keys.gradle` 로 복사하고 Cognito Identity Pool ID 를 채웁니다.
`keys.gradle` 은 git 에 포함되지 않습니다. 파일이 없으면 빈 값으로 빌드되고 경고만 출력합니다
(DynamoDB 를 쓰지 않는 모듈은 영향이 없습니다).

## 테스트

`network` 모듈에 MockWebServer 기반 단위 테스트가 있습니다 (`ResponseCallErrorBodyTest`).
UI 컴포넌트의 테스트는 아직 없습니다.

## 알려 두는 점

- 로딩 표시(`DProgress`, `DTopLoading`)는 원래 Lottie 애니메이션을 썼지만, 공개본에서는 출처를 확인할 수 없는 애니메이션 파일을 빼고 Material3 `CircularProgressIndicator` 로 대체했습니다. 컴포저블의 API 는 그대로입니다.

## 라이선스

[MIT](LICENSE)
