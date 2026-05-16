# Spring Boot - JSP + MyBatis 프로젝트 세팅 전체 흐름

> 📄 노션 페이지 제목 / 파일명 추천: `Spring Boot - JSP + MyBatis 프로젝트 세팅 전체 흐름`

---

## 1. 주제

Spring Boot 프로젝트에서 JSP 화면과 MyBatis DB 연동을 처음부터 세팅하는 전체 흐름을 단계별로 이해한다.

---

## 2. 왜 배우나요?

Spring Boot는 기본 설정만으로는 JSP를 쓸 수 없다.
JSP를 쓰려면 별도 라이브러리를 추가하고, 파일 경로 규칙도 맞춰야 한다.

MyBatis는 SQL을 Java 코드와 연결해주는 도구다.
직접 SQL을 작성하고 싶을 때 사용한다.

**실생활 비유:**
Spring Boot는 "건물 뼈대"다. JSP는 "인테리어", MyBatis는 "창고 관리 시스템"이다.
건물만 있으면 빈 공간이다. 인테리어와 창고 연결까지 해야 실제로 쓸 수 있는 공간이 된다.

---

## 3. 개념 정리

### 파일/폴더별 역할 한눈에 보기

```
프로젝트 구조
├── build.gradle                          → 필요한 라이브러리 목록 (의존성 선언)
├── src/main/resources/
│   ├── application.properties            → 앱 전체 환경 설정 (DB 경로, JSP 경로 등)
│   └── db/
│       ├── schema.sql                    → 테이블 구조 만드는 SQL
│       └── data.sql                      → 초기 데이터 넣는 SQL
│   └── mapper/
│       └── board/BoardMapper.xml         → SQL 쿼리 파일 (MyBatis가 읽음)
├── src/main/java/.../board/
│   ├── Board.java                        → DB 한 행(row)을 담는 그릇 (모델)
│   ├── BoardMapper.java                  → SQL 메서드 목록 (인터페이스)
│   ├── BoardService.java                 → 비즈니스 로직 처리
│   ├── BoardController.java              → 요청 받고 응답 보내는 관문
│   └── BoardRequest.java                 → 폼에서 넘어온 데이터를 담는 그릇
└── src/main/webapp/WEB-INF/views/
    ├── index.jsp                         → 목록 화면
    └── board/
        ├── save-form.jsp                 → 글쓰기 화면
        ├── detail.jsp                    → 상세 화면
        └── update-form.jsp               → 수정 화면
```

---

### 각 개념 설명

#### build.gradle — 라이브러리 목록

Gradle(그래들)은 프로젝트에 필요한 도구들을 자동으로 다운로드해주는 빌드 도구다.
`implementation`으로 시작하는 줄 하나가 라이브러리 하나다.

| 라이브러리 | 역할 |
|---|---|
| `spring-boot-starter-web` | Spring MVC 웹 기능 전체 포함 |
| `tomcat-embed-jasper` | JSP 파일을 HTML로 변환해주는 엔진 |
| `jakarta.servlet.jsp.jstl-api` | JSP에서 쓰는 태그 라이브러리(JSTL) 인터페이스 |
| `jakarta.servlet.jsp.jstl` (구현체) | JSTL 실제 동작 코드 |
| `mybatis-spring-boot-starter` | MyBatis + Spring 연동 패키지 |
| `spring-boot-starter-data-jpa` | JPA 관련 기능 (H2 초기화에 필요) |
| `h2` | 메모리 기반 임시 DB (개발용) |

⚠️ `tomcat-embed-jasper`가 없으면 JSP 파일을 찾아도 화면이 안 나온다.

---

#### application.properties — 환경 설정 파일

앱 전체의 설정값을 `키=값` 형태로 적는 파일이다.
Spring Boot가 실행될 때 이 파일을 읽어서 동작 방식을 결정한다.

```
spring.mvc.view.prefix=/WEB-INF/views/
spring.mvc.view.suffix=.jsp
```

- 컨트롤러가 `"board/detail"` 을 반환하면 Spring이 자동으로 아래 경로를 완성한다.
- `/WEB-INF/views/` + `board/detail` + `.jsp` → `/WEB-INF/views/board/detail.jsp`
- prefix(앞에 붙이는 것) + 컨트롤러 반환값 + suffix(뒤에 붙이는 것) 조합이다.

```
spring.jpa.defer-datasource-initialization=true
```

- schema.sql → data.sql 순서로 실행되도록 보장한다.
- 이 설정이 없으면 테이블이 생기기 전에 데이터 삽입을 시도해서 오류가 난다.

---

#### schema.sql / data.sql — DB 초기화 파일

`src/main/resources/db/` 폴더 안에 위치한다.

**schema.sql**
```sql
DROP TABLE IF EXISTS board_tb;   -- 기존 테이블 있으면 삭제 (재시작 시 초기화)
CREATE TABLE board_tb (
    id        INT AUTO_INCREMENT PRIMARY KEY,  -- 자동 증가 기본키
    title     VARCHAR(100),                    -- 제목 (최대 100자)
    content   TEXT,                            -- 내용 (길이 제한 없음)
    create_at TIMESTAMP                        -- 작성시간
);
```

