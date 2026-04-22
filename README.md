# K-Bus-System


## 🚀 Git Branch Guide

### 📌 브랜치 전략
- `main` : 배포용 (안정 버전)
- `dev` : 개발 통합 브랜치
- `feature/*` : 기능 개발 브랜치

---

### 🛠️ 프로젝트 클론 후 설정 방법

```bash
# 1. 레포지토리 클론
git clone https://github.com/K-Bus/k-bus-system.git

# 2. 프로젝트 디렉토리 이동
cd k-bus-system

# 3. 원격 브랜치 가져오기
git fetch origin

# 4. dev 브랜치 생성 및 체크아웃
git checkout -b dev origin/dev
```

```bash
# dev 브랜치 최신화

git checkout dev

git pull origin dev

# feature 브랜치 생성

git checkout -b feature/기능명

# 작업 후 커밋

git add .

git commit -m "feat: 기능 설명"

# 원격 브랜치에 push

git push origin feature/기능명
```

## 📌 Commit Convention

### 💡 커밋 작성 규칙
- 최소 작업 단위로 커밋하며, 가능한 한 **작게 쪼개어 커밋**합니다.
- **하나의 커밋에는 하나의 작업만** 포함합니다.
- 내용을 **직관적으로 이해하기 쉽게** 작성합니다.

<br>

### ✅ Commit 메시지 구조
- 커밋 메시지 제목은 `커밋 유형: 작업 내용` 형태로 작성합니다.
    - 예시: `feat: 프로젝트 초기 설정`, `fix: 로그인 오류 수정`
- 원한다면 [Gitmoji](https://inpa.tistory.com/entry/GIT-%E2%9A%A1%EF%B8%8F-Gitmoji-%EC%82%AC%EC%9A%A9%EB%B2%95-Gitmoji-cli)를 사용해도 좋습니다.

<br>

### 📚 Commit 유형 (Prefix)

| 커밋 유형      | 설명 |
|------------|------|
| `feat`     | 새로운 기능 추가 또는 기존 기능 개선 |
| `fix`      | 버그 수정 |
| `refactor` | 코드 리팩토링 (기능 변화 없이 구조 개선) |
| `docs`     | 문서 작업 (README 등) |
| `test`     | 테스트 코드 추가 또는 수정 |
| `build`    | 빌드 관련 파일 수정 (예: yml, properties 등) |
| `perform`  | 성능 개선 |
| `clean`    | 불필요한 코드 제거, 정리 |
| `design`   | UI/UX 디자인 작업 또는 개선 |
| `style`    | 코드 스타일 변경 (세미콜론, 들여쓰기 등) – 기능 변화 없음 |

<br><br>

## 📌 Pull Request Convention

### 💡 PR 작성 규칙
- 최소 작업 단위로 PR을 작성합니다.
- **하나의 PR에는 하나의 작업만** 포함되도록 합니다.
