# 🌱 Seedzip-Backend

<div align="center">
    <img width="800" alt="main" src="https://github.com/user-attachments/assets/c7b3a8ca-e6ef-4c0e-a353-b171aeb522ac" />
    <br><br>
    <a href="https://hits.seeyoufarm.com">
        <img src="https://hits.seeyoufarm.com/api/count/incr/badge.svg?url=https%3A%2F%2Fgithub.com%2FAdoongE%2FBE&count_bg=%2379C83D&title_bg=%23555555&icon=&icon_color=%23E7E7E7&title=hits&edge_flat=false">
    </a>
</div>

## 🛠 About Project
<div align="center" style="display: flex; flex-wrap: wrap; justify-content: center; gap: 10px;">
    <img src="https://github.com/user-attachments/assets/967a67ce-fde2-4746-9a96-20349498149f" width="400">
    <img src="https://github.com/user-attachments/assets/133de42f-42c9-4931-811a-7d40ec2f35f7" width="400">
    <br>
    <img src="https://github.com/user-attachments/assets/3ae351a7-d727-472c-b6e2-67ce384fff04" width="400">
    <img src="https://github.com/user-attachments/assets/c60a12d6-04f4-4800-9aee-9abb931e1b40" width="400">
    <br>
    <img src="https://github.com/user-attachments/assets/99243e29-c704-4d08-872e-0fd5edfd2ffa" width="400">
    <img src="https://github.com/user-attachments/assets/1ae577ef-7da0-4478-b8b9-954ce94b122d" width="400">
</div>

## 🛠 System Architecture
<div align="center">
    <img width="1000" alt="architecture" src="https://github.com/user-attachments/assets/16619ccf-f1c5-4b6e-bce7-41af13225cb4" />
</div>

## 🛠 Stacks

![Spring](https://img.shields.io/badge/Spring-6DB33F.svg?&style=for-the-badge&logo=Spring&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-6DB33F.svg?&style=for-the-badge&logo=Spring%20Boot&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-4479A1.svg?&style=for-the-badge&logo=MySQL&logoColor=white)
<img src="https://img.shields.io/badge/Amazon S3-569A31?style=for-the-badge&logo=Amazon S3&logoColor=white">

![Git](https://img.shields.io/badge/Git-F05032.svg?&style=for-the-badge&logo=Git&logoColor=white)
<img src="https://img.shields.io/badge/github-%23121011.svg?style=for-the-badge&logo=github&logoColor=white">
![IntelliJ IDEA](https://img.shields.io/badge/IntelliJ%20IDEA-000000.svg?&style=for-the-badge&logo=IntelliJ%20IDEA&logoColor=white)

## 📠 Convention

### 🤝 Branch Naming Convention

| 머릿말  | 설명                               |
| ------- | ---------------------------------- |
| main    | 서비스 브랜치                      |
| develop | 배포 전 작업 기준                  |
| feat | 기능 단위 구현                     |
| hotfix  | 서비스 중 긴급 수정 건에 대한 처리 |

<details>
<summary>Branch Naming Convention Detail</summary>
<div markdown="1">

```
master(main) ── develop ── feature
└── hotfix
```

- [ ] [깃 플로우](https://techblog.woowahan.com/2553/)를 베이스로 하여 프로젝트 사이즈에 맞게 재정의했습니다.
- [ ] 브랜치 이름은 `kebab-case`를 따릅니다.
- [ ] 이슈 번호는 가장 마지막에 적습니다. (ex. #_)

#### master(main)

- [ ] 실제 서비스가 이루어지는 브랜치입니다.
- [ ] 이 브랜치를 기준으로 develop 브랜치가 분기됩니다.
- [ ] 배포 중, 긴급하게 수정할 건이 생길시 hotfix 브랜치를 만들어 수정합니다.

#### develop

- [ ] 개발, 테스트, 릴리즈 등 배포 전 작업의 기준이 되는 브랜치입니다.
- [ ] 해당 브랜치를 default로 설정합니다.
- [ ] 이 브랜치에서 feature 브랜치가 분기됩니다.

#### feature

- [ ] 개별 개발자가 맡은 작업을 개발하는 브랜치입니다.
- [ ] feat/(feat-name) 과 같이 머릿말을 feat, 꼬릿말을 개발하는 기능으로 명명합니다.
- [ ] feat-name의 경우 kebab-case를 따릅니다.
- [ ] ex) feat/social-login-#5

#### hotfix

- [ ] 서비스 중 긴급히 수정해야 할 사항이 발생할 때 사용합니다.
- [ ] main에서 분기됩니다.

</div>
</details>

### 🤝 Commit Convention

| 머릿말           | 설명                                                                      |
| ---------------- | ------------------------------------------------------------------------- |
| Feat             | 새로운 기능 추가                                                          |
| Fix              | 버그 수정                                                                 |
| Refactor         | 코드 리팩토링                                                  |
| Style         | 코드 formatting, 세미콜론 누락, 코드 자체의 변경이 없는 경우                                                  |
| Comment          | 필요한 주석 추가 및 변경                                                  |
| Docs             | 문서 수정                                                                 |
| Test             | 테스트 코드, 리팩토링 테스트 코드 추가                        |
| Chore            | 패키지 매니저 수정, 그 외 기타 수정 ex) .gitignore |
| Rename           | 파일 혹은 폴더명을 수정하거나 옮기는 작업만인 경우                        |
| Remove           | 파일을 삭제하는 작업만 수행한 경우                                        |
| !BREAKING CHANGE | 커다란 API 변경의 경우                                                    |
| !HOTFIX          | 코드 포맷 변경, 세미 콜론 누락, 코드 수정이 없는 경우                     |

<details>
<summary>Commit Convention Detail</summary>
<div markdown="1">

### 1. 제목과 본문을 빈행으로 분리

- 커밋 유형 이후 제목과 본문은 한글로 작성하여 내용이 잘 전달될 수 있도록 할 것
- 본문에는 변경한 내용과 이유 설명 (어떻게보다는 무엇 & 왜를 설명)

### 2. 제목 첫 글자는 대문자로, 끝에는 `.` 금지

### 3. 제목은 영문 기준 50자 이내로 할 것

### 4. 마지막에 이슈번호 추가하기

### 5. 자신의 코드가 직관적으로 바로 파악할 수 있다고 생각하지 말자

### 6. 여러가지 항목이 있다면 글머리 기호를 통해 가독성 높이기

```
- 변경 내용 1
- 변경 내용 2
- 변경 내용 3
```

### 8. 예시
커밋유형: 기능 설명 (#이슈번호)
ex) Feat: 로그인 기능 구현 (#5)

</div>
</details>

## 👨🏻‍💻👩🏻‍💻 Developers
<table align="center">
    <tr>
      <td align="center"><a href="https://github.com/chaen-ing"/><img src=https://avatars.githubusercontent.com/u/96906242?v=4 width=300/>
      <td align="center"><a href="https://github.com/gyuseon25"/><img src=https://avatars.githubusercontent.com/u/118058218?v=4 width=300/>
      <td align="center"><a href="https://github.com/plum-king"/><img src=https://avatars.githubusercontent.com/u/77599304?v=4 width=300/>
    </tr>
    <tr>
      <td align="center"><a href="https://github.com/chaen-ing"/>김채은</td>
      <td align="center"><a href="https://github.com/gyuseon25"/>심규선</td>
      <td align="center"><a href="https://github.com/plum-king"/>황지수</td>
  </tr>
</table>