**data.sql**
```sql
INSERT INTO board_tb(title, content, create_at) VALUES('제목1', '내용1', now());
-- now() : 현재 시간 자동 입력
```

- `DROP TABLE IF EXISTS`는 "있으면 지워라"는 뜻이다.
- 앱이 재시작될 때마다 테이블을 새로 만들기 위해 사용한다. (H2 메모리 DB이므로 종료 시 초기화됨)

---

#### Board.java — 모델 클래스

DB 테이블의 한 행(row)을 Java 객체로 표현한 것이다.
DB 컬럼 하나 = Java 필드 하나다.

```java
@Data  // Lombok: getter, setter, toString 등을 자동 생성
public class Board {
    private Integer id;
    private String title;
    private String content;
    private Timestamp createAt;  // DB 컬럼명: create_at → Java 필드명: createAt (자동 매핑)
}
```

- `@Data`는 Lombok(롬복) 어노테이션이다. getter/setter를 직접 쓰지 않아도 자동으로 만들어준다.
- DB 컬럼은 `snake_case`(언더바), Java 필드는 `camelCase`(대문자) 관례를 따른다.
- MyBatis가 `create_at` → `createAt` 변환을 자동으로 처리한다.

---

#### BoardMapper.java — MyBatis 인터페이스

SQL 메서드의 목록을 정의하는 곳이다.
실제 SQL은 XML 파일에 있고, 여기서는 메서드 이름만 선언한다.

```java
@Mapper  // MyBatis가 이 인터페이스를 자동으로 구현체로 만들어줌
public interface BoardMapper {
    List<Board> findAll();        // 전체 목록 조회
    Board findById(int id);       // ID로 단건 조회
    void save(Board board);       // 저장 (INSERT)
    void delete(Board board);     // 삭제 (DELETE)
}
```

- `@Mapper`가 붙으면 MyBatis가 자동으로 구현 코드를 생성한다.
- 직접 구현 코드를 작성하지 않아도 된다. XML에 SQL만 적으면 된다.
- `interface`(인터페이스)는 "이런 기능이 있어야 한다"는 규칙 목록이다.

---

#### BoardMapper.xml — SQL 쿼리 파일

`src/main/resources/mapper/board/` 폴더에 위치한다.
Java 인터페이스의 메서드와 실제 SQL을 연결하는 파일이다.

```xml
<mapper namespace="com.example.프로젝트.board.BoardMapper">
    <!-- namespace = 연결할 Java 인터페이스의 전체 경로 -->

    <select id="findAll" resultType="Board">
        <!-- id = Java 인터페이스의 메서드명과 반드시 일치 -->
        <!-- resultType = 결과를 담을 Java 클래스 -->
        SELECT * FROM board_tb ORDER BY id DESC
    </select>

    <insert id="save" parameterType="Board">
        <!-- parameterType = 전달받는 Java 객체 타입 -->
        <!-- #{title} = Board 객체의 title 필드값을 꺼내서 삽입 -->
        INSERT INTO board_tb(title, content, create_at) VALUES(#{title}, #{content}, now())
    </insert>
</mapper>
```

- `#{필드명}` 문법은 MyBatis가 Java 객체에서 해당 필드 값을 꺼내서 SQL에 넣어주는 방식이다.
- SQL 인젝션(보안 공격)을 막아주는 안전한 방식이다.

⚠️ XML의 `id`값과 Java 인터페이스 메서드명이 다르면 오류가 난다. 철자까지 똑같아야 한다.

---

#### BoardController.java — 컨트롤러

사용자의 요청(URL)을 받아서 처리하고, 결과를 JSP에 전달하는 관문이다.

```java
@Controller
public class BoardController {

    @GetMapping("/boards")
    public String list(Model model) {
        List<Board> boards = boardService.findAll();
        model.addAttribute("boards", boards);  // JSP에 "boards"라는 이름으로 데이터 전달
        return "index";  // → /WEB-INF/views/index.jsp 를 찾아서 보여줌
    }
}
```

- `@GetMapping`은 GET 방식 HTTP 요청을 처리한다.
- `Model`은 컨트롤러에서 JSP로 데이터를 전달할 때 쓰는 가방이다.
- `return "board/detail"` 처럼 반환하면 `prefix + board/detail + suffix` 경로의 JSP를 찾는다.

---

#### JSP 파일 — 화면 (View)

`src/main/webapp/WEB-INF/views/` 폴더에 위치한다.
HTML에 Java 코드와 JSTL 태그를 섞어서 동적인 화면을 만든다.

```jsp
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!-- JSTL core 태그 라이브러리를 "c"라는 이름으로 사용하겠다는 선언 -->

<c:forEach var="board" items="${boards}">
    <!-- boards 목록을 하나씩 꺼내서 board 변수에 담아 반복 -->
    <tr>
        <td>${board.id}</td>
        <td>${board.title}</td>
    </tr>
</c:forEach>
```

