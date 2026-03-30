# DSHelper 🤝
> 달성군 주민을 위한 무료 방문 봉사 서비스 Android 앱

## 📱 소개
DSHelper는 달성군 주민에게 무료 방문 봉사 서비스를 제공하는 Android 앱입니다.
생활 돌봄, 정서적 돌봄, 아이돌봄 등 다양한 봉사 서비스를 간편하게 신청할 수 있습니다.

## 📸 스크린샷
| 홈 화면 | 예약 화면 | 활동 화면 | 마이페이지 |
|--------|---------|---------|---------|
| ![Image](https://github.com/user-attachments/assets/dcd91431-71a2-4f05-ab1d-7533707acda6) | ![Image](https://github.com/user-attachments/assets/4200bdcb-1398-4eba-b3a6-c1c44cea1e79) | <img> | <img> |

## 🛠 기술 스택
| 구분 | 기술 |
|-----|-----|
| Language | Kotlin |
| UI | Jetpack Compose |
| Architecture | Clean Architecture, MVI |
| DI | Hilt |
| Network | Retrofit2, OkHttp3 |
| Async | Coroutines, Flow |
| Local | DataStore |
| Auth | Kakao SDK, Naver SDK, Google Credential Manager |

## 🏗 아키텍처
```
app/
├── data/
│   ├── api/          # Retrofit API 인터페이스, DTO
│   ├── local/        # DataStore
│   └── repository/   # Repository 구현체
├── domain/
│   ├── model/        # Domain 모델
│   ├── repository/   # Repository 인터페이스
│   └── usecase/      # UseCase
├── presentation/
│   ├── auth/         # 로그인
│   ├── home/         # 홈
│   ├── post/         # 활동 게시판
│   ├── help/         # 도움 요청
│   └── mypage/       # 마이페이지
├── di/               # Hilt 모듈
└── navigation/       # Navigation
```

## ✨ 주요 기능

### 소셜 로그인
- 카카오 / 네이버 / 구글 로그인 지원
- 각 SDK 공식 문서 기반 안전한 구현
- JWT 토큰 기반 인증

### 활동 게시판
- 봉사 활동 게시글 목록 / 상세 조회
- 최신순 / 인기순 정렬
- 로컬 검색 기능
- 외부 공유 기능

### 도움 요청 예약
- 커스텀 달력 (일요일만 활성화, 과거 날짜 비활성화)
- 날짜 선택 시 예약된 시간 실시간 조회
- 시작/종료 시간 범위 선택 (최대 3시간)
- 카카오 주소 검색 WebView 연동

### 마이페이지
- 프로필 조회 / 수정
- 로그아웃

## 🔑 MVI 패턴
```kotlin
// Contract 구조
data class PostUiState(...)          // 화면 상태
sealed interface PostEvent { ... }   // 사용자 의도
sealed interface PostSideEffect { ... } // 1회성 이벤트

// ViewModel 단일 진입점
fun onEvent(event: PostEvent) { ... }
```

## 🔧 개발 환경 설정

### 필수 설정
`local.properties`에 아래 키 추가:
```
KAKAO_APP_KEY=your_kakao_native_app_key
NAVER_CLIENT_ID=your_naver_client_id
NAVER_CLIENT_SECRET=your_naver_client_secret
GOOGLE_WEB_CLIENT_ID=your_google_web_client_id
BASE_URL=https://your_server_url/
```

### 요구사항
```
Android Studio Hedgehog 이상
minSdk 26
targetSdk 35
Kotlin 2.1.21
```

## 👥 팀 구성
| 역할 | 인원 |
|-----|-----|
| Android | 1명 |
| Backend | 1명 |
| Design | 1명 |

## 📄 License
```
Copyright 2026 DSHelper Team
```
```

---

### 다음 할 것
```
1. 스크린샷 찍어서 표에 넣기
2. GitHub에 복붙
3. 실제 링크로 교체
