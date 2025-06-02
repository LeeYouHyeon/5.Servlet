서블릿을 통한 게시판 사이트 예시

0. 사용한 개발 언어 및 라이브러리
   1. 언어 : JDK 11
   2. DB : MySQL 8.0.41
   3. Servlet Container : Apache Tomcat 9.0
   4. 라이브러리 : https://mvnrepository.com/
      1) MySQL Connector/J 8.0.33 : 자바와 DB 연결. MySQL의 버전과 앞 두 숫자가 같은 것으로 사용
      2) SLF4J API Module 1.7.36, Apache Log4j API 1.7.36, Apache Log4j Core 2.18.0, Apache Log4j Web 2.18.0 : 로그 관련 라이브러리
      3) JSTL 1.2 : JSP에 반복, 조건, DB 접근 등을 구현하는 커스텀 태그 라이브러리. 표준 라이브러리다.
      4) MyBatis 3.5.10 : 쿼리문 작성을 도와주는 오픈소스 ORM(Object-Relation Mapping) 프레임워크
      5) JSON.simple 1.1.1 : JSON 객체와 String 사이를 변환해주는 라이브러리
      6) Apache Commons Fileupload 1.4, Apache Commons IO 2.11.0, Thumbnailator 0.4.17 : 파일 업로드 관련

1. 프로젝트 개요
   기초적인 게시판을 예시로 삼아 Servlet과 MVC 패턴을 구현하는 방법을 배우는 프로젝트
2. Servlet?
   Servlet은 클라이언트의 요청에 맞는 결과를 전송해주는 자바 프로그램으로, 이 프로젝트는 Spring, Spring Boot 등의 프레임워크 없이 Servlet Container를 직접 조작한다.