- `${변수명}` = EL(Expression Language). 컨트롤러에서 전달한 데이터를 화면에 출력한다.
- `<c:forEach>` = JSTL 반복문. Java의 for 문과 같은 역할이다.
- JSP 파일은 `WEB-INF/views/` 안에 있어야 외부에서 직접 URL로 접근할 수 없어 보안상 안전하다.

---

#### Application.java — 메인 클래스

```java
@MapperScan("com.example.프로젝트명.board")  // 이 패키지 안의 @Mapper를 자동 등록
@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
```

- `@MapperScan`은 지정한 패키지에서 `@Mapper`가 붙은 인터페이스를 자동으로 찾아 등록한다.
- 경로가 틀리면 MyBatis가 Mapper를 찾지 못해 오류가 난다.

---

## 4. 전체 요청 처리 흐름

```
사용자가 URL 요청
    ↓
BoardController (요청 받음)
    ↓
BoardService (비즈니스 로직 처리)
    ↓
BoardMapper.java (인터페이스 메서드 호출)
    ↓
BoardMapper.xml (실제 SQL 실행)
    ↓
H2 DB (데이터 조회/저장)
    ↓
결과를 Controller로 반환
    ↓
Model에 데이터 담기
    ↓
JSP 화면에서 데이터 출력
```

---

## 5. 왜 이렇게 설정했는지 — 변경 이유 정리

| 설정/코드 | 왜 이렇게 해야 하나 |
|---|---|
| `tomcat-embed-jasper` 추가 | Spring Boot 내장 Tomcat은 기본적으로 JSP 렌더링 기능이 빠져 있다. 명시적으로 추가해야 JSP가 작동한다. |
| JSP를 `WEB-INF/views/` 안에 배치 | `WEB-INF` 폴더는 외부에서 URL로 직접 접근 불가. 반드시 컨트롤러를 통해야만 접근 가능해서 보안상 안전하다. |
| `defer-datasource-initialization=true` | schema.sql이 data.sql보다 먼저 실행되도록 순서 보장. 없으면 테이블이 없는 상태에서 INSERT가 실행되어 오류 발생. |
| XML `id` = 인터페이스 메서드명 | MyBatis가 XML의 id로 어떤 메서드인지 찾는다. 이름이 다르면 연결 자체가 안 된다. |
| `camelCase` 필드명 | Java 관례는 camelCase, DB 관례는 snake_case다. MyBatis가 두 형식을 자동으로 매핑해준다. 맞추지 않으면 null 값이 들어온다. |
| `@MapperScan` | `@Mapper`가 붙은 인터페이스를 Spring이 자동으로 찾게 하는 설정. 없으면 BoardMapper를 주입(inject)받을 수 없어 오류가 난다. |

---

## 6. 빠른 복습 포인트

> 이것만 봐도 전체 흐름이 기억난다

- **JSP를 쓰려면** `tomcat-embed-jasper` + JSTL 라이브러리 2개를 `build.gradle`에 추가한다
- **JSP 파일 위치**는 반드시 `src/main/webapp/WEB-INF/views/` 안이어야 한다
- **컨트롤러가 반환하는 문자열** = prefix + 반환값 + suffix 조합으로 JSP 경로가 완성된다
- **XML의 `id`** = Java 인터페이스 메서드명. 철자 하나라도 다르면 오류난다
- **`#{필드명}`** = MyBatis가 Java 객체에서 값을 꺼내 SQL에 대입하는 문법
- **`${변수명}`** = JSP에서 Model로 전달받은 데이터를 출력하는 문법
- **`@MapperScan("패키지경로")`** = 메인 클래스에 붙여야 Mapper가 자동 등록된다
- **`defer-datasource-initialization=true`** = schema 먼저, data 나중 실행 순서 보장

---

### 핵심 체크리스트

- [ ] `tomcat-embed-jasper` 의존성 있는지 확인
- [ ] JSP 파일이 `src/main/webapp/WEB-INF/views/` 안에 있는지 확인
- [ ] XML `id`가 Java 인터페이스 메서드명과 완전히 일치하는지 확인
- [ ] `schema.sql`이 `data.sql`보다 먼저 실행되도록 설정됐는지 확인
- [ ] `@MapperScan` 패키지 경로가 실제 패키지명과 일치하는지 확인

---

### 세팅 순서 (처음부터 다시 할 때)

```
build.gradle 의존성 추가
    → application.properties 설정 (JSP 경로 + DB 설정)
    → schema.sql 작성 (테이블 구조)
    → data.sql 작성 (초기 데이터)
    → Board.java (모델)
    → BoardMapper.java (인터페이스)
    → BoardMapper.xml (SQL 쿼리)
    → BoardService.java (비즈니스 로직)
    → BoardController.java (요청 처리)
    → JSP 파일 (화면)
    → Application.java에 @MapperScan 추가
```
